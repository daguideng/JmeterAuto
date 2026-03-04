package com.atguigu.gmall.controller.inter;


import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall.Impl.interfaceImpl.InterConfigModel;
import com.atguigu.gmall.Impl.interfaceImpl.JmeterInterType;
import com.atguigu.gmall.Impl.perfImpl.JmeterSession;
import com.atguigu.gmall.Impl.perfImpl.PerfConfigStatus;
import com.atguigu.gmall.Interface.JmeterPostParameterIntel;
import com.atguigu.gmall.activemq.PublishController;
import com.atguigu.gmall.activemq.ThreadByRunnableSendMq;
import com.atguigu.gmall.common.bean.ResponseMeta;
import com.atguigu.gmall.common.bean.ResponseResult;
import com.atguigu.gmall.entity.Interface_config;
import com.atguigu.gmall.service.jmeterinter.InterConfigServer;
import com.atguigu.gmall.zookeeperip.DyIPaddressPubicProvider;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @Author: dengdagui
 * @Description: jmeter接口测试前端传过来的参数：
 * @Date: Created in 2018-7-2
 */
@Slf4j
@Component
@RestController
@RequestMapping("/jmeterinter")
//@Api(value = "脚本上传模块", tags = "Mars-性能脚本上传模块", description = "性能脚本上传模块")
@Controller

public class JmeterInterPostParameterImpl implements JmeterPostParameterIntel {

    @Autowired
    private InterConfigServer interConfigServer ;

    @Autowired
    private PublishController publishController;

    @Autowired
    private DyIPaddressPubicProvider dyIPaddressPubicProvider ;




    /**
     * 前端控制后端接口测试参数
     */
    //@ApiOperation(value = "SYS:PRIVILEGE: 性能基本配置")
    @RequestMapping(method = RequestMethod.POST, value = "config")
    public Map<String,Object> jmeterperslist(String configxml,HttpSession session) throws DocumentException {

        log.info("configxml : {}, 性能参数 : {}", configxml);

        Map <String, Object> mapmodel = new HashMap<>();

        Map<String, Object> map = new HashMap <>();

        // configxml = String.valueOf((configxml.trim().toString()).split("\n"));

        Document doc = DocumentHelper.parseText(configxml);
        Element el_root = doc.getRootElement();
        Iterator<?> it = el_root.elementIterator();

        while (it.hasNext()) {
            // 遍历该子节点
            Object o = it.next();// 再获取该子节点下的子节点
            Element el_row = (Element) o;
            el_row.getText();
            Iterator <?> it_row = el_row.elementIterator();
            // 遍历所有性能参数的类型并存储在内存中:
            for (String type : JmeterInterType.InterType()) {

                List <Object> list = new ArrayList<>();
                if (el_row.elementText("no").equals(type)) {
                    while (it_row.hasNext()) {
                        // 遍历节点
                        Element el_ename = (Element) it_row.next();
                        String betweenValueStatus = el_ename.getText().toString();
                        // 排除并发用户数，这一条数据:
                        if (!betweenValueStatus.equals(type)) {

                            list.add(betweenValueStatus.trim());

                        }

                    }


                    map.put(type, list);

                }


            }

        }

        String uuidRandomStr = UUID.randomUUID().toString().substring(0,12);


        Interface_config interconfig = new Interface_config(map.get(JmeterInterType.Inter_Threads).toString(),map.get(JmeterInterType.Inter_RunTime).toString(),map.get(JmeterInterType.Inter_DelayTime).toString(),map.get(JmeterInterType.Inter_Retry).toString(),
                map.get(JmeterInterType.Inter_Modiy_Interval_Time).toString(),map.get(JmeterInterType.Inter_Add_Custom_Listener).toString(),map.get(JmeterInterType.Inter_Add_Log_Jtil).toString(),
                map.get(JmeterInterType.Inter_Add_Jmeter_Log).toString(),map.get(JmeterInterType.Inter_Set_Between_Value).toString(),uuidRandomStr, PerfConfigStatus.start);



        //Thread.currentThread().getName() 存入Session, 为同一个session保存性能配置信息的验证点
        session.setAttribute(JmeterSession.SESSION_INTER, uuidRandomStr);
        log.info("threadname="+session.getAttribute(JmeterSession.SESSION_INTER)) ;
        log.info("interconfig="+interconfig);

        //配置参数入库：
        interConfigServer.insertInterconfig(interconfig);


        mapmodel.put("result",new ResponseResult(ResponseMeta.SUCCESS, "提交成功")) ;

        map.clear();


        //发amq消息为agent自动启动运行做准备：
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("config", "configInter");
        jsonObj.put("consumerIp",dyIPaddressPubicProvider.getConsumerOsIpaddress());
        jsonObj.toJSONString();
        ThreadByRunnableSendMq thread = new ThreadByRunnableSendMq(publishController,jsonObj) ;
        new Thread(thread, "发送：客户端基本参数初始化 configInter......").start();

        return  mapmodel ;

    }



    /**
     * 前端控制后端性能测试参数
     */
    public Map<String,Object> interConfigParm(InterConfigModel interConfigModel, HttpSession session, HttpServletResponse response) throws Exception {

        log.info("interConfig : {}, 接口参数 : {}", interConfigModel);

        Map <String, Object> mapmodel = new HashMap <>();

        Map <String, Object> map = new HashMap <>();

        String uuidRandomStr = UUID.randomUUID().toString().substring(0,10);

        Interface_config pcf = new Interface_config();

        pcf.setThreads(Arrays.toString(interConfigModel.getVuser().split(",")));
        pcf.setRuntime(Arrays.toString(new String[]{interConfigModel.getRuntime(),interConfigModel.getSleeptime()}));
        pcf.setDelaytime(Arrays.toString(interConfigModel.getDelaytime().split(",")));
        pcf.setIfretry(Arrays.toString(new String[]{(interConfigModel.getRetry().equalsIgnoreCase("false")?"No":"Yes"),interConfigModel.getRetryVal()}));
        pcf.setIfoutinterval(Arrays.toString(new String[]{(interConfigModel.getOutput().equalsIgnoreCase("false")?"No":"Yes"),interConfigModel.getOutputInterval()}));
        pcf.setIfcustomlistener(Arrays.toString((interConfigModel.getCustomListener().equalsIgnoreCase("false")?"No":"Yes").split(",")));
        pcf.setIfrecordlogjtl(Arrays.toString((interConfigModel.getJtlListener().equalsIgnoreCase("true")?"Yes":"No").split(",")));
        pcf.setIfrecordlogjmeter(Arrays.toString((interConfigModel.getLogListener().equalsIgnoreCase("false")?"No":"Yes").split(",")));
        pcf.setIfbetweenvalue(Arrays.toString((interConfigModel.getBetweenValue().equalsIgnoreCase("false")?"No":"Yes").split(",")));
        pcf.setThreadname(uuidRandomStr);
        pcf.setStatus(PerfConfigStatus.start);


        session.setAttribute(JmeterSession.SESSION_INTER, uuidRandomStr);



        log.info("session值="+session.getAttribute(JmeterSession.SESSION_INTER)) ;
        log.info("pcf="+pcf);



        /**
         Perfconfig  pfconfig = new Perfconfig(map.get(JmeterPerfType.Perf_Threads).toString(),map.get(JmeterPerfType.Perf_RunTime).toString(),map.get(JmeterPerfType.Perf_DelayTime).toString(),map.get(JmeterPerfType.Perf_Retry).toString(),
         map.get(Perf_Modiy_Interval_Time).toString(),map.get(JmeterPerfType.Perf_Add_Custom_Listener).toString(),map.get(JmeterPerfType.Perf_Add_Log_Jtil).toString(),
         map.get(JmeterPerfType.Perf_Add_Jmeter_Log).toString(),map.get(JmeterPerfType.Perf_Set_Between_Value).toString(),uuidRandomStr,PerfConfigStatus.start);
         ***/




        //配置参数入库：
        interConfigServer.insertInterconfig(pcf);

        session.setAttribute(JmeterSession.SESSION_INTER, uuidRandomStr);

        //发消息给客户端:
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("config", "configInter");
        jsonObj.put("consumerIp",dyIPaddressPubicProvider.getConsumerOsIpaddress());
        jsonObj.toJSONString();
        ThreadByRunnableSendMq thread = new ThreadByRunnableSendMq(publishController,jsonObj) ;
        new Thread(thread, "发送：客户端基本参数初始化 configPerf......").start();



        mapmodel.put("result",new ResponseResult(ResponseMeta.SUCCESS, "提交成功")) ;

        map.clear();
        System.out.println();

        return  mapmodel ;

    }

}


