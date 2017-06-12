package com.github.liaoheng.common.util;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 数字处理工具
 *
 * @author liaoheng
 * @version 2016-12-30
 */
public class NumberUtils {

    private static DecimalFormat formatDecimal2 = new DecimalFormat("0.00");
    private static DecimalFormat formatDecimal1 = new DecimalFormat("0.0");
    private static DecimalFormat formatDecimal0 = new DecimalFormat("0");

    /** number to string **/
    public static String getNumberToString(int number) {
        return String.valueOf(number);
    }

    public static String getNumberToString(long number) {
        return String.valueOf(number);
    }

    public static String getNumberToString(double number) {
        return String.valueOf(number);
    }

    /** string to number **/
    public static int getStringToInt(String txt) {
        if (TextUtils.isEmpty(txt)) {
            return 0;
        } else {
            return Integer.parseInt(txt);
        }
    }

    public static long getStringToLong(String txt) {
        if (TextUtils.isEmpty(txt)) {
            return 0L;
        } else {
            return Long.parseLong(txt);
        }
    }

    public static float getStringToFloat(String txt) {
        if (TextUtils.isEmpty(txt)) {
            return 0F;
        } else {
            return Float.parseFloat(txt);
        }
    }

    public static double getStringToDouble(String txt) {
        if (TextUtils.isEmpty(txt)) {
            return 0F;
        } else {
            return Double.parseDouble(txt);
        }
    }

    /** format  number to string **/
    public static String formatNumber0ToString(BigDecimal number) {
        return formatDecimal0.format(getBigDecimal(number));
    }

    public static String formatNumber0ToString(double number) {
        return formatDecimal0.format(number);
    }

    public static String formatNumber0ToString(float number) {
        return formatDecimal0.format(number);
    }

    public static String formatNumber1ToString(BigDecimal number) {
        return formatDecimal1.format(number);
    }

    public static String formatNumber1ToString(double number) {
        return formatDecimal1.format(number);
    }

    public static String formatNumber2ToString(BigDecimal number) {
        return formatDecimal2.format(getBigDecimal(number));
    }

    public static String formatNumber2ToString(double number) {
        return formatDecimal2.format(number);
    }

    public static String formatNumber2ToString(float number) {
        return formatDecimal2.format(number);
    }

    /** format number to string **/
    public static double formatNumber0ToDouble(BigDecimal number) {
        return Double.parseDouble(formatDecimal0.format(number));
    }

    public static double formatNumber1ToDouble(BigDecimal number) {
        return Double.parseDouble(formatDecimal1.format(number));
    }

    public static double formatNumber2ToDouble(BigDecimal number) {
        return Double.parseDouble(formatDecimal2.format(number));
    }

    /** 单位转换 **/

    /**
     * kg to 英磅(lb)
     * http://www.metric-conversions.org/zh-hans/weight/kilograms-to-pounds.htm
     */
    @NonNull
    public static BigDecimal kgToLb(BigDecimal kg) {
        return bigDecimalDivide(getBigDecimal(kg), new BigDecimal("0.45359237"), 10, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * kg to 英磅(lb)
     * http://www.metric-conversions.org/zh-hans/weight/kilograms-to-pounds.htm
     * <br/>使用字符可以避免科学计数在运算时的数据误差
     */
    @NonNull
    public static BigDecimal kgToLb(double kg) {
        return kgToLb(new BigDecimal(Double.toString(kg)));
    }

    /**
     * kg to 英石(st)
     * http://www.metric-conversions.org/zh-hans/weight/kilograms-to-stones.htm
     */
    @NonNull
    public static BigDecimal kgToSt(BigDecimal kg) {
        return bigDecimalMultiply(getBigDecimal(kg), new BigDecimal("0.15747"));
    }

    /**
     * kg to 英石(st)
     * http://www.metric-conversions.org/zh-hans/weight/kilograms-to-stones.htm
     * <br/>使用字符可以避免科学计数在运算时的数据误差
     */
    public static BigDecimal kgToSt(double kg) {
        return kgToSt(new BigDecimal(Double.toString(kg)));
    }

    /**
     * kg to st:lb
     */
    public static String kgToStLb(BigDecimal kg) {
        BigDecimal lb = kgToLb(kg);
        return lbToStLb(lb);
    }

    /**
     * kg to st:lb
     * <br/>使用字符可以避免科学计数在运算时的数据误差
     */
    public static String kgToStLb(double kg) {
        return kgToStLb(new BigDecimal(Double.toString(kg)));
    }

    /**
     * 英磅(lb) to kg
     * http://www.metric-conversions.org/zh-hans/weight/pounds-to-kilograms.htm
     */
    @NonNull
    public static BigDecimal lbToKg(BigDecimal lb) {
        return bigDecimalDivide(getBigDecimal(lb), new BigDecimal("2.204622622"), 10, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 英磅(lb) to kg
     * http://www.metric-conversions.org/zh-hans/weight/pounds-to-kilograms.htm
     * <br/>使用字符可以避免科学计数在运算时的数据误差
     */
    @NonNull
    public static BigDecimal lbToKg(double lb) {
        return lbToKg(new BigDecimal(Double.toString(lb)));
    }

    /**
     * 英磅(lb) to 英石(st)
     * http://www.metric-conversions.org/weight/pounds-to-stones.htm
     */
    public static BigDecimal lbToSt(BigDecimal lb) {
        return bigDecimalDivide(getBigDecimal(lb), new BigDecimal("14.0"), 10, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 英磅(lb) to 英石(st)
     * http://www.metric-conversions.org/weight/pounds-to-stones.htm
     * <br/>使用字符可以避免科学计数在运算时的数据误差
     */
    public static BigDecimal lbToSt(double lb) {
        return lbToSt(new BigDecimal(Double.toString(lb)));
    }

    /**
     * lb to st:lb
     */
    public static String lbToStLb(BigDecimal lb) {
        if (isZero(lb)) {
            return "0:0";
        }
        BigDecimal st = lbToSt(lb);
        String s = st.toString();
        String[] split = StringUtils.split(s, ".");
        String t = split[0];
        BigDecimal v = BigDecimal.ZERO;
        if (split.length == 2) {
            String l = split[1];
            v = new BigDecimal("0." + l);
            v = NumberUtils.stToLb(v);
        }
        return t + ":" + formatNumber1ToString(v);
    }

    /**
     * lb to st:lb
     * <br/>使用字符可以避免科学计数在运算时的数据误差
     */
    public static String lbToStLb(double lb) {
        return lbToStLb(new BigDecimal(Double.toString(lb)));
    }

    /**
     * 英石(st) to kg
     * http://www.metric-conversions.org/zh-hans/weight/stones-to-kilograms.htm
     */
    @NonNull
    public static BigDecimal stToKg(BigDecimal st) {
        return bigDecimalDivide(getBigDecimal(st), new BigDecimal("0.15747"), 10, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 英石(st) to kg
     * http://www.metric-conversions.org/zh-hans/weight/stones-to-kilograms.htm
     * <br/>使用字符可以避免科学计数在运算时的数据误差
     */
    @NonNull
    public static BigDecimal stToKg(double st) {
        return stToKg(new BigDecimal(Double.toString(st)));
    }

    /**
     * 英石(st) to lb
     * http://www.metric-conversions.org/zh-hans/weight/stones-to-pounds.htm
     */
    @NonNull
    public static BigDecimal stToLb(BigDecimal st) {
        return bigDecimalMultiply(getBigDecimal(st), new BigDecimal("14.0"));
    }

    /**
     * 英石(st) to lb
     * http://www.metric-conversions.org/zh-hans/weight/stones-to-pounds.htm
     * <br/>使用字符可以避免科学计数在运算时的数据误差
     */
    @NonNull
    public static BigDecimal stToLb(double st) {
        return stToLb(new BigDecimal(Double.toString(st)));
    }

    /**
     * st:lb to kg
     */
    @NonNull
    public static BigDecimal stLbToKg(String stLb) {
        String[] split = StringUtils.split(stLb, ":");
        String t = split[0];
        String l = split[1];
        BigDecimal v = BigDecimal.ONE;
        if (split.length == 2) {
            BigDecimal lb = stToLb(new BigDecimal(t));
            v = lb.add(new BigDecimal(l));
        }
        return lbToKg(v);
    }

    /** BigDecimal  计算 与工具 **/

    @NonNull
    public static BigDecimal getBigDecimal(BigDecimal number) {
        return number == null ? BigDecimal.ZERO : number;
    }

    @NonNull
    public static BigDecimal getBigDecimalByString(String number) {
        if (TextUtils.isEmpty(number)) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(number);
    }

    public static boolean isZero(BigDecimal number) {
        return getBigDecimal(number).compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * 减法
     */
    @NonNull
    public static BigDecimal bigDecimalSubtract(BigDecimal b1, BigDecimal b2) {
        return getBigDecimal(b1).subtract(getBigDecimal(b2));
    }

    /**
     * 加法
     */
    @NonNull
    public static BigDecimal bigDecimalAdd(BigDecimal b1, BigDecimal b2) {
        return getBigDecimal(b1).add(getBigDecimal(b2));
    }

    /**
     * 乘法
     */
    @NonNull
    public static BigDecimal bigDecimalMultiply(BigDecimal b1, BigDecimal b2) {
        return getBigDecimal(b1).multiply(getBigDecimal(b2));
    }

    /**
     * 除法 See: {@link BigDecimal#divide(BigDecimal, int, int)}
     */
    @NonNull
    public static BigDecimal bigDecimalDivide(BigDecimal b1, BigDecimal b2, int scale,
            int roundingMode) {
        return getBigDecimal(b1).divide(getBigDecimal(b2), scale, roundingMode);
    }

    /**
     * 除法  取5位小数使用BigDecimal.ROUND_HALF_UP四舍五入
     */
    @NonNull
    public static BigDecimal bigDecimalDivide(BigDecimal b1, BigDecimal b2) {
        return bigDecimalDivide(b1, b2, 5, BigDecimal.ROUND_HALF_UP);
    }
}
