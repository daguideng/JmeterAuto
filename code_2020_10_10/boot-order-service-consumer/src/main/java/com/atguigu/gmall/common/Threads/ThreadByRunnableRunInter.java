package com.atguigu.gmall.common.Threads;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2018-10-24
 */

import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall.Interface.RunScriptControllerIntel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;


/**
 * 通过实现Runnable接口来创建线程类
 * 1.Runnable非常适合多个相同线程来处理同一份资源的情况
 * 2.Runnable可以避免由于Java的单继承机制带来的局限
 * 3.如果想获取当前线程句柄，只能用Thread.currentThread()方法
 */
@Component
public class ThreadByRunnableRunInter implements Runnable {


    Map map ;

    @Resource(name ="runScriptInterControllerImpl" )
    RunScriptControllerIntel RunScriptInterControllerImpl;

    private HttpServletRequest request ;


    public ThreadByRunnableRunInter(RunScriptControllerIntel RunScriptInterControllerImpl , Map map,HttpServletRequest request ){
        this.RunScriptInterControllerImpl = RunScriptInterControllerImpl ;
        this.map = map ;
        this.request = request ;
    }



    public ThreadByRunnableRunInter(){}

    public void run() {
        //添加同步快,如果还多用户都在上传所以用到同步
        synchronized (this){
            try {
                //向jmeter的代理方，发送文件地址，为agent下载：
                HttpSession session = (HttpSession) map.get("session");
                String[] ids = (String[]) map.get("ids");
                JSONObject json = JSONObject.parseObject((String)map.get("interfaceConfig"));
                System.out.println("interfaceConfig_json------>"+json);
                RunScriptInterControllerImpl.getRunmanyScript(session,ids,json,request);
                System.out.println();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }


}