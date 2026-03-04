package com.atguigu.gmall.common.jobs.factory;

import com.atguigu.gmall.common.interceptor.MDCHolder;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class MarsJobListener implements JobListener {

    private static final Logger logger = LoggerFactory.getLogger(MarsJobListener.class);

    @Override
    public String getName() {
        return "quartz_job_context_trace";
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        String traceId = UUID.randomUUID().toString();
        MDCHolder.putTraceId(traceId);

        String des = context.getJobDetail().getDescription();

        if(StringUtils.isBlank(des)){
            des = context.getJobInstance().getClass().getSimpleName();
        }

        logger.info("==========quartz job start," + des);
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        MDCHolder.clear();
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {

        String des = context.getJobDetail().getDescription();

        if(StringUtils.isBlank(des)){
            des = context.getJobInstance().getClass().getSimpleName();
        }

        logger.info("==========quartz job end," + des);
        MDCHolder.clear();
    }

}
