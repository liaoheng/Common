package com.github.liaoheng.common.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.View;

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

    public static Logcat.ILogcat log() {
        return Logcat.get().logger();
    }

    public static Logcat.ILogcat alog() {
        return Logcat.get().log();
    }

    //------------------------------------------show----------------------------------------------------

    public interface ILogToast {
        void showLog(Context context, String hint);

        /**
         * 提示TOAST，并写入系统日志
         *
         * @param context {@link Context}
         * @param userHint 给用户的提示内容
         * @param sysHint 系统日志的内容
         * @param e {@link Throwable}
         */
        void e(String TAG, @NonNull Context context, String userHint, String sysHint,
                @NonNull Throwable e);

        /**
         * 提示TOAST，并写入系统日志
         *
         * @param context {@link Context}
         * @param userHint 给用户的提示内容
         * @param e {@link Throwable}
         */
        void e(String TAG, @NonNull Context context, String userHint, @NonNull Throwable e);

        /**
         * 提示TOAST，并写入系统日志
         *
         * @param context {@link Context}
         * @param e {@link Throwable}
         */
        void e(String TAG, @NonNull Context context, @NonNull Throwable e);

        /**
         * 提示TOAST，并写入系统日志
         *
         * @param context {@link Context}
         * @param userHint 给用户与系统的提示内容
         */
        void e(String TAG, @NonNull Context context, String userHint);

        /**
         * 提示TOAST，并写入系统日志
         *
         * @param context {@link Context}
         * @param userHint 给用户与系统的提示内容
         */
        void e(String TAG, @NonNull Context context, @StringRes int userHint);
    }

    public interface ILogSnack {
        void showLog(View view, String hint);

        /**
         * 提示TOAST，并写入系统日志
         *
         * @param view {@link View}
         * @param userHint 给用户的提示内容
         * @param sysHint 系统日志的内容
         * @param e {@link Throwable}
         */
        void e(String TAG, @NonNull View view, String userHint, String sysHint,
                @NonNull Throwable e);

        /**
         * 提示TOAST，并写入系统日志
         *
         * @param view {@link View}
         * @param userHint 给用户的提示内容
         * @param e {@link Throwable}
         */
        void e(String TAG, @NonNull View view, String userHint, @NonNull Throwable e);

        /**
         * 提示TOAST，并写入系统日志
         *
         * @param view {@link View}
         * @param e {@link Throwable}
         */
        void e(String TAG, @NonNull View view, @NonNull Throwable e);

        /**
         * 提示TOAST，并写入系统日志
         *
         * @param view {@link View}
         * @param userHint 给用户与系统的提示内容
         */
        void e(String TAG, @NonNull View view, String userHint);

        /**
         * 提示TOAST，并写入系统日志
         *
         * @param view {@link View}
         * @param userHint 给用户与系统的提示内容
         */
        void e(String TAG, @NonNull View view, @StringRes int userHint);

        /******************  activity **********************/

        /**
         * 提示TOAST，并写入系统日志
         *
         * @param activity {@link Activity}
         * @param userHint 给用户的提示内容
         * @param sysHint 系统日志的内容
         * @param e {@link Throwable}
         */
        void e(String TAG, @NonNull Activity activity, String userHint, String sysHint,
                @NonNull Throwable e);

        /**
         * 提示TOAST，并写入系统日志
         *
         * @param activity {@link Activity}
         * @param userHint 给用户的提示内容
         * @param e {@link Throwable}
         */
        void e(String TAG, @NonNull Activity activity, String userHint, @NonNull Throwable e);

        /**
         * 提示TOAST，并写入系统日志
         *
         * @param activity {@link Activity}
         * @param e {@link Throwable}
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

    public static class LogSnack implements L.ILogSnack {
        @Override
        public void showLog(View view, String hint) {
            UIUtils.showSnack(view, hint);
        }

        @Override
        public void e(String TAG, @NonNull View view, String userHint, String sysHint,
                @NonNull Throwable e) {
            Logcat.get().logger().e(TAG, e, sysHint);
            if (TextUtils.isEmpty(userHint)) {
                userHint = e.getMessage();
            }
            showLog(view, userHint);
        }

        @Override
        public void e(String TAG, @NonNull View view, String userHint, @NonNull Throwable e) {
            e(TAG, view, userHint, null, e);
        }

        @Override
        public void e(String TAG, @NonNull View view, @NonNull Throwable e) {
            e(TAG, view, null, e);
        }

        @Override
        public void e(String TAG, @NonNull View view, String userHint) {
            Logcat.get().logger().e(TAG, userHint);
            showLog(view, userHint);
        }

        @Override
        public void e(String TAG, @NonNull View view, @StringRes int userHint) {
            e(TAG, view, view.getContext().getString(userHint));
        }

        @Override
        public void e(String TAG, @NonNull Activity activity, String userHint, String sysHint,
                @NonNull Throwable e) {
            e(TAG, UIUtils.getActivityContentView(activity), userHint, sysHint, e);
        }

        @Override
        public void e(String TAG, @NonNull Activity activity, String userHint,
                @NonNull Throwable e) {
            e(TAG, activity, userHint, null, e);
        }

        @Override
        public void e(String TAG, @NonNull Activity activity, @NonNull Throwable e) {
            e(TAG, activity, null, e);
        }

        @Override
        public void e(String TAG, @NonNull Activity activity, String userHint) {
            e(TAG, activity.getWindow().getDecorView(), userHint);
        }

        @Override
        public void e(String TAG, @NonNull Activity activity, @StringRes int userHint) {
            e(TAG, activity, activity.getString(userHint));
        }
    }

    private static ILogSnack snack;

    public static ILogSnack getSnack() {
        if (snack == null) {
            snack = new LogSnack();
        }
        return snack;
    }
}
