package com.atguigu.gmall.dao;

import com.atguigu.gmall.entity.Jmeter_perf_run_ip;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface Jmeter_perf_run_ipMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Jmeter_perf_run_ip record);

    int insertSelective(Jmeter_perf_run_ip record);

    Jmeter_perf_run_ip selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Jmeter_perf_run_ip record);

    int updateByPrimaryKey(Jmeter_perf_run_ip record);

    List<Jmeter_perf_run_ip> selectByAll();
}