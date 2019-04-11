package com.github.liaoheng.common.util;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Surface;
import android.widget.Toast;
import com.github.liaoheng.common.R;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.ResourceObserver;
import io.reactivex.subscribers.ResourceSubscriber;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * 工具集
 * * <br/> Dependency : rxjava ,rxandroid
 *
 * @author liaoheng
 * @version 2015-11-25 23:33
 */
public class Utils {
    /**
     * URL是否为指定网站
     */
    public static boolean isCurAuthority(String baseAuthority, String url) {
        String authority = Uri.parse(url).getAuthority();
        if (TextUtils.isEmpty(authority)) {
            return false;
        }
        return baseAuthority.contentEquals(authority);
    }

    /**
     * URL链接添加参数
     */
    public static String appendUrlParameter(String url, String key, String value) {
        Uri uri = Uri.parse(url);
        if (uri.getBooleanQueryParameter(key, false)) {//有KEY值不修改
            return url;
        }
        return new Uri.Builder().scheme(uri.getScheme()).encodedAuthority(uri.getEncodedAuthority())
                .encodedPath(uri.getEncodedPath()).encodedQuery(uri.getEncodedQuery())
                .appendQueryParameter(key, value).encodedFragment(uri.getEncodedFragment()).build()
                .toString();
    }

    private static long exitTime;

    /**
     * 双击退出activity
     */
    public static void doubleExitActivity(final Activity activity) {
        doubleOperation(activity, 2000, activity.getString(R.string.lcm_press_again_to_exit),
                new Callback.EmptyCallback<Void>() {
                    @Override
                    public void onSuccess(Void o) {
                        activity.finish();
                    }
                });
    }

    /**
     * 双击事件触发
     *
     * @param context  {@link Context}
     * @param interval 间隔时间 ms
     * @param msg      提示
     * @param callback 回调{@link Callback#onSuccess(Object)}
     */
    public static void doubleOperation(Context context, int interval, String msg,
            Callback<Void> callback) {
        if ((System.currentTimeMillis() - exitTime) > interval) {
            exitTime = System.currentTimeMillis();
            UIUtils.showToast(context, msg, Toast.LENGTH_SHORT);
        } else {
            callback.onSuccess(null);
        }
    }

    /**
     * 去除电话号码中的 "-" 和" "
     */
    public static String phoneStringToNumberString(String phoneString) {
        if (TextUtils.isEmpty(phoneString)) {
            return phoneString;
        }
        return phoneString.replaceAll("-", "").replaceAll(" ", "");
    }

    /**
     * 从HTTP Response contentDisposition 中得到文件名
     *
     * @param contentDisposition <a href="https://www.w3.org/Protocols/rfc2616/rfc2616-sec19.html">rfc2616</a>
     * @param def                默认值
     */
    public static String getContentDispositionFileName(String contentDisposition, String def) {
        if (TextUtils.isEmpty(contentDisposition)) {
            return def;
        }
        String[] f = TextUtils.split(contentDisposition, "=");
        String fileName;
        if (f.length < 2) {
            return def;
        } else {
            fileName = f[1];
            return fileName.replaceAll("\"", "");
        }
    }

    /**
     * download file to sd /
     *
     * @see DownloadManager.Request#setDestinationInExternalPublicDir
     */
    public static long systemDownloadPublicDir(Context context, String title, String url,
            String dir, String fileName) {
        Uri downUrl = Uri.parse(url);
        DownloadManager downloadManager = (DownloadManager) context
                .getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = getSystemDownloadRequest(title, downUrl,
                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(dir, fileName);
        return downloadManager.enqueue(request);
    }

    /**
     * download file to sd /Android/data/:package/files/
     *
     * @see DownloadManager.Request#setDestinationInExternalFilesDir
     */
    public static long systemDownloadFilesDir(Context context, String title, String url, String dir,
            String fileName) {
        Uri downUrl = Uri.parse(url);
        DownloadManager downloadManager = (DownloadManager) context
                .getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = getSystemDownloadRequest(title, downUrl,
                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalFilesDir(context, dir, fileName);
        return downloadManager.enqueue(request);
    }

    public static DownloadManager.Request getSystemDownloadRequest(String title, Uri downloadUrl,
            int notificationVisibility) {
        DownloadManager.Request request = new DownloadManager.Request(downloadUrl);
        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        // 设置标题
        request.setTitle(title);
        // 显示下载进度的提示
        request.setNotificationVisibility(notificationVisibility);
        request.setVisibleInDownloadsUi(true);
        return request;
    }

    //https://stackoverflow.com/questions/3366925/deep-copy-duplicate-of-javas-bytebuffer
    public static ByteBuffer cloneByteBuffer(ByteBuffer original) {
        ByteBuffer clone = ByteBuffer.allocate(original.capacity());
        original.rewind();//copy from the beginning
        clone.put(original);
        original.rewind();
        clone.flip();
        return clone;
    }

    public static String toNotNullString(String s) {
        return TextUtils.isEmpty(s) ? "" : s;
    }

    public static String toNotNullString(Object o) {
        return o == null ? "" : String.valueOf(o);
    }

    public static String toNullString(String s) {
        return TextUtils.isEmpty(s) ? null : s;
    }

    public static Long toNullLong(String s) {
        return TextUtils.isEmpty(s) ? null : Long.parseLong(s);
    }

    public static Integer toNullInteger(String s) {
        return TextUtils.isEmpty(s) ? null : Integer.parseInt(s);
    }

    public static List<Long> longArray2List(long[] array) {
        List<Long> longs = new ArrayList<>();
        if (array == null || array.length <= 0) {
            return longs;
        }
        for (long a : array) {
            longs.add(a);
        }
        return longs;
    }

    public static List<Integer> intArray2List(int[] array) {
        List<Integer> longs = new ArrayList<>();
        if (array == null || array.length <= 0) {
            return longs;
        }
        for (int a : array) {
            longs.add(a);
        }
        return longs;
    }

    public static void clearSurface(SurfaceTexture texture) {
        Surface surface = new Surface(texture);
        if (surface.isValid()) {
            Canvas canvas = surface.lockCanvas(null);
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            surface.unlockCanvasAndPost(canvas);
        }
        surface.release();
    }

    /**
     * 检查是否拥有指定的所有权限
     */
    public static boolean checkPermissionAllGranted(Context context, String[] permissions) {
        for (String permission : permissions) {
            if (!checkPermissionGranted(context, permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查权限
     */
    public static boolean checkPermissionGranted(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    //==============================================================================================
    //  rxjava1
    //==============================================================================================

    /**
     * 退订RxJava
     *
     * @param subscription {@link rx.Subscription#unsubscribe()}
     */
    public static void unsubscribe(rx.Subscription subscription) {
        if (subscription == null) {
            return;
        }
        if (subscription.isUnsubscribed()) {
            return;
        }
        subscription.unsubscribe();
    }

    public static <T> rx.Subscriber<T> getSubscribe(final Callback<T> listener) {
        return new rx.Subscriber<T>() {
            @Override
            public void onStart() {
                super.onStart();
                if (listener != null) {
                    HandlerUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listener.onPreExecute();
                        }
                    });
                }
            }

            @Override
            public void onCompleted() {
                if (listener != null) {
                    listener.onFinish();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (listener != null) {
                    listener.onPostExecute();
                    listener.onError(new SystemException(e));
                }
            }

            @Override
            public void onNext(T t) {
                if (listener != null) {
                    listener.onPostExecute();
                    listener.onSuccess(t);
                }
            }
        };
    }

    @Deprecated
    public static <T> rx.Subscriber<T> getSubscribe2(final Callback2<T> listener) {
        return new rx.Subscriber<T>() {
            @Override
            public void onStart() {
                super.onStart();
                if (listener != null) {
                    HandlerUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listener.onPreExecute();
                        }
                    });
                }
            }

            @Override
            public void onCompleted() {
                if (listener != null) {
                    listener.onFinish();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (listener != null) {
                    listener.onPostExecute();
                    listener.onError(e);
                }
            }

            @Override
            public void onNext(T t) {
                if (listener != null) {
                    listener.onPostExecute();
                    listener.onSuccess(t);
                }
            }
        };
    }

    public static <T> rx.Subscription addSubscribe1(rx.Observable<T> observable,
            final Callback<T> listener) {
        return observable.observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(getSubscribe(listener));
    }

    @Deprecated
    public static <T> rx.Subscription addSubscribe2(rx.Observable<T> observable,
            final Callback2<T> listener) {
        return observable.observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(getSubscribe2(listener));
    }

    //==============================================================================================
    //  rxjava2
    //==============================================================================================

    /**
     * 弃置RxJava
     *
     * @param subscription {@link Disposable#dispose()}
     */
    public static void dispose(Disposable subscription) {
        if (subscription == null) {
            return;
        }
        if (subscription.isDisposed()) {
            return;
        }
        subscription.dispose();
    }

    public static <T> ResourceObserver<T> getResourceObserver(final Callback<T> listener) {
        return new ResourceObserver<T>() {
            @Override
            protected void onStart() {
                if (listener != null) {
                    HandlerUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listener.onPreExecute();
                        }
                    });
                }
                super.onStart();
            }

            @Override
            public void onComplete() {
                if (listener != null) {
                    listener.onFinish();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (listener != null) {
                    listener.onPostExecute();
                    listener.onError(new SystemException(e));
                }
            }

            @Override
            public void onNext(T t) {
                if (listener != null) {
                    listener.onPostExecute();
                    listener.onSuccess(t);
                }
            }
        };
    }

    public static <T> ResourceSubscriber<T> getResourceSubscriber(final Callback<T> listener) {
        return new ResourceSubscriber<T>() {
            @Override
            protected void onStart() {
                if (listener != null) {
                    HandlerUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listener.onPreExecute();
                        }
                    });
                }
                super.onStart();
            }

            @Override
            public void onComplete() {
                if (listener != null) {
                    listener.onFinish();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (listener != null) {
                    listener.onPostExecute();
                    listener.onError(new SystemException(e));
                }
            }

            @Override
            public void onNext(T t) {
                if (listener != null) {
                    listener.onPostExecute();
                    listener.onSuccess(t);
                }
            }
        };
    }

    public static <T> Disposable addSubscribe(Observable<T> observable,
            final Callback<T> listener) {
        ResourceObserver<T> resourceObserver = getResourceObserver(listener);
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(resourceObserver);
        return resourceObserver;
    }

    public static <T> Disposable addSubscribe(Flowable<T> observable,
            final Callback<T> listener) {
        ResourceSubscriber<T> resourceSubscriber = getResourceSubscriber(listener);
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(resourceSubscriber);
        return resourceSubscriber;
    }
}
