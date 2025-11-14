package com.github.liaoheng.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.github.liaoheng.Common;

/**
 * 日志处理
 *
 * @author liaoheng
 * @date 2025-11-11 16:17
 */
public class L {

    private static ILogger mLogger;

    /**
     * LOG INIT
     *
     * @param tag        global
     * @param isLoggable 是否开启日志
     */
    public static void init(String tag, boolean isLoggable) {
        mLogger = new BaseLogger(tag);
        mLogger.setPrint(isLoggable);
    }

    /**
     * LOG INIT 默认打开日志
     *
     * @param tag global
     */
    public static void init(String tag) {
        init(tag, true);
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
        return !mLogger.isNotPrint();
    }

    public static ILogger alog() {
        return mLogger;
    }

    public static boolean isNotPrint() {
        return mLogger.isNotPrint();
    }

    public static void setPrint(boolean print) {
        mLogger.setPrint(print);
    }

    public static String getTag(String tag) {
        return mLogger.getTag(tag);
    }

    public static void json(String message) {
        mLogger.json(message);
    }

    public static void json(@NonNull String TAG, String message) {
        mLogger.json(TAG, message);
    }

    public static void v(String message, @Nullable Object... parameter) {
        mLogger.v(message, parameter);
    }

    public static void v(@NonNull String TAG, String message, @Nullable Object... parameter) {
        mLogger.v(TAG, message, parameter);
    }

    public static void d(String message, @Nullable Object... parameter) {
        mLogger.d(message, parameter);
    }

    public static void d(@NonNull String TAG, String message, @Nullable Object... parameter) {
        mLogger.d(TAG, message, parameter);
    }

    public static void i(String message, @Nullable Object... parameter) {
        mLogger.i(message, parameter);
    }

    public static void i(@NonNull String TAG, String message, @Nullable Object... parameter) {
        mLogger.i(TAG, message, parameter);
    }

    public static void w(String message, @Nullable Object... parameter) {
        mLogger.w(message, parameter);
    }

    public static void w(@NonNull String TAG, String message, @Nullable Object... parameter) {
        mLogger.w(TAG, message, parameter);
    }

    public static void w(@Nullable Throwable e, String message, @Nullable Object... parameter) {
        mLogger.w(e, message, parameter);
    }

    public static void w(String TAG, @Nullable Throwable e, String message, @Nullable Object... parameter) {
        mLogger.w(TAG, e, message, parameter);
    }

    public static void e(String message, @Nullable Object... parameter) {
        mLogger.e(message, parameter);
    }

    public static void e(@NonNull String TAG, String message, @Nullable Object... parameter) {
        mLogger.e(TAG, message, parameter);
    }

    public static void e(@NonNull Throwable e, String message, @Nullable Object... parameter) {
        mLogger.e(e, message, parameter);
    }

    public static void e(@NonNull String TAG, @NonNull Throwable e, String message, @Nullable Object... parameter) {
        mLogger.e(TAG, e, message, parameter);
    }

    //------------------------------------------show----------------------------------------------------

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
         * @param context  {@link Context}
         * @param userHint 给用户与系统的提示内容
         */
        void e(String TAG, @NonNull Context context, String userHint);

        /**
         * 提示TOAST，并写入系统日志
         *
         * @param context  {@link Context}
         * @param userHint 给用户与系统的提示内容
         */
        void e(String TAG, @NonNull Context context, @StringRes int userHint);
    }

    public interface ILogSnack {
        void showLog(View view, String hint);

        /**
         * 提示TOAST，并写入系统日志
         *
         * @param view     {@link View}
         * @param userHint 给用户的提示内容
         * @param sysHint  系统日志的内容
         * @param e        {@link Throwable}
         */
        void e(String TAG, @NonNull View view, String userHint, String sysHint,
                @NonNull Throwable e);

        /**
         * 提示TOAST，并写入系统日志
         *
         * @param view     {@link View}
         * @param userHint 给用户的提示内容
         * @param e        {@link Throwable}
         */
        void e(String TAG, @NonNull View view, String userHint, @NonNull Throwable e);

        /**
         * 提示TOAST，并写入系统日志
         *
         * @param view {@link View}
         * @param e    {@link Throwable}
         */
        void e(String TAG, @NonNull View view, @NonNull Throwable e);

        /**
         * 提示TOAST，并写入系统日志
         *
         * @param view     {@link View}
         * @param userHint 给用户与系统的提示内容
         */
        void e(String TAG, @NonNull View view, String userHint);

        /**
         * 提示TOAST，并写入系统日志
         *
         * @param view     {@link View}
         * @param userHint 给用户与系统的提示内容
         */
        void e(String TAG, @NonNull View view, @StringRes int userHint);

        /******************  activity **********************/

        /**
         * 提示TOAST，并写入系统日志
         *
         * @param activity {@link Activity}
         * @param userHint 给用户的提示内容
         * @param sysHint  系统日志的内容
         * @param e        {@link Throwable}
         */
        void e(String TAG, @NonNull Activity activity, String userHint, String sysHint,
                @NonNull Throwable e);

        /**
         * 提示TOAST，并写入系统日志
         *
         * @param activity {@link Activity}
         * @param userHint 给用户的提示内容
         * @param e        {@link Throwable}
         */
        void e(String TAG, @NonNull Activity activity, String userHint, @NonNull Throwable e);

        /**
         * 提示TOAST，并写入系统日志
         *
         * @param activity {@link Activity}
         * @param e        {@link Throwable}
         */
        void e(String TAG, @NonNull Activity activity, @NonNull Throwable e);

        /**
         * 提示TOAST，并写入系统日志
         *
         * @param activity {@link Activity}
         * @param userHint 给用户与系统的提示内容
         */
        void e(String TAG, @NonNull Activity activity, String userHint);

        /**
         * 提示TOAST，并写入系统日志
         *
         * @param activity {@link Activity}
         * @param userHint 给用户与系统的提示内容
         */
        void e(String TAG, @NonNull Activity activity, @StringRes int userHint);
    }

    public static class LogToast implements L.ILogToast {

        @Override
        public void showLog(Context context, String hint) {
            UIUtils.showToast(context, hint);
        }

        @Override
        public void e(String TAG, @NonNull Context context, String userHint, String sysHint,
                @NonNull Throwable e) {
            Logcat.get().logger().e(TAG, e, sysHint);
            if (TextUtils.isEmpty(userHint)) {
                userHint = e.getMessage();
            }
            showLog(context, userHint);
        }

        @Override
        public void e(String TAG, @NonNull Context context, String userHint, @NonNull Throwable e) {
            e(TAG, context, userHint, null, e);
        }

        @Override
        public void e(String TAG, @NonNull Context context, @NonNull Throwable e) {
            e(TAG, context, null, e);
        }

        @Override
        public void e(String TAG, @NonNull Context context, String userHint) {
            Logcat.get().logger().e(TAG, userHint);
            showLog(context, userHint);
        }

        @Override
        public void e(String TAG, @NonNull Context context, @StringRes int userHint) {
            e(TAG, context, context.getResources().getString(userHint));
        }
    }

    private static ILogToast toast;

    public static ILogToast getToast() {
        if (toast == null) {
            toast = new LogToast();
        }
        return toast;
    }
}
