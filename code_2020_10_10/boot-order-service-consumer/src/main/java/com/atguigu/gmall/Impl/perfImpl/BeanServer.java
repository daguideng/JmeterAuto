package com.atguigu.gmall.Impl.perfImpl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2018-6-26
 */
public class BeanServer {


    public ApplicationContext getApplicationContext(String benaname){

        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath*:/templates/spring/spring-mvc.mxl");
      //  ApplicationContext ac = new ClassPathXmlApplicationContext(new String[]{"applicationContext.xml","dao.xml"});


        return ac ;
    }





}
