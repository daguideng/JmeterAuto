package com.atguigu.gmall.controller.perf;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall.Impl.perfImpl.JmeterNode;
import com.atguigu.gmall.Impl.perfImpl.JmeterPerRunStatus;
import com.atguigu.gmall.Impl.perfImpl.JmeterSession;
import com.atguigu.gmall.activemq.PublishController;
import com.atguigu.gmall.activemq.ThreadByRunnableSendMq;
import com.atguigu.gmall.common.bean.response.Result;
import com.atguigu.gmall.common.utils.CommandUtil;
import com.atguigu.gmall.common.utils.OSUtils;
import com.atguigu.gmall.entity.Upload_info;
import com.atguigu.gmall.service.jmeterperf.PerfConfigServer;
import com.atguigu.gmall.service.jmeterperf.UploadScriptServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @Author: dengdagui
 * @Description: 控制台控制jmeter代理的进程:
 * @Date: Created in 2018-7-19
 */

@Slf4j
@RestController
@RequestMapping("/jmeterperf")
//@Api(value = "控制台控制代理", tags = "Mars-控制台控制代理", description = "控制台控制代理")
public class JmeterPerfKillJmeterAgent {





    @Autowired
    private PerfConfigServer perfConfigServer;

    @Autowired
    private UploadScriptServer uploadScriptServer;

    @Autowired
    private PublishController publishController;


    @Value("${jmeterProperties.Perf}")
    private String jmeterProperties_Perf;


    /**
     * 控制台控制代理进程
     */
    //@ApiOperation(value = "控制台控制代理进程", notes = "控制台控制代理进程")
    @RequestMapping(value = "/controllkillAgent", method = RequestMethod.POST)
    public Result <?> list(HttpSession session) throws Exception {

        Result <Map <String, Object>> result = new Result <>();

        //运行脚本时，检查jmeter代理服务是否已启动，如果没有启动则自动启动jmeter


        JmeterNode.storelist.clear();
        JmeterNode.ThreadJmeterNode.clear();

        System.gc();
        log.info("jmeterNode处理完毕 {}");


        JSONObject jsonObj = new JSONObject();
        jsonObj.put("msg", "killPerfJmeterAgentProcess");
        jsonObj.toJSONString();


        ThreadByRunnableSendMq thread = new ThreadByRunnableSendMq(publishController,jsonObj) ;
        new Thread(thread, "发送：Perf的agent都kill掉......").start();


        /*
        ThreadByRunnableSendMq thread = new ThreadByRunnableSendMq(threadDataMessageProducer, jsonObj) ;
        new Thread(thread, "发送:控制台向代理服务器发送kill 代理消息......").start();
        **/


        


        List <String> strlist = Arrays.asList(jmeterProperties_Perf.split(","));

        StringBuffer sb = new StringBuffer(200);

        for (String jmeterBin : strlist) {


            jmeterBin = jmeterBin.replace("/jmeter.properties", "");

            System.out.println("jmeterBin----->" + jmeterBin);

            if (OSUtils.isWindows()) {
                sb.append("echo.|"+jmeterBin + "/" + "shutdown.cmd");
               // sb.append(jmeterBin + "/" + "shutdown.cmd");

            } else {
                sb.append(jmeterBin + "/" + "shutdown.sh");

            }

        }

        String commandArray[] = sb.toString().split(",");


        for (int i = 0; i < commandArray.length; i++) {
            try {
                CommandUtil.commandExecution(commandArray[i]);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                //清理内存方面的：
                sb.setLength(0);
            }


        }


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date nowTime = new Date();
        String currentTime = format.format(nowTime);

        //更新运行时状态为：stop
        String sessionName = (String) session.getAttribute(JmeterSession.SESSION_PERF);
        perfConfigServer.updateRunStates(JmeterPerRunStatus.Perf_Stop, sessionName);

        List <Upload_info> list = uploadScriptServer.updateStopByRunId(JmeterPerRunStatus.Perf_Run);


        if (list.size() > 0) {
            for (int run = 0; run < list.size(); run++) {
                int runId = list.get(run).getId();

                uploadScriptServer.updatePerfRunStates(JmeterPerRunStatus.Perf_Stop, runId, JmeterPerRunStatus.Perf_Stop, currentTime);
            }

        }


        Map <String, Object> map = new HashMap <>();
        Map <String, Object> json = new HashMap <>();
        map.put("code", 0);
        map.put("data", "向jmeter代理成功发送命令");
        map.put("msg", "killPerfJmeterAgentProcess");
        json.put("result", map);

        result.setData(json);

        return result;
    }


}

