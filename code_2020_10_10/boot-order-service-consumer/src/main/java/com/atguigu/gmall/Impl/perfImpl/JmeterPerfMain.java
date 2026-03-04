package com.atguigu.gmall.Impl.perfImpl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall.activemq.PublishController;
import com.atguigu.gmall.activemq.ThreadByRunnableSendMq;
import javax.servlet.http.HttpSession;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;




/**
 * @Author: dengdagui
 * @Description:  性能测试入口
 * @Date: Created in 2018-7-20
 */
@Slf4j
@Component
public class JmeterPerfMain {




    @Autowired
    private JmeterPerfProcess jmeterPerfProcess ;

   

    @Autowired
    private PublishController publishController;

    @Value("${jmeter.load.run}")
    private String jmeter_load_run;

    @Value("${TO}")
    private String toemail;



    /**
     * 性能测试主函数入口
     */
    public void jmeterPerMainEnter(String jmeterControlNode,String scriptJmeterZip,String jmeterScriptName,int scriptCount,int scriptSumSize,String resultReportDir,String reportHtmlName,HttpSession session) throws Exception {

        //1.解压
        jmeterPerfProcess.unzipScript(scriptJmeterZip);

        //2.相应关联文件替换：(各处相互依赖的参数或文件)
        String scriptNamePath = jmeterPerfProcess.resolvDependencies(jmeterControlNode,jmeterScriptName);


        //3.根据配置文件修改脚本的配置：(控制台输出间隔)
        jmeterPerfProcess.modiyJmeterOtherParameter(jmeterControlNode,session);


        //4.是否对重要参数的修改:(可选)   //以后进行扩展，

        //4.5 控制台与agent进行时间同步，
       // jmeterPerfProcess.updateTimeControll();



        //5.性能测试命令运行：
        try {
            jmeterPerfProcess.commandRunJmeterPerf(jmeterControlNode, scriptNamePath, jmeterScriptName,scriptCount,resultReportDir,reportHtmlName,session);
        }catch (Exception e){
            String runType =  jmeter_load_run;
            jmeterPerfProcess.removeThreadJmeterNode(jmeterControlNode);
            System.out.println("确保节点： "+runType+" 【jmeter-server】进程正常开启,开启后再次运行性能,性能测试将从失败的并发数继续运行!");
            log.info("确保节点： "+runType+" 【jmeter-server】进程正常开启,开启后再次运行性能,性能测试将从失败的并发数继续运行!");

            //jmeter-agent  kill jmeter-server
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("runJmeterAgentException", "runJmeterAgentException");
            jsonObj.toJSONString();
            ThreadByRunnableSendMq thread = new ThreadByRunnableSendMq(publishController,jsonObj) ;
            new Thread(thread, "发送:jmeter控制台运行中异常向代理服务器发消息......").start();

            throw new Exception("执行命令失败! 或 收集结果数据失败,请重新运行性能!!");
        }

        //6.发邮件：
        if(scriptCount == scriptSumSize ) {   //最后一个脚本运行完毕时发邮件：
            String toEmailAddress = toemail ;
            jmeterPerfProcess.sendEmailReport(toEmailAddress);   //报告的第一种合成报告方式：
            jmeterPerfProcess.sendEmailReportSingTyle(toEmailAddress);  //报告的第二种单独模板报告方式：
        }

        //7.对缓存中的jmeter节点进行处理：
        jmeterPerfProcess.removeThreadJmeterNode(jmeterControlNode);


    }



}
