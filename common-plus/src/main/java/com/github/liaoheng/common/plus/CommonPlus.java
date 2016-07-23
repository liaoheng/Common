package com.github.liaoheng.common.plus;

import android.content.Context;
import com.github.liaoheng.common.Common;
import com.github.liaoheng.common.plus.util.OkHttp3Utils;
import com.github.liaoheng.common.plus.util.OkHttpUtils;
import com.github.liaoheng.common.plus.util.PicassoUtils;
import com.github.liaoheng.common.util.PreferencesUtils;

/**
 * @author liaoheng
 * @version 2015-11-4 10:47:27
 */
public class CommonPlus {
    public static String DISK_CACHE_DIR        = "imgCache";
    public static long   IMAGE_DISK_CACHE_SIZE = 500 * 1024 * 1024; // 500MB;

    public static void init(Context context, String projectName, boolean isDebug) {
        Common.init(context, projectName, isDebug);
        OkHttpUtils.init().setDefaultCache().initialization();
        PicassoUtils.init().setDefaultDownloader().setDebug(isDebug).initialization(context);
        PreferencesUtils.init(context);
    }

    public static void init2(Context context, String projectName, boolean isDebug) {
        Common.init(context, projectName, isDebug);
        OkHttp3Utils.init().setDefaultCache().initialization();
        PreferencesUtils.init(context);
    }
}
