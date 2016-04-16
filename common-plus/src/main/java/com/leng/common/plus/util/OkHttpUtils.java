package com.leng.common.plus.util;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okio.Buffer;

import org.apache.commons.io.FilenameUtils;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.leng.common.util.BitmapUtils;
import com.leng.common.util.Callback;
import com.leng.common.util.FileUtils;
import com.leng.common.util.L;
import com.leng.common.util.NetException;
import com.leng.common.util.SystemException;
import com.leng.common.util.SystemRuntimeException;
import com.leng.common.util.Utils;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Dispatcher;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.internal.Util;
import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.RxLifecycle;

/**
 * OKHttp 工具类
 *
 * @author liaoheng
 * @version 2015-07-01 14:55
 */
public class OkHttpUtils {
    private final String   TAG          = OkHttpUtils.class.getSimpleName();
    public final MediaType JSON         = MediaType.parse("application/json; charset=utf-8");
    private final Handler  mMainHandler = new Handler(Looper.getMainLooper());

    private final OkHttpClient        mClient;
    private final Map<String, String> mHeaders;
    private final List<HeaderPlus>    mHeaderPlus;

    public interface HeaderPlus {
        /**
         * http请求拦截
         * @param originalRequest 原始Request
         * @param builder 前面处理后的builder
         * @param parameters 前面向后传递的参数
         * @return {@link Request.Builder}
         */
        Request.Builder headerInterceptor(Request originalRequest, Request.Builder builder,Map<String,Object> parameters);
    }

    private OkHttpUtils(OkHttpClient client, List<HeaderPlus> headerPluses,
                        Map<String, String> headers) {
        this.mClient = client;
        this.mHeaderPlus = headerPluses;
        this.mHeaders = headers;

        mClient.networkInterceptors().add(new HeaderPlusInterceptor());
        mClient.networkInterceptors().add(new LogInterceptor(TAG));
    }

    private static OkHttpUtils INSTANCE;

    public static OkHttpUtils get() {
        if (INSTANCE == null) {
            synchronized (OkHttpUtils.class) {
                if (INSTANCE == null) {
                    INSTANCE = init().build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 单线程
     *
     * @return {@link OkHttpClient}
     */
    public static OkHttpClient getSingleOkHttpClient() {
        OkHttpClient client = OkHttpUtils.get().cloneClient();
        ExecutorService threadPoolExecutor = Executors
            .newSingleThreadExecutor(Util.threadFactory("OkHttp Dispatcher", false));
        Dispatcher dispatcher = new Dispatcher(threadPoolExecutor);
        client.setDispatcher(dispatcher);
        return client;
    }

    public static OkHttpUtils setInit(OkHttpUtils okHttpUtils) {
        return INSTANCE = okHttpUtils;
    }

    public static Init init() {
        return new Init();
    }

    public static class Init {
        private final int           DEFAULT_HTTP_DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB;
        private OkHttpClient        client;
        private Map<String, String> headers;
        private List<HeaderPlus>    headerPlus;
        private Cache               cache;

        public Init setDefaultCache() {
            return setDefaultCache(DEFAULT_HTTP_DISK_CACHE_SIZE);
        }

        public Init setDefaultCache(long httpDiskCacheSize) {
            try {
                cache = getDefaultCache(httpDiskCacheSize);
            } catch (SystemException ignored) {
            }
            return this;
        }

        public Cache getDefaultCache(long httpDiskCacheSize) throws SystemException {
            File cacheFile = FileUtils.createCacheSDAndroidDirectory("httpCache");
            if (httpDiskCacheSize == 0) {
                httpDiskCacheSize = DEFAULT_HTTP_DISK_CACHE_SIZE;
            }
            return new Cache(cacheFile, httpDiskCacheSize);
        }

        public void initialization() {
            OkHttpUtils.setInit(build());
        }

        public OkHttpUtils build() {
            if (client == null) {
                client = new OkHttpClient();
            }
            if (headerPlus == null) {
                headerPlus = new LinkedList<>();
            }
            if (headers == null) {
                headers = new LinkedHashMap<>();
            }
            if (cache != null) {
                client.setCache(cache);
            }
            return new OkHttpUtils(client, headerPlus, headers);
        }

        public OkHttpClient getClient() {
            return client;
        }

        public Init setClient(OkHttpClient client) {
            this.client = client;
            return this;
        }

        public Map<String, String> getHeaders() {
            return headers;
        }

        public Init setHeaders(Map<String, String> headers) {
            this.headers = new LinkedHashMap<>(headers);
            return this;
        }

        public Init addHeaders(String key,String value) {
            if (this.headers == null){
                this.headers = new LinkedHashMap<>();
            }
            this.headers.put(key,value);
            return this;
        }

        public List<HeaderPlus> getHeaderPlus() {
            return headerPlus;
        }

        public Init setHeaderPlus(List<HeaderPlus> headerPlus) {
            this.headerPlus = new LinkedList<>(headerPlus);
            return this;
        }

        public Init addHeaderPlus(HeaderPlus... headerPlus) {
            this.headerPlus = new LinkedList<>(Arrays.asList(headerPlus));
            return this;
        }

        public Cache getCache() {
            return cache;
        }

        public Init setCache(Cache cache) {
            this.cache = cache;
            return this;
        }
    }

    public OkHttpClient cloneClient() {
        if (getClient() == null) {
            throw new IllegalStateException("Not Initialize");
        }
        return getClient().clone();
    }

    public OkHttpClient getClient() {
        if (mClient == null) {
            throw new IllegalStateException("Not Initialize");
        }
        return mClient;
    }

    public void clearCache() {
        if (getClient().getCache() == null) {
            return;
        }
        FileUtils.cleanPath(getClient().getCache().getDirectory());
    }

    public Map<String, String> getHeaders() {
        if (mHeaders == null) {
            throw new IllegalStateException("Not Initialize");
        }
        return mHeaders;
    }

    public void addHeader(Map<String, String> headers) {
        getHeaders().putAll(headers);
    }

    public void updateHeader(String key, Map<String, String> header) {
        getHeaders().remove(key);
        addHeader(header);
    }

    public void updateHeader(String key, String value) {
        getHeaders().remove(key);
        addHeader(key,value);
    }

    public void addHeader(String key, String value) {
        getHeaders().put(key, value);
    }

    public List<HeaderPlus> getHeaderPlus() {
        if (mHeaderPlus == null) {
            throw new IllegalStateException("Not Initialize");
        }
        return mHeaderPlus;
    }

    public void addHeaderPlus(List<HeaderPlus> headerPluses) {
        getHeaderPlus().addAll(headerPluses);
    }

    public void addHeaderPlus(HeaderPlus... headerPluses) {
        getHeaderPlus().addAll(Arrays.asList(headerPluses));
    }

    public void updateHeaderPlus(int location, HeaderPlus headerPlus) {
        getHeaderPlus().remove(location);
        addHeaderPlus(headerPlus);
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    public void updateHeaderPlus(Objects location, HeaderPlus headerPlus) {
        getHeaderPlus().remove(location);
        addHeaderPlus(headerPlus);
    }

    public void addHeaderPlus(HeaderPlus headerPlus) {
        getHeaderPlus().add(headerPlus);
    }

    public void setCookie(Map<String, String> map) {
        if (map != null && !map.isEmpty()) {
            List<String> cookies = new LinkedList<>();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                cookies.add(entry.getKey() + "=" + entry.getValue());
            }
            updateHeader("Cookie", buildCookieHeader(cookies));
        } else {
            clearCookie();
        }
    }

    public void clearCookie() {
        getHeaders().remove("Cookie");
    }

    /**
     * Send all cookies in one big header, as recommended by
     * <a href="http://tools.ietf.org/html/rfc6265#section-4.2.1">RFC 6265</a>.
     */
    private String buildCookieHeader(List<String> cookies) {
        if (cookies.size() == 1)
            return cookies.get(0);
        StringBuilder sb = new StringBuilder();
        for (int i = 0, size = cookies.size(); i < size; i++) {
            if (i > 0)
                sb.append("; ");
            sb.append(cookies.get(i));
        }
        return sb.toString();
    }

    public Request.Builder newRequestBuilder() {
        return new Request.Builder();
    }

    private class HeaderPlusInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            Request request = chain.request();
            Map<String,Object> map = new HashMap<>();

            if (!getHeaderPlus().isEmpty()) {
                for (HeaderPlus mHeaderPlu : getHeaderPlus()) {
                    builder = mHeaderPlu.headerInterceptor(request, builder,map);
                }
            }
            if (!getHeaders().isEmpty()) {
                for (Map.Entry<String, String> entry : getHeaders().entrySet()) {
                    builder.addHeader(entry.getKey(), entry.getValue());
                    L.Log.d(TAG, "add header: %s : %s", entry.getKey(), entry.getValue());
                }
            }
            return chain.proceed(builder.build());
        }
    }

    public static class LogInterceptor implements Interceptor {

        private String tag;

        public LogInterceptor() {
            tag = LogInterceptor.class.getSimpleName();
        }

        public LogInterceptor(String tag) {
            this.tag = tag;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {

            Response response = chain.proceed(chain.request());
            Request request = chain.request().newBuilder().build();
            String url = request.urlString();
            String method = request.method();

            L.Log.d(tag, "Url:%s    Method:%s", url, method);
            if (request.body() != null) {
                final Buffer buffer = new Buffer();
                request.body().writeTo(buffer);
                L.json(tag, buffer.readUtf8());
            }
            return response;
        }
    }

    public <T> Subscription addSubscribe(Observable<T> observable, final Callback<T> listener) {
        Subscriber<T> subscriber = new Subscriber<T>() {
            @Override
            public void onStart() {
                super.onStart();
                if (listener != null) {
                    mMainHandler.post(new Runnable() {
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
        return observable.observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    public void error(Response response) throws IOException {
        if (!response.isSuccessful()) {
            throw new IOException(String.format("Network Error: %s", response));
        }
    }

    public String getSync(String url) throws NetException {
        return getSync(url, newRequestBuilder());
    }

    public String getSync(String url, Request.Builder builder) throws NetException {
        return getSync(getClient(), url, builder);
    }

    public String getSync(OkHttpClient client, String url) throws NetException {
        return getSync(client, url, newRequestBuilder());
    }

    public String getSync(OkHttpClient client, String url,
                          Request.Builder builder) throws NetException {
        return getSync(client, builder.url(url).build());
    }

    public String getSync(OkHttpClient client, Request request) throws NetException {
        try {
            Response response = client.newCall(request).execute();
            String json = response.body().string();
            error(response);
            L.json(TAG, json);
            return json;
        } catch (IOException e) {
            throw new NetException(NetException.NET_ERROR, e);
        }
    }

    public String postSync(String url, String jsonBody) throws NetException {
        return postSync(url, jsonBody, newRequestBuilder());
    }

    public String postSync(String url, String jsonBody,
                           Request.Builder builder) throws NetException {
        return postSync(getClient(), url, jsonBody, builder);
    }

    public String postSync(OkHttpClient client, String url, String jsonBody) throws NetException {
        return postSync(client, url, jsonBody, newRequestBuilder());
    }

    public String postSync(OkHttpClient client, String url, String jsonBody,
                           Request.Builder builder) throws NetException {
        RequestBody body = RequestBody.create(JSON, jsonBody);
        return postSync(client, builder, body, url);
    }

    public String postSync(OkHttpClient client, Request.Builder builder, RequestBody body,
                           String url) throws NetException {
        return postSync(client, builder.url(url).post(body).build());
    }

    public String postSync(OkHttpClient client, Request request) throws NetException {
        try {
            Response response = client.newCall(request).execute();
            String json = response.body().string();
            error(response);
            L.json(TAG, json);
            return json;
        } catch (IOException e) {
            throw new NetException(NetException.NET_ERROR, e);
        }
    }

    public Response headSync(String url) throws NetException {
        return headSync(url, newRequestBuilder());
    }

    public Response headSync(String url, Request.Builder builder) throws NetException {
        return headSync(getClient(), url, builder);
    }

    public Response headSync(OkHttpClient client, String url,
                             Request.Builder builder) throws NetException {
        return headSync(client, builder.url(url).head().build());
    }

    public Response headSync(OkHttpClient client, Request request) throws NetException {
        try {
            Response response = client.newCall(request).execute();
            error(response);
            Map<String, List<String>> stringListMap = response.headers().toMultimap();
            for (Map.Entry<String, List<String>> entry : stringListMap.entrySet()) {
                L.Log.d(TAG, "header > key:%s   value:%s ", entry.getKey(), entry.getValue());
            }
            return response;
        } catch (IOException e) {
            throw new NetException(NetException.NET_ERROR, e);
        }
    }

    public Subscription getAsyncToJsonString(Observable<ActivityEvent> eventObservable, String url,
                                             Callback<String> listener) {
        Observable.Transformer<String, String> lifecycle = RxLifecycle
            .bindActivity(eventObservable);
        return addSubscribe(getAsyncToJsonString(url).compose(lifecycle), listener);
    }

    public Subscription getAsyncToJsonString(String url, Callback<String> listener) {
        return getAsyncToJsonString(getClient(), url, listener);
    }

    public Subscription getAsyncToJsonString(OkHttpClient client, String url,
                                             Callback<String> listener) {
        return addSubscribe(getAsyncToJsonString(client, url), listener);
    }

    public Observable<String> getAsyncToJsonString(String url) {
        return getAsyncToJsonString(getClient(), url);
    }

    public Observable<String> getAsyncToJsonString(OkHttpClient client, String url) {
        return getAsyncToJsonString(client, Observable.just(url));
    }

    public Observable<String> getAsyncToJsonString(final OkHttpClient client,
                                                   Observable<String> observable) {
        return getAsyncToJsonString(client,newRequestBuilder(),observable);
    }

    public Observable<String> getAsyncToJsonString(final Request.Builder builder,
                                                   Observable<String> observable) {
        return getAsyncToJsonString(getClient(),builder,observable);
    }

    public Observable<String> getAsyncToJsonString(final OkHttpClient client, final Request.Builder builder,
                                                   Observable<String> observable) {
        return observable.observeOn(Schedulers.io()).map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                try {
                    return getSync(client, s,builder);
                } catch (SystemException e) {
                    throw new SystemRuntimeException(e);
                }
            }
        });
    }

    public Subscription postAsyncToJsonString(Observable<ActivityEvent> eventObservable, String url,
                                              String json, final Callback<String> listener) {
        Observable.Transformer<String, String> lifecycle = RxLifecycle
            .bindActivity(eventObservable);
        return addSubscribe(postAsyncToJsonString(url, Observable.just(json)).compose(lifecycle),
            listener);
    }

    public Subscription postAsyncToJsonString(String url, String json,
                                              final Callback<String> listener) {
        return postAsyncToJsonString(url, Observable.just(json), listener);
    }

    public Subscription postAsyncToJsonString(final String url, Observable<String> observable,
                                              final Callback<String> listener) {
        return addSubscribe(postAsyncToJsonString(url, observable), listener);
    }

    public Observable<String> postAsyncToJsonString(String url, String json) {
        return postAsyncToJsonString(url, Observable.just(json));
    }

    /**
     *
     * @param url
     * @param observable is jsonBody
     * @return
     */
    public Observable<String> postAsyncToJsonString(String url, Observable<String> observable) {
        return postAsyncToJsonString(getClient(), url, observable);
    }

    public Observable<String> postAsyncToJsonString(final OkHttpClient client, final String url,
                                                    Observable<String> observable) {
        return observable.observeOn(Schedulers.io()).map(new Func1<String, String>() {
            @Override
            public String call(String jsonBody) {
                try {
                    return postSync(client, url, jsonBody);
                } catch (SystemException e) {
                    throw new SystemRuntimeException(e);
                }
            }
        });
    }

    public class FileDownload {
        private String fileName;
        private String url;
        private byte[] bytes;
        private Bitmap bitmap;
        private File   file;

        public FileDownload() {
        }

        public FileDownload(File file) {
            this.file = file;
        }

        public FileDownload(String fileName, String url) {
            this.fileName = fileName;
            this.url = url;
        }

        public FileDownload(String fileName, Bitmap bitmap) {
            this.fileName = fileName;
            this.bitmap = bitmap;
        }

        public FileDownload(String fileName, byte[] bytes) {
            this.fileName = fileName;
            this.bytes = bytes;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        public byte[] getBytes() {
            return bytes;
        }

        public void setBytes(byte[] bytes) {
            this.bytes = bytes;
        }

        public File getFile() {
            return file;
        }

        public void setFile(File file) {
            this.file = file;
        }
    }

    public Observable.Transformer<String, FileDownload> applyGetFileName() {
        return new Observable.Transformer<String, FileDownload>() {
            @Override
            public Observable<FileDownload> call(Observable<String> stringObservable) {
                return stringObservable.observeOn(Schedulers.io())
                    .flatMap(new Func1<String, Observable<FileDownload>>() {
                    @Override
                    public Observable<FileDownload> call(String url) {
                        try {
                            String extension = FilenameUtils.getExtension(url);
                            if (!TextUtils.isEmpty(extension)) {
                                return Observable
                                    .just(new FileDownload(FilenameUtils.getName(url), url));
                            }
                            Response response = OkHttpUtils.get().headSync(url);
                            String header = response.header("Content-Disposition");
                            String fileName = Utils.getContentDispositionFileName(header,UUID.randomUUID().toString() + ".jpg");
                            return Observable.just(new FileDownload(fileName, url));
                        } catch (NetException e) {
                            throw new SystemRuntimeException(e);
                        }
                    }
                });
            }
        };
    }

    public Observable.Transformer<FileDownload, FileDownload> applyDownloadFile() {
        return new Observable.Transformer<FileDownload, FileDownload>() {
            @Override
            public Observable<FileDownload> call(Observable<FileDownload> downloadObservable) {
                return downloadObservable
                    .flatMap(new Func1<FileDownload, Observable<FileDownload>>() {
                    @Override
                    public Observable<FileDownload> call(FileDownload fileDownload) {
                        try {
                            OkHttpClient client = OkHttpUtils.get().cloneClient();
                            client.setReadTimeout(2, TimeUnit.MINUTES);
                            Request request = new Request.Builder().url(fileDownload.getUrl())
                                .build();
                            Response response = client.newCall(request).execute();
                            error(response);
                            return Observable.just(new FileDownload(fileDownload.getFileName(),
                                response.body().bytes()));
                        } catch (IOException e) {
                            throw new SystemRuntimeException(e);
                        }
                    }
                });
            }
        };
    }

    public Observable.Transformer<String, FileDownload> applyDownloadImage() {
        return new Observable.Transformer<String, FileDownload>() {
            @Override
            public Observable<FileDownload> call(Observable<String> downloadObservable) {
                return downloadObservable.compose(applyGetFileName()).compose(applyDownloadFile())
                    .flatMap(new Func1<FileDownload, Observable<FileDownload>>() {
                    @Override
                    public Observable<FileDownload> call(FileDownload fileDownload) {
                        Bitmap bitmap = BitmapUtils.byteToBitmap(fileDownload.getBytes());
                        return Observable
                            .just(new FileDownload(fileDownload.getFileName(), bitmap));
                    }
                });
            }
        };
    }

    public Subscription downloadImage(final String url, Callback<FileDownload> callback) {
        Observable<FileDownload> compose = Observable.just(url).compose(applyDownloadImage());
        return addSubscribe(compose, callback);
    }

    public Subscription downloadImageToFile(final String url, final Context context, final File dir,
                                            Callback<FileDownload> callback) {
        Observable<FileDownload> observable = Observable.just(url).compose(applyDownloadImage())
            .map(new Func1<FileDownload, FileDownload>() {
                @Override
                public FileDownload call(FileDownload fileName) {
                    try {
                        File file = FileUtils.createFile(dir, fileName.getFileName());
                        Bitmap.CompressFormat format = BitmapUtils
                            .getImageExtension(fileName.getFileName());
                        BitmapUtils.insertImage(fileName.getBitmap(), format, file);
                        BitmapUtils.saveImageToSystemPhoto(context, file);
                        return new FileDownload(file);
                    } catch (SystemException e) {
                        throw new SystemRuntimeException(e);
                    }
                }
            });
        return addSubscribe(observable, callback);
    }

}
