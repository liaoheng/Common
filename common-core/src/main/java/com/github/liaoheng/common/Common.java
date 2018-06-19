package com.github.liaoheng.common;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.github.liaoheng.common.util.L;
import com.github.liaoheng.common.util.PreferencesUtils;

import net.danlew.android.joda.JodaTimeAndroid;

/**
 * @author liaoheng
 * @version 2015-07-22 01:03
 */
public class Common {

    private static String PROJECT_NAME;
    private static boolean DEBUG;

    public static void init(@NonNull Context context, String projectName, boolean isDebug) {
        if (TextUtils.isEmpty(projectName)) {
            PROJECT_NAME = context.getPackageName();
        } else {
            PROJECT_NAME = projectName;
        }
        DEBUG = isDebug;

        L.init(PROJECT_NAME, isDebug);
        try {
            Class.forName("net.danlew.android.joda.JodaTimeAndroid");
            JodaTimeAndroid.init(context);
        } catch (ClassNotFoundException ignored) {
        }
        PreferencesUtils.init(context);
    }

    public static String getProjectName() {
        if (TextUtils.isEmpty(PROJECT_NAME)) {
            throw new IllegalStateException("Not init");
        }
        return PROJECT_NAME;
    }

    public static boolean isDebug() {
        return DEBUG;
    }
}
