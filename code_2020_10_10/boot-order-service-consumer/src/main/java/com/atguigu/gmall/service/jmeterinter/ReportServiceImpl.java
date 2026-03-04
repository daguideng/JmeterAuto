package com.atguigu.gmall.service.jmeterinter;

import com.atguigu.gmall.Impl.interfaceImpl.InterReportImpl;
import com.atguigu.gmall.Impl.perfImpl.PerfReportImpl;
import com.atguigu.gmall.common.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 接口测试报告服务实现类
 */
@Slf4j
@Service
public class ReportServiceImpl implements ReportService {

   

    @Autowired
    private InterReportImpl interReportImpl;

    @Autowired
    private PerfReportImpl perfReportImpl;


    @Override
    public String generateInterReport(String[] ids, String lastruntimeStrs) {
        // 将数组参数拼接成逗号分隔字符串，复用原有逻辑

        log.info("接口测试报告请求--1: ids={}, lastruntimeStrs={}", ids, lastruntimeStrs);
        System.out.println("接口测试报告请求--1");


         String lastruntime;
            if (lastruntimeStrs.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
                // 已经是格式化的日期时间字符串，直接使用
                lastruntime = lastruntimeStrs;
                log.info("使用已有的格式化时间: {}", lastruntime);
            } else {
                // 是时间戳，需要格式化
                lastruntime = DateUtil.timeStamp2Date(lastruntimeStrs, "yyyy-MM-dd HH:mm:ss");
                log.info("格式化后的运行时间: {}", lastruntime);
            }


        log.info("接口测试报告请求: ids={}, lastruntimeStrs={}", ids, lastruntime);
        System.out.println("ids.length=" + ids.length);
     
        StringBuilder reportBuilder = new StringBuilder(2000);

        try {
            // 1. 生成报告头部
            generateInterReportHeader(reportBuilder);

            // 2. 生成报告摘要（只使用第一个ID）
            generateInterReportSummary(reportBuilder, ids, lastruntime);

            // 3. 生成详细报告内容
            //generateDetailedReport(reportBuilder, idsArray, lastruntimeArray);

            generateDetailedInterReport(reportBuilder, ids, lastruntime);

            // 4. 添加JS脚本
            generateReportFooter(reportBuilder);

            log.info("接口测试报告生成成功，总长度: {}", reportBuilder.length());
            
        } catch (Exception e) {
            log.error("生成接口测试报告失败: {}", e.getMessage(), e);
            return generateErrorReport("报告生成失败: " + e.getMessage());
        }

        return reportBuilder.toString();



      
    }
 
    
    
    @Override
    public String generatePerfReport(String[] ids, String lastruntimeStrs) {
        // 将数组参数拼接成逗号分隔字符串，复用原有逻辑

        log.info("性能测试报告请求--2: ids={}, lastruntimeStrs={}", ids, lastruntimeStrs);
        System.out.println("性能测试报告请求--2");



        String lastruntime;
            if (lastruntimeStrs.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
                // 已经是格式化的日期时间字符串，直接使用
                lastruntime = lastruntimeStrs;
                log.info("使用已有的格式化时间: {}", lastruntime);
            } else {
                // 是时间戳，需要格式化
                lastruntime = DateUtil.timeStamp2Date(lastruntimeStrs, "yyyy-MM-dd HH:mm:ss");
                log.info("格式化后的运行时间: {}", lastruntime);
            }
       


        String[] idsArray = ids;
      //  String[] lastruntimeArray = lastruntimeStrs.split(",");

        log.info("性能测试报告请求: ids={}, lastruntimeStrs={}", ids, lastruntime);
       
      //  System.out.println("lastruntimeArray.length=" + lastruntimeArray.length);

        System.out.println("idsArray=" + idsArray);
     //   System.out.println("lastruntimeArray=" + lastruntimeArray[0]);


         // 参数长度校验

        StringBuilder reportBuilder = new StringBuilder(2000);

        try {
            // 1. 生成报告头部
            generatePerfReportHeader(reportBuilder);

            // 2. 生成报告摘要（只使用第一个ID）
            generatePerfReportSummary(reportBuilder, idsArray, lastruntime);

            // 3. 生成详细报告内容
            //generateDetailedReport(reportBuilder, idsArray, lastruntimeArray);

            generateDetailedPerfReport(reportBuilder, idsArray, lastruntime);

            // 4. 添加JS脚本
            generateReportFooter(reportBuilder);

            log.info("接口测试报告生成成功，总长度: {}", reportBuilder.length());
            
        } catch (Exception e) {
            log.error("生成接口测试报告失败: {}", e.getMessage(), e);
            return generateErrorReport("报告生成失败: " + e.getMessage());
        }

        return reportBuilder.toString();



      
    }
  
   

    /**
     * 生成报告头部
     */
    private void generateInterReportHeader(StringBuilder reportBuilder) {
        try {
            String headerContent = interReportImpl.getReportContent("templates/properties/InterReport/head.html");
            if (StringUtils.isEmpty(headerContent)) {
                log.warn("报告头部模板为空");
                headerContent = "<h1>接口测试报告</h1>";
            } else {
                headerContent = String.format(headerContent, "接口测试报告");
            }
            reportBuilder.append(headerContent);
            log.info("报告头部生成完成");
        } catch (Exception e) {
            log.error("读取报告头部模板失败: {}", e.getMessage());
            reportBuilder.append("<h1>接口测试报告</h1>");
        }
    }



     /**
     * 生成报告头部
     */
    private void generatePerfReportHeader(StringBuilder reportBuilder) {
        try {
            String headerContent = interReportImpl.getReportContent("templates/properties/PerfReport/head.html");
            if (StringUtils.isEmpty(headerContent)) {
                log.warn("报告头部模板为空");
                headerContent = "<h1>性能测试报告</h1>";
            } else {
                headerContent = String.format(headerContent, "性能测试报告");
            }
            reportBuilder.append(headerContent);
            log.info("报告头部生成完成");
        } catch (Exception e) {
            log.error("读取报告头部模板失败: {}", e.getMessage());
            reportBuilder.append("<h1>性能测试报告</h1>");
        }
    }

   



     /**
     * 生成报告摘要
     */
    private void generateInterReportSummary(StringBuilder reportBuilder, String[] idsArray, String lastruntime) {
        log.info("开始生成报告摘要 - IDs: {}, lastruntime: {}", Arrays.toString(idsArray), lastruntime);


      
        try {
          
             String summaryTemplate;
            try {
                summaryTemplate = interReportImpl.getReportContent("templates/properties/InterReport/summary.html");
            } catch (Exception e) {
                log.error("读取报告摘要模板失败: {}", e.getMessage());
                return;
            }
            
            if (StringUtils.isEmpty(summaryTemplate)) {
                log.warn("报告摘要模板为空");
                return;
            }

            // 使用第一个ID生成摘要
           
            // 处理ID数组参数，可能包含方括号
            List<Integer> list = new ArrayList<>();
            for (String idStr : idsArray) {
                // 移除方括号和空格
                String cleanId = idStr.replaceAll("[\\[\\] ]", "").trim();
                if (!cleanId.isEmpty()) {
                    try {
                        list.add(Integer.parseInt(cleanId));
                    } catch (NumberFormatException e) {
                        log.warn("ID参数格式错误，跳过: {}", idStr);
                    }
                }
            }


                      

            Map<String, Object> summaryMap = interReportImpl.getInterSummaryInfo(lastruntime, list);
            if (summaryMap == null || summaryMap.isEmpty()) {
            //    log.warn("报告摘要信息为空，ID: {}，使用默认值生成摘要", id);
                // 使用默认值生成摘要，而不是跳过
                summaryMap = new HashMap<>();
                summaryMap.put("starttime", lastruntime);
                summaryMap.put("endtime", lastruntime);
                summaryMap.put("elapsedtime", "elapsedtime");
                summaryMap.put("sucessCount", "0");
                summaryMap.put("failCount", "0");
            }

            // 安全转换时间字段为字符串
            String starttime = summaryMap.get("starttime") != null ? summaryMap.get("starttime").toString() : "";
            String endtime = summaryMap.get("endtime") != null ? summaryMap.get("endtime").toString() : "";
            String elapsedtime = summaryMap.get("elapsedtime") != null ? summaryMap.get("elapsedtime").toString() : "";
            String succesCount = summaryMap.get("sucessCount") != null ? summaryMap.get("sucessCount").toString() : "0";
            String failCount = summaryMap.get("failCount") != null ? summaryMap.get("failCount").toString() : "0";

            String summary = MessageFormat.format(summaryTemplate,
                    starttime,
                    endtime,
                    elapsedtime,
                    succesCount,
                    failCount,
                    "0",
                    "0");
            reportBuilder.append(summary);
            log.info("报告摘要生成完成");
            
        } catch (Exception e) {
            log.error("生成报告摘要失败: {}", e.getMessage());
        }
    }



    private void generateDetailedInterReport(StringBuilder reportBuilder, String[] idsArray, String lastruntimeStrs) {
        String mainTemplate;
        try {
            mainTemplate = interReportImpl.getReportContent("templates/properties/InterReport/main.html");
        } catch (Exception e) {
            log.error("读取报告主体模板失败: {}", e.getMessage());
            return;
        }
        
        if (StringUtils.isEmpty(mainTemplate)) {
            log.warn("报告主体模板为空");
            return;
        }

        StringBuilder detailedContent = new StringBuilder();
        int totalDetails = 0;



        // 处理ID数组参数，可能包含方括号
        List<Integer> list = new ArrayList<>();
        for (String idStr : idsArray) {
            // 移除方括号和空格
            String cleanId = idStr.replaceAll("[\\[\\] ]", "").trim();
            if (!cleanId.isEmpty()) {
                try {
                    list.add(Integer.parseInt(cleanId));
                } catch (NumberFormatException e) {
                    log.warn("ID参数格式错误，跳过: {}", idStr);
                }
            }
        }

         List<String> detailList = interReportImpl.getDetailQueryInterEmailTypeReport(lastruntimeStrs, list);

               
                if (detailList != null && !detailList.isEmpty()) {
                    for (String detail : detailList) {
                        detailedContent.append(detail);
                    }
                    totalDetails += detailList.size();
                    log.info(" detailList的详细报告条目数: {}", detailList.size());
                } else {
                    log.warn(" detailList的详细报告条目数: {}", detailList.size());
                }


        String finalContent = mainTemplate.replace("{0}", detailedContent.toString());
        reportBuilder.append(finalContent);
        log.info("详细报告生成完成，总条目数: {}", totalDetails);
    }

    


    
     /**
     * 生成报告摘要
     */
    private void generatePerfReportSummary(StringBuilder reportBuilder, String[] idsArray, String lastruntime) {
        log.info("开始生成报告摘要 - IDs: {}, lastruntime: {}", Arrays.toString(idsArray), lastruntime);
        
      
        try {
            

             String summaryTemplate;
            try {
                summaryTemplate = interReportImpl.getReportContent("templates/properties/PerfReport/summary.html");
            } catch (Exception e) {
                log.error("读取报告摘要模板失败: {}", e.getMessage());
                return;
            }
            
            if (StringUtils.isEmpty(summaryTemplate)) {
                log.warn("报告摘要模板为空");
                return;
            }

            // 使用第一个ID生成摘要
           
            

            List<Integer> list = new ArrayList<>();
            for (String idStr : idsArray) {
                // 移除方括号和空格
                String cleanId = idStr.replaceAll("[\\[\\] ]", "").trim();
                if (!cleanId.isEmpty()) {
                    try {
                        list.add(Integer.parseInt(cleanId));
                    } catch (NumberFormatException e) {
                        log.warn("ID参数格式错误，跳过: {}", idStr);
                    }
                }
            }


                      

            Map<String, Object> summaryMap = perfReportImpl.getPerfSummaryInfo(lastruntime, list);
            if (summaryMap == null || summaryMap.isEmpty()) {
            //    log.warn("报告摘要信息为空，ID: {}，使用默认值生成摘要", id);
                // 使用默认值生成摘要，而不是跳过
                summaryMap = new HashMap<>();
                summaryMap.put("starttime", lastruntime);
                summaryMap.put("endtime", lastruntime);
                summaryMap.put("elapsedtime", String.valueOf(Long.parseLong(lastruntime) - Long.parseLong(lastruntime)));
                summaryMap.put("succesCount", "0");
                summaryMap.put("failCount", "0");
            }

            // 安全转换时间字段为字符串
            String starttime = summaryMap.get("starttime") != null ? summaryMap.get("starttime").toString() : "";
            String endtime = summaryMap.get("endtime") != null ? summaryMap.get("endtime").toString() : "";
            String elapsedtime = summaryMap.get("elapsedtime") != null ? summaryMap.get("elapsedtime").toString() : "";
            String succesCount = summaryMap.get("sucessCount") != null ? summaryMap.get("sucessCount").toString() : "0";
            String failCount = summaryMap.get("failCount") != null ? summaryMap.get("failCount").toString() : "0";

            String summary = MessageFormat.format(summaryTemplate,
                    starttime,
                    endtime,
                    elapsedtime,
                    succesCount,
                    failCount,
                    "0",
                    "0");
            reportBuilder.append(summary);
            log.info("报告摘要生成完成");
            
        } catch (Exception e) {
            log.error("生成报告摘要失败: {}", e.getMessage());
        }
    }


    private void generateDetailedPerfReport(StringBuilder reportBuilder, String[] idsArray, String lastruntimeStrs) {
        String mainTemplate;
        try {
            mainTemplate = interReportImpl.getReportContent("templates/properties/PerfReport/main.html");
        } catch (Exception e) {
            log.error("读取报告主体模板失败: {}", e.getMessage());
            return;
        }
        
        if (StringUtils.isEmpty(mainTemplate)) {
            log.warn("报告主体模板为空");
            return;
        }

        StringBuilder detailedContent = new StringBuilder();
        int totalDetails = 0;


        // List<Integer> list = Arrays.stream(idsArray)
        //                   .map(Integer::parseInt)
        //                   .collect(Collectors.toList());

        // 处理ID数组参数，可能包含方括号
        List<Integer> list = new ArrayList<>();
        for (String idStr : idsArray) {
            // 移除方括号和空格
            String cleanId = idStr.replaceAll("[\\[\\] ]", "").trim();
            if (!cleanId.isEmpty()) {
                try {
                    list.add(Integer.parseInt(cleanId));
                } catch (NumberFormatException e) {
                    log.warn("ID参数格式错误，跳过: {}", idStr);
                }
            }
        }

         List<String> detailList = perfReportImpl.getDetailQueryPerfEmailTypeReport(lastruntimeStrs, list);

               
                if (detailList != null && !detailList.isEmpty()) {
                    for (String detail : detailList) {
                        detailedContent.append(detail);
                    }
                    totalDetails += detailList.size();
                    log.info(" detailList的详细报告条目数: {}", detailList.size());
                } else {
                    log.warn(" detailList的详细报告条目数: {}", detailList.size());
                }


        String finalContent = mainTemplate.replace("{0}", detailedContent.toString());
        reportBuilder.append(finalContent);
        log.info("详细报告生成完成，总条目数: {}", totalDetails);
    }

    /**
     * 生成报告页脚（JS脚本）
     */
    private void generateReportFooter(StringBuilder reportBuilder) {
        try {
            String jsContent = interReportImpl.getReportContent("templates/properties/InterReport/js.html");
            if (!StringUtils.isEmpty(jsContent)) {
                reportBuilder.append(jsContent);
                log.info("JS脚本添加完成");
            } else {
                log.warn("JS脚本模板为空");
            }
        } catch (Exception e) {
            log.error("读取JS脚本模板失败: {}", e.getMessage());
        }
    }

    /**
     * 解析ID参数
     */
    private Integer parseId(String id) {
        try {
            return Integer.parseInt(id.trim());
        } catch (NumberFormatException e) {
            log.error("ID参数格式错误: {}", id);
            throw new IllegalArgumentException("ID参数格式错误: " + id);
        }
    }

    /**
     * 生成错误报告
     */
    private String generateErrorReport(String errorMessage) {
        return "<h1>报告生成失败</h1><p>" + errorMessage + "</p>";
    }




    // 转换为 Integer[]
public static Integer[] convertToIntegerArray(String[] stringArray) {
    return Arrays.stream(stringArray)
            .map(Integer::parseInt)
            .toArray(Integer[]::new);
}

// 转换为 int[] (基本类型数组)
public static int[] convertToIntArray(String[] stringArray) {
    return Arrays.stream(stringArray)
            .mapToInt(Integer::parseInt)
            .toArray();
}
}