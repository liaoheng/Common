package com.github.liaoheng.common.thread;

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

    @Override
    public Handler getRunnable() {
        return mRunnable;
    }

    public String getName() {
        return mThread == null ? "" : mThread.getName();
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
