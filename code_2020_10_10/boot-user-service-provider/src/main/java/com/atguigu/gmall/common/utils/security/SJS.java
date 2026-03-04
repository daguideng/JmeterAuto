package com.atguigu.gmall.common.utils.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.TreeMap;

public class SJS {

    //entrance

    /**
     * 对请求参数进行加密
     *
     * @param requestParams 请求参数
     * @param secret        根秘钥
     * @return
     * @throws Exception
     */
    public String getSJSInfo(TreeMap<String, Object> requestParams, String secret) throws Exception {
        return sjsSi(buildParamStr(requestParams), secret);
    }


    //encrypt
    private String sjsSi(String signStr, String secret)
            throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        return Md5.encrypt(signStr + secret);
    }

    //build paramStr
    private String buildParamStr(TreeMap<String, Object> requestParams) {
        StringBuilder retStr = new StringBuilder();
        for (String key : requestParams.keySet()) {
            if (retStr.length() == 0) {
                retStr.append(key + "=" + String.valueOf(requestParams.get(key)));
            } else {
                retStr.append("&" + key + "=" + String.valueOf(requestParams.get(key)));
            }
        }
        return retStr.toString();
    }

}
