package com.atguigu.gmall.common.utils.security;

import com.atguigu.gmall.common.constant.RES_STATUS;
import com.atguigu.gmall.common.exception.ServiceException;
import com.atguigu.gmall.common.filter.JsonValueFilter;
import com.atguigu.gmall.common.utils.url.UrlUtils;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SecurityUtils {



    private static final Logger logger = LoggerFactory.getLogger(SecurityUtils.class);

    private static String KEY = "A48A2@!%$ADE69AD";

    private static final List<String> allowFileExt = Arrays.asList("bmp","gif","jpg","jpeg","png","tiff","pcx","tga","exif","fpx","svg","psd","cdr","pcd","dxf","ufo","eps","ai","hdri","raw","wmf","fli","flc","emf");

    /**
     * 身份证号只保留后面四位
     * @param idNo
     * @return
     */
    public static String encryptIdNo(String idNo){
        if(null == idNo){
            return null;
        }

        return StringUtils.trimToEmpty(idNo).replaceAll("\\d+(\\w{4})","*********$1");
    }

    /**
     * 加密
     * @param str
     * @return
     */
    public static String aesEncrypt(String str) {
        try {
            if(StringUtils.isBlank(str)){
                return null;
            }
            byte[] keyBytes = Arrays.copyOf(KEY.getBytes("ASCII"), 16);
            SecretKey key = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] cleartext = str.getBytes("UTF-8");
            byte[] ciphertextBytes = cipher.doFinal(cleartext);
            return Hex.encodeHexString(ciphertextBytes).toUpperCase();
        } catch (Exception e) {
            logger.error("encode error", e);
        }
        return str;
    }

    /**
     * 与thread传输 加密
     * @param str
     * @return
     */
    public static String aesEncrypt4Thread(String str,String keyStr) {
        try {
            if(StringUtils.isBlank(str)){
                return null;
            }
            byte[] keyBytes = Arrays.copyOf(keyStr.getBytes("ASCII"), 16);
            SecretKey key = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] cleartext = str.getBytes("UTF-8");
            byte[] ciphertextBytes = cipher.doFinal(cleartext);
            return Hex.encodeHexString(ciphertextBytes).toUpperCase();
        } catch (Exception e) {
            logger.error("encode error", e);
        }
        return str;
    }


    /**
     * 解密
     * @param str
     * @return
     */
    public static String aesDecrypt(String str) {
        try {
            if(StringUtils.isBlank(str)){
                return null;
            }

            byte[] keyBytes = Arrays.copyOf(KEY.getBytes("ASCII"), 16);
            SecretKey key = new SecretKeySpec(keyBytes, "AES");
            Cipher decipher = Cipher.getInstance("AES");
            decipher.init(Cipher.DECRYPT_MODE, key);
            char[] cleartext = str.toCharArray();
            byte[] decodeHex = Hex.decodeHex(cleartext);
            byte[] ciphertextBytes = decipher.doFinal(decodeHex);
            return new String(ciphertextBytes);
        } catch (Exception e) {
            logger.error("decode error", e);
        }
        return str;
    }

    /**
     * 敏感信息，处理成如下规则：前三位****后四位
     * @return
     */
    public static  String desensitization(String str){

        StringBuffer sb = new StringBuffer();

        if(StringUtils.isBlank(str)){
            return str;
        }

        //前三位****后四位
        int length = str.length();

        if(length <= 4){
            return "****";
        }

        for(int i=0;i<3;i++){
            sb.append(str.charAt(i));
        }

        sb.append("****");

        for(int i = length-4;i< length;i++){
            sb.append(str.charAt(i));
        }

        return sb.toString();
    }


    /**
     * URL参数脱敏
     * @param url
     * @return
     */
    public static String desensitizationUrl(String url){

        if(StringUtils.isBlank(url)){
            return url;
        }

        int index    = url.indexOf("?");

        String baseUrl = "";
        String query   = "";

        if(index > -1){
            baseUrl = url.substring(0, index);
            query   = url.substring(index+1);
        }else{
            return url;
        }

        Map<String, String> paramMap = UrlUtils.toMap(query);

        StringBuilder sb = new StringBuilder();

        if(null != paramMap){
            int i = 1;
            for(Map.Entry<String, String> entry : paramMap.entrySet()){

                if(i !=1){
                    sb.append("&");
                }

                sb.append(entry.getKey()).append("=");

                if(JsonValueFilter.MOBILE.equals(entry.getKey())
                        || JsonValueFilter.MOBILE_BACKUP.equals(entry.getKey())
                        || JsonValueFilter.RESERVE_MOBILE.equals(entry.getKey())
                        || JsonValueFilter.ID_NUMBER.equals(entry.getKey())
                        || JsonValueFilter.ID_NO.equals(entry.getKey())
                        || JsonValueFilter.ID_CARD.equals(entry.getKey())){

                    sb.append(SecurityUtils.desensitization(entry.getValue()));
                    sb.append("[log:").append(SecurityUtils.aesEncrypt(entry.getValue())).append("]");

                }else if("socialInsuranceInfo".equals(entry.getKey())){
                    sb.append("*****");
                }else{
                    sb.append(entry.getValue());
                }

                i++;
            }
        }

        return  baseUrl+"?"+sb.toString();
    }

    /**
     * 请设置8-20包含数字、字母、特殊字符的密码
     *
     * @param pass
     */
    public static void passWordStrength(String pass){
        //1、长度8-20
        int length = StringUtils.length(pass);
        if(length < 8 || length > 20){
            throw new ServiceException(RES_STATUS.AUTH_PASSWORD_STRENGTH);
        }

        boolean hasNum    = false;
        boolean hasLetter = false;
        boolean hasOther  = false;
        for(int i=0;i<length;i++ ){
            String letter = String.valueOf(pass.charAt(i));

            if(false == hasNum){
                hasNum = letter.matches("^\\d+$");
            }
            if(false == hasLetter){
                hasLetter = letter.matches("^[a-zA-Z]+$");
            }

            if(false == hasOther){
                hasOther = letter.matches("^[~!@#\\$%\\^&*()_+]+$");
            }
        }

        if(false == hasNum || false == hasLetter || false == hasOther){
            throw new ServiceException(RES_STATUS.AUTH_PASSWORD_STRENGTH);
        }
    }

    /**
     * 校验图片扩展名
     * @param fileName
     */
    public static void checkImageExt(String fileName){
        String ext = FilenameUtils.getExtension(fileName).toLowerCase();
        if(!allowFileExt.contains(ext)){
            throw new ServiceException(RES_STATUS.AUTH_IMAGE_ILLEGAL);
        }
    }


    public static void main(String[] args) {

        //String sql = "INSERT INTO `zhima_credit_infos` ( `lend_request_id`, `app_id`, `id_no`, `name`, `enabled`, `status`, `status_msg`, `success_time`, `open_id`, `error_msg` ) SELECT t.id, '1004543', c.`idNo`, c.`name`, 1, 1, '获取成功', NOW(), 'open_id', '历史数据处理' FROM `lend_requests` t, loan_products p, `lend_customers` c WHERE t.`appliedProduct_id` = p.`id` AND t.`lender_id` = c.`id` AND t.`id` IN (SELECT m.`lend_request_id` FROM `request_mapping` m WHERE m.`thread_lend_request_id` IN(2564399) )";

        String sql = "UPDATE credit_infos t SET credit_rslt='Success!', `enabled` =1,`success_time` = NOW(),`report_time`=NOW(),credit_file_path='2017/12/loan/external/2017_12_20/8e046020-8e64-4e07-8817-fclcf276d2fc.html' WHERE t.`lend_request_id` = 1941659";
        sql = "UPDATE credit_infos t SET credit_rslt='Success!', `enabled` =1,`success_time` = NOW(),`report_time`=NOW() WHERE t.`id` = 2166797";

        sql = "UPDATE `learning_research` t SET t.`type` = 'NEED_CHECKING' WHERE id = 52";
        sql ="UPDATE `credit_infos` t SET t.`credit_file_path` = '2018/04/loan/external/2018_04_09/251b3606-7281-4f10-ad24-0d60992fa6d6.html' WHERE t.`lend_request_id` IN ('2390381','2391216')";

        sql = "UPDATE `user_infos` t SET t.`group_id` = 3101 WHERE t.`group_id` = 3100 AND t.`enabled` =1 ";

        sql = "UPDATE `user_infos` t SET t.`group_id` = 3100 WHERE t.`enabled` =1  AND t.`employeeNumber` = '10025870'";

        String a = SecurityUtils.aesEncrypt(sql);
        System.out.println(a);
        System.out.println(SecurityUtils.aesDecrypt(a));



    }


}
