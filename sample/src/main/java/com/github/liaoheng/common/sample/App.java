package com.github.liaoheng.common.sample;

import android.app.Application;
import com.github.liaoheng.common.Common;
import com.github.liaoheng.common.network.OkHttp3Utils;
import com.github.liaoheng.common.plus.CommonPlus;
import com.github.liaoheng.common.util.L;
import java.util.Map;
import okhttp3.Request;

/**
 * @author liaoheng
 * @version 2016-06-24 16:55
 */
public class App extends Application {
    @Override public void onCreate() {
        super.onCreate();
        //Common.init(this, "CommonSample", BuildConfig.DEBUG);
        //OkHttp3Utils.init().setDefaultCache().addHeaderPlus(new OkHttp3Utils.HeaderPlusListener() {
        //    @Override public Request.Builder headerInterceptor(Request originalRequest,
        //                                                       Request.Builder builder,
        //                                                       Map<String, Object> parameters) {
        //        L.Log.d("zzzz","header");
        //        return builder;
        //    }
        //}).initialization();

        CommonPlus.init(this, "CommonSample", BuildConfig.DEBUG);
    }
}
