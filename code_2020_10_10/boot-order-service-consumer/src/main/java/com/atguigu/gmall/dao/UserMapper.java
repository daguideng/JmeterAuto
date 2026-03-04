package com.atguigu.gmall.dao;

import com.atguigu.gmall.entity.User;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;


@Mapper
@Component
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

  //  User selectUserByUsername(@Param("username") String username);
   // User selectUserByUsername(String username);

    int updateEmailById(@Param("id") Integer id, @Param("email") String email);

    int updateBydisableId(@Param("id") Integer id );

    int updateByenableId(@Param("id") Integer id);

    int updatePwdById(@Param("id") Integer id,@Param("password") String password);

    List<User> queryUserByNull();

    List<User> queryUserByUsername(@Param("username") String username);

    User selectUserByUsername(String username);
}