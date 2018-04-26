package com.github.liaoheng.common.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.liaoheng.common.Common;

/**
 * 日志处理
 *
 * @author liaoheng
 * @version 2015-07-21 20:05:42
 * @see <a href='https://github.com/orhanobut/logger'>Logger</a>
 */
public class L {
    /**
     * LOG INIT
     *
     * @param tag global
     * @param isLoggable 是否开启日志
     */
    public static void init(String tag, boolean isLoggable) {
        Logcat.get().init(tag, isLoggable);
    }

    /**
     * LOG INIT 默认打开日志
     *
     * @param tag global
     */
    public static void init(String tag) {
        Logcat.get().init(tag, true);
    }

    /**
     * LOG INIT 默认使用项目名打开日志
     */
    public static void init(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            init(context.getResources().getString(labelRes));
        } catch (PackageManager.NameNotFoundException ignored) {
            init(Common.getProjectName());
        }
    }

    /**
     * 是否打开日志
     *
     * @return true开，false关
     */
    public static boolean isPrint() {
        return Logcat.isPrint();
    }

    /**
     * JSON
     *
     * @param message json string
     */
    @Deprecated
    public static void json(String message) {
        L.log().json(message);
    }

    /**
     * JSON
     *
     * @param TAG LOG TAG
     * @param message json string
     */
    @Deprecated
    public static void json(String TAG, String message) {
        L.log().json(TAG, message);
    }

    /**
     * XML
     *
     * @param message xml string
     */
    @Deprecated
    public static void xml(String message) {
        L.log().xml(message);
    }

    /**
     * XML
     *
     * @param TAG LOG TAG
     * @param message xml string
     */
    @Deprecated
    public static void xml(String TAG, String message) {
        L.log().xml(TAG, message);
    }

    /**
     * DEBUG
     *
     * @param message 提示模板 %s
     * @param parameter 参数
     */
    @Deprecated
    public static void d(String message, Object... parameter) {
        L.log().d(message, parameter);
    }

    /**
     * DEBUG
     *
     * @param TAG LOG TAG
     * @param message 提示模板 %s
     * @param parameter 参数
     */
    @Deprecated
    public static void d(String TAG, String message, Object... parameter) {
        L.log().d(TAG, message, parameter);
    }

    /**
     * INFO
     *
     * @param message 提示模板 %s
     * @param parameter 参数
     */
    @Deprecated
    public static void i(String message, Object... parameter) {
        L.log().i(message, parameter);
    }

    /**
     * INFO
     *
     * @param TAG LOG TAG
     * @param message 提示模板 %s
     * @param parameter 参数
     */
    @Deprecated
    public static void i(String TAG, String message, Object... parameter) {
        L.log().i(TAG, message, parameter);
    }

    /**
     * WARN
     *
     * @param message 提示模板 %s
     * @param parameter 参数
     */
    @Deprecated
    public static void w(String message, Object... parameter) {
        L.log().i(message, parameter);
    }

    /**
     * WARN
     *
     * @param TAG LOG TAG
     * @param message 提示模板 %s
     * @param parameter 参数
     */
    @Deprecated
    public static void w(String TAG, String message, Object... parameter) {
        L.log().w(TAG, message, parameter);
    }

    /**
     * ERROR
     *
     * @param e {@link Throwable}
     * @param message 错误信息 %s站位
     * @param parameter 错误信息内容
     */
    @Deprecated
    public static void e(Throwable e, String message, Object... parameter) {
        L.log().e(e, message, parameter);
    }

    /**
     * ERROR
     *
     * @param TAG LOG TAG
     * @param e {@link Throwable}
     * @param message 错误信息 %s站位
     * @param parameter 错误信息内容
     */
    @Deprecated
    public static void e(String TAG, Throwable e, String message, Object... parameter) {
        L.log().e(TAG, e, message, parameter);
    }

    /**
     * ERROR
     *
     * @param message 错误信息 %s站位
     * @param parameter 错误信息内容
     */
    @Deprecated
    public static void e(String message, Object... parameter) {
        L.log().e(message, parameter);
    }

    /**
     * ERROR
     *
     * @param TAG LOG TAG
     * @param message 错误信息 %s站位
     * @param parameter 错误信息内容
     */
    @Deprecated
    public static void e(String TAG, String message, Object... parameter) {
        L.log().e(TAG, message, parameter);
    }

    /**
     * ERROR
     *
     * @param TAG LOG TAG
     * @param e {@link Throwable}
     */
    @Deprecated
    public static void e(String TAG, @NonNull Throwable e) {
        L.log().e(TAG, e);
    }

    /**
     * {@link android.util.Log}
     */
    @Deprecated
    public static class Log {

        /**
         * DEBUG
         *
         * @param message 提示模板 %s
         * @param parameter 参数
         */
        public static void d(@NonNull String message, @Nullable Object... parameter) {
            L.alog().d(message, parameter);
        }

        /**
         * DEBUG
         *
         * @param TAG LOG TAG
         * @param message 提示模板 %s
         * @param parameter 参数
         */
        public static void d(String TAG, @NonNull String message, @Nullable Object... parameter) {
            L.alog().d(TAG, message, parameter);
        }

        /**
         * INFO
         *
         * @param message 提示模板 %s
         * @param parameter 参数
         */
        public static void i(@NonNull String message, @Nullable Object... parameter) {
            L.alog().i(message, parameter);
        }

        /**
         * INFO
         *
         * @param TAG LOG TAG
         * @param message 提示模板 %s
         * @param parameter 参数
         */
        public static void i(String TAG, @NonNull String message, @Nullable Object... parameter) {
            L.alog().i(TAG, message, parameter);
        }

        /**
         * WARN
         *
         * @param message 提示模板 %s
         * @param parameter 参数
         */
        public static void w(@NonNull String message, @Nullable Object... parameter) {
            L.alog().w(message, parameter);
        }

        /**
         * WARN
         *
         * @param TAG LOG TAG
         * @param message 提示模板 %s
         * @param parameter 参数
         */
        public static void w(String TAG, @NonNull String message, @Nullable Object... parameter) {
            L.alog().w(TAG, message, parameter);
        }

        /**
         * WARN
         *
         * @param e {@link Throwable}
         * @param message 提示模板 %s
         * @param parameter 参数
         */
        public static void w(@Nullable Throwable e, @NonNull String message, @Nullable Object... parameter) {
            L.alog().w(e, message, parameter);
        }

        /**
         * WARN
         *
         * @param TAG LOG TAG
         * @param e {@link Throwable}
         * @param message 提示模板 %s
         * @param parameter 参数
         */
        public static void w(String TAG, @Nullable Throwable e, @NonNull String message,
                @Nullable Object... parameter) {
            L.alog().w(TAG, e, message, parameter);
        }

        /**
         * WARN
         *
         * @param e {@link Throwable}
         */
        public static void w(@Nullable Throwable e) {
            L.alog().w(e);
        }

        /**
         * WARN
         *
         * @param TAG LOG TAG
         * @param e {@link Throwable}
         */
        public static void w(String TAG, @Nullable Throwable e) {
            L.alog().w(TAG, e);
        }

        /**
         * ERROR
         *
         * @param e {@link Throwable}
         * @param message 错误信息 %s站位
         * @param parameter 错误信息内容
         */
        public static void e(Throwable e, @NonNull String message,
                @Nullable Object... parameter) {
            L.alog().e(e, message, parameter);
        }

        /**
         * ERROR
         *
         * @param TAG LOG TAG
         * @param e {@link Throwable}
         * @param message 错误信息 %s站位
         * @param parameter 错误信息内容
         */
        public static void e(String TAG, @NonNull Throwable e, @NonNull String message,
                @Nullable Object... parameter) {
            L.alog().e(TAG, e, message, parameter);
        }

        /**
         * ERROR
         *
         * @param message 错误信息 %s站位
         * @param parameter 错误信息内容
         */
        public static void e(@NonNull String message, @Nullable Object... parameter) {
            L.alog().e(message, parameter);
        }

        /**
         * ERROR
         *
         * @param TAG LOG TAG
         * @param message 错误信息 %s站位
         * @param parameter 错误信息内容
         */
        public static void e(String TAG, @NonNull String message, @Nullable Object... parameter) {
            L.alog().e(TAG, message, parameter);
        }

        /**
         * ERROR
         *
         * @param e {@link Throwable}
         */
        public static void e(@NonNull Throwable e) {
            L.alog().e(e);
        }

        /**
         * ERROR
         *
         * @param TAG LOG TAG
         * @param e {@link Throwable}
         */
        public static void e(String TAG, @NonNull Throwable e) {
            L.alog().e(TAG, e);
        }
    }

    public static Logcat.ILogger log() {
        return Logcat.get().logger();
    }

    public static Logcat.ILogcat alog() {
        return Logcat.get().log();
    }

    public static Logcat.ILogToast getToast() {
        return Logcat.get().getToast();
    }

    public static Logcat.ILogSnack getSnack() {
        return Logcat.get().getSnack();
    }
}
