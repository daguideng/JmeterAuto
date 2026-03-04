package com.atguigu.gmall.controller.perf;

import com.atguigu.gmall.common.bean.response.Result;
import com.atguigu.gmall.common.constant.RES_STATUS;
import com.atguigu.gmall.service.jmeterperf.UploadScriptServer;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: dengdagui
 * @Description:界面下载脚本时用：
 * @Date: Created in 2019-12-6
 */
@Slf4j
@RestController
@RequestMapping("/jmeterperf")
//@Api(value = "脚本上传模块", tags = "Mars-性能脚本上传模块", description = "性能脚本上传模块")
public class JmeterPerfScriptDeleControll {
    @Autowired
    private UploadScriptServer uploadScriptServer;


    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    Result<?> scriptDelete(@RequestParam(required = true ) String id, HttpSession session, HttpServletResponse response) throws Exception {

        Result <Object> result = new Result <>();
        log.info("删除脚本时id{}", id);



        if ("".equals(id) || null==id) {

            Map<String, Object> map = new HashMap<>();
            map.put("msg", "id为空");
            result.setStatus(RES_STATUS.BAD_PARAM_NULL);
            result.setData(map);
            return result;
        }


        //1.查出这个脚本下载url,String urlStr, String fileName, String savePath
        Map runResult = uploadScriptServer.deleteScriptByKey(Integer.valueOf(id));
        result.setData(runResult);

        return result;

   }



}
