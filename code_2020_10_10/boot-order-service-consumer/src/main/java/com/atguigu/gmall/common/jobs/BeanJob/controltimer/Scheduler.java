package com.atguigu.gmall.common.jobs.BeanJob.controltimer;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2019-6-27
 */

import com.atguigu.gmall.common.jobs.BeanJob.QuartzController;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class Scheduler{


    private static final Logger logger = Logger.getLogger(Scheduler.class);

    @Autowired
    private QuartzController quartzController ;


    @Scheduled(cron = "0 */15 * * * ?")
    public void testTasks() {


        try {
          //  logger.info("开始定制定时器......");
         //   HttpRequest.sendGet(url,"");
            //先清空任务，再执行新任务:
            quartzController.deleteScheduleJob2();
            quartzController.scheduleJob2();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}