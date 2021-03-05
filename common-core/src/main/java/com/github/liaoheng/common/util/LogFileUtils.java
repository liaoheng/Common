package com.github.liaoheng.common.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.StringDef;

import org.joda.time.DateTime;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 日志写入文件
 *
 * @author liaoheng
 * @version 0.1.1
 * @date 2016-09-22 16:26
 */
public class LogFileUtils {
    public final static String LEVEL_VERBOSE = " VERBOSE ";
    public final static String LEVEL_DEBUG = " DEBUG ";
    public final static String LEVEL_INFO = " INFO ";
    public final static String LEVEL_WARN = " WARN ";
    public final static String LEVEL_ERROR = " ERROR ";

    @StringDef({ LEVEL_VERBOSE, LEVEL_ERROR, LEVEL_WARN, LEVEL_DEBUG,
            LEVEL_INFO })
    @Retention(RetentionPolicy.SOURCE)
    public @interface LevelFlags {}

    private final String DEFAULT_FILE_NAME = "debug_log.txt";
    private final AtomicBoolean isClose = new AtomicBoolean(true);
    private FileOutputStream mFileOutputStream;
    private String mLogFilePath = "";
    private LogFileCallback mCallback;

    public interface LogFileCallback {
        File getFile();
    }

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

    public File createCachePath(Context context, String dir, String fileName) throws IOException {
        File log = FileUtils.createProjectSpaceDir(context, dir);
        if (TextUtils.isEmpty(fileName)) {
            fileName = DEFAULT_FILE_NAME;
        }
        return FileUtils.createFile(log, fileName);
    }

    public File open(Context context) throws IOException {
        return open(context, "");
    }

    public File open(Context context, String fileName) throws IOException {
        return open(context, "Log", fileName);
    }

    public File open(Context context, String dir, String fileName) throws IOException {
        return open(createCachePath(context,dir,fileName));
    }

    public File open(File logFile) {
        try {
            FileUtils.createFile(logFile);
            mFileOutputStream = new FileOutputStream(logFile, true);//不覆盖
            mLogFilePath = logFile.getAbsolutePath();
            isClose.set(false);
        } catch (IOException ignored) {
        }
        return logFile;
    }

    public void open(LogFileCallback callback) {
        mCallback = callback;
        open(callback.getFile());
    }

    public void close() {
        if (mFileOutputStream == null) {
            return;
        }
        try {
            isClose.set(true);
            mFileOutputStream.close();
            mFileOutputStream = null;
        } catch (IOException ignored) {
        }
    }

    public void clearFile() {
        FileUtils.delete(new File(mLogFilePath));
    }

    private void checkLogFile(File logFile) {
        if (!mLogFilePath.equals(logFile.getAbsolutePath())) {
            open(logFile);
        }
    }

    public File getLogFile() {
        return new File(mLogFilePath);
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

    public synchronized void log(@LevelFlags String level, String tag,
            String logEntry) {
        writeLog(level, tag, null, logEntry);
    }

    public synchronized void log(@LevelFlags String level, String tag,
            Throwable throwable,
            String logEntry) {
        writeLog(level, tag, throwable, logEntry);
    }

    private synchronized void writeLog(String severityLevel, String tag, Throwable throwable,
            String logEntry) {
        if (isClose.get()){
            return;
        }
        if (mCallback != null) {
            checkLogFile(mCallback.getFile());
        }
        if (mFileOutputStream==null){
            return;
        }
        String currentDateTime = DateTime.now().toString("yyyy-MM-dd HH:mm:ss.SSS");
        String stencil = currentDateTime + "   |" + severityLevel + "|   " + tag + " : " + logEntry;
        try {
            FileChannel channel = mFileOutputStream.getChannel();
            channel.write(ByteBuffer.wrap(stencil.getBytes()));
            if (throwable != null) {
                channel.write(ByteBuffer.wrap("\n".getBytes()));
                channel.write(ByteBuffer.wrap(Log.getStackTraceString(throwable).getBytes()));
            }
            channel.write(ByteBuffer.wrap("\n".getBytes()));
        } catch (IOException ignored) {
        }
    }

}
