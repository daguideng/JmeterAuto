package com.atguigu.gmall.Impl.perfImpl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall.activemq.PublishController;
import com.atguigu.gmall.activemq.ResultDataMessageProducer;
import com.atguigu.gmall.activemq.ThreadByRunnableSendMq;
import com.atguigu.gmall.common.utils.*;
import com.atguigu.gmall.service.jmeterperf.PerfConfigServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: dengdagui
 * @Description:jmeter性能测试总流程
 * @Date: Created in 2018-7-20
 */
@Slf4j
@Component
public class JmeterPerfProcess {


    @Autowired
    private JsonDataToData jsonDataToData;
    
    @Autowired
    private PerfConfigServer perfConfigServer ;

    @Autowired
    private JmeterPerfResultLogName jmeterPerfResultLogName ;

    @Autowired
    private CommandJmeter commandJmeter;

    @Autowired
    private ResultDataMessageProducer threadDataMessageProducer;

    @Autowired
    private SendEmailReport sendEmailReport ;

    @Autowired
    private PublishController publishController;

    @Autowired
    private SendEmailUtil sendEmailUtil;

  //  @Autowired
  //  private StatisticsServer statisticsServer ;

    String destPath = null;



    @Value("${jmeterScriptDir}")
    private String jmeterScriptDir  ;


    @Value("${resultReportDir}")
    private String resultReportDir ;


    @Value("${controllUpdateTimeServer}")
    private String controllUpdateTimeServer ;


    //1.解压相应目录
    public void unzipScript(String scriptJmeterZip) throws Exception {


        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");  //yyyyMMddHHmmss
        String todayTime = df.format(new Date()) + "/";

        destPath = jmeterScriptDir.replace("\\", "/") + todayTime;


        resultReportDir = resultReportDir.replace("\\", "/") + todayTime;

        File srcFile = new File(scriptJmeterZip);


        //destPath存在则删除：
        FileOperate.delAllFile(destPath);
        ZipUtils.decompress(srcFile, destPath);


    }



    /**
 * 确保路径以目录分隔符结尾
 */
private String ensureDirectoryPath(String path) {
    if (StringUtils.isBlank(path)) {
        return "/";
    }
    return path.replace("\\", "/").replaceAll("/+$", "") + "/";
}





    //2.相应依赖文件拷入相应目录
    public String resolvDependencies(String jmeterControlNode, String jmeterScriptName) throws UnsupportedEncodingException {


        String scenarioFile = destPath;

        String jmxname = jmeterPerfResultLogName.getJmeterjmx();


        File fileOrder = new File(scenarioFile +jmeterScriptName + jmxname);

        //有些比较怪异常的文件:
        String scenarioFileDirectory = scenarioFile;


        File f = new File(scenarioFileDirectory);
        if (f.isDirectory()) {
            File[] fileArray = f.listFiles();
            if (fileArray != null) {
                for (int i = 0; i < fileArray.length; i++) {
                    //递归调用
                    if (fileArray[i].getName().endsWith("jmx")) {
                        fileArray[i].renameTo(fileOrder);
                    }

                }


                //不管参数化与jar都统一进行数据替换:
                FileOperate readfilenameFileOperate = new FileOperate();
                ArrayList <File> filelist = new ArrayList <File>();
                readfilenameFileOperate.ReadAllFileEndWithFile(scenarioFileDirectory, filelist);

                for (int i = 0; i < filelist.size(); i++) {
                    //对.jar 文件进行写入内存,为jmeter调用jar服务:
              //      if (filelist.get(i).getName().contains(".") && !filelist.get(i).getName().endsWith(".jmx") && !filelist.get(i).getName().equals("jmeter.log") && !filelist.get(i).equals("log.jtl")) {

                        if (filelist.get(i).getName().toString().contains(".") ) {

                        String jmeterRootPath = jmeterControlNode;

                        String jmeterLibJarPath = jmeterRootPath.replace("bin/jmeter.properties", "lib/ext/jartemp/");

                        //无则创建目录:
                        FileOperate.newFolder(jmeterLibJarPath);

                        //拷备到jmeter的相对路径中:
                        FileOperate.copyFile(filelist.get(i).getAbsolutePath().replace("\\", "/"), jmeterLibJarPath + filelist.get(i).getName(), true);

                        //对解压后的脚本.jmx进行配置修改:	 	修改:jar,class,java引用路径:
                        String jmeterLibJarAbsolutePath = jmeterLibJarPath + filelist.get(i).getName();

                    
                        // 原错误代码
                        FileReplayUtils.modiyFilePathParameter(fileOrder.toString(),
                                filelist.get(i).getName(), jmeterLibJarAbsolutePath);
                        
                        //已拷准完,则删除掉结果目录的jar,参数等文件:以后会考虑使用以下方法：现在不删除
                            /**
                        File deFile = new File(filelist.get(i).getAbsolutePath());
                        if (deFile.exists()) {
                           deFile.delete();


                            String endwithFile = filelist.get(i).getAbsolutePath().replace("\\", "/");
                            String deletefolder = endwithFile.split(scenarioFile)[1].split("/")[0];
                            System.out.println("deletefolder="+deletefolder) ;
                            File delefolderfile = new File((scenarioFile + deletefolder).replace("\\", "/"));
                            delefolderfile.delete();

                        }
                             ***/

                    }


                }

            }
        }


        return fileOrder.toString().replace("\\", "/");


    }


    //3.根据配置修改相应jmeter脚本的各参数: 主要是threads数与运行时间,及场景延迟时间（秒）
    public void modiyJmeterParameter(String scriptPath, String threads, String runTime, String sleepTime, String delaytime) {


        long currenTime = System.currentTimeMillis() ;
        FileReplayUtils.modiyScenarioConfig(scriptPath, threads, currenTime, runTime, sleepTime, delaytime);

    }

    //4.根据配置修改控制台输出间隔:
     public void modiyJmeterOtherParameter(String jmeterControlNode,HttpSession session){

         String threadName = session.getAttribute("threadname").toString();
         Map<String,Object> mapkey = perfConfigServer.getByValueServer(threadName);

         List <String> intervaldata = GetJmeterDataUtil.getJmeterPerfListData(mapkey.get("ifoutinterval").toString());


         //对于修改控制台输出时间间隔则修改：  数据：[Yes, 30]
          if(intervaldata.get(0).equalsIgnoreCase("Yes")) {
               FileReplayUtils.modiyInterval(jmeterControlNode, "summariser.interval",
               "summariser.interval=" + intervaldata.get(1));

              //修改summariser.name  为控制台输出结果的前提条件
              FileReplayUtils.modiyInterval(jmeterControlNode, "summariser.name",
                      "summariser.name=summary");

              //修改summariser.out  为控制台输出结果的前提条件
              FileReplayUtils.modiyInterval(jmeterControlNode, "summariser.out",
                      "summariser.out=true");

          }


          //对于jmeter4.0在linux不能正常生成报告的bug而写的方法：
         String jmeterUserPropertiesPath = jmeterControlNode.replace("jmeter.properties","user.properties") ;
         FileReplayUtils.modiyCanReportGenerator(jmeterUserPropertiesPath, "jmeter.reportgenerator.temp_dir",
                 "jmeter.reportgenerator.temp_dir=" + resultReportDir.replace("\\", "/"));


     }


    //4.5控制台时间同步：
    public void updateTimeControll() throws Exception {
        String updateTimeServer = controllUpdateTimeServer ;

        try {
            CommandUtil.commandExecution(updateTimeServer);
        }catch (Exception e){
            CommandUtil.commandExecution("yum -y install ntpdate");
            log.info("控制台时间同步失败，请检查是否安装命令： ntpdate");
        }


    }




    //4.运行性能测试
    public void commandRunJmeterPerf(String jmeterControlNode, String scriptNamePath, String jmeterScriptName, int scriptCount,String resultReportDir,String reportHtmlName,HttpSession session) throws Exception {

        //得到jmeter的bin目录：
        String jmeterPathBin = jmeterControlNode.replace("jmeter.properties", "jmeter");
       //   String jmeterPathBin = jmeterControlNode.replace("jmeter.properties", "ApacheJMeter.jar");

        String threadName = session.getAttribute("threadname").toString();
        Map<String,Object> mapkey = perfConfigServer.getByValueServer(threadName);

        //得到运行时间：
        List <String> runtimelist = GetJmeterDataUtil.getJmeterPerfListData(mapkey.get("runtime").toString());

        //0.延迟场景运行(秒)
        String delaytime = GetJmeterDataUtil.getJmeterPerfStringData(mapkey.get("delaytime").toString());

        //1.运行时间：(分)
        String runtime = runtimelist.get(0);
        //2.思考时间：（秒）
        String sleeptime = runtimelist.get(1);
        if (StringUtils.isBlank(sleeptime)) {
            sleeptime = "0";
        }


        //是否记录日志log.jtl(Yes/No)
        String ifRecordLogJtl = GetJmeterDataUtil.getJmeterPerfStringData(mapkey.get("ifrecordlogjtl").toString());

        //是否记录日志jmeter.log(Yes/No)
        String ifRecordLogJmeter = GetJmeterDataUtil.getJmeterPerfStringData(mapkey.get("ifrecordlogjmeter").toString());

        //得到并发数：
        List <String> threadslist = GetJmeterDataUtil.getJmeterPerfListData(mapkey.get("threads").toString());

        for(int i = 0 ; i <threadslist.size();i++){

            String  threadnum = threadslist.get(i) ;

            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            String time = "_" + df.format(new Date());

            String thread_runtime = time + "_" + threadnum + "_" + runtime;

            //先得到各生成结果日志名称：
            String jmeterLog = jmeterScriptName + jmeterPerfResultLogName.getJmeterLog();
            String jmeterJtl = jmeterScriptName + jmeterPerfResultLogName.getJmeterJtl();
            String report = jmeterScriptName + jmeterPerfResultLogName.getReport();

            //生成相应结果报告目录：
            String jmeterReportLogDir = resultReportDir + jmeterScriptName + thread_runtime + "/" + "log/";
            String jmeterReportJtlDir = resultReportDir + jmeterScriptName + thread_runtime + "/" + "jtl/";
            String jmeterHtmlReportDir = resultReportDir + jmeterScriptName + thread_runtime + "/" + report;
            jmeterSendHtmlReport = (resultReportDir  + "html/").replace("\\","/") ;


            //创建Log目录：
            FileOperate.newFolder(jmeterReportLogDir);
            //创建jtl目录：
            FileOperate.newFolder(jmeterReportJtlDir);
            //创建Html报告目录：
            FileOperate.newFolder(jmeterHtmlReportDir);

            //最终结果生成报告log的文件：
            String logReportfile = jmeterReportLogDir + jmeterLog;
            //最终结果生成报告log的文件：
            String jtlReportfile = jmeterReportJtlDir + jmeterJtl;


            //1.修改并发数及运行时间：
            this.modiyJmeterParameter(scriptNamePath.toString(), threadnum, runtime, sleeptime, delaytime);


            StringBuffer nodeIp = null;


            log.info(" 运行脚本："+jmeterScriptName+" 线程数"+threadnum+" 测试开始........");
            System.out.println(" 运行脚本："+jmeterScriptName+" 线程数"+threadnum+" 测试开始........");

            try {
                //2.执行命令：
                nodeIp = commandJmeter.commandPerfJmeter(jmeterPathBin, scriptNamePath.toString(), jtlReportfile, logReportfile, jmeterHtmlReportDir, ifRecordLogJtl, ifRecordLogJmeter);
            } catch (Exception e) {
            //    throw new Exception("执行命令失败! 请重新运行性能!!");
               break;
            }


            try {

                //清空当前报告:当第一个脚本且是运行第一次并发数时：
                /*
                if(1 == scriptCount && threadnum.equals(threadslist.get(0))) {
                    statisticsServer.deleteMiddNightStatistics();
                    System.out.println("清空当前报告 执行......");
                }
                */

                //3.收集结果：
                String jsFile = jmeterHtmlReportDir;
                jsonDataToData.reportIndex(jmeterPathBin,jsFile, threadnum, nodeIp, jtlReportfile, jmeterScriptName);

            } catch (Exception e) {
              //  throw new Exception("收集结果数据失败,请重新运行性能!!");
                break ;
            }

             //以单个脚本结果格式生成报告模板：
            if(i == threadslist.size()-1){
                emailHtmlAbsolutePaht = resultReportDir +  reportHtmlName ;
                sendEmailReport.writeEmailReport(resultReportDir,reportHtmlName,jmeterScriptName);

            }
        }

    }


    //5.发email报告:1
    String jmeterSendHtmlReport = null ;
    public void sendEmailReport(String toemalAdress) throws Exception {
        System.out.println("jmeterSendHtmlReport====>"+jmeterSendHtmlReport);
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

        StringBuffer message =  sendEmailReport.writeEmailReport();
        sendEmailUtil.sendHtmlEmail(message.toString());
        message.setLength(0);
    }


    //6.发email报告:2
    String emailHtmlAbsolutePaht = null ;
    public void sendEmailReportSingTyle(String toemalAdress) throws Exception {
        StringBuffer message  = sendEmailReport.sendSingTyleModel(emailHtmlAbsolutePaht) ;
        sendEmailUtil.sendHtmlEmail(message.toString());
        message.setLength(0);
    }



    //7.处理jmeterNode
    public void removeThreadJmeterNode(String jmeterControlNode) {

        boolean targer = JmeterNode.ThreadJmeterNode.remove(Thread.currentThread(), jmeterControlNode);
        if (targer == true) {
            JmeterNode.storelist.remove(jmeterControlNode);
            log.info("jmeterNode处理完毕 {}");
            System.out.println("jmeterNode处理完毕 {}");
        }


        //成功运行后,强制结束：jmeter-server  [agent]
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("runJmeterAgentSucessOver", "runJmeterAgentSucessOver");
        jsonObj.toJSONString();
        ThreadByRunnableSendMq thread = new ThreadByRunnableSendMq(publishController,jsonObj);
        new Thread(thread, "发送:jmeter控制台成功运行结束，向代理服务器发消息......").start();
        System.out.println();


    }

}
