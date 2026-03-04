package com.atguigu.gmall.controller.inter;

import com.atguigu.gmall.common.bean.response.Result;
import com.atguigu.gmall.common.web.InterCurrentReortQuery;
import com.atguigu.gmall.service.jmeterinter.JmeterIntercurrentreportServer;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/jmeterinter")
//@Api(value = "性能自动化模块", tags = "PERF-性能自动化模块")
public class JmeterInterReportController {



    @Autowired
    JmeterIntercurrentreportServer jmeterIntercurrentreportServer;

    //http://localhost:8082/jmeterperf/report/list?search=jiao_verify&filter=[{"operator":"ge","property":"starttime","value":"2018-08-09 13:44:51"},{"operator":"le","property":"endtime","value":"2018-08-09 14:40:39"}]
    //@ApiOperation(value = "perf：列表展示性能测试报告")
    @RequestMapping(value = "/report/list", method = RequestMethod.POST)
    public Result <?> list(@RequestParam(required = false) String search,
                           @RequestParam(required = false, defaultValue = "[]") String filter,
                           @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer limit)
            throws IOException {




        log.info("输入的查询条件search:{}", search);
        log.info("输入的查询条件filter:{}", filter);
        // 输入数组转为List<String> 支持一次传入多个serviceType;Query也支持list值对比，只要是值对比即可
        InterCurrentReortQuery query = new InterCurrentReortQuery();


        if (null != search || !"".equals(search)) {
            query.setScriptname(search);
        }



        if (null != page && !"".equals(page)) {
            query.setPageNo(page);
        }

        if (null != limit && !"".equals(limit)) {
            query.setPageSize(limit);
        }

        query.setSearch(search);
        query.setFilter(filter);


        Result <Map <String, Object>> result = new Result <>();
        result.setData(this.jmeterIntercurrentreportServer.list(query));
        return result;
    }


}
