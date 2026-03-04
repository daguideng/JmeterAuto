package com.atguigu.gmall.dao;

import com.atguigu.gmall.entity.Apdex;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;


@Mapper
@Component
public interface ApdexMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Apdex record);

    int insertSelective(Apdex record);

    Apdex selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Apdex record);

    int updateByPrimaryKey(Apdex record);
}