package com.github.liaoheng.common.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Parcelable;
import android.telephony.TelephonyManager;
import java.util.UUID;

/**
 * 其它工具
 * @author liaoheng
 */
public class AppUtils {
    private static final String TAG = AppUtils.class.getSimpleName();

    /**
     * 获取应用程序名称
     * @param context
     * @return
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
            throw new SystemException("应用程序名称获取失败!", e);
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
            throw new SystemException("版本信息获取失败!", e);
        }
    }

    public static String phoneStringToNumberString(String PhoneString) {
        return PhoneString.replaceAll("-", "").replaceAll(" ", "");
    }

    /**
     * 重启应用:需要启动界面与后面界面在一个task中，如果使用单例需用singleTask。
     *
     * @param activity
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
     * @param context
     * @param title  快捷键的标题
     *               @param logo  快捷键的图标
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
     *  得到唯一设备ID,需要权限:uses-permission android:name="android.permission.READ_PHONE_STATE"
     *  @see <a href='http://stackoverflow.com/questions/2785485/is-there-a-unique-android-device-id/2853253#2853253'>stackoverflow</a>
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        return deviceUuid.toString();
    }
}
