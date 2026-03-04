package com.atguigu.gmall.dao;

import com.atguigu.gmall.common.page.Criterion;
import com.atguigu.gmall.entity.Upload_info;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface Upload_infoMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Upload_info record);

    int insertSelective(Upload_info record);

    Upload_info selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Upload_info record);

    int updateByPrimaryKey(Upload_info record);




    List <Upload_info> queryApiExecuteSumCount(@Param("criterions") List<Criterion> criterions);

    PageList<Map<String, Object>> queryApiExecuteLog4Manage(@Param("criterions") List<Criterion> criterions, PageBounds pb);


    //map 对于每一个字段对应相应的内容的key,value,便于根据字段取值：
    public Map<String,Object> getByPrimaryKey(Integer id) ;


    //返回最后一条数据:
    public Upload_info selectOrderByLimit() ;

    //返回所有运行状态的数据:
    public List<Upload_info> selectByRunState(@Param("state") String state) ;


    //map 对性能测试运行不同状态随时更新“：
    public  int updatePerfRunState(@Param("state") String status,@Param("perflastruntime") String lastruntime,@Param("id") int jmeterScriptName) ;

    //map 对接口测试运行不同状态随时更新“：
    public  int updateInterRunState(@Param("interstate") String status,@Param("lastruntime") String lastruntime,@Param("id") int jmeterScriptName) ;

    public  int deleteScriptState(@Param("id") Integer id) ;

    //返回所有的数据为前台分页等做准备:
    public List<Upload_info> selectAllByPage() ;


}