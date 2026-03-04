package com.atguigu.gmall.service.jmeterperf;


import com.atguigu.gmall.common.page.Criterion;
import com.atguigu.gmall.common.web.PerfCurrentReortQuery;
import com.atguigu.gmall.dao.StatisticsMapper;
import com.atguigu.gmall.entity.Statistics;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Map键常量类
 */
class MapKeyConstants {
    static final String TOTAL_COUNT = "totalCount";
    static final String TOTAL_PAGES = "totalPages";
    static final String PAGE = "page";
    static final String LIST = "list";
}

/**
 * @Author: dengdagui
 * @Description:jmeter性能测试当前报告各种服务操作：
 * @Date: Created in 2018-8-7
 */
@Slf4j
@Component
public class StatisticsServer {



    @Autowired
    private StatisticsMapper statisticsMapper;
    


    public void insertStatistics(Statistics record) throws Exception {

        int count = statisticsMapper.insert(record);

        if (count > 0) {
            log.info("性能报告数据入库成功！");

        } else {
            log.info("性能报告数据入库失败！");

        }

    }


    /***
     * 对表Statistics的各种状态的更新，对于某种状态，分别可以查看，发邮件报告，数据报表隐藏
     * 数据初始状态:0  ---->数据发送报告状态:1
     */
    public void UpdateStatus_0_1(Integer state) {


        try {
            Boolean targer = statisticsMapper.updateStatus_0_1(state);
            if (targer == true) {
                log.info("更新数据库statistics状态" + state + "成功!");
            } else {
                log.info("更新数据库statistics状态" + state + "失败!");
            }
        } catch (Exception e) {
            log.info("更新数据库statistics状态" + state + "失败!");
            e.printStackTrace();
        }

    }


    /***
     * 对表Statistics的各种状态的更新，对于某种状态，分别可以查看，发邮件报告，数据报表隐藏
     * 数据发送报告状态:1  ---->数据隐藏状态:2
     */
    public void UpdateStatus_1_2(Integer state) {

        try {
            Boolean targer = statisticsMapper.updateStatus_1_2(state);
            if (targer == true) {
                log.info("更新数据库statistics状态" + state + "成功!");
            } else {
                log.info("更新数据库statistics状态" + state + "失败!");
            }

        } catch (Exception e) {
            log.info("更新数据库statistics状态" + state + "失败!");
        }

    }


    /**
     * 根据query分页查询出Per的性能结果列表
     */
    public Map <String, Object> list(PerfCurrentReortQuery query) {
        PageBounds pb = query.getPBAsc();
        List<Criterion> criterions = query.criterion();
        log.info("查询Query对象:{}", query);
        PageList<Map <String, Object>> apiExecuteLogList = statisticsMapper.queryApiExecuteLog4Manage(criterions, pb);
        log.info("数据库查询结果:{}条数据", apiExecuteLogList.size());

        List<Statistics> sumcount = statisticsMapper.queryApiExecuteSumCount(criterions);
        int totalCount = sumcount.size();
        int pageSize = pb.getLimit();
        int totalPages = (totalCount + pageSize - 1) / pageSize; // 计算总页数的公式
        int currentPage = pb.getPage();

        Map <String, Object> resultMap = new HashMap<>();
        resultMap.put(MapKeyConstants.TOTAL_COUNT, totalCount);
        resultMap.put(MapKeyConstants.TOTAL_PAGES, totalPages);
        resultMap.put(MapKeyConstants.PAGE, currentPage);
        resultMap.put(MapKeyConstants.LIST, apiExecuteLogList);

        log.info("分页查询结果:{}总记录数, {}总页数, 当前第{}页", 
                totalCount, totalPages, currentPage);

        return resultMap;
    }


    /***
     * 清空当天报告：
     */
    public void deleteMiddNightStatistics() {

        int count = statisticsMapper.deleteAll();
        if (count > 0) {
            log.info("性能报告初始化成功！");

        } else {
            log.info("性能报告初始化失败！");
        }

    }


    /**
     * 性能测试不同状态随时更新数据库的配置状态
     */
    public void updateLastRunTime(String lastruntime, int id) {


        int result = statisticsMapper.updatePerfLastRunTime(lastruntime, id);


        if (result > 0) {
            log.info("更新lastrunTime状态 " + result + " 成功!");
        } else {
            log.info("更新lastrunTime状态 " + result + " 失败!");
        }

    }


    /***
     * 生成简洁报告：
     */
     public Map<String, Object>  simpleReport(PerfCurrentReortQuery query){
         PageBounds pb = query.getPBAsc();
         List<Criterion> criterions = query.criterion();
         
         log.info("查询简洁报告Query对象:{}", query);
         PageList<Map<String, Object>> apiExecuteLogList = statisticsMapper.selectSimpleReport(criterions, pb);
         log.info("简洁报告数据库查询结果:{}条数据", apiExecuteLogList.size());
         
         Map<String, Object> resultMap = new HashMap<>();
         resultMap.put(MapKeyConstants.LIST, apiExecuteLogList);
         
         return resultMap;
     }







    /**
     * 生成LinkType 报告：
     */
    public List<Statistics> linkTypeReport(String id,String perfstarttime) {
        int uploadid = Integer.valueOf(id);
        String label = "Total";

        log.info("查询Query对象:{}");
        List<Statistics> linkTypeList = statisticsMapper.linkTypeReport(uploadid,perfstarttime,label);
        log.info("数据库查询结结果:{}," + linkTypeList.size());
        return linkTypeList;
    }








    /**
     * 生成LinkType 报告时多少运行成功，多少运行失败
     */
    public List<Statistics> runSucessFail(String id,String perfstarttime,String ko) {
        int uploadid = Integer.valueOf(id);
        log.info("查询Query对象:{}");
        //  int label_trager = 0;

        List<Statistics> linkTypeList = null ;
        if("0.00".equals(ko)) {   //统计成功数
            linkTypeList = statisticsMapper.linkTypeReportSucessCount(uploadid, perfstarttime, ko);
        }else{    //统计失败数
            linkTypeList = statisticsMapper.linkTypeReportFailCount(uploadid, perfstarttime, ko);
        }
        log.info("数据库查询结结果:{}," + linkTypeList.size());
        return linkTypeList;
    }







    /**
     * 生成历史LinkType 报告：
     */
    public List<Statistics> historyLinkTypeReport(String id,String scriptname) {
        int uploadid = Integer.valueOf(id);
        String label = "Total";

        log.info("查询Query对象:{}");
        List<Statistics> historylinkTypeList = statisticsMapper.historyLinkTypeReport(uploadid,scriptname,label);
        log.info("数据库查询结结果:{}," + historylinkTypeList.size());
        return historylinkTypeList;
    }







    /**
     * 生成histoyrLinkType 报告时多少运行成功，多少运行失败
     */
    public List<Statistics> historyRunSucessFail(String id,String scriptname,String ko) {
        int uploadid = Integer.valueOf(id);
        log.info("查询Query对象:{}");
        //  int label_trager = 0;

        List<Statistics> linkTypeList = null ;
        if("0.00".equals(ko)) {   //统计成功数
            linkTypeList = statisticsMapper.historyLinkTypeReportSucessCount(uploadid, scriptname, ko);
        }else{    //统计失败数
            linkTypeList = statisticsMapper.historyLinkTypeReportFailCount(uploadid, scriptname, ko);
        }
        log.info("数据库查询结结果:{}," + linkTypeList.size());
        return linkTypeList;
    }





    /**
     * 生成DetailQueryType 报告：
     */
    public List<Statistics> detailQueryTypeReport(String perfstarttime) {
        System.out.printf("perfstarttime---->"+perfstarttime);
        log.info("查询Query对象:{}");
        List<Statistics> detailQueryTypeList = statisticsMapper.detailQueryTypeReport(perfstarttime);
        log.info("数据库查询结结果:------->{}," + detailQueryTypeList.size());
        return detailQueryTypeList;
    }





    /**
     * 生成DetailHistoryQueryType 报告：
     */
    public List<Statistics> detailHistoryQueryTypeReport(String scriptname) {
        System.out.printf("scriptname---->"+scriptname);
        log.info("查询Query对象:{}");
        List<Statistics> detailHistoryQueryTypeList = statisticsMapper.detailHistoryQueryTypeReport(scriptname);
        log.info("数据库查询结结果:------->{}," + detailHistoryQueryTypeList.size());
        return detailHistoryQueryTypeList;
    }







}


