package com.atguigu.gmall.common.bean;

import java.io.Serializable;
public class ResponseResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**响应码 */
    private int code;

    /**响应消息*/
    private String msg;
    /**
     * 返回数据
     */
    private T data;

    /**
     * 根据枚举生成对象类型
     */
    public ResponseResult(ResponseMeta meta) {
        this.code=meta.getCode();
        this.msg=meta.getMsg();
    }
    /**
     * 根据枚举生成对象类型
     */
    public ResponseResult(ResponseMeta meta,T data) {
        this.code=meta.getCode();
        this.msg=meta.getMsg();
        this.data=data;
    }
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public ResponseResult setData(T result) {
        this.data = result;
        return this;
    }

}