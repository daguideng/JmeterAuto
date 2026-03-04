package com.atguigu.gmall.common.bean.response;


import com.atguigu.gmall.common.constant.RES_STATUS;

/**
 * 对外返回值统一封转，由此转为json
 * Result
 * @param <T>
 */
public class Result<T> {

    /**
	 * 对外返回的对象
	 */
    private T data;

    /**
	 * 返回状态码
	 */
    private int code;

    /**
	 * 返回消息
	 */
    private String msg;


    public Result() {
        super();
        this.code = 0;
        this.msg = "success";
    }

    public Result(RES_STATUS status) {
        super();
        this.code = status.code;
        this.msg = status.msg;
        if (status == RES_STATUS.SUCCESS) {
            this.msg = status.name();
        }
    }

    public Result(int code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }

    public Result(T data, int code, String msg) {
        super();
        this.data = data;
        this.code = code;
        this.msg = msg;
    }

    public void setStatus(RES_STATUS status) {
        this.code = status.code;
        this.msg = status.msg;
    }

    public T getData() {
        return this.data;
    }

    public Result<T> setData(T data) {
        this.data = data;
        return this;
    }

    public Result<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public int getCode() {
        return this.code;
    }

    public Result<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public String getMsg() {
        return this.msg;
    }



	/**
	 * 服务器unix utc时间戳秒值
	 *
	 * @return
	 */
    public long getTimestamp() {
        return System.currentTimeMillis() / 1000;
    }


	
}
