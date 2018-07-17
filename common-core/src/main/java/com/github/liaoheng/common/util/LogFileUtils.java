package com.github.liaoheng.common.util;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.StringDef;
import android.text.TextUtils;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 不带system log
 *
 * @author liaoheng
 * @version 2016-09-22 16:26
 */
public class LogFileUtils {
    private static final String TAG = LogFileUtils.class.getSimpleName();

    public final static String LEVEL_VERBOSE = " VERBOSE ";
    public final static String LEVEL_DEBUG = " DEBUG ";
    public final static String LEVEL_INFO = " INFO ";
    public final static String LEVEL_WARN = " WARN ";
    public final static String LEVEL_ERROR = " ERROR ";

    @StringDef({ LEVEL_VERBOSE, LEVEL_ERROR, LEVEL_WARN, LEVEL_DEBUG,
            LEVEL_INFO })
    @Retention(RetentionPolicy.SOURCE)
    public @interface LevelFlags {}

    private static final String DEFAULT_FILE_NAME = "debug_log.txt";
    private File mLogFile = new File(
            Environment.getExternalStorageDirectory(), DEFAULT_FILE_NAME);
    private static LogFileUtils instance;

    private LogFileUtils() {
    }

    public static synchronized LogFileUtils get() {
        if (instance == null) {
            synchronized (LogFileUtils.class) {
                if (instance == null) {
                    instance = new LogFileUtils();
                }
            }
        }
        return instance;
    }

    public void init(Context context) throws SystemException {
        init(context,"");
    }

    public void init(File logFile) {
        if (logFile == null) {
            return;
        }
        mLogFile = logFile;
        L.Log.d(TAG, "init log file : %s ", mLogFile.getAbsoluteFile());
    }

    public void init(Context context,String fileName) throws SystemException {
        File log = FileUtils.createProjectSpaceDir(context,"Log");
        if (TextUtils.isEmpty(fileName)) {
            fileName = DEFAULT_FILE_NAME;
        }
        mLogFile = FileUtils.createFile(log, fileName);
        L.Log.d(TAG, "init log file : %s ", mLogFile.getAbsoluteFile());
    }

    private OutputStream mFileOutputStream;

    public synchronized void open() {
        try {
            mFileOutputStream = FileUtils.openOutputStream(mLogFile, true);//不覆盖
        } catch (IOException ignored) {
        }
    }

    public synchronized void close() {
        IOUtils.closeQuietly(mFileOutputStream);
        mFileOutputStream = null;
    }

    public void clearFile() {
        close();
        FileUtils.delete(mLogFile);
    }

    public synchronized void w(String tag, String logEntry, Object... o) {
        log(LEVEL_WARN, tag, getLog(logEntry, o));
    }

    public synchronized void w(String tag, Throwable throwable) {
        log(LEVEL_WARN, tag, throwable, throwable.getMessage());
    }

    public synchronized void w(String tag, Throwable throwable, String logEntry, Object... o) {
        log(LEVEL_WARN, tag, throwable, getLog(logEntry, o));
    }

    public synchronized void e(String tag, String logEntry, Object... o) {
        log(LEVEL_ERROR, tag, getLog(logEntry, o));
    }

    public synchronized void e(String tag, Throwable throwable) {
        log(LEVEL_ERROR, tag, throwable, throwable.getMessage());
    }

    public synchronized void e(String tag, Throwable throwable, String logEntry, Object... o) {
        log(LEVEL_ERROR, tag, throwable, getLog(logEntry, o));
    }

    public synchronized void d(String tag, String logEntry, Object... o) {
        log(LEVEL_DEBUG, tag, getLog(logEntry, o));
    }

    public synchronized void i(String tag, String logEntry, Object... o) {
        log(LEVEL_INFO, tag, getLog(logEntry, o));
    }

    public synchronized String getLog(String logEntry, Object... o) {
        return String.format(logEntry, o);
    }

    public synchronized void log(@LevelFlags String level, String tag, String logEntry) {
        writeLog(level, tag, null, logEntry);
    }

    public synchronized void log(@LevelFlags String level, String tag, Throwable throwable,
            String logEntry) {
        writeLog(level, tag, throwable, logEntry);
    }

    private synchronized void writeLog(String severityLevel, String tag, Throwable throwable,
            String logEntry) {
        if (mFileOutputStream == null) {
            return;
        }
        String currentDateTime = DateTime.now().toString("yyyy-MM-dd HH:mm:ss.SSS");
        String stencil = currentDateTime + "   |" + severityLevel + "|   " + tag + " : " + logEntry;
        try {
            IOUtils.write(stencil, mFileOutputStream);
            if (throwable != null) {
                IOUtils.write("\n", mFileOutputStream);
                throwable.printStackTrace(new PrintStream(mFileOutputStream));
                mFileOutputStream.flush();
            }
            IOUtils.write("\n", mFileOutputStream);
        } catch (IOException ignored) {
        }
    }

}
