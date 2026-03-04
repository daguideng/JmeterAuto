package com.atguigu.gmall.dao;

import com.atguigu.gmall.common.page.Criterion;
import com.atguigu.gmall.entity.Statistics;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;


@Mapper
@Component
public interface StatisticsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Statistics record);

    int insertSelective(Statistics record);

    Statistics selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Statistics record);

    int updateByPrimaryKey(Statistics record);


    int deleteAll();

    public Boolean updateStatus_0_1(int statis_0);
    public Boolean updateStatus_1_2(int statis_1);

    PageList<Map<String, Object>> selectSimpleReport(@Param("criterions") List<Criterion> criterions, PageBounds pb);

    List <Statistics> queryApiExecuteSumCount(@Param("criterions") List<Criterion> criterions);

    PageList<Map<String, Object>> queryApiExecuteLog4Manage(@Param("criterions") List<Criterion> criterions, PageBounds pb);

    public int updatePerfLastRunTime(@Param("lastruntime") String lastruntime,@Param("id") int id) ;

    List<Statistics> selectByAll(Map<String, String> map);

    //随时查询当前进度报告：
    List<Statistics> selectProgressReport(@Param("state") Integer state);




    //新性能测试报告：
    List<Statistics> historyLinkTypeReport(@Param("uploadid") Integer uploadid, @Param("scriptname") String scriptname,@Param("label") String label);

    List<Statistics> linkTypeReport(@Param("uploadid") Integer uploadid, @Param("perfstarttime") String perfstarttime,@Param("label") String label);

    List<Statistics> linkTypeReportSucessCount(@Param("uploadid") Integer uploadid, @Param("perfstarttime") String perfstarttime, @Param("ko") String ko);

    List<Statistics> linkTypeReportFailCount(@Param("uploadid") Integer uploadid, @Param("perfstarttime") String perfstarttime, @Param("ko") String ko);

    List<Statistics> historyLinkTypeReportSucessCount(@Param("uploadid") Integer uploadid, @Param("scriptname") String scriptname, @Param("ko") String ko);

    List<Statistics> historyLinkTypeReportFailCount(@Param("uploadid") Integer uploadid, @Param("scriptname") String scriptname, @Param("ko") String ko);

    List<Statistics> detailQueryTypeReport(@Param("perfstarttime") String perfstarttime);

    List<Statistics> detailHistoryQueryTypeReport(@Param("scriptname") String scriptname);

    List<Statistics> historyReportTypeReport(@Param("scriptName") String scriptName, @Param("label") String label);




}