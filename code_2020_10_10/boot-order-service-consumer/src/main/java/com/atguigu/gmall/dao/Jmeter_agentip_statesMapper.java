package com.atguigu.gmall.dao;

import com.atguigu.gmall.entity.Jmeter_agentip_states;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component("consumer_jmeter_agentip_statesMapper")
@Mapper
public interface Jmeter_agentip_statesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Jmeter_agentip_states record);

    int insertSelective(Jmeter_agentip_states record);

    Jmeter_agentip_states selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Jmeter_agentip_states record);

    int updateByPrimaryKey(Jmeter_agentip_states record);

    Jmeter_agentip_states selectByIp(String ip);

    int updateAgentStatus(Jmeter_agentip_states record);

    List<Jmeter_agentip_states> selectByStatusToIp(List<String> listin);

    List<Jmeter_agentip_states> selectByStatusRunIp(List<String> listin);

    List<Jmeter_agentip_states> selectByStatusTpye(@Param("type") String type,List<String> listcomp);

    //查询所有
    List<Jmeter_agentip_states> queryAgentStatusByNull();

    //查询条件为:type
    List<Jmeter_agentip_states> queryAgentStatus(@Param("type") String type);

    //updatetype
    int updatetype(@Param("id") Integer id, @Param("type") String type);


    //根据id,禁用定时器:
    int pause(@Param("id") Integer id);

    //根据id,启用定时器:
    int enable(@Param("id") Integer id);


    //清空 为了得到最新可用的agnet ip
    int truncateTable();



    //insert 为了得到最新可用的agnet ip
    int insertRefresh(Jmeter_agentip_states record);



}