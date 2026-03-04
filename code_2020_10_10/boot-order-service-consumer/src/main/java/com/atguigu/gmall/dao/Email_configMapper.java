package com.atguigu.gmall.dao;

import com.atguigu.gmall.entity.Email_config;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import java.util.Map;

@Mapper
@Component
public interface Email_configMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Email_config record);

    int insertSelective(Email_config record);

    Email_config selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Email_config record);

    int updateByPrimaryKey(Email_config record);

    //map 对于每一个字段对应相应的内容的key,value,便于根据字段取值：
    public Map<String,Object> getByPrimaryKey(Integer id) ;

}