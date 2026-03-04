package com.atguigu.gmall.activemq;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2018-9-5
 */
import javax.jms.Topic;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class ResultDataMessageProducer {

    private JmsTemplate template;

    private Topic destination;

    public void setTemplate(JmsTemplate template) {
        this.template = template;
    }

    public void setDestination(Topic destination) {
        this.destination = destination;
    }

    public void send(String message) {
        this.template.convertAndSend(this.destination, message);
    }

}