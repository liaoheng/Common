package com.github.liaoheng.common.plus;

import android.content.Context;
import android.support.annotation.NonNull;
import com.github.liaoheng.common.Common;
import com.github.liaoheng.common.network.CommonNet;

/**
 * @author liaoheng
 * @version 2015-11-4 10:47:27
 */
public class CommonPlus {
    public static void init(@NonNull Context context, String projectName, boolean isDebug) {
        Common.init(context, projectName, isDebug);
        CommonNet.init(context, isDebug);
    }

    public static void init2(@NonNull Context context, String projectName, boolean isDebug) {
        Common.init(context, projectName, isDebug);
        CommonNet.init2();
    }
}
