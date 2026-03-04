package com.atguigu.gmall.controller.inter;

import com.atguigu.gmall.Interface.SendEmailReportIntel;
import com.atguigu.gmall.common.bean.response.Result;
import com.atguigu.gmall.common.constant.RES_STATUS;
import com.atguigu.gmall.common.utils.DateUtil;
import com.atguigu.gmall.service.jmeterperf.StatisticsServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
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
public class JmeterInterlinkReport {

    @Autowired
    @Qualifier("sendEmailInterReportImpl")
    private SendEmailReportIntel sendEmailIntereport;





    //@ApiOperation(value = "inter：超链接简洁接口测试报告")
    @RequestMapping(value = "/link/listX", method =  RequestMethod.GET)
    @ResponseBody
    public Result<?> interReportList(@RequestParam(required = true) String  id, @RequestParam(required = true) String lastruntime, HttpServletResponse response) throws Exception
    {
        return simpleReportList(id, lastruntime, response);
    }

    //@ApiOperation(value = "perf：超链接简洁性能报告")
    @RequestMapping(value = "/link/list/test", method =  RequestMethod.GET)
    @ResponseBody
    public Result<?> simpleReportList(@RequestParam(required = true) String  id, @RequestParam(required = true) String lastruntime, HttpServletResponse response) throws Exception
    {


        Result <Object> result = new Result <>();

     //   log.info("输入的查询条件filter:{}", id);
     //   log.info("输入的查询条件filter:{}", lastruntime);


        if ("".equals(id ) || null == id || "".equals(lastruntime ) || null == lastruntime) {
     //       log.info(RES_STATUS.BAD_PARAM_NULL.msg);

     //       System.out.println();
            Map<String,Object> map = new HashMap<>();
            map.put("msg","id或lastruntime为空");
            result.setStatus(RES_STATUS.BAD_PARAM_NULL);
            result.setData(map);
     //       log.info("$result{}",result);

            return result;
        }


        String timeStamp2 = null ;
        if(lastruntime.contains("-") && lastruntime.contains(":")) {
            timeStamp2 = DateUtil.date2TimeStamp(lastruntime.trim(), "yyyy-MM-dd HH:mm:ss");
        }else{
            timeStamp2 = lastruntime ;
        }


        timeStamp2 = DateUtil.timeStamp2Date(timeStamp2,null);

     //   System.out.println("timeStamp2--->"+timeStamp2);


        StringBuffer str = sendEmailIntereport.writeLinkReport(id.trim(),timeStamp2);

        response.getWriter().print(str);

        return null;
    }


    //@ApiOperation(value = "inter：历史报告")
    @RequestMapping(value = "/link/historylist/test", method =  RequestMethod.GET)
    public Result<?> simpleHistoryReportList(@RequestParam(required = true) String  id, @RequestParam(required = true) String scriptname, HttpServletResponse response) throws Exception
    {


        Result <Object> result = new Result <>();

    //       log.info("输入的查询条件filter:{}", id);
    //    log.info("输入的查询条件filter:{}", scriptname);


        if ("".equals(id ) || null == id || "".equals(scriptname ) || null == scriptname) {
   //         log.info(RES_STATUS.BAD_PARAM_NULL.msg);
    //        System.out.println();
            Map<String,Object> map = new HashMap<>();
            map.put("msg","id或lastruntime为空");
            result.setStatus(RES_STATUS.BAD_PARAM_NULL);
            result.setData(map);
    //        log.info("$result{}",result);
            return result;
        }




        StringBuffer str = sendEmailIntereport.writeLinkHistoryReport(scriptname);

        response.getWriter().print(str);

        return null;
    }


}
