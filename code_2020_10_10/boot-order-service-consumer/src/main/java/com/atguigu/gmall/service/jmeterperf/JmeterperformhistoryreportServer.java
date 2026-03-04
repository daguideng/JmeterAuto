package com.atguigu.gmall.service.jmeterperf;

import com.atguigu.gmall.common.page.Criterion;
import com.atguigu.gmall.common.web.PerfHistoryReortQuery;
import com.atguigu.gmall.dao.Jmeter_perfor_history_reportMapper;
import com.atguigu.gmall.entity.Jmeter_perfor_history_report;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2018-8-7
 */
@Slf4j
@Service
public class JmeterperformhistoryreportServer {



    @Autowired
    private Jmeter_perfor_history_reportMapper  jmeter_perfor_history_reportMapper ;

    public void insertHistoryReport(Jmeter_perfor_history_report record) throws Exception{

        int count =  jmeter_perfor_history_reportMapper.insert(record);

        if(count > 0) {
            log.info("性能历史报告数据入库成功！");

        }else{
            log.info("性能历史报告数据入库失败！");
        }

    }


    /**
     * 性能测试不同状态随时更新数据库的配置状态
     */
    public void updateLastRunTime(String lastruntime,int id ) {


        int result = jmeter_perfor_history_reportMapper.updatePerfLastRunTime(lastruntime, id);


        if (result > 0) {
            log.info("更新lastrunTime状态 " + result + " 成功!");
        } else {
            log.info("更新lastrunTime状态 " + result + " 失败!");
        }

    }



    /**
     * 根据query分页查询出Per的性能结果列表
     */
    public Map<String, Object> list(PerfHistoryReortQuery query) {
        PageBounds pb = query.getPB();
        List<Criterion> criterions = query.criterion();
        log.info("查询Query对象:{}");
        PageList<Map<String, Object>> apiExecuteLogList = jmeter_perfor_history_reportMapper.queryApiExecuteLog4Manage(criterions, pb);
        log.info("数据库查询结结果:{}," +apiExecuteLogList.size());




        Map<String, Object> mesg = new HashMap<>();
        mesg.put("totalCount", apiExecuteLogList.getPaginator().getTotalCount());
        mesg.put("totalPages", apiExecuteLogList.getPaginator().getTotalPages());
        mesg.put("page", apiExecuteLogList.getPaginator().getPage());
        mesg.put("list", apiExecuteLogList);
        return mesg;
    }




    public List<Map<String, Object>> linkTypeReport(String id, String scriptname) {
        log.info("===== 开始执行linkTypeReport查询 =====");
        log.info("查询参数 - id: {}, scriptname: {}", id, scriptname);
        
        // 将String类型的id转换为Integer
        Integer idInt = null;
        try {
            idInt = Integer.parseInt(id);
            log.info("id转换成功: {} -> {}", id, idInt);
        } catch (NumberFormatException e) {
            log.error("id格式转换错误: {}", id, e);
            return new ArrayList<>(); // 返回空列表避免空指针异常
        }
        
        // 调用支持id和scriptname参数的mapper方法
        log.info("准备调用mapper方法: queryApiExecuteLog4ManageByIdAndScriptname");
        List<Map<String, Object>> result = jmeter_perfor_history_reportMapper.queryApiExecuteLog4ManageByIdAndScriptname(idInt, scriptname);
        
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
                log.warn("1. 表中没有匹配id=[{}]和scriptname=[{}]的数据", idInt, scriptname);
                log.warn("2. 参数格式不正确");
                log.warn("3. 数据库查询条件有问题");
            }
        }
        
        log.info("===== linkTypeReport查询结束 =====");
        return result;
    }



    /**
     * 根据状态统计数量
     */
    public int countByStatus(String id, String scriptname, String status) {
        log.info("统计jmeter_perfor_history_report表中状态为{}的数据数量", status);
        
        // 将String类型的id转换为Integer
        Integer idInt = null;
        try {
            idInt = Integer.parseInt(id);
            log.info("id转换成功: {} -> {}", id, idInt);
        } catch (NumberFormatException e) {
            log.error("id格式转换错误: {}", id, e);
            return 0; // 返回0避免空指针异常
        }
        
        // 调用mapper方法获取数据
        List<Map<String, Object>> result = jmeter_perfor_history_reportMapper.queryApiExecuteLog4ManageByIdAndScriptname(idInt, scriptname);
        int count = 0;
        if (result != null) {
            for (Map<String, Object> item : result) {
                if (status.equals(String.valueOf(item.get("ko")))) {
                    count++;
                }
            }
        }
        
        return count;
    }

}
