package com.atguigu.gmall.mock.controller;

import com.atguigu.gmall.common.bean.ResponseMeta;
import com.atguigu.gmall.common.bean.ResponseResult;
import com.atguigu.gmall.common.bean.response.Result;
import com.atguigu.gmall.entity.Mock_config;
import com.atguigu.gmall.mock.common.web.MockConfigQuery;
import com.atguigu.gmall.mock.service.Impl.MockServiceConfigImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dengdagui
 * @ClassName: JmeterPerfUploadInfController
 * @Description: 获取接口测试上传脚本信息:
 * @date 2018年6月8日 下午1:37:51
 */
@Slf4j
@RestController
@RequestMapping("/mock")
//@Api(value = "接口自动化模块", tags = "PERF-性能自动化模块")
public class MockConfigController {



    @Autowired
    MockServiceConfigImpl mockServiceConfigImpl;


    //@ApiOperation(value = "perf：列表展示脚本上传信息")
    @PostMapping(value = "/mockconfig/list")
    //http://localhost:8082/jmeterperf/script/report/list?search=jiao_verify&filter=[{"operator":"ge","property":"uploadtime","value":"2018-08-09 13:44:51"},{"operator":"le","property":"uploadtime","value":"2018-08-09 14:40:39"}]
    public Result <?> list(@RequestParam(required = false) String mock_name,
                           @RequestParam(required = false) String mock_url,
                           @RequestParam(required = false, defaultValue = "[]") String create_time,
                           @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer limit,
                           HttpServletRequest request, HttpServletResponse response)
            throws IOException {


        create_time = URLDecoder.decode(create_time);

     //   log.info("输入的查询条件mock_name:{}", mock_name);
     //   log.info("输入的查询条件mock_url:{}", mock_url);
      //  log.info("输入的查询条件create_time:{}", create_time);
        // 输入数组转为List<String> 支持一次传入多个serviceType;Query也支持list值对比，只要是值对比即可
        MockConfigQuery query = new MockConfigQuery();


        if (null != mock_name || !"".equals(mock_name)) {
            query.setMockName(mock_name);
        }

        if (null != mock_url || !"".equals(mock_url)) {
            query.setMockUrl(mock_url);
        }



        if (null != page && !"".equals(page)) {
            query.setPageNo(page);
        }

        if (null != limit && !"".equals(limit)) {
            query.setPageSize(limit);
        }

        query.setFilter(create_time);

        Result <Map <String, Object>> result = new Result <>();
        result.setData(this.mockServiceConfigImpl.list(query));
        return result;
    }





    @PostMapping(value = "/mockconfig/save")
    @ResponseBody
    public Result <?> insertMockConfig(@RequestBody Mock_config mockConfig , HttpServletRequest request, HttpServletResponse response)
            throws Exception {


     //   log.info("输入的查询条件mockConfig:{}", mockConfig);


        Result <Map <String, Object>> result = new Result <>();

        //保存MockConfig
        String status = mockServiceConfigImpl.saveMockConfigData(mockConfig);


        Map<String,Object>  responseMessage = new HashMap <>() ;
        Map<String,Object>  mapstate = new HashMap <>() ;
        mapstate.put("state", status);

        responseMessage.put("result",new ResponseResult(ResponseMeta.SUCCESS, mapstate));
        System.out.println("result--->");
        log.info("$result{}:",result) ;


        result.setData(responseMessage) ;


        return result;
    }


    /***
     * 对mockconfig进行编辑:
     * @param mockConfig
     * @param request
     * @param response
     * @return
     * @throws Exception
     */

    @PostMapping(value = "/mockconfig/edit")
    @ResponseBody
    public Result <?> editMockConfig(@RequestBody Mock_config mockConfig , HttpServletRequest request, HttpServletResponse response)
            throws Exception {


   //     log.info("输入的查询条件mockConfig:{}", mockConfig);


        Result <Map <String, Object>> result = new Result <>();

        //编辑MockConfig
        String status = mockServiceConfigImpl.editMockConfigData(mockConfig);

        Map<String,Object>  responseMessage = new HashMap <>() ;
        Map<String,Object>  mapstate = new HashMap <>() ;
        mapstate.put("state", status);

        responseMessage.put("result",new ResponseResult(ResponseMeta.SUCCESS, mapstate));
        System.out.println("result--->");
    //    log.info("$result{}:",result) ;


        result.setData(responseMessage) ;


        return result;
    }


    /**
     *  mockconfig启用或禁用
     * @return
     */
    @PostMapping(value = "/mockconfig/dostatus")
    public Result <?> doStatusMockConfig(@RequestParam(required = false) String id , HttpServletRequest request, HttpServletResponse response)throws Exception{


      //  log.info("输入的查询条件mockConfig:{}", id);


        Result <Map <String, Object>> result = new Result <>();

        //编辑MockConfig
        String status = mockServiceConfigImpl.doStatusMockConfigData(id);

        Map<String,Object>  responseMessage = new HashMap <>() ;
        Map<String,Object>  mapstate = new HashMap <>() ;
        mapstate.put("state", status);
        mapstate.put("msg", "操作状态成功!");

        responseMessage.put("result",new ResponseResult(ResponseMeta.SUCCESS,mapstate));
    //    System.out.println("result--->");
    //    log.info("$result{}:",result) ;

        result.setMsg("操作状态成功!");
        result.setData(responseMessage) ;


        return result;
    }

}
