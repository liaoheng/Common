package com.github.liaoheng.common.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author liaoheng
 * @version 2018-11-15 12:35
 */
public class WorkProcessQueueHelper<T> implements IWorkProcessThread {
    private final BlockingQueue<T> mDataQueue = new LinkedBlockingQueue<>();
    private IWorkProcessThread mWorkProcessThread;

    public WorkProcessQueueHelper(IWorkProcessThread workProcessThread) {
        mWorkProcessThread = workProcessThread;
    }

    public IWorkProcessThread getWorkProcessThread() {
        return mWorkProcessThread;
    }

    public BlockingQueue<T> getQueue() {
        return mDataQueue;
    }

    @Override
    public void start() {
        if (mWorkProcessThread == null) {
            return;
        }
        mWorkProcessThread.start();
    }

    @Override
    public void stop() {
        if (mWorkProcessThread == null) {
            return;
        }
        mWorkProcessThread.stop();
    }

    @Override
    public boolean isRunning() {
        return mWorkProcessThread != null && mWorkProcessThread.isRunning();
    }

    @Override
    public Handler getRunnable() {
        return mWorkProcessThread == null ? null : mWorkProcessThread.getRunnable();
    }

    public void putQueue(T data) {
        if (!isRunning()) {
            return;
        }
        mDataQueue.offer(data);
    }

    public T takeQueue() {
        if (!isRunning()) {
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

    public void destroy() {
        stop();
        clearQueue();
    }

}
