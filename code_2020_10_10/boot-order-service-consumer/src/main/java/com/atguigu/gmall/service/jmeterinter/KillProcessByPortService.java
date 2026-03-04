package com.atguigu.gmall.service.jmeterinter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.List;

@Service
public class KillProcessByPortService {

    /**
     * 第一种方式：指定端口号kill进程
     */

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void killProcessPort(Integer portToKill) {
        //  int portToKill = 8080; // 你要杀死的端口号

        try {
            // 检查端口是否被占用
            if (isPortInUse(portToKill)) {
                // 杀死占用指定端口的进程
                killProcessByPort(portToKill);
                System.out.println("已杀死端口 " + portToKill + " 上的进程");
            } else {
                System.out.println("端口 " + portToKill + " 未被占用");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isPortInUse(int port) {
        try (ServerSocket ignored = new ServerSocket(port, 0, InetAddress.getByName("localhost"))) {
            return false;
        } catch (IOException e) {
            return true;
        }
    }

    private static void killProcessByPort(int port) throws IOException {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            // Windows系统执行的命令
            String command = "cmd /c netstat -ano | find \"" + port + "\" | find \"LISTENING\"";
            Process process = Runtime.getRuntime().exec(command);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] tokens = line.trim().split("\\s+");
                    String pid = tokens[tokens.length - 1];
                    // 杀死进程
                    Runtime.getRuntime().exec("taskkill /F /PID " + pid);
                }
            }
        } else {
            // 非Windows系统执行的命令
            Runtime.getRuntime().exec("lsof -i :" + port + " | awk 'NR!=1 {print $2}' | xargs kill -9");
        }
    }


    /**
     * 第二种方式：kill进程
     */
    public  void killJmeter(String processName) {
        try {
            // 指定JMeter进程的名称，这里假设为"jmeter"
           // String processName = "Djava.rmi.server.hostname";

            // 构建杀死进程的命令
            String command = "pkill -f " + processName;

            // 执行命令
            Process process = Runtime.getRuntime().exec(command);

            // 获取命令执行的输出信息
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // 等待命令执行完成
            process.waitFor();

            // 打印命令执行的退出值
            System.err.println("Exit Value: " + process.exitValue());
            logger.info("Exit Value: " + process.exitValue());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }




    /**
     * 查询jmeter进程
     */

    public Boolean queryJmetetProcess() {
        List<String> inputArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();

        for (String arg : inputArguments) {
            if (arg.contains("Djava.rmi.server.hostname")) {
                System.out.println("找到 JMeter 进程：" + arg);
                logger.info("找到 JMeter 进程：" + arg);
                return Boolean.TRUE;
            }else {
                logger.info("未找到 JMeter 进程：" + arg);
                return Boolean.FALSE;
            }
        }

        return  Boolean.FALSE;
    }



}