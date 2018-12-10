package com.github.liaoheng.common.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.liaoheng.common.util.Callback;
import com.github.liaoheng.common.util.Utils;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

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
            final Callback<Bitmap> listener) {
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

    public Observable<String> clearCache(Context context) {
        return Observable.just(context)
                .observeOn(Schedulers.io())
                .map(new Function<Context, Context>() {
                    @Override
                    public Context apply(Context context) {
                        Glide.get(context).clearDiskCache();
                        return context;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Context, String>() {
                    @Override
                    public String apply(Context context) {
                        Glide.get(context).clearMemory();
                        return "ok";
                    }
                });
    }

    public Disposable clearCache(Context context, Callback<String> callback) {
        return Utils.addSubscribe(clearCache(context), callback);
    }

}
