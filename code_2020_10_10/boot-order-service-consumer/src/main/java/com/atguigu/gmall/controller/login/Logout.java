package com.atguigu.gmall.controller.login;

import com.atguigu.gmall.common.bean.response.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2019-8-9
 */
@Slf4j
@RestController
public class Logout {




    /**
     * 用户登录:
     */
    @RequestMapping(value = "/user/logout", method = RequestMethod.POST)
    public Result<?> list(HttpSession session)
            throws Exception {


        Result <Map<String, Object>> result = new Result <>();

        Map <String, Object> map = new HashedMap();

        map.put("code",20000);
        map.put("message","logout sucess");


        //1.token 清空:
        result.setData(map);

        ObjectMapper objectMapper = new ObjectMapper();
        log.info("logout{} ", objectMapper.writeValueAsString(result));

        return result;
    }



}
