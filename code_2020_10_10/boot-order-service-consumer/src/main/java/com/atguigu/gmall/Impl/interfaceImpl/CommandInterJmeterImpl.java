package com.atguigu.gmall.Impl.interfaceImpl;

import com.atguigu.gmall.Interface.CommandJmeterIntel;
import com.atguigu.gmall.common.utils.FileReplayUtils;
import com.atguigu.gmall.common.utils.OSUtils;
import com.atguigu.gmall.service.jmeterperf.Jmeter_perf_run_ipServer;
import com.atguigu.gmall.zookeeperip.DynamicHTTPHostProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2018-7-26
 */
@Slf4j
@Component
public class CommandInterJmeterImpl implements CommandJmeterIntel {


    @Autowired
    private DynamicHTTPHostProvider dynamicHTTPHostProvider;

    @Autowired
    private Jmeter_perf_run_ipServer jmeter_perf_run_ipServer;


    //@Value("${jmeterAgent.IP.Inter}")
    //private String jmeterAgent_IP_Inters;

    @Value("${Jjmeter.reportgenerator.overall_granularity}")
    private String Jjmeter_reportgenerator_overall_granularity;



    //@Value("${jmeterProperties.Inters}")
    //private String jmeterProperties_Inters;

    

    public StringBuffer commandJmeter(String jmeterControlNode, String jmeterBinDirPath, String jmxAbsolutePath, String jtlAbsolutePath, String logAbsolutePath, String reportAbsolutePath, String ifRecordLogJtl, String ifRecordLogJmeter) throws Exception {

        StringBuffer nodeIpBuffer = new StringBuffer(50);
        String agentInfo = dynamicHTTPHostProvider.getRandomIP("Inter").toString();
        log.info("agentInfo--->" + agentInfo);

        // 遍历待执行目录构建jmx静默运行命令,默认运行方式：
        String loadRunType = null;
        String runType = null;
        //从数据库查询出来负载机状态不为0时，可指定负载机运行:
        if (0 == jmeter_perf_run_ipServer.selectByAllJmeter_perf_run_ip().get(0).getSpecifiedState()) {
            loadRunType = " -R "+agentInfo;
            log.info("loadRunType====>"+loadRunType);

        } else {
            //指定ip或端口运行：
            if(jmeter_perf_run_ipServer.selectByAllJmeter_perf_run_ip().get(0).getJmeterLoadRun().contains(":")){
                //如果指定端口则运行-r，否则运行-R
                String runAgentIp = jmeter_perf_run_ipServer.selectByAllJmeter_perf_run_ip().get(0).getJmeterLoadRun();
                loadRunType = " -R "+runAgentIp;
                log.info("loadRunType====>"+loadRunType);

            }else {
                runType = "-R " + jmeter_perf_run_ipServer.selectByAllJmeter_perf_run_ip().get(0).getJmeterLoadRun();
                loadRunType = " " + runType + " ";
                log.info("loadRunType====>" + "运行-R级别");
                log.info("loadRunType====>"+loadRunType);
            }
        }


        String command = null;
        //报告更细粒度：    
        String reportgenerator_overall_granularity = " -Jjmeter.reportgenerator.overall_granularity="+Integer.valueOf(Jjmeter_reportgenerator_overall_granularity) ;

     //   String reportgenerator_overall_granularity = " " ;
     //   String Jlog_level_jmeter=" -Jlog_level.jmeter=DEBUG ";
        String Jlog_level_jmeter=" ";

        if (ifRecordLogJtl.equalsIgnoreCase("Yes") && ifRecordLogJmeter.equalsIgnoreCase("Yes")) {
            command = jmeterBinDirPath +reportgenerator_overall_granularity+ " -n -t " + jmxAbsolutePath + loadRunType + " -r -l " + jtlAbsolutePath + Jlog_level_jmeter 
            +" -j " + logAbsolutePath +  
            " -e -o " + reportAbsolutePath;
        } else if (ifRecordLogJtl.equalsIgnoreCase("Yes") && !ifRecordLogJmeter.equalsIgnoreCase("Yes")) {
            command = jmeterBinDirPath +reportgenerator_overall_granularity+ " -n -t " + jmxAbsolutePath + loadRunType + " -r -l " + jtlAbsolutePath + Jlog_level_jmeter
                    + " " + "" + " -e -o " + reportAbsolutePath;
        } else if (!ifRecordLogJtl.equalsIgnoreCase("Yes") && ifRecordLogJmeter.equalsIgnoreCase("Yes")) {
            command = jmeterBinDirPath +reportgenerator_overall_granularity+ " -n -t " + jmxAbsolutePath + loadRunType + " -r -l " + jtlAbsolutePath  + Jlog_level_jmeter
                    + " -j " + logAbsolutePath + " -e -o " + reportAbsolutePath;
        } else if (!ifRecordLogJtl.equalsIgnoreCase("Yes") && !ifRecordLogJmeter.equalsIgnoreCase("Yes")) {
            command = jmeterBinDirPath +reportgenerator_overall_granularity+ " -n -t " + jmxAbsolutePath + loadRunType + " -r -l " + jtlAbsolutePath + Jlog_level_jmeter

                    + " " + "" + " -e -o " + reportAbsolutePath.toString();

        }



        log.info("command=" + command);

        if (OSUtils.isWindows()) {
            command = "cmd /c " + command;
            log.info("待执行的JMX命令：{}," + command);
            System.out.println("待执行的JMX命令：{}," + command);
        }


        log.info("command==============>" + command);

        try {
            String line = null;
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));

            while ((line = input.readLine()) != null) {
                System.out.println("执行命令结果"+line);
                log.info("执行命令结果：{}" + line);
                //得到远程节点：
                if (line.contains("remote engine:")) {
                    String nodeIp = line.split("remote engine:")[1].toString().trim();
                    nodeIpBuffer.append(nodeIp);
                    nodeIpBuffer.append(",");
                }
            }
            System.gc();
            int exitValue = process.waitFor();
            if (exitValue == 0) {
                log.info("成功执行命令!!");
                process.getOutputStream().close();
            }
        } catch (IOException e) {

            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new Exception("执行命令失败!!!");
            //   e.printStackTrace();
        }


        return nodeIpBuffer;

    }


    /**
     * 获取控制台节点与代理节点（IP）对应关系，方便不同的对控制节点获取不同的代理节点IP:
     */

     /*** 
    public String retrunAgentIP(String jmeterControlNode) throws IOException {


        System.out.println("jmeterControlNode---->" + jmeterControlNode);


        String[] jmeterAgent_IP_Inter = jmeterAgent_IP_Inters.split(",");

        String[] jmeterProperties_Inter = jmeterProperties_Inters.split(",");


        List <String> jmeterAagent = new ArrayList <String>(Arrays.asList(jmeterAgent_IP_Inter));
        List <String> jmeterProperties = new ArrayList <String>(Arrays.asList(jmeterProperties_Inter));

        Map <Object, Object> map = new HashMap <>();

        for (int i = 0; i < jmeterProperties.size(); i++) {
            map.put(jmeterProperties.get(i), jmeterAagent.get(i));

            System.out.println("jmeterProperties.get(i)---->" + jmeterProperties.get(i));
            System.out.println("jmeterAagent.get(i)---->" + jmeterAagent.get(i));
        }


        String agentIp = (String) map.get(jmeterControlNode);

        return agentIp;

    }

    ***/



    public void modiyPropertiesIpPortInter(String jmeterControlNode, String agentIpPort) {

        //修改:PropertiesIpPort(remote_hosts)
        log.info("remote_hosts：");               //server.rmi.port
        FileReplayUtils.modiyJmeterPropertiesIpPort(jmeterControlNode, "remote_hosts",
                "remote_hosts=" + agentIpPort);
        log.info("remote_hosts：" + agentIpPort + "修改完毕");
    }


}
