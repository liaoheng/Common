package com.github.liaoheng.common.util;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * @author liaoheng
 * @date 2021-12-02 11:27
 */
public class DelayHandler extends Handler {

    public DelayHandler(@NonNull Looper looper) {
        super(looper);
    }

    public DelayHandler(@NonNull Looper looper, Callback callback) {
        super(looper, callback);
    }

    public void sendDelayed(int what, long delayMillis) {
        removeMessages(what);
        sendMessageDelayed(obtainMessage(what), delayMillis);
    }

    public void sendDelayed(int what, Object obj, long delayMillis) {
        removeMessages(what);
        sendMessageDelayed(obtainMessage(what, obj), delayMillis);
    }

    public void sendDelayed(int what, int arg1, int arg2, Object obj, long delayMillis) {
        removeMessages(what);
        sendMessageDelayed(obtainMessage(what,arg1,arg2, obj), delayMillis);
    }
}
