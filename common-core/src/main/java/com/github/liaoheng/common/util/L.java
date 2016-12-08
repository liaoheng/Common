package com.github.liaoheng.common.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.View;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.Settings;

/**
 * 日志处理
 *
 * @author liaoheng
 * @version 2015-07-21 20:05:42
 */
public class L {
    private static final String TAG = L.class.getSimpleName();

    public static boolean DEBUG = true;

    /**
     * LOG INIT
     *
     * @param tag     {@link Logger#init(String)}
     * @param isDebug 是否开启日志
     */
    public static void init(String tag, boolean isDebug) {
        Settings init = Logger.init(tag);
        if (!L.isPrint()) {
            init.setLogLevel(LogLevel.NONE);
        }
        DEBUG = isDebug;
    }

    /**
     * LOG INIT 默认打开日志
     *
     * @param tag {@link Logger#init(String)}
     */
    public static void init(String tag) {
        init(tag, true);
    }

    /**
     * LOG INIT 默认使用项目名打开日志
     *
     * @param context {@link Context}
     */
    public static void init(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            init(context.getResources().getString(labelRes));
        } catch (PackageManager.NameNotFoundException e) {
            L.Log.e(TAG, e);
        }
    }

    /**
     * 是否打开日志
     *
     * @return true开，false关
     */
    public static boolean isPrint() {
        return DEBUG;
    }

    /**
     * JSON
     *
     * @param TAG     LOG TAG
     * @param message json string
     */
    public static void json(String TAG, String message) {
        Logger.t(TAG).json(message);
    }

    /**
     * INFO
     *
     * @param TAG       LOG TAG
     * @param message   提示模板 %s
     * @param parameter 参数
     */
    public static void i(String TAG, String message, Object... parameter) {
        Logger.t(TAG).i(message, parameter);
    }

    /**
     * DEBUG
     *
     * @param TAG       LOG TAG
     * @param message   提示模板 %s
     * @param parameter 参数
     */
    public static void d(String TAG, String message, Object... parameter) {
        Logger.t(TAG).d(message, parameter);
    }

    /**
     * WARN
     *
     * @param TAG       LOG TAG
     * @param message   提示模板 %s
     * @param parameter 参数
     */
    public static void w(String TAG, String message, Object... parameter) {
        Logger.t(TAG).w(message, parameter);
    }

    /**
     * ERROR
     *
     * @param TAG       LOG TAG
     * @param e         {@link Exception}
     * @param message   错误信息 %s站位
     * @param parameter 错误信息内容
     */
    public static void e(String TAG, @NonNull Throwable e, String message, Object... parameter) {
        Logger.t(TAG).e(e, message, parameter);
    }

    /**
     * ERROR
     *
     * @param TAG       LOG TAG
     * @param message   错误信息 %s站位
     * @param parameter 错误信息内容
     */
    public static void e(String TAG, String message, Object... parameter) {
        Logger.t(TAG).e(message, parameter);
    }

    /**
     * ERROR
     *
     * @param TAG LOG TAG
     * @param e   {@link Throwable}
     */
    public static void e(String TAG, @NonNull Throwable e) {
        Logger.t(TAG).e(e, e.getMessage());
    }

    /**
     * {@link android.util.Log}
     */
    public static class Log {

        /**
         * DEBUG
         *
         * @param TAG       LOG TAG
         * @param message   提示模板 %s
         * @param parameter 参数
         */
        public static void d(String TAG, String message, Object... parameter) {
            if (isPrint()) {
                android.util.Log.d(TAG, String.format(message, parameter));
            }
        }

        /**
         * INFO
         *
         * @param TAG       LOG TAG
         * @param message   提示模板 %s
         * @param parameter 参数
         */
        public static void i(String TAG, String message, Object... parameter) {
            if (isPrint()) {
                android.util.Log.i(TAG, String.format(message, parameter));
            }
        }

        /**
         * WARN
         *
         * @param TAG       LOG TAG
         * @param message   提示模板 %s
         * @param parameter 参数
         */
        public static void w(String TAG, String message, Object... parameter) {
            if (isPrint()) {
                android.util.Log.w(TAG, String.format(message, parameter));
            }
        }

        /**
         * WARN
         *
         * @param TAG       LOG TAG
         * @param e         {@link Exception}
         */
        public static void w(String TAG, @NonNull Throwable e) {
            if (isPrint()) {
                android.util.Log.w(TAG, e);
            }
        }

        /**
         * ERROR
         *
         * @param TAG       LOG TAG
         * @param e         {@link Exception}
         * @param message   错误信息 %s站位
         * @param parameter 错误信息内容
         */
        public static void e(String TAG, @NonNull Throwable e, String message,
                             Object... parameter) {
            if (isPrint()) {
                android.util.Log.e(TAG, String.format(message, parameter), e);
            }
        }

        /**
         * ERROR
         *
         * @param TAG       LOG TAG
         * @param message   错误信息 %s站位
         * @param parameter 错误信息内容
         */
        public static void e(String TAG, String message, Object... parameter) {
            if (isPrint()) {
                android.util.Log.e(TAG, String.format(message, parameter));
            }
        }

        /**
         * ERROR
         *
         * @param TAG LOG TAG
         * @param e   {@link Throwable}
         */
        public static void e(String TAG, @NonNull Throwable e) {
            if (isPrint()) {
                android.util.Log.e(TAG, e.getMessage(), e);
            }
        }
    }

    private static L.ILogToast toast;

    public static L.ILogToast getToast() {
        if (toast == null) {
            toast = new LogToast();
        }
        return toast;
    }

    private static L.ILogSnack snack;

    public static L.ILogSnack getSnack() {
        if (snack == null) {
            snack = new LogSnack();
        }
        return snack;
    }

    public interface ILogToast {
        void showLog(Context context, String hint);

        /**
         * 提示TOAST，并写入系统日志
         *
         * @param context  {@link Context}
         * @param userHint 给用户的提示内容
         * @param sysHint  系统日志的内容
         * @param e        {@link Throwable}
         */
        void e(String TAG, @NonNull Context context, String userHint, String sysHint,
               @NonNull Throwable e);

        /**
         * 提示TOAST，并写入系统日志
         *
         * @param context  {@link Context}
         * @param userHint 给用户的提示内容
         * @param e        {@link Throwable}
         */
        void e(String TAG, @NonNull Context context, String userHint, @NonNull Throwable e);

        /**
         * 提示TOAST，并写入系统日志
         *
         * @param context {@link Context}
         * @param e       {@link Throwable}
         */
        void e(String TAG, @NonNull Context context, @NonNull Throwable e);

        /**
         * 提示TOAST，并写入系统日志
         *
         * @param context {@link Context}
         * @param userHint    给用户与系统的提示内容
         */
        void e(String TAG, @NonNull Context context, String userHint);

        /**
         * 提示TOAST，并写入系统日志
         *
         * @param context {@link Context}
         * @param userHint    给用户与系统的提示内容
         */
        void e(String TAG, @NonNull Context context, @StringRes int userHint);
    }

    public interface ILogSnack {
        void showLog(View view, String hint);

        /**
         * 提示TOAST，并写入系统日志
         *
         * @param view  {@link View}
         * @param userHint 给用户的提示内容
         * @param sysHint  系统日志的内容
         * @param e        {@link Throwable}
         */
        void e(String TAG, @NonNull View view, String userHint, String sysHint,
               @NonNull Throwable e);

        /**
         * 提示TOAST，并写入系统日志
         *
         * @param view  {@link View}
         * @param userHint 给用户的提示内容
         * @param e        {@link Throwable}
         */
        void e(String TAG, @NonNull View view, String userHint, @NonNull Throwable e);

        /**
         * 提示TOAST，并写入系统日志
         *
         * @param view {@link View}
         * @param e       {@link Throwable}
         */
        void e(String TAG, @NonNull View view, @NonNull Throwable e);

        /**
         * 提示TOAST，并写入系统日志
         *
         * @param view {@link View}
         * @param userHint    给用户与系统的提示内容
         */
        void e(String TAG, @NonNull View view, String userHint);

        /**
         * 提示TOAST，并写入系统日志
         *
         * @param view {@link View}
         * @param userHint    给用户与系统的提示内容
         */
        void e(String TAG, @NonNull View view, @StringRes int userHint);

        /******************  activity **********************/

        /**
         * 提示TOAST，并写入系统日志
         *
         * @param activity  {@link Activity}
         * @param userHint 给用户的提示内容
         * @param sysHint  系统日志的内容
         * @param e        {@link Throwable}
         */
        void e(String TAG, @NonNull Activity activity, String userHint, String sysHint,
               @NonNull Throwable e);

        /**
         * 提示TOAST，并写入系统日志
         *
         * @param activity  {@link Activity}
         * @param userHint 给用户的提示内容
         * @param e        {@link Throwable}
         */
        void e(String TAG, @NonNull Activity activity, String userHint, @NonNull Throwable e);

        /**
         * 提示TOAST，并写入系统日志
         *
         * @param activity {@link Activity}
         * @param e       {@link Throwable}
         */
        void e(String TAG, @NonNull Activity activity, @NonNull Throwable e);

        /**
         * 提示TOAST，并写入系统日志
         *
         * @param activity {@link Activity}
         * @param userHint    给用户与系统的提示内容
         */
        void e(String TAG, @NonNull Activity activity, String userHint);

        /**
         * 提示TOAST，并写入系统日志
         *
         * @param activity {@link Activity}
         * @param userHint    给用户与系统的提示内容
         */
        void e(String TAG, @NonNull Activity activity, @StringRes int userHint);
    }
}
