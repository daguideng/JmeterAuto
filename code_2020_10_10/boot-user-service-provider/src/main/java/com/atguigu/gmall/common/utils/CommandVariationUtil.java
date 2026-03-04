package com.atguigu.gmall.common.utils;

import com.atguigu.gmall.activemq.actuator.MenuAgentStatus;
import com.atguigu.gmall.activemq.topic.BootReportDataRequestReceive;
import com.atguigu.gmall.service.impl.AgentIpRunStatus;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2018-8-17
 */
@Component
@Slf4j
public class CommandVariationUtil {



    @Autowired
    private AgentIpRunStatus agentIpRunStatus;


    public StringBuffer commandExecution(String command, String agentIpPort) throws CustomRunException {


        // 遍历待执行目录构建jmx静默运行命令,默认运行方式：

        StringBuffer sb = new StringBuffer();

        System.out.println("command=" + command);

        String hostIP = agentIpPort;

        System.out.println("hostIP-->" + hostIP);


        if (OSUtils.isWindows()) {
            command = "cmd /c " + command;
        } else {
            //  command = command ;
        }

        log.info("待执行的JMX命令：{}," + command);

        if(null == BootReportDataRequestReceive.runusername ){
            BootReportDataRequestReceive.runusername = "admin";
        }


        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
            String line = null;
            while ((line = input.readLine()) != null) {
                if (line.contains("Created")) {
                    agentIpRunStatus.updateDataStatusAgentInfo(hostIP, MenuAgentStatus.STATUS_CREATED, null);
                    log.info("Created======>"+line);
                    System.out.println("Created======>"+line);
                } else if (line.contains("Starting")) {
                    agentIpRunStatus.updateDataStatusAgentInfo(hostIP, MenuAgentStatus.STATUS_RUN, BootReportDataRequestReceive.runusername);
                    log.info("Starting======>"+line);
                    System.out.println("Starting======>"+line);
                } else if (line.contains("Finished")) {
                    agentIpRunStatus.updateDataStatusAgentInfo(hostIP, MenuAgentStatus.STATUS_FINISHED, null);
                    log.info("Finished======>"+line);
                    System.out.println("Finished======>"+line);
                } else if (line.contains("(mmap) failed") || (line.contains("memory")) || (line.contains("detected"))
                || (line.contains("error")) || (line.contains("Problematic")) || (line.contains("unlimited"))
                        || (line.contains("(line.contains(\"unlimited\"))"))
                        || (line.contains("dump")) || (line.contains("Memory")) || (line.contains("physical"))
                        || (line.contains("swap")) || (line.contains("vm_info")) || (line.contains("HotSpot")) ) {
                    agentIpRunStatus.updateDataStatusAgentInfo(hostIP, MenuAgentStatus.STATUS_MMAP_FAILED, null);
                    log.info("mmap======>"+line);
                    System.out.println("mmap======>"+line);
                }
                System.out.println("命令---->" + line);
                log.info("line:{}" + line);
                sb.append(line);

            }

            int exitValue = process.waitFor();
            if (exitValue == 0) {
                System.out.println("成功执行命令!!");
                process.getOutputStream().close();
            }

            System.gc();
        } catch (IOException e) {

            e.printStackTrace();
        } catch (InterruptedException e) {
            //  throw new CustomRunException("执行命令失败!!!");
            e.printStackTrace();
        }


        return sb;
    }

}
