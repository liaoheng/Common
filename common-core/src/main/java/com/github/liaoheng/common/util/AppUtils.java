package com.github.liaoheng.common.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Parcelable;
import android.support.annotation.RequiresPermission;
import android.text.TextUtils;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * app工具
 *
 * @author liaoheng
 */
public class AppUtils {
    /**
     * 获取应用程序名称
     *
     * @throws SystemException
     * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2014-5-07
     */
    public static String getAppName(Context context) throws SystemException {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            throw new SystemException("Application name fetching failed", e);
        }
    }

    /**
     * 获得系统版本信息
     *
     * @param application {@link Context}
     * @return {@link PackageInfo}
     */
    public static PackageInfo getVersionInfo(Context application) throws SystemException {
        PackageManager mg = application.getPackageManager();
        try {
            return mg.getPackageInfo(application.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            throw new SystemException("Version information acquisition failed", e);
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
     * @param logo 快捷键的图标
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
    @Deprecated
    public static String getDeviceId(Context context) {
        return Utils.getDeviceId(context);
    }

    /**
     * 得到唯一设备ID
     *
     * @see <a href='http://stackoverflow.com/questions/2785485/is-there-a-unique-android-device-id/2853253#2853253'>stackoverflow</a>
     */
    @SuppressLint("HardwareIds")
    @Deprecated
    public static String getDeviceSerialId() {
        return Utils.getDeviceSerialId();
    }

    /**
     * //TODO 需要测试
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
            if (tasks==null || tasks.isEmpty()){
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
}
