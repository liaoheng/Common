package com.github.liaoheng.common.util;

import android.content.Context;

/**
 * 常用单位转换的辅助类
 *
 *
 *
 */
@Deprecated
public class DensityUtils
{
    private DensityUtils()
    {
        /** cannot be instantiated **/
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * dp转px
     *
     * @param context
     * @param dpVal
     * @return
     */
    @Deprecated
    public static int dp2px(Context context, float dpVal)
    {
        return DisplayUtils.dp2px(context,dpVal);
    }

    /**
     * sp转px
     *
     * @param context
     * @param spVal
     * @return
     */
    @Deprecated
    public static int sp2px(Context context, float spVal)
    {
        return DisplayUtils.sp2px(context,spVal);
    }

    /**
     * px转dp
     *
     * @param context
     * @param pxVal
     * @return
     */
    @Deprecated
    public static float px2dp(Context context, float pxVal)
    {
        return DisplayUtils.px2dp(context,pxVal);
    }
    /**
     * px转sp
     *
     * @param context
     * @param pxVal
     * @return
     */
    @Deprecated
    public static float px2sp(Context context, float pxVal)
    {
        return DisplayUtils.px2sp(context,pxVal);
    }

}
