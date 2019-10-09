package com.github.liaoheng.common.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import androidx.annotation.NonNull;

/**
 * assist us in sensing state of the networks.
 * <br/> Dependency : android.permission.ACCESS_NETWORK_STATE
 *
 * @author liaoheng
 * @version 2017-1-4
 */
@SuppressWarnings("MissingPermission")
@SuppressLint({ "WifiManagerPotentialLeak", "HardwareIds" })
public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();

    interface NetType {
        int NONE = 1;
        int MOBILE = 2;
        int WIFI = 4;
        int OTHER = 8;
    }

    /**
     * 获取ConnectivityManager
     */
    public static ConnectivityManager getConnManager(@NonNull Context context) {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /**
     * <a href="https://developer.android.com/reference/android/net/wifi/WifiManager.html">WifiManager</a>
     */
    public static WifiManager getWifiManager(@NonNull Context context) {
        return (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    /**
     * <a href="https://developer.android.com/reference/android/net/wifi/WifiManager.html">WifiManager</a>
     */
    public static WifiInfo getWifiInfo(@NonNull Context context) {
        return getWifiManager(context).getConnectionInfo();
    }

    /**
     * 判断当前wifi中是否含有 关键字
     *
     * @param contains 关键字
     */
    public static boolean isContainsWifiName(Context context, @NonNull String contains) {
        if (!isWifiEnabled(context)) {
            return false;
        }
        WifiInfo wifiInfo = getWifiManager(context).getConnectionInfo();
        return wifiInfo != null && wifiInfo.getSSID() != null && wifiInfo.getSSID().contains(contains);
    }

    /**
     * 判断wifi是否打开
     */
    public static boolean isWifiEnabled(@NonNull Context context) {
        WifiManager wifiManager = getWifiManager(context);
        return wifiManager.isWifiEnabled();
    }

    /**
     * 判断当前使用网络的连接状态
     *
     * @return boolean 不管wifi，还是mobile net，只有当前在连接状态返回true,反之false。
     */
    public static boolean isConnected(Context context) {
        NetworkInfo[] nets = NetworkUtils.getConnManager(context).getAllNetworkInfo();
        if (nets.length > 0) {
            for (NetworkInfo net : nets) {
                if (net.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断当前使用网络类型
     *
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
     *
     * @return 有true
     */
    public static boolean isWifiConnected(Context context) {
        NetworkInfo net = getConnManager(context).getActiveNetworkInfo();
        return net != null && net.getType() == ConnectivityManager.TYPE_WIFI && net.isConnected();
    }

    /**
     * 是否存在有效的移动连接
     *
     * @return 有true
     */
    public static boolean isMobileConnected(Context context) {
        NetworkInfo net = getConnManager(context).getActiveNetworkInfo();
        return net != null && net.getType() == ConnectivityManager.TYPE_MOBILE && net.isConnected();
    }

    /**
     * 获取没有双引号的 {@link WifiInfo#getSSID} 名。
     */
    public static String getWifiSsidName(String wifiName) {
        if (TextUtils.isEmpty(wifiName) || "<unknown ssid>".equals(wifiName)) {
            return "";
        }
        int len = wifiName.length();
        if (wifiName.startsWith("\"") && wifiName.endsWith("\"")) {
            wifiName = wifiName.substring(1, len - 1);
        }
        return wifiName;
    }

    /**
     * 获取wifi ssid名，编码为UTF-8
     */
    public static String getWifiSsidNameCharset(WifiInfo wifiInfo) {
        try {
            Field wifiSsidfield = WifiInfo.class
                    .getDeclaredField("mWifiSsid");
            wifiSsidfield.setAccessible(true);
            Class<?> wifiSsidClass = wifiSsidfield.getType();
            Object wifiSsid = wifiSsidfield.get(wifiInfo);
            Method method = wifiSsidClass
                    .getDeclaredMethod("getOctets");
            byte[] bytes = (byte[]) method.invoke(wifiSsid);
            return new String(bytes, "UTF-8");
        } catch (Exception e) {
            L.Log.w(TAG, e);
        }
        return null;
    }

    /**
     * <a href="https://stackoverflow.com/questions/11705906/programmatically-getting-the-mac-of-an-android-device">get
     * mac address</a>
     */
    public static String getMacAddress(@NonNull Context context) {
        return getWifiInfo(context).getMacAddress();
    }
}
