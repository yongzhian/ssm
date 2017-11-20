package cn.zain.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Zain on 2017/7/4.
 */
public class NumberTools {
    private static final Logger logger = LogManager.getLogger(NumberTools.class);
    private static final String[] CN_UPPER_MONETARY_NUMBER = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};//数字大写
    private static final String[] CN_UPPER_MONETARY_UNIT = {"分", "角", "元", "拾", "佰", "仟", "万", //货币单位大写
            "拾", "佰", "仟", "亿", "拾", "佰", "仟", "兆", "拾", "佰", "仟"};
    private static final String CN_FULL = "整";
    private static final String CN_NEGATIVE = "负";
    private static final int MONEY_PRECISION = 2; //金额的精度，默认值为2
    private static final String CN_ZERO_FULL = "零元" + CN_FULL; //特殊字符：零元整

    private static final String[] CN_UPPER_NORMAL_NUMBER = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};//数字大写
    private static final String[] CN_UPPER_NORMAL_UNIT = {"", "十", "百", "千", "万",
            "十", "百", "千", "亿", "十", "百", "千", "兆", "十", "百", "千"};

    private static final Map<String, String> DIGIT_NUMBER_MAP = new HashMap<>();
    private static final Map<String, String> DIGIT_UNIT_MAP = new HashMap<>();

    static {
        DIGIT_NUMBER_MAP.put("零", "0");
        DIGIT_NUMBER_MAP.put("壹", "1");
        DIGIT_NUMBER_MAP.put("一", "1");
        DIGIT_NUMBER_MAP.put("贰", "2");
        DIGIT_NUMBER_MAP.put("二", "2");
        DIGIT_NUMBER_MAP.put("叁", "3");
        DIGIT_NUMBER_MAP.put("三", "3");
        DIGIT_NUMBER_MAP.put("肆", "4");
        DIGIT_NUMBER_MAP.put("四", "4");
        DIGIT_NUMBER_MAP.put("伍", "5");
        DIGIT_NUMBER_MAP.put("五", "5");
        DIGIT_NUMBER_MAP.put("陆", "6");
        DIGIT_NUMBER_MAP.put("六", "6");
        DIGIT_NUMBER_MAP.put("柒", "7");
        DIGIT_NUMBER_MAP.put("七", "7");
        DIGIT_NUMBER_MAP.put("捌", "8");
        DIGIT_NUMBER_MAP.put("八", "8");
        DIGIT_NUMBER_MAP.put("玖", "9");
        DIGIT_NUMBER_MAP.put("九", "9");

        DIGIT_UNIT_MAP.put("拾", "0");
        DIGIT_UNIT_MAP.put("十", "0");
        DIGIT_UNIT_MAP.put("佰", "00");
        DIGIT_UNIT_MAP.put("百", "00");
        DIGIT_UNIT_MAP.put("仟", "000");
        DIGIT_UNIT_MAP.put("千", "000");
        DIGIT_UNIT_MAP.put("万", "0000");
        DIGIT_UNIT_MAP.put("亿", "00000000");
        DIGIT_UNIT_MAP.put("兆", "000000000000");
    }


    /**
     * 功能说明 ：比较2个double的大小
     * 若number1>number2返回1，若number1<number2返回-1，若number1=number2返回0。
     *
     * @param number1 double
     * @param number2 double
     * @return int
     */
    public static int compare(double number1, double number2) {
        BigDecimal bigDecimal1 = BigDecimal.valueOf(number1);
        BigDecimal bigDecimal2 = BigDecimal.valueOf(number2);
        return bigDecimal1.compareTo(bigDecimal2);
    }

    /**
     * 功能说明：基于金额进行数字转大写
     *
     * @param numberOfMoney
     * @return
     */
    public static String digitUpper4Money(BigDecimal numberOfMoney) {
        StringBuffer sb = new StringBuffer();
        int sign = numberOfMoney.signum(); //-1 for negative, 0 for zero, or 1 for positive
        if (sign == 0) {
            return CN_ZERO_FULL;
        }
        //向左移动MONEY_PRECISION位，基于四舍五入
        long number = numberOfMoney.movePointRight(MONEY_PRECISION).setScale(0, BigDecimal.ROUND_HALF_UP).abs().longValue();
        long scale = number % 100;  // 得到小数点后两位值
        int numUnit = 0;
        int numIndex = 0;
        boolean getZero = false; //是否是整数
        // 判断最后两位数，一共有四中情况：00 = 0, 01 = 1, 10, 11
        if (scale == 0) { //为0被100整除，无小数
            numIndex = 2;
            number = number / 100;
            getZero = true; //是整数
        }
        if ((scale > 0) && (!(scale % 10 > 0))) { //为整十
            numIndex = 1;
            number = number / 10;
            getZero = true;
        }

        int zeroSize = 0;
        while (true) {
            if (number <= 0) {
                break;
            }
            // 每次获取到最后一个数
            numUnit = (int) (number % 10);
            if (numUnit > 0) {
                if ((numIndex == 9) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONETARY_UNIT[6]);
                }
                if ((numIndex == 13) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONETARY_UNIT[10]);
                }
                sb.insert(0, CN_UPPER_MONETARY_UNIT[numIndex]);
                sb.insert(0, CN_UPPER_MONETARY_NUMBER[numUnit]);
                getZero = false;
                zeroSize = 0;
            } else {
                ++zeroSize;
                if (!(getZero)) {
                    sb.insert(0, CN_UPPER_MONETARY_NUMBER[numUnit]);
                }
                if (numIndex == 2) {
                    if (number > 0) {
                        sb.insert(0, CN_UPPER_MONETARY_UNIT[numIndex]);
                    }
                } else if (((numIndex - 2) % 4 == 0) && (number % 1000 > 0)) {
                    sb.insert(0, CN_UPPER_MONETARY_UNIT[numIndex]);
                }
                getZero = true;
            }
            // 让number每次都去掉最后一个数
            number = number / 10;
            ++numIndex;
        }
        // 如果signum == -1，则说明输入的数字为负数，就在最前面追加特殊字符：负
        if (sign == -1) {
            sb.insert(0, CN_NEGATIVE);
        }
        // 输入的数字小数点后两位为"00"的情况，则要在最后追加特殊字符：整
        if (scale == 0) {
            sb.append(CN_FULL);
        }

        return sb.toString();
    }

    /**
     * 功能说明：数字转大写
     *
     * @param decimal
     * @return
     */
    public static String digitUpper(BigDecimal decimal) {
        int sign = decimal.signum(); //-1 for negative, 0 for zero, or 1 for positive

        StringBuffer sb = new StringBuffer();

        //处理整数
        long number = decimal.abs().longValue();
        int numIndex = 0; //单位
        int zeroSize = 0; //0的数量，处理连续0
        while (true) {
            if (number <= 0) {
                break;
            }
            // 每次获取到最后一个数
            int num = (int) (number % 10);
            if (num > 0) {
                if (numIndex == 7 && zeroSize >= 3) {
                    sb.insert(0, CN_UPPER_NORMAL_UNIT[6]);
                }
                if (numIndex == 11 && zeroSize >= 3) {
                    sb.insert(0, CN_UPPER_NORMAL_UNIT[11]);
                }
                sb.insert(0, CN_UPPER_NORMAL_UNIT[numIndex]);
                sb.insert(0, CN_UPPER_NORMAL_NUMBER[num]);
                zeroSize = 0;
            } else {
                if (zeroSize == 0 && numIndex > 0) { //非连续0且非首位的0则添加0
                    sb.insert(0, CN_UPPER_NORMAL_NUMBER[num]);
                }
                if (numIndex % 4 == 0 && number % 1000 > 0) {
                    sb.insert(0, CN_UPPER_NORMAL_UNIT[numIndex]);
                }
                ++zeroSize;
            }
            // 让number每次都去掉最后一个数
            number = number / 10;
            ++numIndex;
        }
        if (sign == -1) {
            sb.insert(0, CN_NEGATIVE);
        }

        //处理小数
        String decimalStr = decimal.toString();
        if (!decimalStr.contains(".")) { //无小数
            return sb.toString();
        }
        decimalStr = decimalStr.substring(decimalStr.indexOf(".") + 1, decimalStr.length());
        sb.append("点");
        for (int i = 0; i < decimalStr.length(); i++) {
            sb.append(CN_UPPER_NORMAL_NUMBER[Integer.parseInt(decimalStr.substring(i, i + 1))]);
        }
        return sb.toString();
    }

    /**
     * 功能说明：中文数字串转小写
     *
     * @param cnNumStr
     * @return
     */
    public static BigDecimal digitLower(String cnNumStr) {
        if (StringUtils.isBlank(cnNumStr)) {
            return null;
        }
        //替换非数字字符
        cnNumStr = cnNumStr.replaceAll("[^(零壹贰叁肆伍陆柒捌玖一二三四五六七八九十百千万十百千亿十百千兆十百千点)-+.\\d]", "");
        if (StringTools.isDecimal(cnNumStr)) {
            return new BigDecimal(cnNumStr);
        }

        if ("十".equals(cnNumStr)) {
            return new BigDecimal(10);
        }

        BigDecimal result = new BigDecimal(0);

        for (int i = 0; i < cnNumStr.length(); i++) {
            if ("点".equals(cnNumStr.substring(i, i + 1))) { //小数开始
                StringBuilder sb = new StringBuilder("0.");
                for (int j = i + 1; j < cnNumStr.length(); j++) {
                    if (DIGIT_NUMBER_MAP.containsKey(cnNumStr.substring(j, j + 1))) {
                        sb.append(DIGIT_NUMBER_MAP.get(cnNumStr.substring(j, j + 1)));
                    }
                }
                result = result.add(new BigDecimal(sb.toString()));
                break;
            }

            if (DIGIT_NUMBER_MAP.containsKey(cnNumStr.substring(i, i + 1))) {// 取出数字
                StringBuilder sb = new StringBuilder(DIGIT_NUMBER_MAP.get(cnNumStr.substring(i, i + 1)));
                int maxBit = 0; //最大位，如万、千
                for (int j = i + 1; j < cnNumStr.length(); j++) { //取单位
                    if (DIGIT_UNIT_MAP.containsKey(cnNumStr.substring(j, j + 1))) {
                        int currBit = DIGIT_UNIT_MAP.get(cnNumStr.substring(j, j + 1)).length();
                        if (maxBit == 0 || maxBit <= currBit) {
                            maxBit = currBit;
                            sb.append(DIGIT_UNIT_MAP.get(cnNumStr.substring(j, j + 1)));
                        }
                    }
                }
                if (result.intValue() % 10 == 0) {
                    result = result.add(new BigDecimal(sb.toString()));
                }
            }
        }
        return result;
    }

    /**
     * 功能说明 ： 对数组求和
     *
     * @param array
     * @return
     */
    public static int sum(int[] array) {
        int sum = 0;
        for (int i = 0; i < array.length; i++) {
            sum += array[i];
        }
        return sum;
    }
}
