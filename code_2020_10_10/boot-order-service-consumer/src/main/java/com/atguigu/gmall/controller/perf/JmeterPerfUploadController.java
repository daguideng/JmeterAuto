package com.atguigu.gmall.controller.perf;

import com.atguigu.gmall.Interface.JmeterScriptUploadIntel;
import com.atguigu.gmall.common.bean.response.Result;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


/**
 *
 * @Author: dengdagui
 * @Description: 性能脚本上传接口
 * @Date: Created in 2018-7-19
 */

@Slf4j
@RestController
@RequestMapping("/jmeterperf")
//@Api(value = "脚本上传模块", tags = "Mars-性能脚本上传模块", description = "性能脚本上传模块")
public class JmeterPerfUploadController {


    @Resource(name = "jmeterScriptPerfUploadImpl")
    private JmeterScriptUploadIntel jmeterScriptPerfUploadImpl;



    /**
     * 性能测试脚本上传
     */
    //@ApiOperation(value = "性能脚本上传模块", notes = "脚本上传模块")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Result <?> list(@RequestParam(value = "file", required = false) MultipartFile file,
            HttpServletResponse response, HttpSession session)
            throws Exception {


        Result <Map <String, Object>> result = new Result <>();

        if (null != file || !"".equals(file)) {
            System.out.println("file--->"+file);
            Map runResult =  jmeterScriptPerfUploadImpl.uploadScript(file,response,session);
            result.setData(runResult);
        }

        return result;
    }


}
