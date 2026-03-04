package com.atguigu.gmall.controller.perf;

import com.atguigu.gmall.Impl.perfImpl.PerfReportImpl;
import com.atguigu.gmall.Interface.SendEmailReportIntel;
import com.atguigu.gmall.common.bean.response.Result;
import com.atguigu.gmall.common.constant.RES_STATUS;
import com.atguigu.gmall.common.utils.DateUtil;
import com.atguigu.gmall.service.jmeterperf.StatisticsServer;
import com.atguigu.gmall.service.jmeterperf.JmeterperformcurrentreportServer;
import com.atguigu.gmall.dao.Upload_infoMapper;
import com.atguigu.gmall.entity.Upload_info;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.atguigu.gmall.service.jmeterinter.ReportService;


/**
 * @Author: dengdagui
 * @Description:点起链接生成报告，
 * @Date: Created in 2018-10-29
 */
@Slf4j
@RestController
@RequestMapping("/jmeterperf")
//@Api(value = "性能报告模块", tags = "Mars-超链接简洁性能报告模块", description = "超链接简洁性能报告")
public class JmeterPerflinkReport {

    @Autowired
    @Qualifier("sendEmailPerfReportImpl")
    private SendEmailReportIntel sendEmailPerfReport;


     @Autowired
    private PerfReportImpl perfReportImpl ;



    @Autowired
    private ReportService reportService;



    //@Autowired
    //StatisticsServer statisticsServer;

    @Autowired
    private Upload_infoMapper upload_infoMapper;

    @Autowired
    private JmeterperformcurrentreportServer jmeterperformcurrentreportServer;

    //@ApiOperation(value = "perf：超链接简洁性能报告")
    @RequestMapping(value = "/link/list", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String simplePerfReportList(
            @RequestParam(required = true) String id, 
            @RequestParam(required = true) String lastruntime,
            HttpServletResponse response) {
        log.info("性能测试报告请求: id={}, lastruntime={}", id, lastruntime);

        try {
            // 设置响应字符编码
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            
            // 调用Service层生成报告（不传递HttpServletResponse参数）
          //  String reportContent = reportService.generateReport(id, lastruntime);
            String[] ids = id.split(",");
            String reportContent = reportService.generatePerfReport(ids, lastruntime);
            
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
    public StringBuilder simpleHistoryReportList(@RequestParam(required = true) String id, HttpServletResponse response) throws Exception {


        Result<Object> result = new Result<>();

        log.info("输入的查询条件filter:{}", id);
        
        String scriptname = "";
         Integer uploadId = 0 ;
        
        // 根据uploadid从upload_info表查询scriptname
        try {
             uploadId = Integer.parseInt(id);
            Upload_info uploadInfo = upload_infoMapper.selectByPrimaryKey(uploadId);
            if (uploadInfo != null) {
                scriptname = uploadInfo.getScriptname();
                log.info("从upload_info表查询到scriptname: {}", scriptname);
            } else {
                log.warn("未找到upload_info表中id={}的记录", uploadId);
            }
        } catch (NumberFormatException e) {
            log.error("id参数格式错误: {}", id, e);
        }

        log.info("查询条件scriptname:{}", scriptname);


        StringBuilder sbf = new StringBuilder(800);
        InputStream sourceFile = null;
        BufferedReader bufReader = null;

        //1. heads
        String heads = perfReportImpl.getReportContent("templates/properties/PerfReport/head.html");
        heads = String.format(heads, "性能历史测试报告");
        sbf.append(heads);

        //2.summary
       // lastruntime = DateUtil.timeStamp2Date(lastruntime, "yyyy-MM-dd HH:mm:ss");
        if(null != id) {
            String summary = perfReportImpl.getReportContent("templates/properties/PerfReport/summary.html");

          //  System.out.println("lastruntime--->" + lastruntime);
            Map<String, Object> map = perfReportImpl.getInterHistorySummaryInfo(id, scriptname);
            System.out.println("map-->" + map);
            System.out.println("summary-->" + summary);
            //  summary = String.format(summary, "0","0","0","0","0","0","0");
            summary = MessageFormat.format(summary, map.get("starttime"), map.get("endtime"), map.get("elapsedtime"), map.get("succesCount"), map.get("failCount"), "0", "0");
            System.out.println("summary-->" + summary);

            sbf.append(summary);
        }

        //3.main
        String main = perfReportImpl.getReportContent("templates/properties/PerfReport/main.html");
        List<String> detailList = perfReportImpl.getHistoryDetailReport(scriptname, uploadId);

        // 添加对jmeter_perfor_current_report表的查询
        log.info("开始查询jmeter_perfor_current_report表，uploadId={}", uploadId);
        List<Map<String, Object>> currentReportData = jmeterperformcurrentreportServer.detailQueryTypeReport(null, scriptname, uploadId);
        log.info("当前报告表查询结果: {}条数据", currentReportData.size());

        StringBuilder reportList = new StringBuilder();
        StringBuilder lineReport = new StringBuilder();

        for(int i = 0 ; i<detailList.size();i++ ){
            // id;
            lineReport.append(detailList.get(i));

        }

        main = main.replace("{0}", lineReport.toString());
        sbf.append(main);



        //4.js
        String js = perfReportImpl.getReportContent("templates/properties/PerfReport/js.html");
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
            sourceFile = JmeterPerfReportImpl.class.getClassLoader().getResourceAsStream("templates/properties/PerfReport/head.html");
            bufReader = new BufferedReader(new InputStreamReader(sourceFile, "utf-8"));

            for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {
                sbf.append(tmp);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (bufReader != null) {
                bufReader.close();
            }
            if (sourceFile != null) {
                sourceFile.close();
            }
        }

        return new StringBuilder(sbf.toString());
    }


}
