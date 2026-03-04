package com.atguigu.gmall.dao;

import com.atguigu.gmall.entity.Interface_run_ip;

public interface Interface_run_ipMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Interface_run_ip record);

    int insertSelective(Interface_run_ip record);

    Interface_run_ip selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Interface_run_ip record);

    int updateByPrimaryKey(Interface_run_ip record);




}