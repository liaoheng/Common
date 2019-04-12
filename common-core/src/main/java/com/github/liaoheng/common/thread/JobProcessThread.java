package com.github.liaoheng.common.thread;

/**
 * @author liaoheng
 * @version 2018-11-06 10:17
 */
public class JobProcessThread extends JobThread {
    private WorkProcessThread.Handler mHandler;

    public JobProcessThread(WorkProcessThread.Handler handler) {
        super(handler.name());
        mHandler = handler;
    }

    @Override
    protected void process() throws InterruptedException {
        mHandler.onStart(getName());
        while (isRunning()) {
            mHandler.onHandler();
        }
        mHandler.onStop(getName());
    }
}
