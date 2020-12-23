package com.github.liaoheng.common;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.github.liaoheng.common.util.L;

import net.danlew.android.joda.JodaTimeAndroid;

/**
 * @author liaoheng
 * @version 2015-07-22 01:03
 */
public class Common {

    private static String PROJECT_NAME;
    private static String PACKAGE_NAME;
    private static boolean sDebug;
    public static int sAppVersion = 1;

    public static void baseInit(@NonNull Context context, String projectName, boolean isDebug) {
        baseInit(context, projectName, null, isDebug);
    }

    public static void baseInit(@NonNull Context context, String projectName, String packageName, boolean isDebug) {
        PACKAGE_NAME = TextUtils.isEmpty(packageName) ? context.getPackageName() : packageName;
        PROJECT_NAME = TextUtils.isEmpty(projectName) ? PACKAGE_NAME : projectName;
        sDebug = isDebug;
        try {
            Class.forName("net.danlew.android.joda.JodaTimeAndroid");
            JodaTimeAndroid.init(context);
        } catch (ClassNotFoundException ignored) {
        }
    }

    public static void init(@NonNull Context context, String projectName, boolean isDebug) {
        baseInit(context, projectName, isDebug);
        L.init(PROJECT_NAME, isDebug);
    }

    public static String getProjectName() {
        if (TextUtils.isEmpty(PROJECT_NAME)) {
            throw new IllegalStateException("Not init");
        }
        return PROJECT_NAME;
    }

    public static String getPackageName() {
        return PACKAGE_NAME;
    }

    public static boolean isDebug() {
        return sDebug;
    }
}
