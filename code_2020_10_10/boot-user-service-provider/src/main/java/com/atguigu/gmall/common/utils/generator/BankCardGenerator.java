package com.atguigu.gmall.common.utils.generator;


import java.util.Stack;
import java.util.Vector;

/**
 * 银行卡号生成器
 *
 * @author ellen
 */
public class BankCardGenerator {


    public static final String[] VISA_PREFIX_LIST = new String[]{"4539", "4556", "4916", "4532", "4929", "40240071",
            "4485", "4716", "4"};
    public static final String[] VISA_JIAN = new String[]{"62170008", "62170003"};
    public static final String[] VISA_GONG = new String[]{"62220238", "62220205", "62122612"};
    public static final String[] VISA_QING = new String[]{"62125202"};
    public static final String[] MASTERCARD_PREFIX_LIST = new String[]{"51", "52", "53", "54", "55"};
    public static final String[] MASTERCARD = new String[]{"6217", "6228", "6212", "6222", "6226", "6214", "6225"};
    public static final String[] AMEX_PREFIX_LIST = new String[]{"34", "37"};
    public static final String[] DISCOVER_PREFIX_LIST = new String[]{"6011"};
    public static final String[] DINERS_PREFIX_LIST = new String[]{"300", "301", "302", "303", "36", "38"};
    public static final String[] ENROUTE_PREFIX_LIST = new String[]{"2014", "2149"};
    public static final String[] JCB_PREFIX_LIST = new String[]{"35"};
    public static final String[] VOYAGER_PREFIX_LIST = new String[]{"8699"};

    public static String strrev(String str) {
        if (str == null) {
            return "";
        } else {
            String revstr = "";

            for (int i = str.length() - 1; i >= 0; --i) {
                revstr = revstr + str.charAt(i);
            }

            return revstr;
        }
    }


    public static String completed_number(String prefix, int length) {
        String ccnumber;
        for (ccnumber = prefix; ccnumber.length() < length - 1; ccnumber = ccnumber
                + (new Double(Math.floor(Math.random() * 10.0D))).intValue()) {
            ;
        }

        String reversedCCnumberString = strrev(ccnumber);
        Vector<Integer> reversedCCnumberList = new Vector<Integer>();

        int sum;
        for (sum = 0; sum < reversedCCnumberString.length(); ++sum) {
            reversedCCnumberList.add(new Integer(String.valueOf(reversedCCnumberString.charAt(sum))));
        }

        sum = 0;
        int pos = 0;

        int checkdigit;
        for (Integer[] reversedCCnumber = (Integer[]) reversedCCnumberList
                .toArray(new Integer[reversedCCnumberList.size()]); pos < length - 1; pos += 2) {
            checkdigit = reversedCCnumber[pos].intValue() * 2;
            if (checkdigit > 9) {
                checkdigit -= 9;
            }

            sum += checkdigit;
            if (pos != length - 2) {
                sum += reversedCCnumber[pos + 1].intValue();
            }
        }

        checkdigit = (new Double(((Math.floor((double) (sum / 10)) + 1.0D) * 10.0D - (double) sum) % 10.0D)).intValue();
        ccnumber = ccnumber + checkdigit;
        return ccnumber;
    }

    public static String[] credit_card_number(String[] prefixList, int length, int howMany) {
        Stack<String> result = new Stack<String>();

        for (int i = 0; i < howMany; ++i) {
            int randomArrayIndex = (int) Math.floor(Math.random() * (double) prefixList.length);
            String ccnumber = prefixList[randomArrayIndex];
            result.push(completed_number(ccnumber, length));
        }

        return (String[]) result.toArray(new String[result.size()]);
    }

    /**
     * 生成一组MASTERCARD卡号
     *
     * @param howMany 所需卡个数
     * @return
     */
    public static String[] generateMasterCardNumbers(int howMany) {
        return credit_card_number(MASTERCARD, 19, howMany);
    }

    /**
     * 生成一组VISA_JIAN卡号
     *
     * @param howMany 所需卡个数
     * @return
     */
    public static String[] generateJIANBankCardNumbers(int howMany) {
        return credit_card_number(VISA_JIAN, 19, howMany);
    }

    /**
     * 生成一组VISA_GONG卡号
     *
     * @param howMany 所需卡个数
     * @return
     */
    public static String[] generateGONGBankCardNumbers(int howMany) {
        return credit_card_number(VISA_GONG, 19, howMany);
    }

    /**
     * 生成一个MASTERCARD卡号
     *
     * @return
     */
    public static String generateMasterCardNumber() {
        return credit_card_number(MASTERCARD, 19, 1)[0];
    }

    /**
     * 生成一个VISA_JIAN卡号
     *
     * @return
     */
    public static String generateJIANBankCardNumber() {
        return credit_card_number(VISA_JIAN, 19, 1)[0];
    }

    /**
     * 生成一个VISA_GONG卡号
     *
     * @return
     */
    public static String generateGONGBankCardNumber() {
        return credit_card_number(VISA_GONG, 19, 1)[0];
    }

    /**
     * 生成一个VISA_QING卡号
     *
     * @return
     */
    public static String generateQINGBankCardNumber() {
        return credit_card_number(VISA_QING, 16, 1)[0];
    }

    /**
     * 判断已知的卡号是否有效
     *
     * @param creditCardNumber 传入的银行卡号
     * @return
     */
    public static boolean isValidCreditCardNumber(String creditCardNumber) {
        boolean isValid = false;

        try {
            String reversedNumber = (new StringBuffer(creditCardNumber)).reverse().toString();
            int mod10Count = 0;

            for (int i = 0; i < reversedNumber.length(); ++i) {
                int augend = Integer.parseInt(String.valueOf(reversedNumber.charAt(i)));
                if ((i + 1) % 2 == 0) {
                    String productString = String.valueOf(augend * 2);
                    augend = 0;

                    for (int j = 0; j < productString.length(); ++j) {
                        augend += Integer.parseInt(String.valueOf(productString.charAt(j)));
                    }
                }

                mod10Count += augend;
            }

            if (mod10Count % 10 == 0) {
                isValid = true;
            }
        } catch (NumberFormatException arg7) {
            ;
        }

        return isValid;
    }

    public static void main(String[] args) {
        System.out.println(generateMasterCardNumber());
        System.out.println(generateJIANBankCardNumber());
        System.out.println(generateGONGBankCardNumber());
        String[] creditcardnumbers = generateMasterCardNumbers(11);

        for (int i = 0; i < creditcardnumbers.length; ++i) {
            System.out.println(
                    creditcardnumbers[i] + ":" + (isValidCreditCardNumber(creditcardnumbers[i]) ? "valid" : "invalid"));
        }


        String new_str = strrev("qwert");

        System.out.println(new_str);

    }
}