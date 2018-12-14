package com.github.liaoheng.common.util;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import com.github.liaoheng.common.R;

import java.io.File;

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
 * @version 2015-11-25 23:33
 */
public class Utils {
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
}
