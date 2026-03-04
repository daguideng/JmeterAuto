package com.atguigu.gmall.dao;

import com.atguigu.gmall.entity.Interface_config;
import com.atguigu.gmall.entity.Perf_config;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Map;


@Mapper
@Component
public interface Perf_configMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Perf_config record);

    int insertSelective(Perf_config record);

    Perf_config selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Perf_config record);

    int updateByPrimaryKey(Perf_config record);


    //map 对于每一个字段对应相应的内容的key,value,便于根据字段取值：
    public Map<String,Object> getByPrimaryKey(Integer id) ;


    //map 对于每一个字段对应相应的内容的key,value,便于根据字段取值：
    public  Map <String, Object> getByVauleKey(@Param("threadname") String threadname) ;

    //map 对性能测试运行不同状态随时更新“：
    public  int updatePerfRunState(@Param("status") String status,@Param("threadname") String threadname) ;


    //取数据库中最后一条数据：
    Perf_config selectByLastThreadname();



}