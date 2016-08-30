package com.github.liaoheng.common;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.github.liaoheng.common.util.FileUtils;
import com.github.liaoheng.common.util.L;
import com.github.liaoheng.common.util.SystemRuntimeException;
import java.io.File;
import net.danlew.android.joda.JodaTimeAndroid;

/**
 *
 *  @author <a href="https://github.com/litesuits/android-common">litesuits</a>
 *  @author <a href="http://www.trinea.cn" target="_blank">Trinea</a>
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
        JodaTimeAndroid.init(context);
        L.init(PROJECT_NAME, isDebug);
    }

    public static String getProjectName() {
        if (TextUtils.isEmpty(PROJECT_NAME)) {
            throw new SystemRuntimeException("not init Common");
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
