package com.atguigu.gmall.controller.filedownload;

import com.atguigu.gmall.Impl.interfaceImpl.JmeterLogInfo;
import com.atguigu.gmall.Interface.SendEmailReportIntel;
import com.atguigu.gmall.common.bean.response.Result;
import com.atguigu.gmall.common.utils.DateUtil;
import com.atguigu.gmall.controller.inter.JmeterInterReportImpl;
import com.atguigu.gmall.service.jmeterinter.JmeterInterfaceTop5ErrorsServer;
import com.atguigu.gmall.service.jmeterperf.JmeterPerfTop5ErrorServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2019-5-21
 */
@Slf4j
@RestController
public class FileDownLoad {



    @Resource(name = "sendEmailPerfReportImpl")
    private SendEmailReportIntel sendEmailReportIntel;

    @Resource(name = "sendEmailInterReportImpl")
    private SendEmailReportIntel sendEmailInterReportImpl;


    @Autowired
    private JmeterInterfaceTop5ErrorsServer jmeterInterfaceTop5ErrorsServer;

    @Autowired
    private JmeterPerfTop5ErrorServer jmeterPerfTop5ErrorServer;


    @Autowired
    private JmeterInterReportImpl jmeterInterReportImpl;


    @Value("${jmeterStore}")
    private String  jmeterStore ;


    @Value("${jmeterStore.Inter}")
    private String jmeterStore_Inter;

    int countdown = 0;


    /**
     * 下载脚本时超链接:
     *
     * @param uploadName
     * @param scriptNameFile
     * @param res
     */
    @RequestMapping(value = "/mars/images/{perf}/jmeterStore/{uploadName}/{scriptNameFile}", method = RequestMethod.GET)
    public void uploadAgentDownload(@PathVariable("perf") String perf,
                                         @PathVariable("uploadName") String uploadName,
                                         @PathVariable("scriptNameFile") String scriptNameFile,
                                         HttpServletResponse res) {



        String scriptNameDir = null;
        scriptNameFile = scriptNameFile + ".zip";
        if (perf.equals("perf")) {
            scriptNameDir = jmeterStore + uploadName + "/";
        } else if (perf.equals("initer")) {
            scriptNameDir = jmeterStore_Inter + uploadName + "/";
        }
        res.setHeader("content-type", "application/octet-stream");
        res.setContentType("application/octet-stream");
        res.setHeader("Content-Disposition", "attachment;filename=" + scriptNameFile);
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;


        countdown++;

        try {
            os = res.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(new File(scriptNameDir
                    + scriptNameFile)));
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }



        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        log.info("负载机下载测试脚本数量为countdown:{}", countdown);



    }


    /**
     * 查看报告超链接时用时详细报告时直接从数据库中查询生成数据报告
     *
     * @param scriptname
     * @param lastruntime
     * @param
     */

    //<a href=http://192.168.88.217:8089/mars/ReportResultDir/inter/test/1645251465 style=color:blue;text-decoration:none>Total</a>

    /** 
    @RequestMapping(value = "/mars/{ReportResultDir}/{type}/{scriptname}/{lastruntime}", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public StringBuffer DownloadReport(
            //    @PathVariable(value = "mars", required = true) String mars,
            @PathVariable(value = "ReportResultDir", required = true) String ReportResultDir,
            @PathVariable(value = "type", required = true) String type,
            @PathVariable(value = "scriptname", required = true) String scriptname,
            @PathVariable(value = "lastruntime", required = true) String lastruntime,
            HttpServletRequest request, HttpServletResponse response) throws Exception {


        request.setCharacterEncoding("utf-8");
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        response.setCharacterEncoding("utf-8");




        System.out.printf("lastruntime--->"+lastruntime);
        StringBuffer sb = new StringBuffer(1000);
        try {

            //根据type的类型来判断是否是生成性能测试报告还是接口性能测试报告：
            if (type.equals("perf")) {

                lastruntime = DateUtil.timeStamp2Date(lastruntime,null);

                return sendEmailReportIntel.toDetailReport(lastruntime, scriptname, sb);
            } else if (type.equals("inter")) {
                // 需要提供一个有效的id参数，这里使用默认值或从其他地方获取
                String defaultId = "0"; // 或者根据业务逻辑获取实际的id
                String result = jmeterInterReportImpl.simpleInterReportList(defaultId, lastruntime, response);
                System.out.println("result---->"+result);
                 response.getWriter().print(result);
               // return sendEmailInterReportImpl.toDetailReport(lastruntime, scriptname, sb);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    ***/


    /**
     * 查看异常超链接Total报告
     *
     * @param scriptname
     * @param lastruntime
     * @param
     */

    @RequestMapping(value = "/mars/{ReportResultDir}/error/{transactionName}/{type}/{scriptname}/{lastruntime}", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String DownloadErrorReport(

            @PathVariable(value = "type", required = true) String type,
            @PathVariable(value = "scriptname", required = true) String scriptname,
            @PathVariable(value = "lastruntime", required = true) String lastruntime,
            @PathVariable(value = "ReportResultDir", required = true) String ReportResultDir,
            @PathVariable(value = "transactionName", required = true) String transactionName,


            HttpServletRequest request, HttpServletResponse response) throws Exception {


        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        response.setContentType("text/html;charset=utf-8");


        StringBuffer sb = new StringBuffer(1000);
        try {

            lastruntime = DateUtil.timeStamp2Date(lastruntime,null);

            System.out.printf("lastruntime--->"+lastruntime);

            //根据type的类型来判断是否是生成性能测试报告还是接口性能测试报告：
            if (type.equals("perf")) {


                sendEmailReportIntel.toErrorDetailReport(lastruntime, scriptname, transactionName, sb);
            } else if (type.equals("inter")) {
                sendEmailInterReportImpl.toErrorDetailReport(lastruntime, scriptname, transactionName, sb);

            }

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    /***
     * 为点击错误页的Total可以下载jpllog文件：
     */
    @RequestMapping(value = "/mars/{ReportResultDir}/jpllog/{type}/{jmeterScriptName}/{lastruntime}", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public void DownloadJplLog(
            @PathVariable(value = "ReportResultDir", required = true) String ReportResultDir,
            @PathVariable(value = "type", required = true) String type,
            @PathVariable(value = "jmeterScriptName", required = true) String jmeterScriptName,
            @PathVariable(value = "lastruntime", required = true) String lastruntime,
            HttpServletResponse res) throws Exception {

        res.setCharacterEncoding("utf-8");
        res.setHeader("Content-type", "text/html;charset=UTF-8");
        res.setContentType("text/html;charset=utf-8");


        lastruntime = DateUtil.timeStamp2Date(lastruntime,null);
        System.out.printf("lastruntime--->"+lastruntime);



        String jtlReportfile = null;
        String scriptNameFile = null;
        if (type.equals("perf")) {
            jtlReportfile = jmeterPerfTop5ErrorServer.interLogJplPath(lastruntime, jmeterScriptName, "Total");

        } else if (type.equals("initer")) {
            jtlReportfile = jmeterInterfaceTop5ErrorsServer.interLogJplPath(lastruntime, jmeterScriptName, "Total");

        }

        System.out.println("jtlReportfile--->" + jtlReportfile);
        String[] jplname = jtlReportfile.split("/");
        scriptNameFile = jplname[jplname.length - 1];

        res.setHeader("content-type", "application/octet-stream");
        res.setContentType("application/octet-stream");
        res.setHeader("Content-Disposition", "attachment;filename=" + scriptNameFile);
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;

        countdown++;

        try {
            os = res.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(new File(jtlReportfile)));
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        log.info("负载机下载测试脚本数量为countdown:{}", countdown);


    }


    /**
     * 下载jmeterLog查未生成报告的原因:
     *
     * @param ReportResultDir
     * @param res
     */
    @RequestMapping(value = "/mars/{ReportResultDir}/why", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public StringBuffer uploadAgentDownload(
            @PathVariable("ReportResultDir") String ReportResultDir,
            HttpServletResponse res) throws Exception {


        StringBuffer sbf = new StringBuffer(2000);

        Boolean downWhy = true;

        if (downWhy == true) {  //文件可以下载:


            try {
                if (null == JmeterLogInfo.getmapLogInfoPath().split("/")) {
                }
            }catch (Exception e){
                log.error("请耐心等待,节点正在运行,不明白请联系管理员!");
                sbf.append("请耐心等待,节点正在运行,不明白请联系管理员!");
                return sbf ;
            }

            //jmeterLog日志的绝对路径：
            String[] jmeterLogPath = JmeterLogInfo.getmapLogInfoPath().split("/");
            String scriptNameFile = jmeterLogPath[jmeterLogPath.length - 1];

            res.setHeader("content-type", "application/octet-stream");
            res.setContentType("application/octet-stream");
            res.setHeader("Content-Disposition", "attachment;filename=" + scriptNameFile);
            byte[] buff = new byte[1024];
            BufferedInputStream bis = null;
            OutputStream os = null;


            countdown++;

            try {
                os = res.getOutputStream();
                bis = new BufferedInputStream(new FileInputStream(new File(JmeterLogInfo.getmapLogInfoPath())));
                int i = bis.read(buff);
                while (i != -1) {
                    os.write(buff, 0, buff.length);
                    os.flush();
                    i = bis.read(buff);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
            log.info("负载机下载测试脚本数量为countdown:{}", countdown);

            sbf.append("请下载附件,并查看详细不通过的原因.....");

            return sbf;

        } else {   //不下载,只显示：

            InputStream sourceFile = null;
            BufferedReader bufReader = null;

            try {

                //Jmeter的Log信息内容：
                File file = new File(JmeterLogInfo.getmapLogInfoPath());
                sourceFile = new FileInputStream(file);
                bufReader = new BufferedReader(new InputStreamReader(sourceFile, "utf-8"));

                for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {

                    if(tmp.equals("\r") || tmp.equals("\n")) {

                        sbf.append("\r\n");
                        sbf.append(System.getProperty("line.separator"));
                    }else{
                        sbf.append(tmp);
                    }

                }




            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                bufReader.close();

            }

        }

        return sbf;
    }




    @RequestMapping("/webdownloadFile")
    private Result<?> webdownloadFile(@RequestParam("filepath") String filepath, HttpServletResponse response){

        Map<String ,Object> map = new HashMap <>();

        filepath = filepath.replace("\\","/");
        //被下载的文件在服务器中的路径,
        String downloadFilePath = filepath.substring(0,filepath.lastIndexOf("/"))+"/"  ;
        //被下载文件的名称
        String fileName = (new File(filepath)).getName();

        File file = new File(downloadFilePath);
        if (file.exists()) {
            response.setContentType("application/force-download");// 设置强制下载不打开            
            response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream outputStream = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    outputStream.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                map.put("code",0);
            } catch (Exception e) {
                map.put("code",-1);
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        Result <Map<String, Object>> result = new Result <>();
        result.setData(map);

        return result;
    }

}


