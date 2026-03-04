package com.atguigu.gmall.controller.inter;


import com.atguigu.gmall.Impl.interfaceImpl.JmeterScriptInterlUploadImpl;
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
@RequestMapping("/jmeterinter")
//@Api(value = "脚本上传模块", tags = "Mars-接口脚本上传模块", description = "接口脚本上传模块")

public class JmeterInterUploadController {



    @Resource(name = "jmeterScriptInterlUploadImpl")
    private JmeterScriptInterlUploadImpl jmeterScriptInterlUploadImpl;




    /**
     * 接口测试脚本上传
     */
    //@ApiOperation(value = "性能脚本上传模块", notes = "脚本上传模块")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> list(@RequestParam(value = "file", required = false) MultipartFile file,
                          HttpServletResponse response, HttpSession session)
            throws Exception {

        Result <Map <String, Object>> result = new Result <>();

        if (null != file || !"".equals(file)) {
            Map runResult =  jmeterScriptInterlUploadImpl.uploadScript(file,response,session);
            result.setData(runResult);
        }

        return result;
    }


}
