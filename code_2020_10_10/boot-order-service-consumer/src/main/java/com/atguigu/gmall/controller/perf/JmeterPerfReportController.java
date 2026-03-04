package com.atguigu.gmall.controller.perf;

import com.atguigu.gmall.common.bean.response.Result;
import com.atguigu.gmall.common.web.PerfCurrentReortQuery;
import com.atguigu.gmall.service.jmeterperf.StatisticsServer;
import com.atguigu.gmall.entity.Statistics;
import java.util.List;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dengdagui
 * @ClassName: JmeterPerfReportController
 * @Description: 获取性能测试执行的统计数据
 * @date 2018年6月8日 下午1:37:51
 */
@Slf4j
@RestController
@RequestMapping("/jmeterperf")
//@Api(value = "性能自动化模块", tags = "PERF-性能自动化模块")
public class JmeterPerfReportController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    // 已移除对statistics表的查询操作
    
    //http://localhost:8082/jmeterperf/report/list?search=jiao_verify&filter=[{"operator":"ge","property":"starttime","value":"2018-08-09 13:44:51"},{"operator":"le","property":"endtime","value":"2018-08-09 14:40:39"}]
    //@ApiOperation(value = "perf：列表展示性能测试报告")
    @RequestMapping(value = "/report/list", method = RequestMethod.POST)
    public Result <?> list(@RequestParam(required = false) String search,
                           @RequestParam(required = false, defaultValue = "[]") String filter,
                           @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer limit)
            throws IOException {
        Result <Map <String, Object>> result = new Result <>();
        result.setCode(500);
        result.setMsg("已禁止对statistics表的查询操作");
        return result;
    }
    
    // 添加link/list接口用于查询特定ID和perfstarttime的性能数据
    @RequestMapping(value = "/link/data", method = RequestMethod.GET)
    public Result<?> linkList(@RequestParam("id") String id, 
                             @RequestParam("perfstarttime") String perfstarttime) {
        Result<List<Statistics>> result = new Result<>();
        result.setCode(500);
        result.setMsg("已禁止对statistics表的查询操作");
        return result;
    }


}
