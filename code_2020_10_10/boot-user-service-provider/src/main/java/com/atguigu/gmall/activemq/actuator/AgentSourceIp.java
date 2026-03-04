package com.atguigu.gmall.activemq.actuator;

import com.atguigu.gmall.common.utils.HttpClientUtil;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2019-6-21
 */
@Component
public class AgentSourceIp {

    @Value("${server.port}")
    private  String server_port ;



    @Value("${host.ip}")
    private  String host_ip ;


    private String ACTUATOR_URL = "http://127.0.0.1:" ;


    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 1.获取操作系统名：
     * /actuator/env  【os.name】
     * @param map
     */

    public void  getOsNameIp(Map<String, String> map){
        String osNameUrl = ACTUATOR_URL +server_port+ "/actuator/env" ;
        //String ipaddress = HttpClientUtil.doGet(osNameUrl).split("spring.cloud.client.ip-address")[1].split("\"value\":")[1].split("}")[0].replace("\"","");
        String ipaddress = host_ip ;
        map.put("ipaddress",ipaddress);
    }

}
