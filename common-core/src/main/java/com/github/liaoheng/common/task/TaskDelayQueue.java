package com.github.liaoheng.common.task;

import androidx.annotation.Nullable;

import com.github.liaoheng.common.thread.IWorkProcessThread;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author liaoheng
 * @version 2020-01-06 17:00
 */
public class TaskDelayQueue<T extends TaskDelayQueue.DelayedTask> extends DelayQueue<T> implements IWorkProcessThread {

    private IWorkProcessThread mWorkProcessThread;

    public TaskDelayQueue(IWorkProcessThread workProcessThread) {
        mWorkProcessThread = workProcessThread;
    }

    @Override
    public void start() {
        mWorkProcessThread.start();
    }

    @Override
    public void stop() {
        clear();
        mWorkProcessThread.stop();
    }

    @Override
    public boolean isRunning() {
        return mWorkProcessThread.isRunning();
    }

    @Nullable
    @Override
    public Handler getRunnable() {
        return mWorkProcessThread.getRunnable();
    }

    @SuppressWarnings("WeakerAccess")
    public static class DelayedTask implements Delayed {
        private int key;
        private long executeTime;//ms

        public int getKey() {
            return key;
        }

        public DelayedTask(long delayTime) {
            this(0, delayTime);
        }

        public DelayedTask(int key, long delayTime) {
            this.key = key;
            this.executeTime = System.currentTimeMillis() + delayTime;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(executeTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            if (this.getDelay(TimeUnit.MILLISECONDS) > o.getDelay(TimeUnit.MILLISECONDS)) {
                return 1;
            } else if (this.getDelay(TimeUnit.MILLISECONDS) < o.getDelay(TimeUnit.MILLISECONDS)) {
                return -1;
            }
            return 0;
        }
    }

    @Nullable
    @Override
    public T take() {
        try {
            return super.take();
        } catch (InterruptedException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void put(TaskDelayQueue.DelayedTask t) {
        super.put((T) t);
    }
}
