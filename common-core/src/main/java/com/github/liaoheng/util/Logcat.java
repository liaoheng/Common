package com.github.liaoheng.util;

/**
 * 日志处理
 *
 * @author liaoheng
 * @version 2018-04-24 14:31
 */
@Deprecated
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

    private ILogger mLogger;

    public synchronized ILogger logger() {
        if (mLogger == null) {
            synchronized (BaseLogger.class) {
                mLogger = new BaseLogger(mGlobalTag);
            }
        }
        return mLogger;
    }

    public synchronized ILogger log() {
        return logger();
    }
}
