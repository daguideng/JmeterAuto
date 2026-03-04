package com.atguigu.gmall.Interface;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2018-9-27
 */
public interface JmeterProcessIntel{
    //1.解压相应目录
    public void unzipScript(String scriptJmeterZip) throws Exception ;

    //2.相应依赖文件拷入相应目录
    public String resolvDependencies(String jmeterControlNode, String jmeterScriptName) throws UnsupportedEncodingException;

    //3.根据配置修改相应jmeter脚本的各参数: 主要是threads数与运行时间,及场景延迟时间（秒）
    public void modiyJmeterParameter(String scriptPath, String threads, String runTime, String sleepTime, String delaytime);

    //4.根据配置修改控制台输出间隔:
    public void modiyJmeterOtherParameter(String jmeterControlNode,JSONObject json,HttpSession session);

    //4.5控制台时间同步：
    public void updateTimeControll() throws Exception;

    //4.运行性能测试
    public void commandRunJmeter(String jmeterControlNode, String scriptNamePath, String jmeterScriptName, int scriptCount, int scriptSumSize, String resultReportDir, String reportHtmlName, Integer runId, String currentTime, JSONObject json, HttpSession session) throws Exception ;

    //5.发email报告:1
   // public void sendEmailReport(String toemalAdress, String subject) throws Exception ;

    //6.发email报告:2
    public void sendEmailReportSingTyle(String toemalAdress, String subject) throws Exception;

    //7.处理jmeterNode
    public void removeThreadJmeterNode(String jmeterControlNode) ;


}
