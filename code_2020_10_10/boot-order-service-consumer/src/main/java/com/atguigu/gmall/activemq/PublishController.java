package com.atguigu.gmall.activemq;

import javax.jms.Queue;
import javax.jms.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2019-5-20
 */
//@RestController
//@RequestMapping("/publish")
@Component
public class PublishController {

    public PublishController(){}


    @Autowired
    private JmsMessagingTemplate jms;

    @Autowired
    private Queue queue;

    @Autowired
    private Topic topic;

 //   @RequestMapping("/queue")
    public String sendQueue(String msg){
         jms.convertAndSend(queue,msg);
        return "queue 发送成功";
    }

    @JmsListener(destination = "out.queue")
    public void consumerMsg(String msg){
        System.out.println(msg);
    }

  //  @RequestMapping("/topic")
    public String sendTopic(String msg){
        jms.convertAndSend(topic,msg);
        return "topic 发送成功";
    }
}