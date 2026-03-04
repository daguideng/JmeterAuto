package com.atguigu.gmall.Impl.perfImpl;

import com.atguigu.gmall.Interface.CommandJmeterIntel;
import com.atguigu.gmall.common.utils.FileReplayUtils;
import com.atguigu.gmall.common.utils.OSUtils;
import com.atguigu.gmall.service.jmeterperf.Jmeter_perf_run_ipServer;
import com.atguigu.gmall.zookeeperip.DynamicHTTPHostProvider;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2018-7-26
 */
@Slf4j
@Component
public class CommandPerfJmeterImpl implements CommandJmeterIntel {


    @Autowired
    Jmeter_perf_run_ipServer jmeter_perf_run_ipServer;

    @Autowired
    private DynamicHTTPHostProvider dynamicHTTPHostProvider;



    @Value("${Jjmeter.reportgenerator.overall_granularity}")
    private String Jjmeter_reportgenerator_overall_granularity;



    public StringBuffer commandJmeter(String jmeterControlNode, String jmeterBinDirPath, String jmxAbsolutePath, String jtlAbsolutePath, String logAbsolutePath, String reportAbsolutePath, String ifRecordLogJtl, String ifRecordLogJmeter) throws Exception {

        StringBuffer nodeIpBuffer = new StringBuffer(300);

        String agentInfoStr = dynamicHTTPHostProvider.getRandomIP("Perf").toString();

        log.info("jmeterControlNode:{}," + jmeterControlNode);
        // 遍历待执行目录构建jmx静默运行命令,默认运行方式：
        String loadRunType = null;
        String runType = null;
        //从数据库查询出来负载机状态不为0时，可指定负载机运行:
        if (0 == jmeter_perf_run_ipServer.selectByAllJmeter_perf_run_ip().get(0).getSpecifiedState()) {
            loadRunType = " -R " + agentInfoStr;
            log.info("loadRunType====>" + "运行-R级别");
        } else {
            //指定ip或端口运行：
            if (jmeter_perf_run_ipServer.selectByAllJmeter_perf_run_ip().get(0).getJmeterLoadRun().contains(":")) {
                //如果指定端口则运行-r，否则运行-R
                String runAgentIp = jmeter_perf_run_ipServer.selectByAllJmeter_perf_run_ip().get(0).getJmeterLoadRun();
                loadRunType = " -R " + runAgentIp;
                log.info("loadRunType====>" + "运行-R级别");
                System.out.println("loadRunType====>" + loadRunType);
            } else {
                runType = "-R " + jmeter_perf_run_ipServer.selectByAllJmeter_perf_run_ip().get(0).getJmeterLoadRun();
                loadRunType = " " + runType + " ";
                log.info("loadRunType====>" + "运行-R级别");
            }
        }

        //清空

        String command = null;

        //报告更细粒度：    
        String reportgenerator_overall_granularity = " -Jjmeter.reportgenerator.overall_granularity="+Integer.valueOf(Jjmeter_reportgenerator_overall_granularity) ;


        //不管ifRecordLogJtl的value值,必然生成jtl日志,只对 log日志有判断:
        if (ifRecordLogJtl.equalsIgnoreCase("Yes") && ifRecordLogJmeter.equalsIgnoreCase("Yes")) {
            command = jmeterBinDirPath +reportgenerator_overall_granularity+ " -n -t " + jmxAbsolutePath + loadRunType + " -r -l " + jtlAbsolutePath
                    + " -j " + logAbsolutePath + " -e -o " + reportAbsolutePath;
        } else if (ifRecordLogJtl.equalsIgnoreCase("Yes") && !ifRecordLogJmeter.equalsIgnoreCase("Yes")) {
            command = jmeterBinDirPath +reportgenerator_overall_granularity+ " -n -t " + jmxAbsolutePath + loadRunType + " -r -l " + jtlAbsolutePath

                    + " " + "" + " -e -o " + reportAbsolutePath;
        } else if (!ifRecordLogJtl.equalsIgnoreCase("Yes") && ifRecordLogJmeter.equalsIgnoreCase("Yes")) {
            command = jmeterBinDirPath +reportgenerator_overall_granularity+ " -n -t " + jmxAbsolutePath + loadRunType + " -r -l " + jtlAbsolutePath

                    + " -j " + logAbsolutePath + " -e -o " + reportAbsolutePath;
        } else if (!ifRecordLogJtl.equalsIgnoreCase("Yes") && !ifRecordLogJmeter.equalsIgnoreCase("Yes")) {
            command = jmeterBinDirPath +reportgenerator_overall_granularity+ " -n -t " + jmxAbsolutePath + loadRunType + " -r -l " + jtlAbsolutePath

                    + " " + "" + " -e -o " + reportAbsolutePath;
        }


        System.out.println("command=" + command);

        if (OSUtils.isWindows()) {
            command = "cmd /c " + command;
            log.info("待执行的JMX命令：{}," + command);
        }


        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
            String line = null;
            while ((line = input.readLine()) != null) {
                System.out.println(line);
                log.info("执行结果：{}" + line);
                //得到远程节点：
                if (line.contains("remote engine:")) {
                    String nodeIp = line.split("remote engine:")[1].trim();
                    nodeIpBuffer.append(nodeIp);
                    nodeIpBuffer.append(",");
                    System.out.println();
                }

            }
            System.gc();
            int exitValue = process.waitFor();
            if (exitValue == 0) {
                log.info("成功执行命令!!");
                System.out.println();
                process.getOutputStream().close();
            }
        } catch (IOException e) {

            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println();
            throw new Exception("执行命令失败!!!");
            //   e.printStackTrace();
        }


        return nodeIpBuffer;

    }

    public void modiyPropertiesIpPortPerf(String jmeterControlNode, String agentIpPort) {

        //修改:PropertiesIpPort(remote_hosts)
        log.info("remote_hosts：");               //server.rmi.port
        FileReplayUtils.modiyJmeterPropertiesIpPort(jmeterControlNode, "remote_hosts",
                "remote_hosts=" + agentIpPort);
        log.info("remote_hosts：" + agentIpPort + "修改完毕");
    }


}
