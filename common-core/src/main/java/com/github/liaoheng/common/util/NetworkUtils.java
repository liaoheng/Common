package com.github.liaoheng.common.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;

/**
 * assist us in sensing state of the networks.
 * need  android.permission.ACCESS_NETWORK_STATE
 * @author MaTianyu
 * @author liaoheng
 * @version 2017-1-4
 * */
@SuppressWarnings("MissingPermission")
public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();

    interface NetType {
        int NONE   = 1;
        int MOBILE = 2;
        int WIFI   = 4;
        int OTHER  = 8;
    }

    /**
     * 获取ConnectivityManager
     */
    public static ConnectivityManager getConnManager(Context context) {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /**
     * 判断当前使用网络的连接状态
     * @return boolean 不管wifi，还是mobile net，只有当前在连接状态返回true,反之false。
     */
    public static boolean isConnected(Context context) {
        NetworkInfo net = getConnManager(context).getActiveNetworkInfo();
        return net != null && net.isConnected();
    }

    /**
     * 判断有无网络正在连接中（查找网络、校验、获取IP等）。
     * @return boolean 不管wifi，还是mobile net，只有当前在连接状态（可有效传输数据）才返回true,反之false。
     */
    public static boolean isConnectedOrConnecting(Context context) {
        NetworkInfo[] nets = getConnManager(context).getAllNetworkInfo();
        if (nets != null) {
            for (NetworkInfo net : nets) {
                if (net.isConnectedOrConnecting()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断当前使用网络类型
     * @return {@link NetType}
     */
    public static int getConnectedType(Context context) {
        NetworkInfo net = getConnManager(context).getActiveNetworkInfo();
        if (net != null) {
            switch (net.getType()) {
                case ConnectivityManager.TYPE_WIFI:
                    return NetType.WIFI;
                case ConnectivityManager.TYPE_MOBILE:
                    return NetType.MOBILE;
                default:
                    return NetType.OTHER;
            }
        }
        return NetType.NONE;
    }

    /**
     * 是否存在有效的WIFI连接
     * @return 有true
     */
    public static boolean isWifiConnected(Context context) {
        NetworkInfo net = getConnManager(context).getActiveNetworkInfo();
        return net != null && net.getType() == ConnectivityManager.TYPE_WIFI && net.isConnected();
    }

    /**
     * 是否存在有效的移动连接
     * @return 有true
     */
    public static boolean isMobileConnected(Context context) {
        NetworkInfo net = getConnManager(context).getActiveNetworkInfo();
        return net != null && net.getType() == ConnectivityManager.TYPE_MOBILE && net.isConnected();
    }

    public static String getWifiSsidNmae(String wifiName) {
        if (TextUtils.isEmpty(wifiName)) {
            return "No Wifi Name";
        }
        int len = wifiName.length();
        if (wifiName.startsWith("\"") && wifiName.endsWith("\"")) {
            wifiName = wifiName.substring(1, len - 1);
        }
        return wifiName;
    }

    /**
     * <a href="https://developer.android.com/reference/android/net/wifi/WifiManager.html">WifiManager</a>
     */
    public static WifiInfo getWifiInfo(@NonNull Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifi.getConnectionInfo();
    }

    /**
     * 获取 MAC 地址 , 6.0 需要相应权限:android.permission.ACCESS_WIFI_STATE
     */
    public static String getMacAddress(@NonNull Context context) {
        return getWifiInfo(context).getMacAddress();
    }

    /**
     * 打印当前各种网络状态
     * @param context
     * @return boolean
     */
    public static boolean printNetworkInfo(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo in = connectivity.getActiveNetworkInfo();
            L.Log.d(TAG, "-------------$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$-------------");
            L.Log.d(TAG, "getActiveNetworkInfo: " + in);
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    // if (info[i].getType() == ConnectivityManager.TYPE_WIFI) {
                    L.Log.d(TAG, "NetworkInfo[" + i + "]isAvailable : " + info[i].isAvailable());
                    L.Log.d(TAG, "NetworkInfo[" + i + "]isConnected : " + info[i].isConnected());
                    L.Log.d(
                            TAG,
                            "NetworkInfo[" + i + "]isConnectedOrConnecting : "
                                    + info[i].isConnectedOrConnecting());
                    L.Log.d(TAG, "NetworkInfo[" + i + "]: " + info[i]);
                    // }
                }
                L.Log.d(TAG, "\n");
            } else {
                L.Log.d(TAG, "getAllNetworkInfo is null");
            }
        }
        return false;
    }
}
