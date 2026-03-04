package com.atguigu.gmall.controller.useradmin;

import com.atguigu.gmall.common.bean.response.Result;
import com.atguigu.gmall.common.constant.RES_STATUS;
import com.atguigu.gmall.common.utils.StringUtils;
import com.atguigu.gmall.dao.UserMapper;
import com.atguigu.gmall.entity.User;
import com.atguigu.gmall.service.impl.UserServerImpl;
import java.util.HashMap;
import java.util.Map;
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
 * @Description:用户新增 修改密码 启用禁用 查询等操作
 * @Date: Created in 2019-9-20
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class useradmin {




    @Autowired
    private UserServerImpl userServerImpl;


    @Autowired
    private UserMapper userMapper;

    /**
     * 新增用户:
     *
     * @param user
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    @ResponseBody
    Result <?> addUser(@RequestBody User user) throws Exception {
        Result <Object> result = new Result <>();
        log.info("新增用户 user {}", user);

        if ("".equals(user.toString().trim()) || null == user) {
            log.info(RES_STATUS.BAD_PARAM_NULL.msg);
            Map <String, Object> map = new HashMap <>();
            map.put("user", user);
            result.setStatus(RES_STATUS.BAD_PARAM_NULL);
            result.setData(map);
            return result;
        }

        Map <String, Object> map = new HashedMap();

        StringBuffer sb = new StringBuffer();
        User dbuser = userMapper.selectUserByUsername(user.getUsername());

        if( null != dbuser && !"".equals(dbuser)) {
            String usernameDb = dbuser.getUsername();
            sb.append(usernameDb);
            map.put("username", sb.toString());
            map.put("msg", usernameDb+"用户在数据库中已存在!");
            map.put("count",0);
            result.setData(map);
            sb.setLength(0);
            return result;
        }


        User   adduser = new User();


        String passwd = user.getPassword();
        String passwdMd5 = StringUtils.string2Md5(passwd);
        System.out.println("passwdMd5----------->"+passwdMd5);


        adduser.setUsername(user.getUsername());
        //密码是简单的md5(passwd)
        adduser.setPassword(passwdMd5);
        adduser.setEmailaddress(user.getEmailaddress());

        log.info("user------------>"+user);
        Map runResult = userServerImpl.addUser(adduser);
        result.setData(runResult);

        return result;

    }


    /**
     * 修改Email：
     *
     * @param id
     * @param email
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updateEmail", method = RequestMethod.POST)
    @ResponseBody
    Result <?> updateEmail(@RequestParam(required = true) Integer id, @RequestParam(required = true) String email) throws Exception {
        Result <Object> result = new Result <>();
        log.info("修改id: id {},email{}", id, email);

        if ("".equals(email.toString().trim()) || null == email) {
            log.info(RES_STATUS.BAD_PARAM_NULL.msg);
            Map <String, Object> map = new HashMap <>();
            map.put("email", email);
            result.setStatus(RES_STATUS.BAD_PARAM_NULL);
            result.setData(map);
            return result;
        }

        Map runResult = userServerImpl.updateEmail(id, email);
        result.setData(runResult);

        return result;

    }


    /**
     * 禁用用户状态：
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/disableUser", method = RequestMethod.POST)
    @ResponseBody
    Result <?> disableUser(@RequestParam(required = true) Integer id) throws Exception {
        Result <Object> result = new Result <>();
        log.info("禁用id: id {}", id);

        if ("".equals(id.toString().trim()) || null == id) {
            log.info(RES_STATUS.BAD_PARAM_NULL.msg);
            Map <String, Object> map = new HashMap <>();
            map.put("id", id);
            result.setStatus(RES_STATUS.BAD_PARAM_NULL);
            result.setData(map);
            return result;
        }

        Map runResult = userServerImpl.disableUser(id);
        result.setData(runResult);

        return result;

    }


    /**
     * 启用有效用户：
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/enableUser", method = RequestMethod.POST)
    @ResponseBody
    Result <?> enableUser(@RequestParam(required = true) Integer id) throws Exception {
        Result <Object> result = new Result <>();
        log.info("启用id: id {}", id);

        if ("".equals(id.toString()) || null == id) {
            log.info(RES_STATUS.BAD_PARAM_NULL.msg);
            Map <String, Object> map = new HashMap <>();
            map.put("Id", id);
            result.setStatus(RES_STATUS.BAD_PARAM_NULL);
            result.setData(map);
            return result;
        }

        Map runResult = userServerImpl.enableUser(id);
        result.setData(runResult);

        return result;

    }


    /**
     * 修改用户密码：
     *
     * @param id
     * @param password
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updatePwd", method = RequestMethod.POST)
    @ResponseBody
    Result <?> updatePwd(@RequestParam(required = true) Integer id, @RequestParam(required = true) String password) throws Exception {
        Result <Object> result = new Result <>();
        log.info("修改用户 id{},password{}", id, password);

        if ("".equals(password.toString()) || null == password) {
            log.info(RES_STATUS.BAD_PARAM_NULL.msg);
            Map <String, Object> map = new HashMap <>();
            map.put("password", password);
            result.setStatus(RES_STATUS.BAD_PARAM_NULL);
            result.setData(map);
            return result;
        }

        String passwdMd5 = StringUtils.string2Md5(password);

        Map runResult = userServerImpl.updatePwd(id, passwdMd5);
        result.setData(runResult);

        return result;

    }



    @RequestMapping(value = "/queryUserInfo", method = RequestMethod.POST)
    @ResponseBody
    Result <?> queryUserInfo(@RequestParam(required = false) String username) throws Exception {
        Result <Object> result = new Result <>();
        log.info("查询用户名为:username{}", username);

        Map runResult = userServerImpl.queryUserInfo(username);
        result.setData(runResult);

        return result;

    }




    /**
     * 新增用户有效性验证:(新增用户时不能用户名相同的)
     */
    @RequestMapping(value = "/check/adduser", method = RequestMethod.POST)
    @ResponseBody
    public Result <?> list(@RequestParam(required = true) String username)
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
            if( null != usernameDb && !"".equals(usernameDb)) {
                sb.append(usernameDb);
                map.put("username", sb.toString());
                result.setData(map);
                sb.setLength(0);

            }
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
