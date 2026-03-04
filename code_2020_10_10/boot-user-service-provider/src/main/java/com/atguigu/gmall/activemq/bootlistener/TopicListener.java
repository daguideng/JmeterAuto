package com.atguigu.gmall.activemq.bootlistener;

import com.atguigu.gmall.activemq.topic.BootReportDataRequestReceive;
import com.atguigu.gmall.activemq.topic.BootThreadReceiverScriptUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2019-5-27
 */
@Component
public class TopicListener {



    @Autowired
    private BootReportDataRequestReceive bootReportDataRequestReceive ;

  //  @Autowired
  //  private BootThreadReceiverScriptUpload bootThreadReceiverScriptUpload ;

    @JmsListener(destination = "publish.topic.testSpringboot.perf.Topic", containerFactory = "jmsListenerContainerTopic")
    public void receive(String text) throws Exception {
        System.out.println("TopicListener: consumer-a 收到一条信息: " + text);

       // bootReportDataRequestReceive.receivTopicMessage(text);


        try {


         //   BootReportDataRequestReceive bootReportDataRequestReceive ;


            BootThreadReceiverScriptUpload thread = new BootThreadReceiverScriptUpload(bootReportDataRequestReceive,text) ;
            new Thread(thread, "TopicListener: consumer-a 收到一条信息......").start();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}