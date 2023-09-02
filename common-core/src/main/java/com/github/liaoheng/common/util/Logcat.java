package com.github.liaoheng.common.util;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
    private String mGlobalTag;

    /**
     * LOG INIT
     *
     * @param tag        global
     * @param isLoggable 是否开启日志
     */
    public void init(String tag, boolean isLoggable) {
        mGlobalTag = tag;
        isPrint = isLoggable;
        try {
            Class.forName("com.orhanobut.logger.Logger");
            FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder().methodCount(0).tag(tag).build();
            com.orhanobut.logger.Logger.clearLogAdapters();
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
         * JSON
         *
         * @param message json string
         */
        void json(String message);

        /**
         * JSON
         *
         * @param TAG     LOG tag
         * @param message json string
         */
        void json(@NonNull String TAG, String message);

        /**
         * XML
         *
         * @param message xml string
         */
        void xml(String message);

        /**
         * XML
         *
         * @param TAG     LOG tag
         * @param message xml string
         */
        void xml(@NonNull String TAG, String message);

        /**
         * VERBOSE
         *
         * @param message   提示模板 %s
         * @param parameter 参数
         */
        void v(String message, @Nullable Object... parameter);

        /**
         * VERBOSE
         *
         * @param TAG       LOG tag
         * @param message   提示模板 %s
         * @param parameter 参数
         */
        void v(@NonNull String TAG, String message, @Nullable Object... parameter);

        /**
         * DEBUG,Collections are supported
         *
         * @param object 参数
         */
        void da(Object object);

        /**
         * DEBUG,Collections are supported
         *
         * @param TAG    LOG tag
         * @param object 参数
         */
        void da(@NonNull String TAG, Object object);

        /**
         * DEBUG
         *
         * @param message   提示模板 %s
         * @param parameter 参数
         */
        void d(String message, @Nullable Object... parameter);

        /**
         * DEBUG
         *
         * @param TAG       LOG tag
         * @param message   提示模板 %s
         * @param parameter 参数
         */
        void d(@NonNull String TAG, String message, @Nullable Object... parameter);

        /**
         * INFO
         *
         * @param message   提示模板 %s
         * @param parameter 参数
         */
        void i(String message, @Nullable Object... parameter);

        /**
         * INFO
         *
         * @param TAG       LOG tag
         * @param message   提示模板 %s
         * @param parameter 参数
         */
        void i(@NonNull String TAG, String message, @Nullable Object... parameter);

        /**
         * WARN
         *
         * @param message   提示模板 %s
         * @param parameter 参数
         */
        void w(String message, @Nullable Object... parameter);

        /**
         * WARN
         *
         * @param TAG       LOG tag
         * @param message   提示模板 %s
         * @param parameter 参数
         */
        void w(@NonNull String TAG, String message, @Nullable Object... parameter);

        /**
         * WARN
         *
         * @param e         {@link Throwable}
         * @param message   提示模板 %s
         * @param parameter 参数
         */
        void w(@Nullable Throwable e, String message, @Nullable Object... parameter);

        /**
         * WARN
         *
         * @param TAG       LOG tag
         * @param e         {@link Throwable}
         * @param message   提示模板 %s
         * @param parameter 参数
         */
        void w(String TAG, @Nullable Throwable e, String message,
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
         * @param TAG LOG tag
         * @param e   {@link Throwable}
         */
        void w(String TAG, @Nullable Throwable e);

        /**
         * ERROR
         *
         * @param e         {@link Throwable}
         * @param message   错误信息 %s站位
         * @param parameter 错误信息内容
         */
        void e(@NonNull Throwable e, String message, @Nullable Object... parameter);

        /**
         * ERROR
         *
         * @param TAG       LOG tag
         * @param e         {@link Throwable}
         * @param message   错误信息 %s站位
         * @param parameter 错误信息内容
         */
        void e(@NonNull String TAG, @NonNull Throwable e, String message, @Nullable Object... parameter);

        /**
         * ERROR
         *
         * @param message   错误信息 %s站位
         * @param parameter 错误信息内容
         */
        void e(String message, @Nullable Object... parameter);

        /**
         * ERROR
         *
         * @param TAG       LOG tag
         * @param message   错误信息 %s站位
         * @param parameter 错误信息内容
         */
        void e(@NonNull String TAG, String message, @Nullable Object... parameter);

        /**
         * ERROR
         *
         * @param e {@link Throwable}
         */
        void e(@NonNull Throwable e);

        /**
         * ERROR
         *
         * @param TAG LOG tag
         * @param e   {@link Throwable}
         */
        void e(@NonNull String TAG, @NonNull Throwable e);
    }

    public static abstract class BaseLogcat implements ILogcat {
        protected String createTag(String tag, String prefix) {
            return TextUtils.isEmpty(prefix) ? tag : prefix + (TextUtils.isEmpty(tag) ? "" : "-" + tag);
        }
    }

    public class Logger extends BaseLogcat {
        private String prefix;

        private String createTag(String tag) {
            return createTag(tag, prefix);
        }

        @Override
        public void prefix(String prefix) {
            this.prefix = prefix;
        }

        @Override
        public void v(String message, @Nullable Object... parameter) {
            com.orhanobut.logger.Logger.t(createTag(null)).v(createMessage(message, parameter));
        }

        @Override
        public void v(@NonNull String TAG, String message, @Nullable Object... parameter) {
            com.orhanobut.logger.Logger.t(createTag(TAG)).v(createMessage(message, parameter));
        }

        @Override
        public void json(String message) {
            if (isNull(message)) {
                return;
            }
            com.orhanobut.logger.Logger.t(createTag(null)).json(message);
        }

        @Override
        public void json(@NonNull String TAG, String message) {
            if (isNull(message)) {
                return;
            }
            com.orhanobut.logger.Logger.t(createTag(TAG)).json(message);
        }

        @Override
        public void xml(String message) {
            if (isNull(message)) {
                return;
            }
            com.orhanobut.logger.Logger.t(createTag(null)).xml(message);
        }

        @Override
        public void xml(@NonNull String TAG, String message) {
            if (isNull(message)) {
                return;
            }
            com.orhanobut.logger.Logger.t(createTag(TAG)).xml(message);
        }

        @Override
        public void da(Object object) {
            com.orhanobut.logger.Logger.t(createTag(null)).d(object);
        }

        @Override
        public void da(@NonNull String TAG, Object object) {
            com.orhanobut.logger.Logger.t(createTag(TAG)).d(object);
        }

        @Override
        public void d(String message, @Nullable Object... parameter) {
            com.orhanobut.logger.Logger.t(createTag(null)).d(createMessage(message, parameter));
        }

        @Override
        public void d(@NonNull String TAG, String message, @Nullable Object... parameter) {
            com.orhanobut.logger.Logger.t(createTag(TAG)).d(createMessage(message, parameter));
        }

        @Override
        public void i(String message, @Nullable Object... parameter) {
            com.orhanobut.logger.Logger.t(createTag(null)).i(createMessage(message, parameter));
        }

        @Override
        public void i(@NonNull String TAG, String message, @Nullable Object... parameter) {
            com.orhanobut.logger.Logger.t(createTag(TAG)).i(createMessage(message, parameter));
        }

        @Override
        public void w(String message, @Nullable Object... parameter) {
            com.orhanobut.logger.Logger.t(createTag(null)).w(createMessage(message, parameter));
        }

        @Override
        public void w(@NonNull String TAG, String message, @Nullable Object... parameter) {
            com.orhanobut.logger.Logger.t(createTag(TAG)).w(createMessage(message, parameter));
        }

        @Override
        public void w(@Nullable Throwable e, String message, @Nullable Object... parameter) {
            String stackTraceString = android.util.Log.getStackTraceString(e);
            com.orhanobut.logger.Logger.t(createTag(null))
                    .w(createMessage(message, parameter) + " : " + stackTraceString);
        }

        @Override
        public void w(String TAG, @Nullable Throwable e, String message, @Nullable Object... parameter) {
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
        public void e(@NonNull Throwable e, String message, @Nullable Object... parameter) {
            com.orhanobut.logger.Logger.t(createTag(null)).e(e, createMessage(message, parameter));
        }

        @Override
        public void e(@NonNull String TAG, @NonNull Throwable e, String message,
                @Nullable Object... parameter) {
            com.orhanobut.logger.Logger.t(createTag(TAG)).e(e, createMessage(message, parameter));
        }

        @Override
        public void e(String message, @Nullable Object... parameter) {
            com.orhanobut.logger.Logger.t(createTag(null)).e(createMessage(message, parameter));
        }

        @Override
        public void e(@NonNull String TAG, String message, @Nullable Object... parameter) {
            com.orhanobut.logger.Logger.t(createTag(TAG)).e(createMessage(message, parameter));
        }

        @Override
        public void e(@NonNull Throwable e) {
            com.orhanobut.logger.Logger.t(createTag(null)).e(e, e.getMessage() == null ? "" : e.getMessage());
        }

        @Override
        public void e(@NonNull String TAG, @NonNull Throwable e) {
            com.orhanobut.logger.Logger.t(createTag(TAG)).e(e, e.getMessage() == null ? "" : e.getMessage());
        }
    }

    private ILogcat mLogger;

    public synchronized ILogcat logger() {
        if (mLogger == null) {
            synchronized (Logger.class) {
                mLogger = newLogger();
            }
        }
        return mLogger;
    }

    public ILogcat newLogger() {
        return new Logger();
    }

    public class Log extends BaseLogcat {
        private String tag;
        private String prefix;

        public Log(String tag) {
            this.tag = tag;
        }

        private String createTag(String tag) {
            return createTag(tag, prefix);
        }

        @Override
        public void prefix(String prefix) {
            this.prefix = prefix;
        }

        @Override
        public void json(String message) {

        }

        @Override
        public void json(@NonNull String TAG, String message) {

        }

        @Override
        public void xml(String message) {

        }

        @Override
        public void xml(@NonNull String TAG, String message) {

        }

        @Override
        public void v(String message, @Nullable Object... parameter) {
            v(tag, message, parameter);
        }

        @Override
        public void v(@NonNull String TAG, String message, @Nullable Object... parameter) {
            if (!isPrint()) {
                return;
            }
            android.util.Log.v(createTag(TAG), createMessage(message, parameter));
        }

        @Override
        public void da(@Nullable Object object) {
            d(createTag(tag), object);
        }

        @Override
        public void da(@NonNull String TAG, @Nullable Object object) {
            if (!isPrint()) {
                return;
            }
            android.util.Log.d(createTag(TAG), toLogString(object));
        }

        @Override
        public void d(String message, @Nullable Object... parameter) {
            d(createTag(tag), message, parameter);
        }

        @Override
        public void d(@NonNull String TAG, String message, @Nullable Object... parameter) {
            if (!isPrint()) {
                return;
            }
            android.util.Log.d(createTag(TAG), createMessage(message, parameter));
        }

        @Override
        public void i(String message, @Nullable Object... parameter) {
            i(createTag(tag), message, parameter);
        }

        @Override
        public void i(@NonNull String TAG, String message, @Nullable Object... parameter) {
            if (!isPrint()) {
                return;
            }
            android.util.Log.i(createTag(TAG), createMessage(message, parameter));
        }

        @Override
        public void w(String message, @Nullable Object... parameter) {
            w(createTag(tag), message, parameter);
        }

        @Override
        public void w(@NonNull String TAG, String message, @Nullable Object... parameter) {
            if (!isPrint()) {
                return;
            }
            android.util.Log.w(createTag(TAG), createMessage(message, parameter));
        }

        @Override
        public void w(@Nullable Throwable e, String message, @Nullable Object... parameter) {
            w(createTag(tag), e, message, parameter);
        }

        @Override
        public void w(String TAG, @Nullable Throwable e, String message, @Nullable Object... parameter) {
            if (!isPrint()) {
                return;
            }
            android.util.Log.w(createTag(TAG), createMessage(message, parameter), e);
        }

        @Override
        public void w(@Nullable Throwable e) {
            w(createTag(tag), e);
        }

        @Override
        public void w(String TAG, @Nullable Throwable e) {
            if (!isPrint()) {
                return;
            }
            android.util.Log.w(createTag(TAG), e);
        }

        @Override
        public void e(@NonNull Throwable e, String message, @Nullable Object... parameter) {
            e(createTag(tag), e, message, parameter);
        }

        @Override
        public void e(@NonNull String TAG, @NonNull Throwable e, String message,
                @Nullable Object... parameter) {
            if (!isPrint()) {
                return;
            }
            android.util.Log.e(createTag(TAG), createMessage(message, parameter), e);
        }

        @Override
        public void e(@NonNull String message, @Nullable Object... parameter) {
            e(createTag(tag), message, parameter);
        }

        @Override
        public void e(@NonNull String TAG, String message, @Nullable Object... parameter) {
            if (!isPrint()) {
                return;
            }
            android.util.Log.e(createTag(TAG), createMessage(message, parameter));
        }

        @Override
        public void e(@NonNull Throwable e) {
            e(createTag(tag), e);
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
                mLog = new Log(mGlobalTag);
            }
        }
        return mLog;
    }

    public ILogcat newLog() {
        return new Log(mGlobalTag);
    }

    private boolean isNull(Object object) {
        return object == null;
    }

    /**
     * com.orhanobut.logger.LoggerPrinter#createMessage
     */
    @NonNull
    public static String createMessage(String message, @Nullable Object... args) {
        if (null == message) {
            return "";
        }
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
}
