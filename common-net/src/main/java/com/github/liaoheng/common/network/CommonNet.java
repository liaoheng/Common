package com.github.liaoheng.common.network;

import android.content.Context;

/**
 * @author liaoheng
 * @version 2016-09-22 10:57
 */
public class CommonNet {
    public static String DISK_CACHE_DIR = "imgCache";
    public static String HTTP_CACHE_DIR = "httpCache";
    public static long IMAGE_DISK_CACHE_SIZE = 500 * 1024 * 1024; // 500MB;

    public static void init(Context context) {
        OkHttp3Utils.init().setDefaultCache(context).initialization();
    }
}
