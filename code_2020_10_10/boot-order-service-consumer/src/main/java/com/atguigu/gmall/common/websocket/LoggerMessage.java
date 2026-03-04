package com.atguigu.gmall.common.websocket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2019-9-27
 */
/*
 * 日志消息实体类，这里用了lombok插件
 * 没有安装该插件的话，就手动添加get、set方法、toString方法
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
public class LoggerMessage
{
    private String body;
    private String timestamp;
    private String threadName;
    private String className;
    private String level;
    private String exception;
    private String cause;

    public LoggerMessage(){}
}