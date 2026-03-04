package com.atguigu.gmall.Impl.perfImpl;

import org.springframework.stereotype.Component;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2018-8-17
 */
@Component
public class CustomRunException extends RuntimeException {

    public CustomRunException(){}

    public CustomRunException(String msg){
        super(msg);
    }
}
