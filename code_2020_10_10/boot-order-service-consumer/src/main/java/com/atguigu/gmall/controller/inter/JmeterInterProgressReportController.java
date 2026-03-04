package com.atguigu.gmall.controller.inter;


import com.atguigu.gmall.Impl.interfaceImpl.JmeterScriptInterlUploadImpl;
import com.atguigu.gmall.Interface.SendEmailReportIntel;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author: dengdagui
 * @Description: 随时查询接口进度报告
 * @Date: Created in 2018-7-19
 */

@Slf4j
@RestController
@RequestMapping("/jmeterinter")
//@Api(value = "脚本上传模块", tags = "Mars-接口脚本上传模块", description = "接口脚本上传模块")

public class JmeterInterProgressReportController {



    @Resource(name = "sendEmailInterReportImpl")
    private SendEmailReportIntel sendEmailInterReportImpl;


    @Resource(name = "jmeterScriptInterlUploadImpl")
    private JmeterScriptInterlUploadImpl jmeterScriptInterlUploadImpl;



    /**
     * 接口测试报告,随时查询:
     * state =0 是查询当前报告进度
     * state= 1 是查询以前报告信息
     */
    //@ApiOperation(value = "报告查询模块", notes = "报告查询模块")
    @RequestMapping(value = "/selectProgressReport", method = RequestMethod.POST,produces ="text/html;charset=UTF-8")
    @ResponseBody
    public String list(@RequestParam(value = "state", required = true) Integer state,
                       HttpServletResponse response, HttpSession session) throws Exception {

        StringBuffer  sbf = new StringBuffer(2000);

        sendEmailInterReportImpl.selectProgressReport(state,sbf);

        return sbf.toString();
    }


}
