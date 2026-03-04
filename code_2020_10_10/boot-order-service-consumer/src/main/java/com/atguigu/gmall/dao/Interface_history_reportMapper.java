package com.atguigu.gmall.dao;

import com.atguigu.gmall.common.page.Criterion;
import com.atguigu.gmall.entity.Interface_history_report;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;


@Mapper
@Component
public interface Interface_history_reportMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Interface_history_report record);

    int insertSelective(Interface_history_report record);

    Interface_history_report selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Interface_history_report record);



    PageList<Map<String, Object>> queryApiExecuteLog4Manage(@Param("criterions") List<Criterion> criterions, PageBounds pb);

    int updateByPrimaryKey(Interface_history_report record);
}