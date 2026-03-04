package com.atguigu.gmall.common.exception;

/**
 * 业务异常类
 */
public class MarsException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 5100417370834652973L;
    public MarsException() {
    }
    /**
     * @param message
     * @param cause
     */
    public MarsException(String message, Throwable cause) {
        super(message, cause);
    }
    /**
     * @param message
     */
    public MarsException(String message) {
        super(message);
    }
    /**
     * @param cause
     */
    public MarsException(Throwable cause) {
        super(cause);
    }
}