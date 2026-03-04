package com.atguigu.gmall.common.filter;

import com.alibaba.fastjson.serializer.ValueFilter;
import com.atguigu.gmall.common.utils.security.SecurityUtils;
import org.apache.commons.lang.StringUtils;

public class JsonValueFilter implements ValueFilter {

    private static final JsonValueFilter valueFilter = new JsonValueFilter();

    public static final String ID_NO       = "idNo";//证件号
    public static final String ID_CARD     = "idCard";//证件号
    public static final String ID_NUMBER   = "idNumber";//证件号

    public static final String MOBILE      = "mobile";//手机号
    public static final String PHONE       = "phone";//手机号


    public static final String FIXEDTELEPHONE       = "fixedTelephone";//单位电话


    public static final String REQUEST_ID_NO   = "requestIdNo";//证件号
    public static final String MOBILE_BACKUP   = "mobileBackup";//备用手机号
    public static final String RESERVE_MOBILE  = "reserveMobile";//备用手机号
    public static final String CONDITION       = "condition";//手机号搜索

    public static final String PHOTO           = "photo";//照片


    public static final String USERNAME        = "username";//银行流水用户名
    public static final String ACCOUNT         = "account";//社保登录用户


    public static JsonValueFilter getInstance(){
        return valueFilter;
    }

    private JsonValueFilter(){

    }


    @Override
    public Object process(Object object, String name, Object value) {

        if(null == value){
            return value;
        }

        if(ID_NO.equals(name)
                ||MOBILE.equals(name)
                ||ID_CARD.equals(name)
                ||ID_NUMBER.equals(name)
                ||REQUEST_ID_NO.equals(name)
                ||CONDITION.equals(name)
                ||RESERVE_MOBILE.equals(name)
                ||MOBILE_BACKUP.equals(name)
                ||PHONE.equals(name)
                ||FIXEDTELEPHONE.equals(name)){

            String valueStr = String.valueOf(value);

            if(StringUtils.isBlank(valueStr)){
                return null;
            }

            StringBuilder sb = new StringBuilder();

            sb.append(SecurityUtils.desensitization(valueStr));
            sb.append("[log:").append(SecurityUtils.aesEncrypt(valueStr)).append("]");

            return sb.toString();
        }

        if(PHOTO.equals(name)) {
            return "不打印日志";
        }

        if(USERNAME.equals(name)
                ||ACCOUNT.equals(name)) {
            String valueStr = String.valueOf(value);

            if(valueStr.matches("[0-9]{11}")) {
                StringBuilder sb = new StringBuilder();

                sb.append(SecurityUtils.desensitization(valueStr));
                sb.append("[log:").append(SecurityUtils.aesEncrypt(valueStr)).append("]");;

                return sb.toString();
            }
        }

        if("socialInsuranceInfo".equals(name)){
            return "******";
        }

        return value;
    }

    public static void main(String[] args) {
        String valueStr = "22345678901";
        System.out.println(valueStr.matches("[0-9]{11}"));
    }

}
