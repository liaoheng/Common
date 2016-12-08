package com.github.liaoheng.common.util;

import android.support.annotation.NonNull;
import java.math.BigDecimal;

/**
 * 数字处理工具
 * @author liaoheng
 * @version 2016-11-25 16:39
 */
public class NumberUtils {

    public static BigDecimal getBigDecimal(BigDecimal bigDecimal) {
        return bigDecimal == null ? new BigDecimal(0) : bigDecimal;
    }

    /**
     * 减法
     * @param b1
     * @param b2
     * @return
     */
    @NonNull public static BigDecimal bigDecimalSubtract(BigDecimal b1, BigDecimal b2) {
        return getBigDecimal(b1).subtract(getBigDecimal(b2));
    }

    /**
     * 加法
     * @param b1
     * @param b2
     * @return
     */
    @NonNull public static BigDecimal bigDecimalAdd(BigDecimal b1, BigDecimal b2) {
        return getBigDecimal(b1).add(getBigDecimal(b2));
    }

    /**
     * 乘法
     * @param b1
     * @param b2
     * @return
     */
    @NonNull public static BigDecimal bigDecimalMultiply(BigDecimal b1, BigDecimal b2) {
        return getBigDecimal(b1).multiply(getBigDecimal(b2));
    }

    /**
     * 除法
     * @param b1
     * @param b2
     * @param scale {@link BigDecimal#divide(BigDecimal, int, int)}
     * @param roundingMode
     * @return
     */
    @NonNull public static BigDecimal bigDecimalDivide(BigDecimal b1, BigDecimal b2, int scale,
                                                       int roundingMode) {
        return getBigDecimal(b1).divide(getBigDecimal(b2), scale, roundingMode);
    }

    /**
     * 除法   取3位 BigDecimal.ROUND_DOWN取整
     * @param b1
     * @param b2
     * @return
     */
    @NonNull public static BigDecimal bigDecimalDivide(BigDecimal b1, BigDecimal b2) {
        return bigDecimalDivide(b1, b2, 3, BigDecimal.ROUND_DOWN);
    }
}