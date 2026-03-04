package com.atguigu.gmall.Impl.perfImpl;


import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall.Interface.RunScriptControllerIntel;
import com.atguigu.gmall.activemq.PublishController;
import com.atguigu.gmall.activemq.ResultDataMessageProducer;
import com.atguigu.gmall.activemq.ThreadByRunnableSendMq;
import com.atguigu.gmall.common.bean.ResponseMeta;
import com.atguigu.gmall.common.bean.ResponseResult;
import com.atguigu.gmall.service.jmeterperf.PerfConfigServer;
import com.atguigu.gmall.service.jmeterperf.StatisticsServer;
import com.atguigu.gmall.service.jmeterperf.UploadScriptServer;
import com.atguigu.gmall.zookeeperip.DyIPaddressPubicProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 性能脚本支持多个脚本分别执行不同性能测试的批量性能测试功能
 *
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2018-7-19
 */
@Slf4j
@Component("runScriptPerfControllerImpl")
public class RunScriptPerfControllerImpl implements RunScriptControllerIntel {


    @Autowired
    private JmeterPerfMainImpl jmeterPerfMainImpl;

    @Autowired
    private ResultDataMessageProducer threadDataMessageProducer;

    @Autowired
    private StatisticsServer statisticsServer;

    @Autowired
    private PerfConfigServer perfConfigServer;

    @Autowired
    private UploadScriptServer uploadScriptServer;


    @Autowired
    private PublishController publishController;


    @Value("${resultReportDir}")
    private String resultReportDir ;


    @Value("${jmeterProperties.Perf}")
    private String jmeterProperties_Perf ;



    @Autowired
    private DyIPaddressPubicProvider dyIPaddressPubicProvider;


    /**
     * @param session
     * @param ids  前端传每条脚本的Id号，根据id号对所有脚本进行排序并进行性能测试
     * @return
     */

    String sessionName = null;

    public Map <String, Object> getRunmanyScript(HttpSession session, String[] ids, JSONObject json, HttpServletRequest request) throws Exception {


        //如果是定时器： 则创建session
        if (!JmeterSession.SESSION_TIMER_Mid.equals(JmeterSession.SESSION_TIMER_End)) {
            session = request.getSession(true);
            log.info("session--->" + session);
            session.setAttribute("username", "timeruserPerf");
            session.setAttribute(JmeterSession.SESSION_PERF, JmeterSession.SESSION_TIMER_End);
        }


        sessionName = (String) session.getAttribute(JmeterSession.SESSION_PERF);

        SortedMap<Integer, String> scriptNameMap = new TreeMap<>();

        log.info("sessionName=" + sessionName);

        Map <String, Object> modelMap = new HashMap<String, Object>();

        //更新脚本运行状态：
        Map<String, Integer> scriptId = new HashMap <>();


        String[] jsonArr = ids;

        log.info("jsonArr-->" + jsonArr.toString());

        //得到排序后的脚本路径：
        Map <Integer, String> mapsort = new TreeMap <Integer, String>(
                new Comparator<Integer>() {
                    public int compare(Integer obj1, Integer obj2) {
                        // 降序排序
                        return obj1.compareTo(obj2);
                    }
                });


        //得到排序后的运行id:
        Map <Integer, Integer> mapsortId = new TreeMap <Integer, Integer>(
                new Comparator <Integer>() {
                    public int compare(Integer obj1, Integer obj2) {
                        // 降序排序
                        return obj1.compareTo(obj2);
                    }
                }
        );


        String sessionUserName = null ;
        System.err.println("session username----->"+ session.getAttribute("username"))   ;
        //vue 有session，但后台更新重启了，session前台还是原来的，但后台没有获取到,以免agent受到影响：
        if(null ==  session.getAttribute("username")){
            sessionUserName = " ";
        }else{
            sessionUserName = (String) session.getAttribute("username");
        }

        //运行脚本时，检查jmeter代理服务是否已启动，如果没有启动则自动启动jmeter
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("runJmeterAgentCheck", "runJmeterAgentCheck");
        jsonObj.put("consumerIp", dyIPaddressPubicProvider.getConsumerOsIpaddress());
        jsonObj.put("runusername",sessionUserName);
        jsonObj.toJSONString();
        ThreadByRunnableSendMq thread = new ThreadByRunnableSendMq(publishController, jsonObj);
        new Thread(thread, "发送:检查jmeter代理服务器检查消息......").start();


        //根据此变量决定详细结果的类型:

        for (int t = 0; t < jsonArr.length; t++) {

            int id = Integer.valueOf(jsonArr[t]);

            Map <String, Object> mapkey = uploadScriptServer.getByPrimaryKeyServer(id);

            log.info("执行的脚本为：" + mapkey);

            String scriptrunorder = (String) mapkey.get("scriptrunorder");

            String scriptpath = (String) mapkey.get("scriptpath");


            if (scriptrunorder.equals("") || scriptrunorder == null || Integer.valueOf("100") == 100) {
                scriptrunorder = String.valueOf(100 + t);
            }

            mapsort.put(Integer.valueOf(scriptrunorder), scriptpath);
            mapsortId.put(Integer.valueOf(scriptrunorder), id);
        }


        //统计脚本次数,为生成报告做准备：
        int scriptCount = 0;

        //更新数据库状态为：0---->1  由先发过邮件的变成，不能发邮件的状态:
        statisticsServer.UpdateStatus_0_1(ReportStates.INSERT_0);


        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");  //yyyyMMddHHmmss
        String todayTime = df.format(new Date()) + "/";
        resultReportDir = resultReportDir.replace("\\", "/") + todayTime;
        //生成报告目录：
        resultReportDir = resultReportDir.replace("\\", "/");

        //生成报告html文件名：
        String reportHtmlName = df.format(new Date()) + ".html";


        /**
         * 根据请求对jmeter的控制台进行分发：对控制台支持多用户并发(现在性能测试只做单线程压测)
         */
        String jmeterControlNode = this.getRunJmeterNode();


        int scriptSumSize = mapsort.size();
        Set <Integer> keySet = mapsort.keySet();
        Iterator <Integer> iter = keySet.iterator();

        while (iter.hasNext()) {
            //统计脚本次数,为生成报告做准备：
            scriptCount++;
            Integer key = iter.next();
            log.info(key + ":" + mapsort.get(key));

            // 1.先把脚本文件进行复制并重命名:
            String scriptJmeterZip = mapsort.get(key);

            //得到运行脚本名称：
            File file = new File(scriptJmeterZip);

            String fileName = file.getName();
            String jmeterScriptName = fileName.substring(0, fileName.length() - 4);

            log.info("scriptJmeterZip=" + scriptJmeterZip);

            scriptNameMap.put(scriptCount, jmeterScriptName);


            //得到运行脚本id：
            int runId = mapsortId.get(key);

            //更新性能测试的运行状态：
            uploadScriptServer.updatePerfRunStates(JmeterPerRunStatus.Perf_Run, runId, JmeterPerRunStatus.Perf_Run, "");


            try {
                //开始运行，进入运行主类：
                jmeterPerfMainImpl.jmeterMainEnter(jmeterControlNode, scriptJmeterZip, jmeterScriptName, scriptCount, scriptSumSize, resultReportDir, reportHtmlName, scriptNameMap, runId,json,session);
            } catch (Exception e) {
                modelMap.put("result", new ResponseResult(ResponseMeta.NODE_NO_RUN_ERROR, "节点未运行收集报告失败"));
                return modelMap;
            }


        }


        modelMap.put("result", new ResponseResult(ResponseMeta.SUCCESS, "提交成功"));
        return modelMap;


    }


    //不是总流程的一部分：获取运行的用那个jmeter节点运行

    /**
    public String getRunJmeterNode() throws UnsupportedEncodingException, InterruptedException {

        List getlastNode = JmeterNode.getRunJmeterNode(jmeterProperties.toString());

        String jmeterRunNode = new String(getlastNode.get(getlastNode.size() - 1).toString().getBytes(), "utf-8");


        //更新性能测试的运行状态：
        perfConfigServer.updateRunStates(JmeterPerRunStatus.Perf_Run, sessionName);


        //给jmeter服务器启动时间：
        // Thread.sleep(1000);

        return jmeterRunNode;
    }

     ***/


     //不是总流程的一部分：获取运行的用那个jmeter节点运行
     public String getRunJmeterNode() throws UnsupportedEncodingException {

     String[] jmeterProperties = jmeterProperties_Perf.replace("\\", "/").split(",");

     String jmeterRunNode = null;

     //        for (threadControlNumber = 0; threadControlNumber < jmeterProperties.length; threadControlNumber++) {
     //
     //            if (threadControlNumber <= jmeterProperties.length - 1) {
     jmeterRunNode = jmeterProperties[0];

     //            } else {
     //                //如果大于按控制台的并数则又从0开始
     //                threadControlNumber = 0;
     //                this.getRunJmeterNode();
     //            }
     //        }


     /**
     List getlastNode = JmeterNode.getRunJmeterNode(jmeterProperties.toString());

     System.out.println("getlastNode.size=" + getlastNode.size());

     String jmeterRunNode = new String(getlastNode.get(getlastNode.size() - 1).toString().getBytes(), "utf-8");

     System.out.println("jmeterRunNode=" + jmeterRunNode);
     return jmeterRunNode;

     **/

         //更新性能测试的运行状态：
         perfConfigServer.updateRunStates(JmeterPerRunStatus.Perf_Run, sessionName);


         //给jmeter服务器启动时间：
         //  Thread.sleep(1000);

        return jmeterRunNode;
}


}
