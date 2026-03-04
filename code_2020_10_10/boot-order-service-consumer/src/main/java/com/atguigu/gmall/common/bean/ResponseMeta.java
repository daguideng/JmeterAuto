package com.atguigu.gmall.common.bean;

/**
 *
 * @ClassName: ResponseMeta
 * @Description: 响应元数据
 * @date 2018年4月13日
 *
 */
public enum ResponseMeta {
    //成功
    SUCCESS(0,9000000,"操作成功"),

    //公共异常
    INTERNAL_SERVER_ERROR(10000,9010000, "出了点问题，请稍后再试"),//未知错误
    BAD_REQUEST(10001,9010001,"请求Json字符串无法解析"),
    METHOD_ARGUMENT_NOT_VALID(10002, 9010002,"请求参数不匹配"),
    METHOD_PARAM_NOT_VALID(10003,9010003,"请求参数未通过校验 "),
    MTHOD_TYPE_NOT_VALID(10004,9010004,"请求方法不正确"),
    CONTENT_TYPE_NOT_SUPPORTED(10005,9010005,"请求content-type不正确"),

    ENTITY_DUPLICATED(10011,9010011,"已经存在相应实体"),
    ENTITY_NOT_FOUND(10012,9010012,"无法找到相应实体"),

    //文件类
    UPLOAD_SIZELIMIT_EXCEPTION(20006,9020006,"上传文件不能超过50M"),
    DOWNLOAD_EXCEL_FAILED(20007,9020007, "下载文件失败"),
    READ_FILE_ERROR(20008,9020008,"读取文件失败"),
    WRITE_FILE_ERROR(20009,9020009,"写入文件失败"),
    FILE_NOT_FOUND(20010,9020010,"文件未找到"),
    TASK_DOWN_ERROR(20011,9020011,"任务已成功，请勿重复提交"), //后台执行批量任务时不允许过快提交

    //性能测试相关:
    NODE_NO_RUN_ERROR(80011,19020000,"Jmeter节点未运行"),
    PERF_EPORT_NO_FOUND(80012,19020001,"未生成结果报告"),
    PERF_UPLOAD_File_FORMAT_ERROR(80013,19020002,"上传文件格式不正确"),

    //业务相关错误：用户认证授权相关异常
    UNKNOWN_ACCOUNT(30001,9030001,"账户不存在"),
    INCORRECT_CREDENTIAL(30002,9030002,"认证信息不正确，请重新输入密码"),
    LOCKED_ACCOUNT(30003,9030003,"账户被锁,请联系管理员"),
    AUTHENTICATION_FAILED(30004,9030004,"验证不通过，请联系管理员"),
    PASSWORD_EXPIRED(30005,9030005,"密码已过期无法登录系统,请联系管理员"),
    NO_OPERATION_AUTHORITY(30006,9030006,"用户无权进行此操作"),
    ;
    private final Integer code; // 响应码
    private final Integer monitorCode; //运维监控码
    private final String msg; //响应消息

    private ResponseMeta(Integer code, Integer monitorCode, String msg) {
        this.code=code;
        this.monitorCode=monitorCode;
        this.msg=msg;
    }


    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Integer getMonitorCode() {
        return monitorCode;
    }

    @Override
    public String toString() {
        String s = "[code:"+code+", monitorCode:"+monitorCode+",msg:"+msg+"]";
        return s;
    }
}