package com.atguigu.gmall;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.PropertySource;


//@
//start
//@ImportResource(locations={"classpath:consumer.xml"})

//targer
@EnableDubbo(scanBasePackages = "com.atguigu.gmall")
@EnableHystrix
@SpringBootApplication(scanBasePackages = "com.atguigu.gmall")


@MapperScan("com.atguigu.gmall.dao")
//@PropertySource(value="classpath:templates/properties/quartz.properties",encoding = "utf-8")
//@ImportResource(locations={"classpath:spring/spring-timeTask.xml"})
//@PropertySource(value="classpath:quartz.properties",encoding = "utf-8")
//@ImportResource(locations={"classpath:logback-spring.xml"})
//@PropertySource(value="classpath:/application.properties",encoding = "utf-8")
//@PropertySource(value="classpath:/application.properties",encoding = "utf-8")
//@PropertySource(value="classpath:/templates/properties/config.properties",encoding = "utf-8")
//@PropertySource(value="classpath:/templates/properties/interfaceconfig.properties",encoding = "utf-8")

//@EnableWebSocketMessageBroker
public class BootOrderServiceConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootOrderServiceConsumerApplication.class, args);
    }

}
