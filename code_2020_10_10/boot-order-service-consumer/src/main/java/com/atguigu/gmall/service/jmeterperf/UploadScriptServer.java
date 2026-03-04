package com.atguigu.gmall.service.jmeterperf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall.Impl.perfImpl.JmeterPerRunStatus;
import com.atguigu.gmall.dao.Upload_infoMapper;
import com.atguigu.gmall.entity.Upload_info;
import com.atguigu.gmall.entity.User;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: dengdagui
 * @Description:  上传脚本服务层：
 * @Date: Created in 2018-7-5
 */
@Slf4j
@Service
public class UploadScriptServer {




    @Autowired
    private Upload_infoMapper upload_infoMapper ;

    public void insert(Upload_info record){

        List list = new ArrayList<Upload_info>() ;

        System.out.println("record--->"+record);

        String signString= JSON.toJSONString(record);
        System.out.println("json_test---->"+signString);



        int insert = upload_infoMapper.insert(record);

        if(insert >0){

            log.info("上传脚本,相关信息入库成功!");
        }else {
            log.error("上传脚本,相关信息入库失败!");
        }


    }


    public List<Upload_info> getSelectAllById(Integer id){
        log.info("开始查询upload_info表，id={}", id);
        
        // 先尝试直接SQL查询来诊断问题
        try {
            Map<String, Object> resultMap = upload_infoMapper.getByPrimaryKey(id);
            if (resultMap != null) {
                log.info("直接SQL查询结果: {}", resultMap);
                log.info("lastruntime字段值: {}", resultMap.get("lastruntime"));
                log.info("所有字段: {}", resultMap.keySet());
            } else {
                log.warn("直接SQL查询返回空结果，id={}", id);
            }
        } catch (Exception e) {
            log.error("直接SQL查询失败: {}", e.getMessage());
        }

        Upload_info uploadInfo = upload_infoMapper.selectByPrimaryKey(id);
        List<Upload_info> uploadinfor = new ArrayList<>();
        if (uploadInfo != null) {
            log.info("MyBatis查询结果 - id: {}, scriptname: {}, lastruntime: {}", 
                    uploadInfo.getId(), uploadInfo.getScriptname(), uploadInfo.getLastruntime());
            uploadinfor.add(uploadInfo);
        } else {
            log.warn("MyBatis查询返回空结果，id={}", id);
        }

        return  uploadinfor ;
    }


    /**
     * 以id为查询条件，得到key,value结果
     * @param id
     * @return
     */
    public Map<String,Object> getByPrimaryKeyServer(Integer id){

      Map<String,Object>  mapkey    =  upload_infoMapper.getByPrimaryKey(id);

        return  mapkey ;
    }


    /**
     * id为更新数据库条件，更新性能执行顺序：
     */
    public Map<String,Object> updateScriptOrder(String upload ){


         JSONObject job = JSONObject.parseObject(upload);
         Integer id = Integer.valueOf((String)job.get("id"));
         String uploadtime = (String) job.get("uploadtime");
         String lastruntime = (String) job.get("lastruntime");
         String username = (String) job.get("username");
         String scriptname = (String) job.get("scriptname");
         String interfacename = (String) job.get("interfacename");
         String scripttype = (String) job.get("scripttype");
         String operationtype = (String) job.get("operationtype");
         String scriptpath = (String) job.get("scriptpath");
         String runbutton = (String) job.get("runbutton");
         String scriptrunorder = (String) job.get("scriptrunorder");
         String testname0 = (String) job.get("testname0");
         String testname1 = (String) job.get("testname1");
         String testname2 = (String) job.get("testname2");
         String testname3 = (String) job.get("testname3");
         String urL0 = (String) job.get("urL0");
         String urL1 = (String) job.get("urL1");
         String urL2 = (String) job.get("urL2");
         String urL3 = (String) job.get("urL3");
         String urL0replace = (String) job.get("urL0replace");
         String urL1replace = (String) job.get("urL1replace");
         String urL2replace = (String) job.get("urL2replace");
         String urL3replace = (String) job.get("urL3replace");


        Upload_info   recond = new Upload_info(id,uploadtime,lastruntime,username,scriptname,interfacename,
                scripttype,operationtype,scriptpath,runbutton,scriptrunorder,testname0,testname1,
                testname2,testname3,urL0,urL1,urL2,urL3,urL0replace,urL1replace,urL2replace,urL3replace);


            Map<String,Object>  mappar = new HashMap <>() ;
            Map<String,Object>  json = new HashMap <>() ;
            int updateResult = upload_infoMapper.updateByPrimaryKeySelective(recond);
            if(updateResult == 1){
                mappar.put("code",0);
                mappar.put("data","提交成功");
                mappar.put("msg","系统成功响应");
                json.put("result",mappar);

            }else{
                mappar.put("code",-1);
                mappar.put("data","提交失败");
                mappar.put("msg","系统成功响应");
                json.put("result",mappar);
            }

            return json ;

    }


    /***
     * 返回最后一条上传脚本信息，目的是返回上传相关消息：
     */
    public Upload_info  selectOrderByLimit(){

        Upload_info   recond  = upload_infoMapper.selectOrderByLimit();

        return recond ;

    }




    /**
     * 性能测试不同状态随时更新数据库的配置状态
     */
    public void updatePerfRunStates(String state,int id,String runstate,String perflastruntime){

        int result = 0 ;
        if(runstate.equalsIgnoreCase(JmeterPerRunStatus.Perf_Run)){
            result =  upload_infoMapper.updatePerfRunState(state,"",id);
        }else{
            result =  upload_infoMapper.updatePerfRunState(state,perflastruntime,id);
        }


        if(result >0){
            log.info("更新配置状态 "+state+" 成功!");
        }else{
            log.info("更新配置状态 "+state+" 失败!");
        }

    }


    /**
     * 接口测试不同状态随时更新数据库的配置状态
     */
    public void updateInterRunStates(String state,int id,String runstate,String lastruntime){

        int result = 0 ;
        if(runstate.equalsIgnoreCase(JmeterPerRunStatus.Inter_Run)){
            result =  upload_infoMapper.updateInterRunState(state,"",id);
        }else{
            result =  upload_infoMapper.updateInterRunState(state,lastruntime,id);
        }


        if(result >0){
            log.info("更新配置状态 "+state+" 成功!");
        }else{
            log.info("更新配置状态 "+state+" 失败!");
        }

    }


    /**
     * 查询当前运行状态的行，停止时所有运行状态的行都更新为：stop状态:
     */
    public List<Upload_info> updateStopByRunId(String runState){

        List<Upload_info> record = upload_infoMapper.selectByRunState(runState);

        return record ;

    }






    /**
     * id为更新数据库条件，更新性能执行顺序：
     */
    public Map<String,Object> updateScriptOrder(Upload_info uploadinfo){


        User user = new User() ;

        Upload_info upload = new Upload_info();

        upload.setId(uploadinfo.getId());

        upload.setUploadtime(upload.getUploadtime());

        if("".equals(uploadinfo.getLastruntime())){

            Date date = new Date();
            SimpleDateFormat sdformat  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String LgTime = sdformat.format(date);
            upload.setLastruntime(LgTime);
        }else{
            upload.setLastruntime(uploadinfo.getLastruntime());

        }

        upload.setUsername(user.getUsername());
        upload.setScriptname(uploadinfo.getScriptname());
        upload.setInterfacename(uploadinfo.getInterfacename());
        upload.setScripttype(uploadinfo.getScripttype());
        upload.setOperationtype(uploadinfo.getOperationtype());
        upload.setScriptpath(uploadinfo.getScriptpath());
        upload.setRunbutton(uploadinfo.getRunbutton());
        upload.setScriptrunorder(uploadinfo.getScriptrunorder());
        upload.setTestname0(uploadinfo.getTestname0());
        upload.setTestname1(uploadinfo.getTestname1());
        upload.setTestname0(uploadinfo.getTestname0());
        upload.setTestname3(uploadinfo.getTestname3());

        upload.setUrl0(uploadinfo.getUrl0());
        upload.setUrl1(uploadinfo.getUrl1());
        upload.setUrl2(uploadinfo.getUrl2());
        upload.setUrl3(uploadinfo.getUrl3());


        upload.setUrl0replace(uploadinfo.getUrl0replace());
        upload.setUrl1replace(uploadinfo.getUrl1replace());
        upload.setUrl2replace(uploadinfo.getUrl2replace());
        upload.setUrl3replace(uploadinfo.getUrl3replace());



        Map<String,Object>  map = new HashMap <>() ;
        Map<String,Object>  json = new HashMap <>() ;
        int updateResult = upload_infoMapper.updateByPrimaryKeySelective(uploadinfo);
        if(updateResult == 1){

            map.put("code",0);
            map.put("data","更新数据库成功");
            map.put("msg","系统成功响应");
            json.put("result",map);

        }else{
            map.put("code",10001);
            map.put("data","更新数据库失败");
            map.put("msg","系统成功响应");
            json.put("result",map);


        }

        return json ;

    }


    /**
     * 对数据进行逻辑删除
     */
    public Map<String,Object> deleteScriptByKey(int id){

        Map<String,Object> map = new HashMap <>();

        Map<String,Object> resultmap = new HashMap <>();

        int result =  upload_infoMapper.deleteByPrimaryKey(id);


        if(result >0){
            log.info("删除脚本id{} "+id+" 成功!");
            map.put("code",0);
            map.put("msg","删除脚本成功");
            resultmap.put("result",map);
        }else{
            log.info("删除脚本id{} "+id+" 失败!");
            map.put("code",-1);
            map.put("msg","删除脚本失败");
            resultmap.put("result",map);
        }

        return resultmap ;

    }


}
