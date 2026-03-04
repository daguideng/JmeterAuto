package com.atguigu.gmall.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.atguigu.gmall.dao.Jmeter_agentip_statesMapper;
import com.atguigu.gmall.dao.Jmeter_runip_statesMapper;
import com.atguigu.gmall.entity.Jmeter_agentip_states;
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
 * @Date: Created in 2019-9-9
 */
@Slf4j
@Service
public class AgentStatusImpl {




    @Autowired
    private Jmeter_agentip_statesMapper jmeter_agentip_statesMapper;


    @Autowired
    private Jmeter_runip_statesMapper jmeter_runip_statesMapper ;

    /**
     * 定时器显示数据：
     */

    public Map <String, Object> queryAgentStatus(String type) throws UnsupportedEncodingException {

        Map <String, Object> map = new HashedMap();

        List <Jmeter_agentip_states> result = null;

        if (null == type || "".equals(type)) {
            result = jmeter_agentip_statesMapper.queryAgentStatusByNull();
        } else {
            result = jmeter_agentip_statesMapper.queryAgentStatus(type);

        }


        if (result.size() > 0) {
            log.info("查询timertypeconfig  " + type + " 成功!");
        } else {
            log.info("未查询到数据");
        }

        JSONArray array = JSONArray.parseArray(JSON.toJSONString(result));

        //    String json = MyJsonUtil.object_to_json(result);
        //     System.out.println("json---->"+json);
        //     System.out.println("array---->"+array);
        map.put("json", array);
        map.put("result", result);


        return map;

    }


    /***
     * update Type
     * @return
     * @throws UnsupportedEncodingException
     */
    public Map <String, Object> updatetype(Integer id, String type) throws UnsupportedEncodingException {

        Map <String, Object> map = new HashedMap();


        System.out.println("id---->"+id);
        System.out.println("type---->"+type);

        int count_agentip = jmeter_agentip_statesMapper.updatetype(id, type);
       // int count_runip = jmeter_runip_statesMapper.updatetype(id, type);


        if (count_agentip> 0  ) {
            log.info("查询timertypeconfig  " + type + " 成功!");
        } else {
            log.info("未查询到数据");
        }

        map.put("count_agentip", count_agentip);


        return map;

    }


    /**
     * 禁用
     * @param id
     * @return
     */
    public Map<String,Object> pause(Integer id){

        int count_agentip =  jmeter_agentip_statesMapper.pause(id);
      //  int count_runip =  jmeter_runip_statesMapper.pause(id);

        Map<String,Object> map = new HashedMap();

        if(count_agentip > 0 ) {
            log.info("禁用成功！");

        }else{
            log.info("禁用失败！");
        }

        map.put("count",count_agentip);

        return map;

    }


    /**
     * 启用
     * @param id
     * @return
     */
    public Map<String,Object> enable(Integer id){

        int count_agentip =  jmeter_agentip_statesMapper.enable(id);
    //    int count_runip =  jmeter_runip_statesMapper.enable(id);

        Map<String,Object> map = new HashedMap();

        if(count_agentip > 0  ) {
            log.info("启用成功！");

        }else{
            log.info("启用失败！");
        }

        map.put("count",count_agentip);

        return map;

    }


}
