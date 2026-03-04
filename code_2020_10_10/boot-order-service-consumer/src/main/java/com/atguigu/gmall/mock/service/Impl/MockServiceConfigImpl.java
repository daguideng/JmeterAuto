package com.atguigu.gmall.mock.service.Impl;


import cn.hutool.core.convert.Convert;
import com.atguigu.gmall.common.page.Criterion;
import com.atguigu.gmall.dao.Mock_configMapper;
import com.atguigu.gmall.entity.Mock_config;
import com.atguigu.gmall.mock.common.web.MockConfigQuery;
import com.atguigu.gmall.mock.service.MockServiceConfig;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class MockServiceConfigImpl implements MockServiceConfig {

    @Autowired
    private Mock_configMapper mock_configMapper ;


    /**
     * 根据query分页查询出Per的性能结果列表
     */
    @Override
    public Map<String, Object> list(MockConfigQuery query) {

        PageBounds pb = query.getPB();
        List<Criterion> criterions = query.criterion();

        log.info("查询Query对象:{}");
        PageList<Map<String, Object>> apiExecuteLogList = mock_configMapper.queryApiExecuteLog4Manage(criterions, pb);

        log.info("数据库查询结结果:{}," +apiExecuteLogList.size());

        List<Mock_config> sumcount = mock_configMapper.queryApiExecuteSumCount(criterions);
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


    /***
     * 保存mockConfig
     */
    @Override
    public String saveMockConfigData(Mock_config  mockconfig){
        String result = null ;
        Mock_config mock_config = Convert.convert(Mock_config.class, mockconfig);

        String mockResult = mock_config.getMockResult().trim();
        mockResult = mockResult.replace(" ","");
        mockconfig.setMockResult(mockResult);
        String weight = mock_config.getWeight();
        String timeout = mock_config.getTimeout();
        String tag = mock_config.getTag();
        //如果前端统一为："",则给优先级一个默认值
        if("".equals(weight)){
            mock_config.setWeight("1");
        }
        if("".equals(timeout)){
            mock_config.setTimeout("0");
        }

        if(null == tag){
            mock_config.setTag("0");
        }

        int count = mock_configMapper.insertSelective(mock_config);

        if(count > 0){
            result = "sucess";
        }else{
            result = "false";
        }

        log.info("mockConfig保存数据结果:{}",result);

        return result ;
    }




    /***
            * 编辑mockConfig
     */
    @Override
    public String editMockConfigData(Mock_config  mockconfig){
        String result = null ;
        Mock_config mock_config = Convert.convert(Mock_config.class, mockconfig);

        String weight = mock_config.getWeight();
        String timeout = mock_config.getTimeout();
        String tag = mock_config.getTag();
        //如果前端统一为："",则给优先级一个默认值
        if("".equals(weight)){
            mock_config.setWeight("1");
        }
        if("".equals(timeout)){
            mock_config.setTimeout("0");
        }

        if(null == tag){
            mock_config.setTag("0");
        }

        int count = mock_configMapper.updateByPrimaryKey(mock_config);

        if(count > 0){
            result = "sucess";
        }else{
            result = "false";
        }

        log.info("mockConfig编辑数据结果:{}",result);

        return result ;
    }


    /****
     * 启用或禁用mockConfig的状态
     */
    @Override
    public String doStatusMockConfigData(String  id){
        String result = null ;

        int ids = Integer.valueOf(id);
        //1.查数据库状态:
        Mock_config row = mock_configMapper.selectByPrimaryKey(Integer.valueOf(id));

        log.info("查询数据库内容row：{}",row);
        String stauts = row.getTag();

        int count = 0 ;
        if("0".equals(stauts)){
            //2.如果是0则变成1
            count = mock_configMapper.updateStatusById("1",ids);

        }else{
            //2.如果1则变成0
            count = mock_configMapper.updateStatusById("0",ids);
        }


        if(count > 0){
            result = "sucess";
        }else{
            result = "false";
        }



        log.info("mockConfig编辑数据结果:{}",result);

        return result ;
    }


    /**
     * 根据拦截器、过滤器得到的url到数据库中检索：
     */

    @Override
    public List<Mock_config>  interceptorList(String url){


      //  Mock_config  interceptorList = null ;

         // if(map.size() == 0){
              //1.当无参数时调用：
         List<Mock_config>  interceptorList = mock_configMapper.selectByUrl(url);
      //    }else{
              //2.当有参数时调用：
             //  interceptorList = mock_configMapper
     //     }






        return interceptorList ;
    }



}
