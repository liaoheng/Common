package com.github.liaoheng.common.util;

import android.os.Handler;
import android.os.Looper;

/**
 * @author MaTianyu
 * @author liaoheng
 * @version 2016-11-30
 */
@SuppressWarnings("WeakerAccess") public class HandlerUtils {
    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    public static void runOnUiThread(Runnable runnable) {
        post(runnable);
    }

    public static void runOnUiThreadDelay(Runnable runnable, long delayMillis) {
        post(runnable, delayMillis);
    }

    public static void removeRunable(Runnable runnable) {
        HANDLER.removeCallbacks(runnable);
    }

    public static void post(Runnable runnable) {
        HANDLER.post(runnable);
    }

    public static void post(Runnable runnable, long delayMillis) {
        HANDLER.postDelayed(runnable, delayMillis);
    }
}
