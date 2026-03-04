# 设置Java环境
export JAVA_HOME=/opt/jdk1.8/jdk1.8.0_131
export PATH=$JAVA_HOME/bin:$PATH

# 检查Java
java -version > /dev/null 2>&1 || { echo "Error: Java not available"; exit 1; }
echo "Java version: $(java -version 2>&1 | head -1)"

# 变量定义
JAR_NAME="jmeter-agent-0.0.1.jar"
CONFIG_FILE="application.properties"

# 文件检查
[ ! -f "$JAR_NAME" ] && { echo "Error: $JAR_NAME not found"; exit 1; }
[ ! -f "$CONFIG_FILE" ] && { echo "Error: $CONFIG_FILE not found"; exit 1; }

# 获取端口
SERVER_PORT=$(grep '^server\.port' "$CONFIG_FILE" | awk -F'=' '{print $2}' | tr -d '\r' | tr -d ' ')
[ -z "$SERVER_PORT" ] && { echo "Error: server.port not found"; exit 1; }
echo "Server port: $SERVER_PORT"

# 停止现有进程
echo "Stopping existing processes..."
pkill -f "$JAR_NAME" >/dev/null 2>&1
lsof -ti:"$SERVER_PORT" | xargs kill -9 >/dev/null 2>&1
sleep 2
pkill -9 -f "$JAR_NAME" >/dev/null 2>&1

# 清理旧的nohup.out（可选）
#[ -f "nohup.out" ] && mv "nohup.out" "nohup.out.old_$(date +%Y%m%d_%H%M%S)"

# 启动应用
echo "Starting application with nohup..."
nohup java -jar "$JAR_NAME" > nohup.out 2>&1 &
APP_PID=$!
echo "Process started with PID: $APP_PID"

# 使用Actuator健康检查等待启动
MAX_WAIT=60
WAIT_INTERVAL=3
ELAPSED=0
HEALTH_URL="http://localhost:${SERVER_PORT}/actuator/health"

echo "Waiting for application to become healthy (max ${MAX_WAIT}s)..."

while [ $ELAPSED -lt $MAX_WAIT ]; do
    # 检查健康端点
    if curl -s --max-time 2 "$HEALTH_URL" | grep -q '"status":"UP"'; then
        # 双重验证：同时检查启动成功日志
        if tail -n 20 nohup.out | grep -q "Started .* in .* seconds"; then
            ACTUAL_PID=$(lsof -ti:"$SERVER_PORT" | head -1)
            echo "✅ Success! Application is healthy and running."
            echo "📋 PID: ${ACTUAL_PID:-$APP_PID}"
            echo "🌐 Health: $HEALTH_URL"
            echo "📝 View logs: tail -f nohup.out"
            exit 0
        fi
    fi

    sleep $WAIT_INTERVAL
    ELAPSED=$((ELAPSED + WAIT_INTERVAL))
    echo "Waited ${ELAPSED}s for health check..."
done

# 等待超时后的处理
echo "❌ Application failed to become healthy within ${MAX_WAIT}s"
echo "=== Last 50 lines of log ==="
tail -50 nohup.out
echo "==========================="

# 检查进程是否还在运行
if ps -p $APP_PID > /dev/null 2>&1; then
    echo "⚠️  Process $APP_PID is still running. Leaving it for manual inspection."
    echo "💡 You can check: tail -f nohup.out"
    exit 0
else
    echo "💥 Process $APP_PID has died. Check logs above for errors."
    exit 1
fi
