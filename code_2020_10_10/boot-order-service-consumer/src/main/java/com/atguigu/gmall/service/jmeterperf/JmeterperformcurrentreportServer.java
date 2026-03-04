package com.atguigu.gmall.service.jmeterperf;

import com.atguigu.gmall.common.page.Criterion;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import java.util.HashMap;
import java.util.Map;
import com.atguigu.gmall.dao.Jmeter_perfor_current_reportMapper;
import com.atguigu.gmall.dao.Jmeter_perfor_history_reportMapper;
import com.atguigu.gmall.entity.Jmeter_perfor_current_report;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: dengdagui
 * @Description: jmeter性能测试当前报告服务操作
 * @Date: Created in 2018-8-7
 */
@Slf4j
@Service
public class JmeterperformcurrentreportServer {

    @Autowired
    private Jmeter_perfor_current_reportMapper jmeter_perfor_current_reportMapper;

    @Autowired
    private Jmeter_perfor_history_reportMapper jmeter_perfor_history_reportMapper;

    public void insertCurrentReport(Jmeter_perfor_current_report record) throws Exception {

        int count = jmeter_perfor_current_reportMapper.insert(record);

        if (count > 0) {
            log.info("性能当前报告数据入库成功！");

        } else {
            log.info("性能当前报告数据入库失败！");

        }

    }

    /**
     * 根据perfstarttime查询详细报告
     */
    public List<Map<String, Object>> detailQueryTypeReport(String lastruntime, String scriptname, Integer uploadId) {
        log.info("===== 开始查询jmeter_perfor_current_report表 =====");
        log.info("传入的lastruntime参数值: [{}]", lastruntime);
        log.info("参数类型: {}", lastruntime != null ? lastruntime.getClass().getName() : "null");
        log.info("传入的scriptname参数值: [{}]", scriptname);
        log.info("参数类型: {}", scriptname != null ? scriptname.getClass().getName() : "null");
        log.info("传入的uploadId参数值: [{}]", uploadId);
        
        // 构建查询条件
        List<Criterion> criterions = new ArrayList<>();
        
        // 添加lastruntime条件 - 将时间戳转换为日期格式进行模糊查询
        if (lastruntime != null && !lastruntime.trim().isEmpty()) {
            // 判断是否为时间戳格式（纯数字）
            if (lastruntime.matches("\\d+")) {
                // 时间戳格式，转换为日期时间格式
                String dateTime = com.atguigu.gmall.common.utils.DateUtil.timeStamp2Date(lastruntime, "yyyy-MM-dd HH:mm:ss");
                log.info("时间戳 {} 转换为日期格式: {}", lastruntime, dateTime);
                criterions.add(new Criterion("lastruntime >=", dateTime));
            } else {
                // 已经是日期时间格式，直接使用
                criterions.add(new Criterion("lastruntime >=", lastruntime));
            }
        }
        
        // 添加scriptname条件（支持模糊查询）
        if (scriptname != null && !scriptname.trim().isEmpty()) {
            criterions.add(new Criterion("scriptname like", "%" + scriptname + "%"));
        }
        
        // 添加uploadid条件
        if (uploadId != null && uploadId > 0) {
            criterions.add(new Criterion("uploadid =", uploadId));
        }
        
        // 执行查询 - 即使条件为空也查询所有数据
        List<Map<String, Object>> result = null;
        if (!criterions.isEmpty()) {
            result = jmeter_perfor_current_reportMapper.queryApiExecuteLog4Manage(criterions, new PageBounds());
        } else {
            log.info("查询条件为空，查询所有数据");
            // 如果没有条件，查询所有数据
            result = jmeter_perfor_current_reportMapper.queryApiExecuteLog4Manage(new ArrayList<>(), new PageBounds());
        }
        
        // 检查查询结果
        if (result == null) {
            log.error("查询结果为null");
        } else {
            log.info("数据库查询结果: {}条数据", result.size());
            
            // 如果有数据，打印第一条记录
            if (result.size() > 0) {
                log.info("第一条记录内容: {}", result.get(0));
                log.info("记录中的starttime字段值: {}", result.get(0).get("starttime"));
                log.info("记录中的scriptname字段值: {}", result.get(0).get("scriptname"));
                log.info("记录中的lastruntime字段值: {}", result.get(0).get("lastruntime"));
            } else {
                log.warn("查询结果为空，可能原因:");
                log.warn("1. lastruntime=[{}]且scriptname=[{}]的数据", lastruntime, scriptname);
                log.warn("2. 参数格式不正确");
                log.warn("3. 数据库查询条件有问题");
            }
        }
        
        log.info("===== 查询结束 =====");
        return result;
    }

    /**
     * 根据状态统计数量
     */
    public int countByStatus(String uploadid, String lastruntime, String scriptname, String status) {
        log.info("统计jmeter_perfor_current_report表中状态为{}的数据数量", status);
        log.info("统计条件 - lastruntime: {}, scriptname: {}", lastruntime, scriptname);
        
        // 构建查询条件
        List<Criterion> criterions = new ArrayList<>();
        
        // 添加lastruntime条件
        if (lastruntime != null && !lastruntime.trim().isEmpty()) {
            criterions.add(new Criterion("lastruntime >=", lastruntime));
        }
        
        // 添加scriptname条件（支持模糊查询）
        if (scriptname != null && !scriptname.trim().isEmpty()) {
            criterions.add(new Criterion("scriptname like", "%" + scriptname + "%"));
        }
        
        // 执行查询
        List<Map<String, Object>> result = null;
        if (!criterions.isEmpty()) {
            result = jmeter_perfor_current_reportMapper.queryApiExecuteLog4Manage(criterions, new PageBounds());
        } else {
            log.warn("查询条件为空，将返回空结果");
            result = new ArrayList<>();
        }
        
        int count = 0;
        for (Map<String, Object> item : result) {
            if (status.equals(String.valueOf(item.get("ko")))) {
                count++;
            }
        }
        
        log.info("状态为{}的数据数量: {}", status, count);
        return count;
    }
    
    /**
     * 根据id和perfstarttime查询链接类型报告
     */
    public List<Map<String, Object>> linkTypeReport(String uploadid, String lastruntime) {
        log.info("===== 开始执行linkTypeReport查询 =====");
        log.info("查询参数 - uploadid: {}, lastruntime: {}", uploadid, lastruntime);
        
        // 将String类型的id转换为Integer
        Integer idInt = null;
        try {
            idInt = Integer.parseInt(uploadid);
            log.info("uploadid转换成功: {} -> {}", uploadid, idInt);      
        } catch (NumberFormatException e) {
            log.error("uploadid格式转换错误: {}", uploadid, e);
            return new ArrayList<>(); // 返回空列表避免空指针异常
        }
        
        // 调用支持id和lastruntime参数的mapper方法
        log.info("准备调用mapper方法: queryApiExecuteLog4ManageByIdAndPerfstarttime");
        List<Map<String, Object>> result = jmeter_perfor_current_reportMapper.queryApiExecuteLog4ManageByIdAndPerfstarttime(idInt, lastruntime);
        
        // 检查查询结果
        if (result == null) {
            log.error("查询结果为null");
        } else {
            log.info("linkTypeReport查询结果: {}条数据", result.size());
            
            // 如果有数据，打印第一条记录
            if (result.size() > 0) {
                log.info("第一条记录内容: {}", result.get(0));
                log.info("记录中的starttime字段值: {}", result.get(0).get("starttime"));
                log.info("记录中的endtime字段值: {}", result.get(0).get("endtime"));
            } else {
                log.warn("查询结果为空，可能原因:");
                log.warn("1. 表中没有匹配id=[{}]和lastruntime=[{}]的数据", idInt, lastruntime);
                log.warn("2. 参数格式不正确");
                log.warn("3. 数据库查询条件有问题");
            }
        }
        
        log.info("===== linkTypeReport查询结束 =====");
        return result;
    }
    
    /**
     * 根据id和perfstarttime查询历史链接类型报告
     * @return 包含报告数据的Map，key包括starttime、endtime、elapsedtime和data(原始数据列表)
     */
    public Map<String, Object> historyLinkTypeReport(String id, String lastruntime) {
        log.info("查询历史jmeter_perfor_current_report表，id: {}, lastruntime: {}", id, lastruntime);
        
        // 将String类型的id转换为Integer
        Integer idInt = null;
        try {
            idInt = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            log.error("id格式转换错误: {}", id, e);
            return new HashMap<>(); // 返回空Map避免空指针异常
        }
        
        // 使用修改后的mapper方法，同时传入id和lastruntime参数
        List<Map<String, Object>> result = jmeter_perfor_current_reportMapper.queryApiExecuteLog4ManageByIdAndPerfstarttime(idInt, lastruntime);
        log.info("historyLinkTypeReport查询结果: {}条数据", result.size());
        
        Map<String, Object> mapResult = new HashMap<>();
        if (result != null && !result.isEmpty()) {
            mapResult.put("data", result);
            mapResult.put("starttime", result.get(0).get("starttime"));
            mapResult.put("endtime", result.get(result.size()-1).get("endtime"));
        }
        
        return mapResult;
    }

    /**
     * 从jmeter_perfor_history_report表中查询label包含特定内容的数据
     */
    public List<Map<String, Object>> queryHistoryReportByLabel(String labelContains) {
        log.info("===== 开始查询jmeter_perfor_history_report表 =====");
        log.info("查询条件: label包含[{}]", labelContains);
        
        // 构建查询条件
        List<Criterion> criterions = new ArrayList<>();
        // 创建Criterion对象，正确构建LIKE查询条件
        // 条件中不包含参数占位符，让MyBatis自动处理参数绑定
        Criterion criterion = new Criterion("label LIKE", "%" + labelContains + "%");
        criterions.add(criterion);
        
        // 执行查询，使用空的PageBounds对象而不是null
        List<Map<String, Object>> result = jmeter_perfor_history_reportMapper.queryApiExecuteLog4Manage(criterions, new PageBounds());
        
        // 检查查询结果
        if (result == null) {
            log.error("查询结果为null");
        } else {
            log.info("数据库查询结果: {}条数据", result.size());
            
            // 如果有数据，打印第一条记录
            if (result.size() > 0) {
                log.info("第一条记录内容: {}", result.get(0));
            }
        }
        
        log.info("===== 查询结束 =====");
        return result;
    }

    /**
     * 从jmeter_perfor_history_report表中查询label包含特定内容且uploadid匹配的数据
     */
    public List<Map<String, Object>> queryHistoryReportByLabelAndUploadId(String labelContains, Integer uploadId) {
        log.info("===== 开始查询jmeter_perfor_history_report表 =====");
        log.info("查询条件: label包含[{}], uploadId=[{}]", labelContains, uploadId);
        
        // 构建查询条件
        List<Criterion> criterions = new ArrayList<>();
        
        // 添加label条件
        Criterion labelCriterion = new Criterion("label LIKE", "%" + labelContains + "%");
        criterions.add(labelCriterion);
        
        // 添加uploadId条件（精确匹配）
        if (uploadId != null && uploadId > 0) {
            Criterion uploadIdCriterion = new Criterion("uploadid =", uploadId);
            criterions.add(uploadIdCriterion);
        }
        
        // 执行查询，使用空的PageBounds对象而不是null
        List<Map<String, Object>> result = jmeter_perfor_history_reportMapper.queryApiExecuteLog4Manage(criterions, new PageBounds());
        
        // 检查查询结果
        if (result == null) {
            log.error("查询结果为null");
        } else {
            log.info("数据库查询结果: {}条数据", result.size());
            
            // 如果有数据，打印第一条记录
            if (result.size() > 0) {
                log.info("第一条记录内容: {}", result.get(0));
            }
        }
        
        log.info("===== 查询结束 =====");
        return result;
    }




    /***
     * 详细列表信息：
     */
  
    public List<String>  getDetailPerfReport(String lastruntime, List<Integer> uploadid){
        log.info("getDetailPerfReport方法被调用 - lastruntime: {}, uploadid: {}", lastruntime, uploadid);
        
        StringBuilder  sb = new StringBuilder(500);

        List<Map<String, Object>> detailReport = this.detailQueryTypePerEmailfReport(lastruntime, uploadid);
        
        log.info("detailReport查询结果: {}条数据, lastruntime888: {}, uploadid888: {}", detailReport.size(), lastruntime, uploadid);
        System.out.println("lastruntime888: " + lastruntime);
        System.out.println("uploadid888: " + uploadid);



      
        

         List<String> list = new ArrayList<>(500);

        // 遍历每个报告项
        for (Map<String, Object> reportMap : detailReport) {
            // 生成表格行
            list.add("<tr>");
            list.add("<td>" + reportMap.get("id") + "</td>");
            list.add("<td>" + reportMap.get("starttime") + "</td>");
            list.add("<td>" + reportMap.get("scriptname") + "</td>");
            list.add("<td>" + reportMap.get("threads") + "</td>");
            list.add("<td>" + reportMap.get("label") + "</td>");
            list.add("<td>" + reportMap.get("samples") + "</td>");
            list.add("<td>" + reportMap.get("ko") + "</td>");
            list.add("<td>" + reportMap.get("error") + "</td>");
            list.add("<td>" + reportMap.get("min") + "</td>");
            list.add("<td>" + reportMap.get("max") + "</td>");
            list.add("<td>" + reportMap.get("median") + "</td>");
            list.add("<td>" + reportMap.get("thpct90") + "</td>");
            list.add("<td>" + reportMap.get("thpct95") + "</td>");
            list.add("<td>" + reportMap.get("thpct99") + "</td>");
            list.add("<td>" + reportMap.get("average") + "</td>");
            list.add("<td>" + reportMap.get("throughput") + "</td>");
            list.add("<td>" + reportMap.get("received") + "</td>");
            list.add("<td>" + reportMap.get("sent") + "</td>");

            // 根据Ko值设置状态样式
            String koValue = String.valueOf(reportMap.get("ko"));
            String statusClass;
            String statusText;
            switch (koValue) {
                case "0.00":
                    statusClass = "status-pass";
                    statusText = "pass";
                    break;
                case "1.00":
                    statusClass = "status-fail";
                    statusText = "fail";
                    break;
                case "2.00":
                    statusClass = "status-warning";
                    statusText = "warning";
                    break;
                default:
                    statusClass = "status-except";
                    statusText = "exception";
            }
            list.add("<td class=\"" + statusClass + "\">" + statusText + "</td>");

            list.add("<td>" + reportMap.get("ip") + "</td>");
            list.add("</tr>");
        }

        return list ;


    }

  



     /**
     * 生成DetailQueryType 报告：
     */

     /** 
    public List<Map<String, Object>> detailQueryTypeReport(String lastruntime, Integer uploadid) {
        System.out.printf("lastruntime---->"+lastruntime);
        log.info("查询Query对象:{}, uploadid: {}", uploadid);
        List<Map<String, Object>>  detailQueryTypeList = interface_current_reportMapper.detailQueryTypeReport(lastruntime, uploadid);
        log.info("数据库查询结结果:------->{}," + detailQueryTypeList.size());
        return detailQueryTypeList;
    }

    **/



     /**
     * 生成DetailQueryType 报告：
     */
    public List<Map<String, Object>> detailQueryTypePerEmailfReport(String lastruntime, List<Integer> uploadid) {
        System.out.println("lastruntime11---->"+lastruntime);
        System.out.println("uploadid11---->"+uploadid);
        log.info("查询Query对象:{}, uploadid: {}", uploadid);
        List<Map<String, Object>>  detailQueryTypeList = jmeter_perfor_current_reportMapper.detailQueryTypePerfReport(lastruntime, uploadid);
        log.info("数据库查询结结果:------->{}," + detailQueryTypeList.size());
        return detailQueryTypeList;
    }



/**
     * 生成Sum汇总报告：
     */
    public List<Map<String, Object>> sumPerfReport(String lastruntime, List<Integer> uploadid) {
       
        log.info("查询Query对象:{}, uploadid: {}", uploadid);
        List<Map<String, Object>>  detailQueryTypeList = jmeter_perfor_current_reportMapper.sumPerfReport(lastruntime, uploadid);
        log.info("数据库查询结结果:------->{}," + detailQueryTypeList.size());
        return detailQueryTypeList;
    }


}