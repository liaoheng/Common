package com.github.liaoheng.common.util;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * 数字处理工具
 * @author liaoheng
 * @version 2016-12-30
 */
public class NumberUtils {

    private static DecimalFormat formatDecimal2 = new DecimalFormat("0.00");
    private static DecimalFormat formatDecimal1 = new DecimalFormat("0.0");
    private static DecimalFormat formatDecimal0 = new DecimalFormat("0");

    static {
        formatDecimal2.setRoundingMode(RoundingMode.DOWN);
        formatDecimal1.setRoundingMode(RoundingMode.DOWN);
        formatDecimal0.setRoundingMode(RoundingMode.DOWN);
    }

    /**--  int -**/
    public static int getStringToInt(String txt) {
        if (TextUtils.isEmpty(txt)) {
            return 0;
        } else {
            return Integer.parseInt(txt);
        }
    }

    public static String getNumberToString(int number) {
        return String.valueOf(number);
    }

    /**--  long -**/
    public static long getStringToLong(String txt) {
        if (TextUtils.isEmpty(txt)) {
            return 0L;
        } else {
            return Long.parseLong(txt);
        }
    }

    public static String getNumberToString(long number) {
        return String.valueOf(number);
    }

    /**--  float -**/
    public static float getStringToFloat(String txt) {
        if (TextUtils.isEmpty(txt)) {
            return 0F;
        } else {
            return Float.parseFloat(txt);
        }
    }

    public static String getNumberToString(float number) {
        return String.valueOf(number);
    }

    public static String formatNumber2ToString(float number) {
        return formatDecimal2.format(number);
    }

    public static String formatNumber0ToString(float number) {
        return formatDecimal0.format(number);
    }

    /**--  double -**/
    public static double getStringToDouble(String txt) {
        if (TextUtils.isEmpty(txt)) {
            return 0F;
        } else {
            return Double.parseDouble(txt);
        }
    }

    public static String getNumberToString(double number) {
        return String.valueOf(number);
    }

    public static String formatNumber2ToString(double number) {
        return formatDecimal2.format(number);
    }

    public static String formatNumber0ToString(double number) {
        return formatDecimal0.format(number);
    }

    /**--  Decimal -**/

    @NonNull public static BigDecimal getBigDecimal(BigDecimal bigDecimal) {
        return bigDecimal == null ? new BigDecimal(0) : bigDecimal;
    }

    public static BigDecimal getStringToBigDecimal(String bigDecimal) {
        if (TextUtils.isEmpty(bigDecimal)) {
            return new BigDecimal(0);
        }
        return new BigDecimal(bigDecimal);
    }

    public static String getNumberToString(BigDecimal bigDecimal) {
        return formatNumber2ToString(bigDecimal);
    }

    public static String formatNumber2ToString(BigDecimal bigDecimal) {
        return formatDecimal2.format(getBigDecimal(bigDecimal));
    }

    public static String formatNumber1ToString(double number) {
        return formatDecimal1.format(number);
    }

    public static String formatNumber0ToString(BigDecimal bigDecimal) {
        return formatDecimal0.format(getBigDecimal(bigDecimal));
    }

    /**
     * kg to 英磅(lb)
     * http://www.metric-conversions.org/zh-hans/weight/kilograms-to-pounds.htm
     */
    @NonNull public static BigDecimal kgToLb(BigDecimal bigDecimal) {
        return bigDecimalMultiply(getBigDecimal(bigDecimal), new BigDecimal(2.2046));
    }

    /**
     * kg to 英磅(lb)
     * http://www.metric-conversions.org/zh-hans/weight/kilograms-to-pounds.htm
     */
    public static double kgToLb(double kg) {
        return kg * 2.2046;
    }

    /**
     * kg to 英石(st)
     * http://www.metric-conversions.org/zh-hans/weight/kilograms-to-stones.htm
     */
    @NonNull public static BigDecimal kgToSt(BigDecimal bigDecimal) {
        return bigDecimalMultiply(getBigDecimal(bigDecimal), new BigDecimal(0.15747));
    }

    /**
     * kg to 英石(st)
     * http://www.metric-conversions.org/zh-hans/weight/kilograms-to-stones.htm
     */
    public static double kgToSt(double kg) {
        return kg * 0.15747;
    }

    /**
     * 英磅(lb) to kg
     * http://www.metric-conversions.org/zh-hans/weight/pounds-to-kilograms.htm
     */
    @NonNull public static BigDecimal lbToKg(BigDecimal bigDecimal) {
        return bigDecimalDivide(getBigDecimal(bigDecimal), new BigDecimal(2.2046));
    }

    /**
     * 英磅(lb) to kg
     * http://www.metric-conversions.org/zh-hans/weight/pounds-to-kilograms.htm
     */
    public static double lbToKg(double lb) {
        return lb / 2.2046;
    }

    /**
     * 英磅(lb) to 英石(st)
     * http://www.metric-conversions.org/weight/pounds-to-stones.htm
     */
    public static double lbToFt(double lb) {
        return lb * 0.071429;
    }

    /**
     *  英石(st) to kg
     * http://www.metric-conversions.org/zh-hans/weight/stones-to-kilograms.htm
     */
    @NonNull public static BigDecimal stToKg(BigDecimal bigDecimal) {
        return bigDecimalDivide(getBigDecimal(bigDecimal), new BigDecimal(0.15747));
    }

    /**
     *  英石(st) to kg
     * http://www.metric-conversions.org/zh-hans/weight/stones-to-kilograms.htm
     */
    public static double stToKg(double st) {
        return st / 0.15747;
    }

    /**
     *  英石(st) to lb
     * http://www.metric-conversions.org/zh-hans/weight/stones-to-pounds.htm
     */
    @NonNull public static BigDecimal stToLb(BigDecimal bigDecimal) {
        return bigDecimalMultiply(getBigDecimal(bigDecimal), new BigDecimal(14.000));
    }

    /**
     *  英石(st) to lb
     * http://www.metric-conversions.org/zh-hans/weight/stones-to-pounds.htm
     */
    public static double stToLb(double st) {
        return st * 14.000;
    }

    /**
     * kg to st:lb
     */
    public static String kgToStLb(double kg) {
        double st = NumberUtils.kgToSt(kg);
        String s = String.valueOf(st);
        String[] split = StringUtils.split(s, ".");
        String t = split[0];
        String l = split[1];
        double v = Double.parseDouble("0." + l);
        v = NumberUtils.stToLb(v);
        return t + ":" + NumberUtils.formatNumber1ToString(v);
    }

    /**
     * lb to st:lb
     */
    public static String lbToStLb(double lb) {
        double st = NumberUtils.lbToFt(lb);
        String s = String.valueOf(st);
        String[] split = StringUtils.split(s, ".");
        String t = split[0];
        String l = split[1];
        double v = Double.parseDouble("0." + l);
        v = NumberUtils.stToLb(v);
        return t + ":" + NumberUtils.formatNumber1ToString(v);
    }

    /**
     * st:lb to kg
     */
    public static double stLbToKg(String stLb) {
        String[] split = StringUtils.split(stLb, ":");
        String t = split[0];
        String l = split[1];
        double st = stToKg(Double.parseDouble(t));
        double lb = lbToKg(Double.parseDouble(l));
        return st + lb;
    }

    /**
     * 减法
     */
    @NonNull public static BigDecimal bigDecimalSubtract(BigDecimal b1, BigDecimal b2) {
        return getBigDecimal(b1).subtract(getBigDecimal(b2));
    }

    /**
     * 加法
     */
    @NonNull public static BigDecimal bigDecimalAdd(BigDecimal b1, BigDecimal b2) {
        return getBigDecimal(b1).add(getBigDecimal(b2));
    }

    /**
     * 乘法
     */
    @NonNull public static BigDecimal bigDecimalMultiply(BigDecimal b1, BigDecimal b2) {
        return getBigDecimal(b1).multiply(getBigDecimal(b2));
    }

    /**
     * 除法 See: {@link BigDecimal#divide(BigDecimal, int, int)}
     */
    @NonNull public static BigDecimal bigDecimalDivide(BigDecimal b1, BigDecimal b2, int scale,
                                                       int roundingMode) {
        return getBigDecimal(b1).divide(getBigDecimal(b2), scale, roundingMode);
    }

    /**
     * 除法  取3位小数使用BigDecimal.ROUND_DOWN取整
     */
    @NonNull public static BigDecimal bigDecimalDivide(BigDecimal b1, BigDecimal b2) {
        return bigDecimalDivide(b1, b2, 3, BigDecimal.ROUND_DOWN);
    }
}
