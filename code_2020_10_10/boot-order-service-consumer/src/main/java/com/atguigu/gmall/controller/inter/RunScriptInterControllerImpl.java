package com.atguigu.gmall.controller.inter;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall.Impl.interfaceImpl.JmeterInterMainImpl;
import com.atguigu.gmall.Impl.perfImpl.JmeterPerRunStatus;
import com.atguigu.gmall.Impl.perfImpl.JmeterSession;
import com.atguigu.gmall.Impl.perfImpl.ReportStates;
import com.atguigu.gmall.Interface.RunScriptControllerIntel;
import com.atguigu.gmall.activemq.PublishController;
import com.atguigu.gmall.activemq.ResultDataMessageProducer;
import com.atguigu.gmall.activemq.ThreadByRunnableSendMq;
import com.atguigu.gmall.common.bean.ResponseMeta;
import com.atguigu.gmall.common.bean.ResponseResult;
import com.atguigu.gmall.dao.Jmeter_agentip_statesMapper;
import com.atguigu.gmall.entity.Jmeter_agentip_states;
import com.atguigu.gmall.service.jmeterinter.JmeterIntercurrentreportServer;
import com.atguigu.gmall.service.jmeterperf.UploadScriptServer;
import com.atguigu.gmall.zookeeperip.DyIPaddressPubicProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2018-9-26
 */
@Slf4j
@Component
@Controller
@RequestMapping("/jmeterinter")
//@Api(value = "Jmeter自动化模块", tags = "PERF-Jmeter自动化模块" ,description = "Jmeter运行模块")
public class RunScriptInterControllerImpl implements RunScriptControllerIntel {

    @Autowired
    private UploadScriptServer uploadScriptServer;

    @Autowired
    private JmeterInterMainImpl jmeterInterMainImpl;

    @Autowired
    private ResultDataMessageProducer threadDataMessageProducer;

    @Autowired
    private JmeterIntercurrentreportServer jmeterIntercurrentreportServer;


    @Autowired
    private PublishController publishController;


    @Autowired
    private DyIPaddressPubicProvider dyIPaddressPubicProvider;

    @Autowired
    private Jmeter_agentip_statesMapper consumer_jmeter_agentip_statesMapper;


    @Value("${jmeterProperties.Inter}")
    private String jmeterProperties_Inter  ;

    @Value("${resultReportDir.Inter}")
    private String resultReportDir_Inter ;


    //  private HttpServletRequest request  ;

    // 控制接口同时运行多少线程:
    //public static Integer threadControlNumber = 0;


    //public static String[] jmeterProperties = null ;


    RunScriptInterControllerImpl() {
        super();
    }


    /**
     * @param session
     * @param ids  前端传每条脚本的Id号，根据id号对所有脚本进行排序并进行性能测试
     * @return
     */

    SortedMap<Integer, String> scriptNameMap = new TreeMap<>();

    //@ApiOperation(value = "inter：测试运行接口")
    @RequestMapping(value = "/runX", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getRunmanyScript(HttpSession session, String[] ids, JSONObject json, HttpServletRequest request) throws Exception {


        System.err.println("runX------->");

        Map<String, Object> map = new HashMap<>();
        JSONObject jsonObj = new JSONObject();


        //如果是定时器： 则创建session
        if (!JmeterSession.SESSION_TIMER_Mid.equals(JmeterSession.SESSION_TIMER_End)) {

            session = request.getSession(true);
            System.out.println("session--->" + session);
            session.setAttribute("runusername", "timeruserInter");
            session.setAttribute(JmeterSession.SESSION_INTER, JmeterSession.SESSION_TIMER_End);
        }

        String sessionName = (String) session.getAttribute(JmeterSession.SESSION_INTER);


        if (null == sessionName) {

            Map<String, Object> mapresult = new HashMap<>();

            jsonObj.put("session", "null");
            map.put("请先提交jmeter接口测试配置信息", jsonObj);
            mapresult.put("result", map);
            return mapresult;
        }

        //如果所有agent都不可用,则提前提示：
        List<String> listcomp = new ArrayList<String>();
        listcomp.add("Created");
        listcomp.add("Finished");

        List<Jmeter_agentip_states> agentipList = consumer_jmeter_agentip_statesMapper.selectByStatusTpye("Inter", listcomp);

        System.out.println("agentipList--->" + agentipList.size());
        if (0 == agentipList.size()) {
            jsonObj.put("message", "无接口代理机器进行接口测试");
            map.put("result", jsonObj);
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


        //得到排序后的运行id:
        Map<Integer, Integer> mapsortId = new TreeMap<Integer, Integer>(
                new Comparator<Integer>() {
                    public int compare(Integer obj1, Integer obj2) {
                        // 降序排序
                        return obj1.compareTo(obj2);
                    }
                });


        String[] jsonArr = ids;


        String sessionUserName = null;
        System.err.println("session username----->" + session.getAttribute("username"));
        //vue 有session，但后台更新重启了，session前台还是原来的，但后台没有获取到,以免agent受到影响：
        if (null == session.getAttribute("username")) {
            sessionUserName = " ";
        } else {
            sessionUserName = (String) session.getAttribute("username");
        }

        //运行脚本时，检查jmeter代理服务是否已启动，如果没有启动则自动启动jmeter
        jsonObj.put("runJmeterAgentCheck", "runJmeterAgentCheck");
        jsonObj.put("consumerIp", dyIPaddressPubicProvider.getConsumerOsIpaddress());
        jsonObj.put("runusername", sessionUserName);
        jsonObj.toJSONString();
        ThreadByRunnableSendMq thread = new ThreadByRunnableSendMq(publishController, jsonObj);
        new Thread(thread, "发送:检查jmeter代理服务器检查消息......").start();

        //给jmeter服务器启动时间：
        //  Thread.sleep(5000);

        System.out.println("jsonArr.length===" + jsonArr.length);
        //根据此变量决定详细结果的类型:

        for (int interInit = 0; interInit < jsonArr.length; interInit++) {

            int id = Integer.valueOf(jsonArr[interInit]);

            Map<String, Object> mapkey = uploadScriptServer.getByPrimaryKeyServer(id);

            System.out.println("mapkey=" + mapkey);
            log.info("执行的脚本为：" + mapkey);

            String scriptrunorder = (String) mapkey.get("scriptrunorder");

            String scriptpath = (String) mapkey.get("scriptpath");

            String username = (String) mapkey.get("username");

            String scriptname = (String) mapkey.get("scriptname");


            if (scriptrunorder.equals("") || scriptrunorder == null || Integer.valueOf("100") == 100) {
                scriptrunorder = String.valueOf(100 + interInit);
            }

            mapsort.put(Integer.valueOf(scriptrunorder), scriptpath);
            mapsortId.put(Integer.valueOf(scriptrunorder), id);
        }


        /**
         * 根据请求对jmeter的控制台进行分发：对控制台支持多用户并发
         */
        //得到控制台节点：
        String jmeterControlNode = this.getRunJmeterNode();

        // 1.查库 得到 scriptrunorder, scriptpath 的排序

        //统计脚本次数,为生成报告做准备：
        int scriptCount = 0;

        //更新数据库状态为：0---->1  由发过邮件的状态变成，不能发邮件的状态:
        jmeterIntercurrentreportServer.UpdateStatus_0_1(ReportStates.INSERT_0);


        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");  //yyyyMMddHHmmss
        String todayTime = df.format(new Date()) + "/";
        String resultReportDir = resultReportDir_Inter.replace("\\", "/") + todayTime;
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

            //得到运行脚本名称：
            File file = new File(scriptJmeterZip);

            String fileName = file.getName();
            String jmeterScriptName = fileName.substring(0, fileName.length() - 4);

            System.out.println("jmeterScriptName11--->" + jmeterScriptName);

            scriptNameMap.put(scriptCount, jmeterScriptName);


            //得到运行脚本id：
            int runId = mapsortId.get(key);

            //更新接口测试的运行状态：
            uploadScriptServer.updateInterRunStates(JmeterPerRunStatus.Inter_Run, runId, JmeterPerRunStatus.Inter_Run, "");


            try { //开始运行，进入运行主类：
                jmeterInterMainImpl.jmeterMainEnter(jmeterControlNode, scriptJmeterZip, jmeterScriptName, scriptCount, scriptSumSize, resultReportDir, reportHtmlName, scriptNameMap, runId, json, session);
            } catch (Exception e) {

                scriptNameMap.clear();
                modelMap.put("result", new ResponseResult(ResponseMeta.NODE_NO_RUN_ERROR, "节点未运行收集报告失败"));
                return modelMap;
            }


        }


        scriptNameMap.clear();

        modelMap.put("result", new ResponseResult(ResponseMeta.SUCCESS, "提交成功"));
        return modelMap;


    }


    //不是总流程的一部分：获取运行的用那个jmeter节点运行
    public String getRunJmeterNode() throws UnsupportedEncodingException {


        String[] jmeterProperties = jmeterProperties_Inter.replace("\\", "/").split(",");

        log.info("接口测试的配置路径是:{}",jmeterProperties);

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

        return jmeterRunNode;
    }


}


