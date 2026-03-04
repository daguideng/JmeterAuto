package com.atguigu.gmall.service.jmeterinter;

import com.atguigu.gmall.common.page.Criterion;
import com.atguigu.gmall.common.web.InterHistoryReortQuery;
import com.atguigu.gmall.dao.Interface_history_reportMapper;
import com.atguigu.gmall.entity.Interface_history_report;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2018-8-7
 */
@Slf4j
@Component
public class JmeterInterormhistoryreportServer {



    @Autowired
    private Interface_history_reportMapper interface_history_reportMapper ;

    public void jmeterIntermhistoryreportMapper(Interface_history_report record) throws Exception{

        int count =  interface_history_reportMapper.insert(record);

        if(count > 0) {
            log.info("性能历史报告数据入库成功！");

        }else{
            log.info("性能历史报告数据入库失败！");
        }

    }



    /**
     * 根据query分页查询出Per的性能结果列表
     */
    public Map<String, Object> list(InterHistoryReortQuery query) {
        PageBounds pb = query.getPB();
        List<Criterion> criterions = query.criterion();
    //    log.info("查询Query对象:{}");
        PageList<Map<String, Object>> apiExecuteLogList = interface_history_reportMapper.queryApiExecuteLog4Manage(criterions, pb);
    //    log.info("数据库查询结结果:{}," +apiExecuteLogList.size());

        Map<String, Object> m = new HashMap<>();
        m.put("totalCount", apiExecuteLogList.getPaginator().getTotalCount());
        m.put("totalPages", apiExecuteLogList.getPaginator().getTotalPages());
        m.put("page", apiExecuteLogList.getPaginator().getPage());
        m.put("list", apiExecuteLogList);
        System.out.println();
        return m;
    }




}
