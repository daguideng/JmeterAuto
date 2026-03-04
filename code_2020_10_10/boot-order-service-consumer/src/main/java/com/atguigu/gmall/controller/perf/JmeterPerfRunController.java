package com.atguigu.gmall.controller.perf;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall.Impl.perfImpl.JmeterNode;
import com.atguigu.gmall.Impl.perfImpl.JmeterPerRunStatus;
import com.atguigu.gmall.Impl.perfImpl.JmeterSession;
import com.atguigu.gmall.Interface.RunScriptControllerIntel;
import com.atguigu.gmall.common.Threads.ThreadByRunnableRunPerf;
import com.atguigu.gmall.common.bean.ResponseMeta;
import com.atguigu.gmall.common.bean.ResponseResult;
import com.atguigu.gmall.common.bean.response.Result;
import com.atguigu.gmall.common.constant.RES_STATUS;
import com.atguigu.gmall.entity.Perf_config;
import com.atguigu.gmall.entity.Upload_info;
import com.atguigu.gmall.service.impl.UpdateJmeterStatus;
import com.atguigu.gmall.service.jmeterperf.PerfConfigServer;
import com.atguigu.gmall.service.jmeterperf.UploadScriptServer;
import com.atguigu.gmall.zookeeperip.DynamicHTTPHostProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;


/**
 * 性能脚本支持多个脚本分别执行不同性能测试的批量执行性能测试功能
 * @Author: dengdagui
 * @Description: 运行参数为：ids:1,2,3....
 * @Date: Created in 2018-7-19
 */

@Slf4j
@RestController
@RequestMapping("/jmeterperf")
//@Api(value = "性能自动化模块", tags = "PERF-性能自动化模块" ,description = "性能运行模块")
public class JmeterPerfRunController {




    @Resource(name = "runScriptPerfControllerImpl")
    RunScriptControllerIntel runScriptPerfController;

    @Autowired
    UploadScriptServer uploadScriptServer;

    @Autowired
    private JmeterNode jmeterNode ;

    @Autowired
    private DynamicHTTPHostProvider dynamicHTTPHostProvider;

    @Autowired
    private UpdateJmeterStatus updateJmeterStatus ;


    @Autowired
    private PerfConfigServer perfConfigServer ;



    /**
     * 运行接口：
     *
     * @param session
     * @param ids
     * @return
     * @throws Exception
     */
    //@ApiOperation(value = "perf：性能测试运行接口")
    @RequestMapping(value = "/run", method = RequestMethod.POST)
    public Result <?> list(HttpSession session, @RequestParam(value = "ids") String[] ids,@RequestParam(value = "perfConfig") String perfConfig ,HttpServletRequest request, HttpServletResponse response)
            throws Exception {


        Result <Map <String, Object>> result = new Result <>();

        Map <String, Object>   map = new HashMap <>() ;

        Map <String, Object>   datamap = new HashMap <>() ;

        JSONObject jsonObj = new JSONObject();


        //如果是定时器： 则创建session
        if(!JmeterSession.SESSION_TIMER_Mid.equals(JmeterSession.SESSION_TIMER_End)){
            session = request.getSession(true);
            System.out.println("session--->" + session);

            session.setAttribute(JmeterSession.SESSION_PERF, JmeterSession.SESSION_TIMER_End);
        }




        if(null == session.getAttribute(JmeterSession.SESSION_PERF)){

            //如果session失效则重新获session:
            Perf_config perf_config = perfConfigServer.getLastLastThreadname();
            String threadName = perf_config.getThreadname();
            session.setAttribute(JmeterSession.SESSION_PERF, threadName);

            log.info("重新从数据库中获取sessin:{}",threadName);


            jsonObj.put("msg", "sessin值为Null,请先提交jmeter性能配置信息");
            jsonObj.put("code", 600);
            map.put("result",jsonObj);
            result.setData(datamap) ;
           // return  result ;
        }



        /**
        if( jmeterNode.runNodeServerRunable(session)== 1 ){


            jsonObj.put("msg", "jmeter性能服务器所有节点正在运行请稍后再试!");
            jsonObj.put("code", 601);

            map.put("result",jsonObj);

            result.setData(map) ;
            return  result ;

        }
         ***/





        JSONObject json = JSONObject.parseObject(perfConfig);
      //  System.out.printf("perfConfig------------>"+perfConfig);
        System.out.printf("json------------>"+json);



        //先进行数据库表的复制： 不要复制
       // updateJmeterStatus.upStatus();

        // 接口测试时检查节点或控制台服务器是否用完，如果用完则对前端进行提示：
        try {


            String agentInfo = dynamicHTTPHostProvider.getRandomIP("Perf").toString();
            if(("".equals(agentInfo) || null == agentInfo)){
                log.info("所有性能测试的agent现在没有可用的！");
            }else if(JmeterNode.storelist.size() == JmeterNode.ThreadJmeterNode.size() && (JmeterNode.storelist.size()!=0) ){

                log.info("所有性能测试控制台节点的master节点已用完，现没有可运行的节点！");
            }
        }catch (Exception e){
            jsonObj.put("msg", "jmeter性能服务器所有节点状态都已禁用,请检查!");
            jsonObj.put("code", 601);
            map.put("result",jsonObj);
            result.setData(map) ;
            return  result ;

        }




        //运行脚本时，检查jmeter代理服务是否已启动，如果没有启动则自动启动jmeter
        Map<String,Object>  objmap = new HashMap <>() ;

        objmap.put("session", session);
        objmap.put("ids", ids);
        objmap.put("perfConfig", perfConfig);


        // ThreadByRunnableRunPerf thread = new ThreadByRunnableRunPerf(runScriptPerfController, objmap,request) ;
        // new Thread(thread, "异步运行jmeterPerf性能测试......").start();

        ThreadByRunnableRunPerf thread = new ThreadByRunnableRunPerf(runScriptPerfController, objmap,request) ;
        new Thread(thread, "").start();




        //让线程等待1.5秒
     //   Thread.sleep(1500);

        Map<String,Object>  responseMessage = new HashMap <>() ;
        Map<String,Object>  mapstate = new HashMap <>() ;
        mapstate.put("state", JmeterPerRunStatus.Perf_Run);

        responseMessage.put("result",new ResponseResult(ResponseMeta.SUCCESS, mapstate));



        result.setData(responseMessage) ;


        return result;
    }




    /**
     * 运行接口：
     *
     * @param session
     * @param ids
     * @return
     * @throws Exception
     */
    //@ApiOperation(value = "perf：性能测试运行接口")
    @RequestMapping(value = "/runX", method = RequestMethod.POST)
    public Result <?> listX(HttpSession session, @RequestParam(value = "ids") String[] ids, HttpServletRequest request, HttpServletResponse response)
            throws Exception {


        Result <Map <String, Object>> result = new Result <>();

        Map <String, Object>   map = new HashMap <>() ;

        Map <String, Object>   datamap = new HashMap <>() ;

        JSONObject jsonObj = new JSONObject();


        //如果是定时器： 则创建session
        if(!JmeterSession.SESSION_TIMER_Mid.equals(JmeterSession.SESSION_TIMER_End)){
            session = request.getSession(true);
            System.out.println("session--->" + session);

            session.setAttribute(JmeterSession.SESSION_PERF, JmeterSession.SESSION_TIMER_End);
        }




        if(null == session.getAttribute(JmeterSession.SESSION_PERF)){


            jsonObj.put("msg", "sessin值为Null,请先提交jmeter性能配置信息");
            jsonObj.put("code", 600);
            map.put("result",jsonObj);
            result.setData(datamap) ;
            return  result ;
        }




        if( jmeterNode.runNodeServerRunable(session)== 1 ){


            jsonObj.put("msg", "jmeter性能服务器所有节点正在运行请稍后再试!");
            jsonObj.put("code", 601);

            map.put("result",jsonObj);

            result.setData(map) ;
            return  result ;

        }


        //先进行数据库表的复制： 不要复制
        // updateJmeterStatus.upStatus();

        // 接口测试时检查节点或控制台服务器是否用完，如果用完则对前端进行提示：
        try {


            String agentInfo = dynamicHTTPHostProvider.getRandomIP("Perf").toString();
            if(("".equals(agentInfo) || null == agentInfo)){
                log.info("所有性能测试的agent现在没有可用的！");
            }else if(JmeterNode.storelist.size() == JmeterNode.ThreadJmeterNode.size() && (JmeterNode.storelist.size()!=0) ){

                log.info("所有性能测试控制台节点的master节点已用完，现没有可运行的节点！");
            }
        }catch (Exception e){
            jsonObj.put("msg", "jmeter性能服务器所有节点状态都已禁用,请检查!");
            jsonObj.put("code", 601);
            map.put("result",jsonObj);
            result.setData(map) ;
            return  result ;

        }




        //运行脚本时，检查jmeter代理服务是否已启动，如果没有启动则自动启动jmeter
        Map<String,Object>  objmap = new HashMap <>() ;

        objmap.put("session", session);
        objmap.put("ids", ids);

        // ThreadByRunnableRunPerf thread = new ThreadByRunnableRunPerf(runScriptPerfController, objmap,request) ;
        // new Thread(thread, "异步运行jmeterPerf性能测试......").start();

        ThreadByRunnableRunPerf thread = new ThreadByRunnableRunPerf(runScriptPerfController, objmap,request) ;
        new Thread(thread, "").start();




        //让线程等待1.5秒
        //   Thread.sleep(1500);

        Map<String,Object>  responseMessage = new HashMap <>() ;
        Map<String,Object>  mapstate = new HashMap <>() ;
        mapstate.put("state", JmeterPerRunStatus.Perf_Run);

        responseMessage.put("result",new ResponseResult(ResponseMeta.SUCCESS, mapstate));



        result.setData(responseMessage) ;


        return result;
    }


    /**
     * 性能测试保存脚本执行顺序接口
     */
    //@ApiOperation(value = "perf：性能测试执行顺序保存接口")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Result <?> updateOrder(HttpSession session, @RequestParam(value = "upload") String upload)
            throws Exception {


        Result <Map <String, Object>> result = new Result <>();

        if (null == upload || "".equals(upload)) {
            Result <Object> Errorresult = new Result <>();
            log.info(RES_STATUS.BAD_PARAM_NULL.msg);
            Errorresult.setStatus(RES_STATUS.BAD_PARAM_NULL);
            return Errorresult.setData(Errorresult);
        }

        if (null != upload || !"".equals(upload)) {
            Map runResult = uploadScriptServer.updateScriptOrder(upload);
            result.setData(runResult);
        }

        return result;
    }


    //@ApiOperation(value = "PERF：性能测试执行顺序保存接口")
    @RequestMapping(value = "/updateScriptOrder", method = RequestMethod.POST)
    public Result <Object> login(@RequestBody Upload_info uploadinfo, HttpSession session) {
        Result <Object> result = new Result <>();
        log.info("get updateScriptOrder param updateScriptOrderParm={}", uploadinfo);



        if ("".equals(uploadinfo.getId()) || "".equals(uploadinfo.getScriptname())) {
            log.info(RES_STATUS.BAD_PARAM_NULL.msg);
            result.setStatus(RES_STATUS.BAD_PARAM_NULL);
            return result;
        }


        Map runResult = uploadScriptServer.updateScriptOrder(uploadinfo);
        result.setData(runResult);

        return result;

    }

}
