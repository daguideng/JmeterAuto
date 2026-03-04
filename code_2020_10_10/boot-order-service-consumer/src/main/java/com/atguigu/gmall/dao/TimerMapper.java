package com.atguigu.gmall.dao;

import com.atguigu.gmall.entity.Timer;

public interface TimerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Timer record);

    int insertSelective(Timer record);

    Timer selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Timer record);

    int updateByPrimaryKey(Timer record);
}