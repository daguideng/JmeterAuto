package com.atguigu.gmall.Impl.interfaceImpl;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2018-9-28
 */

import com.atguigu.gmall.Impl.perfImpl.ReadWriteReportData;
import com.atguigu.gmall.Impl.perfImpl.ReportStates;
import com.atguigu.gmall.Interface.JsonDataToDataIntel;
import com.atguigu.gmall.common.utils.*;
import com.atguigu.gmall.entity.Interface_current_report;
import com.atguigu.gmall.entity.Interface_history_report;
import com.atguigu.gmall.entity.Jmeter_interface_top5_errors;
import com.atguigu.gmall.service.jmeterinter.JmeterIntercurrentreportServer;
import com.atguigu.gmall.service.jmeterinter.JmeterInterfaceTop5ErrorsServer;
import com.atguigu.gmall.service.jmeterinter.JmeterInterormhistoryreportServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * @Author: dengdagui
 * @Description:fastJson 解析json串
 * @Date: Created in 2018-8-3
 */
@Slf4j
@Component
public class JsonDataToDataInterImpl implements JsonDataToDataIntel {

    @Value("${server.address}")
    private String server_address ;

    @Value("${server.port}")
    private String server_port ;

    @Autowired
    private JmeterInterfaceTop5ErrorsServer jmeterInterfaceTop5ErrorsServer;


    @Autowired
    private JmeterIntercurrentreportServer jmeterIntercurrentreportServer;

    @Autowired
    private ReadWriteReportData readWriteReportData;

    @Autowired
    private JmeterInterormhistoryreportServer jmeterInterormhistoryreportServer;


    @Value("${ReporLinkstUrl.Inter}")
    private String ReporLinkstUrl_Inter;


    //读性能数据并把数据入库：
    public void reportIndex(String jmeterPathBin, String jsFile, String thread, StringBuffer nodeIp, String jtlReportfile, Integer runId, String reportRunTime, String jmeterScriptName, String scriptNamePath, String currentTime ,HttpSession session) throws Exception {

        String jsFileReport = jsFile + "/content/js/dashboard.js";

        jsFileReport = jsFileReport.replace("\\", "/");

        System.out.println("jsFileReport====>" + jsFileReport);


        //精确得到性能测试运行时间,为以后性能测试报告做性能对比提供参考时间：

        StringBuffer runStartEndTime = this.runTime(jtlReportfile);

        String json = null;

        Thread.sleep(600);
        String command = jmeterPathBin + " -g " + jtlReportfile + " -o " + jsFile;
                System.out.println("自动生成报告命令：" + command);
                CommandUtil.commandExecution(command.trim());

        Thread.sleep(400);        


        try {
            File jsfile = new File(jsFileReport);

            //如果  -e -o 没有自动生成报告则用命令强制生成报告!
            if (!jsfile.exists()) {
                //命令自动生成报告:
                command = jmeterPathBin + " -g " + jtlReportfile + " -o " + jsFile;
                System.out.println("自动生成报告命令：" + command);
                CommandUtil.commandExecution(command.trim());

            }

            //预留生成报告时间为:3秒
                Thread.sleep(5000);



            //接口测试正常数据：
            json = readWriteReportData.modiyInterval(jsFileReport.toString(), "statisticsTable");
            System.out.println("json---11>"+json);
          //  json= json.replace("Infinity",null).replace("NaN",null);

            System.out.println("json===22>" + json);
            //接口测试正常数据：
            this.indexDataToReport(json, jsFile, thread, nodeIp, jtlReportfile, runId, reportRunTime, jmeterScriptName, runStartEndTime, session);


            //接口测试异常数据：
            json = readWriteReportData.top5ErrorsBySamplerData(jsFileReport.toString(), "top5ErrorsBySamplerTable");
            System.out.println("jsonerror===>" + json);
            //接口测试异常数据：
            this.indexDataErrorToReport(json, jsFile, thread, jtlReportfile, reportRunTime, scriptNamePath, jmeterScriptName);


        } catch (Exception e) {
            throw new Exception("未生成HTML报告,搜索报告不成功!!!");
        }


    }


    /**
     * 接口测试报告正常数据入库
     */
    public void indexDataToReport(String json, String jsFile, String thread, StringBuffer nodeIp, String jtlReportfile, Integer runId, String reportRunTime, String jmeterScriptName, StringBuffer runStartEndTime, HttpSession session) throws Exception {
        String[] reportData = json.split("\"data\":");

        List <String> list = new ArrayList <>(asList(reportData));

        for (String str : list) {

            String strsub = str.split(", \"isController\"")[0];
            if (!strsub.contains("\"overall\":")) {

                if (strsub.contains("\"Total\"")) {
                    //   System.out.println("Total===>"+strsub);
                    String total = strsub.split("\\[")[1].split("]")[0].replace("\"", "");
                    String[] totalArray = total.split(",");



                    //增加Error超链接：


                    String reportType = "TotalReport";

                    //添加性能测试总的指标：
                    try {
                        indexReport(totalArray, reportType, thread, nodeIp, runStartEndTime, jtlReportfile, jsFile, runId, reportRunTime, jmeterScriptName, session);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    //    System.out.println("Data===>"+strsub);

                    // 各事务值报告指标：
                    String data_index = strsub.split("\\[")[1].split("]")[0];
                    //   System.out.println("data_index===>"+data_index);
                    String[] itermsAraay = data_index.split(",");

                    String reportType = "DetailReport";
                    try {
                        indexReport(itermsAraay, reportType, thread, nodeIp, runStartEndTime, jtlReportfile, jsFile, runId, reportRunTime, jmeterScriptName, session);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }


        //清空内存：
        nodeIp.setLength(0);
        runStartEndTime.setLength(0);
        System.gc();
    }


    /***
     * 接口异常数据入库：
     */
    public void indexDataErrorToReport(String json, String jsFile, String thread, String jtlReportfile, String reportRunTime, String scriptNamePath, String jmeterScriptName) throws Exception {


        //错误数据写入title:
        this.interFaceErrorTitle(json, jmeterScriptName, thread, reportRunTime, jsFile, jtlReportfile);

        //错误数据写入items:
        this.interFaceErrorItems(json, jmeterScriptName, thread, reportRunTime, jsFile, scriptNamePath, jtlReportfile);


    }


    /***
     * 异常数据写入title部
     */
    public void interFaceErrorTitle(String json, String jmeterScriptName, String thread, String reportRunTime, String jsFile, String jtlReportfile) throws UnsupportedEncodingException {

        String strOvalData = json.split(", \"isController\"")[0];
        if (strOvalData.contains("\"Total\"")) {

            String total = strOvalData.split("\"data\": ")[1].replace("&quot;", "\"").replace("quot;", "").replace("[", "").replace("\"", "");


            System.out.println("------->total" + total);
            String[] errHeadTotal = total.replace("\"", "").split(",");


            List <String> HeadTotal = new ArrayList <>(Arrays.asList(errHeadTotal));

            //为 Total进行下载种超链接锚：即点Total  下载：jplpath
            for (String TotalLinck : HeadTotal) {
                if (TotalLinck.equals("Total")) {

                    String lastruntime = DateUtil.date2TimeStamp(reportRunTime.trim(), "yyyy-MM-dd HH:mm:ss");

                    StringBuffer sb1 = new StringBuffer(80);
                    String ReporLinkstUrl = ReporLinkstUrl_Inter.replace("\\", "/");
                    sb1.append(ReporLinkstUrl + "jpllog/" + "initer" + "/" + jmeterScriptName + "/" + lastruntime);
                    TotalLinck = "<a href=" + sb1.toString() + " style=color:blue;text-decoration:none>" + TotalLinck + "</a>";
                    sb1.setLength(0);

                    HeadTotal.set(0, TotalLinck);

                }
            }

            String indexPath = jsFile + "/index.html";

            //写入Total的内容：
            try {

                Jmeter_interface_top5_errors interfaceTotal = new Jmeter_interface_top5_errors(jmeterScriptName, thread, reportRunTime, HeadTotal, null, jtlReportfile, indexPath, 0);
                jmeterInterfaceTop5ErrorsServer.insertInterfaceTop5Error(interfaceTotal);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }


    /***
     * 异常数据写入Items部
     */
    public void interFaceErrorItems(String json, String jmeterScriptName, String thread, String reportRunTime, String jsFile, String scriptNamePath, String jtlReportfile) throws IOException {

        if (json.contains("\"items\"")) {

            // 各事务值报告指标：
            String[] data_index = json.split("\"items\":")[1].split("\\{\"data\":");
            String[] keyErrorArrayListIndex = null;
            String url = null;
            for (int x = 1; x < data_index.length; x++) {

                if (!data_index[x].contains("[]") && !data_index[x].equals("\\[")) {
                    String keyErrorIndex = data_index[x].split(", \"isController\":")[0].replace("\"", "");

                    keyErrorArrayListIndex = keyErrorIndex.replace("\\/", "/").replace("&quot;", "\"").replace("quot;", "").replace("[", "").replace("\"", "").replace(" ", "").split(",");

                    System.out.println("keyErrorIndex---->" + keyErrorIndex);
                    System.out.println("keyErrorArrayListIndex.size()---->" + keyErrorArrayListIndex.length);

                    //事物对应URL:
                    keyErrorIndex = keyErrorArrayListIndex[0].replace("\"", "").trim();
                    url = FileReplayUtils.rransactionUrl(scriptNamePath, keyErrorIndex, "\"HTTPSampler.path\">");


                    url = URLDecoder.decode(url, "UTF-8");


                    List <String> Erroritems = new ArrayList <>(Arrays.asList(keyErrorArrayListIndex));

                    String indexPath = jsFile + "/index.html";


                    //写入错误事务的详细情况：
                    try {

                        Jmeter_interface_top5_errors interfaceTotal = new Jmeter_interface_top5_errors(jmeterScriptName, thread, reportRunTime, Erroritems, url, jtlReportfile, indexPath, 0);
                        jmeterInterfaceTop5ErrorsServer.insertInterfaceTop5Error(interfaceTotal);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }


            }


        }


    }


    /**
     * 如果只是用于程序中的格式化数值然后输出，那么这个方法还是挺方便的。
     * 应该是这样使用：System.out.println(String.format("%.3f", d));
     *
     * @param d
     * @return
     */
    public String formatDouble(double d) {
        String str = String.format("%.2f", d);
        return str;
    }

    public String getType(Object object) {
        String typeName = object.getClass().getName();
        int length = typeName.lastIndexOf(".");
        String type = typeName.substring(length + 1);
        return type;
    }


    public StringBuffer runTime(String jtlReportfile) {

        StringBuffer timeBuffer = new StringBuffer();
        String filstLine = GetFileFirstLastLine.getFileSecondLine(jtlReportfile).split(",")[0];
        String lastLine = GetFileFirstLastLine.getFileLastLine(jtlReportfile).split(",")[0];

        //时间转换：
        String startTime = TimeStampUtils.stampToDate(filstLine);
        String endTime = TimeStampUtils.stampToDate(lastLine);

        timeBuffer.append(startTime);
        timeBuffer.append(",");
        timeBuffer.append(endTime.trim());


        return timeBuffer;
    }


    public void indexReport(String[] Array, String reportType, String thread, StringBuffer nodeIp, StringBuffer runStartEndTime, String jtlReportfile, String jsFile, Integer runId, String reportRunTime, String jmeterScriptName, HttpSession session) throws Exception {

        DecimalFormat df = new DecimalFormat("#.00");

        // 直接使用Map存储数据，避免索引错位问题
        Map<String, Object> dataMap = new HashMap<>();

        //添加 uploadid
        dataMap.put("uploadid", runId);

        //添加 lastrunttime
        dataMap.put("lastruntime", reportRunTime);

        //脚本名 网络下载
        StringBuffer sb = new StringBuffer(70);
        String ReporLinkstUrl = ReporLinkstUrl_Inter.replace("\\", "/");

        System.out.println("-------->jtlReportfile"+jtlReportfile) ;
        String simpleReportUrl = jtlReportfile.split("ReportResultDir/")[1];
        sb.append(ReporLinkstUrl);
        sb.append(simpleReportUrl);

        //添加报告的超链接
        String reportHtml = jsFile + "/index.html" ;
        reportHtml = reportHtml.split("ReportResultDir/")[1];
        String urlReportPath = "http://"+ server_address + ":"+server_port + "/report"+"/" + reportHtml ;
        String jmeterScriptName_linck = "<a href="+ urlReportPath + ">"+jmeterScriptName +"</a>" ;

        System.out.println("simpleReportUrl===========>" + simpleReportUrl);
        // 添加脚本名的jtl.log的超链接
        dataMap.put("scriptname", jmeterScriptName_linck);
        sb.setLength(0);

        //线程数
        dataMap.put("threads", thread);

        String jmeterScriptNameLabel = null;
        String transactionName = null;

        for (int i = 0; i < Array.length; i++) {
            //添加事务名
            if (i == 0) {
                StringBuffer sb1 = new StringBuffer(70);
                transactionName = Array[0].replace("\"", "").replace(" ", "");
                String lastruntime = DateUtil.date2TimeStamp(reportRunTime.trim(), "yyyy-MM-dd HH:mm:ss");
                sb1.append(ReporLinkstUrl + "inter" + "/" + jmeterScriptName + "/" + lastruntime);
                jmeterScriptNameLabel = "<a href=" + sb1.toString() + " style=color:blue;text-decoration:none>" + transactionName + "</a>";

                System.out.println("jmeterScriptNameLabel-------->" + jmeterScriptNameLabel);

                if (jmeterScriptNameLabel.trim().contains("Total")) {
                    dataMap.put("label", jmeterScriptNameLabel.trim());
                    sb1.setLength(0);
                } else {
                    dataMap.put("label", Array[0].trim().replace("\"", "").replace(" ", ""));
                }
            }
            if (i != 0) {
                //对double值进行四舍五入，保留两位小数
                String sumeValue = null;
                double value = 0.000F;
                if (Array[i].trim().equalsIgnoreCase("Infinity")) {
                    sumeValue = Array[i].trim();
                } else if (Array[i].trim().equalsIgnoreCase("NaN")) {
                    sumeValue = Array[i].trim();
                } else {
                    try {
                        value = Double.valueOf(df.format(Double.valueOf(Array[i].trim())));
                        sumeValue = formatDouble(value);
                    } catch (Exception e) {
                        sumeValue = Array[i].trim();
                    }
                }

                //对于 Error% 所得到值加上%
                if (i == 3) {
                    String errorValue = Array[i].replace("\"", "").replace(" ", "");
                    if (!errorValue.equals("0.0")) {
                        errorValue = this.formatDouble(Double.valueOf(errorValue));
                        StringBuffer sb1 = new StringBuffer(80);
                        String lastruntime = DateUtil.date2TimeStamp(reportRunTime.trim(), "yyyy-MM-dd HH:mm:ss");
                        ReporLinkstUrl = ReporLinkstUrl_Inter.replace("\\", "/");
                        sb1.append(ReporLinkstUrl + "error/" + transactionName + "/inter" + "/" + jmeterScriptName + "/" + lastruntime);
                        sumeValue = "<a href=" + sb1.toString() + " style=color:red;text-decoration:none>" + errorValue + "</a>";
                        sb1.setLength(0);
                    } else {
                        sumeValue = errorValue;
                    }
                    dataMap.put("error", sumeValue);
                } else if (i == 1) {
                    dataMap.put("samples", sumeValue);
                } else if (i == 2) {
                    dataMap.put("ko", sumeValue);
                } else if (i == 4) {
                    dataMap.put("average", sumeValue);
                } else if (i == 5) {
                    dataMap.put("min", sumeValue);
                } else if (i == 6) {
                    dataMap.put("max", sumeValue);
                } else if (i == 7) {
                    // median值在数组中的位置是7（在max和thpct90之间）
                    dataMap.put("median", sumeValue);
                } else if (i == 8) {
                    dataMap.put("thpct90", sumeValue);
                } else if (i == 9) {
                    dataMap.put("thpct95", sumeValue);
                } else if (i == 10) {
                    dataMap.put("thpct99", sumeValue);
                } else if (i == 11) {
                    dataMap.put("throughput", sumeValue);
                } else if (i == 12) {
                    dataMap.put("received", sumeValue);
                } else if (i == 13) {
                    dataMap.put("sent", sumeValue);
                }
            }
        }

        // 添加节点ip
        String ipPort = nodeIp.toString();
        dataMap.put("ip", ipPort.substring(0, ipPort.length() - 1));

        //添加 开始运行时间
        String runTime = runStartEndTime.toString();
        dataMap.put("starttime", runTime.split(",")[0]);

        //添加 结束运行时间
        dataMap.put("endtime", runTime.split(",")[1].trim());

        // 添加jtlpath和indexpath
        dataMap.put("jtlpath", jtlReportfile);
        dataMap.put("indexpath", jsFile + "/index.html");

        // 添加state
        dataMap.put("state", ReportStates.INSERT_0);

        //入库
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(dataMap);

        System.out.println("888888888--->"+json);

        // 使用Map构造函数创建对象
        Interface_current_report listData = new Interface_current_report(dataMap);
        // 使用Map构造函数创建Interface_history_report对象
        Interface_history_report historylist = new Interface_history_report(dataMap);
        log.info("listData===>" + listData.getAverage());
        log.info("historylist===>" + historylist.getAverage());
        try {
            jmeterIntercurrentreportServer.insertStatistics(listData);
            jmeterInterormhistoryreportServer.jmeterIntermhistoryreportMapper(historylist);
            log.info("线程：" + thread + "运行结果保存数据成功!!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {

            //释放内存：
            dataMap.clear();

        }


    }

}