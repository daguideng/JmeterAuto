package com.atguigu.gmall.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2019-8-23
 */

@RestController
@RequestMapping("/wang")
public class WangController {



    @PostMapping("/test")
    public String test(Integer page,Integer limit){

        System.out.println(page);
        System.out.println(limit);

        return "123";
    }
}
