package com.atguigu.gmall.Impl.perfImpl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall.Interface.JsonDataToDataIntel;
import com.atguigu.gmall.common.utils.DateUtil;
import com.atguigu.gmall.common.utils.FastJsonUtil;
import com.atguigu.gmall.common.utils.CommandUtil;
import com.atguigu.gmall.common.utils.FileReplayUtils;
import com.atguigu.gmall.common.utils.GetFileFirstLastLine;
import com.atguigu.gmall.common.utils.TimeStampUtils;
import com.atguigu.gmall.entity.Jmeter_perf_top5_errors;
import com.atguigu.gmall.entity.Jmeter_perfor_current_report;
import com.atguigu.gmall.entity.Jmeter_perfor_history_report;
import com.atguigu.gmall.entity.Statistics;
import com.atguigu.gmall.service.jmeterperf.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.alibaba.druid.pool.DruidDataSource;
import javax.sql.DataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.*;

import static com.atguigu.gmall.common.utils.FastJsonUtil.bean2Json;
import static com.atguigu.gmall.common.utils.FastJsonUtil.json2Map;

/**
 * @Author: dengdagui
 * @Description:fastJson 解析json串
 * @Date: Created in 2018-8-3
 */
@Component
@Slf4j
public class JsonDataToDataPerfImpl implements JsonDataToDataIntel {



    @Value("${server.address}")
    private String server_address ;

    @Value("${server.port}")
    private String server_port ;


    @Autowired
    private StatisticsServer statisticsServer;

    @Autowired
    private ReadWriteReportData readWriteReportData;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private JmeterperformhistoryreportServer jmeterperformhistoryreportServer;

    @Autowired
    private JmeterperformcurrentreportServer jmeterperformcurrentreportServer;



    @Autowired
    private PerfConfigServer perfConfigServer;


    @Autowired
    private UploadScriptServer uploadScriptServer;

    @Autowired
    private JmeterPerfTop5ErrorServer jmeterPerfTop5ErrorServer;

    @Value("${ReporLinkst.Url}")
    private String ReporLinkst_Url ;

    int count = 0;
      
    //读性能数据并把数据入库：
    public void reportIndex(String jmeterPathBin, String jsFile, String thread, StringBuffer nodeIp, String jtlReportfile, Integer runId, String reportRunTime, String jmeterScriptName, String scriptNamePath,String perfstarttime, HttpSession session) throws Exception {

        Thread.sleep(3000);

         //命令自动生成报告:
        String command = jmeterPathBin + " -g " + jtlReportfile + " -o " + jsFile;
        System.out.println("自动生成报告命令：" + command);
        CommandUtil.commandExecution(command);

        Thread.sleep(1000);

        // 初始化DecimalFormat对象
        DecimalFormat df = new DecimalFormat("#.00");

        String jsFileReport = jsFile + "/content/js/dashboard.js";

        jsFileReport = jsFileReport.replace("\\", "/");


        log.info("jsFileReport====>" + jsFileReport);

        //精确得到性能测试运行时间,为以后性能测试报告做性能对比提供参考时间：

        StringBuffer runStartEndTime = this.runTime(jtlReportfile);

        String json = null;

        try {
            File jsfile = new File(jsFileReport);

      //如果  -e -o 没有自动生成报告则用命令强制生成报告!
            if (!jsfile.exists()) {
                Thread.sleep(3000);

                // 重新生成报告命令（解决变量未定义问题）
                // 重新生成报告命令（解决变量未定义问题）
                command = jmeterPathBin + " -g " + jtlReportfile + " -o " + jsFile;
                System.out.println("自动生成报告命令：" + command);
                CommandUtil.commandExecution(command);
                //命令自动生成报告:
                command = jmeterPathBin + " -g " + jtlReportfile + " -o " + jsFile;
                System.out.println("自动生成报告命令：" + command);
                CommandUtil.commandExecution(command);

            }

            //正文报告指标：
            json = readWriteReportData.modiyInterval(jsFileReport.toString(), "statisticsTable");
            System.out.println("json===>" + json);

            //入库性能正常报告指标：
            this.indexDataToReport(json, jsFile, thread, nodeIp, jtlReportfile, runId, reportRunTime, jmeterScriptName, runStartEndTime,perfstarttime, session);


            //异常报告指标：
            json = readWriteReportData.top5ErrorsBySamplerData(jsFileReport.toString(), "top5ErrorsBySamplerTable");
            System.out.println("errosjson===>" + json);

            //入库性能异常报告指标
            this.indexDataErrorToReport(json, jsFile, thread, jtlReportfile, reportRunTime, jmeterScriptName, scriptNamePath);


        } catch (Exception e) {
            
                 log.error("未生成HTML报告,搜索报告不成功!!!",e);
        
                 throw new Exception("未生成HTML报告,搜索报告不成功!!!");
        }


    }


    /***
     * 性能数据入库
     */
    public void indexDataToReport(String json, String jsFile, String thread, StringBuffer nodeIp, String jtlReportfile, Integer runId, String reportRunTime, String jmeterScriptName, StringBuffer runStartEndTime,String perfstarttime, HttpSession session) throws Exception {

        Map <Object, Object> hashMap = new HashMap();

        hashMap = json2Map(json);

        String overall = hashMap.get("overall").toString();

        JSONObject dataTotal = JSONObject.parseObject(overall);


        String total = dataTotal.getString("data").split("\\[")[1].split("]")[0];
        String[] totalArray = total.split(",");


        //添加性能测试总的指标：
        indexReport(totalArray, thread, nodeIp, runStartEndTime, jtlReportfile, jsFile, runId, reportRunTime, jmeterScriptName,perfstarttime, session);
        JSONArray jArray = new JSONArray((List <Object>) hashMap.get("items"));


        //添加性能测试各事务指标:
        for (int x = 0; x < jArray.toArray().length; x++) {

            String reportIndex = jArray.toArray()[x].toString();
            JSONObject reportIndex_json = JSONObject.parseObject(reportIndex);
            String data_index = reportIndex_json.getString("data").split("\\[")[1].split("]")[0];


            // 各事务值报告指标：
            String[] itermsAraay = data_index.split(",");
            indexReport(itermsAraay, thread, nodeIp, runStartEndTime, jtlReportfile, jsFile, runId, reportRunTime, jmeterScriptName,perfstarttime, session);

        }


        //清空内存：
        nodeIp.setLength(0);
        runStartEndTime.setLength(0);
      //  System.gc();
    }


    /***
     * 性能异常数据入库
     */
    public void indexDataErrorToReport(String json, String jsFile, String thread, String jtlReportfile, String reportRunTime, String jmeterScriptName, String scriptNamePath) throws Exception {

        //异常数据写入title部
        this.perfErrorTitle(json, jmeterScriptName, thread, reportRunTime, jsFile, jtlReportfile);

        //异常数据写入Items部
        this.perfErrorItems(json, jmeterScriptName, thread, reportRunTime, jsFile, scriptNamePath, jtlReportfile);

    }


    /***
     * 异常数据写入title部
     */
    public void perfErrorTitle(String json, String jmeterScriptName, String thread, String reportRunTime, String jsFile, String jtlReportfile) throws UnsupportedEncodingException {

        String strOvalData = json.split(", \"isController\"")[0];
        if (strOvalData.contains("\"Total\"")) {

            String total = strOvalData.split("\"data\": ")[1].replace("&quot;", "\"").replace("quot;", "").replace("[", "").replace("\"", "");


            String[] errHeadTotal = total.split(",");


            List <String> HeadTotal = new ArrayList <>(Arrays.asList(errHeadTotal));

            //为 Total进行下载种超链接锚：即点Total  下载：jplpath
            for (String TotalLinck : HeadTotal) {
                if (TotalLinck.equals("Total")) {

                    String lastruntime = DateUtil.date2TimeStamp(reportRunTime.trim(), "yyyy-MM-dd HH:mm:ss");

                    StringBuffer sb1 = new StringBuffer(80);
                    String ReporLinkstUrl = ReporLinkst_Url.replace("\\", "/");
                    sb1.append(ReporLinkstUrl + "jpllog/" + "perf" + "/" + jmeterScriptName + "/" + lastruntime);
                    TotalLinck = "<a href=" + sb1.toString() + " style=color:blue;text-decoration:none>" + TotalLinck + "</a>";
                    sb1.setLength(0);

                    HeadTotal.set(0, TotalLinck);

                }
            }

            String indexPath = jsFile + "/index.html";

            //写入Total的内容：
            try {

                Jmeter_perf_top5_errors perfTotal = new Jmeter_perf_top5_errors(jmeterScriptName, thread, reportRunTime, HeadTotal, null, jtlReportfile, indexPath, 0);
                jmeterPerfTop5ErrorServer.JmeterPerfTop5Error(perfTotal);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }


    /***
     * 异常数据写入Items部
     */
    public void perfErrorItems(String json, String jmeterScriptName, String thread, String reportRunTime, String jsFile, String scriptNamePath, String jtlReportfile) throws IOException {

        if (json.contains("\"items\"")) {

            String url = null;

            // 各事务值报告指标：
            String[] data_index = json.split("\"items\":")[1].split("\\{\"data\":");
            String[] keyErrorArrayListIndex = null;
            for (int x = 1; x < data_index.length; x++) {


                if (!data_index[x].contains("[]") && !data_index[x].equals("\\[")) {
                    String keyErrorIndex = data_index[x].split(", \"isController\":")[0].replace(" ", "").replace("\"", "");


                    keyErrorArrayListIndex = keyErrorIndex.replace("\\/", "/").replace("&quot;", "\"").replace("quot;", "").replace("[", "").replace(" ", "").replace("\"", "").split(",");


                    //事物对应URL:
                    keyErrorIndex = keyErrorArrayListIndex[0].replace("\"", "").trim().replace("\"", "");
                    url = FileReplayUtils.rransactionUrl(scriptNamePath, keyErrorIndex, "\"HTTPSampler.path\">");

                    List <String> Erroritems = new ArrayList <>(Arrays.asList(keyErrorArrayListIndex));

                    String indexPath = jsFile + "/index.html";


                    //写入错误事务的详细情况：
                    try {

                        Jmeter_perf_top5_errors perfitems = new Jmeter_perf_top5_errors(jmeterScriptName, thread, reportRunTime, Erroritems, url, jtlReportfile, indexPath, 0);
                        jmeterPerfTop5ErrorServer.JmeterPerfTop5Error(perfitems);

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
        
        try {
            // 参数校验
            if (jtlReportfile == null || jtlReportfile.trim().isEmpty()) {
                log.error("JTL报告文件路径不能为空");
                return timeBuffer;
            }
            
            // 获取文件第二行和最后一行
            String firstLine = GetFileFirstLastLine.getFileSecondLine(jtlReportfile);
            String lastLine = GetFileFirstLastLine.getFileLastLine(jtlReportfile);
            
            // 检查行内容是否为空
            if (firstLine == null || firstLine.trim().isEmpty() || lastLine == null || lastLine.trim().isEmpty()) {
                log.error("JTL文件内容格式不正确，无法获取有效数据行");
                return timeBuffer;
            }
            
            // 分割行内容并获取时间戳
            String[] firstLineParts = firstLine.split(",");
            String[] lastLineParts = lastLine.split(",");
            
            if (firstLineParts.length == 0 || lastLineParts.length == 0) {
                log.error("JTL文件内容格式不正确，无法获取时间戳数据");
                return timeBuffer;
            }
            
            String startTimeStamp = firstLineParts[0];
            String endTimeStamp = lastLineParts[0];
            
            // 时间转换
            String startTime = TimeStampUtils.stampToDate(startTimeStamp);
            String endTime = TimeStampUtils.stampToDate(endTimeStamp);
            
            timeBuffer.append(startTime);
            timeBuffer.append(",");
            timeBuffer.append(endTime);
        } catch (Exception e) {
            log.error("处理JTL文件时间信息时发生异常: {}", e.getMessage(), e);
        }

        return timeBuffer;
    }


    public void indexReport(String[] Array, String thread, StringBuffer nodeIp, StringBuffer runStartEndTime, String jtlReportfile, String jsFile, Integer runId, String reportRunTime, String jmeterScriptName, String  perfstarttime, HttpSession session) throws Exception {

        DecimalFormat df = new DecimalFormat("#.00");

        Map<String, Object> dataMap = new HashMap<>();

        //添加 uploadid:
        dataMap.put("uploadid", runId);

        //添加 性能起始时间：
        dataMap.put("perfstarttime", perfstarttime);

        //添加 lastruntime:
        dataMap.put("lastruntime", reportRunTime);

        //添加报告的超链接:
        String reportHtml = jsFile + "/index.html" ;
        reportHtml = reportHtml.split("ReportResultDir/")[1];
        String urlReportPath = "http://"+ server_address + ":"+server_port + "/report"+"/" + reportHtml ;
        String jmeterScriptName_linck = "<a href=\""+ urlReportPath + "\">"+jmeterScriptName +"</a>" ;
        dataMap.put("scriptNameLink", jmeterScriptName_linck);  //得到脚本名的jtl.log的超链接

        //线程数：
        dataMap.put("threadCount", thread);

        String transactionName = null;
        String jmeterScriptNameTotal = null;
        String errorLick = null;

        // 初始化未从JTL获取的必填字段为空字符串
        dataMap.put("ko", "");
        dataMap.put("min", "");
        dataMap.put("max", "");
        dataMap.put("thpct90", "");
        dataMap.put("thpct95", "");
        dataMap.put("thpct99", "");
        dataMap.put("received", "0");
        dataMap.put("sent", "0");

        // 添加去重标记
        String uniqueKey = jmeterScriptName + ":" + (transactionName != null ? transactionName : "") + ":" + reportRunTime;
        dataMap.put("uniqueKey", uniqueKey);

        // 记录JTL数据数组内容，用于调试
        System.out.println("JTL数据数组长度: " + Array.length);
        log.info("JTL数据数组长度: {}", Array.length);
        for (int j = 0; j < Array.length; j++) {
            System.out.println("JTL数据[" + j + "]: " + Array[j]);
            log.info("JTL数据[{}]: {}", j, Array[j]);
        }

        // 从JTL文件直接解析received和sent字段
        try {
            File jtlFile = new File(jtlReportfile);
            if (jtlFile.exists() && jtlFile.isFile()) {
                // 流式处理，避免一次性加载整个文件到内存
                try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(jtlFile), "UTF-8"))) {
                    // 读取标题行
                    String headerLine = br.readLine();
                    if (headerLine != null) {
                        String[] headers = headerLine.split(",");
                        int bytesIndex = -1;  // received对应bytes
                        int sentBytesIndex = -1;  // sent对应sentBytes

                        // 查找字段索引
                        for (int i = 0; i < headers.length; i++) {
                            if (headers[i].trim().equals("bytes")) {
                                bytesIndex = i;
                            } else if (headers[i].trim().equals("sentBytes")) {
                                sentBytesIndex = i;
                            }
                        }

                        // 如果找到字段，逐行处理数据
                        if (bytesIndex != -1 || sentBytesIndex != -1) {
                            long totalBytes = 0;
                            long totalSentBytes = 0;
                            int validLines = 0;
                            String line;

                            while ((line = br.readLine()) != null) {
                                String[] parts = line.split(",");

                                if (parts.length > Math.max(bytesIndex, sentBytesIndex)) {
                                    try {
                                        if (bytesIndex != -1 && !parts[bytesIndex].trim().isEmpty()) {
                                            totalBytes += Long.parseLong(parts[bytesIndex].trim());
                                        }
                                        if (sentBytesIndex != -1 && !parts[sentBytesIndex].trim().isEmpty()) {
                                            totalSentBytes += Long.parseLong(parts[sentBytesIndex].trim());
                                        }
                                        validLines++;
                                    } catch (NumberFormatException e) {
                                        log.error("解析数字失败: {}", e.getMessage());
                                    }
                                }
                            }

                            // 计算平均值并设置到dataMap
                            if (validLines > 0) {
                                dataMap.put("received", String.valueOf(totalBytes / validLines));
                                dataMap.put("sent", String.valueOf(totalSentBytes / validLines));
                            } else {
                                dataMap.put("received", "0");
                                dataMap.put("sent", "0");
                            }
                        }
                    }
                } catch (IOException e) {
                    log.error("读取JTL文件失败: {}", e.getMessage());
                }
            } else {
                log.error("JTL文件不存在或不是一个文件: {}", jtlReportfile);
            }
        } catch (Exception e) {
            log.error("处理JTL文件时发生异常: {}", e.getMessage());
        }

        // 修复指标映射逻辑，基于dashboard.js数据格式重新映射
        // 映射索引已根据dashboard.js文件中的统计表格列顺序更新
        // 表格列顺序: Label, #Samples, FAIL, Error %, Average, Min, Max, Median, 90th pct, 95th pct, 99th pct, Transactions/s, Received, Sent
        for (int i = 0; i < Array.length; i++) {
            //添加事务名:
            if (i == 0) {
                transactionName = Array[0].replace("\"", "");
                StringBuffer sb1 = new StringBuffer();
                String lastruntime = DateUtil.date2TimeStamp(reportRunTime.trim(), "yyyy-MM-dd HH:mm:ss");
                String ReporLinkstUrl = ReporLinkst_Url.replace("\\", "/");
                sb1.append(ReporLinkstUrl + "perf" + "/" + jmeterScriptName + "/" + lastruntime);
                jmeterScriptNameTotal = "<a href=" + sb1.toString() + " style=color:blue;text-decoration:none>" + transactionName + "</a>";

                if (jmeterScriptNameTotal.trim().contains("Total")) {
                    dataMap.put("transactionName", jmeterScriptNameTotal.trim());
                } else {
                    dataMap.put("transactionName", Array[0].trim().replace("\"", "").replace(" ", ""));
                }
            } else {
                //对double值进行四舍五入，保留两位小数
                String sumeValue = null;
                if (Array[i].trim().equalsIgnoreCase("Infinity") || Array[i].trim().equalsIgnoreCase("NaN")) {
                    sumeValue = Array[i].trim();
                } else {
                    try {
                        double value = Double.valueOf(df.format(Double.valueOf(Array[i].trim())));
                        sumeValue = formatDouble(value);
                    } catch (Exception e) {
                        sumeValue = Array[i].trim();
                        e.printStackTrace();
                    }
                }

                // 根据索引确定性能指标类型 - 修复后映射
                switch (i) {
                    case 1:
                        dataMap.put("samples", sumeValue);  // 样本数
                        dataMap.put("sampleCount", sumeValue);
                        break;
                    case 2:
                        dataMap.put("ko", sumeValue);  // 失败数
                        break;
                    case 3:
                        // Error% 处理
                        String errorValueNew = Array[i].replace("\"", "").replace(" ", "");
                        if (!errorValueNew.equals("0.0")) {
                            errorValueNew = this.formatDouble(Double.valueOf(errorValueNew));
                            String lastruntime = DateUtil.date2TimeStamp(reportRunTime.trim(), "yyyy-MM-dd HH:mm:ss");
                            String ReporLinkstUrlResout = ReporLinkst_Url.replace("\\", "/");
                            errorLick = ReporLinkstUrlResout + "error/" + transactionName + "/perf" + "/" + jmeterScriptName + "/" + lastruntime;
                            sumeValue = "<a href=\"" + errorLick.toString() + "\" style=color:red;text-decoration:none>" + errorValueNew + "</a>";
                        }
                        dataMap.put("error", sumeValue);  // 错误率
                        dataMap.put("errorPercent", sumeValue);
                        break;
                    case 4:
                        dataMap.put("average", sumeValue);  // 平均响应时间
                        dataMap.put("averageResponseTime", sumeValue);
                        break;
                    case 5:
                        dataMap.put("min", sumeValue);  // 最小响应时间
                        break;
                    case 6:
                        dataMap.put("max", sumeValue);  // 最大响应时间
                        break;
                    case 7:
                        dataMap.put("median", sumeValue);  // 中位数响应时间
                        break;
                    case 8:
                        dataMap.put("thpct90", sumeValue);  // 90%响应时间
                        break;
                    case 9:
                        dataMap.put("thpct95", sumeValue);  // 95%响应时间
                        break;
                    case 10:
                        dataMap.put("thpct99", sumeValue);  // 99%响应时间
                        break;
                    case 11:
                        dataMap.put("throughput", sumeValue);  // 吞吐量
                        break;
                    case 12:
                        // 直接使用dashboard.js中的received值
                        if (sumeValue == null || sumeValue.trim().isEmpty()) {
                            sumeValue = "0";
                            log.warn("received指标为空，设置默认值0");
                        }
                        dataMap.put("received", sumeValue);
                        log.info("dashboard.js中的received值: {}", sumeValue);
                        break;
                    case 13:
                        // 直接使用dashboard.js中的sent值
                        if (sumeValue == null || sumeValue.trim().isEmpty()) {
                            sumeValue = "0";
                            log.warn("sent指标为空，设置默认值0");
                        }
                        dataMap.put("sent", sumeValue);
                        log.info("dashboard.js中的sent值: {}", sumeValue);
                        break;
                    default:
                        // 其他性能指标，使用通用键名
                        dataMap.put("performanceMetric_" + i, sumeValue);
                        break;
                }
            }

            //加上运行时的节点ip及端口号
            if (i == Array.length - 1) {
                String ipPort = nodeIp.toString();
                String runTime = runStartEndTime.toString();
                dataMap.put("nodeIp", ipPort.substring(0, ipPort.length() - 1));
                dataMap.put("startRunTime", runTime.split(",")[0]);
                dataMap.put("endRunTime", runTime.split(",")[1]);
            }
        }

        //添加 jtlPath:
        dataMap.put("jtlPath", jtlReportfile);

        //添加 indexpath:
        dataMap.put("indexPath", jsFile + "/index.html");

        //添加 status:
        dataMap.put("status", ReportStates.INSERT_0);

        // 入库前详细日志，记录所有重要指标
        log.info("准备入库，dataMap内容: {}", dataMap);
        log.info("transactionName: {}", dataMap.get("transactionName"));
        log.info("sampleCount: {}", dataMap.get("sampleCount"));
        log.info("errorPercent: {}", dataMap.get("errorPercent"));
        log.info("averageResponseTime: {}", dataMap.get("averageResponseTime"));
        log.info("throughput: {}", dataMap.get("throughput"));
        log.info("received: {}", dataMap.get("received"));
        log.info("sent: {}", dataMap.get("sent"));
        log.info("min: {}", dataMap.get("min"));
        log.info("max: {}", dataMap.get("max"));
        log.info("thpct90: {}", dataMap.get("thpct90"));
        log.info("thpct95: {}", dataMap.get("thpct95"));
        log.info("thpct99: {}", dataMap.get("thpct99"));
        log.info("nodeIp: {}", dataMap.get("nodeIp"));
        log.info("startRunTime: {}", dataMap.get("startRunTime"));
        log.info("endRunTime: {}", dataMap.get("endRunTime"));
        log.info("数据唯一标识: {}", dataMap.get("uniqueKey"));

        // 确保所有重要指标都已设置
        if (dataMap.get("sampleCount") == null || dataMap.get("sampleCount").toString().trim().isEmpty()) {
            log.error("重要指标sampleCount未设置或为空");
            dataMap.put("sampleCount", "0");
        }
        if (dataMap.get("averageResponseTime") == null || dataMap.get("averageResponseTime").toString().trim().isEmpty()) {
            log.error("重要指标averageResponseTime未设置或为空");
            dataMap.put("averageResponseTime", "0");
        }
        if (dataMap.get("throughput") == null || dataMap.get("throughput").toString().trim().isEmpty()) {
            log.error("重要指标throughput未设置或为空");
            dataMap.put("throughput", "0");
        }

        // 入库:
        Statistics statisticslist = new Statistics(dataMap);
        Jmeter_perfor_history_report historylist = new Jmeter_perfor_history_report();
        Jmeter_perfor_current_report currentlist = new Jmeter_perfor_current_report();

        // 为历史报告设置与当前报告相同的数据
        historylist.setUploadid((Integer) dataMap.get("uploadid"));
        historylist.setLastruntime(dataMap.get("lastruntime") != null ? (String) dataMap.get("lastruntime") : "");
        historylist.setScriptname(dataMap.get("scriptNameLink") != null ? (String) dataMap.get("scriptNameLink") : "");
        historylist.setThreads(dataMap.get("threadCount") != null ? (String) dataMap.get("threadCount") : "0");
        historylist.setLabel(dataMap.get("transactionName") != null ? (String) dataMap.get("transactionName") : "");
        historylist.setSamples(dataMap.get("sampleCount") != null ? (String) dataMap.get("sampleCount") : "0");
        historylist.setKo(dataMap.get("ko") != null ? (String) dataMap.get("ko") : "0");
        historylist.setError(dataMap.get("errorPercent") != null ? (String) dataMap.get("errorPercent") : "0");
        historylist.setAverage(dataMap.get("averageResponseTime") != null ? (String) dataMap.get("averageResponseTime") : "0");
        historylist.setMin(dataMap.get("min") != null ? (String) dataMap.get("min") : "0");
        historylist.setMax(dataMap.get("max") != null ? (String) dataMap.get("max") : "0");
        historylist.setMedian(dataMap.get("median") != null ? (String) dataMap.get("median") : "0");
        historylist.setThpct90(dataMap.get("thpct90") != null ? (String) dataMap.get("thpct90") : "0");
        historylist.setThpct95(dataMap.get("thpct95") != null ? (String) dataMap.get("thpct95") : "0");
        historylist.setThpct99(dataMap.get("thpct99") != null ? (String) dataMap.get("thpct99") : "0");
        historylist.setThroughput(dataMap.get("throughput") != null ? (String) dataMap.get("throughput") : "0");
        historylist.setReceived(dataMap.get("received") != null ? (String) dataMap.get("received") : "0");
        historylist.setSent(dataMap.get("sent") != null ? (String) dataMap.get("sent") : "0");
        historylist.setIp(dataMap.get("nodeIp") != null ? (String) dataMap.get("nodeIp") : "");
        historylist.setStarttime(dataMap.get("startRunTime") != null ? (String) dataMap.get("startRunTime") : "");
        historylist.setEndtime(dataMap.get("endRunTime") != null ? (String) dataMap.get("endRunTime") : "");
        historylist.setJtlpath(dataMap.get("jtlPath") != null ? (String) dataMap.get("jtlPath") : "");
        historylist.setIndexpath(dataMap.get("indexPath") != null ? (String) dataMap.get("indexPath") : "");
        historylist.setState(dataMap.get("status") != null ? (Integer) dataMap.get("status") : 0);
        
        // 入库前详细检查关键指标
        log.info("入库前dataMap内容: {}", dataMap);
        log.info("transactionName: {}", dataMap.get("transactionName"));
        log.info("sampleCount: {}", dataMap.get("sampleCount"));
        log.info("errorPercent: {}", dataMap.get("errorPercent"));
        log.info("averageResponseTime: {}", dataMap.get("averageResponseTime"));
        log.info("throughput: {}", dataMap.get("throughput"));
        log.info("startRunTime: {}", dataMap.get("startRunTime"));
        log.info("endRunTime: {}", dataMap.get("endRunTime"));

        // 确保关键指标不为空
        if (dataMap.get("sampleCount") == null || dataMap.get("sampleCount").toString().trim().isEmpty()) {
            log.error("关键指标sampleCount为空，设置默认值0");
            dataMap.put("sampleCount", "0");
        }
        if (dataMap.get("startRunTime") == null || dataMap.get("startRunTime").toString().trim().isEmpty()) {
            log.error("关键指标startRunTime为空，设置当前时间");
            dataMap.put("startRunTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        }
        if (dataMap.get("endRunTime") == null || dataMap.get("endRunTime").toString().trim().isEmpty()) {
            log.error("关键指标endRunTime为空，设置当前时间");
            dataMap.put("endRunTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        }

        // 显式设置当前报告数据，确保所有字段都被正确赋值并处理空值
        currentlist.setUploadid((Integer) dataMap.get("uploadid"));
        currentlist.setLastruntime(dataMap.get("lastruntime") != null ? (String) dataMap.get("lastruntime") : "");
        currentlist.setScriptname(dataMap.get("scriptNameLink") != null ? (String) dataMap.get("scriptNameLink") : "");
        currentlist.setThreads(dataMap.get("threadCount") != null ? (String) dataMap.get("threadCount") : "0");
        currentlist.setLabel(dataMap.get("transactionName") != null ? (String) dataMap.get("transactionName") : "");
        currentlist.setSamples(dataMap.get("sampleCount") != null ? (String) dataMap.get("sampleCount") : "0");
        currentlist.setKo(dataMap.get("ko") != null ? (String) dataMap.get("ko") : "0");
        currentlist.setError(dataMap.get("errorPercent") != null ? (String) dataMap.get("errorPercent") : "0");
        currentlist.setAverage(dataMap.get("averageResponseTime") != null ? (String) dataMap.get("averageResponseTime") : "0");
        currentlist.setMin(dataMap.get("min") != null ? (String) dataMap.get("min") : "0");
        currentlist.setMax(dataMap.get("max") != null ? (String) dataMap.get("max") : "0");
        currentlist.setMedian(dataMap.get("median") != null ? (String) dataMap.get("median") : "0");
        currentlist.setThpct90(dataMap.get("thpct90") != null ? (String) dataMap.get("thpct90") : "0");
        currentlist.setThpct95(dataMap.get("thpct95") != null ? (String) dataMap.get("thpct95") : "0");
        currentlist.setThpct99(dataMap.get("thpct99") != null ? (String) dataMap.get("thpct99") : "0");
        currentlist.setThroughput(dataMap.get("throughput") != null ? (String) dataMap.get("throughput") : "0");
        currentlist.setReceived(dataMap.get("received") != null ? (String) dataMap.get("received") : "0");
        currentlist.setSent(dataMap.get("sent") != null ? (String) dataMap.get("sent") : "0");
        currentlist.setIp(dataMap.get("nodeIp") != null ? (String) dataMap.get("nodeIp") : "");
        currentlist.setStarttime(dataMap.get("startRunTime") != null ? (String) dataMap.get("startRunTime") : "");
        currentlist.setEndtime(dataMap.get("endRunTime") != null ? (String) dataMap.get("endRunTime") : "");
        currentlist.setJtlpath(dataMap.get("jtlPath") != null ? (String) dataMap.get("jtlPath") : "");
        currentlist.setIndexpath(dataMap.get("indexPath") != null ? (String) dataMap.get("indexPath") : "");
        currentlist.setState(dataMap.get("status") != null ? (Integer) dataMap.get("status") : 0);
        
        log.info("创建Statistics对象: {}", statisticslist);
        log.info("创建并设置Jmeter_perfor_history_report对象: {}", historylist);
        log.info("创建Jmeter_perfor_current_report对象: {}", currentlist);

        // 添加事务管理，确保数据一致性
        TransactionStatus transactionStatus = null;
        try {
            // 开始事务
            transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());

            // 检查数据源状态
            log.info("检查数据源状态...");
            if (dataSource instanceof DruidDataSource) {
                DruidDataSource druidDataSource = (DruidDataSource) dataSource;
                log.info("Druid连接池状态: 活跃连接数={}, 连接总数={}, 最大连接数={}, 初始化连接数={}",
                        druidDataSource.getActiveCount(),
                        druidDataSource.getPoolingCount(),
                        druidDataSource.getMaxActive(),
                        druidDataSource.getInitialSize());
            }

            // 执行入库操作
            log.info("开始执行Statistics数据入库...");
        //    log.info("Statistics数据: {}", statisticslist);
         //   statisticsServer.insertStatistics(statisticslist);
         //   log.info("Statistics数据入库操作完成");

            log.info("开始执行Jmeter_perfor_history_report数据入库...");
            log.info("Jmeter_perfor_history_report数据: {}", historylist);
            // 直接插入单条记录
            if (historylist != null) {
                if (historylist instanceof Jmeter_perfor_history_report) {
                    jmeterperformhistoryreportServer.insertHistoryReport((Jmeter_perfor_history_report) historylist);
                } else {
                    log.error("历史报告数据类型错误: {}", historylist.getClass().getName());
                }
            }
            log.info("Jmeter_perfor_history_report数据入库操作完成");

            log.info("开始执行Jmeter_perfor_current_report数据入库...");
            log.info("Jmeter_perfor_current_report数据: {}", currentlist);
            jmeterperformcurrentreportServer.insertCurrentReport(currentlist);
            log.info("Jmeter_perfor_current_report数据入库操作完成");

            // 提交事务
            transactionManager.commit(transactionStatus);
            log.info("事务提交成功");
            log.info("线程：" + thread + "运行结果保存数据操作完成");
            log.info("数据已成功入库: transactionName={}, sampleCount={}, uploadid={}", 
                dataMap.get("transactionName"), dataMap.get("sampleCount"), dataMap.get("uploadid"));
        } catch (Exception e) {
            // 回滚事务
            if (transactionStatus != null) {
                transactionManager.rollback(transactionStatus);
                log.error("事务回滚成功");
            }
            log.error("数据入库失败: {}", e.getMessage());
            log.error("异常详情: transactionName={}, sampleCount={}, uploadid={}", 
                dataMap.get("transactionName"), dataMap.get("sampleCount"), dataMap.get("uploadid"));
            log.error("异常堆栈: ", e);
            // 尝试重新获取连接并重试入库
            try {
                log.info("尝试重新获取连接并重试入库...");
                Thread.sleep(1000); // 等待1秒

                // 重试时开启新事务
                TransactionStatus retryTransactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
                try {
                    log.info("开始执行Statistics数据重试入库...");
               //     statisticsServer.insertStatistics(statisticslist);
                    log.info("Statistics数据重试入库操作完成");

                    log.info("开始执行Jmeter_perfor_history_report数据重试入库...");
                    // 直接插入单条记录
                    if (historylist != null) {
                        if (historylist instanceof Jmeter_perfor_history_report) {
                            jmeterperformhistoryreportServer.insertHistoryReport((Jmeter_perfor_history_report) historylist);
                        } else {
                            log.error("历史报告数据类型错误: {}", historylist.getClass().getName());
                        }
                    }
                    log.info("Jmeter_perfor_history_report数据重试入库操作完成");

                    log.info("开始执行Jmeter_perfor_current_report数据重试入库...");
                    jmeterperformcurrentreportServer.insertCurrentReport(currentlist);
                    log.info("Jmeter_perfor_current_report数据重试入库操作完成");

                    // 提交重试事务
                    transactionManager.commit(retryTransactionStatus);
                    log.info("重试事务提交成功");
                } catch (Exception retryEx) {
                    // 回滚重试事务
                    if (retryTransactionStatus != null) {
                        transactionManager.rollback(retryTransactionStatus);
                        log.error("重试事务回滚成功");
                    }
                    log.error("重试入库失败: {}", retryEx.getMessage());
                    log.error("重试异常堆栈: ", retryEx);
                }
            } catch (Exception retryEx) {
                log.error("重试准备失败: {}", retryEx.getMessage());
                log.error("重试准备异常堆栈: ", retryEx);
            }
        }

        //释放内存：
        dataMap.clear();



    }

}