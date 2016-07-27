package com.github.liaoheng.common.util;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;
import java.io.File;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import rx.Subscription;

/**
 * @author liaoheng
 * @author <a href="http://jodd.org" target="_blank">jodd</a>
 * @version 2015-11-25 23:33
 */
public class Utils {
    public static final String ANDROID_RESOURCE = "android.resource://";

    /**
     *  Resource to Uri
     * @param context
     * @param resourceId
     * @return {@link Uri}
     */
    public static String resourceIdToUri(Context context, int resourceId) {
        return ANDROID_RESOURCE + context.getPackageName() + File.separator + resourceId;
    }

    /**
     * 是否为网络地址
     * @param url
     * @return
     */
    public static boolean isHtmlUrl(String url) {
        return isHtmlUrl(Uri.parse(url));
    }

    /**
     * 是否为网络地址
     * @param uri
     * @return
     */
    public static boolean isHtmlUrl(Uri uri) {
        return !TextUtils.isEmpty(uri.getScheme());
    }

    /**
     * URL是否为指定网站
     * @param baseAuthority
     * @param url
     * @return
     */
    public static boolean isCurAuthority(String baseAuthority, String url) {
        Uri uri = Uri.parse(url);
        if (!isHtmlUrl(uri)) {
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
     * @param url
     * @param key
     * @param value
     * @return
     */
    public static String appendUrlParameter(String url, String key, String value) {
        if (!isHtmlUrl(url)) {
            return url;
        }
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
     *双击退出activity
     * @param activity
     */
    public static void doubleExitActivity(final Activity activity) {
        doubleOperation(activity, 2000, "再按一次退出程序！", new Callback.EmptyCallback<Void>() {
            @Override public void onSuccess(Void o) {
                activity.finish();
            }
        });
    }

    /**
     * 双击事件触发
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

    public static void unsubscribe(Subscription subscription) {
        if (subscription == null) {
            return;
        }
        if (subscription.isUnsubscribed()) {
            return;
        }
        subscription.unsubscribe();
    }

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
     * setDestinationInExternalPublicDir
     * @param context
     * @param title
     * @param url
     * @param dir
     * @param fileName
     * @return
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
     * setDestinationInExternalFilesDir
     * @param context
     * @param title
     * @param url
     * @param dir
     * @param fileName
     * @return
     */
    public static long systemDownloadFilesDir(Context context, String title, String url,
                                                  String dir, String fileName) {
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

    /**
     * Returns string representation of an object, while checking for <code>null</code>.
     */
    public static String toString(Object value) {
        if (value == null) {
            return null;
        }
        return value.toString();
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
     s	 */
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
