package com.atguigu.gmall.service.jmeterinter;

import com.atguigu.gmall.dao.Interface_configMapper;
import com.atguigu.gmall.entity.Interface_config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * @Author: dengdagui
 * @Description: 性能配置文件入库
 * @Date: Created in 2018-7-23
 */
@Slf4j
@Service
public class InterConfigServer {




    @Autowired
    private Interface_configMapper interface_configMapper ;

    public void insertInterconfig(Interface_config record){

        int count =  interface_configMapper.insert(record);

        if(count > 0) {
            log.info("接口配置参数入库成功！");

        }else{
            log.info("接口配置参数入库失败！");
        }

    }



    /**
     * 以id为查询条件，得到key,value结果
     * @param id
     * @return
     */
    public Map<String,Object> getByPrimaryKeyServer(Integer id){

        Map<String,Object> mapkey    =  interface_configMapper.getByPrimaryKey(id);

        return  mapkey ;
    }




    /**
     * 以id为查询条件，得到key,value结果
     * @param threadname
     * @return
     */
    public Map<String,Object> getByValueServer(String threadname){

        Map<String,Object> mapkey    =  interface_configMapper.getByVauleKey(threadname);

        return  mapkey ;
    }


    /**
     * 性能测试不同状态随时更新数据库的配置状态
     */
    public void updateRunStates(String status,String threadname){

        int result  =  interface_configMapper.updateInterRunState(status,threadname);
        if(result >0){
            log.info("更新配置状态 "+status+" 成功!");
        }else{
            log.info("更新配置状态 "+status+" 失败!");
        }

    }


    /***
     * 查询最后一条数据,目的是为了当session失效时，取最后一条数据:
     */
    public Interface_config getLastLastThreadname(){

        Interface_config result  =  interface_configMapper.selectByLastThreadname();

        return result ;

    }

}
