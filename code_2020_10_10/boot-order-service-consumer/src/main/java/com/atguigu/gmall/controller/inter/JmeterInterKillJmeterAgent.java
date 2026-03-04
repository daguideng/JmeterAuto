package com.atguigu.gmall.controller.inter;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall.Impl.perfImpl.JmeterNode;
import com.atguigu.gmall.Impl.perfImpl.JmeterPerRunStatus;
import com.atguigu.gmall.Impl.perfImpl.JmeterPerfProcessImpl;
import com.atguigu.gmall.Impl.perfImpl.JmeterSession;
import com.atguigu.gmall.activemq.PublishController;
import com.atguigu.gmall.activemq.ThreadByRunnableSendMq;
import com.atguigu.gmall.common.bean.response.Result;
import com.atguigu.gmall.common.utils.CommandUtil;
import com.atguigu.gmall.common.utils.KillJMeterNodeProcess;
import com.atguigu.gmall.common.utils.OSUtils;
import com.atguigu.gmall.entity.Upload_info;
import com.atguigu.gmall.service.jmeterinter.InterConfigServer;
import com.atguigu.gmall.service.jmeterinter.KillProcessByPortService;
import com.atguigu.gmall.service.jmeterperf.UploadScriptServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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
@RequestMapping("/jmeterinter")
public class JmeterInterKillJmeterAgent {

    @Autowired
    private InterConfigServer interConfigServer;

    @Autowired
    private UploadScriptServer uploadScriptServer;

    @Autowired
    private PublishController publishController;

    @Resource
    private KillProcessByPortService killProcessByPortService;

    @Value("${jmeterProperties.Inter}")
    private String jmeterProperties_Inter;

    @Resource
    private KillJMeterNodeProcess killJMeterNodeProcess;

    @Value("${jmeterProperties.Perf}")
    private String jmeterProperties_Perf;


    @Autowired
    private JmeterPerfProcessImpl jmeterPerfProcessImpl;


    /**
     * 控制台控制代理进程
     */
    @RequestMapping(value = "/controllkillAgent", method = RequestMethod.POST)
    public Result<?> list(HttpSession session) throws Exception {
        Result<Map<String, Object>> result = new Result<>();

        // 清理相关资源
        JmeterNode.storelist.clear();
        JmeterNode.ThreadJmeterNode.clear();
        
        log.info("jmeterNode处理完毕 {}");

        // 发送MQ消息
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("msg", "killInterJmeterAgentProcess");
        
        ThreadByRunnableSendMq thread = new ThreadByRunnableSendMq(publishController, jsonObj);
        new Thread(thread, "发送：Perf的agent都kill掉......").start();

        // 杀进程操作
        killJMeterNodeProcess.killJmeter();
        
        String processName = "jmeter_inter";
        killProcessByPortService.killJmeter(processName);
        killProcessByPortService.killJmeter(processName);
        
        int port = 1099;
        killProcessByPortService.killProcessPort(port);

        // 修复：使用可修改的ArrayList替代Arrays.asList
        List<String> interlist = new ArrayList<>(Arrays.asList(jmeterProperties_Inter.split(",")));
        List<String> perlist = new ArrayList<>(Arrays.asList(jmeterProperties_Perf.split(",")));
        
        // 现在可以安全地执行addAll操作
        interlist.addAll(perlist);

        // 修复：使用List来存储命令，而不是StringBuffer
        List<String> commands = new ArrayList<>();

        try {
            for (String jmeterBin : interlist) {
                jmeterBin = jmeterBin.replace("/jmeter.properties", "");

                String shutdownScript;
                if (OSUtils.isWindows()) {
                    shutdownScript = jmeterBin + "/shutdown.cmd";
                } else {
                    shutdownScript = jmeterBin + "/shutdown.sh";
                }
                commands.add(shutdownScript);
            }

            // 分别执行每个命令
            for (String command : commands) {
                try {
                    log.info("执行关闭命令: {}", command);
                    CommandUtil.commandExecution(command);
                    // 添加短暂延迟，避免同时执行过多命令
                    Thread.sleep(100);
                } catch (Exception e) {
                    log.error("执行命令失败: {}", command, e);
                }
            }

            // 更新状态
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = format.format(new Date());

            String sessionName = (String) session.getAttribute(JmeterSession.SESSION_PERF);
            if (sessionName != null) {
                interConfigServer.updateRunStates(JmeterPerRunStatus.Inter_Stop, sessionName);
            }

            List<Upload_info> list = uploadScriptServer.updateStopByRunId(JmeterPerRunStatus.Inter_Run);

            if (list.size() > 0) {
                for (int run = 0; run < list.size(); run++) {
                    int runId = list.get(run).getId();
                    // 注释掉的代码保持原样
           //     uploadScriptServer.updateInterRunStates(JmeterPerRunStatus.Inter_Stop, runId, JmeterPerRunStatus.Inter_Stop, currentTime);
                }
            }

        } catch (Exception e) {
            log.error("服务器节点清空异常", e);
        }

        Map<String, Object> map = new HashMap<>();
        Map<String, Object> json = new HashMap<>();
        map.put("code", 0);
        map.put("data", "向jmeter代理成功发送命令");
        map.put("msg", "killInterJmeterAgentProcess");
        json.put("result", map);
        result.setData(json);


        JmeterNode.ThreadJmeterNode.clear();
        JmeterNode.storelist.clear();

      

        return result;
    }
}