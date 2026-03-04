package com.atguigu.gmall.activemq.topic;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;

@Service
public class KillProcessByPort {
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
}
