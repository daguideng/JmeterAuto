package com.atguigu.gmall.Impl.interfaceImpl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall.Interface.SendEmailReportIntel;
import com.atguigu.gmall.common.utils.FileOperate;
import com.atguigu.gmall.common.utils.HttpClientUtil;
import com.atguigu.gmall.dao.Interface_current_reportMapper;
import com.atguigu.gmall.entity.Interface_current_report;
import com.atguigu.gmall.entity.Jmeter_interface_top5_errors;
import com.atguigu.gmall.service.jmeterinter.JmeterInterfaceTop5ErrorsServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/**
 * @Author: dengdagui
 * @Description:发邮件报告： jmeter 接口测试
 * @Date: Created in 2018-9-17
 */
@Slf4j
@Component
public class SendEmailInterReportImpl implements SendEmailReportIntel {


    @Autowired
    private JmeterInterfaceTop5ErrorsServer jmeterInterfaceTop5ErrorsServer;

    @Autowired
    private Interface_current_reportMapper interface_current_reportMapper ;


    @Value("${ReporLinkstUrl.Inter}")
    private String ReporLinkstUrl_Inter;



    @Value("${serverUrl.Inter}")
    private String serverUrl_Inter;


    StringBuffer strBuf = new StringBuffer(800);
    List <Object> replrtlist = new ArrayList <>();

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

            sourceFile = SendEmailInterReportImpl.class.getClassLoader().getResourceAsStream("templates/properties/emailReportHead.html");
            bufReader = new BufferedReader(new InputStreamReader(sourceFile, "utf-8"));

            JSONArray array = getReportList(null, null, null, null);
            for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {

                if (tmp.contains("ReportBody")) {
                    if (array.size() == 0) {

                        String ReporLinkstUrl = ReporLinkstUrl_Inter.replace("\\", "/");
                        String lickTager = ReporLinkstUrl + "why";

                        tmp = "Sorry!   No test data was generated......<br>";
                        tmp = "<a href=" + lickTager + " style=color:red;text-decoration:none>" + tmp + "</a>";

                        sbf.append(tmp);
                        sbf.append("<td><br></td>");

                    }
                    String linevalue = null;
                    for (int i = 0; i < array.size(); i++) {
                        linevalue = ReportContent(array, i).toString();
                        if (!"databaseIsNull".equals(linevalue)) {
                            sbf.append(linevalue);
                        }
                        linevalue = null;
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
            bufReader.close();
            sourceFile.close();
        }

        return sbf;

    }

    @Override
    public StringBuffer writeEmailReport(String emailReportDir, String reportHtml, String scriptName, int scriptCount, int scriptSumSize, int X, int lastScriptNum) throws Exception {
        return null;
    }


    /***
     * 为单独每个脚本生成一个测试结果而写的方法：
     */
    public StringBuffer writeEmailReportModiy(String emailReportDir, String reportHtml, String scriptName, int scriptSumSize, int scriptadd, StringBuffer sbb) throws Exception {


        InputStream sourceFile = null;
        BufferedReader bufReader = null;
        BufferedWriter out = null;

        try {
            String emailReportPath = this.createHtml(emailReportDir, reportHtml);

            //读入emailReport的主要head内容：
            sourceFile = SendEmailInterReportImpl.class.getClassLoader().getResourceAsStream("templates/properties/emailReportHead.html");
            bufReader = new BufferedReader(new InputStreamReader(sourceFile, "utf-8"));


            File file = new File(emailReportPath);
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));

            JSONArray array = getReportList("SingleType", scriptName, null, null);
            for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {

                int arraySise = 0;

                if (tmp.contains("ReportBody")) {

                    if (array.size() == 0) {

                        arraySise = 0;
                        String ReporLinkstUrl = ReporLinkstUrl_Inter.replace("\\", "/");
                        String lickTager = ReporLinkstUrl + "why";

                        tmp = "Sorry! " + scriptName + " No test data was generated......<br>";
                        tmp = "<a href=" + lickTager + " style=color:red;text-decoration:none>" + tmp + "</a>";

                        out.write("<td><br></td>");
                        out.write(tmp);
                        sbb.append("<td><br></td>");
                        sbb.append(tmp);

                    } else {

                        arraySise = 1;
                        String reportValue = null;
                        for (int i = 0; i < array.size(); i++) {
                            reportValue = ReportContentSecond(array, i).toString();
                            tmp = ReportContent(array, i).toString();
                            if (!"databaseIsNull".equals(tmp)) {
                                out.write(tmp.toString());
                                out.flush();
                                sbb.append(reportValue.toString());
                            }
                            replrtlist.clear();
                            strBuf.setLength(0);

                            if (i == (array.size() - 1)) {
                                System.out.println("startIndex========>" + i);
                                System.out.println("(array.size()-1))========>" + (array.size() - 1));
                                //在 IP之后添加br 每一个脚本的信息输完后加： br 行：
                                out.write("<td><br></td>");
                                out.flush();
                                sbb.append("<td><br></td>");
                            }
                            reportValue = null;
                        }
                    }


                } else if (tmp.contains("<table") || tmp.contains("<tbody>")) {
                    if (scriptadd == 0) {
                        out.write(tmp);
                        out.flush();
                        sbb.append(tmp);
                    }
                } else if (tmp.contains("</table>") || tmp.contains("</tbody>")) {
                    if ((scriptSumSize - 1) == (scriptadd)) {   //为了保持 所有脚本的一致宽度， 最后一个脚本时
                        out.write(tmp);
                        out.flush();
                        sbb.append(tmp);
                    }
                } else {
                    //只有脚本中有数据才输出，否则只输出脚本未生成报告数据：
                    sbb.append(tmp);
                    out.write(tmp.toString());
                    out.flush();

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
                System.out.println();
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
                System.out.println();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return DirFilePath + "/" + emailReportFile;
    }

    /**
     * 发送邮件报告的第二种模板：
     * sendEmailReport singleTyle model
     */
    public StringBuffer sendSingTyleModel(String emailReportPath) {

        StringBuffer sbf = new StringBuffer(2000);

        try {
            //读入emailReport的主要head内容：
            InputStream sourceFile = new FileInputStream(new File(emailReportPath));
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(sourceFile, "utf-8"));
            System.out.println();

            for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {
                sbf.append(tmp);
            }
            bufReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return sbf;
    }


    /**
     * 得到所有报告ArrayList
     *
     * @param
     * @return
     * @throws Exception
     */
    public JSONArray getReportList(String SingleType, String scriptName, String id, String lastruntime) throws Exception {

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
            urlSuffix = "[{\"operator\":\"eq\",\"property\":\"uploadid\",\"value\":\"" + runid + "\"},{\"operator\":\"eq\",\"property\":\"lastruntime\",\"value\":\"" + lastruntime + "\"},{\"operator\":\"like\",\"property\":\"label\",\"value\":\"Total\"}]";
        } else if ("DetailQueryType".equals(SingleType)) {
            urlSuffix = "[{\"operator\":\"eq\",\"property\":\"lastruntime\",\"value\":\"" + lastruntime + "\"}]";

        } else if("HistoryReportType".equals(SingleType)) {   //接口历史报告:
            urlSuffix = "[{\"operator\":\"like\",\"property\":\"scriptname\",\"value\":\"" + scriptName + "\"}]";
        } else {
            urlSuffix = "[{\"operator\":\"eq\",\"property\":\"state\",\"value\":0},{\"operator\":\"like\",\"property\":\"label\",\"value\":\"Total\"}]";
            System.out.println();

        }

        String url = serverUrl_Inter.replace("\\", "/") + "jmeterinter/simplereport/list?filter=" + URLEncoder.encode(urlSuffix, "UTF-8");


        //1.get请求得到
        String reportResutl = HttpClientUtil.doPost(url,"");
        //对于特殊字符进行转义，否则json解析不了：
        // theString.Replace(">", "&gt;");
        // theString.Replace("<", "&lt;");

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
            for (Object index : replrtlist) {
                if (null == index) {
                    index = "&nbsp;";  //&emsp;  //&nbsp;
                }
                String strline = "<td style=" + "\"color: rgb(49, 132, 155); border-width: 0px 1px 1px 0px; border-right-style: dashed; border-right-color: rgb(138, 162, 191); border-bottom-style: solid; border-bottom-color: rgb(75, 172, 198); background-color: rgb(210, 234, 241); width: 25px; height: 15px;\">" + index + "</td>";
                strBuf.append(strline.trim());

            }
            strBuf.append("<tr>");

        }

        return strBuf;

    }


    /***
     public static void main(String []args) throws Exception {
     SendEmailReport  test = new SendEmailReport();

     String dirDir = "D:\\mars\\ReportResultDir\\20180921155727" ;
     String reportHtml = "20180921155727.html" ;
     String scriptName = "dxUcreditMainProces" ;
     String file = "D:\\mars\\ReportResultDir\\20180828165104\\jiao_verify_test_20180828165330_3_1\\jiao_verify_test_20180828165330_report\\report.html" ;
     test.writeEmailReport( dirDir, reportHtml, scriptName);


     }

     ***/


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
            String value = scriptNameMap.get(key);


            //生成reports:
            this.writeEmailReportModiy(emailReportDir, reportHtml, value, scriptNameMap.size(), scriptInit, sbb);
            scriptInit++;
        }


        //2.根据生成的html文件再发送报告：
        /**
         StringBuffer sbf = new StringBuffer(2000);
         try {
         //读入emailReport的主要head内容：
         InputStream sourceFile = new FileInputStream(new File(emailReportPath));
         BufferedReader bufReader = new BufferedReader(new InputStreamReader(sourceFile, "utf-8"));
         System.out.println();
         for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {
         sbf.append(tmp);
         System.out.println();
         }
         bufReader.close();
         } catch (IOException e) {
         e.printStackTrace();
         }
         **/

        return sbb;
    }


    /***
     * 为单独点击Total超链接而再次查询当前事务详情数据而写的方法：
     */
    public StringBuffer toDetailReport(String lastruntime, String scriptName, StringBuffer sbb) throws Exception {

        StringBuffer sbf = new StringBuffer(2000);

        InputStream sourceFile = null;
        BufferedReader bufReader = null;

        try {


            //读入emailReport的主要head内容：
            sourceFile = SendEmailInterReportImpl.class.getClassLoader().getResourceAsStream("templates/properties/emailReportHead.html");
            bufReader = new BufferedReader(new InputStreamReader(sourceFile, "utf-8"));


            JSONArray array = getReportList("DetailQueryType", scriptName, null, lastruntime);
            for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {


                int arraySise = 0;

                if (tmp.contains("ReportBody")) {

                    if (array.size() == 0) {
                        arraySise = 0;
                        String ReporLinkstUrl = ReporLinkstUrl_Inter.replace("\\", "/");
                        String lickTager = ReporLinkstUrl + "why";

                        tmp = "Sorry! " + scriptName + " No test data was generated......<br>";
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

            sourceFile.close();
            bufReader.close();


        }

        return sbb;

    }


    /***
     * 为单独点击error%超链接查看错误详细报告：
     */
    public StringBuffer toErrorDetailReport(String lastruntime, String scriptName, String transactionName, StringBuffer sbb) throws Exception {

        StringBuffer sbf = new StringBuffer(2000);

        InputStream sourceFile = null;
        BufferedReader bufReader = null;

        try {


            //读入emailReport的主要head内容：
            sourceFile = SendEmailInterReportImpl.class.getClassLoader().getResourceAsStream("templates/properties/emailReportError.html");
            bufReader = new BufferedReader(new InputStreamReader(sourceFile, "utf-8"));

            List <Jmeter_interface_top5_errors> interTop5ErrorList = jmeterInterfaceTop5ErrorsServer.selectJmeter_inter_top5_error(lastruntime, scriptName, transactionName);

            for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {


                int arraySise = 0;

                if (tmp.contains("ReportBody")) {

                    if (interTop5ErrorList.size() == 0) {
                        arraySise = 0;

                        String ReporLinkstUrl = ReporLinkstUrl_Inter.replace("\\", "/");
                        String lickTager = ReporLinkstUrl + "why";

                        tmp = "Sorry! " + scriptName + " No test data was generated.......<br>";
                        tmp = "<a href=" + lickTager + " style=color:red;text-decoration:none>" + tmp + "</a>";

                        sbb.append("<td><br></td>");
                        sbb.append(tmp);


                    } else {

                        arraySise = 1;

                        String reportValue = null;
                        for (int i = 0; i < interTop5ErrorList.size(); i++) {

                            reportValue = this.interErrorTop5Line(i, interTop5ErrorList).toString();
                            if (!"databaseIsNull".equals(reportValue)) {

                                sbb.append(reportValue.toString());

                            }
                            replrtlist.clear();
                            strBuf.setLength(0);

                            if (i == (interTop5ErrorList.size() - 1)) {

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

            sourceFile.close();
            bufReader.close();


        }

        return sbb;

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
        for (Object index : replrtlist) {
            if (null == index) {
                index = "&nbsp;";  //&emsp;  //&nbsp;
            }
            String strline = "<td style=" + "\"color: rgb(49, 132, 155); border-width: 0px 1px 1px 0px; border-right-style: dashed; border-right-color: rgb(138, 162, 191); border-bottom-style: solid; border-bottom-color: rgb(75, 172, 198); background-color: rgb(210, 234, 241); width: 25px; height: 15px;\">" + index + "</td>";

            strBuf.append(strline);

        }
        strBuf.append("</tr>");

        //  }

        return strBuf;


    }


/**
 对发生第二种报告的bug修改：根据数据库中已存在的数据直接生成报告：
 **/
/**
 public StringBuffer sendSingTyleModel(String emailReportPath){
 StringBuffer   sbf = new StringBuffer(2000);

 //1.根据共多少脚本生成html文件：


 //2.根据生成的html文件再发送报告：
 try {
 //读入emailReport的主要head内容：
 InputStream sourceFile = new FileInputStream(new File(emailReportPath));
 BufferedReader bufReader = new BufferedReader(new InputStreamReader(sourceFile, "utf-8"));
 System.out.println();
 for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {
 sbf.append(tmp);
 System.out.println();
 }
 bufReader.close();
 } catch (IOException e) {
 e.printStackTrace();
 }


 return sbf ;
 }

 ***/

    /** 发送邮件报告的第二种模板：
     * sendEmailReport singleTyle model
     */

    /**
     public StringBuffer sendSingTyleModel(String emailReportDir, String reportHtml,SortedMap<Integer,String> scriptNameMap) throws Exception {

     String emailReportPath = this.createHtml(emailReportDir,reportHtml) ;

     StringBuffer sbb = new StringBuffer(8000);

     //1.根据共多少脚本生成html文件：
     Set <Integer> k = scriptNameMap.keySet();
     Iterator<Integer> it = k.iterator();
     int scriptInit = 0 ;
     while(it.hasNext()){
     Integer key = it.next();
     String value = scriptNameMap.get(key);


     //生成reports:
     System.out.println("scriptName===================>"+value);
     this.writeEmailReportModiy(emailReportDir, reportHtml, value, scriptNameMap.size(),  scriptInit);
     scriptInit ++ ;
     }





     //2.根据生成的html文件再发送报告：
     //  StringBuffer   sbf = new StringBuffer(2000);
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



     return sbb ;
     }

     ***/


    /**
     * 为点查看报告按钮而写的接口方法:：
     *
     * @param
     * @throws Exception
     */
    public StringBuffer writeLinkReport(String id,String lastruntime) throws Exception {


        StringBuffer sbf = new StringBuffer(800);

        InputStream sourceFile = null;
        BufferedReader bufReader = null;

        try {

            //读入emailReport的主要head内容：
            sourceFile = SendEmailInterReportImpl.class.getClassLoader().getResourceAsStream("templates/properties/PerfReport/head.html");




            bufReader = new BufferedReader(new InputStreamReader(sourceFile, "utf-8"));

            for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {

                if (tmp.contains("ReportBody")) {

                    JSONArray array = getReportList("LinkType", null, id, lastruntime);

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
            bufReader.close();
            sourceFile.close();
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
            sourceFile = SendEmailInterReportImpl.class.getClassLoader().getResourceAsStream("templates/properties/emailReportHead.html");
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
            bufReader.close();
            sourceFile.close();
        }


        return sbf;

    }


    public StringBuffer interErrorTop5Line(int i, List <Jmeter_interface_top5_errors> list) throws UnsupportedEncodingException {

        StringBuffer errorstf = new StringBuffer(500);

        List <String> indexlist = new ArrayList <>(500);

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
                indexe = "&nbsp;";
            }
            String strline = "<td style=" + "\"color: rgb(49, 132, 155); border-width: 0px 1px 1px 0px; border-right-style: dashed; border-right-color: rgb(138, 162, 191); border-bottom-style: solid; border-bottom-color: rgb(75, 172, 198); background-color: rgb(210, 234, 241); width: 25px; height: 15px;\">" + indexe + "</td>";

            errorstf.append(strline);

        }
        errorstf.append("</tr>");


        return errorstf;


    }




    public StringBuffer interselectProgressReport(int i, List <Interface_current_report> list) throws UnsupportedEncodingException {


        StringBuffer rogressbf = new StringBuffer(500);

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


        String strline = null ;


        rogressbf.append("<tr>");
        for (Object indexe : indexlist) {
            if (null == indexe) {
                indexe = "&nbsp;";
            }



            strline = "<td style=" + "\"color: rgb(49, 132, 155); border-width: 0px 1px 1px 0px; border-right-style: dashed; border-right-color: rgb(138, 162, 191); border-bottom-style: solid; border-bottom-color: rgb(75, 172, 198); background-color: rgb(210, 234, 241); width: 25px; height: 15px;\">" + indexe + "</td>";



            rogressbf.append(strline);

        }
        rogressbf.append("</tr>");


        return rogressbf;


    }



    /***
     * 为随时查看报告数据而写的方法：
     */
    public StringBuffer selectProgressReport(Integer state ,StringBuffer sbf) throws Exception {

        InputStream sourceFile = null;
        BufferedReader bufReader = null;

        try {


            //读入emailReport的主要head内容：
            sourceFile = SendEmailInterReportImpl.class.getClassLoader().getResourceAsStream("templates/properties/emailReportHead.html");
            bufReader = new BufferedReader(new InputStreamReader(sourceFile, "utf-8"));


            List<Interface_current_report>  interface_current_reports = interface_current_reportMapper.selectProgressReport(state);
            for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {


                int arraySise = 0;

                if (tmp.contains("ReportBody")) {

                    if (interface_current_reports.size() == 0) {
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
                        for (int i = 0; i < interface_current_reports.size(); i++) {

                            reportValue = interselectProgressReport(i,interface_current_reports).toString();
                            System.out.println("reportValue--->" + reportValue);
                            if (!"databaseIsNull".equals(reportValue)) {

                                sbf.append(reportValue.toString());

                            }
                            replrtlist.clear();
                            strBuf.setLength(0);

                            if (i == (interface_current_reports.size() - 1)) {

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

            sourceFile.close();
            bufReader.close();


        }

        return sbf;

    }


}
