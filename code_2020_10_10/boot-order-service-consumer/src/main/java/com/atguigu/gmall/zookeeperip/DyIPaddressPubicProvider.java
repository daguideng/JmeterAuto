package com.atguigu.gmall.zookeeperip;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author: dengdagui
 * @Description: 性能测试主机传动态IP地址给Provider端
 * @Date: Created in 2019-6-17
 */
@Component
public class DyIPaddressPubicProvider {

    @Value("${server.port}")
    private  String server_port ;

    @Value("${server.address}")
    private  String server_address ;



    private String ACTUATOR_URL = "http://127.0.0.1:" ;



    /**
     * 1.获取操作系统名：
     * /actuator/env  【os.name】
     *
     */
    public  String getConsumerOsIpaddress(){

      //  String osNameUrl = ACTUATOR_URL +server_port+ "/actuator/env" ;
     //   String ipaddress = HttpClientUtil.doGet(osNameUrl).split("spring.cloud.client.ip-address")[1].split("\"value\":")[1].split("}")[0].replace("\"","");

        String ipaddress = server_address;

        return ipaddress ;


    }
}
