package com.atguigu.gmall.common.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * @Author: dengdagui
 * @Description: 字符串替换主要替换相应参数文件
 * @Date: Created in 2018-7-20
 */

public class FileReplayUtils {



    /***
     * 修改脚本中时用jar,java,class,参烽的各种文件:的调用时修改其绝对路径:
     */
    public static void modiyFilePathParameter(String sourceFile, String findStr,
                                       String replaceStr) {

        try {
            BufferedReader bufReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(
                            sourceFile)), "utf-8"));
            StringBuffer strBuf = new StringBuffer(5000);

            for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {

                // 替换线程:
                if (tmp.contains(findStr) && tmp.contains("elementType")) {
                    //   String findStrold = (String) tmp.substring(tmp.indexOf(":") - 1, tmp.lastIndexOf(findStr)) + findStr;
                    String findStrold = tmp.split("name=\"")[1].split("\"")[0];
                    tmp = tmp.replace(findStrold, replaceStr);

                }else if (tmp.contains(findStr)) {
                    String findStrold = tmp.split(">")[1].split("<")[0];
                    tmp = tmp.replace(findStrold, replaceStr);

                }


                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));

                //System.out.println("strBut1---->"+strBuf.toString());

            }

            PrintWriter printWriter = new PrintWriter(sourceFile);
            printWriter.write(strBuf.toString().toCharArray());
            bufReader.close();
            printWriter.flush();
            printWriter.close();
            strBuf.setLength(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }







    /***
     * 修改脚本并发，开始时间，持续运行时间：
     */
    public static void modiyScenarioConfig(String sourceFile, String threads,
                                    long startTime, String duration,String sleepTime,String delaytime) {
        //化成秒
        long durationTime = (long) Float.valueOf(duration).floatValue() * 60  ;

        //化成毫秒时间：
        long endTime = startTime + (long) Float.valueOf(duration).floatValue() * 60 * 1000  ;


        try {
            BufferedReader bufReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(
                            sourceFile)),"utf-8"));
            StringBuffer strBuf = new StringBuffer();

            for (String tmp = null; (tmp = bufReader.readLine()) != null;) {

                // 替换勾上"永远"，这样才能控制运行时间生效，这是先决条件：在最后面

                // 替换线程:
                if (tmp.contains("ThreadGroup.num_threads")) {
                    tmp = tmp.replace(tmp,
                            "<stringProp name=\"ThreadGroup.num_threads\">"
                                    + threads + "</stringProp>");
                    // 替换开始时间:
                } else if (tmp.contains("ThreadGroup.start_time")) {
                    tmp = tmp.replace(tmp,
                            "<longProp name=\"ThreadGroup.start_time\">"
                                    + startTime + "</longProp>");
                    // 替换结束时间:
                } else if (tmp.contains("ThreadGroup.end_time")) {
                    // <longProp
                    // name="ThreadGroup.end_time">1408611986000</longProp>
                    tmp = tmp.replace(tmp,
                            "<longProp name=\"ThreadGroup.end_time\">"
                                    + endTime + "</longProp>");
                    //
                } else if (tmp.contains("ThreadGroup.scheduler")) {
                    // <boolProp name="ThreadGroup.scheduler">true</boolProp>
                    tmp = tmp.replace(tmp,
                            "<boolProp name=\"ThreadGroup.scheduler\">"
                                    + "true" + "</boolProp>");
                    // 替换持续时间:
                } else if (tmp.contains("ThreadGroup.duration")) {
                    tmp = tmp.replace(tmp,
                            "<stringProp name=\"ThreadGroup.duration\">"
                                    + durationTime + "</stringProp>");
                    // 替换勾上"永远"，这样才能控制运行时间生效，这是先决条件：:
                } else if (tmp.contains("LoopController.loops")) {
                    tmp = tmp.replace(tmp,
                            "<intProp name=\"LoopController.loops\">" + "-1"
                                    + "</intProp>");
                    //替换思考时间  (可优化)
                }else if (tmp.contains("ThreadGroup.ramp_time")) {
                    tmp = tmp.replace(tmp,
                            "<stringProp name=\"ThreadGroup.ramp_time\">"
                                    + sleepTime + "</stringProp>");
                    //替换延迟场景时间 （可优化）
                }else if (tmp.contains("ThreadGroup.delay")) {
                    tmp = tmp.replace(tmp,
                            "<stringProp name=\"ThreadGroup.delay\">"+delaytime+"</stringProp>");

                }

                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));

            }

            PrintWriter printWriter = new PrintWriter(sourceFile);
            printWriter.write(strBuf.toString().toCharArray());
            bufReader.close();
            printWriter.flush();
            printWriter.close();
            strBuf.setLength(0); ;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /***
     * 修改脚本并发，默认运行次数（1次），为接口测试写的方法：
     */
    public static void modiyScenarioInterDefault(String sourceFile, String threads,
                                          String delaytime) {

        try {
            BufferedReader bufReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(
                            sourceFile)),"utf-8"));
            StringBuffer strBuf = new StringBuffer();

            for (String tmp = null; (tmp = bufReader.readLine()) != null;) {

                // 替换线程:
                if (tmp.contains("ThreadGroup.num_threads")) {
                    tmp = tmp.replace(tmp,
                            "<stringProp name=\"ThreadGroup.num_threads\">"
                                    + threads + "</stringProp>");

                }  else if (tmp.contains("ThreadGroup.scheduler")) {
                    tmp = tmp.replace(tmp,
                            "<boolProp name=\"ThreadGroup.scheduler\">"
                                    + "false" + "</boolProp>");

                } else if (tmp.contains("LoopController.continue_forever")) {  //禁勾上"永远"，
                    tmp = tmp.replace(tmp,
                            "<boolProp name=\"LoopController.continue_forever\">" + "false"
                                    + "</boolProp>\n");
                }else if (tmp.contains("ThreadGroup.delay")) {  //替换延迟场景时间 （可优化）
                    tmp = tmp.replace(tmp,
                            "<stringProp name=\"ThreadGroup.delay\">"+delaytime+"</stringProp>");

                }else if (tmp.contains("LoopController.loops")) {  //默认运行一次
                    tmp = tmp.replace(tmp,
                            "<stringProp name=\"LoopController.loops\">1</stringProp>");

                }

                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));

            }

            PrintWriter printWriter = new PrintWriter(sourceFile);
            printWriter.write(strBuf.toString().toCharArray());
            bufReader.close();
            printWriter.flush();
            printWriter.close();
            strBuf.delete(0, strBuf.length()) ;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }







    /***
     * 此方法为设置接口测试而写的方法，如果重试次设置为"Yes"则进行接口测试
     */
    public void modiyRetryConfig(String sourceFile, String thread,
                                 String retryNumberStr) {

        try {
            BufferedReader bufReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(
                            sourceFile)),"utf-8"));
            StringBuffer strBuf = new StringBuffer();

            for (String tmp = null; (tmp = bufReader.readLine()) != null;) {

                // 替换勾上"永远"，这样才能控制运行时间生效，这是先决条件：在最后面

                // 替换线程:
                if (tmp.contains("ThreadGroup.num_threads")) {
                    tmp = tmp.replace(tmp,
                            "<stringProp name=\"ThreadGroup.num_threads\">"
                                    + thread + "</stringProp>");
                    // 不知能否换,要测试
                }  else if (tmp.contains("ThreadGroup.scheduler")) {
                    // <boolProp name="ThreadGroup.scheduler">true</boolProp>
                    tmp = tmp.replace(tmp,
                            "<boolProp name=\"ThreadGroup.scheduler\">"
                                    + "false" + "</boolProp>");
                    // 替换勾上"永远"，这样才能控制运行时间生效，这是先决条件：:
                } else if (tmp.contains("LoopController.loops")) {
                    tmp = tmp.replace(tmp,
                            "<intProp name=\"LoopController.loops\">" + retryNumberStr
                                    + "</intProp>");
                }

                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));



            }

            PrintWriter printWriter = new PrintWriter(sourceFile);
            printWriter.write(strBuf.toString().toCharArray());

            printWriter.flush();
            printWriter.close();
            bufReader.close();
            strBuf.delete(0, strBuf.length()) ;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /***
     * 为生成测试报告时生成tps曲线：
     */
    public void updateTps(String sourceFile, ArrayList<String> tps,
                          ArrayList<String> runtime, String vuser) {


        try {
            BufferedReader bufReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(
                            sourceFile)),"utf-8"));

            StringBuffer strBuf = new StringBuffer();

            for (String tmp = null; (tmp = bufReader.readLine()) != null;) {
                // 替换线程:
                // 替换开始时间:
                if (tmp.contains("categories")) {
                    String str1 = "categories: [ ";
                    String str2 = "]";
                    for (int i = 0; i < runtime.size(); i++) {
                        if (i == runtime.size() - 1) {
                            str1 = str1 + "\'" + runtime.get(i) + "\'" + str2;
                        } else {
                            str1 = str1 + "\'" + runtime.get(i) + "\'" + ",";
                        }

                    }
                    tmp = tmp.replace(tmp, str1);
                    // 替换Tps:
                } else if (tmp.contains("data")) {
                    String str1 = "data: [";
                    String str2 = "]";

                    for (int i = 0; i < tps.size(); i++) {
                        if (i == tps.size() - 1) {
                            str1 = str1 + tps.get(i) + str2;
                        } else {
                            str1 = str1 + tps.get(i) + ",";
                        }
                    }
                    tmp = tmp.replace(tmp, str1);
                    // 替换用户:
                } else if (tmp.contains("name:")) {

                    tmp = tmp.replace(tmp, "name:" + "\'" + vuser + " Vuser"
                            + "\'" + ",");
                }
                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));
            }

            PrintWriter printWriter = new PrintWriter(sourceFile);
            printWriter.write(strBuf.toString().toCharArray());
            bufReader.close();
            printWriter.flush();
            printWriter.close();
            strBuf.delete(0, strBuf.length()) ;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * 为生成测试报告时生成成功和失败失败的事务/秒曲线：
     */
    public void updatetransactionsPerSecond(String sourceFile,
                                            ArrayList<String> SucessCount, ArrayList<String> ErroyCount,
                                            ArrayList<String> runtime, String vuser) {

        // 控制每行的变量:
        int dataline = 0;

        int nameline = 0;

        try {
            BufferedReader bufReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(
                            sourceFile)),"utf-8"));
            StringBuffer strBuf = new StringBuffer();

            for (String tmp = null; (tmp = bufReader.readLine()) != null;) {

                // 替换线程:
                if (tmp.contains("categories")) { // 替换开始时间:

                    String str1 = "categories: [ ";
                    String str2 = "]";

                    for (int i = 0; i < runtime.size(); i++) {
                        if (i == runtime.size() - 1) {
                            str1 = str1 + "\'" + runtime.get(i) + "\'" + str2;
                        } else {
                            str1 = str1 + "\'" + runtime.get(i) + "\'" + ",";
                        }
                    }
                    tmp = tmp.replace(tmp, str1);

                } else if (tmp.contains("data")) { // 成功事务数每秒:

                    if (dataline == 0) {
                        String str1 = "data: [";
                        String str2 = "]";
                        for (int i = 0; i < SucessCount.size(); i++) {
                            if (i == SucessCount.size() - 1) {
                                str1 = str1 + SucessCount.get(i) + str2;
                            } else {
                                str1 = str1 + SucessCount.get(i) + ",";
                            }
                        }
                        tmp = tmp.replace(tmp, str1);
                        dataline++;
                    } else if (dataline == 1) {
                        String str1 = "data: [";
                        String str2 = "]";

                        for (int i = 0; i < ErroyCount.size(); i++) {
                            if (i == ErroyCount.size() - 1) {
                                str1 = str1 + ErroyCount.get(i) + str2;
                            } else {
                                str1 = str1 + ErroyCount.get(i) + ",";
                            }
                        }
                        tmp = tmp.replace(tmp, str1);
                        dataline++;
                    }

                } else if (tmp.contains("name:")) { // 替换用户:
                    if (nameline == 0) {
                        tmp = tmp
                                .replace(tmp, "name:" + "\'"
                                        + "SuccessfulTransactionPerSecond"
                                        + "\'" + ",");
                        nameline++;
                    } else if (nameline == 1) {
                        tmp = tmp.replace(tmp, "name:" + "\'"
                                + "FailedTransactionsPerSecond" + "\'" + ",");
                        nameline++;
                    }
                }

                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));
            }

            PrintWriter printWriter = new PrintWriter(sourceFile);
            printWriter.write(strBuf.toString().toCharArray());
            bufReader.close();
            printWriter.flush();
            printWriter.close();
            strBuf.setLength(0); ;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * 生成成功数与失败数柱形对比图:
     */
    public void updateCylindricalContrast(String sourceFile,
                                          ArrayList<String> SucessCount, ArrayList<String> ErroyCount,
                                          ArrayList<String> runtime, String vuser) {

        // 控制每行的变量:
        int dataline = 0;

        int nameline = 0;

        try {
            BufferedReader bufReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(
                            sourceFile)),"utf-8"));
            StringBuffer strBuf = new StringBuffer();

            for (String tmp = null; (tmp = bufReader.readLine()) != null;) {

                // 替换线程:
                if (tmp.contains("categories")) { // 替换开始时间:

                    String str1 = "categories: [ ";
                    String str2 = "]";

                    for (int i = 0; i < runtime.size(); i++) {
                        if (i == runtime.size() - 1) {
                            str1 = str1 + "\'" + runtime.get(i) + "\'" + str2;
                        } else {
                            str1 = str1 + "\'" + runtime.get(i) + "\'" + ",";
                        }
                    }
                    tmp = tmp.replace(tmp, str1);

                } else if (tmp.contains("data")) { // 成功事务数每秒:

                    if (dataline == 0) {
                        String str1 = "data: [";
                        String str2 = "]";
                        for (int i = 0; i < SucessCount.size(); i++) {
                            if (i == SucessCount.size() - 1) {
                                str1 = str1 + SucessCount.get(i) + str2;
                            } else {
                                str1 = str1 + SucessCount.get(i) + ",";
                            }
                        }
                        tmp = tmp.replace(tmp, str1);
                        dataline++;
                    } else if (dataline == 1) {
                        String str1 = "data: [";
                        String str2 = "]";

                        for (int i = 0; i < ErroyCount.size(); i++) {
                            if (i == ErroyCount.size() - 1) {
                                str1 = str1 + ErroyCount.get(i) + str2;
                            } else {
                                str1 = str1 + ErroyCount.get(i) + ",";
                            }
                        }
                        tmp = tmp.replace(tmp, str1);
                        dataline++;
                    }

                } else if (tmp.contains("name:")) { // 替换用户:
                    if (nameline == 0) {
                        tmp = tmp.replace(tmp, "name:" + "\'"
                                + "NumberOfSuccessful" + "\'" + ",");
                        nameline++;
                    } else if (nameline == 1) {
                        tmp = tmp.replace(tmp, "name:" + "\'"
                                + "NumberOfFailures" + "\'" + ",");
                        nameline++;
                    }
                }

                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));
            }

            PrintWriter printWriter = new PrintWriter(sourceFile);
            printWriter.write(strBuf.toString().toCharArray());
            bufReader.close();
            printWriter.flush();
            printWriter.close();
            strBuf.delete(0, strBuf.length()) ;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * 生成成功数与失败数饼图: piechart
     */
    public void piechart(String sourceFile, String sucessRate, String failRate,
                         String vuser) {

        // 控制每行的变量:
        int dataline = 0;

        // int nameline = 0 ;

        try {
            BufferedReader bufReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(
                            sourceFile)),"utf-8"));
            StringBuffer strBuf = new StringBuffer();

            for (String tmp = null; (tmp = bufReader.readLine()) != null;) {

                // 替换线程:
                if (tmp.contains("data")) { // 成功事务数每秒:

                    if (dataline == 0) {
                        String str1 = "data: [[";
                        String str2 = "]]";
                        String str3 = "],[";

                        String str5 = str1 + "\'SuccessfulTransactionRate\'"
                                + "," + sucessRate + str3
                                + "\'FailTransactionRate\'" + "," + failRate
                                + str2;

                        tmp = tmp.replace(tmp, str5);
                        dataline++;
                    }

                }

                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));
            }

            PrintWriter printWriter = new PrintWriter(sourceFile);
            printWriter.write(strBuf.toString().toCharArray());
            bufReader.close();
            printWriter.flush();
            printWriter.close();
            strBuf.delete(0, strBuf.length()) ;

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /***
     * 修改jmeter的系统配置文件: jmeter.properties 参数:summariser.interval
     */
    public static void modiyInterval(String sourceFile, String findString,
                                     String updateString) {

        try {
            BufferedReader bufReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(
                            sourceFile)),"utf-8"));
            StringBuffer strBuf = new StringBuffer();

            for (String tmp = null; (tmp = bufReader.readLine()) != null;) {

                // 替换线程:
                if (tmp.contains(findString)) { // 成功事务数每秒:
                    tmp = tmp.replace(tmp, updateString);
                }

                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));
            }

            PrintWriter printWriter = new PrintWriter(sourceFile);
            printWriter.write(strBuf.toString().toCharArray());
            bufReader.close();
            printWriter.flush();
            printWriter.close();
            strBuf.delete(0, strBuf.length()) ;

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /***
     * 修改jmeter在linux不能生成报告的bug: user.properties 参数:jmeter.reportgenerator.temp_dir
     */
    public static void modiyCanReportGenerator(String sourceFile, String findString,
                                     String updateString) {
        //最后一行是否存在：不存在则insert  :jmeter.reportgenerator.temp_dir
        String temp_dir = GetFileFirstLastLine.getFileLastLine(sourceFile);
        if(!temp_dir.contains(findString)){
            try {
                FileWriter writer = new FileWriter(sourceFile, true);
                writer.write("\n");
                writer.write(updateString);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    /***
     * 为生成平均响应时间与最大响应时间的曲线:
     */
    public void updateMaxAvgResponsed(String sourceFile,
                                      ArrayList<String> AvgRespond, ArrayList<String> MaxRespond,
                                      ArrayList<String> runtime, String vuser) {

        // 控制每行的变量:
        int dataline = 0;

        int nameline = 0;

        try {
            BufferedReader bufReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(
                            sourceFile)),"utf-8"));
            StringBuffer strBuf = new StringBuffer();

            for (String tmp = null; (tmp = bufReader.readLine()) != null;) {

                // 替换线程:
                if (tmp.contains("categories")) { // 替换开始时间:

                    String str1 = "categories: [ ";
                    String str2 = "]";

                    for (int i = 0; i < runtime.size(); i++) {
                        if (i == runtime.size() - 1) {
                            str1 = str1 + "\'" + runtime.get(i) + "\'" + str2;
                        } else {
                            str1 = str1 + "\'" + runtime.get(i) + "\'" + ",";
                        }
                    }
                    tmp = tmp.replace(tmp, str1);

                } else if (tmp.contains("data")) { // 成功事务数每秒:

                    if (dataline == 0) {
                        String str1 = "data: [";
                        String str2 = "]";
                        for (int i = 0; i < AvgRespond.size(); i++) {
                            if (i == AvgRespond.size() - 1) {
                                str1 = str1 + AvgRespond.get(i) + str2;
                            } else {
                                str1 = str1 + AvgRespond.get(i) + ",";
                            }
                        }
                        tmp = tmp.replace(tmp, str1);
                        dataline++;
                    } else if (dataline == 1) {
                        String str1 = "data: [";
                        String str2 = "]";

                        for (int i = 0; i < MaxRespond.size(); i++) {
                            if (i == MaxRespond.size() - 1) {
                                str1 = str1 + MaxRespond.get(i) + str2;
                            } else {
                                str1 = str1 + MaxRespond.get(i) + ",";
                            }
                        }
                        tmp = tmp.replace(tmp, str1);
                        dataline++;
                    }

                }

                else if (tmp.contains("name:")) { // 替换用户:
                    if (nameline == 0) {
                        tmp = tmp.replace(tmp, "name:" + "\'"
                                + "AvgResponsedTime" + "\'" + ",");
                        nameline++;
                    } else if (nameline == 1) {
                        tmp = tmp.replace(tmp, "name:" + "\'"
                                + "MaxResponsedTime" + "\'" + ",");
                        nameline++;
                    }
                }

                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));
            }

            PrintWriter printWriter = new PrintWriter(sourceFile);
            printWriter.write(strBuf.toString().toCharArray());
            bufReader.close();
            printWriter.flush();
            printWriter.close();
            strBuf.delete(0, strBuf.length()) ;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * 为得到网络时间而写得方法,获取请求地址:
     */

    public String getRequestUrlMeth(String sourceFile, String findString) {

        String requestUrl = null;

        // findString  //HTTPSampler.domain

        try {
            BufferedReader bufReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(
                            sourceFile)),"UTF-8"));
            //		StringBuffer strBuf = new StringBuffer();


            for (String tmp = null; (tmp = bufReader.readLine()) != null;) {

                //		if(bufReader.readLine().contains("enabled=\"true\">") ){
                //			 System.out.println("tmp="+tmp) ;

                //	if(tmp.contains("enabled=\"true\">")){

                if (tmp.contains(findString)) { // 成功事务数每秒:

                    requestUrl = tmp.split(">")[1].split("<")[0];

                    if (requestUrl.contains("/")) {

                        requestUrl = tmp.split("/")[0];

                    }

                    if(!requestUrl.equals("") && requestUrl.contains("com")){

                        //		 System.out.println("requestUrl8=" + requestUrl);
                        bufReader.close();
                        return requestUrl ;
                    }else{
                        if(!requestUrl.equals("")){
                            bufReader.close();
                            return requestUrl ;
                        }
                    }
                }

                //    }

            }

            //		strBuf.append(tmp);
            //		strBuf.append(System.getProperty("line.separator"));
            //		}

            //		PrintWriter printWriter = new PrintWriter(sourceFile);
            //		printWriter.write(strBuf.toString().toCharArray());
            bufReader.close();
            //		printWriter.flush();
            //		printWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return requestUrl;

    }

    /***
     * 修改：
     */

    /***
     * 为得到网络时间而写得方法,获取请求地址:
     */
    public String getRequestUrlMethMody(String sourceFile, String findString) {

        String requestUrl = null;

        BufferedReader reader = null;

        BufferedWriter writer0;

        try {
            reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(new File(sourceFile)), "UTF-8"));
        } catch (UnsupportedEncodingException e1) {

            e1.printStackTrace();
        } catch (FileNotFoundException e1) {

            e1.printStackTrace();
        }

        String tempString = null;
        int line = 0;

        try {
            BufferedReader bufReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(
                            sourceFile,"utf-8"))));
            StringBuffer strBuf = new StringBuffer();

            for (String tmp = null; (tmp = bufReader.readLine()) != null;) {

                // 替换线程:
                if (tmp.contains(findString)) { // 成功事务数每秒:

                    requestUrl = tmp.split(">")[1].split("<")[0];

                    if (requestUrl.contains("/")) {

                        requestUrl = tmp.split("/")[0];

                    }


                }

                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));
            }

            PrintWriter printWriter = new PrintWriter(sourceFile);
            printWriter.write(strBuf.toString().toCharArray());
            bufReader.close();
            printWriter.flush();
            printWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return requestUrl;

    }

    /***
     * 得到事务名称:
     */
    public String getTransactionNameMeth(String sourceFile, String findString) {

        String TransactionName = "false";

        try {
            BufferedReader bufReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(
                            sourceFile)),"utf-8"));
            StringBuffer strBuf = new StringBuffer();

            for (String tmp = null; (tmp = bufReader.readLine()) != null;) {

                // 替换线程:
                if (tmp.contains(findString)) { // 成功事务数每秒:

                    TransactionName = tmp.split("testname=")[1].split("\"")[1]
                            .split("\"")[0];

                }

                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));
            }

            PrintWriter printWriter = new PrintWriter(sourceFile);
            printWriter.write(strBuf.toString().toCharArray());
            bufReader.close();
            printWriter.flush();
            printWriter.close();
            strBuf.delete(0, strBuf.length()) ;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return TransactionName;

    }




    /***
     * 修改脚本中高用jar,java,class的调用时修改其绝对路径:
     */
    public void modiyJarClassJavaPath(String sourceFile, String findStr,
                                      String replaceStr) {


        try {
            BufferedReader bufReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(
                            sourceFile)),"utf-8"));
            StringBuffer strBuf = new StringBuffer();

            for (String tmp = null; (tmp = bufReader.readLine()) != null;) {

                // 替换勾上"永远"，这样才能控制运行时间生效，这是先决条件：在最后面
                // 替换线程:
                if (tmp.contains(findStr)) {

                    System.out.println("findStr="+findStr) ;
                    System.out.println("replaceStr="+replaceStr) ;

                    tmp = tmp.replace(tmp,
                            "<stringProp name=\"TestPlan.user_define_classpath\">"
                                    + replaceStr + "</stringProp>");
                }


                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));

            }

            PrintWriter printWriter = new PrintWriter(sourceFile);
            printWriter.write(strBuf.toString().toCharArray());
            bufReader.close();
            printWriter.flush();
            printWriter.close();
            strBuf.delete(0, strBuf.length()) ;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }






    /***
     * 修改脚本中高用jmeter-agent的端口号，对于性能测试而言:
     */
    public static void modiyJmeterPropertiesPort(String sourceFile, String findStr,
                                      String replaceStr) {


        try {
            BufferedReader bufReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(
                            sourceFile)),"utf-8"));
            StringBuffer strBuf = new StringBuffer();

            for (String tmp = null; (tmp = bufReader.readLine()) != null;) {

                // 替换勾上"永远"，这样才能控制运行时间生效，这是先决条件：在最后面
                // 替换线程:
                if (tmp.contains(findStr)) {

                    System.err.println("findStr="+findStr) ;
                    System.err.println("replaceStr="+replaceStr) ;

                    tmp = tmp.replace(tmp,replaceStr );
                }


                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));

            }

            PrintWriter printWriter = new PrintWriter(sourceFile);
            printWriter.write(strBuf.toString().toCharArray());
            bufReader.close();
            printWriter.flush();
            printWriter.close();
            strBuf.delete(0, strBuf.length()) ;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    /***
     * 根据配置在jmeter启动之前先修改内存JVM:
     */
    public static void modiyJmeterPropertiesJvm(String sourceFile, String findStr, String replaceStr) {
        try {
            BufferedReader bufReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(
                            sourceFile)), "utf-8"));
            StringBuffer strBuf = new StringBuffer();

            String targer = "start" ;
            for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {

                // 替换勾上"永远"，这样才能控制运行时间生效，这是先决条件：在最后面
                // 替换线程:
                if (targer.equals("start") && tmp.contains(findStr) ) {
                    tmp = tmp.replace(tmp, replaceStr);
                    targer = "agentendover" ;
                }


                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));

            }

            PrintWriter printWriter = new PrintWriter(sourceFile);
            printWriter.write(strBuf.toString().toCharArray());
            bufReader.close();
            printWriter.flush();
            printWriter.close();
            strBuf.delete(0, strBuf.length());

        } catch (IOException e) {
            e.printStackTrace();
        }


    }




}
