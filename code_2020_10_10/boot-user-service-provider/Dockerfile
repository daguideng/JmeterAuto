# 使用多阶段构建
FROM eclipse-temurin:17-jre-jammy AS builder

# 设置构建参数和环境变量
ARG DEBIAN_FRONTEND=noninteractive
ENV TZ=Asia/Shanghai

# 一次性执行所有 apt 操作
RUN echo 'Acquire::Retries "3";' > /etc/apt/apt.conf.d/80-retries && \
    echo 'Acquire::http::Timeout "120";' > /etc/apt/apt.conf.d/99-timeout

RUN sed -i 's|http://.*ubuntu.com|http://mirrors.aliyun.com|g' /etc/apt/sources.list && \
    apt-get update -qq --fix-missing && \
    apt-get install -y --no-install-recommends ntpdate lsof curl && \
    ln -snf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo Asia/Shanghai > /etc/timezone && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

# 最终镜像
FROM eclipse-temurin:17-jre-jammy

# 设置环境变量
ENV TZ=Asia/Shanghai
ENV JAVA_OPTS="-server -XX:+UseG1GC -XX:MaxGCPauseMillis=150 -Djava.awt.headless=true -Dspring.jmx.enabled=false -Duser.timezone=Asia/Shanghai"
ENV JMETER_OPTS="--add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED"
ENV LANG=C.UTF-8
ENV SPRING_CONFIG_LOCATION=file:/app/config/

# 设置时区
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

WORKDIR /app

# 从builder复制已安装的依赖
COPY --from=builder /usr/sbin/ntpdate /usr/bin/
COPY --from=builder /usr/bin/lsof /usr/bin/
COPY --from=builder /usr/bin/curl /usr/bin/

# 复制应用文件
COPY --chown=root:root jdk8u412-b08 /app/jdk8u412-b08
COPY --chown=root:root apache-jmeter-5.6.3 /usr/local/jmeter/agent/inter/bootServiceProvide/apache-jmeter-5.6.3
COPY --chown=root:root target/jmeter-agent-0.0.1.jar /app/agent-0.0.1.jar

# 创建必要的目录（包括配置目录）
RUN mkdir -p /usr/local/jmeter/agent/inter/agentDownStore && \
    mkdir -p /usr/local/jmeter/agent/inter/bootServiceProvide/logs && \
    mkdir -p /app/logs && \
    mkdir -p /app/config && \
    chmod -R 755 /usr/local/jmeter/agent/inter/bootServiceProvide/apache-jmeter-5.6.3/bin && \
    chmod -R 755 /app/jdk8u412-b08/bin && \
    chmod 755 /app/config

# 复制默认配置文件到配置目录
COPY --chown=root:root src/main/resources/application.properties /app/config/application.properties.default
COPY --chown=root:root src/main/resources/application.yml /app/config/application.yml.default

# 设置环境变量
ENV JAVA_HOME=/opt/java/openjdk \
    JAVA8_HOME=/app/jdk8u412-b08 \
    JMETER_HOME=/usr/local/jmeter/agent/inter/bootServiceProvide/apache-jmeter-5.6.3 \
    PATH=/opt/java/openjdk/bin:$JAVA8_HOME/bin:$JMETER_HOME/bin:$PATH

# 验证安装
RUN echo "=== Java 17 (JMeter使用) ===" && \
    $JAVA_HOME/bin/java -version && \
    echo "=== Java 8 (应用启动使用) ===" && \
    $JAVA8_HOME/bin/java -version && \
    echo "=== Tools验证 ===" && \
    which lsof && which curl && which ntpdate && \
    echo "=== 配置目录 ===" && \
    ls -la /app/config/

# 暴露端口
EXPOSE 8089 21882

# 健康检查
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8089/actuator/health || exit 1

# 启动脚本：检查配置文件是否存在，如果不存在则使用默认配置
CMD ["sh", "-c", " \
if [ ! -f /app/config/application.properties ]; then \
    echo 'Using default application.properties'; \
    cp /app/config/application.properties.default /app/config/application.properties; \
fi; \
if [ ! -f /app/config/application.yml ]; then \
    echo 'Using default application.yml'; \
    cp /app/config/application.yml.default /app/config/application.yml; \
fi; \
echo 'Starting application with config from: /app/config/'; \
/app/jdk8u412-b08/bin/java $JAVA_OPTS -Dspring.config.location=file:/app/config/ -jar /app/agent-0.0.1.jar \
"]