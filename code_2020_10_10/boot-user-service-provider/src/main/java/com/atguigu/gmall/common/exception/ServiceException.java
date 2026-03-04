package com.atguigu.gmall.common.exception;

import com.atguigu.gmall.common.constant.RES_STATUS;

/**
 * 业务异常类，用于服务层异常上抛
 */
public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 2195068675053227207L;

    private int errorCode;

    private String errorMsg;

    public ServiceException(){}

    public ServiceException(RES_STATUS invalidComparisionType) {
        super();
        errorCode = -1;
        errorMsg = "unknown Exception";
    }

    public ServiceException(String message, int errorCode, String errorMsg) {
        super(message+":"+errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public ServiceException(int errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public ServiceException(String message, RES_STATUS status) {
        super(message);
        this.errorCode = status.code;
        this.errorMsg = status.msg;
    }

    /**
     * @param //status
     */
//    public ServiceException(RES_STATUS status) {
//        super(status.name());
//        this.errorCode = status.code;
//        this.errorMsg = status.msg;
//    }


    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public RES_STATUS getResStatus(){
        RES_STATUS status = RES_STATUS.findStatusByCode(errorCode);
        if(null != status){
            return status;
        }
        return RES_STATUS.SERVER_ERROR;
    }

}
