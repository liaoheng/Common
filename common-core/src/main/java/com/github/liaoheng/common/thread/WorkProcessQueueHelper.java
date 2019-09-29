package com.github.liaoheng.common.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author liaoheng
 * @version 2018-11-15 12:35
 */
public class WorkProcessQueueHelper<T> {
    private final BlockingQueue<T> mDataQueue = new LinkedBlockingQueue<>();
    private IWorkProcessThread mWorkProcessThread;

    public WorkProcessQueueHelper(IWorkProcessThread workProcessThread) {
        mWorkProcessThread = workProcessThread;
    }

    public boolean isWorkThreadRunning() {
        return mWorkProcessThread != null && mWorkProcessThread.isRunning();
    }

    public IWorkProcessThread getWorkProcessThread() {
        return mWorkProcessThread;
    }

    public void putQueue(T data) {
        if (!isWorkThreadRunning()) {
            return;
        }
        mDataQueue.offer(data);
    }

    public T takeQueue() {
        if (!isWorkThreadRunning()) {
            return null;
        }
        try {
            return mDataQueue.take();
        } catch (InterruptedException ignored) {
        }
        return null;
    }

    public void clearQueue() {
        mDataQueue.clear();
    }

}
