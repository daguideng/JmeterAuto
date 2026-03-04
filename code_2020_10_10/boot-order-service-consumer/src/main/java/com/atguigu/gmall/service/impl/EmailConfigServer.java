package com.atguigu.gmall.service.impl;

import com.atguigu.gmall.dao.Email_configMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class EmailConfigServer {


    @Autowired
    private Email_configMapper email_configMapper ;


    /**
     * 以id为查询条件，得到key,value结果
     * @param id
     * @return
     */
    public Map<String,Object> getByPrimaryKeyServer(Integer id){

        Map<String,Object> mapkey    =  email_configMapper.getByPrimaryKey(id);

        return  mapkey ;
    }

}