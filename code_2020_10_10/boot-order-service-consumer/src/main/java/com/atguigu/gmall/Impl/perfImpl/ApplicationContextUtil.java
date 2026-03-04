package com.atguigu.gmall.Impl.perfImpl;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2018-6-29
 */
@Service
public class ApplicationContextUtil implements ApplicationContextAware {

    private ApplicationContext context;//声明一个静态变量保存
    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.context=applicationContext;
    }

    public  ApplicationContext getContext(){
        return context;
    }
}