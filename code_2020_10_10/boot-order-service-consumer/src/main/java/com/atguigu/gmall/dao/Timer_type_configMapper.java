package com.atguigu.gmall.dao;

import com.atguigu.gmall.entity.Timer_type_config;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface Timer_type_configMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Timer_type_config record);

    int insertSelective(Timer_type_config record);

    Timer_type_config selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Timer_type_config record);

    int updateByPrimaryKey(Timer_type_config record);

    Timer_type_config selectBytype(@Param("type") String type);

    List<Timer_type_config> selectByStatusTpye(List<String> togetherlist);



    //map 对定时器进行查询“：
    List<Timer_type_config>   queryimerTypeConfig(@Param("jobname") String jobname) ;

    List<Timer_type_config>   queryimerTypeConfigByNull() ;

    //map 对于每一个字段对应相应的内容的key,value,便于根据字段取值：
    public Map<String,Object> getByPrimaryKey(Integer id) ;



    //map 对定时器的timetask更新“：
    public  int updateTimeTpyeConfigBytimetask(@Param("id") Integer id,@Param("timetask") String timetask) ;


    //map 对定时器的ids更新“：
    public  int updateTimeTpyeConfigByids(@Param("id") Integer id,@Param("ids") String ids) ;

    //map 对定时器的timetask更新“：
    public  int updateTimeTpyeConfigByall(Timer_type_config record,@Param("id") Integer id) ;



    //map 对定时器的timetask更新“：
    public  int updateTimerTypeConfigBydeletestate(@Param("deletestate") Integer deletestate,@Param("id") Integer id) ;

    //根据id,禁用定时器:
    int updateBydisableId(@Param("id") Integer id);

    //根据id,启用定时器:
    int updateByableId(@Param("id") Integer id);

    //根据id,删除定时器:
    int deletestateById(@Param("id") Integer id);


    //map 对定时器的Type更新“：
    public  int updateTypeById(@Param("type") String type,@Param("id") Integer id) ;


    //map 对定时器的Threads更新“：
    public  int updateThreadsById(@Param("threads") String threads,@Param("id") Integer id) ;


    //map 对定时器的runtime更新“：
    public  int updateRuntimeById(@Param("runtime") String runtime,@Param("id") Integer id) ;

}