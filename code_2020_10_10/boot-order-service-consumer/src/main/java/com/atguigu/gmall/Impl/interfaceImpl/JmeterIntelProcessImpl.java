package com.atguigu.gmall.Impl.interfaceImpl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall.Impl.perfImpl.JmeterNode;
import com.atguigu.gmall.Impl.perfImpl.JmeterResultLogName;
import com.atguigu.gmall.Impl.perfImpl.JmeterSession;
import com.atguigu.gmall.Interface.JmeterProcessIntel;
import com.atguigu.gmall.activemq.PublishController;
import com.atguigu.gmall.activemq.ResultDataMessageProducer;
import com.atguigu.gmall.activemq.ThreadByRunnableSendMq;
import com.atguigu.gmall.common.utils.*;
import com.atguigu.gmall.common.utils.file.FileUtil;
import com.atguigu.gmall.service.jmeterinter.ReportService;
import com.atguigu.gmall.service.jmeterinter.InterConfigServer;
import com.atguigu.gmall.service.jmeterperf.UploadScriptServer;

import lombok.extern.slf4j.Slf4j;
import com.atguigu.gmall.entity.Upload_info;

import org.hibernate.validator.constraints.pl.REGON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
// UploadScriptServer 类不存在，先注释掉相关引用，后续确认包路径或接口定义后再放开
// import com.atguigu.gmall.service.jmeterinter.UploadScriptServer;

import com.atguigu.gmall.service.jmeterperf.UploadScriptServer;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.atguigu.gmall.controller.inter.JmeterInterReportImpl;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2018-9-27
 */
@Slf4j
@Component
public class JmeterIntelProcessImpl implements JmeterProcessIntel {


    @Autowired
    private JsonDataToDataInterImpl jsonDataToDataInterImpl;


    @Autowired
    private InterConfigServer interConfigServer;

    @Autowired
    private JmeterResultLogName jmeterResultLogName;

    @Autowired
    private CommandInterJmeterImpl commandInterJmeterImpl;

    @Autowired
    private ResultDataMessageProducer threadDataMessageProducer;

    @Autowired
    private SendEmailInterReportImpl sendEmailInterReportImpl;

    @Autowired
    private PublishController publishController;

    @Autowired
    private UploadScriptServer uploadScriptServer;

    @Autowired
     private ReportService reportService;


    @Value("${Path.Parameters}")
    private String PathParameters;

    @Value("${server.address}")
    private String serverAddress;


    @Value("${jmeterScriptDir.Inter}")
    private String jmeterScriptDir_Inter ;

    @Value("${resultReportDir.Inter}")
    private String resultReportDir_Inter ;

    @Autowired
    private JmeterInterReportImpl jmeterInterReportImpl ;

    @Autowired
    private SendEmailUtil sendEmailUtil ;



    @Value("${resultReportDir}")
    private String resultReportDir ;


    @Value("${Heap.inter}")
    private String Heap_inter ;


    @Value("${New.inter}")
    private String New_inter ;


    @Value("${JVM_ARGS.inter_linux}")
    private String JVM_ARGS_inter_linux ;


    @Value("${controllUpdateTimeServer}")
    private String controllUpdateTimeServer ;



    String destPath = null;




    //1.解压相应目录
    public void unzipScript(String scriptJmeterZip) throws Exception {

        System.out.printf("scriptJmeterZip---》"+scriptJmeterZip);

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");  //yyyyMMddHHmmss
        String todayTime = df.format(new Date()) + "/";

        destPath = jmeterScriptDir_Inter.replace("\\", "/") + todayTime;


        resultReportDir = resultReportDir_Inter.replace("\\", "/") + todayTime;

        File srcFile = new File(scriptJmeterZip);


        //destPath存在则删除：
        FileOperate.delAllFile(destPath);
        ZipUtils.decompress(srcFile, destPath);


    }


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
                        System.out.println("fileArray[i]--->"+fileArray[i]);
                        fileArray[i].renameTo(fileOrder);

                    }

                }
            }
        }




        //1.解压:

        String jmeterExtJarPath = null;
        String jmeterLibJarPath = null ;

        List <String> fileAbsolutePathlist = FileOperate.getAllFileNameAbsolutePath(scenarioFile);
        for (String filenameAbsolutePath : fileAbsolutePathlist) {
            filenameAbsolutePath = filenameAbsolutePath.replace("\\", "/");
            File file = new File(filenameAbsolutePath.replace("\\", "/"));
            String fileName = file.getName();

            //2.拷入jar到lib目录(lib/ext/    lib/  都要拷入jar包)
            if (filenameAbsolutePath.contains(".jar") && file.isFile()) {

                jmeterExtJarPath = jmeterControlNode.replace("bin/jmeter.properties", "lib/ext/");
                System.out.println("jmeterControlNode:----->" + jmeterControlNode);

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
              //  FileOperate.copyFile(filenameAbsolutePath, PathParameters + allfileName, true);

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
                System.out.println("replaceParametersAttachment;------>" + replaceParametersAttachment);
                try {
                    FileReplayUtils.modiyFilePathParameter(jmlfileAbsolutePath,
                            findFileNameStr, replaceParametersAttachment);
                }catch (Exception e){
                    System.out.println("修改jmx exception!");
                }

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




    //3.根据配置修改相应jmeter脚本的各参数: 主要是threads数,及场景延迟时间（秒）,默认运行1次
    public void modiyJmeterParameter(String scriptPath, String threads, String Iterationes, String sleepTime, String delaytime) {

        FileReplayUtils.modiyScenarioInterDefault(scriptPath, threads, delaytime, Iterationes);

    }


    //4.根据配置修改控制台输出间隔:
    public void modiyJmeterOtherParameter(String jmeterControlNode,JSONObject json ,HttpSession session) {

        String threadName = session.getAttribute(JmeterSession.SESSION_INTER).toString();
        Map <String, Object> mapkey = interConfigServer.getByValueServer(threadName);

        List <String> intervaldata = GetJmeterDataUtil.getJmeterPerfListData(mapkey.get("ifoutinterval").toString());

        System.out.println("json---->"+json);

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
//        FileReplayUtils.modiyCanReportGenerator(jmeterUserPropertiesPath, "jmeter.reportgenerator.temp_dir",

        FileReplayUtils.modiyCanReportGenerator(jmeterUserPropertiesPath, "jmeter.reportgenerator.temp_dir",
                "jmeter.reportgenerator.temp_dir=" + resultReportDir.replace("\\", "/"));

        //修改java.rmi.server.hostname
        String jmeterSystemPropertiesPath = jmeterControlNode.replace("jmeter.properties", "system.properties");

        FileReplayUtils.modiyInterval(jmeterSystemPropertiesPath, "java.rmi.server.hostname=",
                "java.rmi.server.hostname=" +serverAddress );




    }


    //3.5 设置jmeter运行内存大小：
    public void modiyJmeterJvmParameter(String jmeterControlNode) {
        //jmeter.bat  jmeter.sh
        String jmeterEnvPath = null;
        if (OSUtils.isWindows()) {
            jmeterEnvPath = jmeterControlNode.replace("jmeter.properties", "jmeter.bat");

            //修改内存：
            String HEAP = Heap_inter;
            FileReplayUtils.modiyJmeterPropertiesJvm(jmeterEnvPath, "HEAP", "set HEAP=" + HEAP);

            String NEW = New_inter;
            FileReplayUtils.modiyJmeterPropertiesJvm(jmeterEnvPath, "NEW", "set NEW=" + NEW);

        } else {
            jmeterEnvPath = jmeterControlNode.replace("jmeter.properties", "jmeter.sh");

            //修改内存：
            //JVM_ARGS_inter_linux = JVM_ARGS_inter_linux;
            FileReplayUtils.modiyJmeterPropertiesJvm(jmeterEnvPath, "JVM_ARGS", "JVM_ARGS=" + JVM_ARGS_inter_linux);

        }


    }


    //4.5控制台时间同步：
    public void updateTimeControll() throws Exception {
        String updateTimeServer =controllUpdateTimeServer;

        try {
            CommandUtil.commandExecution(updateTimeServer);
        } catch (Exception e) {
            CommandUtil.commandExecution("yum -y install ntpdate");
            log.info("控制台时间同步失败，请检查是否安装命令： ntpdate");
        }


    }


    //4.运行接口测试
    public void commandRunJmeter(String jmeterControlNode, String scriptNamePath, String jmeterScriptName, int scriptCount, int scriptSumSize, String resultReportDir, String reportHtmlName, Integer runId, String currentTime,JSONObject json ,HttpSession session) throws Exception {

        //得到jmeter的bin目录：
        String jmeterPathBin = jmeterControlNode.replace("jmeter.properties", "jmeter");
        //   String jmeterPathBin = jmeterControlNode.replace("jmeter.properties", "ApacheJMeter.jar");

        String threadName = session.getAttribute(JmeterSession.SESSION_INTER).toString();

        Map <String, Object> mapkey = interConfigServer.getByValueServer(threadName);

        //0.延迟场景运行(秒)
        String delaytime = GetJmeterDataUtil.getJmeterPerfStringData(mapkey.get("delaytime").toString());

      

        List <String>  runtimes = null;

        //0.运行次数次(秒)
        try{
          runtimes = GetJmeterDataUtil.getJmeterInterListData(mapkey.get("runtime").toString());
        }catch(Exception e){
            e.printStackTrace();
        }
      //  System.out.println("runtimes---->"+runtimes);
     //   log.info("runtimes---->"+runtimes);
        
        // 检查runtimes是否为null或空列表
        if (runtimes == null || runtimes.isEmpty()) {
            log.error("runtimes为空或null，使用默认值1");
            runtimes = Arrays.asList("1");
        }
        
        String Iterationes = runtimes.get(0);

        System.out.println("Iterationes---->"+Iterationes);
        log.info("Iterationes---->"+Iterationes);   

        //是否记录日志log.jtl(Yes/No)
        String ifRecordLogJtl = GetJmeterDataUtil.getJmeterPerfStringData(mapkey.get("ifrecordlogjtl").toString());

        //是否记录日志jmeter.log(Yes/No)
        String ifRecordLogJmeter = GetJmeterDataUtil.getJmeterPerfStringData(mapkey.get("ifrecordlogjmeter").toString());

        //得到并发数：
       List <String> threadslist = GetJmeterDataUtil.getJmeterPerfListData(mapkey.get("threads").toString());

     //  List <String> threadslist  = new ArrayList<>();
     //  threadslist.add("1");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


         String reportRunTime = currentTime;
//        Date nowTime = new Date();
//        String reportRunTime = format.format(nowTime);

        for (int i = 0; i < threadslist.size(); i++) {


            String threadnum = threadslist.get(i);

            log.info("当前接口测试并发数为：" + threadnum);

            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            String time = "_" + df.format(new Date());

            String thread_runtime = time + "_" + threadnum;  //+ "_" + runtime;

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
            //最终结果生成报告log的文件：
            String jtlReportfile = jmeterReportJtlDir + jmeterJtl;


            //1.修改并发数及运行时间：
            // 强制： runtime="0" ,sleeptime="0", 后面接口测试也不用：
            String runtime = "0";
            String sleeptime = "0";
            this.modiyJmeterParameter(scriptNamePath.toString(), threadnum, Iterationes, sleeptime, delaytime);

            StringBuffer nodeIp = null;

            log.info(" 共有脚本总数为：" + scriptSumSize + "现在是运行第" + scriptCount + "个  现运行脚本：" + jmeterScriptName + " 线程数" + threadnum + "   测试开始-------------->");
            System.out.println("共有脚本总数为 ：" + scriptSumSize + " 现在是运行第" + scriptCount + " 现运行脚本：" + jmeterScriptName + " 线程数" + threadnum + "   测试开始-------------->");

            try {
                //2.执行命令：
                nodeIp = commandInterJmeterImpl.commandJmeter(jmeterControlNode, jmeterPathBin, scriptNamePath, jtlReportfile, logReportfile, jmeterHtmlReportDir, ifRecordLogJtl, ifRecordLogJmeter);
            } catch (Exception e) {
                log.error("线程数 {} 的JMeter执行失败: {}", threadnum, e.getMessage());
                // 继续执行下一个线程配置，不中断整个流程
                continue;
            }


            try {
                //3.集结果： sleep 2~10秒  如果收集的数据很大
                Thread.sleep(2000);
                String jsFile = jmeterHtmlReportDir;
                jsonDataToDataInterImpl.reportIndex(jmeterPathBin, jsFile, threadnum, nodeIp, jtlReportfile, runId, reportRunTime, jmeterScriptName, scriptNamePath, currentTime, session);

            } catch (Exception e) {
                //  throw new Exception("收集结果数据失败,请重新运行性能!!");
                break;
            }


        }

    }


    //5.发email报告:1
    String jmeterSendHtmlReport = null;
    

    public void sendEmailReport(String subject, List<Integer> runIdList, String currentTime) throws Exception {
        log.info("开始发送邮件报告，主题: {}, runId列表: {}, 当前时间: {}", subject, runIdList, currentTime);

        // 参数校验
        if (runIdList == null || runIdList.isEmpty()) {
            log.warn("runId列表为空，跳过邮件发送");
            return;
        }

        if (subject == null || subject.trim().isEmpty()) {
            log.warn("邮件主题为空，使用默认主题");
            subject = "接口测试报告";
        }

        // 创建runId列表的防御性副本，防止运行时被修改
        List<Integer> processedRunIds = new ArrayList<>(runIdList);
        log.info("原始runId列表: {}, 防御性副本: {}", runIdList, processedRunIds);

        // 等待1秒确保数据准备完成
        Thread.sleep(1000);

        // 构建ID和时间戳列表
        StringBuilder runIdStrBuilder = new StringBuilder();
        StringBuilder lastruntimeBuilder = new StringBuilder();

        boolean hasValidData = false;

        // 处理每个runId（使用防御性副本）
        log.info("开始处理runId列表，总数: {}", processedRunIds.size());
        for (int i = 0; i < processedRunIds.size(); i++) {
            Integer runId = processedRunIds.get(i);
            log.info("处理第{}个runId: {}", i + 1, runId);
            
            try {
                log.info("开始处理runId={}", runId);

                // 查询上传记录
                List<Upload_info> uploadInfoList = uploadScriptServer.getSelectAllById(runId);
                log.info("查询结果：uploadInfoList大小={}", uploadInfoList != null ? uploadInfoList.size() : 0);

                if (uploadInfoList != null && !uploadInfoList.isEmpty()) {
                    Upload_info uploadInfo = uploadInfoList.get(0);
                    String scriptName = uploadInfo.getScriptname();
                    String lastruntime = uploadInfo.getLastruntime();

                    log.info("runId={}对应的scriptName={}, lastruntime={}", runId, scriptName, lastruntime);

                    // 检查lastruntime是否有效
                    if (lastruntime != null && !lastruntime.trim().isEmpty()) {
                        try {
                            // 转换时间格式
                            String timestamp = com.atguigu.gmall.common.utils.DateUtil.date2TimeStamp(
                                lastruntime, "yyyy-MM-dd HH:mm:ss");
                            
                            // 检查时间是否有效（比当前时间早或相等，表示历史数据）
                             log.info("runId={}的时间戳比较 - timestamp: {}, currentTime: {}", runId, timestamp, currentTime);
                             
                             if (timestamp != null && !timestamp.isEmpty()) {
                                 int compareResult = timestamp.compareTo(currentTime);
                                 log.info("runId={}的时间比较结果: {} (timestamp {} currentTime)", 
                                     runId, compareResult, 
                                     compareResult <= 0 ? "<=" : ">", currentTime);
                                 
                                 if (compareResult <= 0) {
                                     // 添加到ID和时间戳列表
                                     if (runIdStrBuilder.length() > 0) {
                                         runIdStrBuilder.append(",");
                                         lastruntimeBuilder.append(",");
                                     }
                                     runIdStrBuilder.append(runId);
                                     lastruntimeBuilder.append(timestamp);
                                     
                                     hasValidData = true;
                                     log.info("runId={}的数据有效，已添加到报告列表", runId);
                                 } else {
                                     log.warn("runId={}的时间戳晚于当前时间（未来数据），跳过", runId);
                                 }
                             } else {
                                 log.warn("runId={}的时间戳无效，跳过", runId);
                             }
                            
                        } catch (Exception e) {
                            log.error("runId={}的时间格式转换失败: {}", runId, e.getMessage());
                        }
                    } else {
                        log.warn("runId={}的lastruntime字段为空", runId);
                    }
                } else {
                    log.warn("runId={}对应的上传记录为空", runId);
                }
                
            } catch (Exception e) {
                log.error("处理runId={}时发生异常: {}", runId, e.getMessage(), e);
            }
        }

        // 如果有有效数据，生成报告并发送邮件
        if (hasValidData) {
            try {
                String runIds = runIdStrBuilder.toString();
                String lastruntimes = lastruntimeBuilder.toString();
                
                log.info("开始生成报告，runIds: {}, lastruntimes: {}", runIds, lastruntimes);
                
                String reportContent = reportService.generateInterReport(runIds.split(","), lastruntimes);
                log.info("报告内容长度: {}", reportContent != null ? reportContent.length() : 0);

                if (reportContent != null && !reportContent.trim().isEmpty()) {
                    log.info("开始发送邮件，主题: {}", subject);
                    
                    boolean sendResult = sendEmailUtil.sendHtmlEmail(reportContent, subject);
                    
                    if (sendResult) {
                        log.info("接口测试报告邮件发送成功");
                    } else {
                        log.error("邮件发送失败");
                    }
                } else {
                    log.warn("报告内容为空，跳过邮件发送");
                }
                
            } catch (Exception e) {
                log.error("生成报告或发送邮件失败: {}", e.getMessage(), e);
                throw e;
            }
        } else {
            log.warn("没有有效的数据可用于生成报告，跳过邮件发送");
        }

        log.info("邮件报告发送流程结束");
    }



    @Override
    public void sendEmailReportSingTyle(String toemalAdress, String subject) throws Exception {

    }


    //6.发email报告:2
    String emailHtmlAbsolutePaht = null;

    /**
     * public void sendEmailReportSingTyle(String toemalAdress,String subject) throws Exception {
     * StringBuffer message  = sendEmailInterReportImpl.sendSingTyleModel(emailHtmlAbsolutePaht) ;
     * SendEmailUtil.sendHtmlEmail(toemalAdress,message.toString(),subject);
     * message.setLength(0);
     * }
     **/


    public void sendEmailReportSingTyle(String toemalAdress, String subject, String resultReportDir, String reportHtmlName, SortedMap <Integer, String> scriptNameMap) throws Exception {


        StringBuffer message = sendEmailInterReportImpl.sendSingTyleModel(resultReportDir, reportHtmlName, scriptNameMap);
        sendEmailUtil.sendHtmlEmail(message.toString(), subject);
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
        ThreadByRunnableSendMq thread = new ThreadByRunnableSendMq(publishController, jsonObj);
        new Thread(thread, "发送:jmeter控制台成功运行结束，向代理服务器发消息......").start();


    }


}
