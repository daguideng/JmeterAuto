package com.atguigu.gmall.Impl.perfImpl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall.Interface.JmeterMainIntel;
import com.atguigu.gmall.activemq.PublishController;
import com.atguigu.gmall.activemq.ThreadByRunnableSendMq;
import com.atguigu.gmall.controller.perf.JmeterPerflinkReport;
import com.atguigu.gmall.service.jmeterperf.Jmeter_perf_run_ipServer;
import com.atguigu.gmall.service.jmeterperf.PerfConfigServer;
import com.atguigu.gmall.service.jmeterperf.UploadScriptServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpSession;

import static java.util.Arrays.sort;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.SortedMap;
import java.util.List;
import java.util.ArrayList;
import com.atguigu.gmall.common.utils.SendEmailUtil;
import com.atguigu.gmall.service.jmeterinter.ReportService;


/**
 * @Author: dengdagui
 * @Description: 性能测试入口
 * @Date: Created in 2018-7-20
 */
@Slf4j
@Component
public class JmeterPerfMainImpl implements JmeterMainIntel {



    @Autowired
    private JmeterPerfProcessImpl jmeterPerfProcessImpl;


    @Autowired
    private PerfConfigServer perfConfigServer;


    @Autowired
    private UploadScriptServer uploadScriptServer;

    @Autowired
    private SendEmailUtil sendEmailUtil;




    @Autowired
    private PublishController publishController;

    @Autowired
    private Jmeter_perf_run_ipServer jmeter_perf_run_ipServer;


    @Value("${jmeter.load.run}")
    private String jmeter_load_run ;

    @Value("${TO}")
    private String toemail;

    @Autowired
    private JmeterPerflinkReport jmeterPerflinkReport;


   
    @Autowired
    private ReportService reportService;


    /**
     * 性能测试主函数入口
     */
    String sessionName = null;

    List<String> runIds = new ArrayList<>();

     int runtimeInit = 0;


     static String uploadininfo_lasttime = null;
     


    @Override
    public void jmeterMainEnter(String jmeterControlNode, String scriptJmeterZip, String jmeterScriptName, int scriptCount, int scriptSumSize, String resultReportDir, String reportHtmlName, SortedMap <Integer, String> scriptNameMap, Integer runId,JSONObject json ,HttpSession session) throws Exception {

        
        log.info("jmeterMainEnter方法被调用 - scriptCount: {}, scriptSumSize: {}, runId: {}", scriptCount, scriptSumSize, runId);
        
        sessionName = (String) session.getAttribute(JmeterSession.SESSION_PERF);

        //1.解压
        try {
            jmeterPerfProcessImpl.unzipScript(scriptJmeterZip);
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("----->解压");

        String scriptNamePath = null;
        //2.相应关联文件替换：(各处相互依赖的参数或文件)
        try {
             scriptNamePath = jmeterPerfProcessImpl.resolvDependencies(jmeterControlNode, jmeterScriptName);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("----->相应关联文件替换");


        //3.根据配置文件修改脚本的配置：(控制台输出间隔)
        try {
            jmeterPerfProcessImpl.modiyJmeterOtherParameter(jmeterControlNode, json, session);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("----->根据配置文件修改脚本的配置");


        //3.5 根据配置设置jmeter的运行内存大小:
        try {
            jmeterPerfProcessImpl.modiyJmeterJvmParameter(jmeterControlNode);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("----->根据配置设置jmeter的运行内存大小");

        //4.是否对重要参数的修改:(可选)   //以后进行扩展，

        //4.5 控制台与agent进行时间同步，
        // jmeterPerfProcess.updateTimeControll();


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date nowTime = new Date();
        String perfstarttime = format.format(nowTime);




        if(scriptCount == 1){
            System.out.println("scriptCount11111"+scriptCount);
             uploadininfo_lasttime= perfstarttime;
             runtimeInit = 1;
             runIds.clear();
        }


       // System.out.println("scriptCount8888"+scriptCount);







        //5.性能测试命令运行：
        try {
            jmeterPerfProcessImpl.commandRunJmeter(jmeterControlNode, scriptNamePath, jmeterScriptName, scriptCount, scriptSumSize, resultReportDir, reportHtmlName, runId, perfstarttime,json ,session);
            runIds.add(runId.toString());
        } catch (Exception e) {
            try {
                jmeterPerfProcessImpl.removeThreadJmeterNode(jmeterControlNode);
                this.ExceptionDo(jmeterControlNode, runId, perfstarttime);
            } catch (Exception e1) {
                log.error(e1.getMessage());
                e1.printStackTrace();
            } finally {
                throw new Exception("执行命令失败! 或 收集结果数据失败,请重新运行性能!!");
            }

        }



         //8.更新性能测试的运行状态：
        this.updateState(runId, uploadininfo_lasttime);

       

        //6.发邮件：
      //  log.info("准备调用sendEmail方法 - scriptCount: {}, scriptSumSize: {}, runIds大小: {}", scriptCount, scriptSumSize, runIds.size());
      //   this.sendEmail(scriptCount, scriptSumSize, resultReportDir, reportHtmlName, scriptNameMap, session);
         //是否发送邮件报告：
         if(sendEmailUtil.sendEmailReportOk() == true){
              this.sendEmail(scriptCount, scriptSumSize, runIds, uploadininfo_lasttime,session);
         }
        
       


         
         


        //7.对缓存中的jmeter节点进行处理：
        jmeterPerfProcessImpl.removeThreadJmeterNode(jmeterControlNode);


        
    }


    /**
     * send email
     */
   // public void sendEmail(int scriptCount, int scriptSumSize, String resultReportDir, String reportHtmlName, SortedMap <Integer, String> scriptNameMap,HttpSession session) {

        public void sendEmail(int scriptCount, int scriptSumSize, List<String> runIds, String uploadininfo_lasttime ,HttpSession session) {


            System.out.println("scriptCount: " + scriptCount);
            System.out.println("scriptSumSize: " + scriptSumSize);

             
          log.info("sendEmail方法被调用 - scriptCount: {}, scriptSumSize: {}, runIds: {}, perfstarttime: {}", scriptCount, scriptSumSize, runIds, uploadininfo_lasttime);
          log.info("条件判断: scriptCount == scriptSumSize -> {} == {} -> {}", scriptCount, scriptSumSize, scriptCount == scriptSumSize);
       
       if (scriptCount == scriptSumSize) {   //最后一个脚本运行完毕时发邮件：

         //   String toEmailAddress = null;
                               //       generatePerfReport(String[] ids, String lastruntimeStrs)
            try {
                // 所有ID使用相同的时间戳
                String reportContent = reportService.generatePerfReport(runIds.toArray(new String[0]), uploadininfo_lasttime);
                log.info("报告内容长度: {}", reportContent != null ? reportContent.length() : 0);

                String subject = "性能测试结果_"+uploadininfo_lasttime;

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


            // try {
            //     jmeterPerfProcessImpl.sendEmailReport(toEmailAddress, EmailReportSubject.PERF_SUBJECT + "_1");   //报告的第一种合成报告方式：
            // } catch (Exception e) {
            //     System.out.println("发送报告的第一种合成报告方式邮件异常!");
            //     e.printStackTrace();
            // }

            // try {
            //     jmeterPerfProcessImpl.sendEmailReportSingTyle(toEmailAddress, EmailReportSubject.PERF_SUBJECT + "_2", resultReportDir, reportHtmlName, scriptNameMap);  //报告的第二种单独模板报告方式：
            // } catch (Exception e) {
            //     System.out.println("发送报告的第二种合成报告方式邮件异常!");
            //     e.printStackTrace();
            // }

        }

       
    }


    /**
     * update
     */
    public void updateState(int runId, String currentTime) {

        try {
            perfConfigServer.updateRunStates(JmeterPerRunStatus.Perf_Sucess, sessionName);
            uploadScriptServer.updatePerfRunStates(JmeterPerRunStatus.Perf_Sucess, runId, JmeterPerRunStatus.Perf_Sucess, currentTime);
        } catch (Exception e) {
            perfConfigServer.updateRunStates(JmeterPerRunStatus.Perf_Fail, sessionName);
            uploadScriptServer.updatePerfRunStates(JmeterPerRunStatus.Perf_Fail, runId, JmeterPerRunStatus.Perf_Fail, currentTime);
            System.out.println();
        }

    }


    /***
     * Exception do
     */
    public void ExceptionDo(String jmeterControlNode, int runId, String currentTime) throws Exception {

        String runType = jmeter_load_run;
        jmeterPerfProcessImpl.removeThreadJmeterNode(jmeterControlNode);
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
        perfConfigServer.updateRunStates(JmeterPerRunStatus.Perf_Fail, sessionName);
        uploadScriptServer.updatePerfRunStates(JmeterPerRunStatus.Perf_Fail, runId, JmeterPerRunStatus.Perf_Fail, currentTime);


        //通过接口得到用户信息：{email}
        String toEmailAddress = null;
        try {
            //  String email = SessionUser.getEmail();
            String email = "294332968@qq.com";

            toEmailAddress = email;
            if ("".equals(email) || null == email) {
                toEmailAddress = toemail;
            }
        } catch (Exception e1) {

            toEmailAddress = toemail;
            System.out.println(e1.getMessage());
        }

        jmeterPerfProcessImpl.sendEmailReport(toEmailAddress, EmailReportSubject.PERF_SUBJECT + "_1");   //报告的第一种合成报告方式：


    }


}
