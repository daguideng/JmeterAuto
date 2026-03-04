package com.atguigu.gmall.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 字符串工具类
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    /**
     * 判断一个集合是否为null或空集合
     *
     * @param c
     * @return
     */
    public static boolean isEmpty(Collection c) {
        if (c == null || c.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断一个map是否为null或空集合
     *
     * @param c
     * @return
     */
    public static boolean isEmpty(Map c) {
        if (c == null || c.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * 判断一个数组是否为null或空集合
     *
     * @param c
     * @return
     */
    public static boolean isEmpty(Object[] c) {
        if (c == null || c.length == 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断一个整形是否为null或空集合
     *
     * @param c
     * @return
     */
    public static boolean isEmpty(Integer c) {
        if (c == null) {
            return true;
        }
        return false;
    }

    /**
     * 判断一个字符串是否为空或者0长串
     *
     * @param c
     * @return
     */
    public static boolean isEmpty(String c) {
        if (c == null || c.length() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串的长度是否合法
     *
     * @param str
     * @param minLength
     * @param maxLength
     * @return
     */
    public static boolean stringLengthIsValid(String str, int minLength,
            int maxLength) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        if (str.length() < minLength || str.length() > maxLength) {
            return false;
        }
        return true;
    }

    /**
     * 按字符分割成列表
     *
     * @param str
     * @param separatorChars
     * @return
     */
    public static List<String> splitToList(String str, String separatorChars) {
        String[] array = org.apache.commons.lang3.StringUtils.split(str,
            separatorChars);
        return Arrays.asList(array);
    }

    /**
     * 按字符分割成列表
     *
     * @param str
     * @param separatorChars
     * @return
     */
    public static List<Integer> splitToIntegerList(String str,
        String separatorChars) {
        String[] array = org.apache.commons.lang3.StringUtils.split(str,
            separatorChars);
        List<Integer> numbers = new ArrayList<Integer>();
        for (String s : array) {
            try {
                numbers.add(Integer.parseInt(s));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return numbers;
    }

    /**
     * @param str
     *        根据类的全名获取在spring中注入的id（默认约定）
     * @return 类在spring注入的id名
     */
    public static String getClassNameInIoc(String str) {
        if (!StringUtils.isEmpty(str)) {
            // 去掉第一个'$'开始的所有字符
            int pos = str.indexOf("$");
            if (pos > 0) {
                str = str.substring(0, pos);
            }
            // 获取最后一个'.'的位置
            pos = str.lastIndexOf(".");
            if (pos > 0) {
                // 截取包名
                String tempName = str.substring(pos + 1);
                if (tempName.length() > 1) {
                    // 截取首字母
                    String firstChar = tempName.substring(0, 1);
                    return firstChar.toLowerCase() + tempName.substring(1);
                }
            }
        }
        return "";
    }

    /**
     * 获取str中子串sub的个数
     *
     * @param str
     * @param sub
     * @return
     */
    public static int countMatches(String str, String sub) {
        if (StringUtils.isEmpty(str) || StringUtils.isEmpty(sub)) {
            return 0;
        }
        int count = 0;
        int idx = 0;
        while ((idx = str.indexOf(sub, idx)) != -1) {
            count++;
            idx += sub.length();
        }
        return count;
    }

    /**
     *
     * 算字符串的MD5值
     * @param source
     * @return
     */
    public static String string2Md5(String source) {
        {
            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                md5.update(source.getBytes("UTF-8"));
                byte[] encryption = md5.digest();

                StringBuffer strBuf = new StringBuffer();
                for (int i = 0; i < encryption.length; i++) {
                    if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                        strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                    } else {
                        strBuf.append(Integer.toHexString(0xff & encryption[i]));
                    }
                }

                return strBuf.toString();
            } catch (NoSuchAlgorithmException e) {
                return "";
            } catch (UnsupportedEncodingException e) {
                return "";
            }
        }
    }

    public static String addQuotes(String s) {
        return "'" + s + "'";
    }
}