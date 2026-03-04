package com.atguigu.gmall.common.jobs.BeanJob;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall.Impl.perfImpl.JmeterSession;
import com.atguigu.gmall.Impl.perfImpl.PerfConfigStatus;
import com.atguigu.gmall.activemq.PublishController;
import com.atguigu.gmall.activemq.ThreadByRunnableSendMq;
import com.atguigu.gmall.common.utils.HttpRequest;
import com.atguigu.gmall.dao.Timer_type_configMapper;
import com.atguigu.gmall.entity.Interface_config;
import com.atguigu.gmall.entity.Perf_config;
import com.atguigu.gmall.entity.Timer_type_config;
import com.atguigu.gmall.service.jmeterinter.InterConfigServer;
import com.atguigu.gmall.service.jmeterperf.PerfConfigServer;
import com.atguigu.gmall.zookeeperip.DyIPaddressPubicProvider;
import org.jboss.logging.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2019-6-25
 */
@RestController
@Component
public class JmeterPerfScheduledJob implements Job {

    private static final Logger logger = Logger.getLogger(JmeterPerfScheduledJob.class);

    @Autowired
    private Timer_type_configMapper timer_type_configMapper;

    @Autowired
    private PerfConfigServer perfConfigServer;

    @Autowired
    private DyIPaddressPubicProvider dyIPaddressPubicProvider;

    @Autowired
    private PublishController publishController;

    @Autowired
    private InterConfigServer interConfigServer;


    @Value("${serverUrl.Inter}")
    private String serverUrl_Inter ;


    @Value("${serverUrl.Perf}")
    private String serverUrl_Perf;



    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        //jmeter性能测试自定义job
        logger.info("执行自定义定时任务");


        System.out.println("jobExecutionContext--->" + jobExecutionContext);

        String jobid = jobExecutionContext.toString().split("job:")[1].split("fireTime")[0].trim();


        int id = Integer.valueOf(jobid.split("#")[1].split("#")[0]);


        //1.是否执行性能测试job
        Timer_type_config recond = timer_type_configMapper.selectByPrimaryKey(id);
        //2.是YES才执行perf的job

        this.doJobTask(recond);





    }


    public void doJobTask(Timer_type_config recond){


        if (recond.getTimestatus().equalsIgnoreCase("Yes") && recond.getDeletestate()==1) {


            String uuidRandomStr = UUID.randomUUID().toString().substring(0, 10);

            //专为定时器
            JmeterSession.SESSION_TIMER_End = uuidRandomStr;

            if (recond.getType().equalsIgnoreCase("Perf")) {

                Perf_config perf_config = new Perf_config();
                perf_config.setThreads(recond.getThreads());
                perf_config.setRuntime(recond.getRuntime());
                perf_config.setDelaytime(recond.getDelaytime());
                perf_config.setIfretry(recond.getIfretry());
                perf_config.setIfoutinterval(recond.getIfoutinterval());
                perf_config.setIfcustomlistener(recond.getIfcustomlistener());
                perf_config.setIfrecordlogjtl(recond.getIfrecordlogjtl());
                perf_config.setIfrecordlogjmeter(recond.getIfrecordlogjmeter());
                perf_config.setIfbetweenvalue(recond.getIfbetweenvalue());
                perf_config.setThreadname(uuidRandomStr);
                perf_config.setStatus(PerfConfigStatus.start);

                //配置参数入库：
                perfConfigServer.insertPerfconfig(perf_config);


                Map<String,String> perfConfigmap = new HashMap<>();

                perfConfigmap.put("ids",recond.getIds());
                perfConfigmap.put("vuser",recond.getThreads());
                perfConfigmap.put("runtime",recond.getRuntime());
                perfConfigmap.put("agents","0");
                perfConfigmap.put("sleeptime","0");
                perfConfigmap.put("delaytime",recond.getDelaytime());
                perfConfigmap.put("retry",recond.getIfretry());
                perfConfigmap.put("output",recond.getIfoutinterval());
                perfConfigmap.put("outputInterval",recond.getIfoutinterval());
                perfConfigmap.put("customListener",recond.getIfcustomlistener());
                perfConfigmap.put("recordlogjtl",recond.getIfrecordlogjtl());
                perfConfigmap.put("recordlogjmeter",recond.getIfrecordlogjmeter());
                perfConfigmap.put("betweenvalue",recond.getIfbetweenvalue());


                String perfConfig =JSONObject.toJSONString(perfConfigmap);


             //  String jst = "{\"ids\":\"4\",\"perfConfig\":\"{\"ids\":4,\"vuser\":\"1,2,3\",\"runtime\":1,\"agents\":\"1,2,3\",\"sleeptime\":0,\"delaytime\":0,\"retry\":false,\"retryVal\":1,\"output\":false,\"outputInterval\":30,\"customListener\":false,\"jtlListener\":true,\"logListener\":false,\"betweenValue\":false}\"}";


                //发消息给客户端:
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("config", "configPerf");
                jsonObj.put("consumerIp", dyIPaddressPubicProvider.getConsumerOsIpaddress());
                jsonObj.toJSONString();
                ThreadByRunnableSendMq thread = new ThreadByRunnableSendMq(publishController, jsonObj);
                new Thread(thread, "发送：客户端基本参数初始化 configPerf......").start();


                //3.调用运行接口：
                String url = serverUrl_Perf + "jmeterperf/run";
                String ids = recond.getIds();

                try {
                    HttpRequest.sendPost(url, "ids=" + ids +"&"+"perfConfig="+perfConfig);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (recond.getType().equalsIgnoreCase("Inter")) {

                Interface_config interface_config = new Interface_config();
                interface_config.setThreads(recond.getThreads());
                interface_config.setRuntime(recond.getRuntime());
                interface_config.setDelaytime(recond.getDelaytime());
                interface_config.setIfretry(recond.getIfretry());
                interface_config.setIfoutinterval(recond.getIfoutinterval());
                interface_config.setIfcustomlistener(recond.getIfcustomlistener());
                interface_config.setIfrecordlogjtl(recond.getIfrecordlogjtl());
                interface_config.setIfrecordlogjmeter(recond.getIfrecordlogjmeter());
                interface_config.setIfbetweenvalue(recond.getIfbetweenvalue());
                interface_config.setThreadname(uuidRandomStr);
                interface_config.setStatus(PerfConfigStatus.start);

                //1.配置参数入库：
                interConfigServer.insertInterconfig(interface_config);



                Map<String,String> perfConfigmap = new HashMap<>();
                perfConfigmap.put("ids",recond.getIds());
                perfConfigmap.put("vuser",recond.getThreads());
                perfConfigmap.put("runtime",recond.getRuntime());
                perfConfigmap.put("agents","0");
                perfConfigmap.put("sleeptime","0");
                perfConfigmap.put("delaytime",recond.getDelaytime());
                perfConfigmap.put("retry",recond.getIfretry());
                perfConfigmap.put("output",recond.getIfoutinterval());
                perfConfigmap.put("outputInterval",recond.getIfoutinterval());
                perfConfigmap.put("customListener",recond.getIfcustomlistener());
                perfConfigmap.put("recordlogjtl",recond.getIfrecordlogjtl());
                perfConfigmap.put("recordlogjmeter",recond.getIfrecordlogjmeter());
                perfConfigmap.put("betweenvalue",recond.getIfbetweenvalue());


                String interConfig =JSONObject.toJSONString(perfConfigmap);


                //2.发消息给客户端:
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("config", "configInter");
                jsonObj.put("consumerIp", dyIPaddressPubicProvider.getConsumerOsIpaddress());
                jsonObj.toJSONString();
                ThreadByRunnableSendMq thread = new ThreadByRunnableSendMq(publishController, jsonObj);
                new Thread(thread, "发送：客户端基本参数初始化 configInter......").start();


                //3.调用运行接口：
                String url = serverUrl_Inter + "jmeterinter/run";
                String ids = recond.getIds();

                try {

                    HttpRequest.sendPost(url, "ids=" + ids+"&"+"interfaceConfig="+interConfig);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }


        }

    }


}
