package com.atguigu.gmall.controller.perf;

import com.atguigu.gmall.common.bean.response.Result;
import com.atguigu.gmall.common.web.PerfCurrentReortQuery;
import com.atguigu.gmall.service.jmeterperf.StatisticsServer;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: dengdagui
 * @Description:简洁报告：
 * @Date: Created in 2018-9-14
 */


@Slf4j
@RequestMapping("/jmeterperf")
//@Api(value = "性能报告模块", tags = "Mars-简洁性能报告模块", description = "简洁性能报告模块")
@Controller
public class JmeterPerfSimpleReportController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    StatisticsServer reportService;

    //    @ApiOperation(value = "perf：简洁性能报告")

    /**
    @RequestMapping(value = "/simplereport/list", method = RequestMethod.GET)
    @ResponseBody
    public Result<?> simpleReportListOld(@RequestParam(required = false, defaultValue = "[]") String filter,
                                      @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer limit)
            throws IOException {


        logger.info("输入的查询条件filter:{}", filter);
        // 输入数组转为List<String> 支持一次
        // 传入多个serviceType;Query也支持list值对比，只要是值对比即可
        //PerfStatisticsQuery query = new PerfStatisticsQuery();

        if (page != null) {
            query.setPageNo(page);
        }
        if (limit != null) {
            query.setPageSize(limit);
        }


        query.setFilter(filter);

        Result <Map<String, Object>> result = new Result <>();
        result.setData(reportService.simpleReport(query));
        return result;
    }
    ***/


    @RequestMapping(value = "/simplereport/list", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> simpleReportList(@RequestParam(required = false, defaultValue = "[]") String filter,
                                      @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer limit) throws IOException {


        logger.info("输入的查询条件filter:{}", filter);

        // 传入多个serviceType;Query也支持list值对比，只要是值对比即可
        PerfCurrentReortQuery query = new PerfCurrentReortQuery();

        if (page != null) {
            query.setPageNo(page);
        }
        if (limit != null) {
            query.setPageSize(limit);
        }




        query.setFilter(filter);

        Result <Map<String, Object>> result = new Result <>();
        result.setData(reportService.simpleReport(query));
        return result;
    }





}
