package com.github.liaoheng.common.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.liaoheng.common.util.Callback2;
import com.github.liaoheng.common.util.HandlerUtils;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Glide Utils
 *
 * @author liaoheng
 * @version 2016-06-24 13:45
 */
public class GlideUtils {
    @DrawableRes
    private int mDefaultError;
    @DrawableRes
    private int mDefaultLoading;

    public int getDefaultError() {
        return mDefaultError;
    }

    public GlideUtils setDefaultError(int mDefaultError) {
        this.mDefaultError = mDefaultError;
        return this;
    }

    public int getDefaultLoading() {
        return mDefaultLoading;
    }

    public GlideUtils setDefaultLoading(int mDefaultLoading) {
        this.mDefaultLoading = mDefaultLoading;
        return this;
    }

    private GlideUtils() {
    }

    private static GlideUtils glideUtils;

    public static GlideUtils get() {
        if (glideUtils == null) {
            glideUtils = new GlideUtils();
        }
        return glideUtils;
    }

    public RequestOptions loadPlaceError() {
        return new RequestOptions().error(mDefaultError).placeholder(mDefaultLoading);
    }

    public RequestOptions loadError() {
        return new RequestOptions().error(mDefaultError);
    }

    public RequestOptions loadNoCache() {
        return new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true);
    }

    public void bitmapImageViewTarget(RequestManager request, ImageView imageView,
            final Callback2<Bitmap> listener) {
        request.asBitmap().into(new BitmapImageViewTarget(imageView) {
            @Override
            public void onStart() {
                listener.onPreExecute();
            }

            @Override
            public void onStop() {
                listener.onPostExecute();
            }

            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                super.onResourceReady(resource, transition);
                listener.onSuccess(null);
            }
        });
    }

    public void clearCache(Context context, final Callback2<Object> callback) {
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onStart() {
                HandlerUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onPreExecute();
                    }
                });
            }

            @Override
            public void onCompleted() {
                callback.onFinish();
            }

            @Override
            public void onError(Throwable e) {
                callback.onPostExecute();
                callback.onError(e);
            }

            @Override
            public void onNext(String o) {
                callback.onPostExecute();
                callback.onSuccess(o);
            }
        };
        Observable.just(context).observeOn(Schedulers.io()).map(new Func1<Context, Context>() {
            @Override
            public Context call(Context context) {
                Glide.get(context).clearDiskCache();
                return context;
            }
        }).observeOn(AndroidSchedulers.mainThread()).map(new Func1<Context, String>() {
            @Override
            public String call(Context context) {
                Glide.get(context).clearMemory();
                return "ok";
            }
        }).subscribe(subscriber);
    }

}
