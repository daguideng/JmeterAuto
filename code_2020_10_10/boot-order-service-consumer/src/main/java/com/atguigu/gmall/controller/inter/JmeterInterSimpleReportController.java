package com.atguigu.gmall.controller.inter;


import com.atguigu.gmall.common.bean.response.Result;
import com.atguigu.gmall.common.web.InterCurrentReortQuery;
import com.atguigu.gmall.service.jmeterinter.JmeterIntercurrentreportServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

/**
 * @Author: dengdagui
 * @Description:简洁报告：
 * @Date: Created in 2018-9-14
 */
@Slf4j
@RestController
@RequestMapping("/jmeterinter")
//@Api(value = "接口报告模块", tags = "Mars-简洁接口报告模块", description = "简洁接口报告模块")
@Controller
public class JmeterInterSimpleReportController {


    @Autowired
    JmeterIntercurrentreportServer jmeterIntercurrentreportServer;

    //@ApiOperation(value = "inter：简洁接口报告")
    @RequestMapping(value = "/simplereport/list", method = RequestMethod.POST)
    public Result<?> simpleReportList(@RequestParam(required = false, defaultValue = "[]") String filter,
                                      @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer limit)
            throws IOException {


    //    log.info("输入的查询条件filter:{}", filter);
        // 输入数组转为List<String> 支持一次传入多个serviceType;Query也支持list值对比，只要是值对比即可
        InterCurrentReortQuery query = new InterCurrentReortQuery();



        if (page != null) {
            query.setPageNo(page);
        }
        if (limit != null) {
            query.setPageSize(limit);
        }


        query.setFilter(filter);

        Result <Map<String, Object>> result = new Result <>();
        result.setData(this.jmeterIntercurrentreportServer.simpleReport(query));

        return result;
    }




}
