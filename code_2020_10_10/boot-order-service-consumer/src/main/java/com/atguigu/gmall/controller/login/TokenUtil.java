package com.atguigu.gmall.controller.login;

/**
 * 功能：生成token<br>
 * 作者：张tt<br>
 * 时间：2017年6月26日<br>
 * 版本：1.0<br>
 *
 */
import java.security.MessageDigest;
import java.util.UUID;
import org.springframework.stereotype.Component;


@Component
public class TokenUtil {

    private static final char[] hexCode = "0123456789abcdef".toCharArray();

    public static String createToken() {
        return generateValue(UUID.randomUUID().toString());
    }

    public static String createToken(String username,String pawd) {
        return generateValue(username+pawd);
    }

    private static String toHexString(byte[] data) {
        if (data == null) {
            return null;
        }
        StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            r.append(hexCode[(b >> 4) & 0xF]);
            r.append(hexCode[(b & 0xF)]);
        }
        return r.toString();
    }

    private static String generateValue(String param) {
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(param.getBytes());
            byte[] messageDigest = algorithm.digest();
            return toHexString(messageDigest);
        } catch (Exception e) {
            throw new RuntimeException("Token cannot be generated.", e);
        }
    }
}

