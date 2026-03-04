package com.atguigu.gmall.dao;

import com.atguigu.gmall.common.page.Criterion;
import com.atguigu.gmall.entity.Jmeter_perfor_history_report;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;


@Mapper
@Component
public interface Jmeter_perfor_history_reportMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Jmeter_perfor_history_report record);

    int insertSelective(Jmeter_perfor_history_report record);

    Jmeter_perfor_history_report selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Jmeter_perfor_history_report record);

    int updateByPrimaryKey(Jmeter_perfor_history_report record);


    PageList<Map<String, Object>> queryApiExecuteLog4Manage(@Param("criterions") List<Criterion> criterions, PageBounds pb);


    int updatePerfLastRunTime(@Param("lastruntime") String lastruntime,@Param("id") int id);


    List<Map<String, Object>> queryApiExecuteLog4ManageByIdAndScriptname(@Param("id") Integer id, @Param("scriptname") String scriptname);


}