package com.atguigu.gmall.activemq.bootactivemq;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2019-5-20
 */

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.stereotype.Component;


@Configuration
@Component
public class ReceiveActiveMQConfig {
    @Value("${queueNamePerf}")
    private String queueNamePerf;

    @Value("${topicNamePerf}")
    private String topicNamePerf;

    @Value("${spring.activemq.user}")
    private String usrName;

    @Value("${spring.activemq.password}")
    private  String password;

    @Value("${spring.activemq.broker-url}")
    private  String brokerUrl;

    @Bean
    public ActiveMQQueue queue(){
        return new ActiveMQQueue(queueNamePerf);
    }

    @Bean
    public ActiveMQTopic topic(){

        return new ActiveMQTopic(topicNamePerf);
    }

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        return new ActiveMQConnectionFactory(usrName, password, brokerUrl);
    }

    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerQueue(ActiveMQConnectionFactory connectionFactory){
        DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
        bean.setConnectionFactory(connectionFactory);
        return bean;
    }

    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerTopic(ActiveMQConnectionFactory connectionFactory){
        DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
        //设置为发布订阅方式, 默认情况下使用的生产消费者方式
        bean.setPubSubDomain(true);
        bean.setConnectionFactory(connectionFactory);
        bean.setSessionTransacted(true);
        bean.setAutoStartup(true);
        //开启持久化订阅
    //    bean.setSubscriptionDurable(true);
        return bean;
    }
}