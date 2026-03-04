package com.atguigu.gmall.controller.inter;

import com.atguigu.gmall.common.bean.response.Result;
import com.atguigu.gmall.common.web.PerfUploadInfoQuery;
import com.atguigu.gmall.service.jmeterperf.UploadInfoServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * @author dengdagui
 * @ClassName: JmeterPerfUploadInfController
 * @Description: 获取接口测试上传脚本信息:
 * @date 2018年6月8日 下午1:37:51
 */
@Slf4j
@RestController
@RequestMapping("/jmeterinter")
//@Api(value = "接口自动化模块", tags = "PERF-性能自动化模块")
public class JmeterInterScriptInfoReportController {



    @Autowired
    UploadInfoServer uploadInfoServer;


    //@ApiOperation(value = "perf：列表展示脚本上传信息")
    @PostMapping(value = "/script/list")
    //http://localhost:8082/jmeterperf/script/report/list?search=jiao_verify&filter=[{"operator":"ge","property":"uploadtime","value":"2018-08-09 13:44:51"},{"operator":"le","property":"uploadtime","value":"2018-08-09 14:40:39"}]
    public Result <?> list(@RequestParam(required = false) String search,
                           @RequestParam(required = false, defaultValue = "[]") String filter,
                           @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer limit,
                           HttpServletRequest request, HttpServletResponse response)
            throws IOException {


        filter = URLDecoder.decode(filter);

        log.info("输入的查询条件search:{}", search);
        log.info("输入的查询条件filter:{}", filter);
        // 输入数组转为List<String> 支持一次传入多个serviceType;Query也支持list值对比，只要是值对比即可
        PerfUploadInfoQuery query = new PerfUploadInfoQuery();



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
        result.setData(this.uploadInfoServer.list(query));
        return result;
    }


}
