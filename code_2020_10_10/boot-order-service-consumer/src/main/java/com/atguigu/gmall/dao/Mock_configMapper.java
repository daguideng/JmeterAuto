package com.atguigu.gmall.dao;

import com.atguigu.gmall.common.page.Criterion;
import com.atguigu.gmall.entity.Mock_config;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
@Mapper
public interface Mock_configMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Mock_config record);

    int insertSelective(Mock_config record);

    Mock_config selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Mock_config record);

    int updateByPrimaryKey(Mock_config record);


    int updateStatusById(@Param("tag") String tag,@Param("id") Integer id);

    List<Mock_config> selectByUrl(@Param("mock_url") String mock_url);

    Mock_config selectByUrlParm(@Param("mock_url") String mock_url);

    List<Mock_config> queryApiExecuteSumCount(@Param("criterions") List<Criterion> criterions);

    PageList<Map<String, Object>> queryApiExecuteLog4Manage(@Param("criterions") List<Criterion> criterions, PageBounds pb);
}