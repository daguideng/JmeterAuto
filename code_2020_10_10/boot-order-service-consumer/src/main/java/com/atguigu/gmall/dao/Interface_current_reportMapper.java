package com.atguigu.gmall.dao;

import com.atguigu.gmall.common.page.Criterion;
import com.atguigu.gmall.entity.Interface_current_report;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;


@Mapper
@Component
public interface Interface_current_reportMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Interface_current_report record);

    int insertSelective(Interface_current_report record);

    Interface_current_report selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Interface_current_report record);

    int updateByPrimaryKey(Interface_current_report record);



    int deleteAll();

    public Boolean updateStatus_0_1(int statis_0);
    public Boolean updateStatus_1_2(int statis_1);

    List<Interface_current_report> queryApiExecuteSumCount(@Param("criterions") List<Criterion> criterions);

    PageList<Map<String, Object>> selectSimpleReport(@Param("criterions") List<Criterion> criterions, PageBounds pb);

    PageList<Map<String, Object>> queryApiExecuteLog4Manage(@Param("criterions") List<Criterion> criterions, PageBounds pb);


    List<Interface_current_report> selectByAll(Map<String, String> map);

    //随时查询当前进度报告：
    List<Interface_current_report> selectProgressReport(@Param("state") Integer state);

    List<Interface_current_report> singleTypeReport(@Param("scriptName") String scriptName);



    List<Interface_current_report> linkTypeReport(@Param("uploadid") Integer uploadid, @Param("lastruntime") String lastruntime,@Param("label") String label);


    List<Interface_current_report> historyLinkTypeReport(@Param("uploadid") Integer uploadid, @Param("scriptname") String scriptname,@Param("label") String label);


    List<Interface_current_report> linkTypeReportSucessCount(@Param("uploadid") Integer uploadid, @Param("lastruntime") String lastruntime, @Param("ko") String ko);

    List<Interface_current_report> historyLinkTypeReportFailCount(@Param("uploadid") Integer uploadid, @Param("scriptname") String lastruntime, @Param("ko") String ko);

    List<Interface_current_report> historyLinkTypeReportSucessCount(@Param("uploadid") Integer uploadid, @Param("scriptname") String scriptname, @Param("ko") String ko);



    List<Interface_current_report> linkTypeReportFailCount(@Param("uploadid") Integer uploadid, @Param("lastruntime") String lastruntime, @Param("ko") String ko);


    List<Map<String, Object>>  detailQueryTypeReport(@Param("lastruntime") String lastruntime, @Param("uploadid") Integer uploadid);


    List<Map<String, Object>> detailHistoryQueryTypeReport(@Param("scriptname") String scriptname, @Param("uploadid") Integer uploadid);


    List<Interface_current_report> historyReportTypeReport(@Param("scriptName") String scriptName, @Param("label") String label);

    List<Interface_current_report> otherTypeReport(@Param("state") Integer state, @Param("label") String label);


    List<Map<String, Object>> detailQueryTypeInterReport(@Param("lastruntime") String lastruntime,  @Param("uploadid") List<Integer> uploadid);


    List<Map<String, Object>> sumInterReport(@Param("lastruntime") String lastruntime,  @Param("uploadid") List<Integer> uploadid);



}