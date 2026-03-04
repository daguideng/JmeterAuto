package com.atguigu.gmall.controller.perf;

import com.atguigu.gmall.common.bean.response.Result;
import com.atguigu.gmall.entity.Jmeter_perf_run_ip;
import com.atguigu.gmall.service.jmeterperf.Jmeter_perf_run_ipServer;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2019-5-29
 */
@Slf4j
@RequestMapping("/jmeterperf")
//@Api(value = "性能报告模块", tags = "Mars-设置负载压力机模块", description = "设置负载压力机模块")
@RestController
public class JmeterPerfJmeterPerfRunIpController {




    @Autowired
    private Jmeter_perf_run_ipServer jmeter_perf_run_ipServer ;


    @RequestMapping(method = RequestMethod.POST, value = "/runip")
    public Result<?> list(@RequestParam(value="runIp",required = true) String runIp,
                          @RequestParam(value="to_email",required = false,defaultValue="" ) String to_email,
                          @RequestParam(value="cc_email",required = false,defaultValue="") String cc_email,
                          @RequestParam(value="bcc_email",required = false,defaultValue="") String bcc_email
                          )
            throws Exception {

        Jmeter_perf_run_ip  runip = new Jmeter_perf_run_ip(runIp,to_email,cc_email,bcc_email);

        Result <Map<String, Object>> result = new Result <>();
        jmeter_perf_run_ipServer.insertJmeter_perf_run_ip(runip);

        Map<String,Object> map = new HashMap <>();

        map.put("result",jmeter_perf_run_ipServer.selectByAllJmeter_perf_run_ip());


        jmeter_perf_run_ipServer.selectByAllJmeter_perf_run_ip();
        result.setData(map);
        return result;
    }



}
