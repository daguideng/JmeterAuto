package com.atguigu.gmall.dao;

import com.atguigu.gmall.entity.Jmeter_perf_top5_errors;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface Jmeter_perf_top5_errorsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Jmeter_perf_top5_errors record);

    int insertSelective(Jmeter_perf_top5_errors record);

    Jmeter_perf_top5_errors selectByPrimaryKey(Integer id);



    List<Jmeter_perf_top5_errors> selectErrorReport(@Param(value="lastruntime")String lastruntime, @Param(value="scriptname")String scriptname, @Param(value="transactionName")String transactionName);


    List<Jmeter_perf_top5_errors> selectAllErrorReport(@Param(value="lastruntime")String lastruntime, @Param(value="scriptname")String scriptname,@Param(value="transactionName") String transactionName);

    List<Jmeter_perf_top5_errors> selectJplPath(@Param(value="lastruntime")String lastruntime, @Param(value="scriptname")String scriptname,@Param(value="transactionName") String transactionName);

    int updateByPrimaryKeySelective(Jmeter_perf_top5_errors record);

    int updateByPrimaryKey(Jmeter_perf_top5_errors record);
}