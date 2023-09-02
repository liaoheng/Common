package com.github.liaoheng.common.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

/**
 * 网络状态监听
 * android.permission.ACCESS_NETWORK_STATE
 *
 * @author liaoheng
 * @date 2021-12-07 15:22
 */
@SuppressLint("MissingPermission")
public class NetworkStatusReceiver {
    private final String TAG = this.getClass().getSimpleName();

    private final Consumer<Boolean> mCallback;
    public boolean mDisconnectRealTime;
    public ConnectivityManager mConnectivityManager;
    private HandlerThread mHandlerThread;
    private DelayHandler mHandler;
    private String mPingUrl;

    public String getPingUrl() {
        return mPingUrl;
    }

    /**
     * @param pingUrl etc: www.google.com || 127.0.0.1
     */
    public void setPingUrl(String pingUrl) {
        mPingUrl = pingUrl;
    }

    public NetworkStatusReceiver(Consumer<Boolean> callback, boolean disconnectRealTime) {
        mCallback = callback;
        mDisconnectRealTime = disconnectRealTime;
    }

    public NetworkStatusReceiver(Consumer<Boolean> callback) {
        this(callback, false);
    }

    private final Handler.Callback mHandlerCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            L.d(TAG, "onReceive#connected: " + msg.obj);
            if (msg.what == 1) {
                Boolean isConnected = (Boolean) msg.obj;
                if (isConnected && !TextUtils.isEmpty(mPingUrl)) {
                    boolean ping = NetworkUtils.ping(mPingUrl);
                    L.alog().d(TAG, "ping: " + ping);
                    mCallback.accept(ping);
                } else {
                    mCallback.accept(isConnected);
                }
            }
            return false;
        }
    };

    private int getConnectState(boolean isConnected) {
        return isConnected ? 1 : 0;
    }

    public void checkConnected(Context context) {
        if (NetworkUtils.isConnected(context)) {
            if (!TextUtils.isEmpty(mPingUrl)) {
                boolean ping = NetworkUtils.ping(mPingUrl);
                L.alog().d(TAG, "ping: " + ping);
                onConnected(ping);
            } else {
                onConnected(true);
            }
        } else {
            onDisConnected(true);
        }
    }

    public void initHandler() {
        mHandlerThread = new HandlerThread(TAG);
        mHandlerThread.start();
        mHandler = new DelayHandler(mHandlerThread.getLooper(), mHandlerCallback);
    }

    public void releaseHandler() {
        if (mHandler != null) {
            mHandler.removeMessages(1);
        }
        if (mHandlerThread != null) {
            mHandlerThread.quit();
        }
    }

    public void registerReceiver(Context context) {
        initHandler();
        checkConnected(context);
        mConnectivityManager = context.getSystemService(ConnectivityManager.class);
        mConnectivityManager.registerDefaultNetworkCallback(mNetworkCallback);
    }

    private void onConnected(boolean realTime) {
        if (mHandler == null) {
            return;
        }
        mHandler.removeMessages(1);
        mHandler.sendDelayed(1, true, realTime ? 200 : 1000);
    }

    private void onDisConnected(boolean realTime) {
        if (mHandler == null) {
            return;
        }
        mHandler.removeMessages(1);
        mHandler.sendDelayed(1, false, realTime ? 200 : 1000);
    }

    private final ConnectivityManager.NetworkCallback mNetworkCallback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(@NonNull Network network) {
            L.alog().d(TAG, "onAvailable");
            onConnected(false);
        }

        @Override
        public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
            L.alog().d(TAG, "onCapabilitiesChanged: " + networkCapabilities);
            super.onCapabilitiesChanged(network, networkCapabilities);
            if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
                L.alog().d(TAG, "onCapabilitiesChanged: onConnected");
                onConnected(false);
            } else {
                L.alog().d(TAG, "onCapabilitiesChanged: onDisConnected");
                onDisConnected(false);
            }
        }

        @Override
        public void onLosing(@NonNull Network network, int maxMsToLive) {
            L.alog().d(TAG, "onLosing: " + maxMsToLive);
            super.onLosing(network, maxMsToLive);
        }

        @Override
        public void onLost(@NonNull Network network) {
            L.alog().d(TAG, "onLost");
            onDisConnected(mDisconnectRealTime);
        }

        @Override
        public void onUnavailable() {
            L.alog().d(TAG, "onUnavailable");
        }
    };

    public void unregisterReceiver(Context context) {
        if (mConnectivityManager != null) {
            mConnectivityManager.unregisterNetworkCallback(mNetworkCallback);
        }
        releaseHandler();
    }

}
