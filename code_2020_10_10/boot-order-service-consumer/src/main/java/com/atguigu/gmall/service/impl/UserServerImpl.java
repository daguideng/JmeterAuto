package com.atguigu.gmall.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.atguigu.gmall.common.utils.MyJsonUtil;
import com.atguigu.gmall.dao.UserMapper;
import com.atguigu.gmall.entity.User;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: dengdagui
 * @Description:用户新增 修改密码 启用禁用 查询等操作
 * @Date: Created in 2019-9-19
 */
@Slf4j
@RestController
public class UserServerImpl {



    @Autowired
    private UserMapper userMapper;

    //1. 增加用户
    public Map <String, Object> addUser(User record) {

        int count = userMapper.insert(record);

        Map <String, Object> map = new HashedMap();

        if (count > 0) {
            log.info("新增用户入库成功！");

        } else {
            log.info("新增用户入库失败！");
        }

        map.put("count", count);

        return map;

    }


    //2. 修改email

    /***
     * 修改email
     * @param id
     * @param email
     * @return
     */
    public Map <String, Object> updateEmail(Integer id, String email) {

        Map <String, Object> map = new HashedMap();

        User timer_type_config = null;

        int count = userMapper.updateEmailById(id, email);
        if (count > 0) {
            timer_type_config = userMapper.selectByPrimaryKey(id);
            log.info("更新 " + email + " 成功!");
        } else {
            log.info("更新 " + email + " 失败!");
        }

        map.put("count", count);
        map.put("data", MyJsonUtil.object_to_json(timer_type_config));

        return map;

    }


    //3. 禁用用户

    /**
     * 禁用
     *
     * @param id
     * @return
     */
    public Map <String, Object> disableUser(Integer id) {

        int countx = userMapper.updateBydisableId(id);

        Map <String, Object> map = new HashedMap();

        if (countx > 0) {
            log.info("禁用用户成功！");

        } else {
            log.info("禁用用户失败！");
        }

        map.put("count", countx);

        return map;

    }


    /**
     * 启用定时器
     *
     * @param id
     * @return
     */
    public Map <String, Object> enableUser(Integer id) {

        int count = userMapper.updateByenableId(id);

        Map <String, Object> map = new HashedMap();

        if (count > 0) {
            log.info("启用用户成功！");

        } else {
            log.info("启用用户失败！");
        }

        map.put("count", count);

        return map;

    }


    //4. 修改密码
    public Map <String, Object> updatePwd(Integer id, String password) {

        int count = userMapper.updatePwdById(id, password);

        Map <String, Object> map = new HashedMap();

        if (count > 0) {
            log.info("修改用户密码成功！");

        } else {
            log.info("修改用户密码失败！");
        }

        map.put("count", count);

        return map;

    }

    //5. 展示用户信息：
    public Map <String, Object> queryUserInfo(String username) throws UnsupportedEncodingException {

        Map <String, Object> map = new HashedMap();

        List <User> result = null;

        if (null == username || "".equals(username)) {
            result = userMapper.queryUserByNull();
        } else {
            result = userMapper.queryUserByUsername(username);

        }


        if (result.size() > 0) {
            log.info("查询user " + username + " 成功!");
        } else {
            log.info("未查询到数据");
        }

        JSONArray array = JSONArray.parseArray(JSON.toJSONString(result));

        //    String json = MyJsonUtil.object_to_json(result);
        //     System.out.println("json---->"+json);
        //     System.out.println("array---->"+array);
        map.put("json", array);


        return map;

    }

}
