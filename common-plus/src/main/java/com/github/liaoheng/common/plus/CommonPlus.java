package com.github.liaoheng.common.plus;

import android.content.Context;

import com.github.liaoheng.common.Common;
import com.github.liaoheng.common.plus.util.OkHttpUtils;
import com.github.liaoheng.common.plus.util.PicassoUtils;
import com.github.liaoheng.common.util.PreferencesUtils;
import com.github.liaoheng.common.util.SystemException;

/**
 * @author liaoheng
 * @version 2015-11-4 10:47:27
 */
public class CommonPlus {

  public static void init(Context context, String projectName, boolean isDebug)
      throws SystemException {
    Common.init(context, projectName, isDebug);
    OkHttpUtils.init().setDefaultCache().initialization();
    PicassoUtils.init().setDefaultDownloader().setDebug(isDebug).initialization(context);
    PreferencesUtils.init(context);
  }
}
