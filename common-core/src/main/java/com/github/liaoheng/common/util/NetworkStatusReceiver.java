package com.github.liaoheng.common.util;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

/**
 * 网络状态监听
 *
 * @author liaoheng
 * @date 2021-12-07 15:22
 */
@SuppressLint("MissingPermission")
public class NetworkStatusReceiver extends BroadcastReceiver {

    private final Callback1<Boolean> mCallback;
    public boolean mDisconnectRealTime;

    public NetworkStatusReceiver(Callback1<Boolean> callback, boolean disconnectRealTime) {
        mCallback = callback;
        mDisconnectRealTime = disconnectRealTime;
    }

    public NetworkStatusReceiver(
            Callback1<Boolean> callback) {
        this(callback, false);
    }

    private final DHandler mHandler = new DHandler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            L.d("NetworkStatusReceiver", "onReceive#connected: " + msg.obj);
            if (msg.what == 1) {
                mCallback.accept((Boolean) msg.obj);
            }
        }
    };

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean connected = NetworkUtils.isConnected(context);
        if (!connected && mDisconnectRealTime) {
            mCallback.accept(false);
        } else {
            mHandler.sendDelayed(1, connected, 1000);
        }
    }

    public void registerReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(this, filter);
    }

    public void unregisterReceiver(Context context) {
        try {
            context.unregisterReceiver(this);
        } catch (Throwable ignored) {
        }
    }

}
