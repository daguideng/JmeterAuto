package com.atguigu.gmall.common.jobs;

/**
 * @Author: dengdagui
 * @Description: 只生成当天报告，第二天则清空操作：以后会定状态决定
 * @Date: Created in 2018-9-14
 */

import com.atguigu.gmall.Impl.perfImpl.ReportStates;
import com.atguigu.gmall.service.jmeterinter.JmeterIntercurrentreportServer;
import com.atguigu.gmall.service.jmeterperf.StatisticsServer;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class OnDayReportJob implements Job {

    @Autowired
    private StatisticsServer statisticsServer ;
    private JmeterIntercurrentreportServer jmeterIntercurrentreportServer ;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {



        //数据已发送报告状态：1---->数据隐藏状态 2 :
        statisticsServer.UpdateStatus_1_2(ReportStates.SENDREPORT_1);
        logger.info("性能测试当前报告数据已发送报告状态：1---->数据隐藏状态 2 .......");

        jmeterIntercurrentreportServer.UpdateStatus_1_2(ReportStates.SENDREPORT_1);
        logger.info("接口测试当前报告数据已发送报告状态：1---->数据隐藏状态 2 .......");

    }

    public OnDayReportJob(){}
}
