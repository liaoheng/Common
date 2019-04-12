package com.github.liaoheng.common.thread;

import com.github.liaoheng.common.util.Logcat;

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
        private Logcat.ILogcat mLog;
        private String mTag;

        public BaseHandler(Logcat.ILogcat log, String tag) {
            mLog = log;
            mTag = tag;
        }

        @Override
        public void onStart(String name) {
            mLog.d(mTag, name + " start");
        }

        @Override
        public void onStop(String name) {
            mLog.d(mTag, name + " stop");
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
    }
}
