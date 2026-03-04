package com.atguigu.gmall.service.impl;

import com.atguigu.gmall.common.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/***
 *  数据同步，查询jmeter最后状态：
 */

@Component
@Slf4j
public class UpdateJmeterStatus {

    @Value("${server.address}")
    private String server_address ;

    @Value("${server.port}")
    private String server_port ;


    public void upStatus(){

        //http://10.193.199.122:8089/agentstatus/refresh    post

        String URL = "http://"+server_address+":"+server_port+"/agentstatus/refresh";

        try{
            HttpClientUtil.doPost(URL,"");
            log.info("表的状态复制成功,ip:为{}，端口:为{}:",server_address,server_port);
        }catch (Exception e){
            e.printStackTrace();
        }



    }

}
