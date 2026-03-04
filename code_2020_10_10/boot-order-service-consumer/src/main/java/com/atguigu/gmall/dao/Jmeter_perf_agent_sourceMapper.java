package com.atguigu.gmall.dao;

import com.atguigu.gmall.entity.Jmeter_perf_agent_source;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface Jmeter_perf_agent_sourceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Jmeter_perf_agent_source record);

    int insertSelective(Jmeter_perf_agent_source record);

    Jmeter_perf_agent_source selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Jmeter_perf_agent_source record);

    int updateByPrimaryKey(Jmeter_perf_agent_source record);

    List<Jmeter_perf_agent_source> queryAgentRourceByNull();

    List<Jmeter_perf_agent_source> queryAgentRource(@Param("ipaddress") String ipaddress);

}