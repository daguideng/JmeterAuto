package com.atguigu.gmall.common.constant;

/**
 * RES_STATUS
 *
 */
public  enum RES_STATUS {

	// 运维配置监控描述
	// 原有监控XXX0XXXX, 监控频率降低30%
	// 1101开头, 系统内部故障, 使用原有频率 三分钟50次
	// 1111开头, 用户问题, 使用原有频率  三分钟100次

	SUCCESS(0, "success", 0), //
	FAILED(10001, "", 500), // L
	AOP_FAILED(10002, "AOP_FAILED", 500), // L
	// 服务器异常汇总
	SERVER_ERROR(10003, "服务器开小差了，请稍后再试", 999), // XXX M
	REPEAT_COMMIT(10004, "请勿重复提交", 400), //
	UNEXCEPTED_FAILED(10005, "UNEXCEPTED_FAILED", 997), // XXX M
	DB_FAILED(10006, "数据库异常", 997), // XXX M


	INVALID_DATA(10010, "请求参数无效", 400), // L
	INVALID_REQUEST(10011, "INVALID_REQUEST", 500), // L
	REPEAT_REQUEST(10012, "请勿频繁操作", 500), // L
	REPEAT_SUBMIT(10013, "重复提交", 500), // L
	SIGN_FAIL(10014, "验签失败,请重新登录", 500),
	NET_ERROR(10015, "网络异常", 500), 
	
	BAD_PARAM(400, "参数无效", 400), // L
	BAD_PARAM_NULL(401, "参数不能为空", 400), // L
	INVALID_COMPARISION_TYPE(402, "无效符号", 400),
	INVALID_DATA_EXCEPTION(403, "业务数据异常(含提交数据必填项缺失)", 400),
    
	EXCEPTION_BUT_COULD_IGNORE(10008, "EXCEPTION_BUT_COULD_IGNORE", 500), // L

	LOGIN_PLEASE(100103, "请登录", 400), // L
	LOGIN_BANNED(100104, "拒绝用户登录", 400), // L
	LOGIN_DEVICE(100105, "非绑定设备，拒绝用户登录", 400), // L
	LOGIN_UNBIND(100106, "非绑定设备，请提交解绑申请", 400), // L
	SESSION_OUT(100107, "当前会话已失效，请重新登录", 400), // L
	GET_VERSION_EXCEPTION(100108, "没有相关的版本信息", 400), // L
	DECODE_USER_KEY_ERROR(100109, "解密用户加密串出现异常，请联系管理员", 400), // L

    // auth and user and group
	AUTH_USER_FALIED(20001, "获取用户数据失败", 500), // L
	AUTH_INVALID_USERNAME(20002, "无效的用户名", 400), // L
	AUTH_INVALID_USER(20003, "无效的用户", 400), // L
	AUTH_INVALID_USERGROUP(20004, "无效的用户组", 400), // L
	USERNAME_IS_NULL(20005, "用户姓名为空", 400), // L
	USERPWD_IS_NULL(21005, "密码为空", 400), // L
	USERPWD_IS_ERROR(21005, "密码错误", 400), // L
	ACCOUNT_IS_NULL(20006, "登录用户名为空", 400), // L
	ADMIN_CANNOT_REMOVE(20007, "超级管理员用户，不可删除", 400),
	BAD_PLATFORM(20008, "无效平台信息", 400), // L
	AUTH_NO_AUTH(20009, "您没有权限执行此操作", 400), // L
	BAD_VERSION(20010, "无效版本信息", 400), // L
	LOW_VERSION(20011, "版本过低", 400), // L
	SQL_EXEC_ERROR(20012, "数据库异常", 400), // L
	PRIVILEGE_NOT_EXIST(20013, "权限不存在", 400), // L
	CURRENT_USER_DOSENT_HAS_ROLE(20014, "当前用户无任何（角色）权限", 400),
	CURRENT_USER_HAS_MULTI_ROLE(200141, "当前用户拥有过多角色，请联系管理员进行变更", 400),
	ROLE_NOT_EXIST(20015, "角色不存在", 400), // L
	ORG_NOT_EXIST(20016, "部门不存在", 400), // L
	ACCOUNT_ALREADY_EXIST(20017, "员工号已存在", 400), // L
	ORG_IS_NULL(20018, "部门为空", 400), // L
	ROLE_IS_NULL(20019, "角色为空", 400), // L
	PASSWORD_EXPIRED(20020, "当前密码已失效，请联系管理员重置密码", 400), // L
	PASSWORD_LOCKED(20021, "密码已锁定，请联系管理员重置密码", 400), // L
	AUTH_INVALID_PASSWORD(20022, "密码有误，请重新输入", 500), // L
	AUTH_PASSWORD_STRENGTH(20023, "请设置8-20包含数字、字母、特殊字符的密码", 500), // L
	NEW_PASSWORD_CANNOT_BE_NULL(20024, "请输入新密码", 500), // L
	CONFIRM_PASSWORD_CANNOT_BE_NULL(20025, "请输入确认密码", 500), // L
	CONFIRM_PASSWORD_NOT_SAME(20026, "两次密码输入不一致", 500), // L
	NEW_PASSWORD_CANNOT_SAMEAS_ORIGIN(20027, "新密码和原密码不能相同", 500), // L
	PASSWD_ENCODING(20028, "密码识别错误", 500), // L
	AUTH_BAD_USER(20029, "用户信息异常", 1110002), // L
	USER_MSG_LIST(20030, "获取用户消息列表失败", 500), // L
	USER_MEMBER_LIST(20031, "获取用户组成员失败", 500), // L
	USER_GETHEADER_EMPTY(20032, "获取用户头像失败", 500), // L
	USER_ADDHEADER_EMPTY(20033, "添加用户头像失败", 500), // L
	USER_UPDATEHEADER_EMPTY(20034, "更新用户头像失败", 500), // L
	AUTH_IMAGE_ILLEGAL(20035, "不允许的图片类型", 500), // L
	INVALID_EMAIL_FORMAT(20036, "邮箱格式不正确", 500), // L
	GET_USER_LIST_ERROR(20037, "获取指定类型用户列表信息异常", 500), // L
	AUTH_KEY_CHANGED(20038, "当前用户验证信息已变更，请重新申请秘钥", 500), // L

	//about system
	ADD_NEW_SYSTEM_SETTING_ERROR(300001, "新增系统参数异常，请稍后重试。", 400),
	UPDATE_NEW_SYSTEM_SETTING_ERROR(300002, "更新系统参数异常，请稍后重试。", 400),
	SYSTEM_SETTING_KEY_NOT_EXIST(300003, "系统参数不存在，请联系管理员！", 400),

	//about file
	DOWNLOAD_FILE_FAILED(400001, "下载文件失败", 600),
	SAVE_FILE_FAILED(400002, "下载文件失败", 600),
	UPLOAD_FILE_INSERT_DB_ERROR(400003, "上传文件保存入库失败", 600),
	NO_FILE_ON_DISK(400004, "未找到对应文件", 600),
	DB_DEFAULT_IMAGE_NULL(400005, "数据库缺失默认附件数据，请联系管理员", 996),
	INVALID_XML_FORMAT(40006,"XML文件格式不正确",500),
	MISSING_XML_NODE(40007,"XML文件提取不到节点信息",500),
	SAVE_XML_FILED(40008,"XML文件保存失败",500),
	NO_INFOPLIST_IN_IPA(40009, "ipa文件不含InfoPlist,请添加", 500),
	NO_APPICON_IN_IPA(40010, "ipa文件不含InfoPlist,请添加", 500),
	UNCOMPLETED_IPA_FILE(40011, "ipa文件内容不完整,请检查", 500),
	PARSE_IPA_INFO_ERROR(40012, "解析ipa获取安装包信息异常", 500),
	PARSE_APK_INFO_ERROR(40013, "解析apk获取安装包信息异常", 500),
	STORE_QR_CODE_FAILED(40014, "存储二维码图片格式出错，请稍后重试", 500),
	STORE_APP_LOGO_FAIED(40015, "保存APPLogo异常", 500),
	NOT_SUPPORTED_APP(40016, "仅支持Android和iPhone应用解析", 500),
    READ_FILE_STRAM_ERROR(40017,"读取文件流异常,请稍后重试", 500),
	GENERATE_QRCODE_FAILED(40018, "生成二维码失败，请稍后重试", 500),

	APP_NOT_EXIST(500001, "系统无存量应用信息，请先添加", 400),
	APP_UPLOAD_NOT_EXIST(500002, "无系统指定应用上传记录，请检查", 400),
	APP_UPLOAD_ATTACHMENT_NOT_EXIST(500003, "指定上传记录未找到合法附件资料，请检查", 400),
	APP_UPLOAD_INFO_NOT_COMPLETED(500004, "应用发布信息不完整，请联系管理员", 400),
	ADD_APP_CER_FAILED(500005, "应用征信信息保存失败,请稍后重试!", 400),
	ADD_APP_CER_INFO_NOT_COMPELETED(500006, "移动端证书信息不完整，请检查重试", 400),
	ADD_APP_CER_TYPE_NOT_SUPPORTED(500007, "当前移动端证书类型不支持，请联系管理员", 400),
	APP_CURRENT_PLATFORM_NOT_EXIST(500008, "系统无指定平台应用上传记录，请检查", 400),

	INVALID_ENVIRONMENT_EXCEPTION(600001,"环境信息校验异常，请检查待测环境配置正确性",400),
    INVALID_DIAMOND_INFO_EXCEPTION(600002,"无法成功获取diamond环境信息",500),


	MAIL_SENDER_CHECK_APP_FAILED(700001, "邮件收发器检索安装包信息失败", 500),

    ;
	


    RES_STATUS(int code, String msg, int saCode) {
        this.code = code;
        this.msg = msg;
        this.saCode = saCode;
    }

    public final int code;
    public final String msg;
    public final int saCode;

    public static RES_STATUS findStatusByCode(int code) {
        for (RES_STATUS status : RES_STATUS.values()) {
            if (status.code == code) {
                return status;
            }
        }
        return null;
    }
    
    public static int findSaCodeByCode(int code){
    	for (RES_STATUS status : RES_STATUS.values()) {
    		if(status.code != code){
    			continue;
    		}
    		return status.saCode;
        }
        return code;
    }
    
}
