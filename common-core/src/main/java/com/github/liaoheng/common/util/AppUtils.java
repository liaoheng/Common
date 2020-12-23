package com.github.liaoheng.common.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.WallpaperManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.os.PowerManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;

import com.github.liaoheng.common.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

/**
 * app工具
 *
 * @author liaoheng
 */
public class AppUtils {
    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    /**
     * 获得系统版本信息
     *
     * @param application {@link Context}
     * @return {@link PackageInfo}
     */
    @Nullable
    public static PackageInfo getVersionInfo(Context application) {
        PackageManager mg = application.getPackageManager();
        try {
            return mg.getPackageInfo(application.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    /**
     * 重启应用:需要启动界面与后面界面在一个task中，如果使用单例需用singleTask。
     */
    public static void restartApplication(Activity activity) {
        Intent intent = activity.getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(activity.getBaseContext().getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    /**
     * 创建一个启动图标
     *
     * @param title 快捷键的标题
     * @param logo  快捷键的图标
     */
    public static void createShortCut(Context context, String title, int logo,
            Class<? extends Activity> activity) {
        //创建一个添加快捷方式的Intent
        Intent addSC = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        //快捷键的图标
        Parcelable icon = Intent.ShortcutIconResource.fromContext(context, logo);
        //创建单击快捷键启动本程序的Intent
        Intent launcherIntent = new Intent(context, activity);
        //设置快捷键的标题
        addSC.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
        //设置快捷键的图标
        addSC.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        //设置单击此快捷键启动的程序
        addSC.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launcherIntent);
        //向系统发送添加快捷键的广播
        context.sendBroadcast(addSC);
    }

    /**
     * 得到由设备生成的唯一ID
     * <br/> Dependency : android.permission.READ_PHONE_STATE
     *
     * @see <a href='http://stackoverflow.com/questions/2785485/is-there-a-unique-android-device-id/2853253#2853253'>stackoverflow</a>
     */
    @SuppressLint({ "HardwareIds" })
    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    public static String getDeviceId(Context context) {
        final TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return UUID.randomUUID().toString();
        }

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(),
                ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        return deviceUuid.toString();
    }

    /**
     * 得到唯一设备ID
     *
     * @see <a href='http://stackoverflow.com/questions/2785485/is-there-a-unique-android-device-id/2853253#2853253'>stackoverflow</a>
     */
    @SuppressLint("HardwareIds")
    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    public static String getDeviceSerialId() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Build.getSerial();
        } else {
            return Build.SERIAL;
        }
    }

    @SuppressLint("HardwareIds")
    public static String getAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * app是否有Activity运行
     *
     * @see <a href='https://stackoverflow.com/questions/30619349/android-5-1-1-and-above-getrunningappprocesses-returns-my-application-packag'>stackoverflow</a>
     */
    @Deprecated
    public static boolean isForeground(Context context) {
        String packageName = context.getPackageName();
        String currentApp = "NULL";
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager usm = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
            if (appList != null && appList.size() > 0) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<>();
                for (UsageStats usageStats : appList) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (!mySortedMap.isEmpty()) {
                    currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                }
            }
        } else {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
            if (tasks == null || tasks.isEmpty()) {
                return false;
            }
            currentApp = tasks.get(0).processName;
        }

        return currentApp.equalsIgnoreCase(packageName);
    }

    /**
     * 启动到应用商店app详情界面
     */
    public static void openPlayStore(Context context) {
        openPlayStore(context, context.getPackageName());
    }

    /**
     * 打开对应app应用商店
     *
     * @param appPkg 目标App的包名
     */
    public static void openPlayStore(Context context, String appPkg) {
        if (TextUtils.isEmpty(appPkg)) {
            return;
        }

        Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + appPkg);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void disabledReceiver(Context context, String receiver) {
        settingReceiver(context, receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED);
    }

    public static void enabledReceiver(Context context, String receiver) {
        settingReceiver(context, receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED);
    }

    public static void settingReceiver(Context context, String receiver, int newState) {
        ComponentName componentName = new ComponentName(context, receiver);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(componentName, newState, PackageManager.DONT_KILL_APP);
    }

    /**
     * 打开忽略电池优化系统设置界面
     *
     * @see <a href="https://developer.android.com/reference/android/provider/Settings#ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS">android doc</a>
     */
    public static boolean showIgnoreBatteryOptimizationSetting(Context context) {
        if (context == null) {
            return false;
        }
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            return false;
        }
        return startActivity(context, new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS));
    }

    /**
     * 打开忽略电池优化对话框
     *
     * @see <a href="https://developer.android.com/reference/android/provider/Settings#ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS">android doc</a>
     */
    public static boolean showIgnoreBatteryOptimizationDialog(Context context) {
        if (context == null) {
            return false;
        }
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            return false;
        }
        Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        return startActivity(context, intent);
    }

    /**
     * 判断应用是否在忽略电池优化中
     *
     * @see <a href="https://developer.android.com/reference/android/provider/Settings#ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS">android doc</a>
     */
    public static boolean isIgnoreBatteryOptimization(Context context) {
        if (context == null) {
            return true;
        }
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            return true;
        }
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        return powerManager == null || powerManager.isIgnoringBatteryOptimizations(context.getPackageName());
    }

    public static boolean startActivity(Context context, Intent intent) {
        try {
            context.startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }

    /**
     * open map application
     *
     * @param longitude 经度
     * @param latitude  纬度
     */
    public static boolean openMap(Context context, String longitude, String latitude) {
        if (TextUtils.isEmpty(latitude) || TextUtils.isEmpty(longitude)) {
            return false;
        }
        Uri uri = Uri.parse("geo:" + latitude + "," + longitude);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        return startActivity(context, intent);
    }

    public static Intent sendEmail(String[] to, String subject, String content) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, content);
        return emailIntent;
    }

    public static boolean sendEmail(Context context, String[] to, String subject, String content) {
        return startActivity(context,
                Intent.createChooser(sendEmail(to, subject, content), context.getString(R.string.lcm_send_email)));
    }

    public static void openBrowser(Context context, String url) {
        if (openBrowser2(context, url)) {
            Toast.makeText(context, R.string.lcm_unable_open_url, Toast.LENGTH_LONG).show();
        }
    }

    public static boolean openBrowser2(Context context, String url) {
        return startActivity(context, new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    @SuppressLint("MissingPermission")
    public static void setWallpaper(Context context, File file, int which) throws IOException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try (InputStream fileInputStream = new FileInputStream(file)) {
                WallpaperManager manager = WallpaperManager.getInstance(context);
                if (manager == null) {
                    throw new IOException("WallpaperManager is null");
                }
                manager.setStream(fileInputStream, null, true, which);
            }
        } else {
            setWallpaper(context, file);
        }
    }

    @SuppressLint("MissingPermission")
    public static void setWallpaper(Context context, File file) throws IOException {
        try (InputStream fileInputStream = new FileInputStream(file)) {
            WallpaperManager.getInstance(context).setStream(fileInputStream);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void setLockScreenWallpaper(Context context, File file) throws IOException {
        setWallpaper(context, file, WallpaperManager.FLAG_LOCK);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void setHomeScreenWallpaper(Context context, File file) throws IOException {
        setWallpaper(context, file, WallpaperManager.FLAG_SYSTEM);
    }
}
