package com.atguigu.gmall.Impl.perfImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall.Impl.interfaceImpl.SendEmailInterReportImpl;
import com.atguigu.gmall.Interface.SendEmailReportIntel;
import com.atguigu.gmall.common.utils.FileOperate;
import com.atguigu.gmall.common.utils.HttpClientUtil;
import com.atguigu.gmall.dao.StatisticsMapper;
import com.atguigu.gmall.entity.Jmeter_perf_top5_errors;
import com.atguigu.gmall.entity.Statistics;
import com.atguigu.gmall.service.jmeterperf.JmeterPerfTop5ErrorServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/**
 * @Author: dengdagui
 * @Description:发邮件报告：
 * @Date: Created in 2018-9-17
 */
@Component("sendEmailPerfReportImpl")
public class SendEmailPerfReportImpl implements SendEmailReportIntel {

    private static final Logger logger = LoggerFactory.getLogger(SendEmailPerfReportImpl.class);

    @Autowired
    private JmeterPerfTop5ErrorServer jmeterPerfTop5ErrorServer ;

    @Autowired
    private StatisticsMapper  statisticsMapper ;

    StringBuffer strBuf = new StringBuffer(800);
    List <String> replrtlist = new ArrayList <>(300);


    @Value("${ReporLinkstUrl.Inter}")
    private String ReporLinkstUrl_Inter ;

    @Value("${ReporLinkst.Url}")
    private  String ReporLinkst_Url;


    @Value("${serverUrl.Perf}")
    private String  serverUrl_Perf ;



    /**
     * 发送邮件或生成报告到本地文件：
     *
     * @param
     * @throws Exception
     */
    public StringBuffer writeEmailReport() throws Exception {

        StringBuffer sbf = new StringBuffer(2000);

        InputStream sourceFile = null;
        BufferedReader bufReader = null;

        try {
            //读入emailReport的主要head内容：
            sourceFile = SendEmailPerfReportImpl.class.getClassLoader().getResourceAsStream("templates/properties/PerfReport/head.html");
            if (sourceFile == null) {
                throw new IOException("无法加载模板文件: templates/properties/PerfReport/head.html");
            }
            bufReader = new BufferedReader(new InputStreamReader(sourceFile, "utf-8"));

            JSONArray array = getReportList(null, null, null, null);

            for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {

                if (tmp.contains("ReportBody")) {

                    if (array.size() == 0) {
                        String ReporLinkstUrl = ReporLinkst_Url.replace("\\", "/");
                        String lickTager = ReporLinkstUrl + "why";

                        tmp = "Sorry!  No test data was generated......<br>";
                        tmp = "<a href=" + lickTager + " style=color:red;text-decoration:none>" + tmp + "</a>";

                        sbf.append("<td><br></td>");
                        sbf.append(tmp);
                    }
                    String linevalue = null;
                    for (int i = 0; i < array.size(); i++) {
                        linevalue = ReportContent(array, i).toString();
                        if (!"databaseIsNull".equals(linevalue)) {
                            sbf.append(linevalue);
                        }
                        replrtlist.clear();
                        strBuf.setLength(0);
                        linevalue = null;
                    }

                } else {
                    sbf.append(tmp);
                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufReader != null) {
                try {
                    bufReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (sourceFile != null) {
                try {
                    sourceFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        return sbf;

    }


    /***
     * 为单独每个脚本生成一个测试结果而写的方法：
     */
    public StringBuffer writeEmailReport(String emailReportDir, String reportHtml, String scriptName, int scriptCount, int scriptSumSize, int X, int lastScriptNum) throws Exception {

        StringBuffer sbf = new StringBuffer(2000);

        InputStream sourceFile = null;
        BufferedReader bufReader = null;
        BufferedWriter out = null;

        try {
            String emailReportPath = this.createHtml(emailReportDir, reportHtml);

            //读入emailReport的主要head内容：
            sourceFile = SendEmailPerfReportImpl.class.getClassLoader().getResourceAsStream("templates/properties/PerfReport/head.html");
            if (sourceFile == null) {
                throw new IOException("无法加载模板文件: templates/properties/PerfReport/head.html");
            }
            bufReader = new BufferedReader(new InputStreamReader(sourceFile, "utf-8"));


            File file = new File(emailReportPath);
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));


            for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {

                if (tmp.contains("ReportBody")) {

                    JSONArray array = getReportList("SingleType", scriptName, null, null);

                    if (array.size() == 0) {

                        String ReporLinkstUrl = ReporLinkst_Url.replace("\\", "/");
                        String lickTager = ReporLinkstUrl + "why";

                        tmp = "Sorry!  No test data was generated......<br>";
                        tmp = "<a href=" + lickTager + " style=color:red;text-decoration:none>" + tmp + "</a>";

                        sbf.append("<td><br></td>");
                        sbf.append(tmp);
                        tmp = "Sorry! " + scriptName + " No test data was generated......<br> ";
                        out.write(tmp);

                    }

                    for (int i = 0; i < array.size(); i++) {
                        tmp = ReportContent(array, i).toString();
                        if (!"databaseIsNull".equals(tmp)) {
                            out.write(tmp);

                        }
                        replrtlist.clear();
                        strBuf.setLength(0);

                        if (i == (array.size() - 1)) {

                            //在 IP之后添加br 每一个脚本的信息输完后加： br 行：
                            out.write("<td><br></td>");

                        }
                    }


                } else if (tmp.contains("<table") || tmp.contains("<tbody>")) {
                    if (scriptCount == 1 && X == (lastScriptNum - 1)) {
                        out.write(tmp);
                    }
                } else if (tmp.contains("</table>") || tmp.contains("</tbody>")) {
                    if (scriptCount == scriptSumSize && X == (lastScriptNum - 1)) {   //为了保持 所有脚本的一致宽度， 最后一个脚本时
                        out.write(tmp);
                    }
                } else {
                    out.write(tmp);
                }

            }


        } catch (IOException e) {

            e.printStackTrace();

        } finally {
            out.flush();
            out.close();
            sourceFile.close();
            bufReader.close();

        }

        return sbf;
    }


    /***
     * 为单独每个脚本生成一个测试结果而写的方法：
     */
    public StringBuffer writeEmailReportModiy(String emailReportDir, String reportHtml, String scriptName, int scriptSumSize, int scriptadd, StringBuffer sbb) throws Exception {

       // StringBuffer sbf = new StringBuffer(8000);

        InputStream sourceFile = null;
        BufferedReader bufReader = null;
        BufferedWriter out = null;

        try {
            String emailReportPath = this.createHtml(emailReportDir, reportHtml);

            //读入emailReport的主要head内容：
            sourceFile = SendEmailPerfReportImpl.class.getClassLoader().getResourceAsStream("templates/properties/PerfReport/head.html");
            if (sourceFile == null) {
                throw new IOException("无法加载模板文件: templates/properties/PerfReport/head.html");
            }
            bufReader = new BufferedReader(new InputStreamReader(sourceFile, "utf-8"));


            File file = new File(emailReportPath);
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));

            JSONArray array = getReportList("SingleType", scriptName, null, null);

            for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {

                int arraySise = 0;

                if (tmp.contains("ReportBody")) {

                    if (array.size() == 0) {
                        arraySise = 0;
                        String ReporLinkstUrl = ReporLinkst_Url.replace("\\", "/");
                        String lickTager = ReporLinkstUrl + "why";

                        tmp = "Sorry! "+scriptName+ "No test data was generated......<br>";
                        tmp = "<a href=" + lickTager + " style=color:red;text-decoration:none>" + tmp + "</a>";

                        sbb.append("<td><br></td>");
                        sbb.append(tmp);

                    } else {

                        arraySise = 1;

                        String reportValue = null;
                        for (int i = 0; i < array.size(); i++) {

                            reportValue = ReportContentSecond(array, i).toString();
                            System.out.println("reportValue--->" + reportValue);
                            if (!"databaseIsNull".equals(reportValue)) {
                                out.write(reportValue.toString());
                                out.flush();
                                sbb.append(reportValue.toString());

                            }
                            replrtlist.clear();
                            strBuf.setLength(0);

                            if (i == (array.size() - 1)) {

                                //在 IP之后添加br 每一个脚本的信息输完后加： br 行：
                                out.write("<td><br></td>");
                                out.flush();

                                sbb.append("<td><br></td>");
                            }
                            reportValue = null;
                        }
                    }


                } else if (tmp.contains("<table") || tmp.contains("<tbody>")) {
                    //  Thread.sleep(1500);
                    if (scriptadd == 0) {
                        out.write(tmp);

                        out.flush();

                        sbb.append(tmp);


                    }
                } else if (tmp.contains("</table>") || tmp.contains("</tbody>")) {
                    //    Thread.sleep(1500);
                    if ((scriptSumSize - 1) == (scriptadd)) {   //为了保持 所有脚本的一致宽度， 最后一个脚本时
                        out.write(tmp);

                        out.flush();
                        sbb.append(tmp);

                    }
                } else {
                    //只有脚本中有数据才输出，否则只输出脚本未生成报告数据：
                    out.write(tmp.toString());
                    out.flush();

                    sbb.append(tmp);

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufReader != null) {
                try {
                    bufReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (sourceFile != null) {
                try {
                    sourceFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sbb;

    }


    /***
     * 为单独点击Total超链接而再次查询当前事务详情数据而写的方法：
     */
    public StringBuffer toDetailReport(String perfstarttime, String scriptName, StringBuffer sbb) throws Exception {


        InputStream sourceFile = null;
        BufferedReader bufReader = null;

        try {


            //读入emailReport的主要head内容：
            sourceFile = SendEmailPerfReportImpl.class.getClassLoader().getResourceAsStream("templates/properties/PerfReport/head.html");
            if (sourceFile == null) {
                throw new IOException("无法加载模板文件: templates/properties/PerfReport/head.html");
            }
            bufReader = new BufferedReader(new InputStreamReader(sourceFile, "utf-8"));


            JSONArray array = getReportList("DetailQueryType", scriptName, null,perfstarttime);
            for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {


                int arraySise = 0;

                if (tmp.contains("ReportBody")) {

                    if (array.size() == 0) {
                        arraySise = 0;


                        String ReporLinkstUrl = ReporLinkst_Url.replace("\\", "/");
                        String lickTager = ReporLinkstUrl + "why";

                        tmp = "Sorry! " + scriptName + " No test data was generated......<br>";
                        tmp = "<a href=" + lickTager + " style=color:red;text-decoration:none>" + tmp + "</a>";

                        sbb.append(tmp);

                        sbb.append("<td><br></td>");
                        sbb.append(tmp);


                    } else {

                        arraySise = 1;

                        String reportValue = null;
                        for (int i = 0; i < array.size(); i++) {

                            reportValue = ReportContentSecond(array, i).toString();

                            if (!"databaseIsNull".equals(reportValue)) {

                                sbb.append(reportValue.toString());

                            }
                            replrtlist.clear();
                            strBuf.setLength(0);

                            if (i == (array.size() - 1)) {

                                //在 IP之后添加br 每一个脚本的信息输完后加： br 行：

                                sbb.append("<td><br></td>");
                            }
                            reportValue = null;
                        }
                    }


                } else if (tmp.contains("<table") || tmp.contains("<tbody>")) {
                    sbb.append(tmp);

                } else if (tmp.contains("</table>") || tmp.contains("</tbody>")) {
                    sbb.append(tmp);

                } else {
                    //只有脚本中有数据才输出，否则只输出脚本未生成报告数据：

                    sbb.append(tmp);

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufReader != null) {
                try {
                    bufReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (sourceFile != null) {
                try {
                    sourceFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sbb;

    }


    /**
     * 创建文件：
     */
    public String createHtml(String DirFilePath, String emailReportFile) {

        try {
            File fileDirs = new File(DirFilePath);
            if (!fileDirs.exists()) {
                //  if (fileDirs.isDirectory()) {
                FileOperate.newFolder(DirFilePath);

                //    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String filePath = DirFilePath + "/" + emailReportFile;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            if (!myFilePath.exists()) {
                myFilePath.createNewFile();
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println();
        }

        return DirFilePath + "/" + emailReportFile;
    }


    /**
     * 得到所有报告ArrayList
     *
     * @param
     * @return
     * @throws Exception
     */
    public JSONArray getReportList(String SingleType, String scriptName, String id, String perfstarttime) throws Exception {

        StringBuffer sb = new StringBuffer(500);


        /**  用httpclient时有特殊字符要转义:
         * { 改为%7B
         } 改为%7D
         "改为%22
         */
        //如果传过来是类型：SingleType 则是生成单个脚本的格式的测试报告，否则不是：
        String urlSuffix = null;
        if ("SingleType".equals(SingleType)) {
            urlSuffix = "[{\"operator\":\"eq\",\"property\":\"state\",\"value\":0},{\"operator\":\"like\",\"property\":\"scriptname\",\"value\":\"" + scriptName + "\"}" +
                    //  ",{\"operator\":\"eq\",\"property\":\"threads\",\"value\":\""+threads+"\"}" +
                    "]";
        } else if ("LinkType".equals(SingleType)) {
            int runid = Integer.valueOf(id);
            urlSuffix = "[{\"operator\":\"eq\",\"property\":\"uploadid\",\"value\":\"" + runid + "\"},{\"operator\":\"eq\",\"property\":\"perfstarttime\",\"value\":\"" + perfstarttime + "\"},{\"operator\":\"like\",\"property\":\"label\",\"value\":\"Total\"}]";
        } else if ("DetailQueryType".equals(SingleType)) {
            //lastruntime
            urlSuffix = "[{\"operator\":\"eq\",\"property\":\"lastruntime\",\"value\":\"" + perfstarttime + "\"}]";

        } else if("HistoryReportType".equals(SingleType)) {   //接口历史报告:
            urlSuffix = "[{\"operator\":\"like\",\"property\":\"scriptname\",\"value\":\"" + scriptName + "\"}]";
        } else {

            urlSuffix = "[{\"operator\":\"eq\",\"property\":\"state\",\"value\":0},{\"operator\":\"like\",\"property\":\"label\",\"value\":\"Total\"}]";

        }

        String url = serverUrl_Perf.replace("\\", "/") + "jmeterperf/simplereport/list?filter=" + URLEncoder.encode(urlSuffix, "UTF-8");


        //1.post请求得到，传入空JSON对象而不是空字符串
        String reportResutl = HttpClientUtil.doPost(url, "{}");
        // 检查响应是否为空
        if (reportResutl == null || reportResutl.isEmpty()) {
            throw new Exception("获取报告数据失败: 服务器返回空响应");
        }
        //对于特殊字符进行转义，否则json解析不了：
        // theString.Replace(">", "&gt;");
        // theString.Replace("<", "&lt;");

        // 添加日志记录请求和响应
        logger.info("请求URL: " + url);
        logger.info("请求参数: {}");
        logger.info("响应结果: " + reportResutl);

        // reportResutl = reportResutl.replace(">","&gt;").replace("<","&lt;");
        JSONObject json = JSONObject.parseObject(reportResutl);
        String data = json.getString("data");
        JSONObject list = JSONObject.parseObject(data);
        JSONArray array = list.getJSONArray("list");
        sb.setLength(0);
        return array;
    }


    public StringBuffer ReportContent(JSONArray array, int i) throws Exception {

        JSONObject jo = array.getJSONObject(i);

        String id = jo.getString("id");
        String starttime = jo.getString("starttime");
        String endtime = jo.getString("endtime");
        String scriptname = jo.getString("scriptname");
        String threads = jo.getString("threads");
        String label = jo.getString("label");
        String samples = jo.getString("samples");
        String ko = jo.getString("ko");
        String error = jo.getString("error");
        String average = jo.getString("average");
        String min = jo.getString("min");
        String max = jo.getString("max");
        String thpct90 = jo.getString("thpct90");
        String thpct95 = jo.getString("thpct95");
        String thpct99 = jo.getString("thpct99");
        String throughput = jo.getString("throughput");
        String received = jo.getString("received");
        String sent = jo.getString("sent");
        String ip = jo.getString("ip");


        if (label.contains("Total")) {  //只生成总的一条进行汇总：
            replrtlist.add(id);
            replrtlist.add(starttime);
            replrtlist.add(endtime);
            replrtlist.add(scriptname);
            replrtlist.add(threads);
            replrtlist.add(label);
            replrtlist.add(samples);
            replrtlist.add(ko);
            replrtlist.add(error);
            replrtlist.add(average);
            replrtlist.add(min);
            replrtlist.add(max);
            replrtlist.add(thpct90);
            replrtlist.add(thpct95);
            replrtlist.add(thpct99);
            replrtlist.add(throughput);
            replrtlist.add(received);
            replrtlist.add(sent);
            replrtlist.add(ip);


            strBuf.append("<tr>");
            for (String index : replrtlist) {
                if (null == index) {
                    index = "&nbsp;";  //&emsp;  //&nbsp;
                }

                if (index.contains(":") && index.contains(",")) {  //性能测试对于多个ip代理机而言分行美观点:
                    index = index.replace(",", ",<br/>");
                }
                String strline = "<td style=" + "\"color: rgb(49, 132, 155); border-width: 0px 1px 1px 0px; border-right-style: dashed; border-right-color: rgb(138, 162, 191); border-bottom-style: solid; border-bottom-color: rgb(75, 172, 198); background-color: rgb(210, 234, 241); width: 60px; height: 15px;\">" + index + "</td>";

                strBuf.append(strline);

            }
            strBuf.append("</tr>");

            return strBuf;

        }
        strBuf.append("databaseIsNull");
        return strBuf;

    }


    public StringBuffer ReportContentSecond(JSONArray array, int i) throws Exception {

        JSONObject jo = array.getJSONObject(i);

        String id = jo.getString("id");
        String starttime = jo.getString("starttime");
        String endtime = jo.getString("endtime");
        String scriptname = jo.getString("scriptname");
        String threads = jo.getString("threads");
        String label = jo.getString("label");
        String samples = jo.getString("samples");
        String ko = jo.getString("ko");
        String error = jo.getString("error");
        String average = jo.getString("average");
        String min = jo.getString("min");
        String max = jo.getString("max");
        String thpct90 = jo.getString("thpct90");
        String thpct95 = jo.getString("thpct95");
        String thpct99 = jo.getString("thpct99");
        String throughput = jo.getString("throughput");
        String received = jo.getString("received");
        String sent = jo.getString("sent");
        String ip = jo.getString("ip");



        //    if(label.contains("Total")) {  //只生成总的一条进行汇总：
        replrtlist.add(id);
        replrtlist.add(starttime);
        replrtlist.add(endtime);
        replrtlist.add(scriptname);
        replrtlist.add(threads);
        replrtlist.add(label);
        replrtlist.add(samples);
        replrtlist.add(ko);
        replrtlist.add(error);
        replrtlist.add(average);
        replrtlist.add(min);
        replrtlist.add(max);
        replrtlist.add(thpct90);
        replrtlist.add(thpct95);
        replrtlist.add(thpct99);
        replrtlist.add(throughput);
        replrtlist.add(received);
        replrtlist.add(sent);
        replrtlist.add(ip);


        strBuf.append("<tr>");
        for (String index : replrtlist) {
            if (null == index) {
                index = "&nbsp;";  //&emsp;  //&nbsp;
            }

            if (index.contains(":") && index.contains(",")) {  //性能测试对于多个ip代理机而言分行美观点:
                index = index.replace(",", ",<br/>");
            }
            String strline = "<td style=" + "\"color: rgb(49, 132, 155); border-width: 0px 1px 1px 0px; border-right-style: dashed; border-right-color: rgb(138, 162, 191); border-bottom-style: solid; border-bottom-color: rgb(75, 172, 198); background-color: rgb(210, 234, 241); width: 25px; height: 15px;\">" + index + "</td>";

            strBuf.append(strline);

        }
        strBuf.append("</tr>");

        return strBuf;


    }




    /***
     * 为单独点击error%超链接查看错误详细报告：
     */
    public StringBuffer toErrorDetailReport(String lastruntime, String scriptName, String transactionName,StringBuffer sbb) throws Exception {

        StringBuffer sbf = new StringBuffer(2000);

        InputStream sourceFile = null;
        BufferedReader bufReader = null;

        try {


            //读入emailReport的主要head内容：
            sourceFile = SendEmailPerfReportImpl.class.getClassLoader().getResourceAsStream("templates/properties/PerfReport/head.html");
            if (sourceFile == null) {
                throw new IOException("无法加载模板文件: templates/properties/PerfReport/head.html");
            }
            bufReader = new BufferedReader(new InputStreamReader(sourceFile, "utf-8"));

            List<Jmeter_perf_top5_errors> perfTop5ErrorList = jmeterPerfTop5ErrorServer.selectJmeter_Perf_top5_error(lastruntime,scriptName,transactionName);

            for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {


                int arraySise = 0;

                if (tmp.contains("ReportBody")) {

                    if (perfTop5ErrorList.size() == 0) {
                        arraySise = 0;
                        String ReporLinkstUrl = ReporLinkst_Url.replace("\\", "/");
                        String lickTager = ReporLinkstUrl + "why";

                        tmp = "Sorry! "+scriptName+ "No test data was generated.....<br>";
                        tmp = "<a href=" + lickTager + " style=color:red;text-decoration:none>" + tmp + "</a>";

                        sbb.append("<td><br></td>");
                        sbb.append(tmp);

                    } else {

                        arraySise = 1;

                        String reportValue = null;
                        for (int i = 0; i < perfTop5ErrorList.size(); i++) {


                            reportValue = this.perfErrorTop5Line(i,perfTop5ErrorList).toString();
                            if (!"databaseIsNull".equals(reportValue)) {

                                sbb.append(reportValue.toString());

                            }
                            replrtlist.clear();
                            strBuf.setLength(0);

                            if (i == (perfTop5ErrorList.size() - 1)) {

                                //在 IP之后添加br 每一个脚本的信息输完后加： br 行：

                                sbb.append("<td><br></td>");
                            }
                            reportValue = null;
                        }
                    }

                } else if (tmp.contains("<table") || tmp.contains("<tbody>")) {
                    sbb.append(tmp);

                } else if (tmp.contains("</table>") || tmp.contains("</tbody>")) {
                    sbb.append(tmp);

                } else {
                    //只有脚本中有数据才输出，否则只输出脚本未生成报告数据：
                    sbb.append(tmp);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufReader != null) {
                try {
                    bufReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (sourceFile != null) {
                try {
                    sourceFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sbb;

    }


    /**
     * 发送邮件报告的第二种模板：
     * sendEmailReport singleTyle model
     */
    public StringBuffer sendSingTyleModel(String emailReportPath) {

        StringBuffer sbf = new StringBuffer(2000);
        InputStream sourceFile = null;
        BufferedReader bufReader = null;

        try {
            //读入emailReport的主要head内容：
            sourceFile = new FileInputStream(new File(emailReportPath));
            bufReader = new BufferedReader(new InputStreamReader(sourceFile, "utf-8"));
            System.out.println();

            for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {
                sbf.append(tmp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufReader != null) {
                try {
                    bufReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (sourceFile != null) {
                try {
                    sourceFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        return sbf;
    }


    /**
     * 发送邮件报告的第二种模板：
     * sendEmailReport singleTyle model
     */
    public StringBuffer sendSingTyleModel(String emailReportDir, String reportHtml, SortedMap <Integer, String> scriptNameMap) throws Exception {

        String emailReportPath = this.createHtml(emailReportDir, reportHtml);

        StringBuffer sbb = new StringBuffer(8000);

        //1.根据共多少脚本生成html文件：
        Set <Integer> k = scriptNameMap.keySet();
        Iterator <Integer> it = k.iterator();
        int scriptInit = 0;
        while (it.hasNext()) {
            Integer key = it.next();
            System.out.println("key--key---->" + key);
            String value = scriptNameMap.get(key);


            //生成reports:
            this.writeEmailReportModiy(emailReportDir, reportHtml, value, scriptNameMap.size(), scriptInit, sbb);
            scriptInit++;
        }


        //2.根据生成的html文件再发送报告：
        /***
         StringBuffer   sbf = new StringBuffer(2000);
         try {
         //读入emailReport的主要head内容：
         InputStream sourceFile = new FileInputStream(new File(emailReportPath));
         BufferedReader bufReader = new BufferedReader(new InputStreamReader(sourceFile, "utf-8"));
         for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {
         //      sbf.append(tmp);

         }
         bufReader.close();
         } catch (IOException e) {
         e.printStackTrace();
         }
         ***/


        return sbb;
    }





    public StringBuffer perfErrorTop5Line(int i,List<Jmeter_perf_top5_errors> list) throws UnsupportedEncodingException {

        StringBuffer  errorstf = new StringBuffer(500);

        List<String> indexlist = new ArrayList <>(500);

        indexlist.add(list.get(i).getId().toString().trim());
        indexlist.add(list.get(i).getRuntime().trim());
        indexlist.add(list.get(i).getScriptname().trim());
        indexlist.add(list.get(i).getThreads().trim());
        indexlist.add(list.get(i).getUrl());
        indexlist.add(list.get(i).getSample().trim());
        indexlist.add(list.get(i).getSamples().trim());
        indexlist.add(list.get(i).getErrors().trim());
        indexlist.add(list.get(i).getError1().trim());
        indexlist.add(list.get(i).getErrors1().trim());
        indexlist.add(list.get(i).getError2().trim());
        indexlist.add(list.get(i).getErrors2().trim());
        indexlist.add(list.get(i).getError3().trim());
        indexlist.add(list.get(i).getErrors3().trim());
        indexlist.add(list.get(i).getError4().trim());
        indexlist.add(list.get(i).getErrors4().trim());
        indexlist.add(list.get(i).getError5().trim());
        indexlist.add(list.get(i).getErrors5().trim());



        errorstf.append("<tr>");
        for (Object indexe : indexlist) {
            if (null == indexe) {
                indexe = "&nbsp; ";
            }
            String strline = "<td style=" + "\"color: rgb(49, 132, 155); border-width: 0px 1px 1px 0px; border-right-style: dashed; border-right-color: rgb(138, 162, 191); border-bottom-style: solid; border-bottom-color: rgb(75, 172, 198); background-color: rgb(210, 234, 241); width: 25px; height: 15px;\">" + indexe + "</td>";

            errorstf.append(strline);

        }
        errorstf.append("</tr>");


        return errorstf ;


    }


    /**
     * 为点查看报告按钮而写的接口方法:：
     *
     * @param
     * @throws Exception
     */
    public StringBuffer writeLinkReport(String id, String perfstarttime) throws Exception {


        StringBuffer sbf = new StringBuffer(800);

        InputStream sourceFile = null;
        BufferedReader bufReader = null;

        try {

            //读入emailReport的主要head内容：
            sourceFile = SendEmailPerfReportImpl.class.getClassLoader().getResourceAsStream("templates/properties/PerfReport/head.html");
            if (sourceFile == null) {
                throw new IOException("无法加载模板文件: templates/properties/PerfReport/head.html");
            }
            bufReader = new BufferedReader(new InputStreamReader(sourceFile, "utf-8"));

            for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {

                if (tmp.contains("ReportBody")) {

                    JSONArray array = getReportList("LinkType", null, id, perfstarttime);

                    if (array.size() == 0) {
                        String ReporLinkstUrl = ReporLinkst_Url.replace("\\", "/");
                        String lickTager = ReporLinkstUrl + "why";

                        tmp = "Sorry! No test data was generated......<br>";
                        tmp = "<a href=" + lickTager + " style=color:red;text-decoration:none>" + tmp + "</a>";

                        sbf.append("<td><br></td>");
                        sbf.append(tmp);
                    }
                    for (int i = 0; i < array.size(); i++) {
                        tmp = ReportContent(array, i).toString();
                        if (!"databaseIsNull".equals(tmp)) {
                            sbf.append(tmp);

                        }
                        replrtlist.clear();
                        strBuf.setLength(0);

                    }
                } else {
                    sbf.append(tmp);
                }


            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufReader != null) {
                try {
                    bufReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (sourceFile != null) {
                try {
                    sourceFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        return sbf;

    }




    /**
     * 查询历史报告而写的接口方法:：
     *
     * @param
     * @throws Exception
     */
    public StringBuffer writeLinkHistoryReport(String scriptName) throws Exception {


        StringBuffer sbf = new StringBuffer(800);

        InputStream sourceFile = null;
        BufferedReader bufReader = null;

        try {

            //读入emailReport的主要head内容：
            sourceFile = SendEmailPerfReportImpl.class.getClassLoader().getResourceAsStream("templates/properties/PerfReport/head.html");
            if (sourceFile == null) {
                throw new IOException("无法加载模板文件: templates/properties/PerfReport/head.html");
            }
            bufReader = new BufferedReader(new InputStreamReader(sourceFile, "utf-8"));

            for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {

                if (tmp.contains("ReportBody")) {

                    JSONArray array = getReportList("HistoryReportType", scriptName, null, null);

                    if (array.size() == 0) {

                        String ReporLinkstUrl = ReporLinkstUrl_Inter.replace("\\", "/");
                        String lickTager = ReporLinkstUrl + "why";

                        tmp = "Sorry!   No test data was generated......<br>";
                        tmp = "<a href=" + lickTager + " style=color:red;text-decoration:none>" + tmp + "</a>";

                        sbf.append("<td><br></td>");
                        sbf.append(tmp);

                    }
                    for (int i = 0; i < array.size(); i++) {
                        tmp = ReportContent(array, i).toString();
                        if (!"databaseIsNull".equals(tmp)) {
                            sbf.append(tmp);

                        }
                        replrtlist.clear();
                        strBuf.setLength(0);

                    }
                } else {
                    sbf.append(tmp);
                }


            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufReader != null) {
                try {
                    bufReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (sourceFile != null) {
                try {
                    sourceFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        return sbf;

    }






    /***
     * 为随时查看报告数据而写的方法：
     */
    public StringBuffer selectProgressReport(Integer state ,StringBuffer sbf) throws Exception {

        InputStream sourceFile = null;
        BufferedReader bufReader = null;

        try {


            //读入emailReport的主要head内容：
            sourceFile = SendEmailPerfReportImpl.class.getClassLoader().getResourceAsStream("templates/properties/PerfReport/head.html");
            if (sourceFile == null) {
                throw new IOException("无法加载模板文件: templates/properties/PerfReport/head.html");
            }
            bufReader = new BufferedReader(new InputStreamReader(sourceFile, "utf-8"));


            List<Statistics>  statistics_current_reports = statisticsMapper.selectProgressReport(state);
            for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {


                int arraySise = 0;

                if (tmp.contains("ReportBody")) {

                    if (statistics_current_reports.size() == 0) {
                        arraySise = 0;
                        String ReporLinkstUrl = ReporLinkstUrl_Inter.replace("\\", "/");
                        String lickTager = ReporLinkstUrl + "why";

                        tmp = "Sorry! "  + " No test data was generated......<br>";
                        tmp = "<a href=" + lickTager + " style=color:red;text-decoration:none>" + tmp + "</a>";

                        sbf.append("<td><br></td>");
                        sbf.append(tmp);


                    } else {

                        arraySise = 1;

                        String reportValue = null;
                        for (int i = 0; i < statistics_current_reports.size(); i++) {

                            reportValue = perfselectProgressReport(i,statistics_current_reports).toString();
                            System.out.println("reportValue--->" + reportValue);
                            if (!"databaseIsNull".equals(reportValue)) {

                                sbf.append(reportValue.toString());

                            }
                            replrtlist.clear();
                            strBuf.setLength(0);

                            if (i == (statistics_current_reports.size() - 1)) {

                                //在 IP之后添加br 每一个脚本的信息输完后加： br 行：

                                sbf.append("<td><br></td>");
                            }
                            reportValue = null;
                        }
                    }

                } else if (tmp.contains("<table") || tmp.contains("<tbody>")) {
                    sbf.append(tmp);

                } else if (tmp.contains("</table>") || tmp.contains("</tbody>")) {
                    sbf.append(tmp);

                } else {
                    //只有脚本中有数据才输出，否则只输出脚本未生成报告数据：
                    sbf.append(tmp);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufReader != null) {
                try {
                    bufReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (sourceFile != null) {
                try {
                    sourceFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sbf;

    }




    public StringBuffer perfselectProgressReport(int i, List <Statistics> list) throws UnsupportedEncodingException {

        String strline = null ;

        StringBuffer sbfreport = new StringBuffer(500);

        List <String> indexlist = new ArrayList <>(500);

        indexlist.add(list.get(i).getId().toString().trim());
        indexlist.add(list.get(i).getStarttime().trim());
        indexlist.add(list.get(i).getEndtime().trim());
        indexlist.add(list.get(i).getScriptname().trim());
        indexlist.add(list.get(i).getThreads().trim());
        indexlist.add(list.get(i).getLabel().trim());
        indexlist.add(list.get(i).getSamples().trim());
        indexlist.add(list.get(i).getKo());
        indexlist.add(list.get(i).getError().trim());
        indexlist.add(list.get(i).getAverage().trim());
        indexlist.add(list.get(i).getMin().trim());
        indexlist.add(list.get(i).getMax().trim());
        indexlist.add(list.get(i).getThpct90().trim());
        indexlist.add(list.get(i).getThpct95().trim());
        indexlist.add(list.get(i).getThpct99().trim());
        indexlist.add(list.get(i).getThroughput().trim());
        indexlist.add(list.get(i).getReceived().trim());
        indexlist.add(list.get(i).getSent().trim());
        indexlist.add(list.get(i).getIp().trim());


        sbfreport.append("<tr>");
        for (Object indexe : indexlist) {
            if (null == indexe) {
                indexe = "&nbsp;";
            }
             strline = "<td style=" + "\"color: rgb(49, 132, 155); border-width: 0px 1px 1px 0px; border-right-style: dashed; border-right-color: rgb(138, 162, 191); border-bottom-style: solid; border-bottom-color: rgb(75, 172, 198); background-color: rgb(210, 234, 241); width: 25px; height: 15px;\">" + indexe + "</td>";

            sbfreport.append(strline);

        }
        sbfreport.append("</tr>");


        return sbfreport;


    }










}
