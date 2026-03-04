package com.atguigu.gmall.controller.agent;

import com.atguigu.gmall.common.bean.response.Result;
import com.atguigu.gmall.service.impl.AgentStatusImpl;
import com.atguigu.gmall.service.impl.JmeterAgentipStatesRefreshServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2019-9-9
 */
@Slf4j
@RestController
@RequestMapping("/agentstatus")
public class AgentStatus {

    /**
     * 查询所有数据:
     * @param jobname
     * @return
     * @throws Exception
     */

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JmeterAgentipStatesRefreshServiceImpl jmeterAgentipStatesRefreshServiceImpl ;


    @Autowired
    private AgentStatusImpl agentStatusImpl;


    @RequestMapping(value = "/query", method = RequestMethod.POST)
    Result<?> queryAgentStusts(@RequestParam(required = false) String type) throws Exception {
        Result <Object> result = new Result <>();
        logger.info("type={}", type);



        Map runResult = agentStatusImpl.queryAgentStatus(type);
        result.setData(runResult);

        return result;

    }


    /***
     * updata type
     * @param id
     * @param type
     * @return
     * @throws Exception
     */

    @RequestMapping(value = "/updatetype", method = RequestMethod.POST)
    Result<?> updatetype(@RequestParam(required = false) Integer id,@RequestParam(required = false) String type) throws Exception {
        Result <Object> result = new Result <>();
        logger.info("updatetype=x{}", type);


        Map runResult = agentStatusImpl.updatetype(id,type);
        result.setData(runResult);

        return result;

    }


    /***
     * 禁用
     * @param id
     * @return
     * @throws Exception
     */

    @RequestMapping(value = "/pause", method = RequestMethod.POST)
    @ResponseBody
    Result<?> pause(@RequestParam(required = false) Integer id) throws Exception {
        Result <Object> result = new Result <>();
        logger.info("id={}", id);


        Map runResult = agentStatusImpl.pause(id);
        result.setData(runResult);

        return result;

    }

    /***
     * 启用:
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/enable", method = RequestMethod.POST)
    @ResponseBody
    Result<?> enable(@RequestParam(required = false) Integer id) throws Exception {
        Result <Object> result = new Result <>();
        logger.info("id={}", id);


        Map runResult = agentStatusImpl.enable(id);
        result.setData(runResult);

        return result;

    }


    @RequestMapping(value = "/refresh", method = RequestMethod.POST)
    @ResponseBody
    Result<?> refresh() throws Exception {
        Result <Object> result = new Result <>();


       // Map runResult = jmeterAgentipStatesRefreshServiceImpl.getRefreshRunIpInfo();

        //重写刷新，不改变数据结果，只刷新：
        Map runResult = jmeterAgentipStatesRefreshServiceImpl.getRWriteRefreshRunIpInfo();

        result.setData(runResult);

         return result;

    }







}
