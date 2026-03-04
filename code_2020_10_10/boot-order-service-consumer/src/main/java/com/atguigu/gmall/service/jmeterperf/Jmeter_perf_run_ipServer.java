package com.atguigu.gmall.service.jmeterperf;

import com.atguigu.gmall.dao.Jmeter_perf_run_ipMapper;
import com.atguigu.gmall.entity.Jmeter_perf_run_ip;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: dengdagui
 * @Description: 主要用于性能测试负载机设置，及暂时用来发性能测试报告
 * @Date: Created in 2019-5-29
 */
@Slf4j
@Service
public class Jmeter_perf_run_ipServer {



    @Autowired
    private Jmeter_perf_run_ipMapper jmeter_perf_run_ipMapper;


    public void insertJmeter_perf_run_ip(Jmeter_perf_run_ip record) throws Exception {

        int count = jmeter_perf_run_ipMapper.insert(record);

        if (count > 0) {
            log.info("数据入库数据:record{}",record);

        } else {
            log.info("性能报告数据入库失败！");

        }

    }



    public List<Jmeter_perf_run_ip> selectByAllJmeter_perf_run_ip() throws Exception {

        List<Jmeter_perf_run_ip> list= jmeter_perf_run_ipMapper.selectByAll();

        return list;

    }

}
