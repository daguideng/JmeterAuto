package com.atguigu.gmall.Impl.perfImpl;


import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall.Interface.JmeterPostParameterIntel;
import com.atguigu.gmall.activemq.PublishController;
import com.atguigu.gmall.activemq.ThreadByRunnableSendMq;
import com.atguigu.gmall.common.bean.ResponseMeta;
import com.atguigu.gmall.common.bean.ResponseResult;
import com.atguigu.gmall.entity.Perf_config;
import com.atguigu.gmall.service.jmeterperf.PerfConfigServer;
import com.atguigu.gmall.zookeeperip.DyIPaddressPubicProvider;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;




/**
 * @Author: dengdagui
 * @Description: jmeter性能测试前端传过来的参数：
 * @Date: Created in 2018-7-2
 */
@Slf4j
@Component
public class JmeterPerfPostParameterImpl implements JmeterPostParameterIntel {

    @Autowired
    private PerfConfigServer perfConfigServer ;


    @Autowired
    private DyIPaddressPubicProvider dyIPaddressPubicProvider ;

    @Autowired
    private PublishController publishController;




    /**
     * 前端控制后端性能测试参数
     */
    public Map<String,Object> jmeterperslist(String configxml, HttpSession session) throws DocumentException {


//        response.setCharacterEncoding("utf-8");
//        response.setHeader("Content-type", "text/html;charset=UTF-8");
//        response.setContentType("text/html;charset=utf-8");

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
            for (String type : JmeterPerfType.perfType()) {

                List<Object> list = new ArrayList<>();
                if (el_row.elementText("no").equals(type)) {
                    while (it_row.hasNext()) {
                        // 遍历节点
                        Element el_ename = (Element) it_row.next();
                        String betweenValueStatus = el_ename.getText().toString();
                        // 排除并发用户数，这一条数据:
                        if (!betweenValueStatus.equals(type)) {

                            list.add(betweenValueStatus);
                        }

                    }


                    map.put(type, list);
                }


            }

        }



        String uuidRandomStr = UUID.randomUUID().toString().substring(0,10);

        Perf_config  pfconfig = new Perf_config(map.get(JmeterPerfType.Perf_Threads).toString(),map.get(JmeterPerfType.Perf_RunTime).toString(),map.get(JmeterPerfType.Perf_DelayTime).toString(),map.get(JmeterPerfType.Perf_Retry).toString(),
                map.get(JmeterPerfType.Perf_Modiy_Interval_Time).toString(),map.get(JmeterPerfType.Perf_Add_Custom_Listener).toString(),map.get(JmeterPerfType.Perf_Add_Log_Jtil).toString(),
                map.get(JmeterPerfType.Perf_Add_Jmeter_Log).toString(),map.get(JmeterPerfType.Perf_Set_Between_Value).toString(),uuidRandomStr,PerfConfigStatus.start);

        //Thread.currentThread().getName() 存入Session, 为同一个session保存性能配置信息的验证点
        session.setAttribute(JmeterSession.SESSION_PERF, uuidRandomStr);
        log.info("threadname="+session.getAttribute(JmeterSession.SESSION_PERF)) ;
        log.info("pfconfig="+pfconfig);

        //配置参数入库：
        perfConfigServer.insertPerfconfig(pfconfig);


        //发消息给客户端:
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("config", "configPerf");
        jsonObj.put("consumerIp",dyIPaddressPubicProvider.getConsumerOsIpaddress());
        jsonObj.toJSONString();
        ThreadByRunnableSendMq thread = new ThreadByRunnableSendMq(publishController,jsonObj) ;
        new Thread(thread, "发送：客户端基本参数初始化 configPerf......").start();



        mapmodel.put("result",new ResponseResult(ResponseMeta.SUCCESS, "提交成功")) ;

        map.clear();

        return  mapmodel ;

    }




    /**
     * 前端控制后端性能测试参数
     */
    public Map<String,Object> perfConfigParm(PerfConfigModel perfConfigModel,HttpSession session,HttpServletResponse response) throws Exception {

        log.info("perfConfig : {}, 性能参数 : {}", perfConfigModel);

        Map <String, Object> mapmodel = new HashMap <>();

        Map <String, Object> map = new HashMap <>();

        String uuidRandomStr = UUID.randomUUID().toString().substring(0,10);

        Perf_config  pcf = new Perf_config();

        pcf.setThreads(Arrays.toString(perfConfigModel.getVuser().split(",")));
        pcf.setRuntime(Arrays.toString(new String[]{perfConfigModel.getRuntime(),perfConfigModel.getSleeptime()}));
        pcf.setDelaytime(Arrays.toString(perfConfigModel.getDelaytime().split(",")));
        pcf.setIfretry(Arrays.toString(new String[]{(perfConfigModel.getRetry().equalsIgnoreCase("false")?"No":"Yes"),perfConfigModel.getRetryVal()}));
        pcf.setIfoutinterval(Arrays.toString(new String[]{(perfConfigModel.getOutput().equalsIgnoreCase("false")?"No":"Yes"),perfConfigModel.getOutputInterval()}));
        pcf.setIfcustomlistener(Arrays.toString((perfConfigModel.getCustomListener().equalsIgnoreCase("false")?"No":"Yes").split(",")));
        pcf.setIfrecordlogjtl(Arrays.toString((perfConfigModel.getJtlListener().equalsIgnoreCase("true")?"Yes":"No").split(",")));
        pcf.setIfrecordlogjmeter(Arrays.toString((perfConfigModel.getLogListener().equalsIgnoreCase("false")?"No":"Yes").split(",")));
        pcf.setIfbetweenvalue(Arrays.toString((perfConfigModel.getBetweenValue().equalsIgnoreCase("false")?"No":"Yes").split(",")));
        pcf.setThreadname(uuidRandomStr);
        pcf.setStatus(PerfConfigStatus.start);

        session.setAttribute(JmeterSession.SESSION_PERF, uuidRandomStr);


        log.info("threadname="+session.getAttribute(JmeterSession.SESSION_PERF)) ;
        log.info("pcf="+pcf);



        /**
        Perfconfig  pfconfig = new Perfconfig(map.get(JmeterPerfType.Perf_Threads).toString(),map.get(JmeterPerfType.Perf_RunTime).toString(),map.get(JmeterPerfType.Perf_DelayTime).toString(),map.get(JmeterPerfType.Perf_Retry).toString(),
                map.get(Perf_Modiy_Interval_Time).toString(),map.get(JmeterPerfType.Perf_Add_Custom_Listener).toString(),map.get(JmeterPerfType.Perf_Add_Log_Jtil).toString(),
                map.get(JmeterPerfType.Perf_Add_Jmeter_Log).toString(),map.get(JmeterPerfType.Perf_Set_Between_Value).toString(),uuidRandomStr,PerfConfigStatus.start);
        ***/




        //配置参数入库：
        perfConfigServer.insertPerfconfig(pcf);



        //发消息给客户端:
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("config", "configPerf");
        jsonObj.put("consumerIp",dyIPaddressPubicProvider.getConsumerOsIpaddress());
        jsonObj.toJSONString();
        ThreadByRunnableSendMq thread = new ThreadByRunnableSendMq(publishController,jsonObj) ;
        new Thread(thread, "发送：客户端基本参数初始化 configPerf......").start();




        mapmodel.put("result",new ResponseResult(ResponseMeta.SUCCESS, "提交成功")) ;
        mapmodel.put("perfconfig",pcf);

        map.clear();

        return  mapmodel ;

    }

}


