package com.atguigu.gmall.controller.inter;

import com.atguigu.gmall.Impl.interfaceImpl.InterReportImpl;
import com.atguigu.gmall.Interface.SendEmailReportIntel;
import com.atguigu.gmall.common.bean.response.Result;
import com.atguigu.gmall.common.utils.DateUtil;
import com.atguigu.gmall.service.jmeterinter.ReportService;
import com.atguigu.gmall.service.jmeterperf.StatisticsServer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;


/**
 * @Author: dengdagui
 * @Description:点起链接生成报告，
 * @Date: Created in 2018-10-29
 */
@Slf4j
@RestController
@RequestMapping("/jmeterinter")
//@Api(value = "性能报告模块", tags = "Mars-超链接简洁性能报告模块", description = "超链接简洁性能报告")
public class JmeterInterReportImpl {

    @Autowired
    @Qualifier("sendEmailInterReportImpl")
    private SendEmailReportIntel sendEmailIntereport;





    @Autowired
    private InterReportImpl interReportImpl ;

    @Autowired
    private ReportService reportService;

    //@ApiOperation(value = "perf：超链接简洁接口报告")
    /**
     * 接口测试报告生成接口
     * @param id 测试ID
     * @param lastruntime 最后运行时间戳
     * @param response HTTP响应
     * @return 报告内容
     */
    @RequestMapping(value = "/link/list", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String simpleInterReportList(
            @RequestParam(required = true) String id, 
            @RequestParam(required = true) String lastruntime,
            HttpServletResponse response) {
        log.info("接口测试报告请求: id={}, lastruntime={}", id, lastruntime);

        try {
            // 设置响应字符编码
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            
            // 调用Service层生成报告（不传递HttpServletResponse参数）
          //  String reportContent = reportService.generateReport(id, lastruntime);
            String[] ids = id.split(",");
            String reportContent = reportService.generateInterReport(ids, lastruntime);
            
            if (reportContent != null && !reportContent.isEmpty()) {
                // 确保HTML包含正确的字符编码声明
                if (!reportContent.contains("charset=")) {
                    reportContent = reportContent.replace("<head>", "<head><meta charset=\"UTF-8\">");
                }
                
                return reportContent;
            } else {
                log.warn("报告内容为空");
                return "<html><head><meta charset=\"UTF-8\"></head><body><h1>报告内容为空</h1></body></html>";
            }
        } catch (Exception e) {
            log.error("生成报告失败: {}", e.getMessage(), e);
            // 返回错误信息
            return "<html><head><meta charset=\"UTF-8\"></head><body><h1>报告生成失败</h1><p>" + e.getMessage() + "</p></body></html>";
        }
    }


    //@ApiOperation(value = "inter：历史报告")
    @RequestMapping(value = "/link/historylist", method = RequestMethod.GET)
    public StringBuilder simpleHistoryReportList(@RequestParam(required = true) String id, @RequestParam(required = true) String scriptname, HttpServletResponse response) throws Exception {


        Result<Object> result = new Result<>();

        log.info("输入的查询条件filter:{}", id);
        log.info("输入的查询条件filter:{}", scriptname);


        StringBuilder sbf = new StringBuilder(800);
        InputStream sourceFile = null;
        BufferedReader bufReader = null;

        //1. heads
        String heads = interReportImpl.getReportContent("templates/properties/PerfReport/head.html");
        heads = String.format(heads, "接口测试历史报告");
        sbf.append(heads);

        //2.summary
       // lastruntime = DateUtil.timeStamp2Date(lastruntime, "yyyy-MM-dd HH:mm:ss");
        if(null != id) {
            String summary = interReportImpl.getReportContent("templates/properties/PerfReport/summary.html");
            if (summary != null) {
                //  System.out.println("lastruntime--->" + lastruntime);
                Map<String, String> map = interReportImpl.getInterHistorySummaryInfo(id, scriptname);
                log.info("历史报告摘要信息: {}", map);
                
                // 确保map中的值不为null
                String starttime = map.get("starttime") != null ? map.get("starttime") : "";
                String endtime = map.get("endtime") != null ? map.get("endtime") : "";
                String elapsedtime = map.get("elapsedtime") != null ? map.get("elapsedtime") : "";
                String succesCount = map.get("succesCount") != null ? map.get("succesCount") : "0";
                String failCount = map.get("failCount") != null ? map.get("failCount") : "0";
                
                summary = MessageFormat.format(summary, starttime, endtime, elapsedtime, succesCount, failCount, "0", "0");
                log.info("格式化后的摘要: {}", summary);

                sbf.append(summary);
            } else {
                log.warn("摘要模板文件未找到或为空");
            }
        }

        //3.main
        String main = interReportImpl.getReportContent("templates/properties/PerfReport/main.html");
        Integer uploadId = null;
        if (id != null && !id.trim().isEmpty()) {
            try {
                uploadId = Integer.parseInt(id);
            } catch (NumberFormatException e) {
                log.error("ID参数格式错误: {}", id, e);
                throw new IllegalArgumentException("ID参数格式错误: " + id);
            }
        }
        
        List<String> detailList = new ArrayList<>();
        if (uploadId != null) {
            detailList = interReportImpl.getHistoryDetailReport(scriptname, uploadId);
        } else {
            log.warn("uploadId为null，跳过历史报告查询");
        }

        StringBuilder lineReport = new StringBuilder();

        for(int i = 0 ; i<detailList.size();i++ ){
            // id;
            lineReport.append(detailList.get(i));

        }

       // main = MessageFormat.format(main, lineReport.toString(), "default");
        main = main.replace("{0}", lineReport.toString());
        sbf.append(main);



        //4.js
        String js = interReportImpl.getReportContent("templates/properties/PerfReport/js.html");
        sbf.append(js);


        System.out.println("main---->"+sbf.toString());

        if(id !=null) {
            response.setHeader("ContentType", "text/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(sbf.toString());
        }else{
            return sbf ;
        }


        return null ;

    }


    /**
     * java读文件：
     */
    @SneakyThrows
    public StringBuilder getReportContent() throws IOException {

        StringBuffer sbf = new StringBuffer(800);

        InputStream sourceFile = null;
        BufferedReader bufReader = null;

        try {

            //读入emailReport的主要head内容：
            sourceFile = JmeterInterReportImpl.class.getClassLoader().getResourceAsStream("templates/properties/PerfReport/head.html");
            bufReader = new BufferedReader(new InputStreamReader(sourceFile, "utf-8"));

            for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {


            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            bufReader.close();
            sourceFile.close();
        }


        return null;
    }


}
