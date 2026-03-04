package com.atguigu.gmall.service.jmeterperf;

import com.atguigu.gmall.dao.Perf_configMapper;
import com.atguigu.gmall.entity.Perf_config;
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
public class PerfConfigServer {



    @Autowired
    private Perf_configMapper perf_configMapper ;

    public void insertPerfconfig(Perf_config record){

        int count =  perf_configMapper.insert(record);

        if(count > 0) {
            log.info("性能配置参数入库成功！");

        }else{
            log.info("性能配置参数入库失败！");
        }

    }



    /**
     * 以id为查询条件，得到key,value结果
     * @param id
     * @return
     */
    public Map<String,Object> getByPrimaryKeyServer(Integer id){

        Map<String,Object> mapkey    =  perf_configMapper.getByPrimaryKey(id);

        return  mapkey ;
    }




    /**
     * 以id为查询条件，得到key,value结果
     * @param threadname
     * @return
     */
    public Map<String,Object> getByValueServer(String threadname){

        Map<String,Object> mapkey    =  perf_configMapper.getByVauleKey(threadname);

        return  mapkey ;
    }


    /**
     * 性能测试不同状态随时更新数据库的配置状态
     */
    public void updateRunStates(String status,String threadname){

        int result  =  perf_configMapper.updatePerfRunState(status,threadname);
        if(result >0){
            log.info("更新配置状态 "+status+" 成功!");
        }else{
            log.info("更新配置状态 "+status+" 失败!");
        }

    }

    /***
     * 查询最后一条数据,目的是为了当session失效时，取最后一条数据:
     */
    public Perf_config getLastLastThreadname(){

        Perf_config result  =  perf_configMapper.selectByLastThreadname();

        return result ;

    }

}
