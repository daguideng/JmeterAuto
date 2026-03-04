package com.atguigu.gmall.Impl.perfImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * @Author: dengdagui
 * @Description:直接获取Html中的报告中获取数据：
 * @Date: Created in 2018-8-3
 */
@Slf4j
@Component
public class ReadWriteReportData {



    public String modiyInterval(String sourceFile, String findString) throws Exception {
    String statisticsTable = null;

    try {
        BufferedReader bufReader = new BufferedReader(
                new InputStreamReader(new FileInputStream(new File(
                        sourceFile)), "utf-8"));

        for (String tmp = null; (tmp = bufReader.readLine()) != null;) {
            if (tmp.contains(findString)) {
                statisticsTable = tmp.split("(\"#statisticsTable\")\\),")[1].split(", function")[0];
                System.out.println("first----->"+statisticsTable);
                
                // 替换非法JSON值
                statisticsTable = statisticsTable.replace("Infinity", "0")
                                               .replace("NaN", "0")
                                               .replace("null", "0")
                                               .replace(": ,", ": 0,")  // 处理空值
                                               .replace(": ,", ": 0,"); // 处理连续逗号

                System.out.println("second----->"+statisticsTable);
            }
        }
        bufReader.close();

    } catch (Exception e) {
        throw new Exception("未生成HTML报告,搜索报告不成功!!!");
    }

    return statisticsTable;
}


    /***
     * 为 top5ErrorsBySamplerTable 生成数据入库：
     * @param sourceFile
     * @param findString
     * @return
     * @throws Exception
     */
    public String  top5ErrorsBySamplerData(String sourceFile, String findString) throws Exception {

        String statisticsTable = null;

        try {
            BufferedReader bufReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(
                            sourceFile)), "utf-8"));

            for (String tmp = null; (tmp = bufReader.readLine()) != null;) {
                if (tmp.contains(findString)) {
                    statisticsTable = tmp.split("(\"#top5ErrorsBySamplerTable\")\\),")[1].split(", function")[0];
                }


            }

            bufReader.close();

        } catch (Exception e) {


            throw new Exception("未生成HTML报告,搜索报告不成功!!!");

        }


        return statisticsTable ;

    }


    /***
     * 被其它程序调用
     * @param args
     */



    public static void main(String args[]) throws Exception {

        ReadWriteReportData  test = new ReadWriteReportData();

         String fileStr = "D:\\mars\\ReportResultDir\\20180820132300\\jiao_verify_20180820132300_1_1\\jiao_verify_20180820132300_report\\content\\js\\dashboard.js" ;
        //dashboard.js
        fileStr = fileStr.replace("\\","/");

        System.out.println("fileStr===>"+fileStr) ;
        String restul = test.modiyInterval(fileStr, "statisticsTable");

        System.out.println("restul===>"+restul) ;

    }



}
