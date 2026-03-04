package com.atguigu.gmall.Impl.interfaceImpl;

import com.alibaba.dubbo.common.utils.Log;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall.Impl.perfImpl.EmailReportSubject;
import com.atguigu.gmall.Impl.perfImpl.JmeterPerRunStatus;
import com.atguigu.gmall.Impl.perfImpl.JmeterSession;
import com.atguigu.gmall.Interface.JmeterMainIntel;
import com.atguigu.gmall.activemq.PublishController;
import com.atguigu.gmall.activemq.ResultDataMessageProducer;
import com.atguigu.gmall.activemq.ThreadByRunnableSendMq;
import com.atguigu.gmall.controller.perf.JmeterPerflinkReport;
import com.atguigu.gmall.service.jmeterinter.InterConfigServer;
import com.atguigu.gmall.service.jmeterinter.ReportService;
import com.atguigu.gmall.service.jmeterperf.Jmeter_perf_run_ipServer;
import com.atguigu.gmall.service.jmeterperf.UploadScriptServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

import static java.util.Arrays.sort;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import com.atguigu.gmall.common.utils.SendEmailUtil;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2018-9-27
 */
@Slf4j
@Component
public class JmeterInterMainImpl implements JmeterMainIntel {


    @Autowired
    private JmeterIntelProcessImpl jmeterIntelProcessImpl;



    @Autowired
    private ResultDataMessageProducer threadDataMessageProducer;

    @Autowired
    private PublishController publishController;

    @Autowired
    private Jmeter_perf_run_ipServer jmeter_perf_run_ipServer;

    @Autowired
    private InterConfigServer interConfigServer;

    @Autowired
    private UploadScriptServer uploadScriptServer;

    @Value("${jmeter.load.run}")
    private String jmeter_load_run ;



      @Autowired
    private SendEmailUtil sendEmailUtil;


   
    @Autowired
    private ReportService reportService;


    /**
     * 接口测试主函数入口
     */
    String sessionName = null;

    List<String> runIds = new ArrayList<>();

     int runtimeInit = 0;

     static String uploadininfo_lasttime = null;

    public void jmeterMainEnter(String jmeterControlNode, String scriptJmeterZip, String jmeterScriptName, int scriptCount, int scriptSumSize, String resultReportDir, String reportHtmlName, SortedMap<Integer, String> scriptNameMap, Integer runId, JSONObject json, HttpSession session) throws Exception {


        sessionName = (String) session.getAttribute(JmeterSession.SESSION_INTER);
        System.out.printf("sessionName---》"+sessionName);

        //1.解压
        jmeterIntelProcessImpl.unzipScript(scriptJmeterZip);
        log.info("解压成功！");

        //2.相应关联文件替换：(各处相互依赖的参数或文件)

        String scriptNamePath = null;
        try {

             scriptNamePath = jmeterIntelProcessImpl.resolvDependencies(jmeterControlNode, jmeterScriptName);
            log.info("关联文件替换成功！");

        } catch (Exception e) {
            e.printStackTrace();
        }


        //3.根据配置文件修改脚本的配置：(控制台输出间隔)
        try {
            jmeterIntelProcessImpl.modiyJmeterOtherParameter(jmeterControlNode, json, session);
            log.info("配置文件修改脚本的配置成功！");

        } catch (Exception e1) {
            e1.printStackTrace();
        }

        //3.5 根据配置设置jmeter的运行内存大小:
        try {
            jmeterIntelProcessImpl.modiyJmeterJvmParameter(jmeterControlNode);
            log.info("设置jmeter的运行内存大小成功！");

        } catch (Exception e1) {
            e1.printStackTrace();
        }


        //4.是否对重要参数的修改:(可选)   //以后进行扩展，

        //4.5 控制台与agent进行时间同步，
        // jmeterPerfProcess.updateTimeControll();


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date nowTime = new Date();
        String currentTime = format.format(nowTime);

        if(scriptCount == 1){
             uploadininfo_lasttime= currentTime;
             runtimeInit = 1;
             runIds.clear();
        }
        



        //5.接口测试命令运行：
        try {


            jmeterIntelProcessImpl.commandRunJmeter(jmeterControlNode, scriptNamePath, jmeterScriptName, scriptCount, scriptSumSize, resultReportDir, reportHtmlName, runId, currentTime, json, session);

            runIds.add(runId.toString());
            log.info("接口测试命令运行成功！");
        } catch (Exception e) {
            //  String runType =  propertyUtil.getProperty("jmeter.load.run");
            jmeterIntelProcessImpl.removeThreadJmeterNode(jmeterControlNode);
            //    logger.info("确保节点： "+runType+" 【jmeter-server】进程正常开启,开启后再次运行性能,性能测试将从失败的并发数继续运行!");

            //jmeter-agent  kill jmeter-server
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("runJmeterAgentException", "runJmeterAgentException");
            jsonObj.toJSONString();
            ThreadByRunnableSendMq thread = new ThreadByRunnableSendMq(publishController, jsonObj);
            new Thread(thread, "发送:jmeter控制台运行中异常向代理服务器发消息......").start();

            //中间失败了，也要发部分邮件：
           // jmeterIntelProcessImpl.sendEmailReport(toEmailAddress, EmailReportSubject.INTER_SUBJECT + "_1");   //报告的第一种合成报告方式：
            //       jmeterIntelProcessImpl.sendEmailReportSingTyle(toEmailAddress,EmailReportSubject.INTER_SUBJECT+"_2");  //报告的第二种单独模板报告方式：

            //更新接口测试的运行状态：
            this.ExceptionDo(jmeterControlNode, runId, currentTime);

            throw new Exception("执行命令失败! 或 收集结果数据失败,请重新运行性能!!");
        }



        //6.对缓存中的jmeter节点进行处理：
        jmeterIntelProcessImpl.removeThreadJmeterNode(jmeterControlNode);


        //7.更新接口测试的运行状态：
        this.updateState(runId, uploadininfo_lasttime);


        //8.发邮件：
        try {
           
             if(sendEmailUtil.sendEmailReportOk() == true){
              this.sendEmail(scriptCount, scriptSumSize, runIds, uploadininfo_lasttime,session);
         }
           
          //  runIdList.clear();
            
        
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * send email
     */
    public void sendEmail(int scriptCount, int scriptSumSize, List<String> runIds, String uploadininfo_lasttime, HttpSession session) {


      


        if (scriptCount == scriptSumSize) {   //最后一个脚本运行完毕时发邮件：
            try {
                // 所有ID使用相同的时间戳
                String reportContent = reportService.generateInterReport(runIds.toArray(new String[0]), uploadininfo_lasttime);
                log.info("报告内容长度: {}", reportContent != null ? reportContent.length() : 0);

                String subject = "接口测试结果_" + uploadininfo_lasttime;

                if (reportContent != null && !reportContent.trim().isEmpty()) {
                    log.info("开始发送邮件，主题: {}", subject);
                    
                    boolean sendResult = sendEmailUtil.sendHtmlEmail(reportContent, subject);
                    if (sendResult) {
                        log.info("邮件发送成功");
                    } else {
                        log.error("邮件发送失败");
                    }
                } else {
                    log.warn("报告内容为空，跳过邮件发送");
                }

            } catch (Exception e) {
                log.error("生成报告或发送邮件时发生异常: {}", e.getMessage(), e);
            }

            runIds.clear();
    
        }



    }


    /**
     * update
     */
    public void updateState(int runId, String currentTime) {

        try {
            interConfigServer.updateRunStates(JmeterPerRunStatus.Inter_Sucess, sessionName);
            uploadScriptServer.updateInterRunStates(JmeterPerRunStatus.Inter_Sucess, runId, JmeterPerRunStatus.Inter_Sucess, currentTime);
        } catch (Exception e) {
            interConfigServer.updateRunStates(JmeterPerRunStatus.Inter_Fail, sessionName);
            uploadScriptServer.updateInterRunStates(JmeterPerRunStatus.Inter_Fail, runId, JmeterPerRunStatus.Inter_Fail, currentTime);
        }

    }


    /***
     * Exception do
     */
    public void ExceptionDo(String jmeterControlNode, int runId, String currentTime) throws Exception {

        String runType = jmeter_load_run;
        jmeterIntelProcessImpl.removeThreadJmeterNode(jmeterControlNode);
        System.out.println("确保节点： " + runType + " 【jmeter-server】进程正常开启,开启后再次运行性能,性能测试将从失败的并发数继续运行!");
        log.info("确保节点： " + runType + " 【jmeter-server】进程正常开启,开启后再次运行性能,性能测试将从失败的并发数继续运行!");

        //jmeter-agent  kill jmeter-server
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("runJmeterAgentException", "runJmeterAgentException");
        jsonObj.toJSONString();
        ThreadByRunnableSendMq thread = new ThreadByRunnableSendMq(publishController, jsonObj);
        new Thread(thread, "发送:jmeter控制台运行中异常向代理服务器发消息......").start();

        //中间失败了，也要发部分邮件：


        //更新性能测试的运行状态：
        interConfigServer.updateRunStates(JmeterPerRunStatus.Perf_Fail, sessionName);
        uploadScriptServer.updateInterRunStates(JmeterPerRunStatus.Perf_Fail, runId, JmeterPerRunStatus.Perf_Fail, currentTime);


        //通过接口得到用户信息：{email}
        String toEmailAddress = null;
        try {
            //  String email = SessionUser.getEmail();
            String email = "294332968@qq.com";
            log.info("session email----->" + email);
            toEmailAddress = email;
            if ("".equals(email) || null == email) {
            }
        } catch (Exception e1) {

            System.out.println(e1.getMessage());
        }

       // jmeterIntelProcessImpl.sendEmailReport(toEmailAddress, EmailReportSubject.PERF_SUBJECT + "_1");   //报告的第一种合成报告方式：


    }
}
