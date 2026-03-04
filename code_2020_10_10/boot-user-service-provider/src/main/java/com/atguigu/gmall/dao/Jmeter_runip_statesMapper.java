package com.atguigu.gmall.dao;

import com.atguigu.gmall.entity.Jmeter_runip_states;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface Jmeter_runip_statesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Jmeter_runip_states record);

    int insertSelective(Jmeter_runip_states record);

    Jmeter_runip_states selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Jmeter_runip_states record);

    int updateByPrimaryKey(Jmeter_runip_states record);

    Jmeter_runip_states selectByIp(String ip);

    int updateRunIPStatus(Jmeter_runip_states record);


     //List<Provider_jmeter_agentip_states> selectByStatusToIp(String states);

}