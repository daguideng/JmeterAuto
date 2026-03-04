package com.atguigu.gmall.controller.timer;

import com.atguigu.gmall.Impl.perfImpl.JmeterPerfPostParameterImpl;
import com.atguigu.gmall.Impl.perfImpl.JmeterSession;
import com.atguigu.gmall.Impl.timerImpl.TimerModel;
import com.atguigu.gmall.common.bean.response.Result;
import com.atguigu.gmall.common.constant.RES_STATUS;
import com.atguigu.gmall.common.jobs.BeanJob.JmeterPerfScheduledJob;
import com.atguigu.gmall.dao.Timer_type_configMapper;
import com.atguigu.gmall.entity.Timer_type_config;
import com.atguigu.gmall.service.impl.TimerServerImpl;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2019-9-2
 */
@Slf4j
@RestController
@RequestMapping("/timer")
public class TimerTypeConfig {



    @Autowired
    JmeterPerfPostParameterImpl jmeterPerfPostParameterImpl;

    @Autowired
    private TimerServerImpl timerServerImpl ;

    @Autowired
    private JmeterPerfScheduledJob jmeterPerfScheduledJob ;

    @Autowired
    private Timer_type_configMapper timer_type_configMapper;


    @RequestMapping(value = "/addtimer", method = RequestMethod.POST)
    @ResponseBody
    //@RequestBody TimerModel timerconfig
    Result<?> addTimer(@RequestBody TimerModel timerconfig, HttpSession session, HttpServletResponse response) throws Exception {
        Result <Object> result = new Result <>();
        log.info("get timerconfig param uperfConfig={}", timerconfig);
        System.out.println("timerconfig----------->"+timerconfig);


        if ("".equals(timerconfig.getVuser()) || "".equals(timerconfig.getRuntime()) || "false".equalsIgnoreCase(timerconfig.getJtlListener())) {
            log.info(RES_STATUS.BAD_PARAM_NULL.msg);
            Map<String, Object> map = new HashMap<>();
            map.put("timerconfig", timerconfig);
            result.setStatus(RES_STATUS.BAD_PARAM_NULL);
            result.setData(map);
            return result;
        }


        Map <String, Object> mapmodel = new HashMap <>();

        Map <String, Object> map = new HashMap <>();

        String uuidRandomStr = UUID.randomUUID().toString().substring(0,10);

        Timer_type_config timer_config = new Timer_type_config();

        timer_config.setThreads(Arrays.toString(timerconfig.getVuser().split(",")));
        timer_config.setRuntime(Arrays.toString(new String[]{timerconfig.getRuntime(),timerconfig.getSleeptime()}));
        timer_config.setDelaytime(Arrays.toString(timerconfig.getDelaytime().split(",")));
        timer_config.setIfretry(Arrays.toString(new String[]{(timerconfig.getRetry().equalsIgnoreCase("false")?"No":"Yes"),timerconfig.getRetryVal()}));
        timer_config.setIfoutinterval(Arrays.toString(new String[]{(timerconfig.getOutput().equalsIgnoreCase("false")?"No":"Yes"),timerconfig.getOutputInterval()}));
        timer_config.setIfcustomlistener(Arrays.toString((timerconfig.getCustomListener().equalsIgnoreCase("false")?"No":"Yes").split(",")));
        timer_config.setIfrecordlogjtl(Arrays.toString((timerconfig.getJtlListener().equalsIgnoreCase("true")?"Yes":"No").split(",")));
        timer_config.setIfrecordlogjmeter(Arrays.toString((timerconfig.getLogListener().equalsIgnoreCase("false")?"No":"Yes").split(",")));
        timer_config.setIfbetweenvalue(Arrays.toString((timerconfig.getBetweenValue().equalsIgnoreCase("false")?"No":"Yes").split(",")));
        timer_config.setThreadname(uuidRandomStr);
        timer_config.setStatus("wait");


        timer_config.setType(timerconfig.getType());
        timer_config.setTimestatus("Yes");
        timer_config.setIds(timerconfig.getIds());
        String timetask = timerconfig.getTimetask().trim();
        timer_config.setTimetask(timetask);
        timer_config.setJobname(timerconfig.getJobname());
        timer_config.setTriggername(timerconfig.getJobname());
        timer_config.setJobgroup(timer_config.getJobname());
        timer_config.setJobdescribe(timer_config.getJobname());
        timer_config.setDeletestate(1);


        session.setAttribute(JmeterSession.SESSION_PERF, uuidRandomStr);




        Map <String, Object> runResult = timerServerImpl.addTimerTypeConfig(timer_config);
        result.setData(runResult);

        return result;

    }


    /*
     * 进行逻辑删除：
     */
    /**
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    Result<?> deleteTimerConfig(Timer_type_config time_type_config,  @RequestParam(value = "id") Integer id) throws Exception {
        Result <Object> result = new Result <>();
        logger.info("get perfConfig param uperfConfig={}", time_type_config);


        Map runResult = timerServerImpl.updateTimerTypeConfigBydeletestate(0,id);
        result.setData(runResult);

        return result;

    }**/


    /**
     * 查询所有数据:
     * @param jobname
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    Result<?> queryTimerJobname(@RequestParam(required = false) String jobname) throws Exception {
        Result <Object> result = new Result <>();
        log.info("jobname={}", jobname);

        /*
        if ("".equals(jobname.trim()) || null == jobname.trim()) {
            logger.info(RES_STATUS.BAD_PARAM_NULL.msg);
            Map<String, Object> map = new HashMap<>();
            map.put("jobname", jobname);
            result.setStatus(RES_STATUS.BAD_PARAM_NULL);
            result.setData(map);
            return result;
        }*/


        Map runResult = timerServerImpl.queryTimerTypeConfig(jobname);
        result.setData(runResult);

        return result;

    }


    /**
     * 更新定时器的时间：
     * @param id
     * @param timetask
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updatecron", method = RequestMethod.POST)
    @ResponseBody
    Result<?> updateTimerTypeConfigTimetask(@RequestParam(value = "id") Integer id,@RequestParam(value = "timetask") String timetask) throws Exception {
        Result <Object> result = new Result <>();
        log.info("get perfConfig param timetask={}", timetask);


        if ("".equals(timetask.trim()) || "".equals(id) || null == timetask ) {
            log.info(RES_STATUS.BAD_PARAM_NULL.msg);
            Map<String, Object> map = new HashMap<>();
            map.put("timetask", timetask);
            result.setStatus(RES_STATUS.BAD_PARAM_NULL);
            result.setData(map);
            return result;
        }



        Map runResult = timerServerImpl.updateTimerTypeConfigTimetask(id,timetask.toString().trim());
        result.setData(runResult);

        return result;

    }


    /**
     * 更新定时的运行脚本ids：
     * @param id
     * @param Ids
     * @return
     * @throws Exception
     */

    @RequestMapping(value = "/updateids", method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    Result<?> updateTimerTypeConfigIds(@RequestParam(value = "id") Integer id,@RequestParam(value = "Ids") String Ids) throws Exception {
        Result <Object> result = new Result <>();
        log.info("get perfConfig param timetask={}", Ids);


        if ("".equals(Ids.trim()) || "".equals(id) || null == Ids ) {
            log.info(RES_STATUS.BAD_PARAM_NULL.msg);
            Map<String, Object> map = new HashMap<>();
            map.put("Ids", Ids);
            result.setStatus(RES_STATUS.BAD_PARAM_NULL);
            result.setData(map);
            return result;
        }


        Map runResult = timerServerImpl.updateTimerTypeConfigIds(id,Ids);
        result.setData(runResult);

        return result;

    }


    /**
     * 定时器所有数据同时更新：
     * @param record
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updateAll", method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    Result<?> updateTimerTypeConfigAll(Timer_type_config record,@RequestParam(value = "id") Integer id) throws Exception {
        Result <Object> result = new Result <>();
        log.info("get perfConfig param timetask={}", record);


        if ("".equals(record) || "".equals(id) || null == record ) {
            log.info(RES_STATUS.BAD_PARAM_NULL.msg);
            Map<String, Object> map = new HashMap<>();
            map.put("record", record);
            result.setStatus(RES_STATUS.BAD_PARAM_NULL);
            result.setData(map);
            return result;
        }


        Map runResult = timerServerImpl.updateTimerTypeConfigAll(record,id);
        result.setData(runResult);

        return result;

    }


    /**
     * 根据id,单独查询每行的key,value
     */
    @RequestMapping(value = "/getvalue", method = RequestMethod.POST)
    @ResponseBody
    Result<?> getByPrimaryKeyServer(@RequestParam(value = "id") Integer id) throws Exception {
        Result <Object> result = new Result <>();

        Map runResult = timerServerImpl.getByPrimaryKeyServer(id);
        result.setData(runResult);

        return result;

    }


    /***
     * 根据id,禁用task
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/pause", method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    Result<?> disableTask(@RequestParam(value = "id") Integer id) throws Exception {
        Result <Object> result = new Result <>();



        Map runResult = timerServerImpl.disable(id);
        result.setData(runResult);

        return result;

    }

    /****
     * 根据id,启用task
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/enable", method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    Result<?> enableTask(@RequestParam(value = "id") Integer id) throws Exception {
        Result <Object> result = new Result <>();

        Map runResult = timerServerImpl.enable(id);
        result.setData(runResult);

        return result;

    }


    /***
     * 删除定时器条目：
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/deletestate", method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    Result<?> deketeState(@RequestParam(value = "id") Integer id) throws Exception {
        Result <Object> result = new Result <>();

        Map runResult = timerServerImpl.deletestate(id);
        result.setData(runResult);

        return result;

    }


    /***
     * 修改类型：
     * @param id
     * @param type
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updateType", method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    Result<?> updateType(@RequestParam(required = true) Integer id,@RequestParam(required = true) String type) throws Exception {
        Result <Object> result = new Result <>();
        Map runResult = timerServerImpl.updateType(type,id);
        result.setData(runResult);
        return result;
    }


    /**
     * 修改Threads
     * @param id
     * @param threads
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updateThreads", method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    Result<?> updateThreads(@RequestParam(required = true) Integer id,@RequestParam(required = true) String threads) throws Exception {
        Result <Object> result = new Result <>();
        Map runResult = timerServerImpl.updateThreads(threads,id);
        result.setData(runResult);
        return result;
    }

    /**
     * 修改Runtime
     * @param id
     * @param runtime
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updateRuntime", method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    Result<?> updateRuntime(@RequestParam(required = true) Integer id,@RequestParam(required = true) String runtime) throws Exception {
        Result <Object> result = new Result <>();
        Map runResult = timerServerImpl.updateRuntime(runtime,id);
        result.setData(runResult);
        return result;
    }




    @ResponseBody
    @RequestMapping(value = "/nowjob", method = RequestMethod.POST)
    public Result<?> nowDoJob(@RequestParam(required = true) String id) {


        Result <Object> result = new Result <>();
        Map<String,Object> map = new HashedMap();

        //1.是否执行性能测试job
        Timer_type_config recond = timer_type_configMapper.selectByPrimaryKey(Integer.valueOf(id));
        //2.是YES才执行perf的job


        try {
            if(recond.getTimestatus().equalsIgnoreCase("Yes") && recond.getDeletestate()==1) {
                jmeterPerfScheduledJob.doJobTask(recond);
                map.put("count", 1);
                map.put("msg", "立即触发定时器成功!");
                result.setData(map);
            }else{
                map.put("count", 0);
                map.put("msg", "立即触发定时器失败!");
                result.setData(map);
            }
        }catch (Exception e){
            map.put("count", 0);
            map.put("msg", "立即触发定时器失败!");
            result.setData(map);
        }

        return result ;

    }


}
