package com.atguigu.gmall.common.utils;

import com.atguigu.gmall.common.enums.Gender;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class IDNumberUtils {
    private static final int[] WEIGHT = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9,
            10, 5, 8, 4, 2};
    private static final char[] CHECKSUM = {'1', '0', 'X', '9', '8', '7', '6',
            '5', '4', '3', '2'};

    /**
     * 校验身份证号
     * <p/>
     * 规则包括：
     * <ul>
     * <li>不能为空</li>
     * <li>18位身份证号：
     * <ul>
     * <li>日期格式必须正确</li>
     * <li>校验位必须正确</li>
     * </ul>
     * </li>
     * <li>15位身份证号：
     * <ul>
     * <li>只要长度是15位就可以了。</li>
     * </ul>
     * </li>
     * </ul>
     *
     * @param id 身份证号
     * @return true：通过校验；false：校验失败。
     */
    public static boolean verifyIDNumber(String id) {
        id = StringUtils.trim(id);
        if (StringUtils.isBlank(id)) {
            return false;
        }

        // 只校验18位
        if (id.length() == 18) {
            String date = id.substring(6, 14);
            boolean flag = true;
            try {
                //判断日期
                Date birthDay = DateUtils.parseDate(date, "yyyyMMdd");
                if (birthDay.after(new Date())) {
                    flag = false;
                }
            } catch (ParseException e) {
                flag = false;
            }
            if (!flag) {
                return false;
            }

            char checksum = IDNumberUtils.generateChecksum(id);
            return Character.toUpperCase(id.charAt(17)) == checksum;
        }
        return id.length() == 15;
    }

    public static char generateChecksum(String idNo) {
        int sum = 0;
        char[] ids = idNo.toCharArray();

        for (int i = 0; i < 17; i++) {
            int n = ids[i] - '0';
            sum += n * IDNumberUtils.WEIGHT[i];
        }

        return IDNumberUtils.CHECKSUM[sum % 11];
    }

    public static Date getBirthday(String idNo) {
        idNo = StringUtils.trim(idNo);
        if (idNo.length() < 14) {
            return new Date();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();

        if (idNo.length() == 15) {
            String dateStr = "19" + idNo.substring(6, 12);
            try {
                date = sdf.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return date;
        } else if (idNo.length() == 18) {
            String dateStr = idNo.substring(6, 14);
            try {
                date = sdf.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return date;
        } else {
            return null;
        }
    }

    public static Gender getGender(String idNo) {
        int digit;

        if (idNo.length() == 15) {
            digit = Integer.parseInt(idNo.substring(14, 15));
        } else if (idNo.length() == 18) {
            digit = Integer.parseInt(idNo.substring(16, 17));
        } else {
            return null;
        }

        return digit % 2 == 0 ? Gender.FEMALE : Gender.MALE;
    }

}
