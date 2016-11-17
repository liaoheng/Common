package com.github.liaoheng.common.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * @author liaoheng
 * @version 2016-11-15 10:51
 */
public class RetrofitUtils {
    private RetrofitUtils() {
    }

    private static RetrofitUtils retrofitUtils;

    public static RetrofitUtils get() {
        if (retrofitUtils == null) {
            retrofitUtils = new RetrofitUtils();
        }
        return retrofitUtils;
    }

    private Retrofit         mRetrofit;
    private Retrofit.Builder mRetrofitBuilder;

    public void init(String baseUrl, OkHttpClient client) {
        mRetrofitBuilder = new Retrofit.Builder().baseUrl(baseUrl).client(client)
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        mRetrofit = mRetrofitBuilder.build();
    }

    public Retrofit.Builder getRetrofitBuilder() {
        return mRetrofitBuilder;
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

}
