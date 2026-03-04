package com.atguigu.gmall.controller.perf;

import com.atguigu.gmall.common.bean.response.Result;
import com.atguigu.gmall.common.web.PerfHistoryReortQuery;
import com.atguigu.gmall.service.jmeterperf.JmeterperformhistoryreportServer;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dengdagui
 * @ClassName: JmeterPerfReportController
 * @Description: 获取性能测试历史执行的统计数据
 * @date 2018年6月8日 下午1:37:51
 */
@Slf4j
@RestController
@RequestMapping("/jmeterperf")
//@Api(value = "性能自动化模块", tags = "PERF-性能自动化模块")
public class JmeterPerfHistoryReportController {




    @Autowired
    private JmeterperformhistoryreportServer jmeterperformhistoryreportServer;

    //@ApiOperation(value = "perf：列表展示性能测试报告")
    @RequestMapping(value = "/historyreport/list", method = RequestMethod.POST)
    public Result<?> list(@RequestParam(required = false) String search,
                          @RequestParam(required = false, defaultValue = "[]") String filter,
                          @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer limit)
            throws IOException {
        log.info("输入的查询条件search:{}", search);
        log.info("输入的查询条件filter:{}", filter);

        // 输入数组转为List<String> 支持一次传入多个serviceType;Query也支持list值对比，只要是值对比即可
        PerfHistoryReortQuery query = new PerfHistoryReortQuery();


        if (null != search || !"".equals(search)) {
            query.setScriptname(search);
        }


        if (page != null) {
            query.setPageNo(page);
        }
        if (limit != null) {
            query.setPageSize(limit);
        }

        query.setSearch(search);
        query.setFilter(filter);

        Result <Map <String, Object>> result = new Result <>();
        result.setData(this.jmeterperformhistoryreportServer.list(query));
        return result;
    }


}
