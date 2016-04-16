package com.leng.common.plus;

import android.content.Context;

import com.leng.common.Common;
import com.leng.common.plus.util.OkHttpUtils;
import com.leng.common.plus.util.PicassoUtils;
import com.leng.common.util.PreferencesUtils;
import com.leng.common.util.SystemException;

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
