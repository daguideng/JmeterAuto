package com.atguigu.gmall.service.jmeterperf;

import com.atguigu.gmall.common.page.Criterion;
import com.atguigu.gmall.common.web.PerfUploadInfoQuery;
import com.atguigu.gmall.dao.Upload_infoMapper;
import com.atguigu.gmall.entity.Upload_info;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
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
public class UploadInfoServer {


    @Autowired
    private Upload_infoMapper upload_infoMapper ;


        /**
         * 根据query分页查询出Per的性能结果列表
         */
        public Map<String, Object> list(PerfUploadInfoQuery query) {
            PageBounds pb = query.getPB();

            List<Criterion> criterions = query.criterion();
          //  log.info("查询Query对象:{}");
            PageList<Map<String, Object>> apiExecuteLogList = upload_infoMapper.queryApiExecuteLog4Manage(criterions, pb);

        //    log.info("数据库查询结结果:{}," +apiExecuteLogList.size());

            List<Upload_info> sumcount = upload_infoMapper.queryApiExecuteSumCount(criterions);
            int totalCount = sumcount.size();


            /*
            总记录数：totalRecord
            每页最大记录数：maxResult
            总页数：totalPage
            totalPage = (totalRecord + maxResult -1) / maxResult;
            其中 maxResult  - 1 就是 totalRecord / maxResult 的最大的余数
            */

            int page = pb.getPage() ;
            int totalPages = (totalCount + page -1)/ page ;


            Map<String, Object> m = new HashMap<>();
     //     m.put("totalCount", apiExecuteLogList.getPaginator().getTotalCount());
            m.put("totalCount", totalCount);
 //          m.put("totalPages", apiExecuteLogList.getPaginator().getTotalPages());
            m.put("totalPages", totalPages);
 //         m.put("page", apiExecuteLogList.getPaginator().getPage());
            m.put("page", page);
            m.put("list", apiExecuteLogList);
            return m;
        }








    }


