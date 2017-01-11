package com.github.liaoheng.common;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.github.liaoheng.common.util.FileUtils;
import com.github.liaoheng.common.util.L;
import com.github.liaoheng.common.util.PreferencesUtils;
import java.io.File;
import net.danlew.android.joda.JodaTimeAndroid;

/**
 * @author liaoheng
 * @version 2015-07-22 01:03
 */
public class Common {

    private static String PROJECT_NAME = BuildConfig.APPLICATION_ID;
    private static boolean DEBUG;
    private static File    mExternalCacheDir;

    public static void init(@NonNull Context context, String projectName, boolean isDebug) {
        if (TextUtils.isEmpty(projectName)) {
            PROJECT_NAME = context.getPackageName();
        } else {
            PROJECT_NAME = projectName;
        }
        DEBUG = isDebug;
        mExternalCacheDir = FileUtils.getSDExternalPath(context);
        L.init(PROJECT_NAME, isDebug);

        JodaTimeAndroid.init(context);
        PreferencesUtils.init(context);
    }

    public static String getProjectName() {
        if (TextUtils.isEmpty(PROJECT_NAME)) {
            throw new IllegalStateException("not init Common");
        }
        return PROJECT_NAME;
    }

    public static boolean isDebug() {
        return DEBUG;
    }

    public static File getSDExternalPath() {
        if (mExternalCacheDir == null) {
            throw new IllegalStateException("not init Common");
        }
        return mExternalCacheDir;
    }
}
