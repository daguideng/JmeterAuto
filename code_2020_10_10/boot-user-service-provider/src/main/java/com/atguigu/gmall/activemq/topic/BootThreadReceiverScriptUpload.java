package com.atguigu.gmall.activemq.topic;

import org.springframework.stereotype.Component;

@Component
public class BootThreadReceiverScriptUpload implements Runnable{


	BootReportDataRequestReceive bootReportDataRequestReceive ;
	Object object ;



	public BootThreadReceiverScriptUpload(){}


    public BootThreadReceiverScriptUpload(BootReportDataRequestReceive bootReportDataRequestReceive, Object object ){
    	this.bootReportDataRequestReceive = bootReportDataRequestReceive ;
    	this.object = object ;


    }
	
	
	 public void run() {

         //添加同步快    如果还多用户都在上传所以用到同步
         synchronized (this){
                 try {
                	 System.out.println(Thread.currentThread().getName()+" 接受服务器脚本上传消息---->");
                     bootReportDataRequestReceive.receivTopicMessage(object.toString());
                     System.out.println();
                     
                 } catch (Exception e) {
                     e.printStackTrace();
                 }


     }
 }

}
