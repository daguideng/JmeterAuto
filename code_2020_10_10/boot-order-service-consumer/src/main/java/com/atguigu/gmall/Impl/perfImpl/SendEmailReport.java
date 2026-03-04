package com.atguigu.gmall.Impl.perfImpl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall.common.utils.FileOperate;
import com.atguigu.gmall.common.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: dengdagui
 * @Description:发邮件报告：
 * @Date: Created in 2018-9-17
 */
@Component
public class SendEmailReport {


    StringBuffer strBuf = new StringBuffer(800);
    List<Object> replrtlist = new ArrayList<>() ;


    @Value("${serverUrl.Inter}")
    private String serverUrl ;


    /**
     * 发送邮件或生成报告到本地文件：
     * @param
     * @throws Exception
     */
    public StringBuffer writeEmailReport() throws Exception {

        StringBuffer   sbf = new StringBuffer(2000);

        InputStream sourceFile = null ;
        BufferedReader bufReader = null ;

        try {
            //读入emailReport的主要head内容：
             sourceFile = SendEmailReport.class.getClassLoader().getResourceAsStream("templates/properties/emailReportHead.html");
             bufReader = new BufferedReader(new InputStreamReader(sourceFile, "utf-8"));

            for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {

                if(tmp.contains("ReportBody")) {

                    JSONArray array = getReportList(null,null);

                    if(array.size()==0){
                        tmp = "Sorry! No test data was generated......<br> " ;
                        sbf.append(tmp);
                    }
                    for(int i = 0 ; i <array.size(); i++) {
                        tmp = ReportContent(array, i).toString();
                        if(!"databaseIsNull".equals(tmp)) {
                            sbf.append(tmp);
                        }
                        replrtlist.clear();
                        strBuf.setLength(0);
                    }
                }else{
                    sbf.append(tmp);
                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            bufReader.close();
            sourceFile.close();
        }


        return sbf ;

    }


    /***
     * 为单独每个脚本生成一个测试结果而写的方法：
     */
    public StringBuffer writeEmailReport(String emailReportDir,String reportHtml,String scriptName) throws Exception {

        StringBuffer   sbf = new StringBuffer(2000);

        InputStream sourceFile = null ;
        BufferedReader bufReader = null ;
        BufferedWriter out = null ;

        try {
            String emailReportPath = this.createHtml(emailReportDir,reportHtml) ;

            //读入emailReport的主要head内容：
             sourceFile = SendEmailReport.class.getClassLoader().getResourceAsStream("templates/properties/emailReportHead.html");
             bufReader = new BufferedReader(new InputStreamReader(sourceFile, "utf-8"));


            File file = new File(emailReportPath) ;
             out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));


            for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {

                if(tmp.contains("ReportBody")) {

                    JSONArray array = getReportList("SingleType",scriptName);

                    if(array.size()==0){
                        tmp = "Sorry! "+scriptName+" No test data was generated......<br> " ;
                        out.write(tmp);

                    }
                    for(int i = 0 ; i <array.size(); i++) {
                        tmp = ReportContent(array, i).toString();
                        if(!"databaseIsNull".equals(tmp)) {
                            out.write(tmp);

                        }
                        replrtlist.clear();
                        strBuf.setLength(0);
                    }
                }else{
                    out.write(tmp);

                }

            }



        } catch (IOException e) {

            e.printStackTrace();

        }finally {
            out.flush();
            out.close();
            sourceFile.close();
            bufReader.close();

        }


        return sbf ;

    }


    /**
     * 创建文件：
     */
    public String createHtml(String DirFilePath,String emailReportFile){

        try {
            File fileDirs = new File(DirFilePath);
            if (!fileDirs.exists()) {
              //  if (fileDirs.isDirectory()) {
                    FileOperate.newFolder(DirFilePath);
            //    }
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        try {
            String filePath = DirFilePath +"/" +emailReportFile;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            if(!myFilePath.exists()){
                myFilePath.createNewFile();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return DirFilePath+"/"+ emailReportFile ;
    }




    /**
     * 得到所有报告ArrayList
     * @param
     * @return
     * @throws Exception
     */
    public JSONArray getReportList(String SingleType,String scriptName) throws Exception{

        StringBuffer sb = new StringBuffer(500) ;

        /**  用httpclient时有特殊字符要转义:
         * { 改为%7B
           } 改为%7D
            "改为%22
         */
        //如果传过来是类型：SingleType 则是生成单个脚本的格式的测试报告，否则不是：
        String urlSuffix = null ;
        if("SingleType".equals(SingleType)){
             urlSuffix = "jmeterperf/simplereport/list?filter=[{\"operator\":\"eq\",\"property\":\"state\",\"value\":0},{\"operator\":\"like\",\"property\":\"scriptname\",\"value\":\""+scriptName+"\"}]&limit=10000";
        }else {
             urlSuffix = "jmeterperf/simplereport/list?filter=[{\"operator\":\"eq\",\"property\":\"state\",\"value\":0}]&limit=10000";
        }
        urlSuffix = urlSuffix.replace("{","%7B").replace("}","%7D").replace("\"","%22");

        String url  = serverUrl.replace("\\", "/") ;
        sb.append(url);
        sb.append(urlSuffix);

        //1.get请求得到
        String reportResutl = HttpClientUtil.doGet(sb.toString());
        //对于特殊字符进行转义，否则json解析不了：
        // theString.Replace(">", "&gt;");
        // theString.Replace("<", "&lt;");

       // reportResutl = reportResutl.replace(">","&gt;").replace("<","&lt;");
        JSONObject json = JSONObject.parseObject(reportResutl);
        String data = json.getString("data") ;
        JSONObject list = JSONObject.parseObject(data);
        JSONArray array = list.getJSONArray("list");
        sb.setLength(0);
        return array ;
    }



    public StringBuffer ReportContent(JSONArray array,int i) throws Exception{

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



            if(label.contains("Total")) {  //只生成总的一条进行汇总：
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
                for(Object index : replrtlist ){
                    if(null == index){
                        index = "&nbsp;" ;  //&emsp;  //&nbsp;
                    }
                    String strline = "<td style="+"\"color: rgb(49, 132, 155); border-width: 0px 1px 1px 0px; border-right-style: dashed; border-right-color: rgb(138, 162, 191); border-bottom-style: solid; border-bottom-color: rgb(75, 172, 198); background-color: rgb(210, 234, 241); width: 25px; height: 15px;\">"+index+"</td>" ;
                    strBuf.append(strline);

                }
                strBuf.append("</tr>");

                   return strBuf ;

            }
                 strBuf.append("databaseIsNull") ;
                 return strBuf ;

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


    /** 发送邮件报告的第二种模板：
     * sendEmailReport singleTyle model
     */
    public StringBuffer sendSingTyleModel(String emailReportPath){

        StringBuffer   sbf = new StringBuffer(2000);

        try {
            //读入emailReport的主要head内容：
            InputStream sourceFile = new FileInputStream(new File(emailReportPath));
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(sourceFile, "utf-8"));

            for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {
                sbf.append(tmp);
            }
            bufReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return sbf ;
    }


}
