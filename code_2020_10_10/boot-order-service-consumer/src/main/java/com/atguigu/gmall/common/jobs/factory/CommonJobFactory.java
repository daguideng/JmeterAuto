package com.atguigu.gmall.common.jobs.factory;

import java.lang.reflect.Method;

import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.scheduling.quartz.DelegatingJob;
import org.springframework.util.ReflectionUtils;

public class CommonJobFactory implements JobFactory {

    @Override
    public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler)
            throws SchedulerException {
        try {
            final Object jobObject = this.createJobInstance(bundle);
            scheduler.getListenerManager().addJobListener(new MarsJobListener());
            return this.adaptJob(jobObject);
        } catch (final Exception ex) {
            throw new SchedulerException("initialize schedule job failed: ", ex);
        }
    }

    protected Object createJobInstance(TriggerFiredBundle bundle)
            throws Exception {
        // Reflectively adapting to differences between Quartz 1.x and Quartz 2.0...
        final Method getJobDetail = bundle.getClass().getMethod("getJobDetail");
        final Object jobDetail = ReflectionUtils.invokeMethod(getJobDetail,
            bundle);
        final Method getJobClass = jobDetail.getClass()
                .getMethod("getJobClass");
        @SuppressWarnings("rawtypes")
        final Class jobClass = (Class) ReflectionUtils.invokeMethod(
            getJobClass, jobDetail);
        return jobClass.newInstance();
    }

    protected Job adaptJob(Object jobObject) throws Exception {
        if (jobObject instanceof Job) {
            return (Job) jobObject;
        } else if (jobObject instanceof Runnable) {
            return new DelegatingJob((Runnable) jobObject);
        } else {
            throw new IllegalArgumentException(
                "Unable to execute job class ["
                        + jobObject.getClass().getName()
                        + "]: only [org.quartz.Job] and [java.lang.Runnable] supported.");
        }
    }
}
