package com.atguigu.gmall.Impl.perfImpl;


import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall.activemq.PublishController;
import com.atguigu.gmall.activemq.ResultDataMessageProducer;
import com.atguigu.gmall.activemq.ThreadByRunnableSendMq;
import com.atguigu.gmall.common.bean.ResponseMeta;
import com.atguigu.gmall.common.bean.ResponseResult;
import com.atguigu.gmall.service.jmeterperf.StatisticsServer;
import com.atguigu.gmall.service.jmeterperf.UploadScriptServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
@Component
public class RunScriptController {

    @Autowired
    private UploadScriptServer uploadScriptServer;

    //  @Autowired


    @Autowired
    private JmeterPerfMain jmeterPerfMain;

    @Autowired
    private ResultDataMessageProducer threadDataMessageProducer;

    @Autowired
    private StatisticsServer statisticsServer;

    @Autowired
    private PublishController publishController;


    @Value("${resultReportDir}")
    private String resultReportDir;





    @Value("${jmeterProperties.Perf}")
    private String jmeterPropertiesx;


    /**
     * @param session
     * @param ids     前端传每条脚本的Id号，根据id号对所有脚本进行排序并进行性能测试
     * @return
     */
    public Map<String, Object> getRunmanyScript(HttpSession session, String[] ids) throws Exception {


        String sessionName = (String) session.getAttribute("threadname");

        if (null == sessionName) {
            Map<String, Object> map = new HashMap<>();
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("session", "null");
            map.put("请先提交jmeter性能测试配置信息", jsonObj);
            return map;
        }

        log.info("sessionName=" + sessionName);

        Map<String, Object> modelMap = new HashMap<String, Object>();


        Map<Integer, String> mapsort = new TreeMap<Integer, String>(
                new Comparator<Integer>() {
                    public int compare(Integer obj1, Integer obj2) {
                        // 降序排序
                        return obj1.compareTo(obj2);
                    }
                });


        String[] jsonArr = ids;

        //运行脚本时，检查jmeter代理服务是否已启动，如果没有启动则自动启动jmeter
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("runJmeterAgentCheck", "runJmeterAgentCheck");
        jsonObj.toJSONString();
        ThreadByRunnableSendMq thread = new ThreadByRunnableSendMq(publishController, jsonObj);
        new Thread(thread, "发送:检查jmeter代理服务器检查消息......").start();

        //给jmeter服务器启动时间：
        //   Thread.sleep(5000);

        System.out.println("jsonArr.length===" + jsonArr.length);
        //根据此变量决定详细结果的类型:

        for (int t = 0; t < jsonArr.length; t++) {

            int id = Integer.valueOf(jsonArr[t]);

            Map<String, Object> mapkey = uploadScriptServer.getByPrimaryKeyServer(id);

            System.out.println("mapkey=" + mapkey);
            log.info("执行的脚本为：" + mapkey);

            String scriptrunorder = (String) mapkey.get("scriptrunorder");

            String scriptpath = (String) mapkey.get("scriptpath");

            String username = (String) mapkey.get("username");

            String scriptname = (String) mapkey.get("scriptname");


            if (scriptrunorder.equals("") || scriptrunorder == null) {
                scriptrunorder = String.valueOf(999 + t);
            }

            mapsort.put(Integer.valueOf(scriptrunorder), scriptpath);
        }


        /**
         * 根据请求对jmeter的控制台进行分发：对控制台支持多用户并发
         */
        String jmeterControlNode = this.getRunJmeterNode();

        // 1.查库 得到 scriptrunorder, scriptpath 的排序

        //统计脚本次数,为生成报告做准备：
        int scriptCount = 0;

        //更新数据库状态为：0---->1  由先发过邮件的变成，不能发邮件的状态:
        statisticsServer.UpdateStatus_0_1(statisticsStates.INSERT_0);


        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");  //yyyyMMddHHmmss
        String todayTime = df.format(new Date()) + "/";
        resultReportDir = resultReportDir.replace("\\", "/") + todayTime;
        //生成报告目录：
        resultReportDir = resultReportDir.replace("\\", "/");

        //生成报告html文件名：
        String reportHtmlName = df.format(new Date()) + ".html";

        int scriptSumSize = mapsort.size();
        Set<Integer> keySet = mapsort.keySet();
        Iterator<Integer> iter = keySet.iterator();
        while (iter.hasNext()) {
            //统计脚本次数,为生成报告做准备：
            scriptCount++;
            Integer key = iter.next();
            System.out.println(key + ":" + mapsort.get(key));

            // 1.先把脚本文件进行复制并重命名:
            String scriptJmeterZip = mapsort.get(key);
            System.out.println("scriptJmeterZip=" + scriptJmeterZip);

            //得到运行脚本名称：
            File file = new File(scriptJmeterZip);

            String fileName = file.getName();
            String jmeterScriptName = fileName.substring(0, fileName.length() - 4);

            log.info("scriptJmeterZip=" + scriptJmeterZip);

            try {
                jmeterPerfMain.jmeterPerMainEnter(jmeterControlNode, scriptJmeterZip, jmeterScriptName, scriptCount, scriptSumSize, resultReportDir, reportHtmlName, session);
            } catch (Exception e) {
                modelMap.put("result", new ResponseResult(ResponseMeta.NODE_NO_RUN_ERROR, "节点未运行收集报告失败"));
                return modelMap;
            }


        }


        modelMap.put("result", new ResponseResult(ResponseMeta.SUCCESS, "提交成功"));
        return modelMap;


    }


    //不是总流程的一部分：获取运行的用那个jmeter节点运行
    public String getRunJmeterNode() throws UnsupportedEncodingException {

        String jmeterProperties[] = jmeterPropertiesx.replace("\\", "/").split(",");

        String jmeterRunNode = null;

        for (int x = 0; x < jmeterProperties.length; x++) {

            if (x <= jmeterProperties.length - 1) {
                jmeterRunNode = jmeterProperties[x];
            } else {
                getRunJmeterNode();
            }
        }

            /**
             List getlastNode = JmeterNode.getRunJmeterNode(jmeterProperties.toString());
             System.out.println("getlastNode.size=" + getlastNode.size());

             String jmeterRunNode = new String(getlastNode.get(getlastNode.size() - 1).toString().getBytes(), "utf-8");

             System.out.println("jmeterRunNode="+jmeterRunNode) ;
             **/
            return jmeterRunNode;
        }


    }
