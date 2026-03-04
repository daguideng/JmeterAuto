package com.atguigu.gmall.dao;

import com.atguigu.gmall.entity.Errors;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;


@Mapper
@Component
public interface ErrorsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Errors record);

    int insertSelective(Errors record);

    Errors selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Errors record);

    int updateByPrimaryKey(Errors record);
}