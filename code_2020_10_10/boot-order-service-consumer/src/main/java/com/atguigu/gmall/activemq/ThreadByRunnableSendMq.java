package com.atguigu.gmall.activemq;


import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall.entity.Upload_info;
import org.springframework.stereotype.Component;

/**
 * 通过实现Runnable接口来创建线程类
 * 1.Runnable非常适合多个相同线程来处理同一份资源的情况
 * 2.Runnable可以避免由于Java的单继承机制带来的局限
 * 3.如果想获取当前线程句柄，只能用Thread.currentThread()方法
 */
@Component
public class ThreadByRunnableSendMq implements Runnable {


    JSONObject jSONObject;


    PublishController publishController;


    public ThreadByRunnableSendMq(PublishController publishController, JSONObject jSONObject) {
        this.publishController = publishController;
        this.jSONObject = jSONObject;
    }


    public ThreadByRunnableSendMq() {
    }

    public void run() {
        //添加同步快,如果还多用户都在上传所以用到同步
       // synchronized (this) {
            try {
                //向jmeter的代理方，发送文件地址，为agent下载：
                //   threadDataMessageProducer.send(jSONObject.toString());
                System.out.println("jSONObject.toString()--->" + jSONObject.toString());

                publishController.sendTopic(jSONObject.toString());
                System.out.println("uploadinfo.toString()===>" + jSONObject.toString());
                System.out.println(Thread.currentThread().getName() + "发送amq---->");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println();
            }


     //   }
    }

    public static void main(String[] arg) {

        //   TopicSender  sendtop = new TopicSender();
        Upload_info uploadinfo = new Upload_info();

        //  ThreadByRunnableSendMq  thread = new ThreadByRunnableSendMq(sendtop,uploadinfo) ;
        //    new Thread(thread, "线程1").start();

        //     new Thread(t1, "线程1").start();
        //      new Thread(t1, "线程2").start();
    }
}