package com.atguigu.gmall.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.util.List;


@Service
public class KillJMeterProcess {


    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public   void killJmeter() {
        try {
            // 指定JMeter进程的名称，这里假设为"jmeter"
            String processName = "Djava.rmi.server.hostname";

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
