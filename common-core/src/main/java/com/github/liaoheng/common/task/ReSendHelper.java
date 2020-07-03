package com.github.liaoheng.common.task;

import android.util.SparseArray;

import com.github.liaoheng.common.thread.WorkProcessThread;
import com.github.liaoheng.common.util.L;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author liaoheng
 * @version 2020-06-15 09:57
 */
public class ReSendHelper {

    private SparseArray<QueueTask> mTask;
    private final String TAG;
    private final ReSendCallback mCallback;

    public interface ReSendCallback {
        void onReSend(Object object);
    }

    public ReSendHelper(ReSendCallback callback, String tag) {
        TAG = tag;
        mCallback = callback;
        mTask = new SparseArray<>();
    }

    private class QueueTask {
        private TaskDelayQueue<ReSendTask> mReSendDelayQueue;
        private SparseArray<Object> mSendSuccessArray;

        public QueueTask() {
            mSendSuccessArray = new SparseArray<>();
            mReSendDelayQueue = new TaskDelayQueue<>(new WorkProcessThread(
                    new WorkProcessThread.BaseHandler(L.alog(), TAG, "ReSendThread:" + UUID.randomUUID().toString()) {

                        @Override
                        public void onHandler() {
                            ReSendTask take = mReSendDelayQueue.take();
                            if (take == null) {
                                return;
                            }
                            int itemId = take.getKey();
                            if (take.count > 3) {
                                L.alog().d(TAG, "[ReSend] invalid groupId : %s, itemId : %s, count : %s",take.groupId, itemId, take.count);
                                return;
                            }
                            Object s = mSendSuccessArray.get(itemId);
                            if (s == null) {
                                L.alog().d(TAG, "[ReSend] failure groupId : %s, itemId : %s, count : %s", take.groupId, itemId, take.count);
                                mCallback.onReSend(take.object);//重新发送数据
                                addReSendTask(take.groupId, take.getKey(), take.object, take.count + 1);//放入重发队列，并增加发送次数
                            } else {
                                L.alog().d(TAG, "[ReSend] success groupId : %s, itemId : %s, count : %s", take.groupId, itemId, take.count);
                                mSendSuccessArray.remove(itemId);//数据发送成功，删除对应成功ID
                            }
                        }
                    }));
        }

        public void start() {
            mReSendDelayQueue.start();
        }

        public void stop() {
            mReSendDelayQueue.stop();
            mSendSuccessArray.clear();
        }

        public void destroy() {
            stop();
            mReSendDelayQueue = null;
            mSendSuccessArray = null;
        }

        public void addSendSuccess(int id, Object object) {
            mSendSuccessArray.put(id, object);
        }

        public void putTask(ReSendTask task) {
            mReSendDelayQueue.put(task);
        }
    }

    public QueueTask getTaskQueue(String groupId) {
        int key = Utils.getKey(groupId);
        QueueTask task = mTask.get(key);
        if (task == null) {
            task = new QueueTask();
            task.start();
            mTask.put(key, task);
        }
        return task;
    }

    public void remove(String mac) {
        int key = Utils.getKey(mac);
        QueueTask queueTask = mTask.get(key);
        if (queueTask != null) {
            queueTask.stop();
            mTask.remove(key);
        }
    }

    public void destroy() {
        for (int i = 0; i < mTask.size(); i++) {
            mTask.valueAt(i).destroy();
        }
        mTask.clear();
        mTask = null;
    }

    public void addReSendTask(String groupId, int itemId, Object object, int count) {
        L.alog().d(TAG, "[ReSend] add groupId : %s, itemId : %s, count : %s ", groupId, itemId, count);
        getTaskQueue(groupId).putTask(new ReSendTask(groupId, itemId, object, count, TimeUnit.SECONDS.toMillis(15)));
    }

    public void addSendSuccess(String groupId, int itemId, Object object) {
        L.alog().d(TAG, "[ReSend] reply groupId : %s, itemId : %s", groupId, itemId);
        getTaskQueue(groupId).addSendSuccess(itemId, object);
    }

    private class ReSendTask extends TaskDelayQueue.DelayedTask {
        private int count;
        private String groupId;
        private Object object;

        public ReSendTask(String groupId, int itemId, Object object, int count, long delayTime) {
            super(itemId, delayTime);
            this.groupId = groupId;
            this.object = object;
            this.count = count;
        }
    }
}
