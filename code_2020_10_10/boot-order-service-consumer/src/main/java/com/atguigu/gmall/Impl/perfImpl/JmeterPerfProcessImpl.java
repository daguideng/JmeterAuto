package com.atguigu.gmall.Impl.perfImpl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall.Impl.interfaceImpl.JmeterLogInfo;
import com.atguigu.gmall.Interface.JmeterProcessIntel;
import com.atguigu.gmall.activemq.PublishController;
import com.atguigu.gmall.activemq.ResultDataMessageProducer;
import com.atguigu.gmall.activemq.ThreadByRunnableSendMq;
import com.atguigu.gmall.common.utils.*;
import com.atguigu.gmall.common.utils.file.FileUtil;
import com.atguigu.gmall.controller.perf.JmeterPerfKillJmeterAgent;
import com.atguigu.gmall.service.jmeterperf.PerfConfigServer;
import com.atguigu.gmall.service.jmeterperf.StatisticsServer;
import com.atguigu.gmall.service.jmeterperf.UploadScriptServer;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ZipUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: dengdagui
 * @Description:jmeter性能测试总流程
 * @Date: Created in 2018-7-20
 */
@Slf4j
@Component
public class JmeterPerfProcessImpl implements JmeterProcessIntel {


    @Autowired
    private JsonDataToDataPerfImpl jsonDataToDataPerfImpl;

    @Resource
    private JmeterPerfKillJmeterAgent jmeterPerfKillJmeterAgent;


    @Autowired
    private PerfConfigServer perfConfigServer;

    @Autowired
    private JmeterResultLogName jmeterResultLogName;

    @Autowired
    private CommandPerfJmeterImpl commandPerfJmeterImpl;

    @Autowired
    private ResultDataMessageProducer threadDataMessageProducer;

    @Autowired
    private SendEmailPerfReportImpl sendEmailPerfReportImpl;

    @Autowired
    private StatisticsServer statisticsServer;

    @Autowired
    private UploadScriptServer uploadScriptServer;

    String destPath = null;

    @Autowired
    private PublishController publishController;


    @Value("${Path.Parameters}")
    private String PathParameters;

    @Value("${server.address}")
    private String serverAddress;

    @Autowired
    private SendEmailUtil sendEmailUtil ;

    @Value("${jmeterScriptDir}")
    private String jmeterScriptDir ;


    @Value("${resultReportDir}")
    private String resultReportDir ;

    @Value("${Heap.perf}")
    private String Heap_perf;

    @Value("${New.perf}")
    private String New_perf ;


    @Value("${JVM_ARGS.perf_linux}")
    private  String JVM_ARGS_perf_linux ;

    @Value("${controllUpdateTimeServer}")
    private  String controllUpdateTimeServer ;



    



    //1.解压相应目录
    public void unzipScript(String scriptJmeterZip) throws Exception {


        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");  //yyyyMMddHHmmss
        String todayTime = df.format(new Date()) + "/";

        destPath = jmeterScriptDir.replace("\\", "/") + todayTime;


        resultReportDir = resultReportDir.replace("\\", "/") + todayTime;

        File srcFile = new File(scriptJmeterZip);


        //destPath存在则删除：
        FileOperate.delAllFile(destPath);
      //  ZipUtils.decompress(srcFile, destPath);

      
        File unzipedDir = ZipUtil.unzip(srcFile.toString(), destPath, CharsetUtil.CHARSET_GBK);
        System.out.println("文件已解压到: " + unzipedDir.getAbsolutePath());
        

    }


    //2.相应依赖文件拷入相应目录
//2.相应依赖文件拷入相应目录
    public String resolvDependencies(String jmeterControlNode, String jmeterScriptName) throws UnsupportedEncodingException {

        //0.其它返回值：
        String scenarioFile = destPath;
        String jmxname = jmeterResultLogName.getJmeterjmx();
        File fileOrder = new File(scenarioFile + jmeterScriptName + jmxname);

        List <String> paramAttachmentList = new ArrayList <>();
        List <String> jmxFileList = new ArrayList <>();


     


        //重命名为日期格式的脚本名称： 如（zhang_umas_20191101192438.jmx）


        File f = new File(scenarioFile);
        if (f.isDirectory()) {
            File[] fileArray = f.listFiles();
            if (fileArray != null) {
                for (int i = 0; i < fileArray.length; i++) {
                    //递归调用
                    if (fileArray[i].getName().endsWith(".jmx")) {
                        fileArray[i].renameTo(fileOrder);

                    }

                }
            }
        }


        //1.解压:

        String jmeterExtJarPath = null;
        String jmeterLibJarPath = null;

        List <String> fileAbsolutePathlist = FileOperate.getAllFileNameAbsolutePath(scenarioFile);
        for (String filenameAbsolutePath : fileAbsolutePathlist) {
            filenameAbsolutePath = filenameAbsolutePath.replace("\\", "/");
            File file = new File(filenameAbsolutePath.replace("\\", "/"));
            String fileName = file.getName();

            //2.拷入jar到lib目录(lib/ext/    lib/  都要拷入jar包)
            if (filenameAbsolutePath.contains(".jar") && file.isFile()) {

                jmeterExtJarPath = jmeterControlNode.replace("bin/jmeter.properties", "lib/ext/");


                jmeterLibJarPath = jmeterControlNode.replace("bin/jmeter.properties", "lib/");

                //拷备到jmeter的相对路径中(lib/ext/):
                FileOperate.copyFile(filenameAbsolutePath, jmeterExtJarPath + fileName, true);

                //拷备到jmeter的相对路径中(lib/):
                FileOperate.copyFile(filenameAbsolutePath, jmeterLibJarPath + fileName, true);
                System.out.println("拷入Jar包:----->" + filenameAbsolutePath);

                //拷备到指定的pathParmes中:
                FileOperate.copyFile(filenameAbsolutePath, PathParameters + fileName, true);


            }


            String allFiletempDir = null;

            //2.1 得到此目录下的所有参数附件绝对路径，为修改脚本提供update的参数方法：
            if (filenameAbsolutePath.contains(".") && file.isFile()) {

                //无则创建目录:
                FileOperate.newFolder(PathParameters);

                //所有内容拷入：PathParametersInter
                String allfileName = (new File(filenameAbsolutePath)).getName();
                FileOperate.copyFile(filenameAbsolutePath, PathParameters + allfileName, true);

                //得到目录下所有文件的绝对路径：
                paramAttachmentList.add(PathParameters + allfileName);
              

            }


            //2.2 得到所有jmx的所有文件：
            if (filenameAbsolutePath.contains(".jmx") && file.isFile()) {
                jmxFileList.add(filenameAbsolutePath);

            }


        }


        //3.得到所有xml的脚本的绝对路径：
        for (String jmlfileAbsolutePath : jmxFileList) {
            System.out.println("含有jmx的脚本;------>" + jmlfileAbsolutePath);
            //4.对所有xml的脚本中相应依赖文件进行替换:
            for (String paramAttachmenFile : paramAttachmentList) {
                String replaceParametersAttachment = paramAttachmenFile.replace("\\", "/");
                String findFileNameStr = (new File(replaceParametersAttachment)).getName();
                System.out.println("findFileNameStr;------>" + findFileNameStr);
                FileReplayUtils.modiyFilePathParameter(jmlfileAbsolutePath,
                        findFileNameStr, replaceParametersAttachment);
            }


            //把修改好的jmx拷到：PathParameters
            String jmxFileName = (new File(jmlfileAbsolutePath)).getName();
            FileOperate.copyFile(jmlfileAbsolutePath, PathParameters + jmxFileName, true);

        }


        paramAttachmentList.clear();
        jmxFileList.clear();

        System.out.println("jmx的主要配置文件的路径：------>" + fileOrder.toString().replace("\\", "/"));

        return fileOrder.toString().replace("\\", "/");


    }

    /***
     public String resolvDependencies(String jmeterControlNode, String jmeterScriptName) throws UnsupportedEncodingException {

     String scenarioFile = destPath;

     String jmxname = jmeterResultLogName.getJmeterjmx();

     File fileOrder = new File(scenarioFile + jmeterScriptName + jmxname);

     //有些比较怪异常的文件:
     String scenarioFileDirectory = scenarioFile;

     String jmeterLibJarPath = null ;
     String jmeterRootPath = null ;
     String jmeterextJarPath = null ;

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

     if (filelist.get(i).getName().contains(".")) {


     jmeterRootPath = jmeterControlNode;

     jmeterLibJarPath = jmeterRootPath.replace("bin/jmeter.properties", "lib/ext/jartemp/");

     //无则创建目录:
     FileOperate.newFolder(jmeterLibJarPath);

     //拷备到jmeter的相对路径中:
     FileOperate.copyFile(filelist.get(i).getAbsolutePath().replace("\\", "/"), jmeterLibJarPath + filelist.get(i).getName(), true);


     //拷备解压后的插件jar包放到lib/ext/下：
     if(filelist.get(i).getName().contains(".jar")){

     jmeterextJarPath = jmeterRootPath.replace("bin/jmeter.properties", "lib/ext/");

     //拷备到jmeter的相对路径中:
     FileOperate.copyFile(filelist.get(i).getAbsolutePath().replace("\\", "/"), jmeterextJarPath + filelist.get(i).getName(), true);

     }



     //对解压后的脚本.jmx进行配置修改:	 	修改:jar,class,java引用路径:
     String jmeterLibJarAbsolutePath = jmeterLibJarPath + filelist.get(i).getName();

     FileReplayUtils.modiyFilePathParameter(fileOrder.toString(),
     filelist.get(i).getName(), jmeterLibJarAbsolutePath);


     //已拷准完,则删除掉结果目录的jar,参数等文件:以后会考虑使用以下方法：现在不删除


     }


     }

     }
     }


     return fileOrder.toString().replace("\\", "/");


     }

     ***/


    //3.根据配置修改相应jmeter脚本的各参数: 主要是threads数与运行时间,及场景延迟时间（秒）
    public void modiyJmeterParameter(String scriptPath, String threads, String runTime, String sleepTime, String delaytime) {


        long currenTime = System.currentTimeMillis();
        FileReplayUtils.modiyScenarioConfig(scriptPath, threads, currenTime, runTime, sleepTime, delaytime);

    }


    //3.5 设置jmeter运行内存大小：
    public void modiyJmeterJvmParameter(String jmeterControlNode) {
        //jmeter.bat  jmeter.sh
        String jmeterEnvPath = null;
        if (OSUtils.isWindows()) {
            jmeterEnvPath = jmeterControlNode.replace("jmeter.properties", "jmeter.bat");

            //修改内存：
            String HEAP = Heap_perf;
            FileReplayUtils.modiyJmeterPropertiesJvm(jmeterEnvPath, "HEAP", "set HEAP=" + HEAP);

            String NEW = New_perf;
            FileReplayUtils.modiyJmeterPropertiesJvm(jmeterEnvPath, "NEW", "set NEW=" + NEW);

        } else {
            jmeterEnvPath = jmeterControlNode.replace("jmeter.properties", "jmeter.sh");

            //修改内存：
            String JVM_ARGS_inter_linux = JVM_ARGS_perf_linux;
            FileReplayUtils.modiyJmeterPropertiesJvm(jmeterEnvPath, "JVM_ARGS", "JVM_ARGS=" + JVM_ARGS_inter_linux);

        }


    }

    //4.根据配置修改控制台输出间隔:
    public void modiyJmeterOtherParameter(String jmeterControlNode,JSONObject json ,HttpSession session) {

        String threadName = session.getAttribute(JmeterSession.SESSION_PERF).toString();
        Map <String, Object> mapkey = perfConfigServer.getByValueServer(threadName);

        List <String> intervaldata = GetJmeterDataUtil.getJmeterPerfListData(mapkey.get("ifoutinterval").toString());
        String ifoutinterval = intervaldata.get(0).equalsIgnoreCase("true")?"Yes":"No";
        String outputInterval = intervaldata.get(1);

        //对于修改控制台输出时间间隔则修改：  数据：[Yes, 30]
        if (ifoutinterval.equalsIgnoreCase("Yes")) {
            FileReplayUtils.modiyInterval(jmeterControlNode, "summariser.interval",
                    "summariser.interval=" + outputInterval);

            //修改summariser.name  为控制台输出结果的前提条件
            FileReplayUtils.modiyInterval(jmeterControlNode, "summariser.name",
                    "summariser.name=summary");

            //修改summariser.out  为控制台输出结果的前提条件
            FileReplayUtils.modiyInterval(jmeterControlNode, "summariser.out",
                    "summariser.out=true");

        }


        //对于jmeter4.0在linux不能正常生成报告的bug而写的方法：
        String jmeterUserPropertiesPath = jmeterControlNode.replace("jmeter.properties", "user.properties");
        FileReplayUtils.modiyCanReportGenerator(jmeterUserPropertiesPath, "jmeter.reportgenerator.temp_dir",
                "jmeter.reportgenerator.temp_dir=" + resultReportDir.replace("\\", "/"));


//修改java.rmi.server.hostname
        String jmeterSystemPropertiesPath = jmeterControlNode.replace("jmeter.properties", "system.properties");

        FileReplayUtils.modiyInterval(jmeterSystemPropertiesPath, "java.rmi.server.hostname=",
                "java.rmi.server.hostname=" + serverAddress);


    }


    //4.5控制台时间同步：
    public void updateTimeControll() throws Exception {
        String updateTimeServer = controllUpdateTimeServer;

        try {
            CommandUtil.commandExecution(updateTimeServer);
        } catch (Exception e) {
            CommandUtil.commandExecution("sudo yum -y install ntpdate");
            log.info("控制台时间同步失败，请检查是否安装命令： ntpdate");
        }


    }


    //4.运行性能测试
    public void commandRunJmeter(String jmeterControlNode, String scriptNamePath, String jmeterScriptName, int scriptCount, int scriptSumSize, String resultReportDir, String reportHtmlName, Integer runId, String perfstarttime,JSONObject json,HttpSession session) throws Exception {

        //得到jmeter的bin目录：
        String jmeterPathBin = jmeterControlNode.replace("jmeter.properties", "jmeter");
        //   String jmeterPathBin = jmeterControlNode.replace("jmeter.properties", "ApacheJMeter.jar");

        String threadName = session.getAttribute(JmeterSession.SESSION_PERF).toString();
        Map <String, Object> mapkey = perfConfigServer.getByValueServer(threadName);

        //得到运行时间：
      //  List <String> runtimelist = GetJmeterDataUtil.getJmeterPerfListData(mapkey.get("runtime").toString());

       // JSONObject jsonResult = JSONObject.parseObject(String.valueOf(json));
      //  System.out.println("json111111—————"+json);


        //0.延迟场景运行(秒)
        String delaytime = GetJmeterDataUtil.getJmeterPerfStringData(mapkey.get("delaytime").toString());

        //1.运行时间：(分)
        String runtimes = GetJmeterDataUtil.getJmeterPerfStringData(mapkey.get("runtime").toString());

         String runtime = runtimes.split(",")[0];
      //  String runtime = JsonPath.read(json, "$.runtime");
        //2.思考时间：（秒）
        String sleeptime =runtimes.split(",")[1];;
     //   String sleeptime = JsonPath.read(json, "$.sleeptime");
        if (StringUtils.isBlank(sleeptime)) {
            sleeptime = "0";
        }


        //是否记录日志log.jtl(Yes/No)
        String ifRecordLogJtl = GetJmeterDataUtil.getJmeterPerfStringData(mapkey.get("ifrecordlogjtl").toString());
     //   String ifRecordLogJtl = ((String)JsonPath.read(json, "$.jtlListener")).equalsIgnoreCase("true")?"Yes":"No";

        //是否记录日志jmeter.log(Yes/No)
        String ifRecordLogJmeter = GetJmeterDataUtil.getJmeterPerfStringData(mapkey.get("ifrecordlogjmeter").toString());
     //   String ifRecordLogJmeter = ((String)JsonPath.read(json, "$.logListener")).equalsIgnoreCase("true")?"Yes":"No";

        //得到并发数：
        List <String> threadslist = GetJmeterDataUtil.getJmeterPerfListData(mapkey.get("threads").toString());
      //  List <String> threadslist = JsonPath.read(json, "$.vuser");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        List<String> list = new ArrayList<>();
        for(String thread:threadslist){
            list.add(thread);
        }


        JSONObject jsonObj = new JSONObject();
        jsonObj.put("msg", "killPerfJmeterAgentProcess");
        jsonObj.toJSONString();

        for (int i = 0; i < threadslist.size(); i++) {

            Date nowTime = new Date();
            String reportRunTime = format.format(nowTime);


            String threadnum = threadslist.get(i);

            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            String time = "_" + df.format(new Date());

            String thread_runtime = time + "_" + threadnum + "_" + runtime;

            //先得到各生成结果日志名称：
            String jmeterLog = jmeterScriptName + jmeterResultLogName.getJmeterLog();
            String jmeterJtl = jmeterScriptName + jmeterResultLogName.getJmeterJtl();
            String report = jmeterScriptName + jmeterResultLogName.getReport();

            //生成相应结果报告目录：
            String jmeterReportLogDir = resultReportDir + jmeterScriptName + thread_runtime + "/" + "log/";
            String jmeterReportJtlDir = resultReportDir + jmeterScriptName + thread_runtime + "/" + "jtl/";
            String jmeterHtmlReportDir = resultReportDir + jmeterScriptName + thread_runtime + "/" + report;
            jmeterSendHtmlReport = (resultReportDir + "html/").replace("\\", "/");


            //创建Log目录：
            FileOperate.newFolder(jmeterReportLogDir);
            FileUtil.createDir(jmeterReportLogDir);
            //创建jtl目录：
            FileOperate.newFolder(jmeterReportJtlDir);
            FileUtil.createDir(jmeterReportJtlDir);
            //创建Html报告目录：
            FileOperate.newFolder(jmeterHtmlReportDir);
            FileUtil.createDir(jmeterHtmlReportDir);

            //最终结果生成报告log的文件：
            String logReportfile = jmeterReportLogDir + jmeterLog;
            //jmeterLog路径进行保存，为以后报错找原因查看：
            JmeterLogInfo.setMapLogInfo(logReportfile);
            //最终结果生成报告jtl的文件：
            String jtlReportfile = jmeterReportJtlDir + jmeterJtl;


            //1.修改并发数及运行时间：
            log.info("scriptNamePath:{}",scriptNamePath);
            this.modiyJmeterParameter(scriptNamePath, threadnum, runtime, sleeptime, delaytime);

            StringBuffer nodeIp = null;
            log.info("共有脚本总数为: {}, 现在是运行第: {}, 现运行脚本名: {}, 当前线程数: {}, 场景运行时间：{}分钟, 共运行线程数：{}",scriptSumSize, scriptCount, jmeterScriptName, threadnum, runtimes,list.toString());

          //log.info(" 共有脚本总数为： " + scriptSumSize + "现在是运行第 " + scriptCount + "个 " + "现运行脚本：" + jmeterScriptName + " 线程数" + threadnum + " 共有运行线程数为： " + threadslist.size() +"场景运行时间："+runtimes +"   测试开始-------------->");
            
       //   System.out.println(" 共有脚本总数为： " + scriptSumSize + "现在是运行第 " + scriptCount + "个 " + "现运行脚本：" + jmeterScriptName + " 线程数" + threadnum + " 共有运行线程数为： " + threadslist.size() + "场景运行时间： "+runtimes+"   测试开始-------------->");


            try {
                //2.执行命令：
                nodeIp = commandPerfJmeterImpl.commandJmeter(jmeterControlNode, jmeterPathBin, scriptNamePath, jtlReportfile, logReportfile, jmeterHtmlReportDir, ifRecordLogJtl, ifRecordLogJmeter);
            } catch (Exception e) {
                  e.printStackTrace();
                  log.error("执行命令失败! 请重新运行性能!!");
                  //重启负载机，发top消息：
                  jmeterPerfKillJmeterAgent.list(session);
              //  ThreadByRunnableSendMq thread = new ThreadByRunnableSendMq(publishController,jsonObj) ;
              //  new Thread(thread, "发送：Perf的agent都kill掉......").start();
                   Thread.sleep(4000);
                //    throw new Exception("执行命令失败! 请重新运行性能!!");
               //   break;
            }

            try {
                //3.收集性能测试结果：
                String jsFile = jmeterHtmlReportDir;
                jsonDataToDataPerfImpl.reportIndex(jmeterPathBin, jsFile, threadnum, nodeIp, jtlReportfile, runId, reportRunTime, jmeterScriptName, scriptNamePath, perfstarttime, session);

            } catch (Exception e) {
                    e.printStackTrace();
                    log.error("收集结果数据失败,请重新运行性能!!");
                //   throw new Exception("收集结果数据失败,请重新运行性能!!");
                //重启负载机，发top消息：
                  jmeterPerfKillJmeterAgent.list(session);
              //  ThreadByRunnableSendMq thread = new ThreadByRunnableSendMq(publishController,jsonObj) ;
             //   new Thread(thread, "发送：Perf的agent都kill掉......").start();
                  Thread.sleep(4000);
                //    break;
            }


        }

    }


    //5.发email报告:1
    String jmeterSendHtmlReport = null;

    public void sendEmailReport(String toemalAdress, String subject) throws Exception {
        System.out.println("jmeterSendHtmlReport====>" + jmeterSendHtmlReport);
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

        StringBuffer message = sendEmailPerfReportImpl.writeEmailReport();
        sendEmailUtil.sendHtmlEmail( message.toString(), subject);
        message.setLength(0);

    }

    // 测试完成，触发邮件发送
    public void sendEmailAfterTestCompletion() throws Exception {
        log.info("测试完成，准备发送邮件报告");
        // 这里可以调用现有的邮件发送方法
        // 需要获取收件人地址和邮件主题，这里使用默认值或从配置中获取
        String toEmailAddress = "default@example.com"; // 需要根据实际情况获取
        String subject = "性能测试报告"; // 需要根据实际情况获取
        sendEmailReport(toEmailAddress, subject);
    }


    String emailHtmlAbsolutePaht = null;

    public void sendEmailReportSingTyle(String toemalAdress, String subject) throws Exception {
        StringBuffer message = sendEmailPerfReportImpl.sendSingTyleModel(emailHtmlAbsolutePaht);
        sendEmailUtil.sendHtmlEmail(message.toString(), subject);
        message.setLength(0);
    }

    //6.发email报告:2
    public void sendEmailReportSingTyle(String toemalAdress, String subject, String resultReportDir, String reportHtmlName, SortedMap <Integer, String> scriptNameMap) throws Exception {


        StringBuffer message = sendEmailPerfReportImpl.sendSingTyleModel(resultReportDir, reportHtmlName, scriptNameMap);
        sendEmailUtil.sendHtmlEmail(message.toString(), subject);
        message.setLength(0);
    }


    //7.处理jmeterNode
    public void removeThreadJmeterNode(String jmeterControlNode) {

        boolean targer = JmeterNode.ThreadJmeterNode.remove(Thread.currentThread(), jmeterControlNode);
        if (targer == true) {
            JmeterNode.storelist.remove(jmeterControlNode);
            JmeterNode.ThreadJmeterNode.clear();
            System.gc();
            log.info("jmeterNode处理完毕 {}");
            System.out.println("jmeterNode处理完毕 {}");
        }


        //成功运行后,强制结束：jmeter-server  [agent]
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("runJmeterAgentSucessOver", "runJmeterAgentSucessOver");
        jsonObj.toJSONString();
        ThreadByRunnableSendMq thread = new ThreadByRunnableSendMq(publishController, jsonObj);

        new Thread(thread, "发送:jmeter控制台成功运行结束，向代理服务器发消息......").start();


    }

}
