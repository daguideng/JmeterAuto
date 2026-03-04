package com.atguigu.gmall.controller.login;


import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall.activemq.PublishController;
import com.atguigu.gmall.activemq.ThreadByRunnableSendMq;
import com.atguigu.gmall.common.bean.response.Result;
import com.atguigu.gmall.common.constant.RES_STATUS;
import com.atguigu.gmall.dao.UserMapper;
import com.atguigu.gmall.entity.User;
import com.atguigu.gmall.service.impl.LoginServerImpl;
import com.atguigu.gmall.zookeeperip.DyIPaddressPubicProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2019-8-9
 */
@Slf4j
@RestController
public class Login {



    @Autowired
    private LoginServerImpl loginServer;


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DyIPaddressPubicProvider dyIPaddressPubicProvider;
    @Autowired
    private PublishController publishController;


    /**
     * 用户登录:
     */
    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public Result <?> list(@RequestBody User user, HttpSession session, HttpServletResponse response)
            throws Exception {

        // session.setAttribute("username",user.getId());

        Result <Map <String, Object>> result = new Result <>();

        if (null != user || !"".equals(user)) {
            System.out.println("user--->" + user);
            Map <String, Object> map = new HashedMap();
            if (null == user.getUsername() || "".equals(user.getUsername().trim())) {
                map.put("msg", "用户名为空");
                result.setStatus(RES_STATUS.USERNAME_IS_NULL);
                result.setData(map);

                log.info("$result{}", result);
                return result;

            }
            if (null == user.getPassword() || "".equals(user.getPassword().trim())) {

                map.put("msg", "用户密码为空");
                result.setStatus(RES_STATUS.USERPWD_IS_NULL);
                result.setData(map);
                log.info("$result{}", result);
                return result;
            }


        }


        //请求数据库验证：
        result = (Result <Map <String, Object>>) loginServer.loginCheck(user, session);

        log.info("$result{}", result);

        //登录成功则发消息：检查jmenter的node是否已启动：
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("runJmeterAgentCheck", "runJmeterAgentCheck");
        jsonObj.put("consumerIp", dyIPaddressPubicProvider.getConsumerOsIpaddress());
        jsonObj.put("runusername","admin");
        jsonObj.toJSONString();
        ThreadByRunnableSendMq thread = new ThreadByRunnableSendMq(publishController, jsonObj);
        new Thread(thread, "发送:检查jmeter代理服务器检查消息......").start();


        return result;
    }





    /**
     * 用户登录:
     */
    @RequestMapping(value = "/userlogin/login", method = RequestMethod.POST)
    public Result <?> listSecond(@RequestBody User user, HttpSession session, HttpServletResponse response)
            throws Exception {

        // session.setAttribute("username",user.getId());

        Result <Map <String, Object>> result = new Result <>();

        if (null != user || !"".equals(user)) {
            System.out.println("user--->" + user);
            Map <String, Object> map = new HashedMap();
            if (null == user.getUsername() || "".equals(user.getUsername().trim())) {
                map.put("msg", "用户名为空");
                result.setStatus(RES_STATUS.USERNAME_IS_NULL);
                result.setData(map);

                log.info("$result{}", result);
                return result;

            }
            if (null == user.getPassword() || "".equals(user.getPassword().trim())) {

                map.put("msg", "用户密码为空");
                result.setStatus(RES_STATUS.USERPWD_IS_NULL);
                result.setData(map);
                log.info("$result{}", result);
                return result;
            }


        }


        //请求数据库验证：
        result = (Result <Map <String, Object>>) loginServer.loginCheck(user, session);

        log.info("$result{}", result);


        return result;
    }







    /**
     * 用户有效性验证:
     */
    @RequestMapping(value = "/user/login/uservalidate", method = RequestMethod.POST)
    public Result <?> list(@RequestParam(required = false) String username)
            throws Exception {

        Map <String, Object> map = new HashedMap();
        Result <Map <String, Object>> result = new Result <>();

        StringBuffer sb = new StringBuffer();


        if ("".equals(username) || null == username) {
            map.put("username", "用户名不能为空");
            result.setData(map);
            result.setStatus(RES_STATUS.AUTH_INVALID_USER);
            log.info("$result{}," + result);
            sb.setLength(0);
            result.setData(map);
            return result;
        }
        try {

            User dbuser = userMapper.selectUserByUsername(username);
            String usernameDb = dbuser.getUsername();
            sb.append(usernameDb);
            map.put("username", sb.toString());
            result.setData(map);
            sb.setLength(0);
            return result;

        } catch (Exception e) {

            map.put("username", "用户名不存在");
            result.setData(map);
            result.setStatus(RES_STATUS.AUTH_INVALID_USER);
            log.info("$result{}" + result);
            result.setData(map);
            sb.setLength(0);
            return result;

        }


    }


}
