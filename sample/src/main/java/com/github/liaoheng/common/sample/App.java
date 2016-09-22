package com.github.liaoheng.common.sample;

import android.app.Application;
import com.github.liaoheng.common.plus.BuildConfig;
import com.github.liaoheng.common.plus.CommonPlus;

/**
 * @author liaoheng
 * @version 2016-06-24 16:55
 */
public class App extends Application {
    @Override public void onCreate() {
        super.onCreate();
        //Common.init(this, "CommonSample", BuildConfig.DEBUG);
        //OkHttpUtils.init().setDefaultCache().initialization();

        CommonPlus.init2(this, "CommonSample", BuildConfig.DEBUG);
    }
}
