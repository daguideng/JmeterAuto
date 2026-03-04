package com.atguigu.gmall.dao;

import com.atguigu.gmall.entity.Report_infor;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;


@Mapper
@Component
public interface Report_inforMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Report_infor record);

    int insertSelective(Report_infor record);

    Report_infor selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Report_infor record);

    int updateByPrimaryKey(Report_infor record);
}