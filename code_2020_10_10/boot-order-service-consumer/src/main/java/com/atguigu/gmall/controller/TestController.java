package com.atguigu.gmall.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2019-10-12
 */

@RestController
public class TestController {


    @RequestMapping("/test")
    public String test(){

        return null;
    }


}
