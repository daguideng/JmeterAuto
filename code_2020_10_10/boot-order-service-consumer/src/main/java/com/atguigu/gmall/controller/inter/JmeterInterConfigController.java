package com.atguigu.gmall.controller.inter;

import com.atguigu.gmall.Impl.interfaceImpl.InterConfigModel;
import com.atguigu.gmall.activemq.PublishController;
import com.atguigu.gmall.common.bean.response.Result;
import com.atguigu.gmall.common.constant.RES_STATUS;
import com.atguigu.gmall.zookeeperip.DyIPaddressPubicProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;


/**
 * @Author: dengdagui
 * @Description: 性能脚本上传接口
 * @Date: Created in 2018-7-19
 */

@Slf4j
@RestController
@RequestMapping("/jmeterinter")
//@Api(value = "脚本上传模块", tags = "Mars-性能脚本上传模块", description = "性能脚本上传模块")
@Controller
public class JmeterInterConfigController {




    @Autowired
    private JmeterInterPostParameterImpl jmeterPostParameterIntel;

    @Autowired
    private PublishController publishController;

    @Autowired
    private DyIPaddressPubicProvider dyIPaddressPubicProvider;


    /**
     * 接口基本配置请求
     */
    //@ApiOperation(value = "SYS:PRIVILEGE: 性能基本配置")
    @RequestMapping(method = RequestMethod.POST, value = "/configX")
    public Result <?> list(
            @RequestParam(value = "configxml") String configxml, HttpSession session)
            throws Exception {


        Result <Map <String, Object>> result = new Result <>();

        if (null != configxml || !"".equals(configxml)) {

            Map runResult = jmeterPostParameterIntel.jmeterperslist(configxml, session);
            result.setData(runResult);
        }

        return result;
    }


    //@ApiOperation(value = "SYS:PRIVILEGE: 性能基本配置信息")
    @RequestMapping(value = "/interconfig", method = RequestMethod.POST)
    public @ResponseBody
    Result <?> perfConfigJmeter(@RequestBody InterConfigModel configConfig, HttpSession session, HttpServletResponse response) throws Exception {

        Result <Object> result = new Result <>();
        log.info("get perfConfig param uperfConfig={}", configConfig);



        if ("".equals(configConfig.getVuser()) || "".equals(configConfig.getRuntime())) {

            Map <String, Object> map = new HashMap <>();
            map.put("jtlListener参数修改为true", "false");
            result.setStatus(RES_STATUS.BAD_PARAM_NULL);
            result.setData(map);
            log.info("并发用户与运行时间为空,session保存不成功，退出");
            return result;
        }


        Map runResult = jmeterPostParameterIntel.interConfigParm(configConfig, session,response);
        result.setData(runResult);

        return result;

    }


}
