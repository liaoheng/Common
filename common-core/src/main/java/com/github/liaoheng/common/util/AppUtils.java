package com.github.liaoheng.common.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Parcelable;

/**
 * 其它工具
 * @author liaoheng
 */
public class AppUtils {
    private static final String TAG = AppUtils.class.getSimpleName();

    /**
     * 获取应用程序名称
     * @param mContext
     * @return
     * @throws SystemException
     * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2014-5-07
     */
    public static String getAppName(Context mContext) throws SystemException {
        try {
            PackageManager packageManager = mContext.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return mContext.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            throw new SystemException("应用程序名称获取失败!", e);
        }
    }

    /**
     * 获得系统版本信息
     *
     * @param application {@link Application}
     * @return {@link PackageInfo}
     */
    public static PackageInfo getVersionInfo(Application application) throws SystemException {
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
     * 重启应用
     * @param activity
     */
    public static void restartApplication(Activity activity) {
        Intent i = activity.getBaseContext().getPackageManager()
            .getLaunchIntentForPackage(activity.getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(i);

        System.exit(0);
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
}
