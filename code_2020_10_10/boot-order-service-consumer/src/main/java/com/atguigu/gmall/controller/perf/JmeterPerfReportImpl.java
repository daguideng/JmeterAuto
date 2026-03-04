package com.atguigu.gmall.controller.perf;

import com.atguigu.gmall.Impl.perfImpl.PerfReportImpl;
import com.atguigu.gmall.Interface.SendEmailReportIntel;
import com.atguigu.gmall.common.bean.response.Result;
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
@RequestMapping("/jmeterperf")
//@Api(value = "性能报告模块", tags = "Mars-超链接简洁性能报告模块", description = "超链接简洁性能报告")
public class JmeterPerfReportImpl {

    @Autowired
    @Qualifier("sendEmailInterReportImpl")
    private SendEmailReportIntel sendEmailIntereport;





    @Autowired
    private PerfReportImpl perfReportImpl ;

    @Autowired
    StatisticsServer reportService;

    @Autowired
    private Upload_infoMapper upload_infoMapper;

    @Autowired
    private JmeterperformcurrentreportServer jmeterperformcurrentreportServer;

    //@ApiOperation(value = "perf：超链接简洁接口报告")
    @RequestMapping(value = "/inter/link/list", method = RequestMethod.GET)
    @ResponseBody
    public StringBuffer simpleReportList(@RequestParam(required = true) String id, @RequestParam(required = true) String perfstarttime, @RequestParam(required = true) String scriptname, HttpServletResponse response) throws Exception {


        Result<Object> result = new Result<>();

        log.info("输入的查询条件filter:{}", id);
        log.info("输入的查询条件filter:{}", perfstarttime);
        log.info("输入的查询条件filter:{}", scriptname);

        String timeStamp2 = null;
        if (perfstarttime.contains("-") && perfstarttime.contains(":")) {
            timeStamp2 = DateUtil.date2TimeStamp(perfstarttime.trim(), "yyyy-MM-dd HH:mm:ss");
        } else {
            timeStamp2 = perfstarttime;
        }


        StringBuffer sbf = new StringBuffer(800);
        InputStream sourceFile = null;
        BufferedReader bufReader = null;

        //1. heads
        String heads = perfReportImpl.getReportContent("templates/properties/PerfReport/head.html");
        heads = String.format(heads, "性能测试报告");
        sbf.append(heads);

        //2.summary
        perfstarttime = DateUtil.timeStamp2Date(perfstarttime, "yyyy-MM-dd HH:mm:ss");
        if(null != id) {
            String summary = perfReportImpl.getReportContent("templates/properties/PerfReport/summary.html");

            System.out.println("perfstarttime--->" + perfstarttime);
            Map<String, String> map = perfReportImpl.getSummaryInfo(id, perfstarttime, scriptname);
            System.out.println("map--->" + map);
            System.out.println("summary-->" + summary);
            //  summary = String.format(summary, "0","0","0","0","0","0","0");
            summary = MessageFormat.format(summary, map.get("starttime"), map.get("endtime"), map.get("elapsedtime"), map.get("succesCount"), map.get("failCount"), "0", "0");
            System.out.println("summary-->" + summary);

            sbf.append(summary);
        }

        //3.main
        String main = perfReportImpl.getReportContent("templates/properties/PerfReport/main.html");
        List<String> detailList = perfReportImpl.getDetailReport(id, perfstarttime);


        StringBuilder lineReport = new StringBuilder();

        for(int i = 0 ; i<detailList.size();i++ ){
           // id;
            lineReport.append(detailList.get(i));

        }

        main = MessageFormat.format(main,   "default",lineReport.toString());
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


    //@ApiOperation(value = "inter：历史报告")
    @RequestMapping(value = "/inter/link/historylist", method = RequestMethod.GET)
    public StringBuilder simpleHistoryReportList(@RequestParam(required = true) String id, HttpServletResponse response) throws Exception {


        Result<Object> result = new Result<>();

        log.info("输入的查询条件filter:{}", id);
        
        String scriptname = "";
        
        // 根据uploadid从upload_info表查询scriptname
        try {
            Integer uploadId = Integer.parseInt(id);
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


        StringBuilder sbf = new StringBuilder(800);
        InputStream sourceFile = null;
        BufferedReader bufReader = null;

        //1. heads
        String heads = perfReportImpl.getReportContent("templates/properties/PerfReport/head.html");
        heads = String.format(heads, "接口测试历史报告");
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
        Integer uploadId = Integer.parseInt(id);
        List<String> detailList = perfReportImpl.getHistoryDetailReport(scriptname, uploadId);


        StringBuilder reportList = new StringBuilder();
        StringBuilder lineReport = new StringBuilder();

        for(int i = 0 ; i<detailList.size();i++ ){
            // id;
            lineReport.append(detailList.get(i));

        }

        main = MessageFormat.format(main, "default",lineReport.toString());
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
