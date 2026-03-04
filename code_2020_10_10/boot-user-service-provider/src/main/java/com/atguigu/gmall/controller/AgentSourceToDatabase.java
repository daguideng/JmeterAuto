package com.atguigu.gmall.controller;

import com.atguigu.gmall.activemq.actuator.ActuatorAgentSource;
import com.atguigu.gmall.entity.Jmeter_perf_agent_source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2019-6-3
 */
@RestController
public class AgentSourceToDatabase {


    @Autowired
    private ActuatorAgentSource actuatorAgentSource ;

    /**
     * Map为写数据库提供数据<>
     * agent常用用资源数据入库:
     */
    @RequestMapping(value="/agentsource",method= RequestMethod.GET)
    @ResponseBody
    public Jmeter_perf_agent_source getSourceMapControl(){

        return actuatorAgentSource.getSourceMap();

    }
}
