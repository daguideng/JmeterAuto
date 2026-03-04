package com.atguigu.gmall.controller.perf;

import com.atguigu.gmall.common.bean.response.Result;
import com.atguigu.gmall.common.constant.RES_STATUS;
import com.atguigu.gmall.entity.Jmeter_perfor_current_report;
import com.atguigu.gmall.service.jmeterperf.JmeterperformcurrentreportServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: dengdagui
 * @Description: 性能测试当前报告控制器
 * @Date: Created in 2024-01-20
 */
@Slf4j
@RestController
@RequestMapping("/jmeterperf/current")
public class JmeterPerfCurrentReportController {

    @Autowired
    private JmeterperformcurrentreportServer jmeterperformcurrentreportServer;

    /**
     * 测试插入当前报告数据
     */
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result<?> insertCurrentReport(@RequestBody Jmeter_perfor_current_report record) {
        Result<Object> result = new Result<>();
        try {
            log.info("开始插入当前报告数据: {}", record);
            jmeterperformcurrentreportServer.insertCurrentReport(record);
            result.setData("插入成功");
            result.setStatus(RES_STATUS.SUCCESS);
        } catch (Exception e) {
            log.error("插入当前报告数据失败: {}", e.getMessage());
            result.setData("插入失败: " + e.getMessage());
            result.setStatus(RES_STATUS.FAILED);
        }
        return result;
    }
}