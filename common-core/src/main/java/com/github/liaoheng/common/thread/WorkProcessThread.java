package com.github.liaoheng.common.thread;

import android.text.TextUtils;

import com.github.liaoheng.common.util.Logcat;

import java.util.UUID;

/**
 * @author liaoheng
 * @version 2018-10-25 11:13
 */
public class WorkProcessThread implements IWorkProcessThread {
    private Handler mRunnable;
    private JobThread mThread;

    @Override
    public boolean isRunning() {
        return mThread != null && mThread.isRunning();
    }

    public String getName() {
        return mThread == null ? "" : mThread.getName();
    }

    public interface Handler {
        String name();

        void onStart(String name);

        void onHandler() throws InterruptedException;

        void onStop(String name);
    }

    public abstract static class BaseHandler implements Handler {
        private String mThreadName;
        private Logcat.ILogcat mLogcat;
        private String mTag;

        public BaseHandler(Logcat.ILogcat logcat, String tag) {
            mLogcat = logcat;
            mTag = tag;
        }

        public BaseHandler(Logcat.ILogcat logcat, String tag, String name) {
            this(logcat, tag);
            mThreadName = name;
        }

        @Override
        public String name() {
            return TextUtils.isEmpty(mThreadName) ? UUID.randomUUID().toString() : mThreadName;
        }

        @Override
        public void onStart(String name) {
            mLogcat.d(mTag, "start > %s", name);
        }

        @Override
        public void onStop(String name) {
            mLogcat.d(mTag, "stop > %s", name);
        }
    }

    public WorkProcessThread(Handler runnable) {
        mRunnable = runnable;
    }

    @Override
    public void start() {
        if (mThread == null || !mThread.isRunning()) {
            mThread = new JobProcessThread(mRunnable);
        }
        mThread.start();
    }

    @Override
    public void stop() {
        if (mThread == null || !mThread.isRunning()) {
            return;
        }
        mThread.shutdown();
        mThread = null;
    }
}
