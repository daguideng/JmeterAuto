package com.atguigu.gmall.dao;

import com.atguigu.gmall.entity.Provider_jmeter_agentip_states;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;


@Component
@Mapper
public interface Provider_jmeter_agentip_statesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Provider_jmeter_agentip_states record);

    int insertSelective(Provider_jmeter_agentip_states record);

    Provider_jmeter_agentip_states selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Provider_jmeter_agentip_states record);

    int updateByPrimaryKey(Provider_jmeter_agentip_states record);


    Provider_jmeter_agentip_states selectByIp(@Param("ipaddress") String ipaddress);

    int updateRunIPStatus(Provider_jmeter_agentip_states record);

    int updateRunIPDownStatus(Provider_jmeter_agentip_states record);
}