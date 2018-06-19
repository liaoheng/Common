package com.github.liaoheng.common.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresPermission;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.github.liaoheng.common.R;

import java.io.File;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subscribers.ResourceSubscriber;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * 未分类工具
 * * <br/> Dependency : rxjava ,rxandroid
 *
 * @author liaoheng
 * @author <a href="http://jodd.org" target="_blank">jodd</a>
 * @version 2015-11-25 23:33
 */
public class Utils {
    public static final String ANDROID_RESOURCE = "android.resource://";

    /**
     * Resource to Uri
     *
     * @return {@link Uri}
     */
    public static String resourceIdToUri(Context context, int resourceId) {
        return ANDROID_RESOURCE + context.getPackageName() + File.separator + resourceId;
    }

    /**
     * URL是否为指定网站
     */
    public static boolean isCurAuthority(String baseAuthority, String url) {
        Uri uri = Uri.parse(url);
        if (!ValidateUtils.isWebUrl(uri)) {
            return false;
        }
        String authority = uri.getAuthority();
        if (TextUtils.isEmpty(authority)) {
            return false;
        }
        if (!baseAuthority.contentEquals(authority)) {
            return false;
        }
        return true;
    }

    /**
     * URL链接添加参数
     */
    public static String appendUrlParameter(String url, String key, String value) {
        //if (!ValidateUtils.isWebUrl(url)) {
        //    return url;
        //}
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
     * @param context {@link Context}
     * @param interval 间隔时间 ms
     * @param msg 提示
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


    //==============================================================================================
    //  rxjava1
    //==============================================================================================
    /**
     * 退订RxJava
     *
     * @param subscription {@link Subscription#unsubscribe()}
     */
    public static void unsubscribe(Subscription subscription) {
        if (subscription == null) {
            return;
        }
        if (subscription.isUnsubscribed()) {
            return;
        }
        subscription.unsubscribe();
    }

    public static <T> Subscriber<T> getSubscribe(final Callback<T> listener) {
        return new Subscriber<T>() {
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

    public static <T> Subscriber<T> getSubscribe2(final Callback2<T> listener) {
        return new Subscriber<T>() {
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

    public static <T> Subscription addSubscribe(Observable<T> observable,
            final Callback<T> listener) {
        return observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSubscribe(listener));
    }

    public static <T> Subscription addSubscribe2(Observable<T> observable,
            final Callback2<T> listener) {
        return observable.observeOn(AndroidSchedulers.mainThread())
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

    public static <T> ResourceSubscriber<T> getFlowableSubscriber(final Callback<T> listener) {
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

    public static <T> ResourceSubscriber<T> getFlowableSubscriber2(final Callback2<T> listener) {
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

    public static <T> ResourceSubscriber addFlowableSubscriber(Flowable<T> observable,
            final Callback<T> listener) {
        ResourceSubscriber<T> flowableSubscriber = getFlowableSubscriber(listener);

        observable.observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(flowableSubscriber);
        return flowableSubscriber;
    }

    public static <T> ResourceSubscriber addFlowableSubscriber2(Flowable<T> observable,
            final Callback2<T> listener) {
        ResourceSubscriber<T> flowableSubscriber = getFlowableSubscriber2(listener);

        observable.observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(flowableSubscriber);
        return flowableSubscriber;
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
     * 得到由设备生成的唯一ID
     * <br/> Dependency : android.permission.READ_PHONE_STATE
     *
     * @see <a href='http://stackoverflow.com/questions/2785485/is-there-a-unique-android-device-id/2853253#2853253'>stackoverflow</a>
     */
    @SuppressLint({ "HardwareIds" })
    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    public static String getDeviceId(Context context) {
        final TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return UUID.randomUUID().toString();
        }

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(),
                ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        return deviceUuid.toString();
    }

    /**
     * 得到唯一设备ID
     *
     * @see <a href='http://stackoverflow.com/questions/2785485/is-there-a-unique-android-device-id/2853253#2853253'>stackoverflow</a>
     */
    @SuppressLint("HardwareIds")
    public static String getDeviceSerialId() {
        return Build.SERIAL;
    }

    /**
     * 从HTTP Response contentDisposition 中得到文件名
     *
     * @param contentDisposition <a href="https://www.w3.org/Protocols/rfc2616/rfc2616-sec19.html">rfc2616</a>
     * @param def 默认值
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
     * @see {@link  DownloadManager.Request#setDestinationInExternalPublicDir}
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
     * @see {@link  DownloadManager.Request#setDestinationInExternalFilesDir}
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

    /**
     * Safely compares two objects just like <code>equals()</code> would, except
     * it allows any of the 2 objects to be <code>null</code>.
     *
     * @return <code>true</code> if arguments are equal, otherwise <code>false</code>
     */
    public static boolean equals(Object obj1, Object obj2) {
        return (obj1 != null) ? (obj1.equals(obj2)) : (obj2 == null);
    }

    // ---------------------------------------------------------------- misc

    /**
     * Returns length of the object. Returns <code>0</code> for <code>null</code>.
     * Returns <code>-1</code> for objects without a length.
     */
    public static int length(Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length();
        }
        if (obj instanceof Collection) {
            return ((Collection) obj).size();
        }
        if (obj instanceof Map) {
            return ((Map) obj).size();
        }

        int count;
        if (obj instanceof Iterator) {
            Iterator iter = (Iterator) obj;
            count = 0;
            while (iter.hasNext()) {
                count++;
                iter.next();
            }
            return count;
        }
        if (obj instanceof Enumeration) {
            Enumeration enumeration = (Enumeration) obj;
            count = 0;
            while (enumeration.hasMoreElements()) {
                count++;
                enumeration.nextElement();
            }
            return count;
        }
        if (obj.getClass().isArray() == true) {
            return Array.getLength(obj);
        }
        return -1;
    }

    /**
     * Returns <code>true</code> if first argument contains provided element.
     * It works for strings, collections, maps and arrays.
     * s
     */
    public static boolean containsElement(Object obj, Object element) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof String) {
            if (element == null) {
                return false;
            }
            return ((String) obj).contains(element.toString());
        }
        if (obj instanceof Collection) {
            return ((Collection) obj).contains(element);
        }
        if (obj instanceof Map) {
            return ((Map) obj).values().contains(element);
        }

        if (obj instanceof Iterator) {
            Iterator iter = (Iterator) obj;
            while (iter.hasNext()) {
                Object o = iter.next();
                if (equals(o, element)) {
                    return true;
                }
            }
            return false;
        }
        if (obj instanceof Enumeration) {
            Enumeration enumeration = (Enumeration) obj;
            while (enumeration.hasMoreElements()) {
                Object o = enumeration.nextElement();
                if (equals(o, element)) {
                    return true;
                }
            }
            return false;
        }
        if (obj.getClass().isArray() == true) {
            int len = Array.getLength(obj);
            for (int i = 0; i < len; i++) {
                Object o = Array.get(obj, i);
                if (equals(o, element)) {
                    return true;
                }
            }
        }
        return false;
    }
}
