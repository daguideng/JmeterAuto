package com.atguigu.gmall.common.enums;


public enum AttachmentType {

    /**
     * 头像
     */
    AVATAR("AVATAR"),
    /**
     * 手机照片
     */
    MOBILE_PIC("MOBILE_PIC"),

    /**
     * 二维码
     */
    QR_CODE("QR_CODE"),

    /**
     * PLIST
     */
    PLIST("PLIST"),

    /**
     * APP_LOGO
     */
    APP_LOGO("APP_LOGO"),

    /**
     * 应用图标
     */
    APP_ICON("APP_ICON"),

    /**
     * 安装包
     */
    APP("APP"),

    /**
     * Android安装包
     */
    ANDROID_APP("ANDROID_APP"),

    /**
     * iOS安装包
     */
    IOS_APP("IOS_APP"),

	/**
	 * JmeterAPI HTML报告
	 */
    API_HTML("API_HTML"),
    /**
	 * JmeterAPI LOG报告
	 */ 
    API_LOG("API_LOG"),
    /**
	 * JmeterAPI JTL报告
	 */
    API_JTL("API_JTL");
	
    private final String string;

    AttachmentType(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return this.string;
    }

}
