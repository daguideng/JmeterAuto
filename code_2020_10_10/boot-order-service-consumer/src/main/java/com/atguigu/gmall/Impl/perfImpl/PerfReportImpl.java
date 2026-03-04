package com.atguigu.gmall.Impl.perfImpl;

import com.atguigu.gmall.common.utils.DateUtil;
import com.atguigu.gmall.controller.perf.JmeterPerfCurrentReportController;
import com.atguigu.gmall.dao.Upload_infoMapper;
import com.atguigu.gmall.entity.Interface_current_report;
import com.atguigu.gmall.entity.Upload_info;
import com.atguigu.gmall.service.jmeterperf.JmeterperformcurrentreportServer;
import com.atguigu.gmall.service.jmeterperf.JmeterperformhistoryreportServer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.util.Arrays.sort;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@Slf4j
public class PerfReportImpl {


    @Autowired
    private JmeterperformcurrentreportServer jmeterperformcurrentreportServer ;

    @Autowired
    private JmeterperformhistoryreportServer jmeterperformhistoryreportServer ;

    @Autowired
    private Upload_infoMapper upload_infoMapper;

    public String  getReportContent(String headPath) throws IOException {

        StringBuilder headstb = new StringBuilder(800);

        FileReader fileReader = null;
        BufferedReader bufReader = null;

        try {
            // 尝试直接从文件系统加载模板文件
            String templatePath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + headPath;
            File templateFile = new File(templatePath);

            if (templateFile.exists()) {
                log.info("直接从文件系统加载模板: {}", templatePath);
                fileReader = new FileReader(templateFile);
                bufReader = new BufferedReader(fileReader);
            } else {
                // 如果文件不存在，回退到classpath加载
                log.info("从classpath加载模板: {}", headPath);
                InputStream sourceFile = PerfReportImpl.class.getClassLoader().getResourceAsStream(headPath);
                bufReader = new BufferedReader(new InputStreamReader(sourceFile, "utf-8"));
            }

            for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {
                headstb.append(tmp);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufReader != null) {
                bufReader.close();
            }
            if (fileReader != null) {
                fileReader.close();
            }
        }

        return headstb.toString();
    }


    /***
     * summary.html  ,开始时间等
     *
     */
    public Map<String,String>  getSummaryInfo(String uploadid,String lastruntime, String scriptname){
        log.info("===== 开始执行getSummaryInfo方法 =====");
        log.info("传入参数 - id: {}, 类型: {}",      uploadid != null ? uploadid.getClass().getName() : "null");
        log.info("传入参数 - lastruntime: {}, 类型: {}", lastruntime, lastruntime != null ? lastruntime.getClass().getName() : "null");
        log.info("传入参数 - scriptname: {}, 类型: {}", scriptname, scriptname != null ? scriptname.getClass().getName() : "null");

        Map<String,String> map = new HashMap<>();

        System.err.println("uploadid: " + uploadid);
        System.err.println("lastruntime: " + lastruntime);
        log.info("准备调用jmeterperformcurrentreportServer.linkTypeReport方法");
        List<Map<String, Object>> list = jmeterperformcurrentreportServer.linkTypeReport(uploadid,lastruntime);


       Map<String, Object> reportData = new HashMap<>();
            if (list != null && !list.isEmpty()) {
                // 取第一个元素的时间信息
                Map<String, Object> firstData = list.get(0);
                reportData.put("starttime", firstData.get("starttime"));
                // 取最后一个元素的结束时间
                Map<String, Object> lastData = list.get(list.size() - 1);
                reportData.put("endtime", lastData.get("endtime"));
                reportData.put("data", list);
            }

            if (reportData != null && reportData.containsKey("starttime")) {
                String starttime = String.valueOf(reportData.get("starttime"));
                String endtime = String.valueOf(reportData.get("endtime"));
                Long elapsedstart = Long.valueOf(DateUtil.date2TimeStamp(starttime,"yyyy-MM-dd HH:mm:ss"));
                Long elapsedend = Long.valueOf(DateUtil.date2TimeStamp(endtime,"yyyy-MM-dd HH:mm:ss"));
                String elapsedtime = String.valueOf(elapsedend - elapsedstart);

         


                map.put("starttime",starttime);
                map.put("endtime",endtime);
                map.put("elapsedtime",elapsedtime+" ms");



                //统计成功数：
                int sucessCount = jmeterperformcurrentreportServer.countByStatus( uploadid, lastruntime, scriptname, "0.00");
                map.put("succesCount",String.valueOf(sucessCount));
                //统计失败数:
                int failCount = jmeterperformcurrentreportServer.countByStatus( uploadid, lastruntime, scriptname, "1.00");
                map.put("failCount",String.valueOf(failCount));
                //统计警告数:
                int warningCount = jmeterperformcurrentreportServer.countByStatus( uploadid, lastruntime, scriptname, "2.00");
                map.put("warningCount",String.valueOf(warningCount));
            } 


        log.info("接口测试报告返回的list: {}", list);
        log.info("查询成功数与失败组成的map结果: {}", map);
        log.info("===== getSummaryInfo方法执行结束 =====");

        return map ;



    }



    /***
     * summary.html  ,历史接口汇总信息:只显示Total:
     *
     */
    public Map<String,Object>  getInterHistorySummaryInfo(String id,String scriptname){

        Map<String,Object> map = new HashMap<>();

        System.err.println(id);
        System.err.println(scriptname);
        List<Map<String, Object>> reportDataList = jmeterperformhistoryreportServer.linkTypeReport(id,scriptname);

            Map<String, Object> reportData = new HashMap<>();
            if (reportDataList != null && !reportDataList.isEmpty()) {
                // 取第一个元素的时间信息
                Map<String, Object> firstData = reportDataList.get(0);
                reportData.put("starttime", firstData.get("starttime"));
                // 取最后一个元素的结束时间
                Map<String, Object> lastData = reportDataList.get(reportDataList.size() - 1);
                reportData.put("endtime", lastData.get("endtime"));
                reportData.put("data", reportDataList);
            }

            if (reportData != null && reportData.containsKey("starttime")) {
                String starttime = String.valueOf(reportData.get("starttime"));
                String endtime = String.valueOf(reportData.get("endtime"));
                Long elapsedstart = Long.valueOf(DateUtil.date2TimeStamp(starttime,"yyyy-MM-dd HH:mm:ss"));
                Long elapsedend = Long.valueOf(DateUtil.date2TimeStamp(endtime,"yyyy-MM-dd HH:mm:ss"));
                String elapsedtime = String.valueOf(elapsedend - elapsedstart);

                map.put("starttime",starttime);
                map.put("endtime",endtime);
                map.put("elapsedtime",elapsedtime+" ms");
                map.put("data", reportData.get("data"));


                //统计成功数：
                // 这里应该传递lastruntime参数，但当前上下文没有合适的lastruntime值
                // 暂时使用空字符串，实际应用中需要从合适的地方获取lastruntime值
                int sucessCount = jmeterperformcurrentreportServer.countByStatus( id, "", scriptname, "0.00");
                map.put("succesCount",String.valueOf(sucessCount));
                //统计失败数:
                int failCount = jmeterperformcurrentreportServer.countByStatus( id, "", scriptname, "1.00");
                map.put("failCount",String.valueOf(failCount));
            } else {
                log.warn("No data found for historyLinkTypeReport with id: {} and scriptname: {}", id, scriptname);
                map.put("error", "No data found");
            }



        log.info("接口测试报告返回的reportData:{}", reportData);

        log.info("查询成功数与失败组成的map结果:{}", map);

        return map ;

    }


    /***
     * 详细列表信息：
     */
    public List<String>  getDetailReport(String uploadid, String lastruntime){

        String scriptname = "";
        
        // 根据uploadid从upload_info表查询scriptname
        try {
            Integer uploadId = Integer.parseInt(uploadid);
            Upload_info uploadInfo = upload_infoMapper.selectByPrimaryKey(uploadId);
            if (uploadInfo != null) {
                scriptname = uploadInfo.getScriptname();
                log.info("从upload_info表查询到scriptname: {}", scriptname);
            } else {
                log.warn("未找到upload_info表中id={}的记录", uploadId);
            }
        } catch (NumberFormatException e) {
            log.error("uploadid参数格式错误: {}", uploadid, e);
        }

       // 获取报告数据
        Integer uploadId = null;
        try {
            uploadId = Integer.parseInt(uploadid);
        } catch (NumberFormatException e) {
            log.error("uploadid参数格式错误: {}", uploadid, e);
        }
        List<Map<String, Object>> detailReport = jmeterperformcurrentreportServer.detailQueryTypeReport(lastruntime, scriptname, uploadId);
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

       // StringBuilder  sb = new StringBuilder(500);

        
        // 调用新方法查询jmeter_perfor_history_report表中label包含"Total"且uploadid匹配的数据
        List<Map<String, Object>> detailReport = jmeterperformcurrentreportServer.queryHistoryReportByLabelAndUploadId("Total", uploadId);
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






     public  List<String>  getDetailQueryPerfEmailTypeReport(String lastruntime, List<Integer> uploadId){

        log.info("lastruntime888={}", lastruntime);
        log.info("uploadId888={}", uploadId);

       List<Map<String, Object>> detailReport = jmeterperformcurrentreportServer.detailQueryTypePerEmailfReport(lastruntime, uploadId);

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
     * summary.html  ,开始时间等
     *
     */
    public Map<String,Object>  getPerfSummaryInfo(String lastruntime,List<Integer> uploadId){

        Map<String,Object> map = new HashMap<>();

        List<Map<String, Object>> detailReport = jmeterperformcurrentreportServer.sumPerfReport(lastruntime, uploadId);

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

}
