package com.atguigu.gmall.service.impl;

import com.atguigu.gmall.dao.Provider_jmeter_agentip_statesMapper;
import com.atguigu.gmall.entity.Provider_jmeter_agentip_states;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2019-6-13
 */
@Service
public class AgentIpRunStatus {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    //@Resource(name = "provider_jmeter_agentip_statesMapper")
    // private Jmeter_agentip_statesMapper provider_jmeter_agentip_statesMapper;

    @Autowired
    private Provider_jmeter_agentip_statesMapper provider_jmeter_agentip_statesMapper;



    @Value("${jmeter.run.type}")
    private String jmeter_run_type;


    public void insertAgentIpStatus(String ip, String status, String runusername) {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式


        /**
        Jmeter_runip_states runIpUpStatus = new Jmeter_runip_states();
        runIpUpStatus.setIpaddress(ip);
        runIpUpStatus.setStates(status);
        runIpUpStatus.setType(jmeter_run_type);
        runIpUpStatus.setTime(df.format(new Date()));
        runIpUpStatus.setRunusername(runusername);
        runIpUpStatus.setRunstate(0);

         **/


        Provider_jmeter_agentip_states agentIpUpStatus = new Provider_jmeter_agentip_states();
        agentIpUpStatus.setIpaddress(ip);
        agentIpUpStatus.setStates(status);
        agentIpUpStatus.setType(jmeter_run_type);
        agentIpUpStatus.setTime(df.format(new Date()));
        agentIpUpStatus.setRunusername(runusername);
        agentIpUpStatus.setRunstate(0);

        System.err.println("runusername---->" + runusername);
        logger.info("insertAgentIpStatus --->runusername {}", runusername);

      //  int statusinfo = provider_jmeter_runtip_statesMapper.insert(runIpUpStatus);
        int agent_statusinfo = provider_jmeter_agentip_statesMapper.insert(agentIpUpStatus);

        if (agent_statusinfo > 0) {

            logger.info("负载机状态信息statusinfo：{},相关信息入库成功!", agent_statusinfo);
        } else {
            logger.error("负载机状态信息statusinfo：{},相关信息入库失败!", agent_statusinfo);
        }


    }


    public void updateDataStatusAgentInfo(String ipaddress, String status, String runusername) {


        //Jmeter_runip_states runip_stateslist = provider_jmeter_runtip_statesMapper.selectByIp(ip);
        System.out.println("ipaddress----->"+ipaddress);
        System.out.println("status----->"+status);
        System.out.println("runusername----->"+runusername);

        System.out.println("Provider_jmeter_agentip_states---->"+provider_jmeter_agentip_statesMapper);


        Provider_jmeter_agentip_states agent_stateslist = provider_jmeter_agentip_statesMapper.selectByIp(ipaddress);


        logger.info("runip_stateslist的结果为：{}", agent_stateslist);
        //如果存在则update,否则先insert
        if (null == agent_stateslist) {
            this.insertAgentIpStatus(ipaddress, status, runusername);
        } else {
            this.updateAgentIpStatus(ipaddress, status, runusername);

        }


    }


    public void updateAgentIpStatus(String ip, String status, String runusername) {


        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式


        Provider_jmeter_agentip_states agentIpUpStatus = new Provider_jmeter_agentip_states();

        /**
        Jmeter_runip_states runIpUpStatus = new Jmeter_runip_states();
        runIpUpStatus.setIpaddress(ip);
        runIpUpStatus.setStates(status);
        runIpUpStatus.setTime(df.format(new Date()));
        runIpUpStatus.setRunusername(runusername);
        logger.error("runIpUpStatus---->" + runIpUpStatus);
        logger.info("runIpUpStatus参数结果是:{}", runIpUpStatus);

        int insert_0 = provider_jmeter_runtip_statesMapper.updateRunIPStatus(runIpUpStatus);
        if (insert_0 > 0) {

            logger.info("runip表更新成功!");
        } else {
            logger.error("runip表更新失败!");
        }

         **/


        agentIpUpStatus.setIpaddress(ip);
        agentIpUpStatus.setStates(status);
        agentIpUpStatus.setTime(df.format(new Date()));
        agentIpUpStatus.setRunusername(runusername);
        logger.error("agentIpUpStatus---->" + agentIpUpStatus);
        logger.info("updateAgentIpStatus --->runusername {}", runusername);
        System.err.println("runusername---->" + runusername);

        logger.info("agentIpUpStatus参数结果是:{}", agentIpUpStatus);
        int insert_1 = provider_jmeter_agentip_statesMapper.updateRunIPStatus(agentIpUpStatus);
        if (insert_1 > 0) {

            logger.info("负载机运行状态更新成功!");
        } else {
            logger.error("上负载机运行状态更新失败!");
        }

    }


    /**
     * 更新数据库中所有status为killed，方便晚上重启jmeter
     *
     * @param status
     *
     */
    public void updateAgentIpDownStatus( String status) {


        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式


        Provider_jmeter_agentip_states agentIpUpStatus = new Provider_jmeter_agentip_states();


        agentIpUpStatus.setStates(status);
        agentIpUpStatus.setTime(df.format(new Date()));
        logger.error("agentIpUpStatus---->" + agentIpUpStatus);


        logger.info("agentIpUpStatus参数结果是:{}", agentIpUpStatus);
        int insert_1 = provider_jmeter_agentip_statesMapper.updateRunIPDownStatus(agentIpUpStatus);
        if (insert_1 > 0) {

            logger.info("负载机运行状态更新成功!");
        } else {
            logger.error("上负载机运行状态更新失败!");
        }

    }


    /**
     * 查看数据库中的状态：
     */
    public Map<String,Object> queryStatusAgentInfo(String ipaddress) {
        try {
            System.out.println("ipaddress----->" + ipaddress);
            System.out.println("Provider_jmeter_agentip_states---->" + provider_jmeter_agentip_statesMapper);
            
            Provider_jmeter_agentip_states agent_stateslist = provider_jmeter_agentip_statesMapper.selectByIp(ipaddress);
            
            Map<String,Object> map = new HashMap<>();
            if (agent_stateslist != null && agent_stateslist.getStates() != null) {
                map.put("ipaddress", agent_stateslist.getStates());
            } else {
                logger.warn("未找到IP地址{}的状态信息", ipaddress);
                map.put("ipaddress", "noipaddress");
            }
            return map;
        } catch (Exception e) {
            logger.error("查询状态信息异常", e);
            throw new RuntimeException("查询状态信息失败", e);
        }
    }
}
