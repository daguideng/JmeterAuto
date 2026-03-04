package com.atguigu.gmall.activemq.topic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall.activemq.actuator.*;
import com.atguigu.gmall.common.utils.*;
import com.atguigu.gmall.service.impl.AgentIpRunStatus;
import com.atguigu.gmall.service.impl.JmeterPerfAgentSourceToDataServer;
import com.atguigu.gmall.service.impl.KillJMeterProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * TODO jmeter代理入口：
 * author:dengdagui
 */


@Component
public class BootReportDataRequestReceive {

    @Autowired
    private JmeterPerfAgentSourceToDataServer jmeterPerfAgentSourceToDataServer;

    @Autowired
    private CommandVariationUtil commandVariationUtil;

    @Resource
    private AgentIpRunStatus agentIpRunStatus ;

    @Resource
    private KillProcessByPort killProcessByPort ;

    @Value("${agentDownStore}")
    public  String agentDownStore;

    @Value("${agentjmeterProperties}")
    public  String agentjmeterProperties;

    @Value("${agentUpdateTimeServer}")
    public   String agentUpdateTimeServer;

    @Value("${dubbo.protocol.port}")
    public  String dubbo_protocol_port;


    /**
    @Value("${Path.Parameters}")
    private String pathParameters ;
     **/



    @Autowired
    private ModiyJmeterJvmParameter modiyJmeterJvmParameter;

    @Autowired
    private AgentSourceIp agentSourceIp;




    @Resource
    private KillJMeterProcess killJMeterProcess ;





    private Logger logger = LoggerFactory.getLogger(this.getClass());


    JSONObject json = null;

    public static String runusername = null;

    /**
     * public void receive(BootReportDataRequestReceive message,String STARAGE) throws Exception{
     * //多线程控制上传脚本与运行脚本:
     * BootThreadReceiverScriptUpload  threadScriptUpload = new BootThreadReceiverScriptUpload(this,message) ;
     * new Thread(threadScriptUpload, "接送：Jmeter控制台的Amq消息......").start();
     * }
     ***/




    private static int  runTask = 0 ;


    public void receivTopicMessage(String message) throws Exception {


      //  myScheduledTask.runCheduledTask();


        logger.info("--------------agentDownStore-------------" + agentDownStore);
        logger.info("--------------agentjmeterProperties-------------" + agentjmeterProperties);
        logger.info("--------------agentUpdateTimeServer-------------" + agentUpdateTimeServer);
        logger.info("--------------dubbo_protocol_port-------------" + dubbo_protocol_port);


        try {

            if (null == message) {
                logger.info("返回消息为空!");
                return;
            }

            json = JSON.parseObject(message);
            System.out.println("json------>" + json);


            //如果运行脚本时则进行检查：
            if ("runJmeterAgentCheck".equals(json.getString("runJmeterAgentCheck"))) {

                logger.info("TopicListener.STARAGE" + "====>" + MenuDefine.STATUS);
                runusername = json.getString("runusername");
                System.out.println("runusername-------run->" + runusername);
                if (MenuDefine.STATUS.equals("INIT")) {
                    this.getIpJmeterStatus();
                }
                /**
                if (MenuDefine.STATUS.equals("INIT")) {
                    logger.info("jmeter-server 第一次启动------>");
                    MenuDefine.STATUS = MenuDefine.END_OVER;
                    String consumerIp = json.getString("consumerIp");
                    logger.info("consumer的ip地址为consumerIp:{}," + consumerIp);
                    this.jmeterAgentCheck();

                }
                 **/


            } else if ("runJmeterAgentException".equals(json.getString("runJmeterAgentCheck")) || "runJmeterAgentSucessOver".equals(json.getString("runJmeterAgentSucessOver"))) {

                runusername = null;

            } else if ("killInterJmeterAgentProcess".equals(json.getString("msg"))) {

                MenuDefine.STATUS = "INIT";
                //1.杀jmeter
                killJMeterProcess.killJmeter();
                //2.杀死对应端口号进程：
                String rmiPort = String.valueOf(Integer.valueOf(dubbo_protocol_port) + 1000);
                killProcessByPort.killProcessPort(Integer.valueOf(rmiPort));
                System.out.println("jmeter agent进程已被杀");
                this.getIpJmeterStatus();
                runusername = null;

            } else if ("runJmeterJmeterUpload".equals(json.getString("runJmeterJmeterUpload"))) {
                //否则就是上传脚本时的种操作 :
                runusername = null;

                System.out.println("上传脚本时的种操作");

               

                String getFilenae = json.getString("scriptpath").toString();
                File downFile = new File(getFilenae);
                logger.info("downFile:{}" + downFile.getName());

                // 下载地址：
                System.out.println("agentDownStore====>" + agentDownStore);
                File filedown = new File(agentDownStore);
                if (!filedown.exists()) {
                    filedown.mkdirs();
                    // System.out.println();
                }


               



                String saveScriptZip = agentDownStore + File.separator + downFile.getName();

               // HttpUtils.downLoadFromUrl(scriptDownLoadFromUrl, file.getName(), agentDownStore);

              
               String DownLoadFromUrl = json.getString("operationtypestr");
          
               logger.info("DownLoadFromUrl=============>" + DownLoadFromUrl);
               HttpUtils.hutoolDown(DownLoadFromUrl,agentDownStore);
            
               logger.info("DownLoadFromUrl=============>" + DownLoadFromUrl);
             //   HttpUtils.downLoadFromUrl(scriptDownLoadFromUrl, downFile.getName(), agentDownStore);
                logger.info("脚本下载成功! 脚本保存地址=============>" + saveScriptZip);
                logger.info(saveScriptZip);

                // 解压
                String unzipResutDir = this.unzipScript(saveScriptZip);

                // 复制
                this.resolvDependencies(downFile.getName().replace(".zip", ".jmx"), unzipResutDir);

                // 如果jmeter没有进程，则启动 jmeter进程:
                // ps -ef | grep  jmeter-server | grep -v grep | awk '{print $2}'
                //检查：
                // this.jmeterAgentCheck();


                //抓紧时间重启动 jmeter-server
                logger.info("TopicListener.STARAGE" + "====>" + MenuDefine.STATUS);
                if (MenuDefine.STATUS.equals("INIT")) {
                    logger.info("jmeter-server 第一次启动------>");
                    MenuDefine.STATUS = MenuDefine.END_OVER;
                    String consumerIp = json.getString("consumerIp");
                    logger.info("consumer的ip地址为consumerIp:{}," + consumerIp);
                    this.jmeterAgentCheck();
                    System.out.println();


                }


            } else if ("configPerf|configInter".contains(json.getString("config"))) {

                logger.info("TopicListener.STARAGE" + "====>" + MenuDefine.STATUS);
                if (MenuDefine.STATUS.equals("INIT")) {
                    logger.info("jmeter-server 第一次启动------>");
                    MenuDefine.STATUS = MenuDefine.END_OVER;
                    String consumerIp = json.getString("consumerIp");
                    logger.info("consumer的ip地址为consumerIp:{}," + consumerIp);
                    logger.info("配置文件开始操作传消息");
                    this.jmeterAgentCheck();


                }
                logger.info("TopicListener.STARAGE" + "====>" + MenuDefine.STATUS);


            }


        } catch (Exception ex) {
            logger.error("MQ 请求处理异常！", ex);
            throw ex;
        }



    }

    // 1.解压相应目录
    public String unzipScript(String scriptJmeterZip) throws Exception {

        File file = new File(scriptJmeterZip);
        System.out.println();
        String scriptName = file.getName().substring(0, file.getName().length() - 4);
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss"); // yyyyMMddHHmmss
        String todayTime = df.format(new Date());

        String destPath = file.getParent() + "/" + todayTime + "/" + scriptName;

        ZipUtilsPbc.unZipFiles(file, destPath);

        // destPath存在则删除：
        // FileOperate.delAllFile(scriptJmeterZip+"/");
        System.out.println();

        return destPath;
    }

    // 2.相应依赖文件拷入相应目录
    public void resolvDependencies(String scriptName, String unzipResutDir) throws Exception {

        File fileOrder = new File(unzipResutDir + "/" + scriptName);

        List<String> paramAttachmentList = new ArrayList <>();
        List <String> jmxFileList = new ArrayList <>();


        String  pathParameters = json.getString("PathParameters");



        //重命名为日期格式的脚本名称： 如（zhang_umas_20191101192438.jmx）
        File f = new File(unzipResutDir);
        if (f.isDirectory()) {
            File[] fileArray = f.listFiles();
            if (fileArray != null) {
                for (int i = 0; i < fileArray.length; i++) {
                    // 递归调用
                    if (fileArray[i].getName().endsWith(".jmx")) {
                        fileArray[i].renameTo(fileOrder);
                    }

                }
            }
        }


        String jmeterExtJarPath = null;
        String jmeterLibJarPath = null;



        List <String> fileAbsolutePathlist = FileOperate.getAllFileNameAbsolutePath(unzipResutDir);

        for (String filenameAbsolutePath : fileAbsolutePathlist) {
            filenameAbsolutePath = filenameAbsolutePath.replace("\\", "/");
            File file = new File(filenameAbsolutePath.replace("\\", "/"));
            String fileName = file.getName();

            //2.拷入jar到lib目录(lib/ext/    lib/  都要拷入jar包)
            if (filenameAbsolutePath.contains(".jar") && file.isFile()) {

                jmeterExtJarPath = agentjmeterProperties
                        .replace("bin/jmeter.properties", "lib/ext/");
                System.out.println("agentjmeterProperties\n:----->" + agentjmeterProperties
                );

                jmeterLibJarPath = agentjmeterProperties
                        .replace("bin/jmeter.properties", "lib/");

                //拷备到jmeter的相对路径中(lib/ext/):
                FileOperate.copyFile(filenameAbsolutePath, jmeterExtJarPath + fileName, true);

                //拷备到jmeter的相对路径中(lib/):
                FileOperate.copyFile(filenameAbsolutePath, jmeterLibJarPath + fileName, true);
                System.out.println("拷入Jar包:----->" + filenameAbsolutePath);

                //拷备到指定的pathParmes中:
                FileOperate.copyFile(filenameAbsolutePath, pathParameters + fileName, true);


            }


            String allFiletempDir = null;

            //2.1 得到此目录下的所有参数附件绝对路径，为修改脚本提供update的参数方法：
            if (filenameAbsolutePath.contains(".") && file.isFile()) {

                //无则创建目录:
                FileOperate.newFolder(pathParameters);

                //所有内容拷入：PathParametersInter
                String allfileName = (new File(filenameAbsolutePath)).getName();
                FileOperate.copyFile(filenameAbsolutePath, pathParameters + allfileName, true);

                //得到目录下所有文件的绝对路径：
                paramAttachmentList.add(pathParameters + allfileName);
                System.out.println("已拷备所有参数文件到指定目录，现已与master文件保持一致");

            }


            //2.2 得到所有jmx的所有文件：
            if (filenameAbsolutePath.contains(".jmx") && file.isFile()) {
                jmxFileList.add(filenameAbsolutePath);

            }


        }


        //3.得到所有xml的脚本的绝对路径：
        for (String jmlfileAbsolutePath : jmxFileList) {
            //4.对所有xml的脚本中相应依赖文件进行替换:
            for (String paramAttachmenFile : paramAttachmentList) {
                String replaceParametersAttachment = paramAttachmenFile.replace("\\", "/");
                String findFileNameStr = (new File(replaceParametersAttachment)).getName();
                FileReplayUtils.modiyFilePathParameter(jmlfileAbsolutePath,
                        findFileNameStr, replaceParametersAttachment);

            }

            //把修改好的jmx拷到：PathParameters
            String jmxFileName = (new File(jmlfileAbsolutePath)).getName();
            FileOperate.copyFile(jmlfileAbsolutePath, pathParameters + jmxFileName, true);
            System.out.println("xml中的文件参数已替换，且已后到相应目录{}：");
        }


        paramAttachmentList.clear();
        jmxFileList.clear();




    }


    /***
     * jmeter agent检查:
     * @throws IOException
     * @throws FileNotFoundException
     */

    public void jmeterAgentCheck() throws FileNotFoundException, IOException {

        //  String command = null;

        //如果是性能测试类型则修改：各个agent的各个端口号，如果是接口测试则不修改端口
        //	if("perfTest".equals(json.getString("testType"))){
        //	int m_start = 1100 ;
        //	int n_end = 1200 ;
        //生成从m_start到n_end的随机整数[m_start,n_end]
        //	int temp=m_start+(int)(Math.random()*(n_end+1-m_start));


        //1. 负载机资源上报:
        try {
            jmeterPerfAgentSourceToDataServer.updateAgentSourceInfo();
            logger.info("负载机资源上报{}");
        }catch (Exception e){
            System.out.println("consumer的ip地址为consumerIp:{},null");
        }


        //启动jmeter的agent之前,设置JVM:
        try {
            modiyJmeterJvmParameter.modiyJmeterJvm(agentjmeterProperties);
        }catch (Exception e){
            System.out.println("启动jmeter的agent之前,设置JVM 报异常");
        }


        String rmiPort = String.valueOf(Integer.valueOf(dubbo_protocol_port) + 1000);


        //修改主机ip
        Map <String, String> map = new HashMap <>();
        agentSourceIp.getOsNameIp(map);
        String hostIP = map.get("ipaddress");
        System.out.println("hostIP-->" + hostIP);
        map.clear();


        logger.info("java.rmi.server.hostname 为：" + hostIP);
        String jmeterSystemPropertiesPath = agentjmeterProperties.replace("jmeter.properties", "system.properties");
        FileReplayUtils.modiyJmeterPropertiesPort(jmeterSystemPropertiesPath, "java.rmi.server.hostname",
                "java.rmi.server.hostname=" + hostIP);
        logger.info("java.rmi.server.hostname：" + hostIP + "修改完毕");


        //server.rmi.localport
        logger.info("server.rmi.localport 为：" + rmiPort);
        FileReplayUtils.modiyJmeterPropertiesPort(agentjmeterProperties, "server.rmi.localport",
                "server.rmi.localport=" + rmiPort);
        logger.info("server.rmi.localport：" + rmiPort + "修改完毕");


        //修改:server_port
        logger.info("server_port：" + rmiPort);               //server.rmi.port
        FileReplayUtils.modiyJmeterPropertiesPort(agentjmeterProperties, "server_port",
                "server_port=" + rmiPort);
        logger.info("server_port：" + rmiPort + "修改完毕");

        StringBuilder sb = new StringBuilder();

        String agentjmeterCmm = null;
        sb.append(agentjmeterProperties.replace("jmeter.properties", "jmeter-server"));
        sb.append(" -Djava.rmi.server.hostname=" + hostIP);

        agentjmeterCmm = sb.toString();
        sb.setLength(0);

        //时间与控制区台同步：
        String updateTimeSever = agentUpdateTimeServer;
        String updateTimeResutl = null;
        try {
            updateTimeResutl = commandVariationUtil.commandExecution(updateTimeSever, "").toString();
        } catch (Exception e) {
            System.out.println("jmeter agent 时间同步不成功!   ===>" + updateTimeResutl);
            System.out.println();
        }

        String agentIpPort = hostIP + ":" + rmiPort;


        String runResutl = null;

        //1.杀死对应端口号进程：
        killProcessByPort.killProcessPort(Integer.valueOf(rmiPort));
        //2.杀jmeter
        killJMeterProcess.killJmeter();

        //2.如果是运行用户由把运行用户的runusername写到数据库中：可以知道现在是谁是运行的;
        runResutl = commandVariationUtil.commandExecution(agentjmeterCmm, agentIpPort).toString();


        if (!"".equals(runResutl)) {
            logger.info(runResutl);
         //   logger.info("jmeter agent 启动成功!");
        } else {
            logger.info("jmeter agent 进程存在，不用启动!");
        }
        //    }


    }


    /**
     * 查询数据库ip的对应jmeter的状态：
     */
    public void getIpJmeterStatus() throws Exception{


        Map <String, String> map = new HashMap <>();
        agentSourceIp.getOsNameIp(map);
        String hostIP = map.get("ipaddress");
        System.out.println("hostIP-->" + hostIP);

        String rmiPort = String.valueOf(Integer.valueOf(dubbo_protocol_port) + 1000);

        String agentIpPort = hostIP + ":" + rmiPort;




        //1. 查jmeter进程是否存在，不存在则启动jmeter进程
        Boolean jmeterProcess = killJMeterProcess.queryJmetetProcess();
        if(jmeterProcess == Boolean.FALSE){

            //1.杀jmeter
            killJMeterProcess.killJmeter();
            //2.杀死对应端口号进程：
            killProcessByPort.killProcessPort(Integer.valueOf(rmiPort));

            MenuDefine.STATUS = "INIT" ;
            if (MenuDefine.STATUS.equals("INIT")) {
                logger.info("jmeter-server 第一次启动------>");
                MenuDefine.STATUS = MenuDefine.END_OVER;
                String consumerIp = json.getString("consumerIp");
                logger.info("consumer的ip地址为consumerIp:{}," + consumerIp);
                this.jmeterAgentCheck();

            }

        }


        //2. 如果jmeter进程存在，则查数据库中对应ip的jmeter的状态
        try {
            Map<String, Object> jmeterStatus = agentIpRunStatus.queryStatusAgentInfo(agentIpPort);

            
            String status = (String) jmeterStatus.get(agentIpPort);
            
            if (status != "noipaddress" && MenuAgentStatus.STATUS_MMAP_FAILED.equals(status)) {
                //1.杀jmeter
                killJMeterProcess.killJmeter();
                //2.杀死对应端口号进程：
                killProcessByPort.killProcessPort(Integer.valueOf(rmiPort));
                MenuDefine.STATUS = "INIT" ;
                //2.重启jmeter

                MenuDefine.STATUS = "INIT" ;
                if (MenuDefine.STATUS.equals("INIT")) {
                    logger.info("jmeter-server 第一次启动------>");
                    MenuDefine.STATUS = MenuDefine.END_OVER;
                    String consumerIp = json.getString("consumerIp");
                    logger.info("consumer的ip地址为consumerIp:{}," + consumerIp);
                    this.jmeterAgentCheck();
                }


            }

        }catch (Exception e){
             e.printStackTrace();
            //1.重启jmeter
        }


    }


}
