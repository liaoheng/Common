package com.github.liaoheng.common.util;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.View;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.PrettyFormatStrategy;

import java.util.Arrays;

/**
 * 日志处理
 *
 * @author liaoheng
 * @version 2018-04-24 14:31
 * @see <a href='https://github.com/orhanobut/logger'>Logger</a>
 */
public final class Logcat {
    private static boolean isPrint = true;
    private String TAG;

    /**
     * LOG INIT
     *
     * @param tag global
     * @param isLoggable 是否开启日志
     */
    public void init(String tag, boolean isLoggable) {
        TAG = tag;
        isPrint = isLoggable;
        try {
            Class.forName("com.orhanobut.logger.Logger");
            FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder().methodCount(0).tag(tag).build();
            com.orhanobut.logger.Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
                @Override
                public boolean isLoggable(int priority, @Nullable String tag) {
                    return isPrint();
                }
            });
        } catch (ClassNotFoundException ignored) {
        }
    }

    /**
     * LOG INIT 默认打开日志
     *
     * @param tag global
     */
    public void init(String tag) {
        init(tag, true);
    }

    /**
     * 是否打开日志
     *
     * @return true开，false关
     */
    public static boolean isPrint() {
        return isPrint;
    }

    private Logcat() {
    }

    private static Logcat mLogcat;

    public static Logcat get() {
        if (null == mLogcat) {
            mLogcat = new Logcat();
        }
        return mLogcat;
    }

    public static Logcat create() {
        return new Logcat();
    }

    public interface ILogcat {
        void prefix(String prefix);

        /**
         * DEBUG,Collections are supported
         *
         * @param object 参数
         */
        void da(@Nullable Object object);

        /**
         * DEBUG,Collections are supported
         *
         * @param TAG LOG TAG
         * @param object 参数
         */
        void da(@NonNull String TAG, @Nullable Object object);

        /**
         * DEBUG
         *
         * @param message 提示模板 %s
         * @param parameter 参数
         */
        void d(@NonNull String message, @Nullable Object... parameter);

        /**
         * DEBUG
         *
         * @param TAG LOG TAG
         * @param message 提示模板 %s
         * @param parameter 参数
         */
        void d(@NonNull String TAG, @NonNull String message, @Nullable Object... parameter);

        /**
         * INFO
         *
         * @param message 提示模板 %s
         * @param parameter 参数
         */
        void i(@NonNull String message, @Nullable Object... parameter);

        /**
         * INFO
         *
         * @param TAG LOG TAG
         * @param message 提示模板 %s
         * @param parameter 参数
         */
        void i(@NonNull String TAG, @NonNull String message, @Nullable Object... parameter);

        /**
         * WARN
         *
         * @param message 提示模板 %s
         * @param parameter 参数
         */
        void w(@NonNull String message, @Nullable Object... parameter);

        /**
         * WARN
         *
         * @param TAG LOG TAG
         * @param message 提示模板 %s
         * @param parameter 参数
         */
        void w(@NonNull String TAG, @NonNull String message, @Nullable Object... parameter);

        /**
         * WARN
         *
         * @param e {@link Throwable}
         * @param message 提示模板 %s
         * @param parameter 参数
         */
        void w(@Nullable Throwable e, @NonNull String message, @Nullable Object... parameter);

        /**
         * WARN
         *
         * @param TAG LOG TAG
         * @param e {@link Throwable}
         * @param message 提示模板 %s
         * @param parameter 参数
         */
        void w(String TAG, @Nullable Throwable e, @NonNull String message,
                @Nullable Object... parameter);

        /**
         * WARN
         *
         * @param e {@link Throwable}
         */
        void w(@Nullable Throwable e);

        /**
         * WARN
         *
         * @param TAG LOG TAG
         * @param e {@link Throwable}
         */
        void w(String TAG, @Nullable Throwable e);

        /**
         * ERROR
         *
         * @param e {@link Throwable}
         * @param message 错误信息 %s站位
         * @param parameter 错误信息内容
         */
        void e(@NonNull Throwable e, @NonNull String message, @Nullable Object... parameter);

        /**
         * ERROR
         *
         * @param TAG LOG TAG
         * @param e {@link Throwable}
         * @param message 错误信息 %s站位
         * @param parameter 错误信息内容
         */
        void e(@NonNull String TAG, @NonNull Throwable e, @NonNull String message, @Nullable Object... parameter);

        /**
         * ERROR
         *
         * @param message 错误信息 %s站位
         * @param parameter 错误信息内容
         */
        void e(@NonNull String message, @Nullable Object... parameter);

        /**
         * ERROR
         *
         * @param TAG LOG TAG
         * @param message 错误信息 %s站位
         * @param parameter 错误信息内容
         */
        void e(@NonNull String TAG, @NonNull String message, @Nullable Object... parameter);

        /**
         * ERROR
         *
         * @param e {@link Throwable}
         */
        void e(@NonNull Throwable e);

        /**
         * ERROR
         *
         * @param TAG LOG TAG
         * @param e {@link Throwable}
         */
        void e(@NonNull String TAG, @NonNull Throwable e);
    }

    public interface ILogger extends ILogcat {

        /**
         * JSON
         *
         * @param message json string
         */
        void json(@NonNull String message);

        /**
         * JSON
         *
         * @param TAG LOG TAG
         * @param message json string
         */
        void json(@NonNull String TAG, @NonNull String message);

        /**
         * XML
         *
         * @param message xml string
         */
        void xml(@NonNull String message);

        /**
         * XML
         *
         * @param TAG LOG TAG
         * @param message xml string
         */
        void xml(@NonNull String TAG, @NonNull String message);
    }

    public class Logger implements ILogger {
        private String prefix;

        @Override
        public void prefix(String prefix) {
            this.prefix = prefix;
        }

        private String createTag(String tag) {
            return TextUtils.isEmpty(prefix) ? tag : prefix + (TextUtils.isEmpty(tag) ? "" : "-" + tag);
        }

        @Override
        public void json(@NonNull String message) {
            com.orhanobut.logger.Logger.t(createTag(null)).json(message);
        }

        @Override
        public void json(@NonNull String TAG, @NonNull String message) {
            com.orhanobut.logger.Logger.t(createTag(TAG)).json(message);
        }

        @Override
        public void xml(@NonNull String message) {
            com.orhanobut.logger.Logger.t(createTag(null)).xml(message);
        }

        @Override
        public void xml(@NonNull String TAG, @NonNull String message) {
            com.orhanobut.logger.Logger.t(createTag(TAG)).xml(message);
        }

        @Override
        public void da(@Nullable Object object) {
            com.orhanobut.logger.Logger.t(createTag(null)).d(object);
        }

        @Override
        public void da(@NonNull String TAG, @Nullable Object object) {
            com.orhanobut.logger.Logger.t(createTag(TAG)).d(object);
        }

        @Override
        public void d(@NonNull String message, @Nullable Object... parameter) {
            com.orhanobut.logger.Logger.t(createTag(null)).d(message, parameter);
        }

        @Override
        public void d(@NonNull String TAG, @NonNull String message, @Nullable Object... parameter) {
            com.orhanobut.logger.Logger.t(createTag(TAG)).d(message, parameter);
        }

        @Override
        public void i(@NonNull String message, @Nullable Object... parameter) {
            com.orhanobut.logger.Logger.t(createTag(null)).i(message, parameter);
        }

        @Override
        public void i(@NonNull String TAG, @NonNull String message, @Nullable Object... parameter) {
            com.orhanobut.logger.Logger.t(createTag(TAG)).i(message, parameter);
        }

        @Override
        public void w(@NonNull String message, @Nullable Object... parameter) {
            com.orhanobut.logger.Logger.t(createTag(null)).w(message, parameter);
        }

        @Override
        public void w(@NonNull String TAG, @NonNull String message, @Nullable Object... parameter) {
            com.orhanobut.logger.Logger.t(createTag(TAG)).w(message, parameter);
        }

        @Override
        public void w(@Nullable Throwable e, @NonNull String message, @Nullable Object... parameter) {
            String stackTraceString = android.util.Log.getStackTraceString(e);
            com.orhanobut.logger.Logger.t(createTag(null))
                    .w(createMessage(message, parameter) + " : " + stackTraceString);
        }

        @Override
        public void w(String TAG, @Nullable Throwable e, @NonNull String message, @Nullable Object... parameter) {
            String stackTraceString = android.util.Log.getStackTraceString(e);
            com.orhanobut.logger.Logger.t(createTag(TAG))
                    .w(createMessage(message, parameter) + " : " + stackTraceString);
        }

        @Override
        public void w(@Nullable Throwable e) {
            com.orhanobut.logger.Logger.t(createTag(null)).w(android.util.Log.getStackTraceString(e));
        }

        @Override
        public void w(String TAG, @Nullable Throwable e) {
            com.orhanobut.logger.Logger.t(createTag(TAG)).w(android.util.Log.getStackTraceString(e));
        }

        @Override
        public void e(@NonNull Throwable e, @NonNull String message, @Nullable Object... parameter) {
            com.orhanobut.logger.Logger.t(createTag(null)).e(e, message, parameter);
        }

        @Override
        public void e(@NonNull String TAG, @NonNull Throwable e, @NonNull String message,
                @Nullable Object... parameter) {
            com.orhanobut.logger.Logger.t(createTag(TAG)).e(e, message, parameter);
        }

        @Override
        public void e(@NonNull String message, @Nullable Object... parameter) {
            com.orhanobut.logger.Logger.t(createTag(null)).e(message, parameter);
        }

        @Override
        public void e(@NonNull String TAG, @NonNull String message, @Nullable Object... parameter) {
            com.orhanobut.logger.Logger.t(createTag(TAG)).e(message, parameter);
        }

        @Override
        public void e(@NonNull Throwable e) {
            com.orhanobut.logger.Logger.t(createTag(null)).e(e, e.getMessage());
        }

        @Override
        public void e(@NonNull String TAG, @NonNull Throwable e) {
            com.orhanobut.logger.Logger.t(createTag(TAG)).e(e, e.getMessage());
        }
    }

    private ILogger mLogger;

    public synchronized ILogger logger() {
        if (mLogger == null) {
            synchronized (Logger.class) {
                mLogger = new Logger();
            }
        }
        return mLogger;
    }

    private class Log implements ILogcat {
        private String prefix;

        @Override
        public void prefix(String prefix) {
            this.prefix = prefix;
        }

        private String createTag(String tag) {
            return TextUtils.isEmpty(prefix) ? tag : prefix + (TextUtils.isEmpty(tag) ? "" : "-" + tag);
        }

        @Override
        public void da(@Nullable Object object) {
            d(createTag(TAG), object);
        }

        @Override
        public void da(@NonNull String TAG, @Nullable Object object) {
            if (!isPrint()) {
                return;
            }
            android.util.Log.d(createTag(TAG), toLogString(object));
        }

        @Override
        public void d(@NonNull String message, @Nullable Object... parameter) {
            d(createTag(TAG), message, parameter);
        }

        @Override
        public void d(@NonNull String TAG, @NonNull String message, @Nullable Object... parameter) {
            if (!isPrint()) {
                return;
            }
            android.util.Log.d(createTag(TAG), createMessage(message, parameter));
        }

        @Override
        public void i(@NonNull String message, @Nullable Object... parameter) {
            i(createTag(TAG), message, parameter);
        }

        @Override
        public void i(@NonNull String TAG, @NonNull String message, @Nullable Object... parameter) {
            if (!isPrint()) {
                return;
            }
            android.util.Log.i(createTag(TAG), createMessage(message, parameter));
        }

        @Override
        public void w(@NonNull String message, @Nullable Object... parameter) {
            w(createTag(TAG), message, parameter);
        }

        @Override
        public void w(@NonNull String TAG, @NonNull String message, @Nullable Object... parameter) {
            if (!isPrint()) {
                return;
            }
            android.util.Log.w(createTag(TAG), createMessage(message, parameter));
        }

        @Override
        public void w(@Nullable Throwable e, @NonNull String message, @Nullable Object... parameter) {
            w(createTag(TAG), e, message, parameter);
        }

        @Override
        public void w(String TAG, @Nullable Throwable e, @NonNull String message, @Nullable Object... parameter) {
            if (!isPrint()) {
                return;
            }
            android.util.Log.w(createTag(TAG), createMessage(message, parameter), e);
        }

        @Override
        public void w(@Nullable Throwable e) {
            w(createTag(TAG), e);
        }

        @Override
        public void w(String TAG, @Nullable Throwable e) {
            if (!isPrint()) {
                return;
            }
            android.util.Log.w(createTag(TAG), e);
        }

        @Override
        public void e(@NonNull Throwable e, @NonNull String message, @Nullable Object... parameter) {
            e(createTag(TAG), e, message, parameter);
        }

        @Override
        public void e(@NonNull String TAG, @NonNull Throwable e, @NonNull String message,
                @Nullable Object... parameter) {
            if (!isPrint()) {
                return;
            }
            android.util.Log.e(createTag(TAG), createMessage(message, parameter), e);
        }

        @Override
        public void e(@NonNull String message, @Nullable Object... parameter) {
            e(createTag(TAG), message, parameter);
        }

        @Override
        public void e(@NonNull String TAG, @NonNull String message, @Nullable Object... parameter) {
            if (!isPrint()) {
                return;
            }
            android.util.Log.e(createTag(TAG), createMessage(message, parameter));
        }

        @Override
        public void e(@NonNull Throwable e) {
            e(createTag(TAG), e);
        }

        @Override
        public void e(@NonNull String TAG, @NonNull Throwable e) {
            if (!isPrint()) {
                return;
            }
            android.util.Log.e(createTag(TAG), e.getMessage(), e);
        }
    }

    private ILogcat mLog;

    public synchronized ILogcat log() {
        if (mLog == null) {
            synchronized (Log.class) {
                mLog = new Log();
            }
        }
        return mLog;
    }

    /**
     * com.orhanobut.logger.LoggerPrinter#createMessage
     */
    @NonNull
    public static String createMessage(@NonNull String message, @Nullable Object... args) {
        return args == null || args.length == 0 ? message : String.format(message, args);
    }

    /**
     * com.orhanobut.logger.Utils#toString
     */
    public static String toLogString(Object object) {
        if (object == null) {
            return "null";
        }
        if (!object.getClass().isArray()) {
            return object.toString();
        }
        if (object instanceof boolean[]) {
            return Arrays.toString((boolean[]) object);
        }
        if (object instanceof byte[]) {
            return Arrays.toString((byte[]) object);
        }
        if (object instanceof char[]) {
            return Arrays.toString((char[]) object);
        }
        if (object instanceof short[]) {
            return Arrays.toString((short[]) object);
        }
        if (object instanceof int[]) {
            return Arrays.toString((int[]) object);
        }
        if (object instanceof long[]) {
            return Arrays.toString((long[]) object);
        }
        if (object instanceof float[]) {
            return Arrays.toString((float[]) object);
        }
        if (object instanceof double[]) {
            return Arrays.toString((double[]) object);
        }
        if (object instanceof Object[]) {
            return Arrays.deepToString((Object[]) object);
        }
        return "Couldn't find a correct type for the object";
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

    public class LogToast implements Logcat.ILogToast {

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

    private ILogToast toast;

    public ILogToast getToast() {
        if (toast == null) {
            toast = new LogToast();
        }
        return toast;
    }

    public class LogSnack implements Logcat.ILogSnack {
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

    private ILogSnack snack;

    public ILogSnack getSnack() {
        if (snack == null) {
            snack = new LogSnack();
        }
        return snack;
    }

}
