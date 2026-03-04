package com.atguigu.gmall.service.impl;

import com.atguigu.gmall.common.bean.response.Result;
import com.atguigu.gmall.common.constant.RES_STATUS;
import com.atguigu.gmall.controller.login.TokenUtil;
import com.atguigu.gmall.dao.UserMapper;
import com.atguigu.gmall.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Map;


/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2019-8-9
 */
@Slf4j
@Service
public class LoginServerImpl {



    public static Map<String,String> maptoken = new HashedMap();


    @Autowired
    private UserMapper userMapper ;

   // final Base64.Decoder decoder = Base64.getDecoder();
   // final Base64.Encoder encoder = Base64.getEncoder();

    //登录验证：
    public Result<?> loginCheck(User user,HttpSession session) throws Exception{

        Map<String,Object> map = new HashedMap();

        Result <Map<String, Object>> result = new Result <>();


        String username = user.getUsername().trim();

        String userpwd = user.getPassword().trim();

        System.out.println("userpwd--->"+userpwd);

    //    byte[] encodedText = userpwd.getBytes("UTF-8");

        //解码  用户提交的userpwd
     //   userpwd = new String(decoder.decode(encodedText), "UTF-8");

        //数据库的pwd:
        System.out.println("username-->"+username);
        User dbuser =  userMapper.selectUserByUsername(username);
        if("".equals(dbuser) || null == dbuser)
        {
            map.put("msg","用户名不存在,请先注册用户!");
            result.setData(map);
            result.setStatus(RES_STATUS.AUTH_INVALID_USER);
            log.info("$result{}," +result);
            return result ;
        }
        String dbpwd = dbuser.getPassword();

    //    byte[] dbencodedText = dbpwd.getBytes("UTF-8");
    //    dbpwd = new String(decoder.decode(dbencodedText), "UTF-8");

        //1.以用户名查询出的pwd进行比较，如果相同则通过
        if(userpwd.equals(dbpwd)){
        //    String urlLogin = "/system/token/login";
            String token = TokenUtil.createToken(username,userpwd);
          //  map.put("token",token);
            map.put("message","sucess");
            map.put("code",20000);

            Map<String,Object> mapdata = new HashedMap();
            mapdata.put("token",token);

            map.put("data",mapdata);

            result.setData(map);

            //生成静态变量为getUserInfo接口进行验证：
            String[] array = {username,userpwd};
            maptoken.put(token,array.toString());
            log.info("$result{}," +result);

            session.setAttribute("username", username);
            session.setAttribute("email", dbuser.getEmailaddress());


            return result ;
        }else{
            map.put("msg","密码不正确");
            result.setData(map);
            result.setStatus(RES_STATUS.USERPWD_IS_ERROR);
            log.info("$result{}," +result);
            return result ;
        }


    }
}
