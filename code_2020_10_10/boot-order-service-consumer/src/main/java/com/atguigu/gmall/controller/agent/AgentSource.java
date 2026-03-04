package com.atguigu.gmall.controller.agent;

import com.atguigu.gmall.common.bean.response.Result;
import com.atguigu.gmall.service.impl.AgentSourceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2019-9-10
 */
@RestController
@RequestMapping("/agentsource")
public class AgentSource {


    /**
     * 查询所有数据:
     * @param ipaddress
     * @return
     * @throws Exception
     */

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private AgentSourceImpl agentSourceImpl;


    @RequestMapping(value = "/query", method = RequestMethod.POST)
    Result<?> queryAgentSource(@RequestParam(required = false) String type) throws Exception {
        Result <Object> result = new Result <>();
        logger.info("ipaddress={}", type);



        Map runResult = agentSourceImpl.queryAgentSource(type);
        result.setData(runResult);

        return result;

    }
}
