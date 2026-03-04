package com.atguigu.gmall.dao;

import com.atguigu.gmall.common.page.Criterion;
import com.atguigu.gmall.entity.Jmeter_perfor_current_report;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Mapper
@Component
public interface Jmeter_perfor_current_reportMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Jmeter_perfor_current_report record);

    int insertSelective(Jmeter_perfor_current_report record);

    Jmeter_perfor_current_report selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Jmeter_perfor_current_report record);

    int updateByPrimaryKey(Jmeter_perfor_current_report record);

    List<Map<String, Object>> queryApiExecuteLog4Manage(@Param("lastruntime") String lastruntime);

    List<Map<String, Object>> queryApiExecuteLog4ManageByIdAndPerfstarttime(@Param("uploadid") Integer uploadid, @Param("lastruntime") String lastruntime);

    PageList<Map<String, Object>> queryApiExecuteLog4Manage(@Param("criterions") List<Criterion> criterions, PageBounds pb);

     List<Map<String, Object>> detailQueryTypePerfReport(@Param("lastruntime") String lastruntime,  @Param("uploadid") List<Integer> uploadid);

     List<Map<String, Object>> sumPerfReport(@Param("lastruntime") String lastruntime,  @Param("uploadid") List<Integer> uploadid);


}