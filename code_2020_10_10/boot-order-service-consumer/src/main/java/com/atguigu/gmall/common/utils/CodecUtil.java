package com.atguigu.gmall.common.utils;


import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * 加解密工具类
 * -----------------------------------------------------------------------------------------------------------
 * 完整版见https://github.com/jadyer/JadyerEngine/blob/master/JadyerEngine-common/src/main/java/com/jadyer/engine/common/util/CodecUtil.java
 * -----------------------------------------------------------------------------------------------------------
 * @version v1.5
 * @history v1.5-->加密和签名时增加UTF-8取字节数组
 * @history v1.4-->增加AES-PKCS7算法加解密数据的方法
 * @history v1.3-->增加buildHMacSign()的签名方法,目前支持<code>HMacSHA1,HMacSHA256,HMacSHA512,HMacMD5</code>算法
 * @history v1.2-->修改buildHexSign()方法,取消用于置顶返回字符串大小写的第四个参数,修改后默认返回大写字符串
 * @history v1.1-->增加AES,DES,DESede等算法的加解密方法
 * @history v1.0-->新增buildHexSign()的签名方法,目前支持<code>MD5,SHA,SHA1,SHA-1,SHA-256,SHA-384,SHA-512</code>算法
 * @update 2015-2-2 下午05:26:32
 * @create Oct 6, 2013 12:00:35 PM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public  class CodecUtil {




    public static final String OAUTH_INTERFACE_PARAM="accessToken";

    private static final String CHARSET = "utf-8";
    /**
     * 密钥算法
     */
    private static final String ALGORITHM_RSA = "RSA";
    private static final String ALGORITHM_RSA_SIGN = "SHA256WithRSA";
    private static final int ALGORITHM_RSA_PRIVATE_KEY_LENGTH = 2048;
    public static final String ALGORITHM_AES = "AES";
    private static final String ALGORITHM_AES_PKCS7 = "AES";
    private static final String ALGORITHM_DES = "DES";
    private static final String ALGORITHM_DES_EDE= "DESede";

    /**
     * 加解密算法/工作模式/填充方式,Java6.0支持PKCS5Padding填充方式,BouncyCastle支持PKCS7Padding填充方式
     * 工作模式有四种-->>ECB：电子密码本模式,CBC：加密分组链接模式,CFB：加密反馈模式,OFB：输出反馈模式
     */
    private static final String ALGORITHM_CIPHER_AES = "AES/ECB/PKCS5Padding";
    private static final String ALGORITHM_CIPHER_AES_PKCS7 = "AES/CBC/PKCS7Padding";
    private static final String ALGORITHM_CIPHER_DES = "DES/ECB/PKCS5Padding";
    private static final String ALGORITHM_CIPHER_DES_EDE = "DESede/ECB/PKCS5Padding";

    private static final Logger log = LoggerFactory.getLogger(CodecUtil.class);

    private CodecUtil(){}

    /**
     * 初始化算法密钥
     * @see <h>目前algorithm参数可选值为AES,DES,DESede,输入其它值时会返回<code>""</code>空字符串</h>
     * @see <h></h>若系统无法识别algorithm会导致实例化密钥生成器失败,此时也会返回<code>""</code>空字符串</h>
     * @param algorithm      指定生成哪种算法的密钥
     * @param isPKCS7Padding 是否采用PKCS7Padding填充方式(需要BouncyCastle支持)
     * @throws DecoderException
     */
    public static String initKey(String algorithm, boolean isPKCS7Padding){
        if(isPKCS7Padding){
            Security.addProvider(new BouncyCastleProvider());
        }
        //实例化密钥生成器
        KeyGenerator kg;
        try {
            kg = KeyGenerator.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            log.error("实例化密钥生成器失败,系统不支持给定的[" + algorithm + "]算法,堆栈轨迹如下", e);
            return "";
        }
        //初始化密钥生成器:AES要求密钥长度为128,192,256位
        if(ALGORITHM_AES.equals(algorithm)){
            kg.init(128);
        }else if(ALGORITHM_AES_PKCS7.equals(algorithm)){
            kg.init(128);
        }else if(ALGORITHM_DES.equals(algorithm)){
            kg.init(56);
        }else if(ALGORITHM_DES_EDE.equals(algorithm)){
            kg.init(168);
        }else{
            return "";
        }

        //生成密钥
        SecretKey secretKey = kg.generateKey();
        //获取二进制密钥编码形式
        if(isPKCS7Padding){
            return Hex.encodeHexString(secretKey.getEncoded());
        }
        return Base64.encodeBase64URLSafeString(secretKey.getEncoded());
    }


    /**
     * 初始化RSA算法密钥对
     * @param keysize RSA1024已经不安全了,建议2048
     * @return 经过Base64编码后的公私钥Map,键名分别为publicKey和privateKey
     * @create Feb 20, 2016 7:34:41 PM
     * @author 玄玉<http://blog.csdn.net/jadyer>
     */
    public static Map<String, String> initRSAKey(int keysize){
        if(keysize != ALGORITHM_RSA_PRIVATE_KEY_LENGTH){
            throw new IllegalArgumentException("RSA1024已经不安全了,请使用"+ALGORITHM_RSA_PRIVATE_KEY_LENGTH+"初始化RSA密钥对");
        }
        //为RSA算法创建一个KeyPairGenerator对象
        KeyPairGenerator kpg;
        try{
            kpg = KeyPairGenerator.getInstance(ALGORITHM_RSA);
        }catch(NoSuchAlgorithmException e){
            throw new IllegalArgumentException("No such algorithm-->[" + ALGORITHM_RSA + "]");
        }
        //初始化KeyPairGenerator对象,不要被initialize()源码表面上欺骗,其实这里声明的size是生效的
        kpg.initialize(ALGORITHM_RSA_PRIVATE_KEY_LENGTH);
        //生成密匙对
        KeyPair keyPair = kpg.generateKeyPair();
        //得到公钥
        Key publicKey = keyPair.getPublic();
        String publicKeyStr = Base64.encodeBase64URLSafeString(publicKey.getEncoded());
        //得到私钥
        Key privateKey = keyPair.getPrivate();
        String privateKeyStr = Base64.encodeBase64URLSafeString(privateKey.getEncoded());
        Map<String, String> keyPairMap = new HashMap<>();
        keyPairMap.put("publicKey", publicKeyStr);
        keyPairMap.put("privateKey", privateKeyStr);
        return keyPairMap;
    }


    /**
     * RSA算法分段加解密数据
     * @param cipher 初始化了加解密工作模式后的javax.crypto.Cipher对象
     * @param opmode 加解密模式,值为javax.crypto.Cipher.ENCRYPT_MODE/DECRYPT_MODE
     * @param datas   待分段加解密的数据的字节数组
     * @return 加密或解密后得到的数据的字节数组
     * @create Feb 21, 2016 1:37:21 PM
     * @author 玄玉<http://blog.csdn.net/jadyer>
     */
    private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas){
        int maxBlock;
        if(opmode == Cipher.DECRYPT_MODE){
            maxBlock = ALGORITHM_RSA_PRIVATE_KEY_LENGTH / 8;
        }else{
            maxBlock = ALGORITHM_RSA_PRIVATE_KEY_LENGTH / 8 - 11;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] buff;
        int i = 0;
        try{
            while(datas.length > offSet){
                if(datas.length-offSet > maxBlock){
                    buff = cipher.doFinal(datas, offSet, maxBlock);
                }else{
                    buff = cipher.doFinal(datas, offSet, datas.length-offSet);
                }
                out.write(buff, 0, buff.length);
                i++;
                offSet = i * maxBlock;
            }
        }catch(Exception e){
            throw new RuntimeException("加解密阀值为["+maxBlock+"]的数据时发生异常", e);
        }
        byte[] resultDatas = out.toByteArray();
        try {
            if(null != out){
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultDatas;
    }


    /**
     * RSA算法公钥加密数据
     * @param data 待加密的明文字符串
     * @param key  RSA公钥字符串
     * @return RSA公钥加密后的经过Base64编码的密文字符串
     * @create Feb 20, 2016 8:25:21 PM
     * @author 玄玉<http://blog.csdn.net/jadyer>
     */
    public static String buildRSAEncryptByPublicKey(String data, String key){
        try{
            //通过X509编码的Key指令获得公钥对象
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(key));
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
            Key publicKey = keyFactory.generatePublic(x509KeySpec);
            //encrypt
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            //return Base64.encodeBase64URLSafeString(cipher.doFinal(data.getBytes(CHARSET)));
            return Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET)));
        }catch(Exception e){
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
        }
    }


    /**
     * RSA算法私钥解密数据
     * @param data 待解密的经过Base64编码的密文字符串
     * @param key  RSA私钥字符串
     * @return RSA私钥解密后的明文字符串
     * @create Feb 20, 2016 8:33:22 PM
     * @author 玄玉<http://blog.csdn.net/jadyer>
     */
    public static String buildRSADecryptByPrivateKey(String data, String key){
        try{
            //通过PKCS#8编码的Key指令获得私钥对象
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(key));
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
            Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
            //decrypt
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            //return new String(cipher.doFinal(Base64.decodeBase64(data)), CHARSET);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data)), CHARSET);
        }catch(Exception e){
            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
        }
    }


    /**
     * RSA算法使用私钥对数据生成数字签名
     * @see注意签名算法SHA1WithRSA已被废弃,推荐使用SHA256WithRSA
     * @param data 待签名的明文字符串
     * @param key  RSA私钥字符串
     * @return RSA私钥签名后的经过Base64编码的字符串
     * @create Feb 20, 2016 8:43:49 PM
     * @author 玄玉<http://blog.csdn.net/jadyer>
     */
    public static String buildRSASignByPrivateKey(String data, String key){
        try{
            //通过PKCS#8编码的Key指令获得私钥对象
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(key));
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
            //sign
            Signature signature = Signature.getInstance(ALGORITHM_RSA_SIGN);
            signature.initSign(privateKey);
            signature.update(data.getBytes(CHARSET));
            return Base64.encodeBase64URLSafeString(signature.sign());
        }catch(Exception e){
            throw new RuntimeException("签名字符串[" + data + "]时遇到异常", e);
        }
    }


    /**
     * RSA算法使用公钥校验数字签名
     * @param data 参与签名的明文字符串
     * @param key  RSA公钥字符串
     * @param sign RSA签名得到的经过Base64编码的字符串
     * @return true--验签通过,false--验签未通过
     * @create Feb 20, 2016 8:51:49 PM
     * @author 玄玉<http://blog.csdn.net/jadyer>
     */
    public static boolean buildRSAverifyByPublicKey(String data, String key, String sign){
        try{
            //通过X509编码的Key指令获得公钥对象
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(key));
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
            PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
            //verify
            Signature signature = Signature.getInstance(ALGORITHM_RSA_SIGN);
            signature.initVerify(publicKey);
            signature.update(data.getBytes(CHARSET));
            return signature.verify(Base64.decodeBase64(sign));
        }catch(Exception e){
            throw new RuntimeException("验签字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * AES算法加密数据
     * @param data 待加密数据
     * @param key  密钥
     * @return 加密后的数据,加密过程中遇到异常导致加密失败则返回<code>""</code>空字符串
     * */
    public static String buildAESEncrypt(String data, String key){
        try{
            //实例化Cipher对象,它用于完成实际的加密操作
            Cipher cipher = Cipher.getInstance(ALGORITHM_CIPHER_AES);
            //还原密钥,并初始化Cipher对象,设置为加密模式
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(Base64.decodeBase64(key), ALGORITHM_AES));
            //执行加密操作,加密后的结果通常都会用Base64编码进行传输
            //将Base64中的URL非法字符如'+','/','='转为其他字符,详见RFC3548
            return Base64.encodeBase64URLSafeString(cipher.doFinal(data.getBytes(CHARSET)));
        }catch(Exception e){
            log.error("加密字符串[" + data + "]时遇到异常,堆栈轨迹如下", e);
            return "";
        }
    }

    /**
     * 先AES加密，在base64转码
     * @param sSrc 待加密数据
     * @param sKey 秘钥
     * @return 加密后的数据
     * @throws Exception
     */
    public static String encrypt(String sSrc, String sKey) throws Exception {
        if (sKey == null) {
            return null;
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            return null;
        }
        byte[] raw = sKey.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));

        return new Base64().encodeToString(encrypted);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }
    /**
     * AES算法解密数据
     * @param data 待解密数据
     * @param key  密钥
     * @return 解密后的数据,解密过程中遇到异常导致解密失败则返回<code>""</code>空字符串
     * */
    public static String buildAESDecrypt(String data, String key){
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM_CIPHER_AES);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(Base64.decodeBase64(key), ALGORITHM_AES));
            return new String(cipher.doFinal(Base64.decodeBase64(data)), CHARSET);
        }catch(Exception e){
            log.error("解密字符串[" + data + "]时遇到异常,堆栈轨迹如下", e);
            return "";
        }
    }
    /**
     * 先BASE64转码，在AES解密
     * @param data 待解密数据
     * @param key 秘钥
     * @return 解密后的数据
     */
    public static String buildAESDecrypt(String data, byte[] key){
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM_CIPHER_AES);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, ALGORITHM_AES));
            return new String(cipher.doFinal(Base64.decodeBase64(data)), CHARSET);
        }catch(Exception e){
            log.error("解密字符串[" + data + "]时遇到异常,堆栈轨迹如下", e);
            return "";
        }
    }


    /**
     * Hmac签名
     * @see <h>Calculates the algorithm digest and returns the value as a hex string</h>
     * @see if system dosen't support this <code>algorithm</code>, return "" not null
     * @param data      待签名数据
     * @param key       签名用到的密钥
     * @param algorithm 目前其有效值为<code>HmacSHA1,HmacSHA256,HmacSHA512,HmacMD5</code>
     * @return String algorithm digest as a lowerCase hex string
     * @create Nov 10, 2014 1:43:25 PM
     * @author 玄玉<http://blog.csdn.net/jadyer>
     */
    public static String buildHmacSign(String data, String key, String algorithm){
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), algorithm);
        Mac mac;
        try {
            mac = Mac.getInstance(algorithm);
            mac.init(secretKey);
        } catch (InvalidKeyException e) {
            log.error("签名字符串[" + data + "]时发生异常:InvalidKey[" + key + "]");
            return "";
        } catch (NoSuchAlgorithmException e) {
            log.error("签名字符串[" + data + "]时发生异常:System doesn't support this algorithm[" + algorithm + "]");
            return "";
        }
        byte[] retByte=null;
        try{
            retByte=data.getBytes(CHARSET);
        }catch(UnsupportedEncodingException ue){
            ue.printStackTrace();
        }
        return Hex.encodeHexString(mac.doFinal(retByte));
    }

    public static void main(String[] args) {
 //       Map<String, String> stringStringMap = initRSAKey(2048);
//        String privateKey = stringStringMap.get("privateKey");
//        String publicKey = stringStringMap.get("publicKey");
//        System.err.println("privateKey--->"+privateKey);
//        System.err.println("publicKey--->"+publicKey);
	/*	log.info("privateKey:"+privateKey);
		log.info("publicKey:"+publicKey);*/
        	String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDpTwF-vJNoeKx331qqv2hxhWUfHeAc4KH5G-FJJofc13gKYEKJ_q2a8ZXWB5JoCAnt17QXe4mamJUt1FaSUU-3HIu8H-fd7keS1DD_Pz_ozXgogxuhSugmdqV-fDBz48M8iqxSoqO1zECrJq7YCK6CggRenKoV5AOp5UqsUjYQyvUuuwdlCk2uy61MykrPMHSixtZKVTFqIXiMPkByrYiz56qHBYsax3YJ-67-RXBfdT03oOlUAMK_sHKCMOoNnneHKpyaBOtLUrVPDJpJZd63MTVsB9RWyqv1zL6BH_pveFBfduk7lHoQ5r_JhdTYz58N7-eV6nCGOoLPwQSMRGkfAgMBAAECggEARjELzRpk6uhrUwEyoO5HOOgeHTd6xpVahby6kaxy8aEtr0l4m1Ww203Ve3f5tyy79_-OYgY7xvQi5Y_dn9WwuvFupOGge6eFhwop15Eq5Bp1DRMsy-DiaTgG4D_yv9sFUQCTiaT-xeI2jodr0PkDr6X4NJLDpSRdac7fIWr2cyRZY1ikBgCr2_Euhodk8Ju_U5V1DejkTXD7RhPPaz2b24YqyhikKYpFCwbcNGkVnzMPfaw2WtesrwcRzRZ9UPJf6mC8j8q_Jym1pVSZWGeaVvI8qOFnojUk-xA8ES0x3pwkv8VyEWBGDPKfgVK7xV8F6d4ZD6ltPhG0RjigPvSGmQKBgQD-vSbdlNgugXz8MisthhwfR-2Ln36BS5mb3YSglgwIPL7RrEIRdpNP8Tf3W4iUz2aNZPrWszRj9Q3GFbok16eTuAJb5LaoUs_el2OM0_EZYmn18j9JD-9693kPLs6wP2B4GcRewXR2IOyyDjfth68rdcj_JH-nv8wJXZXVQzNnGwKBgQDqdrGoVM1vUUjlHtrzRSZAdbOyDUt8m8ftavLw7nha6jHrK9C6ajMnjAxYYjRzRhfezSkUAPFADMprPk2UrAeGGI8IXXDdKqScpAaav2LX9_nnDhh3FEL0b0hPNfHYiz0aejN4qmuJQFmo7T_klsYsCuBAYogNAthxbEnR-hOSTQKBgQDDQ8LS_xFnNKLVvq8SSkasQ7p22F9kRMsOixLq9ZlrhwTOPDi4oifxk3nPBj7_sqttnwYYJW33YRSZznXNX_F4bgiA4CSTikcE3Q7WGHArgSCwAm04WBq-K4yEcuDEgRA1f6ri52-aTUqigfuk--jua28TBKtFruN8GlyMCOMSGQKBgQDMNFphCJLwK_R4EVqS3BwHsjuyjJ-9RJRpNGeo7ZmHXUfi0gN2CH7LJ8-svsN9zoOFulvXn0Z_CWTW6Kl7HiVjZkpU98UP6075WmaiKqQ1AzX1RjKKq-vwpmdtDce4WPT79YiUxKDqJlCwrwmh1qt8unA7w1VJN7w5dR4KMmsPVQKBgAx6MpHAc9zXA7eIoySflmTh8UyvDBrN_x6bpWhp5iYYKZ4QrkIXScY6I6IY3JMfRZxRXSD6njBUqDkDUy_I28sciLbhqOcI_eG-67niYe3ryjkBRAtS3A_tbLJa0TET1AkHpmIS4d4gf0YA4m2xgKfumNNtbNtQwfdWY-r7-wZ0";
        	String publicKey ="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6U8BfryTaHisd99aqr9ocYVlHx3gHOCh-RvhSSaH3Nd4CmBCif6tmvGV1geSaAgJ7de0F3uJmpiVLdRWklFPtxyLvB_n3e5HktQw_z8_6M14KIMboUroJnalfnwwc-PDPIqsUqKjtcxAqyau2AiugoIEXpyqFeQDqeVKrFI2EMr1LrsHZQpNrsutTMpKzzB0osbWSlUxaiF4jD5Acq2Is-eqhwWLGsd2Cfuu_kVwX3U9N6DpVADCv7BygjDqDZ53hyqcmgTrS1K1TwyaSWXetzE1bAfUVsqr9cy-gR_6b3hQX3bpO5R6EOa_yYXU2M-fDe_nlepwhjqCz8EEjERpHwIDAQAB";

        String msPub = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjiB_YIABfarFWlc87cGn4NsVe5vUKI100oyKaAiNupwwF77Sk3LOFLmaTPOy6UxuojFPUwDu67lAFjv5MJYxwfmREoNDPFuhi9nWvkEk9q-hRItOE-6YrOh5Z9B8pbwKSs24VROzHUqcZ_se7bkIrDy-Nuf63o_j7nBoQfjo6Wi-cYZ4vI7qHYHBDl8fjBpNAmAwQLrVQtZ6Sxcjj_pXQPsC_WGF5f9FrMOPzIWQS3ijuPXMLo3J8grd15un3HjaetwXSa5SWLBzT5ATCWnqVaUUTVWB3T5J5RMeTKOwtRgzM5_fJDu2WeNEIxjsyw-nyGa9T10mRQw3kaXnh0RVvQIDAQAB";
        String msPri = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCOIH9ggAF9qsVaVzztwafg2xV7m9QojXTSjIpoCI26nDAXvtKTcs4UuZpM87LpTG6iMU9TAO7ruUAWO_kwljHB-ZESg0M8W6GL2da-QST2r6FEi04T7pis6Hln0HylvApKzbhVE7MdSpxn-x7tuQisPL425_rej-PucGhB-OjpaL5xhni8juodgcEOXx-MGk0CYDBAutVC1npLFyOP-ldA-wL9YYXl_0Wsw4_MhZBLeKO49cwujcnyCt3Xm6fceNp63BdJrlJYsHNPkBMJaepVpRRNVYHdPknlEx5Mo7C1GDMzn98kO7ZZ40QjGOzLD6fIZr1PXSZFDDeRpeeHRFW9AgMBAAECggEAVMMC1F1jBjxLWLaAatc8tLhUFpT3sXuzeUJB5Qt84xb1A6RVV4R3bRrH8C7Cu6pOrMI1qa37z296zgfUdGqOoD2jjGPmPF_1dz9id7LIRaq2xfdCiFaajTxT81r-i6UaikQoM-z8vHJb1xOH_YSFrJrJYlt1XTh5WiGFvBqjZVejiicQDxmkhbm-1ddseD_d3wlzgPdBmtC2otVe8pjlo_p8YfIZhTP9zFdKrnmyUN_EMHYMCqzWqwqbfoCGGOkJOuNHvIYe0S19SXqWIj2D6xt4Bzwwv6DfnX6NhEtNC4u0nhwN63_mYj-IzuiUqdqPRMVYdKYcXHnnI1yTrtKS4QKBgQDLPwfLwm_CZloXfGVl9nNo81fae8aBXCOimuY8OaNUNjwVnQrKLMxHSEStRMIPTIqjdIkHTTWFiFM7KkWpMJmknWyZvg3Q-igkxm7hVAEhkOOMoGH_51pZ6gukLSK2bjPPClmQNeTX7llrZEOIlCldajMJZCArn4yLu1Mil72ZGQKBgQCzBFCJjfQIlpgPb5L4Hi_6Wt7zHqLezzcjuLthrNaCXxZMsXExCRB-6urLC07VS38jZHwXpUv4kRYnCrXLksWomviYBxQ9EoAC-84L8YajJ8fAXcTXGle3lYuKiAig_0qD3RUogY-cWdq-QvecTehcBIEt4h38OJzXE9JOplLiRQKBgQCyT-0uBbr5KcTWoAtxxiOAqyWjiD4ilcypxw5BPiweIKrcJ3gk4eKikzO1dAJxW6yyNPZXUpSnP3AOE0skYYAaXQa_Z29FrYL0qKb0xzdq1Gven4L6-WVaRyzJb9Ppi4umlCT5kV7hVrparo9VTu9vhlGaZp6WxxaOLN2GFux_WQKBgHAfGK4-yYSDhbPNRvi-N4CPX6pYGtyAXmGyNzKDBUtccOQSEvoKWlmTmwEXEQYIphWtvwc0UXPas0w6qghZ4hCrF15ouKVTq_eybKgQWypfJP33_OxYpG3obSCT-QuVOb-DPdhD5YWgxZ965AjyxsCzVZE-WVd6wcj3sjXueGUdAoGAbqAlnEZR1Nm2MJwcAlpdsyHPaWgnc4oImlvg-b9Oq-e01hZ5cGmQDjEqSvaQTn0Fvob3_c95lvFrYOE0-TDBQd3BBY3lYy3ProswqLnJGdkrXpzYVasi8Zyo7qPO4p4ICCmuG1mQcKpU6lB963KeCmH_niZr8brxjpBH3ruGMYs";
        // 待提交表单明文
        String dataPlain = "{\"mobileMd5\":\"c0b6c4c3ac611dad01c54f9b1453b238\",\"appKey\":\"\"}";
        // 加密
        String data = CodecUtil.buildRSAEncryptByPublicKey(dataPlain, msPub);
        log.info("data:"+data);
        // 签名
        String sign = CodecUtil.buildRSASignByPrivateKey(dataPlain, privateKey);
        log.info("sign:"+sign);
        // 验签返回报文
        if (CodecUtil.buildRSAverifyByPublicKey(dataPlain, publicKey, sign)) {
            // 验签成功
            log.info("验签成功.");
        }

        System.err.println("data----->"+data);

        String soleMs = "aUygPWXcKoW3wQYPw0yPdi0do07QXiKsW4qe1vvAerx1hxlKNjR23GCaQ_9i7KSYTYXpakJjgCFx8LNPSt-bZz--sLMEHGLyilOVv7zY1kdJT68Mpvntt45lDAnSNl29TiB4F1PDVOlpGZG_7jCF84-IkPwHqn-dLHd8hzcpEHWUEPwyd_6OfP7bHa1WHWfuSAyu77zZ0HYY8e0hknpNHbgtuQCJWXdaVC4rU-SxwIwT5CYC88icCNZP6W5sYYTlqBEcf5wQ2_A0jhkQ0rUvVAP6cDrb8JMm1Rr5YSbgx1NU-7CU759m-2CuMV-rK2s-xzgz33g2B2tkOFWYf1qSaA";
     //   String data1 = CodecUtil.buildRSADecryptByPrivateKey(data, msPri);
     //   log.info(data1);
        String data2 = CodecUtil.buildRSADecryptByPrivateKey(soleMs, privateKey);
        System.err.println("data2----->"+data2);



    }




}