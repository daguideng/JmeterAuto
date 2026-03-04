package com.atguigu.gmall.common.jobs.BeanJob;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2019-6-25
 */

import com.atguigu.gmall.Impl.perfImpl.ReportStates;
import com.atguigu.gmall.service.jmeterinter.JmeterIntercurrentreportServer;
import com.atguigu.gmall.service.jmeterperf.StatisticsServer;
import org.jboss.logging.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 自定义定时任务
 */
public class ScheduledJob implements Job {

    @Autowired
    private StatisticsServer statisticsServer ;
    private JmeterIntercurrentreportServer jmeterIntercurrentreportServer ;


    private static final Logger logger= Logger.getLogger(ScheduledJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        //执行任务逻辑....
        logger.info("执行自定义定时任务");

        //数据已发送报告状态：1---->数据隐藏状态 2 :
        statisticsServer.UpdateStatus_1_2(ReportStates.SENDREPORT_1);
        logger.info("性能测试当前报告数据已发送报告状态：1---->数据隐藏状态 2 .......");

     //   jmeterIntercurrentreportServer.UpdateStatus_1_2(ReportStates.SENDREPORT_1);
     //   logger.info("接口测试当前报告数据已发送报告状态：1---->数据隐藏状态 2 .......");


    }
}