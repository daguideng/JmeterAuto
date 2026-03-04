package com.atguigu.gmall.service.impl;


import com.atguigu.gmall.dao.Jmeter_agentip_statesMapper;
import com.atguigu.gmall.dao.Jmeter_runip_statesMapper;
import com.atguigu.gmall.entity.Jmeter_agentip_states;
import com.atguigu.gmall.entity.Jmeter_runip_states;
import com.atguigu.gmall.zookeeperip.DynamicHTTPHostProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author: dengdagui
 * @Description: 刷新Jmeter agent的ip的最新状态：
 * @Date: Created in 2019-11-8
 */
@Service
@Slf4j
public class JmeterAgentipStatesRefreshServiceImpl {


    @Resource(name = "consumer_jmeter_agentip_statesMapper")
    private Jmeter_agentip_statesMapper consumer_jmeter_agentip_statesMapper;


    @Resource(name= "jmeter_runip_statesMapper")
    private Jmeter_runip_statesMapper jmeter_runip_statesMapper ;


    @Autowired
    private DynamicHTTPHostProvider dynamicHTTPHostProvider;


    /***
     * 重写刷新,只刷新结果，不改变数据
     * @return
     */

    public Map<String,Object> getRWriteRefreshRunIpInfo() {

        Map<String, Object> map = new HashedMap();


       return (Map<String, Object>) map.put("result",consumer_jmeter_agentip_statesMapper.queryAgentStatusByNull());


    }







        public Map<String,Object> getRefreshRunIpInfo() {


        Map<String,Object> map = new HashedMap();


        Set<String> hashset = new HashSet<>();

        dynamicHTTPHostProvider.providerSet(hashset);

        //为了安全线程：
        //  Set <String> hashsetIp = Collections.synchronizedSet(hashset);

        List<String> listin = new ArrayList<>();
        listin.add("Created");
        listin.add("Finished");
        listin.add("Run");
        listin.add("Mmaped");


        List <Jmeter_runip_states> agentipList = jmeter_runip_statesMapper.selectByStatusRunIp(listin);



        Map<String,Jmeter_runip_states> runInterIpMap = new HashedMap();
        Map<String,Jmeter_runip_states> runPerfIpMap = new HashedMap();

        //从HashSet中删除数据库中states中是：Run 状态的ip
        if (null != agentipList) {

            for (Jmeter_runip_states ipPort : agentipList) {
                //清除类型不是:perf的代理机器
                if ("Inter".equalsIgnoreCase(ipPort.getType())) {
                    for (String zikIp : hashset) {
                        if (zikIp.equals(ipPort.getIpaddress())) {
                            System.out.println("ipPortx---->"+ipPort);
                            runInterIpMap.put(ipPort.getIpaddress(),ipPort);
                        }
                    }
                }

                //类型只选:perf的代理机器
                if ("Perf".equalsIgnoreCase(ipPort.getType())) {
                    for (String zikIp : hashset) {
                        if (zikIp.equals(ipPort.getIpaddress())) {
                            System.out.println("ipPorty---->" + ipPort);
                            runPerfIpMap.put(ipPort.getIpaddress(),ipPort);
                        }
                    }
                }


            }
        }



        //1.得到可运行的agent ip：  (runInterIpMap ,runPerfIpMap)


        //2.清空
        int count = consumer_jmeter_agentip_statesMapper.truncateTable();
        if(count == 0){
            log.info("清空表：jmeter_agentip_states 成功!");
        }else{
            log.info("清空表：jmeter_agentip_states 失败!");
        }



        //3.把数据列表更新到表中:
        Map<String,Jmeter_agentip_states> intermap = new HashedMap();
        Map<String,Jmeter_agentip_states> perfmap = new HashedMap();




         intermap = getResult(runInterIpMap,intermap);
         perfmap = getResult(runPerfIpMap,perfmap);




        int  insert_InterIp_number = 0 ;
        for(Map.Entry <String, Jmeter_agentip_states> mapEntry : intermap.entrySet()) {
            System.out.println(mapEntry.getKey() + "---" + mapEntry.getValue());
            insert_InterIp_number += consumer_jmeter_agentip_statesMapper.insertRefresh((Jmeter_agentip_states)mapEntry.getValue());
        }
        log.info("新增可运行的Inter的agent数为:"+insert_InterIp_number);



        int  insert_PerfIp_number = 0 ;
        for(Map.Entry <String, Jmeter_agentip_states> mapEntry : perfmap.entrySet()) {
            System.out.println(mapEntry.getKey() + "---" + mapEntry.getValue());
            insert_PerfIp_number += consumer_jmeter_agentip_statesMapper.insertRefresh((Jmeter_agentip_states)mapEntry.getValue());
        }

        log.info("新增可运行的Perf的agent数为:"+insert_PerfIp_number);

        int insert_number = insert_InterIp_number + insert_PerfIp_number ;




        //强制清空
        runInterIpMap.clear();
        runPerfIpMap.clear();
        hashset.clear();
        agentipList.clear();
        intermap.clear();
        perfmap.clear();



        map.put("count",insert_number);





        return map ;

    }



    public Map<String,Jmeter_agentip_states> getResult(Map <String, Jmeter_runip_states> runipMap, Map<String,Jmeter_agentip_states> runagentIpMap){



        for(Map.Entry <String, Jmeter_runip_states> mapEntry : runipMap.entrySet()) {
            System.out.println(mapEntry.getKey() + "---" + mapEntry.getValue());
            runagentIpMap.put(mapEntry.getKey(), (Jmeter_agentip_states)mapEntry.getValue());
        }

        return runagentIpMap ;


    }






}
