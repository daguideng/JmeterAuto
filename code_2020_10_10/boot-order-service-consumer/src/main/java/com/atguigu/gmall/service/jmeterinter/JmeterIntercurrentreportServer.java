package com.atguigu.gmall.service.jmeterinter;

import com.atguigu.gmall.common.page.Criterion;
import com.atguigu.gmall.common.web.InterCurrentReortQuery;
import com.atguigu.gmall.dao.Interface_current_reportMapper;
import com.atguigu.gmall.entity.Interface_current_report;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: dengdagui
 * @Description:jmeter接口当前报告各种服务操作：
 * @Date: Created in 2018-8-7
 */
@Slf4j
@Service
public class JmeterIntercurrentreportServer {



    @Autowired
    private Interface_current_reportMapper interface_current_reportMapper;


    public void insertStatistics(Interface_current_report record) throws Exception {

        int count = interface_current_reportMapper.insert(record);

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
            Boolean targer = interface_current_reportMapper.updateStatus_0_1(state);
            if (targer == true) {
                log.info("更新数据库Jmetercurrentreport状态" + state + "成功!");
            } else {
                log.info("更新数据库Jmetercurrentreport状态" + state + "失败!");
            }
        } catch (Exception e) {
            log.info("更新数据库Jmetercurrentreport状态" + state + "失败!");
            e.printStackTrace();
        }

    }


    /***
     * 对表Statistics的各种状态的更新，对于某种状态，分别可以查看，发邮件报告，数据报表隐藏
     * 数据发送报告状态:1  ---->数据隐藏状态:2
     */
    public void UpdateStatus_1_2(Integer state) {

        try {
            Boolean targer = interface_current_reportMapper.updateStatus_1_2(state);
            if (targer == true) {
                log.info("更新数据库Jmetercurrentreport状态" + state + "成功!");
            } else {
                log.info("更新数据库Jmetercurrentreport状态" + state + "失败!");
            }

        } catch (Exception e) {
            log.info("更新数据库statistics状态" + state + "失败!");
        }

    }


    /**
     * 根据query分页查询出Per的性能结果列表
     */
    public Map <String, Object> list(InterCurrentReortQuery queryInfo) {
        PageBounds pb = queryInfo.getPBAsc();
        List <Criterion> criterions = queryInfo.criterion();
        log.info("查询Query对象:{}");
        PageList <Map <String, Object>> apiExecuteLogList = interface_current_reportMapper.queryApiExecuteLog4Manage(criterions, pb);
        log.info("数据库查询结结果:{}," + apiExecuteLogList.size());

        List <Interface_current_report> sumcount = interface_current_reportMapper.queryApiExecuteSumCount(criterions);
        int totalCount = sumcount.size();


        /*
            总记录数：totalRecord
            每页最大记录数：maxResult
            总页数：totalPage
            totalPage = (totalRecord + maxResult -1) / maxResult;
            其中 maxResult  - 1 就是 totalRecord / maxResult 的最大的余数
            */

        int page = pb.getPage();
        int totalPages = (totalCount + page - 1) / page;


        Map <String, Object> m = new HashMap <>();
        m.put("totalCount", totalCount);
        m.put("totalPages", totalPages);
        m.put("page", page);
        m.put("list", apiExecuteLogList);
        return m;
    }


    /***
     * 清空当天报告：
     */
    public void deleteMiddNightStatistics() {

        int count = interface_current_reportMapper.deleteAll();
        if (count > 0) {
            log.info("性能报告初始化成功！");

        } else {
            log.info("性能报告初始化失败！");
        }

    }


    /***
     * 生成简洁报告：
     */
    public Map <String, Object> simpleReport(InterCurrentReortQuery query) {

        PageBounds pb = query.getPBAsc();
        List <Criterion> criterions = query.criterion();
        log.info("查询Query对象:{}");
        PageList <Map <String, Object>> apiExecuteLogList = interface_current_reportMapper.selectSimpleReport(criterions, pb);
        log.info("数据库查询结结果:{}," + apiExecuteLogList.size());

   

        Map <String, Object> m = new HashMap <>();
        //      m.put("totalCount", apiExecuteLogList.getPaginator().getTotalCount());
        //      m.put("totalPages", apiExecuteLogList.getPaginator().getTotalPages());
        //      m.put("page", apiExecuteLogList.getPaginator().getPage());
        m.put("list", apiExecuteLogList);
        return m;


    }


    /**
     * 生成SingleType 报告：
     */

    public List<Interface_current_report> singleTypeReport(String  scriptName) {
        log.info("查询Query对象:{}");
        List<Interface_current_report> singleTypeList = interface_current_reportMapper.singleTypeReport(scriptName);
        log.info("数据库查询结结果:{}," + singleTypeList.size());
        return singleTypeList;
    }



    /**
     * 生成LinkType 报告：
     */
    public List<Interface_current_report> linkTypeReport(String id,String lastruntime) {
        int uploadid = Integer.valueOf(id);
        String label = "Total";

        log.info("查询Query对象:{}");
        List<Interface_current_report> linkTypeList = interface_current_reportMapper.linkTypeReport(uploadid,lastruntime,label);
        log.info("数据库查询结结果:{}," + linkTypeList.size());
        return linkTypeList;
    }



    /**
     * 生成历史LinkType 报告：
     */
    public List<Interface_current_report> historyLinkTypeReport(String id,String scriptname) {
        int uploadid = Integer.valueOf(id);
        String label = "Total";

        log.info("查询Query对象:{}");
        List<Interface_current_report> historylinkTypeList = interface_current_reportMapper.historyLinkTypeReport(uploadid,scriptname,label);
        log.info("数据库查询结结果:{}," + historylinkTypeList.size());
        return historylinkTypeList;
    }


    /**
     * 生成LinkType 报告时多少运行成功，多少运行失败
     */
    public List<Interface_current_report> runSucessFail(String id,String lastruntime,String ko) {
        int uploadid = Integer.valueOf(id);
        log.info("查询Query对象:{}");
      //  int label_trager = 0;

        List<Interface_current_report> linkTypeList = null ;
        if("0.00".equals(ko)) {   //统计成功数
           linkTypeList = interface_current_reportMapper.linkTypeReportSucessCount(uploadid, lastruntime, ko);
        }else{    //统计失败数
            linkTypeList = interface_current_reportMapper.linkTypeReportFailCount(uploadid, lastruntime, ko);
        }
        log.info("数据库查询结结果:{}," + linkTypeList.size());
        return linkTypeList;
    }



    /**
     * 生成histoyrLinkType 报告时多少运行成功，多少运行失败
     */
    public List<Interface_current_report> historyRunSucessFail(String id,String scriptname,String ko) {
        int uploadid = Integer.valueOf(id);
        log.info("查询Query对象:{}");
        //  int label_trager = 0;

        List<Interface_current_report> linkTypeList = null ;
        if("0.00".equals(ko)) {   //统计成功数
            linkTypeList = interface_current_reportMapper.historyLinkTypeReportSucessCount(uploadid, scriptname, ko);
        }else{    //统计失败数
            linkTypeList = interface_current_reportMapper.historyLinkTypeReportFailCount(uploadid, scriptname, ko);
        }
        log.info("数据库查询结结果:{}," + linkTypeList.size());
        return linkTypeList;
    }




    /**
     * 生成DetailQueryType 报告：
     */
    public List<Map<String, Object>> detailQueryTypeReport(String lastruntime, Integer uploadid) {
        System.out.printf("lastruntime---->"+lastruntime);
        log.info("查询Query对象:{}, uploadid: {}", uploadid);
        List<Map<String, Object>>  detailQueryTypeList = interface_current_reportMapper.detailQueryTypeReport(lastruntime, uploadid);
        log.info("数据库查询结结果:------->{}," + detailQueryTypeList.size());
        return detailQueryTypeList;
    }



    /**
     * 生成DetailHistoryQueryType 报告：
     */
    public List<Map<String, Object>>  detailHistoryQueryTypeReport(String scriptname, Integer uploadId) {
        System.out.printf("scriptname---->"+scriptname);
        log.info("查询Query对象:{}, uploadId={}", uploadId);
        List<Map<String, Object>> detailHistoryQueryTypeList = interface_current_reportMapper.detailHistoryQueryTypeReport(scriptname, uploadId);
        log.info("数据库查询结结果:------->{}," + detailHistoryQueryTypeList.size());
        return detailHistoryQueryTypeList;
    }




    /**
     * 生成HistoryReportType 报告：
     */
    public List<Interface_current_report> historyReportTypeReport(String scriptName,String label) {
       //urlSuffix = "[{\"operator\":\"like\",\"property\":\"scriptname\",\"value\":\"" + scriptName + "\"}]";
        log.info("查询Query对象:{}");
        List<Interface_current_report> historyReportTypeList = interface_current_reportMapper.historyReportTypeReport(scriptName,label);
        log.info("数据库查询结结果:{}," + historyReportTypeList.size());
        return historyReportTypeList;
    }



    /**
     * 生成其它 报告：
     */
    public List<Interface_current_report> otherTypeReport(Integer state,String label) {
        //urlSuffix = "[{\"operator\":\"like\",\"property\":\"scriptname\",\"value\":\"" + scriptName + "\"}]";
        log.info("查询Query对象:{}");
        List<Interface_current_report> otherReportTypeList = interface_current_reportMapper.otherTypeReport(state,label);
        log.info("数据库查询结结果:{}," + otherReportTypeList.size());
        return otherReportTypeList;
    }




      /**
     * 生成DetailQueryType 报告：
     */
    public List<Map<String, Object>> detailQueryTypeInterEmailfReport(String lastruntime, List<Integer> uploadid) {
        System.out.println("lastruntime11---->"+lastruntime);
        System.out.println("uploadid11---->"+uploadid);
        log.info("查询Query对象:{}, uploadid: {}", uploadid);
        List<Map<String, Object>>  detailQueryTypeList = interface_current_reportMapper.detailQueryTypeInterReport(lastruntime, uploadid);
        log.info("数据库查询结结果:------->{}," + detailQueryTypeList.size());
        return detailQueryTypeList;
    }



    /**
     * 生成Sum汇总报告：
     */
    public List<Map<String, Object>> sumInterReport(String lastruntime, List<Integer> uploadid) {
        System.out.println("lastruntime11---->"+lastruntime);
        System.out.println("uploadid11---->"+uploadid);
        log.info("查询Query对象:{}, uploadid: {}", uploadid);
        List<Map<String, Object>>  detailQueryTypeList = interface_current_reportMapper.sumInterReport(lastruntime, uploadid);
        log.info("数据库查询结结果:------->{}," + detailQueryTypeList.size());
        return detailQueryTypeList;
    }






}


