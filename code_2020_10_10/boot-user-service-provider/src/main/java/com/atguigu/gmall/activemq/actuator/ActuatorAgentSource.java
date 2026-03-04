package com.atguigu.gmall.activemq.actuator;

import com.atguigu.gmall.common.utils.HttpClientUtil;
import com.atguigu.gmall.common.utils.OSUtils;
import com.atguigu.gmall.entity.Jmeter_perf_agent_source;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2019-5-31
 */

@Component
public class ActuatorAgentSource {


     @Value("${server.port}")
     private  String server_port ;



     private String ACTUATOR_URL = "http://127.0.0.1:" ;


    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 1.获取操作系统名：
     * /actuator/env  【os.name】
     * @param map
     */

    @RequestMapping(value="/env",method= RequestMethod.GET)
    public void  getOsName(Map <String, String> map){

        String osNameUrl = ACTUATOR_URL +server_port+ "/actuator/env" ;
        String osname = HttpClientUtil.doGet(osNameUrl).split("os.name")[1].split("\"value\":")[1].split("}")[0].replace("\"","");


        String hostname = null ;
        if (OSUtils.isWindows()) {
             hostname = HttpClientUtil.doGet(osNameUrl).split("COMPUTERNAME")[1].split("\"value\":")[1].split(",")[0].replace("\"", "");
            ;
        }else if (OSUtils.isLinux()) {
            hostname = HttpClientUtil.doGet(osNameUrl).split("HOSTNAME")[1].split("\"value\":")[1].split(",")[0].replace("\"", "");
            ;
        }else{
            hostname = "no hostname";
        }

        String username = HttpClientUtil.doGet(osNameUrl).split("user.name")[1].split("\"value\":")[1].split("}")[0].replace("\"","");;

        String ipaddress = HttpClientUtil.doGet(osNameUrl).split("spring.cloud.client.ip-address")[1].split("\"value\":")[1].split("}")[0].replace("\"","");;


        map.put("osname",osname);
        map.put("hostname",hostname);
        map.put("username",username);
        map.put("ipaddress",ipaddress);



    }



    /**
     * 2.内存(mem,mem.free,processors)  gc.ps_scavenge.count[gc次数]  gc.ps_scavenge.time[gc时间] ：
     * /actuator/metrics  【mem】【mem.free】【processors】
     * @param map
     */
    @RequestMapping(value="/metrics",method= RequestMethod.GET)
    public void  getCpuSource(Map <String, String> map){

        String cpuCoutUrl = ACTUATOR_URL +server_port+ "/actuator/metrics/system.cpu.count" ;
        String cpuCount = HttpClientUtil.doGet(cpuCoutUrl).split("measurements")[1].split("\"value\":")[1].split("}")[0].replace("\"","");;

        map.put("cpucount",cpuCount);

        String system_cpu_usage_url = ACTUATOR_URL +server_port+ "/actuator/metrics/system.cpu.usage" ;

        String system_cpu_usage = HttpClientUtil.doGet(system_cpu_usage_url).split("measurements")[1].split("\"value\":")[1].split("}")[0].replace("\"","");;

 //       Double system_cpu_usage_d  = Double.valueOf(system_cpu_usage);

        map.put("systemcpuusage",system_cpu_usage);


    }


    /**
     * 3.diskspace diskspace (total,free)：
     * /actuator/health
     * @param map
     */
    @RequestMapping(value="/health",method= RequestMethod.GET)
    public void  getDiskHealth(Map <String, String> map){

        String metricsUrl_health = ACTUATOR_URL +server_port+ "/actuator/health" ;


        String diskSpace_total = HttpClientUtil.doGet(metricsUrl_health).split("\"total\":")[1].split(",")[0];

        String diskSpace_free = HttpClientUtil.doGet(metricsUrl_health).split("\"free\":")[1].split(",")[0];

        map.put("diskSpace_total",diskSpace_total).replace("\"","");;
        map.put("diskSpace_free",diskSpace_free).replace("\"","");;


    }


    public Jmeter_perf_agent_source getSourceMap(){

        Map<String,String> map = new HashMap<>(300) ;

        //1.常用agent资源数据:
        this.getOsName(map);
        this.getCpuSource(map);
     // this.getDiskHealth(map);
        //2.资源入库：
        Jmeter_perf_agent_source  agentSource = new Jmeter_perf_agent_source();

        agentSource.setIpaddress(map.get("ipaddress"));
        agentSource.setUsername(map.get("username"));
        agentSource.setOsname(map.get("osname"));
        agentSource.setHostname(map.get("hostname"));
        agentSource.setCpucount(map.get("cpucount"));
        agentSource.setSystemcpuusage(map.get("systemcpuusage"));

        agentSource.setCpufree(OSUtils.getCpuFre());
        agentSource.setMemory(OSUtils.getMemory());
        agentSource.setDiskram(OSUtils.getDiskRam());
        agentSource.setJavaversion(OSUtils.getJavaVersion());


        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date();
       // String d = sdf.format(date);
        agentSource.setTime(sdf.format(date));

        map.clear();

        return agentSource ;
    }





}




