package com.atguigu.gmall.common.jobs.BeanJob;

import com.atguigu.gmall.activemq.PublishController;
import com.atguigu.gmall.dao.Timer_type_configMapper;
import com.atguigu.gmall.service.jmeterinter.InterConfigServer;
import com.atguigu.gmall.service.jmeterperf.PerfConfigServer;
import com.atguigu.gmall.zookeeperip.DyIPaddressPubicProvider;
import java.util.Map;
import java.util.TreeMap;
import org.jboss.logging.Logger;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2019-6-25
 */
//@RestController
//@RequestMapping("/quartz")
@Component
public class QuartzController {

    @Autowired
    public SchedulerManager myScheduler;

    @Autowired
    private JmeterPerfInterJob jmeterPerfInterJob;


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


    private static final Logger logger = Logger.getLogger(QuartzController.class);

    // @RequestMapping(value = "/job", method = RequestMethod.GET)
    public String scheduleJob2() {


        Map <Integer, String> treemap = new TreeMap <>();


        jmeterPerfInterJob.scheduleJob2((TreeMap <Integer, String>) treemap);


        /**
        String timetask= "0 8 13 * * ?";
        int key = 1 ;

        try {
            myScheduler.startJob(timetask, "#" + String.valueOf(key) + "#", String.valueOf(key), JmeterPerfScheduledJob.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        **/



        for (Map.Entry <Integer, String> entry : treemap.entrySet()) {
            int key = entry.getKey();
            String timetask = entry.getValue();
            treemap.put(key, timetask);
            try {

                //再执行新任务:
                myScheduler.startJob(timetask, "#" + String.valueOf(key) + "#", String.valueOf(key), JmeterPerfScheduledJob.class);
                logger.info("表id为:{}, timetask{}" + key + "," + timetask);
                System.out.println("表id为:{}, timetask{}" + key + "," + timetask);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }




        treemap.clear();

        return "sucess";

    }



  //  @RequestMapping(value = "/del_job2", method = RequestMethod.GET)
    public String deleteScheduleJob2() {
        try {
          //  myScheduler.deleteJob("#", "group2");
         //   myScheduler.deleteJob(jobName, groupName);
            myScheduler.clearAll();
          //  System.out.println("删除定时器成功");
            return "删除定时器成功";
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return "删除定时器失败";
    }


}