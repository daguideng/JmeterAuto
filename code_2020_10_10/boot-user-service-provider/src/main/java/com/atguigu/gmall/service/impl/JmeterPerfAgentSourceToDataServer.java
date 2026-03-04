package com.atguigu.gmall.service.impl;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2019-6-3
 */

import com.atguigu.gmall.activemq.actuator.ActuatorAgentSource;
import com.atguigu.gmall.activemq.actuator.AgentSourceIp;
import com.atguigu.gmall.common.utils.OSUtils;
import com.atguigu.gmall.dao.Jmeter_perf_agent_sourceMapper;
import com.atguigu.gmall.entity.Jmeter_perf_agent_source;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: dengdagui
 * @Description:  上传脚本服务层：
 * @Date: Created in 2018-7-5
 */
@Service
public class JmeterPerfAgentSourceToDataServer {

    @Autowired
    private AgentSourceIp agentSourceIp ;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name="provider_jmeter_perf_agent")
    private Jmeter_perf_agent_sourceMapper provider_jmeter_perf_agent;

    @Autowired
    private ActuatorAgentSource actuatorAgentSource ;


    public void insertAgentSource() {


        int statusinfo = provider_jmeter_perf_agent.insert(actuatorAgentSource.getSourceMap());

        if (statusinfo > 0) {

            logger.info("负载机资源：{},相关信息入库成功!", statusinfo);
        } else {
            logger.error("负载机资源：{},相关信息入库失败!", statusinfo);
        }


    }


    @Transactional
    public void updateAgentSourceInfo() {

        String hostIP = null ;
        if (OSUtils.isWindows()) {
            hostIP = OSUtils.getLocalIP();
        } else {
            hostIP = OSUtils.getLocalIP();
        }

        if("".equals(hostIP) || null == hostIP){
            //修改主机ip
            Map<String, String> map = new HashMap<>();
            agentSourceIp.getOsNameIp(map);
            hostIP = map.get("ipaddress");
            System.out.println("hostIP-->" + hostIP);
            logger.info("hostIP-->" + hostIP);
            map.clear();
        }









        Jmeter_perf_agent_source agentSourcelist = provider_jmeter_perf_agent.selectByIp(hostIP);


        logger.info("agentip_sourcelist的结果为：{}", agentSourcelist);
        //如果存在则update,否则先insert
        if (null == agentSourcelist) {
            this.insertAgentSource();
        } else {
            this.updateAgentSource();

        }


    }



    public void updateAgentSource() {

        int insert = provider_jmeter_perf_agent.updateAgentSource(actuatorAgentSource.getSourceMap());


        if (insert > 0) {

            logger.info("负载机资源更新成功!");
        } else {
            logger.error("负载机资源更新失败!");
        }

    }


}
