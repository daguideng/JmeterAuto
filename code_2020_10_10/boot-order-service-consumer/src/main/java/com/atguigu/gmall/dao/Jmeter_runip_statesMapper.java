package com.atguigu.gmall.dao;

import com.atguigu.gmall.entity.Jmeter_runip_states;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;


@Component("jmeter_runip_statesMapper")
@Mapper
public interface Jmeter_runip_statesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Jmeter_runip_states record);

    int insertSelective(Jmeter_runip_states record);

    Jmeter_runip_states selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Jmeter_runip_states record);

    int updateByPrimaryKey(Jmeter_runip_states record);


    List<Jmeter_runip_states> selectByStatusRunIp(List<String> listin);

    //updatetype
    int updatetype(@Param("id") Integer id, @Param("type") String type);

    //根据id,禁用定时器:
    int pause(@Param("id") Integer id);

    //根据id,启用定时器:
    int enable(@Param("id") Integer id);


    //清空 为了得到最新可用的agnet ip
   // int truncateTable();

    //insert 为了得到最新可用的agnet ip
 //   int insertRefresh(Jmeter_runip_states record);



}