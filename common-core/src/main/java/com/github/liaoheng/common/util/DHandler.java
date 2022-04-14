package com.github.liaoheng.common.util;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * @author liaoheng
 * @date 2021-12-02 11:27
 */
public class DHandler extends Handler {

    public DHandler(@NonNull Looper looper) {
        super(looper);
    }

    public DHandler(@NonNull Looper looper, Callback callback) {
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
}
