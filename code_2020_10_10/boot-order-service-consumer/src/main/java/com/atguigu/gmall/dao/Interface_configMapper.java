package com.atguigu.gmall.dao;

import com.atguigu.gmall.entity.Interface_config;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Map;


@Mapper
@Component
public interface Interface_configMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Interface_config record);

    int insertSelective(Interface_config record);

    Interface_config selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Interface_config record);

    int updateByPrimaryKey(Interface_config record);



    //map 对于每一个字段对应相应的内容的key,value,便于根据字段取值：
    public Map<String,Object> getByPrimaryKey(Integer id) ;


    //map 对于每一个字段对应相应的内容的key,value,便于根据字段取值：
    public  Map <String, Object> getByVauleKey(@Param("threadname") String threadname) ;


    //map 对性能测试运行不同状态随时更新“：
    public  int updateInterRunState(@Param("status") String status,@Param("threadname") String threadname) ;


    //取数据库中最后一条数据：
    Interface_config selectByLastThreadname();
}