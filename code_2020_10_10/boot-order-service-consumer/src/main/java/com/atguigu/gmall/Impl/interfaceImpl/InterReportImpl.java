package com.atguigu.gmall.Impl.interfaceImpl;

import com.atguigu.gmall.common.utils.DateUtil;
import com.atguigu.gmall.controller.inter.JmeterInterReportImpl;
import com.atguigu.gmall.entity.Interface_current_report;
import com.atguigu.gmall.service.jmeterinter.JmeterIntercurrentreportServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.util.Arrays.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@Slf4j
public class InterReportImpl {


    @Autowired
    private JmeterIntercurrentreportServer jmeterIntercurrentreportServer ;


    public String getReportContent(String headPath) throws IOException {

        StringBuilder headstb = new StringBuilder(800);

        InputStream sourceFile = null;
        BufferedReader bufReader = null;

        try {

            //读入emailReport的主要head内容：
         //   sourceFile = JmeterInterReportImpl.class.getClassLoader().getResourceAsStream("templates/properties/report/head.html");
            sourceFile = JmeterInterReportImpl.class.getClassLoader().getResourceAsStream(headPath);
            bufReader = new BufferedReader(new InputStreamReader(sourceFile, "utf-8"));

            for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {

                headstb.append(tmp);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            bufReader.close();
            sourceFile.close();
        }

        return headstb.toString();
    }


    /***
     * summary.html  ,开始时间等
     *
     */
    public Map<String,Object>  getSummaryInfo(String id,String lastruntime){

        Map<String,Object> map = new HashMap<>();

        System.err.println(id);
        System.err.println(lastruntime);
        List<Interface_current_report> list = jmeterIntercurrentreportServer.linkTypeReport(id,lastruntime);
        for(Interface_current_report total : list){
            String starttime = total.getStarttime();
            String endtime = total.getEndtime();
            // 安全解析时间戳，处理可能的格式异常
            Long elapsedstart = 0L;
            Long elapsedend = 0L;
            try {
                String startTimestamp = DateUtil.date2TimeStamp(starttime, "yyyy-MM-dd HH:mm:ss");
                if (!startTimestamp.isEmpty()) {
                    elapsedstart = Long.valueOf(startTimestamp);
                }
                String endTimestamp = DateUtil.date2TimeStamp(endtime, "yyyy-MM-dd HH:mm:ss");
                if (!endTimestamp.isEmpty()) {
                    elapsedend = Long.valueOf(endTimestamp);
                }
            } catch (NumberFormatException e) {
                System.err.println("Failed to parse timestamp: " + e.getMessage());
            }
            String elapsedtime = String.valueOf(elapsedend - elapsedstart);

            map.put("starttime",total.getStarttime());
            map.put("endtime",total.getEndtime());
            map.put("elapsedtime",elapsedtime+" ms");



            //统计成功数：
            List<Interface_current_report> sucessCount = jmeterIntercurrentreportServer.runSucessFail( id, lastruntime, "0.00");
            map.put("succesCount",String.valueOf(sucessCount.size()));
            //统计失败数:
            List<Interface_current_report> failCount = jmeterIntercurrentreportServer.runSucessFail( id, lastruntime, "1.00");
            map.put("failCount",String.valueOf(failCount.size()));


        }

        log.info("接口测试报告返回的list:{}", list);

        log.info("查询成功数与失败组成的map结果:{}", map);

        return map ;

    }



    /***
     * summary.html  ,历史接口汇总信息:只显示Total:
     *
     */
    public Map<String,String>  getInterHistorySummaryInfo(String id,String scriptname){

        Map<String,String> map = new HashMap<>();

        System.err.println(id);
        System.err.println(scriptname);
        List<Interface_current_report> list = jmeterIntercurrentreportServer.historyLinkTypeReport(id,scriptname);

            String starttime = list.get(0).getStarttime();
            String endtime = list.get(list.size()-1).getEndtime();
            // 安全解析时间戳，处理可能的格式异常
            Long elapsedstart = 0L;
            Long elapsedend = 0L;
            try {
                String startTimestamp = DateUtil.date2TimeStamp(starttime, "yyyy-MM-dd HH:mm:ss");
                if (!startTimestamp.isEmpty()) {
                    elapsedstart = Long.valueOf(startTimestamp);
                }
                String endTimestamp = DateUtil.date2TimeStamp(endtime, "yyyy-MM-dd HH:mm:ss");
                if (!endTimestamp.isEmpty()) {
                    elapsedend = Long.valueOf(endTimestamp);
                }
            } catch (NumberFormatException e) {
                System.err.println("Failed to parse timestamp: " + e.getMessage());
            }
            String elapsedtime = String.valueOf(elapsedend - elapsedstart);

            map.put("starttime",starttime);
            map.put("endtime",endtime);
            map.put("elapsedtime",elapsedtime+" ms");



            //统计成功数：
            List<Interface_current_report> sucessCount = jmeterIntercurrentreportServer.historyRunSucessFail( id, scriptname, "0.00");
            map.put("succesCount",String.valueOf(sucessCount.size()));
            //统计失败数:
            List<Interface_current_report> failCount = jmeterIntercurrentreportServer.historyRunSucessFail( id, scriptname, "1.00");
            map.put("failCount",String.valueOf(failCount.size()));



        log.info("接口测试报告返回的list:{}", list);

        log.info("查询成功数与失败组成的map结果:{}", map);

        return map ;

    }


    /***
     * 详细列表信息：
     */
    public List<String>  getDetailReport(String lastruntime, Integer uploadid){

        StringBuilder  sb = new StringBuilder(500);

        List<Map<String, Object>> detailReport = jmeterIntercurrentreportServer.detailQueryTypeReport(lastruntime, uploadid);
      
        

         List<String> list = new ArrayList<>(500);

        // 遍历每个报告项
        for (Map<String, Object> reportMap : detailReport) {
            // 生成表格行
            list.add("<tr>");
            list.add("<td>" + reportMap.get("id") + "</td>");
            list.add("<td>" + reportMap.get("starttime") + "</td>");
            list.add("<td>" + reportMap.get("scriptname") + "</td>");
            list.add("<td>" + reportMap.get("threads") + "</td>");
            list.add("<td>" + reportMap.get("label") + "</td>");
            list.add("<td>" + reportMap.get("samples") + "</td>");
            list.add("<td>" + reportMap.get("ko") + "</td>");
            list.add("<td>" + reportMap.get("error") + "</td>");
            list.add("<td>" + reportMap.get("min") + "</td>");
            list.add("<td>" + reportMap.get("max") + "</td>");
            list.add("<td>" + reportMap.get("median") + "</td>");
            list.add("<td>" + reportMap.get("thpct90") + "</td>");
            list.add("<td>" + reportMap.get("thpct95") + "</td>");
            list.add("<td>" + reportMap.get("thpct99") + "</td>");
            list.add("<td>" + reportMap.get("average") + "</td>");
            list.add("<td>" + reportMap.get("throughput") + "</td>");
            list.add("<td>" + reportMap.get("received") + "</td>");
            list.add("<td>" + reportMap.get("sent") + "</td>");

            // 根据Ko值设置状态样式
            String koValue = String.valueOf(reportMap.get("ko"));
            String statusClass;
            String statusText;
            switch (koValue) {
                case "0.00":
                    statusClass = "status-pass";
                    statusText = "pass";
                    break;
                case "1.00":
                    statusClass = "status-fail";
                    statusText = "fail";
                    break;
                case "2.00":
                    statusClass = "status-warning";
                    statusText = "warning";
                    break;
                default:
                    statusClass = "status-except";
                    statusText = "exception";
            }
            list.add("<td class=\"" + statusClass + "\">" + statusText + "</td>");

            list.add("<td>" + reportMap.get("ip") + "</td>");
            list.add("</tr>");
        }

        return list ;


    }




    /***
     * 历史详细列表信息：
     */
    public List<String>  getHistoryDetailReport(String scriptname, Integer uploadId){


        List<Map<String, Object>> detailReport =  jmeterIntercurrentreportServer.detailHistoryQueryTypeReport(scriptname, uploadId);
        List<String>  list = new ArrayList<>();
        for(Map<String, Object>  reportMap : detailReport){

            if(reportMap.get("label") != null && reportMap.get("label").toString().contains("Total")) {

            list.add("<tr>");
            list.add("<td>"+String.valueOf(reportMap.get("id"))+"</td>");
            list.add("<td>"+String.valueOf(reportMap.get("starttime"))+"</td>");
            list.add("<td>"+reportMap.get("scriptname")+"</td>");
            list.add("<td>"+reportMap.get("threads")+"</td>");
            list.add("<td>"+reportMap.get("label")+"</td>");
            list.add("<td>"+reportMap.get("samples")+"</td>");
            list.add("<td>"+reportMap.get("ko")+"</td>");
            list.add("<td>" + reportMap.get("error") + "</td>");
            list.add("<td>"+reportMap.get("min")+"</td>");
            list.add("<td>"+reportMap.get("max")+"</td>");
            list.add("<td>"+reportMap.get("median")+"</td>");
            list.add("<td>"+reportMap.get("thpct90")+"</td>");
            list.add("<td>"+reportMap.get("thpct95")+"</td>");
            list.add("<td>"+reportMap.get("thpct99")+"</td>");
            list.add("<td>"+reportMap.get("average")+"</td>");
            list.add("<td>"+reportMap.get("throughput")+"</td>");
            list.add("<td>"+reportMap.get("received")+"</td>");
            list.add("<td>"+reportMap.get("sent")+"</td>");

                String koValue = String.valueOf(reportMap.get("ko"));
                String statusClass;
                String statusText;
                switch (koValue) {
                    case "0.00":
                        statusClass = "status-pass";
                        statusText = "pass";
                        break;
                    case "1.00":
                        statusClass = "status-fail";
                        statusText = "fail";
                        break;
                    case "2.00":
                        statusClass = "status-warning";
                        statusText = "warning";
                        break;
                    default:
                        statusClass = "status-except";
                        statusText = "exception";
                }
                list.add("<td class=\"" + statusClass + "\">" + statusText + "</td>");

                list.add("<td>" + reportMap.get("ip") + "</td>");

                list.add("</tr>");

            }


        }


        return list ;

    }




     /***
     * summary.html  ,开始时间等
     *
     */
    public Map<String,Object>  getInterSummaryInfo(String lastruntime,List<Integer> uploadId){

        Map<String,Object> map = new HashMap<>();



        List<Map<String, Object>> detailReport = jmeterIntercurrentreportServer.sumInterReport(lastruntime, uploadId);

        for(Object total : detailReport){
            Map<String, Object> row = (Map<String, Object>) total;
            
            // 安全转换时间字段为字符串
            String starttime = row.get("starttime") != null ? row.get("starttime").toString() : "";
            String endtime = row.get("endtime") != null ? row.get("endtime").toString() : "";
            String elapsedtime = row.get("elapsedtime") != null ? row.get("elapsedtime").toString() : "";
            String sucessCount = row.get("sucessCount") != null ? row.get("sucessCount").toString() : "0";
            String failCount = row.get("failCount") != null ? row.get("failCount").toString() : "0";

            map.put("starttime",starttime);
            map.put("endtime",endtime);
            map.put("elapsedtime",elapsedtime);
            map.put("sucessCount",sucessCount);
            map.put("failCount",failCount);

        }

        log.info("接口测试报告返回的detailReport:{}", detailReport);

        log.info("查询成功数与失败组成的map结果:{}", map);

        return map ;

    }



    public  List<String>  getDetailQueryInterEmailTypeReport(String lastruntime, List<Integer> uploadId){

        log.info("lastruntime888={}", lastruntime);
        log.info("uploadId888={}", uploadId);

       List<Map<String, Object>> detailReport = jmeterIntercurrentreportServer.detailQueryTypeInterEmailfReport(lastruntime, uploadId);

        List<String> list = new ArrayList<>(500);

        // 遍历每个报告项
        for (Map<String, Object> reportMap : detailReport) {
            // 生成表格行
            list.add("<tr>");
            list.add("<td>" + reportMap.get("id") + "</td>");
            list.add("<td>" + reportMap.get("starttime") + "</td>");
            list.add("<td>" + reportMap.get("scriptname") + "</td>");
            list.add("<td>" + reportMap.get("threads") + "</td>");
            list.add("<td>" + reportMap.get("label") + "</td>");
            list.add("<td>" + reportMap.get("samples") + "</td>");
            list.add("<td>" + reportMap.get("ko") + "</td>");
            list.add("<td>" + reportMap.get("error") + "</td>");
            list.add("<td>" + reportMap.get("min") + "</td>");
            list.add("<td>" + reportMap.get("max") + "</td>");
             list.add("<td>" + reportMap.get("median") + "</td>");
            list.add("<td>" + reportMap.get("thpct90") + "</td>");
            list.add("<td>" + reportMap.get("thpct95") + "</td>");
            list.add("<td>" + reportMap.get("thpct99") + "</td>");
            list.add("<td>" + reportMap.get("average") + "</td>");
            list.add("<td>" + reportMap.get("throughput") + "</td>");
            list.add("<td>" + reportMap.get("received") + "</td>");
            list.add("<td>" + reportMap.get("sent") + "</td>");

            // 根据Ko值设置状态样式
            String koValue = String.valueOf(reportMap.get("ko"));
            String statusClass;
            String statusText;
            switch (koValue) {
                case "0.00":
                    statusClass = "status-pass";
                    statusText = "pass";
                    break;
                case "1.00":
                    statusClass = "status-fail";
                    statusText = "fail";
                    break;
                case "2.00":
                    statusClass = "status-warning";
                    statusText = "warning";
                    break;
                default:
                    statusClass = "status-except";
                    statusText = "exception";
            }
            list.add("<td class=\"" + statusClass + "\">" + statusText + "</td>");

            list.add("<td>" + reportMap.get("ip") + "</td>");
            list.add("</tr>");
        }


        return list ;

    }



}
