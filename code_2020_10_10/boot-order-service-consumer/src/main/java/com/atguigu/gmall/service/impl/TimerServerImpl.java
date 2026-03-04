package com.atguigu.gmall.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.atguigu.gmall.common.utils.MyJsonUtil;
import com.atguigu.gmall.dao.Timer_type_configMapper;
import com.atguigu.gmall.entity.Timer_type_config;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2019-8-9
 */
@Slf4j
@Service
public class TimerServerImpl {



    @Autowired
    private Timer_type_configMapper timer_type_configMapper ;

    public Map<String,Object> addTimerTypeConfig(Timer_type_config record){

        int count =  timer_type_configMapper.insert(record);

        Map<String,Object> map = new HashedMap();

        if(count > 0) {
            log.info("定时器配置参数入库成功！");

        }else{
            log.info("定时器配置参数入库失败！");
        }

        map.put("count",count);

        return map;

    }



    /**
     * 以id为查询条件，得到key,value结果
     * @param id
     * @return
     */
    public Map<String,Object> getByPrimaryKeyServer(Integer id){

        Map<String,Object> mapkey    =  timer_type_configMapper.getByPrimaryKey(id);

        return  mapkey ;
    }





    /**
     * 更新定时时器的timetask：
     */
    public Map<String,Object> updateTimerTypeConfigTimetask(Integer id,String timetask){

        Map<String,Object>  map = new HashedMap() ;

        Timer_type_config timer_type_config = null ;

        int count  =  timer_type_configMapper.updateTimeTpyeConfigBytimetask(id,timetask);
        if(count >0){
            timer_type_config = timer_type_configMapper.selectByPrimaryKey(id);
            log.info("更新配置状态 "+timetask+" 成功!");
        }else{
            log.info("更新配置状态 "+timetask+" 失败!");
        }

        map.put("count",count)  ;
        map.put("data",MyJsonUtil.object_to_json(timer_type_config));

        return map ;

    }



    /**
     * 更新定时时器的ids：
     */
    public Map<String,Object> updateTimerTypeConfigIds(Integer id,String Ids){

        Map<String,Object>  map = new HashedMap();

        int count  =  timer_type_configMapper.updateTimeTpyeConfigByids(id,Ids);
        if(count >0){
            log.info("更新配置状态 "+Ids+" 成功!");
        }else{
            log.info("更新配置状态 "+Ids+" 失败!");
        }

        map.put("count",count);

        return map ;

    }



    /**
     * 更新定时时器的所有：
     */
    public Map<String,Object> updateTimerTypeConfigAll(Timer_type_config record,Integer id){

        Map<String,Object> map = new HashedMap();

        int result  =  timer_type_configMapper.updateTimeTpyeConfigByall(record,id);
        if(result >0){
            log.info("更新配置状态 "+record+" 成功!");
        }else{
            log.info("更新配置状态 "+record+" 失败!");
        }

        map.put("result",result);

        return map ;

    }


    /**
     * 定时器显示数据：
     */
    public Map<String,Object> queryTimerTypeConfig(String jobname) throws UnsupportedEncodingException {

        Map<String,Object> map = new HashedMap();

        List <Timer_type_config>  result = null ;

        if( null == jobname || "".equals(jobname)){
            result  =  timer_type_configMapper.queryimerTypeConfigByNull();
        }else{
            result  =  timer_type_configMapper.queryimerTypeConfig(jobname);

        }


        if(result.size() >0){
            log.info("查询timertypeconfig  "+jobname+" 成功!");
        }else{
            log.info("未查询到数据");
        }

        JSONArray array= JSONArray.parseArray(JSON.toJSONString(result));

    //    String json = MyJsonUtil.object_to_json(result);
   //     System.out.println("json---->"+json);
   //     System.out.println("array---->"+array);
        map.put("json",array);


        return map ;

    }


    /**
     * 更新:deletestate
     */
    public Map<String,Object> updateTimerTypeConfigBydeletestate(Integer deletestate,Integer id){

        Map<String,Object> map = new HashedMap();

        int result  =  timer_type_configMapper.updateTimerTypeConfigBydeletestate(deletestate,id);
        if(result >0){
            log.info("更新配置状态 "+deletestate+" 成功!");
        }else{
            log.info("更新配置状态 "+deletestate+" 失败!");
        }

        map.put("result",result);

        return map ;

    }

    /**
     * 禁用
     * @param id
     * @return
     */
    public Map<String,Object> disable(Integer id){

        int countx =  timer_type_configMapper.updateBydisableId(id);

        Map<String,Object> map = new HashedMap();

        if(countx > 0) {
            log.info("禁用定时器成功！");

        }else{
            log.info("禁用定时器失败！");
        }

        map.put("count",countx);

        return map;

    }


    /**
     * 启用定时器
     * @param id
     * @return
     */
    public Map<String,Object> enable(Integer id){

        int count =  timer_type_configMapper.updateByableId(id);

        Map<String,Object> map = new HashedMap();

        if(count > 0) {
            log.info("启用定时器成功！");

        }else{
            log.info("启用定时器失败！");
        }

        map.put("count",count);

        return map;

    }


    /**
     * 删除定时器：
     * @param id
     * @return
     */
    public Map<String,Object> deletestate(Integer id){

        int count =  timer_type_configMapper.deletestateById(id);

        Map<String,Object> map = new HashedMap();

        if(count > 0) {
            log.info("逻辑删除定时器成功！");

        }else{
            log.info("逻辑删除定时器失败！");
        }

        map.put("count",count);
        map.put("msg","确认删除吗？");

        return map;

    }


    /**
     * 修改定时器的类型：
     * @param id
     * @param type
     * @return
     */
    public Map<String,Object> updateType(String type,Integer id){

        System.out.println("type---->"+type);
        System.out.println("id---->"+id);
        int count =  timer_type_configMapper.updateTypeById(type,id);

        Map<String,Object> map = new HashedMap();

        if(count > 0) {
            log.info("启用定时器成功！");

        }else{
            log.info("启用定时器失败！");
        }

        map.put("count",count);

        return map;

    }




    public Map<String,Object> updateThreads(String threads,Integer id){

        System.out.println("threads---->"+threads);
        System.out.println("id---->"+id);
        threads = threads.replace("\"","");
        int count =  timer_type_configMapper.updateThreadsById(threads,id);

        Map<String,Object> map = new HashedMap();

        if(count > 0) {
            log.info("启用定时器成功！");

        }else{
            log.info("启用定时器失败！");
        }

        map.put("count",count);

        return map;

    }


    /**
     * 修改Runtime
      * @param runtime
     * @param id
     * @return
     */
    public Map<String,Object> updateRuntime(String runtime,Integer id){

        System.out.println("runtime---->"+runtime);
        System.out.println("id---->"+id);
        runtime = runtime.replace("\"","");
        int count =  timer_type_configMapper.updateRuntimeById(runtime,id);

        Map<String,Object> map = new HashedMap();

        if(count > 0) {
            log.info("启用定时器成功！");

        }else{
            log.info("启用定时器失败！");
        }

        map.put("count",count);

        return map;

    }




}
