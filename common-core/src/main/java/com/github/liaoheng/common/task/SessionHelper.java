package com.github.liaoheng.common.task;

import android.util.SparseArray;

import androidx.annotation.Nullable;

import com.github.liaoheng.common.thread.WorkProcessThread;
import com.github.liaoheng.common.util.L;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author liaoheng
 * @version 2019-09-25 10:26
 */
public class SessionHelper<T> {

    public static class Session<T> {
        private String id;
        private long start;
        private long end;
        private long time;
        private T object;

        public Session(String id, long start) {
            this.id = id;
            this.start = start;
            this.time = start;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public long getStart() {
            return start;
        }

        public void setStart(long start) {
            this.start = start;
        }

        public long getEnd() {
            return end;
        }

        public void setEnd(long end) {
            this.end = end;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public T getObject() {
            return object;
        }

        public void setObject(T object) {
            this.object = object;
        }
    }

    public interface SessionCallback<T> {
        void onBegin(Session<T> session);

        void onEnd(Session<T> session);

        void onInvalid(Session<T> session);

        void onUpdate(Session<T> session);
    }

    public interface Customize<T> {
        boolean check(Session<T> session);
    }

    private String TAG;
    private TaskDelayQueue<SessionTask> mSessionDelayQueue;
    private SparseArray<Session<T>> mSessionArray = new SparseArray<>();
    private SessionCallback<T> mSessionCallback;
    private Customize<T> mCustom;
    /**
     * milliseconds
     */
    private long expiredTime = TimeUnit.MINUTES.toMillis(5);

    public void setExpiredTime(long expiredTime) {
        this.expiredTime = expiredTime;
    }

    public SparseArray<Session<T>> getSessionArray() {
        return mSessionArray;
    }

    public void setCustom(Customize<T> custom) {
        mCustom = custom;
    }

    public SessionHelper(SessionCallback<T> callback, String tag) {
        TAG = tag;
        mSessionCallback = callback;
        mSessionDelayQueue = new TaskDelayQueue<>(new WorkProcessThread(
                new WorkProcessThread.BaseHandler(L.alog(), TAG,
                        TAG + "_SessionThread") {

                    @Override
                    public void onHandler() {
                        SessionTask take = mSessionDelayQueue.take();
                        if (take == null) {
                            return;
                        }
                        String id = take.getSessionId();
                        Session<T> session = getSession(id);
                        if (session == null) {//当前session已被处理，无需其它操作
                            return;
                        }
                        boolean check;
                        if (mCustom == null) {
                            check = System.currentTimeMillis() - session.getTime() > expiredTime;
                        } else {
                            check = mCustom.check(session);
                        }
                        if (check) {
                            L.alog().d(TAG, "[Session] invalid id : %s ", id);
                            try {
                                if (mSessionCallback != null) {
                                    mSessionCallback.onInvalid(session);
                                }
                            } catch (Exception ignored) {
                            }
                        } else {
                            try {
                                if (mSessionCallback != null) {
                                    mSessionCallback.onUpdate(session);
                                }
                            } catch (Exception ignored) {
                            }
                            addTask(session);
                        }
                    }
                }));
    }

    public boolean isRunning() {
        return mSessionDelayQueue.isRunning();
    }

    public boolean isInit() {
        return mSessionDelayQueue.isRunning();
    }

    public void start() {
        mSessionDelayQueue.start();
    }

    public void stop() {
        mSessionDelayQueue.stop();
        mSessionArray.clear();
    }

    public void addTask(Session<T> session) {
        if (session == null) {
            return;
        }
        mSessionDelayQueue.put(new SessionTask(session));
        L.alog().d(TAG, "[Session] addTask id : %s", session.getId());
    }

    @Nullable
    public synchronized Session<T> getSession(String sessionId) {
        return mSessionArray.get(getSessionId(sessionId));
    }

    private int getSessionId(String sessionId) {
        return Utils.getKey(sessionId);
    }

    private synchronized void putSession(Session<T> session) {
        String sessionId = session.getId();
        mSessionArray.put(getSessionId(sessionId), session);
        L.alog().d(TAG, "[Session] putOrUpdate id : %s ", sessionId);
    }

    public synchronized void removeSession(String sessionId) {
        Session<T> session = getSession(sessionId);
        if (session == null) {
            return;
        }
        mSessionArray.remove(getSessionId(sessionId));
        L.alog().d(TAG, "[Session] remove id : %s ", sessionId);
    }

    public synchronized void clearSession() {
        mSessionArray.clear();
        mSessionDelayQueue.clear();
    }

    public void initSession(List<Session<T>> sessionList) {
        for (Session<T> session : sessionList) {
            mSessionArray.put(getSessionId(session.getId()), session);
            L.alog().d(TAG, "[Session] init session id : %s", session.getId());
        }
    }

    public void beginSession(Session<T> session) {
        addTask(session);
        putSession(session);
        if (mSessionCallback != null) {
            mSessionCallback.onBegin(session);
        }
    }

    public void updateSession(Session<T> session, long time) {
        session.setTime(time);
        putSession(session);
    }

    public void endSession(Session<T> session) {
        endSession(session, System.currentTimeMillis());
    }

    public void endSession(Session<T> session, long end) {
        session.setEnd(end);
        removeSession(session.getId());
        if (mSessionCallback != null) {
            mSessionCallback.onEnd(session);
        }
    }

    static class SessionTask extends TaskDelayQueue.DelayedTask {
        private String sessionId;

        public String getSessionId() {
            return sessionId;
        }

        public SessionTask(Session<?> session) {
            this(session.getId(), TimeUnit.SECONDS.toMillis(60));
        }

        public SessionTask(String sessionId, long delayTime) {
            super(delayTime);
            this.sessionId = sessionId;
        }
    }

}
