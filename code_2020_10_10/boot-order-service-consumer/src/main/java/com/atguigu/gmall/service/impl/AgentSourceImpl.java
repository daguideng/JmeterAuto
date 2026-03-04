package com.atguigu.gmall.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.atguigu.gmall.dao.Jmeter_perf_agent_sourceMapper;
import com.atguigu.gmall.entity.Jmeter_perf_agent_source;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2019-9-10
 */
@Slf4j
@Service
public class AgentSourceImpl {




    @Autowired
    private Jmeter_perf_agent_sourceMapper jmeter_perf_agent_sourceMapper;

    /**
     * 定时器显示数据：
     */

    public Map <String, Object> queryAgentSource(String ipaddress) throws UnsupportedEncodingException {

        Map <String, Object> map = new HashedMap();

        List <Jmeter_perf_agent_source> result = null;

        if (null == ipaddress || "".equals(ipaddress)) {
            result = jmeter_perf_agent_sourceMapper.queryAgentRourceByNull();
        } else {
            result = jmeter_perf_agent_sourceMapper.queryAgentRource(ipaddress);

        }


        if (result.size() > 0) {
            log.info("查询timertypeconfig  " + ipaddress + " 成功!");
        } else {
            log.info("未查询到数据");
        }

        JSONArray array = JSONArray.parseArray(JSON.toJSONString(result));

        map.put("json", array);
        map.put("result", result);


        return map;

    }
}
