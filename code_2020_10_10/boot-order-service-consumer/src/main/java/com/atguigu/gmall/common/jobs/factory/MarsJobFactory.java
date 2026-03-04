package com.atguigu.gmall.common.jobs.factory;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

public class MarsJobFactory extends CommonJobFactory {
    @Autowired
    private AutowireCapableBeanFactory capableBeanFactory;

    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle)
            throws Exception {
        final Object jobInstance = super.createJobInstance(bundle);
        this.capableBeanFactory.autowireBean(jobInstance);
        return jobInstance;
    }
}
