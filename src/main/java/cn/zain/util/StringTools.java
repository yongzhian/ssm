package cn.zain.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * @author Zain
 */
public class StringTools {

    private static final Logger logger = LogManager.getLogger(StringTools.class);

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static Pattern numberPattern = Pattern.compile("[0-9]*");

    private static Pattern decimalPattern = Pattern.compile("-?[0-9]+\\.*[0-9]*");

    private static Pattern numberSplitByCommaPattern = Pattern.compile("\\d+(\\,\\d+)*");

    private StringTools() {
    }

    //使用ThreadLocal以保证线程安全
    private static final ThreadLocal<DateFormat> DATE_FORMATTER = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    public static String getNowDate() {
        return getNow(DATE_FORMAT);
    }

    /**
     * 功能说明：获取当前时间 格式 yyyy-MM-dd HH:mm:ss
     *
     * @return String
     */
    public static String getNow() {
        return getNow(TIME_FORMAT);
    }

    public static String getNow(String format) {
        return DATE_FORMATTER.get().format(new Date());
    }

    public static Date str2Date(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }

        SimpleDateFormat sdf = null;
        if (dateStr.length() == 10 || dateStr.length() == 9) {
            sdf = new SimpleDateFormat(DATE_FORMAT);
        } else {
            sdf = new SimpleDateFormat(TIME_FORMAT);
        }
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            logger.error("日期字符串解析失败。dateStr : " + dateStr, e);
        }

        return null;
    }

    public static String date2Str(Date dt) {
        return StringTools.date2Str(dt, TIME_FORMAT);
    }

    public static String date2Str(Date dt, String format) {
        if (null == dt) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(dt);
    }

    /**
     * 把毫秒转化成日期
     *
     * @param <T>
     * @param dateFormat(日期格式，例如：MM/      dd/yyyy HH:mm:ss)
     * @param millSec(毫秒数,格式为String或Long)
     * @return
     */
    public static <T> String millSecToDate(String dateFormat, T millSec) {
        Long mill = null;
        if (null != millSec && millSec instanceof String) {
            mill = Long.parseLong((String) millSec);
        } else if (millSec instanceof Long) {
            mill = (Long) millSec;
        } else {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(StringUtils.isBlank(dateFormat) ? TIME_FORMAT : dateFormat);
        Date date = new Date(mill);
        return sdf.format(date);
    }

    /**
     * 功能说明： 判断一个毫秒时间是否是当天
     *
     * @param millSec 毫秒时间
     * @return boolean
     */
    public static boolean isCurrDay(long millSec) {
        String currDay = millSecToDate(DATE_FORMAT, System.currentTimeMillis());
        if (StringUtils.isBlank(currDay)) {
            return false;
        }
        return currDay.equalsIgnoreCase(millSecToDate(DATE_FORMAT, millSec));
    }

    public static Timestamp str2Timestamp(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        try {
            return Timestamp.valueOf(dateStr.trim());
        } catch (Exception e) {
            logger.error("日期字符串解析失败。", e);
            return null;
        }
    }

    public static String nvl(Object obj) {
        return StringTools.nvl(obj, "");
    }

    public static String nvl(Object obj, String defaultStr) {
        if (null == obj) {
            return defaultStr;
        }
        String str = obj.toString();
        return StringUtils.isBlank(str) ? defaultStr.trim() : str;
    }

    public static long obj2Long(Object obj) {
        return obj2Long(obj, 0L);
    }

    public static long obj2Long(Object obj, long defaultVal) {
        String str = StringTools.nvl(obj);

        if (!StringUtils.isBlank(str) && StringUtils.isNumeric(str)) {
            return Long.parseLong(str);
        }

        return defaultVal;
    }

    public static long str2Long(String str) {
        return StringTools.obj2Long(str);
    }

    public static long getLong(Map<String, Object> map, String key) {
        if (null == map || map.isEmpty()) {
            return 0L;
        }

        return StringTools.obj2Long(map.get(key));
    }

    public static int obj2Int(Object obj) {
        String str = StringTools.nvl(obj);

        if (StringUtils.isBlank(str)) {
            return 0;
        }

        if (StringUtils.isNumeric(str)) {
            return Integer.parseInt(str);
        }

        return 0;
    }

    public static int str2Int(String str) {
        return StringTools.obj2Int(str);
    }

    public static String getStr(Map<String, Object> map, String key, String defaultStr) {
        if (null == map || map.isEmpty()) {
            return defaultStr;
        }

        return StringTools.nvl(map.get(key), defaultStr);
    }

    public static String getStr(Map<String, Object> map, String key) {

        return StringTools.getStr(map, key, "");
    }

    public static String removeBOM(String str) {
        byte[] newByte = null;
        try {
            newByte = StringTools.removeBOM(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error("removeBOM失败", e);
        }

        String rtnStr = new String(newByte);

        logger.info("rtnStr: " + rtnStr);

        return rtnStr;
    }

    /**
     * 功能说明：去掉BOM头。
     *
     * @param bt
     * @return
     */
    public static byte[] removeBOM(byte[] bt) {
        if (null == bt || bt.length < 3) {
            return bt;
        }

        //EF、BB、BF
        if (bt[0] == -17 && bt[1] == -69 && bt[2] == -65) {
            byte[] nbt = new byte[bt.length - 3];
            System.arraycopy(bt, 3, nbt, 0, nbt.length);
            return nbt;
        } else {
            return bt;
        }
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    public static String byte2HexStr(byte[] buf) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 将二进制转换成16进制 实测效果耗时为byte2HexStr的10倍，微信官方demo使用
     *
     * @param hash
     * @return
     * @deprecated 使用byte2HexStr替换
     */
    @Deprecated
    public static String byteToHex(byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    public static byte[] hexStr2Byte(String hexStr) {
        if (StringUtils.isEmpty(hexStr)) {
            logger.warn("待转换字符串为空,hexStr: " + hexStr);
            return new byte[0];
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * byte数组转base64 mime协议规定base64会在末尾和每隔76个字符添加一个\n
     *
     * @param buf
     * @return
     */
    public static String byte2BASE64(byte[] buf) {
        if (null == buf) {
            return null;
        }
        return new String(Base64.encodeBase64(buf)).replace("\n", "").replace("\r", "");
    }

    public static byte[] str2BASE64(String str) {
        try {
            return Base64.decodeBase64(str);
        } catch (Exception e) {
            logger.error("str2BASE64 失败.", e);
        }
        return new byte[0];
    }


    /**
     * 判断字符串是否全是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        if (null == str || StringUtils.isBlank(str)) {
            return false;
        }
        return numberPattern.matcher(str).matches();
    }

    /**
     * 判断是否是小数（整数也认为是小数的特殊情况）
     * StringUtils.isNumeric("ab2c") = false
     * StringUtils.isNumeric("12-3") = false
     * StringUtils.isNumeric("12.3") = true
     *
     * @param str
     * @return
     */
    public static boolean isDecimal(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        return decimalPattern.matcher(str).matches();
    }

    /**
     * 将多个string合并为一个字符串,并添加一个5位随机数
     *
     * @param div
     * @param strs
     * @return
     */
    public static String packString(String div, String... strs) {
        StringBuilder sb = new StringBuilder();
        for (String str : strs) {
            sb.append(str).append(div);
        }
        return sb.append(genRandomNum(5)).toString().toUpperCase();
    }

    /**
     * 生成指定长度的随机数
     *
     * @param len
     * @return
     */
    public static String genRandomNum(int len) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < len; i++) {
            result.append(Integer.toString(new Random().nextInt(10)));
        }
        return result.toString();
    }

    /**
     * 功能说明 : 生成指定长度的随机字符串
     *
     * @param length
     * @return
     */
    public static String genRandomStr(int length) { // length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 功能说明：是否为手机号。
     *
     * @param phoneNo
     * @return
     */
    public static boolean isPhoneNo(String phoneNo) {
        if (StringUtils.isBlank(phoneNo) || !StringUtils.isNumeric(phoneNo)) {
            return false;
        } else if (phoneNo.startsWith("1") && phoneNo.length() == 11) {
            return true;
        }
        return false;
    }

    /**
     * 功能说明：是否为email。
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {

        if (StringUtils.isBlank(email)) { //空字符串，视为合法
            return true;
        } else if (!email.matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+")) {
            return false;
        }
        return true;
    }

    public static boolean isDateTime(String dateTimeStr) {
        return StringTools.isDateTime(dateTimeStr, TIME_FORMAT);
    }

    public static boolean isDateTime(String dateTimeStr, String format) {
        if (StringUtils.isBlank(dateTimeStr)) {
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format);

        try {
            sdf.parse(dateTimeStr);
            return true;
        } catch (ParseException e) {
            logger.error("日期字符串解析失败。dateTimeStr : " + dateTimeStr, e);
        }

        return false;
    }

    /**
     * 功能说明 ：是否是数字和逗号,如1，2，3 用于类似数据库id拼接
     * author	Zain 2016年6月24日  下午5:16:42
     *
     * @return
     */
    public static boolean isIds(String str) {
        if (null == str || StringUtils.isBlank(str)) {
            return false;
        }
        return numberSplitByCommaPattern.matcher(str).matches();
    }

    public static float str2Float(String str) {
        return obj2Float(str, -1f);
    }

    public static float obj2Float(Object obj) {
        return StringTools.obj2Float(obj, 0f);
    }

    public static float obj2Float(Object obj, float defaultVal) {
        String str = StringTools.nvl(obj);

        if (!StringUtils.isBlank(str)) {
            try {
                return Float.parseFloat(str);
            } catch (Exception e) {
                logger.error("对象转换为Float失败。", e);
                return defaultVal;
            }
        }

        return defaultVal;
    }

    /**
     * 功能说明：使用给定的分隔符格式化MAC地址。
     *
     * @param macNoSeparator 无分隔符的MAC地址
     * @param separator      分隔符
     * @return 格式化后的MAC地址
     */
    public static String formatMac(String macNoSeparator, String separator) {
        if (StringUtils.isEmpty(macNoSeparator)) {
            logger.warn(String.format("源MAC地址：%s 为空，直接返回。", macNoSeparator));
            return macNoSeparator;
        }

        if (macNoSeparator.contains(separator)) {
            logger.warn(String.format("源MAC地址：%s 包含分隔符：%s，直接返回。", macNoSeparator, separator));
            return macNoSeparator;
        }

        if (macNoSeparator.length() != 12) {
            logger.warn(String.format("源MAC地址：%s 长度有误，直接返回。", macNoSeparator));
            return macNoSeparator;
        }


        String[] macArray = new String[6];
        for (int i = 0; i <= 5; i++) {
            macArray[i] = macNoSeparator.substring(i * 2, i * 2 + 2);
        }

        StringBuilder sbMac = new StringBuilder(macArray[0]);
        for (int i = 1; i < macArray.length; i++) {
            sbMac.append(separator);
            sbMac.append(macArray[i]);
        }

        return sbMac.toString();
    }

    /**
     * 功能说明:读取文件到String中
     *
     * @param filePath String
     * @return String
     */
    public static String file2String(String filePath) {
        StringBuilder result = new StringBuilder();
        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
             BufferedReader br = new BufferedReader(inputStreamReader);) {//构造一个BufferedReader类来读取文件
            String s;
            while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
                result.append(s);
            }
        } catch (IOException e) {
            logger.error("读取文件失败,file:" + filePath, e);
        }
        return result.toString();
    }

    /**
     * 功能说明 ：整数相除保留一位小数
     *
     * @param a
     * @param b
     * @return
     */
    public static String division(int a, int b) {
        String result = "";
        float num = (float) a / b;

        DecimalFormat df = new DecimalFormat("0.0");
        result = df.format(num);
        return result;
    }

    /**
     * * 功能说明: 对一个number格式的字符串进行四舍五入，保留指定位数
     *
     * @param doubleStr String
     * @param scale     int
     * @return double
     */
    public static double roundForNumber(String doubleStr, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(doubleStr);
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 功能说明: 对一个number进行四舍五入，保留指定位数
     *
     * @param number double
     * @param scale  int
     * @return double
     */
    public static double roundForNumber(double number, int scale) {
        return roundForNumber(Double.toString(number), scale);
    }
}

