package com.github.liaoheng.common.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.widget.ImageView;
import com.github.liaoheng.common.util.Callback4;
import com.github.liaoheng.common.util.FileUtils;
import com.github.liaoheng.common.util.SystemException;
import com.github.liaoheng.common.util.ValidateUtils;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;
import java.io.File;

/**
 * 图片工具
 * @author liaoheng
 * @version 2015-04-18 01:28
 */
public class PicassoUtils {
    private final String TAG = PicassoUtils.class.getSimpleName();
    private             Picasso mPicasso;
    private             File    mCache;
    @DrawableRes private int     mDefaultError;
    @DrawableRes private int     mDefaultLoading;

    private PicassoUtils(int defaultLoading, int defaultError, Picasso mPicasso, File cache) {
        this.mDefaultLoading = defaultLoading;
        this.mDefaultError = defaultError;
        this.mPicasso = mPicasso;
        this.mCache = cache;
    }

    private static PicassoUtils INSTANCE;

    public static PicassoUtils get() {
        if (INSTANCE == null) {
            throw new IllegalStateException("not init");
        }
        return INSTANCE;
    }

    public static PicassoUtils setInit(PicassoUtils picassoUtils) {
        return INSTANCE = picassoUtils;
    }

    public static class Init {
        private Picasso    picasso;
        private Downloader downloader;
        private File       cache;
        private boolean debug = true;
        @DrawableRes int defaultError;
        @DrawableRes int defaultLoading;

        public Init setDefaultDownloader() {
            return setDefaultDownloader(CommonNet.IMAGE_DISK_CACHE_SIZE);
        }

        public Init setDefaultDownloader(long imageDiskCacheSize) {
            try {
                if (cache == null) {
                    cache = getDefaultCacheFile();
                }
                downloader = getDefaultDownloader(cache, imageDiskCacheSize);
            } catch (SystemException ignored) {
            }
            return this;
        }

        public File getDefaultCacheFile() throws SystemException {
            return FileUtils.createCacheSDAndroidDirectory(CommonNet.DISK_CACHE_DIR);
        }

        /**
         * default use okhttp3
         */
        public Downloader getDefaultDownloader(File cache, long imageDiskCacheSize)
                throws SystemException {
            if (imageDiskCacheSize == 0) {
                imageDiskCacheSize = CommonNet.IMAGE_DISK_CACHE_SIZE;
            }
            try {
                Class.forName("com.jakewharton.picasso.OkHttp3Downloader");
            } catch (ClassNotFoundException ignored) {
                return new OkHttpDownloader(cache, imageDiskCacheSize);
            }
            return new OkHttp3Downloader(cache, imageDiskCacheSize);
        }

        public void initialization(Context context) {
            PicassoUtils.setInit(build(context));
        }

        public PicassoUtils build(Context context) {
            if (picasso == null) {
                Picasso.Builder builder;
                if (downloader != null) {
                    builder = new Picasso.Builder(context).downloader(downloader);
                } else {
                    builder = new Picasso.Builder(context);
                }
                if (debug) {
                    builder.loggingEnabled(true);//打开日志，即log中会打印出目前下载的进度、情况
                    builder.indicatorsEnabled(
                            true);//开启调模式，它能够在图片左上角显示小三角形，这个小三角形的颜色标明了图片的来源：网络、内存缓存、磁盘缓存
                }
                picasso = builder.build();
            }
            if (defaultError == 0) {
                defaultError = R.drawable.lcn_empty_photo;
            }
            if (defaultLoading == 0) {
                defaultLoading = R.drawable.lcn_empty_photo;
            }
            return new PicassoUtils(defaultLoading, defaultError, picasso, cache);
        }

        public Picasso getPicasso() {
            return picasso;
        }

        public Init setPicasso(Picasso picasso) {
            this.picasso = picasso;
            return this;
        }

        public Downloader getDownloader() {
            return downloader;
        }

        public Init setDownloader(Downloader downloader) {
            this.downloader = downloader;
            return this;
        }

        @DrawableRes public int getDefaultError() {
            return defaultError;
        }

        public Init setDefaultError(@DrawableRes int defaultError) {
            this.defaultError = defaultError;
            return this;
        }

        @DrawableRes public int getDefaultLoading() {
            return defaultLoading;
        }

        public Init setDefaultLoading(@DrawableRes int defaultLoading) {
            this.defaultLoading = defaultLoading;
            return this;
        }

        public File getCache() {
            return cache;
        }

        public Init setCache(File cache) {
            this.cache = cache;
            return this;
        }

        public boolean isDebug() {
            return debug;
        }

        public Init setDebug(boolean debug) {
            this.debug = debug;
            return this;
        }
    }

    public static Init init() {
        return new Init();
    }

    public void displayImageUri(Uri uri, ImageView imageView) {
        if (imageError(uri, imageView)) {
            return;
        }
        placeImage(getPicasso().load(uri)).into(imageView);
    }

    public void displayImage(File file, ImageView imageView) {
        displayImage(file, imageView, null);
    }

    public void displayImage(String url, ImageView imageView) {
        displayImage(url, imageView, null);
    }

    public void displayImage(String url, ImageView imageView,
                             com.squareup.picasso.Callback listener) {
        if (imageError(url, imageView)) {
            return;
        }
        placeImage(url).into(imageView, listener);
    }

    public void displayImage(File file, ImageView imageView,
                             com.squareup.picasso.Callback listener) {
        if (imageError(file, imageView)) {
            return;
        }
        placeImage(file).into(imageView, listener);
    }

    public void displayImage(File file, ImageView imageView, int targetWidth, int targetHeight) {
        displayImage(file, imageView, targetWidth, targetHeight, null);
    }

    public void displayImage(String url, ImageView imageView, int targetWidth, int targetHeight) {
        displayImage(url, imageView, targetWidth, targetHeight, null);
    }

    public void displayImage(String url, ImageView imageView, int targetWidth, int targetHeight,
                             com.squareup.picasso.Callback listener) {
        if (imageError(url, imageView)) {
            return;
        }
        placeImage(url).resize(targetWidth, targetHeight).into(imageView, listener);
    }

    public void displayImage(File file, ImageView imageView, int targetWidth, int targetHeight,
                             com.squareup.picasso.Callback listener) {
        if (imageError(file, imageView)) {
            return;
        }
        placeImage(file).resize(targetWidth, targetHeight).into(imageView, listener);
    }

    public Target getDisplayImage(final ImageView imageView,
                                  final Callback4.EmptyCallback<Bitmap> listener) {
        Target target = new Target() {
            //当图片加载时调用
            @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                imageView.setImageBitmap(bitmap);
                listener.onPostExecute();
                listener.onYes(bitmap);
                listener.onFinish(null);
            }

            //当图片加载失败时调用
            @Override public void onBitmapFailed(Drawable errorDrawable) {
                listener.onPostExecute();
                listener.onNo(null);
                listener.onFinish(null);
            }

            //当任务被提交时调用
            @Override public void onPrepareLoad(Drawable placeHolderDrawable) {
                listener.onPreExecute();
            }
        };
        imageView.setTag(target);
        return target;
    }

    public RequestCreator getLoad(String url) {
        return getPicasso().load(url);
    }

    public RequestCreator getLoad(File file) {
        return getPicasso().load(file);
    }

    public RequestCreator noCacheImage(File file) {
        return noCacheImage(getLoad(file));
    }

    public RequestCreator noCacheImage(String url) {
        return noCacheImage(getLoad(url));
    }

    public RequestCreator noCacheImage(RequestCreator creator) {
        return creator
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE);//不要从缓存中取图片,不要把加载的图片放入缓存
    }

    public RequestCreator noCachePlaceImage(File file) {
        return noCachePlaceImage(getLoad(file));
    }

    public RequestCreator noCachePlaceImage(String url) {
        return noCachePlaceImage(getLoad(url));
    }

    public RequestCreator noCachePlaceImage(RequestCreator creator) {
        return placeImage(noCacheImage(creator));
    }

    public RequestCreator placeImage(String url) {
        return placeImage(getLoad(url));
    }

    public RequestCreator placeImage(File file) {
        return placeImage(getLoad(file));
    }

    public RequestCreator placeImage(RequestCreator creator) {
        return placeImage(creator, mDefaultLoading, mDefaultError);
    }

    public RequestCreator placeImage(RequestCreator creator, @DrawableRes int loading,
                                     @DrawableRes int error) {
        if (loading != 0) {//当图片正在加载时显示的图片(optional)
            creator.placeholder(loading);
        }
        if (error != 0) {//当图片加载失败时显示的图片(optional)
            creator.error(error);
        }
        return creator;
    }

    public RequestCreator placeImage(RequestCreator creator, @DrawableRes int error) {
        return placeImage(creator, 0, error);
    }

    public boolean imageError(String url, ImageView imageView) {
        if (TextUtils.isEmpty(url)) {
            imageView.setImageResource(mDefaultError);
            return true;
        }
        return false;
    }

    public boolean imageError(File file, ImageView imageView) {
        if (!FileUtils.existsBoolean(file)) {
            imageView.setImageResource(mDefaultError);
            return true;
        }
        return false;
    }

    public boolean imageError(Uri uri, ImageView imageView) {
        if (uri == null) {
            imageView.setImageResource(mDefaultError);
            return true;
        }
        if (ValidateUtils.isWebUrl(uri)) {
            if (TextUtils.isEmpty(uri.getPath())) {
                imageView.setImageResource(mDefaultError);
                return true;
            }
        } else {
            if (!FileUtils.existsBoolean(new File(uri.getPath()))) {
                imageView.setImageResource(mDefaultError);
                return true;
            }
        }
        return false;
    }

    public Uri StringToUri(String url) {
        if (ValidateUtils.isWebUrl(url)) {
            return Uri.parse(url);
        } else {
            return Uri.fromFile(new File(url));
        }
    }

    public void clearCache() {
        if (mCache == null) {
            return;
        }
        FileUtils.cleanPath(mCache);
    }

    public void setDefaultImage(@DrawableRes int default_error, @DrawableRes int default_loading) {
        this.mDefaultError = default_error;
        this.mDefaultLoading = default_loading;
    }

    public void setDefaultErrorImage(@DrawableRes int error) {
        this.mDefaultError = error;
    }

    public void setDefaultLoadingImage(@DrawableRes int loading) {
        this.mDefaultLoading = loading;
    }

    public Picasso getPicasso() {
        if (mPicasso == null) {
            throw new IllegalStateException("not initialization Picasso");
        }
        return mPicasso;
    }
}
