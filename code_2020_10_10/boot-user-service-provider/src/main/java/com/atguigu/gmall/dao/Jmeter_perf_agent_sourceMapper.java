package com.atguigu.gmall.dao;

import com.atguigu.gmall.entity.Jmeter_perf_agent_source;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component("provider_jmeter_perf_agent")
public interface Jmeter_perf_agent_sourceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Jmeter_perf_agent_source record);

    int insertSelective(Jmeter_perf_agent_source record);

    Jmeter_perf_agent_source selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Jmeter_perf_agent_source record);

    int updateByPrimaryKey(Jmeter_perf_agent_source record);

    Jmeter_perf_agent_source selectByIp(String ip);

    int updateAgentSource(Jmeter_perf_agent_source record);
}