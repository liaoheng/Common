package com.github.liaoheng.common.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import com.github.liaoheng.common.util.BitmapUtils;
import com.github.liaoheng.common.util.Callback;
import com.github.liaoheng.common.util.FileUtils;
import com.github.liaoheng.common.util.L;
import com.github.liaoheng.common.util.NetException;
import com.github.liaoheng.common.util.NetLocalException;
import com.github.liaoheng.common.util.NetServerException;
import com.github.liaoheng.common.util.SystemException;
import com.github.liaoheng.common.util.SystemRuntimeException;
import com.github.liaoheng.common.util.Utils;
import java.io.EOFException;
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
import okhttp3.Cache;
import okhttp3.Dispatcher;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSource;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONException;
import org.json.JSONObject;
import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * OKHttp3 工具类
 *
 * @author liaoheng
 * @version 2016-06-24 13:03:45
 */
@SuppressWarnings("WeakerAccess") public class OkHttp3Utils {
    private final String    TAG          = OkHttp3Utils.class.getSimpleName();
    public final  MediaType JSON         = MediaType.parse("application/json; charset=utf-8");
    private final OkHttpClient             mClient;
    private       ErrorHandleListener      mErrorHandleListener;
    private       Map<String, String>      mHeaders;
    private       List<HeaderPlusListener> mHeaderPlus;

    public interface HeaderPlusListener {
        /**
         * http请求拦截
         * @param originalRequest 原始Request
         * @param builder 前面处理后的builder
         * @param parameters 前面向后传递的参数
         * @return {@link Request.Builder}
         */
        Request.Builder headerInterceptor(Request originalRequest, Request.Builder builder,
                                          Map<String, Object> parameters);
    }

    public interface ErrorHandleListener {
        /**
         *
         * @param response {@link Response}
         * @return Response
         * @throws NetException
         */
        Response checkError(Response response) throws NetException;
    }

    private OkHttp3Utils(OkHttpClient.Builder builder, ErrorHandleListener errorHandleListener,
                         List<HeaderPlusListener> headerPluses, Map<String, String> headers) {
        builder.interceptors().add(new HeaderPlusInterceptor());
        builder.interceptors().add(new ErrorInterceptor());
        builder.interceptors().add(new LogInterceptor(TAG));

        this.mClient = builder.build();
        this.mErrorHandleListener = errorHandleListener;
        this.mHeaderPlus = headerPluses;
        this.mHeaders = headers;
    }

    private OkHttp3Utils(OkHttpClient.Builder builder) {
        this.mClient = builder.build();
        this.mErrorHandleListener = OkHttp3Utils.get().getErrorHandleListener();
        this.mHeaderPlus = OkHttp3Utils.get().getHeaderPlus();
        this.mHeaders = OkHttp3Utils.get().getHeaders();
    }

    private static OkHttp3Utils INSTANCE;

    public static OkHttp3Utils get() {
        if (INSTANCE == null) {
            throw new IllegalStateException("No initialization");
        }
        return INSTANCE;
    }

    /**
     * 单线程
     *
     * @return {@link OkHttpClient}
     */
    public static OkHttpClient getSingleOkHttpClient() {
        return getSingleOkHttpClientBuilder().build();
    }

    /**
     * 单线程
     *
     * @return {@link OkHttpClient}
     */
    public static OkHttpClient.Builder getSingleOkHttpClientBuilder() {
        return getSingleOkHttpClientBuilder(OkHttp3Utils.get().cloneClient());
    }

    /**
     * 单线程
     *
     * @return {@link OkHttpClient}
     */
    public static OkHttpClient.Builder getSingleOkHttpClientBuilder(OkHttpClient.Builder builder) {
        ExecutorService threadPoolExecutor = Executors
                .newSingleThreadExecutor(Util.threadFactory("OkHttp Dispatcher", false));
        Dispatcher dispatcher = new Dispatcher(threadPoolExecutor);
        builder.dispatcher(dispatcher);
        return builder;
    }

    public static OkHttp3Utils setInit(OkHttp3Utils okHttpUtils) {
        return INSTANCE = okHttpUtils;
    }

    public static Init init() {
        return new Init();
    }

    public static class Init {
        private final int DEFAULT_HTTP_DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB;
        private OkHttpClient.Builder     builder;
        private ErrorHandleListener      errorHandleListener;
        private Map<String, String>      headers;
        private List<HeaderPlusListener> headerPlus;
        private Cache                    cache;

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
            File cacheFile = FileUtils.createCacheSDAndroidDirectory(CommonNet.HTTP_CACHE_DIR);
            if (httpDiskCacheSize == 0) {
                httpDiskCacheSize = DEFAULT_HTTP_DISK_CACHE_SIZE;
            }
            return new Cache(cacheFile, httpDiskCacheSize);
        }

        public void initialization() {
            OkHttp3Utils.setInit(build());
        }

        private ErrorHandleListener getDefaultErrorHandleListener() {
            return new ErrorHandleListener() {
                @Override public Response checkError(Response response) throws NetServerException {
                    if (!response.isSuccessful()) {
                        String string = "code : " + response.code();
                        if (response.body().contentLength() > 0) {
                            try {
                                string = response.body().string();
                            } catch (IOException ignored) {
                            }
                        }
                        throw new NetServerException(response.message(), string);
                    }
                    return response;
                }
            };
        }

        public OkHttp3Utils build() {
            if (builder == null) {
                builder = new OkHttpClient.Builder();
            }
            if (headerPlus == null) {
                headerPlus = new LinkedList<>();
            }
            if (headers == null) {
                headers = new LinkedHashMap<>();
            }
            if (cache != null) {
                builder.cache(cache);
            }
            if (errorHandleListener == null) {
                errorHandleListener = getDefaultErrorHandleListener();
            }
            return new OkHttp3Utils(builder, errorHandleListener, headerPlus, headers);
        }

        /**
         * errorHandleListener, headerPlus, headers 使用初始化逻辑
         * @param builder clone OkHttpClient.Builder
         * @return
         */
        public OkHttp3Utils build(OkHttpClient.Builder builder) {
            return new OkHttp3Utils(builder);
        }

        public OkHttpClient.Builder getBuilder() {
            return builder;
        }

        public Init setBuilder(OkHttpClient.Builder builder) {
            this.builder = builder;
            return this;
        }

        public Map<String, String> getHeaders() {
            return headers;
        }

        public Init setHeaders(Map<String, String> headers) {
            this.headers = new LinkedHashMap<>(headers);
            return this;
        }

        public Init addHeaders(String key, String value) {
            if (this.headers == null) {
                this.headers = new LinkedHashMap<>();
            }
            this.headers.put(key, value);
            return this;
        }

        public List<HeaderPlusListener> getHeaderPlus() {
            return headerPlus;
        }

        public Init setHeaderPlus(List<HeaderPlusListener> headerPlus) {
            this.headerPlus = new LinkedList<>(headerPlus);
            return this;
        }

        public Init addHeaderPlus(HeaderPlusListener... headerPlus) {
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

        public ErrorHandleListener getErrorHandleListener() {
            return errorHandleListener;
        }

        public Init setErrorHandleListener(ErrorHandleListener errorHandleListener) {
            this.errorHandleListener = errorHandleListener;
            return this;
        }
    }

    public OkHttpClient.Builder cloneClient() {
        if (getClient() == null) {
            throw new IllegalStateException("Not Initialize");
        }
        return getClient().newBuilder();
    }

    /**
     * clear interceptors
     */
    public OkHttpClient.Builder cloneNewClient() {
        if (getClient() == null) {
            throw new IllegalStateException("Not Initialize");
        }
        OkHttpClient.Builder builder = getClient().newBuilder();
        builder.interceptors().clear();
        return builder;
    }

    public OkHttpClient getClient() {
        if (mClient == null) {
            throw new IllegalStateException("Not Initialize");
        }
        return mClient;
    }

    public void clearCache() {
        if (getClient().cache() == null) {
            return;
        }
        try {
            getClient().cache().delete();
        } catch (IOException ignored) {
        }
    }

    public Map<String, String> getHeaders() {
        if (mHeaders == null) {
            throw new IllegalStateException("Not Initialize");
        }
        return mHeaders;
    }

    public ErrorHandleListener getErrorHandleListener() {
        return mErrorHandleListener;
    }

    public void setErrorHandleListener(ErrorHandleListener mErrorHandleListener) {
        this.mErrorHandleListener = mErrorHandleListener;
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
        addHeader(key, value);
    }

    public void addHeader(String key, String value) {
        getHeaders().put(key, value);
    }

    public List<HeaderPlusListener> getHeaderPlus() {
        if (mHeaderPlus == null) {
            throw new IllegalStateException("Not Initialize");
        }
        return mHeaderPlus;
    }

    public void addHeaderPlus(List<HeaderPlusListener> headerPluses) {
        getHeaderPlus().addAll(headerPluses);
    }

    public void addHeaderPlus(HeaderPlusListener... headerPluses) {
        getHeaderPlus().addAll(Arrays.asList(headerPluses));
    }

    public void updateHeaderPlus(int location, HeaderPlusListener headerPlus) {
        getHeaderPlus().remove(location);
        addHeaderPlus(headerPlus);
    }

    @SuppressWarnings("SuspiciousMethodCalls") public void updateHeaderPlus(Objects location,
                                                                            HeaderPlusListener headerPlus) {
        getHeaderPlus().remove(location);
        addHeaderPlus(headerPlus);
    }

    public void addHeaderPlus(HeaderPlusListener headerPlus) {
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
        if (cookies.size() == 1) {
            return cookies.get(0);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0, size = cookies.size(); i < size; i++) {
            if (i > 0) {
                sb.append("; ");
            }
            sb.append(cookies.get(i));
        }
        return sb.toString();
    }

    public Request.Builder newRequestBuilder() {
        return new Request.Builder();
    }

    private class HeaderPlusInterceptor implements Interceptor {

        @Override public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            Request request = chain.request();
            Map<String, Object> map = new HashMap<>();

            if (!getHeaderPlus().isEmpty()) {
                for (HeaderPlusListener mHeaderPlu : getHeaderPlus()) {
                    builder = mHeaderPlu.headerInterceptor(request, builder, map);
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

    private class ErrorInterceptor implements Interceptor {

        @Override public Response intercept(Chain chain) throws IOException {
            Response response = chain.proceed(chain.request());
            if (mErrorHandleListener != null) {
                try {
                    return mErrorHandleListener.checkError(response);
                } catch (NetException e) {
                    throw new SystemRuntimeException(e);
                }
            } else {
                return response;
            }
        }
    }

    /**
     * http log
     * @see <a href="https://github.com/square/okhttp/blob/master/okhttp-logging-interceptor/src/main/java/okhttp3/logging/HttpLoggingInterceptor.java">HttpLoggingInterceptor</a>
     */
    public static class LogInterceptor implements Interceptor {

        private String tag;

        public LogInterceptor() {
            tag = LogInterceptor.class.getSimpleName();
        }

        public LogInterceptor(String tag) {
            this.tag = tag;
        }

        /**
         * Returns true if the body in question probably contains human readable text. Uses a small sample
         * of code points to detect unicode control characters commonly used in binary file signatures.
         */
        private static boolean isPlaintext(Buffer buffer){
            try {
                Buffer prefix = new Buffer();
                long byteCount = buffer.size() < 64 ? buffer.size() : 64;
                buffer.copyTo(prefix, 0, byteCount);
                for (int i = 0; i < 16; i++) {
                    if (prefix.exhausted()) {
                        break;
                    }
                    int codePoint = prefix.readUtf8CodePoint();
                    if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                        return false;
                    }
                }
                return true;
            } catch (EOFException e) {
                return false; // Truncated UTF-8 sequence.
            }
        }

        @Override public Response intercept(Chain chain) throws IOException {

            Request request = chain.request();
            long t1 = System.nanoTime();
            L.Log.d(tag, "Sending request %s on %s%n%s", request.url(), request.method(),
                    request.headers());

            try {
                if (request.body() != null) {
                    if (!request.body().contentType()
                            .equals(MediaType.parse("multipart/form-data"))) {
                        final Buffer buffer = new Buffer();
                        request.body().writeTo(buffer);
                        if (isPlaintext(buffer)) {
                            L.Log.d(tag, buffer.clone().readUtf8());
                        }
                    }
                }
            } catch (Exception ignored) {
            }

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            L.Log.d(tag, "Received response(%s) for %s in %.1fms%n%s", response.code(),
                    response.request().url(), (t2 - t1) / 1e6d, response.headers());

            try {
                if (response.body() != null) {
                    ResponseBody responseBody = response.body();
                    BufferedSource source = responseBody.source();
                    source.request(Long.MAX_VALUE); // Buffer the entire body.
                    Buffer buffer = source.buffer();
                    if (isPlaintext(buffer)) {
                        L.Log.d(tag, buffer.clone().readUtf8());
                    }
                }
            } catch (Exception ignored) {
            }
            return response;
        }
    }

    public String getSync(String url) throws NetException {
        return getSync(url, newRequestBuilder());
    }

    public String getSync(String url, Request.Builder builder)
            throws NetException {
        return getSync(builder.url(url).build());
    }

    public String getSync(Request request) throws NetException {
        try {
            Response response = getClient().newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            throw new NetLocalException(NetException.NET_ERROR, e);
        }
    }

    public String postSync(String url, String jsonBody) throws NetException {
        return postSync(url, jsonBody, newRequestBuilder());
    }

    public String postSync(String url, String jsonBody,
                           Request.Builder builder) throws NetException {
        RequestBody body = RequestBody.create(JSON, jsonBody);
        return postSync(builder.post(body).url(url).build());
    }

    public String postSync(Request request) throws NetException {
        try {
            Response response = getClient().newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            throw new NetLocalException(NetException.NET_ERROR, e);
        }
    }

    public Response headSync(String url) throws NetException {
        return headSync(url, newRequestBuilder());
    }

    public Response headSync(String url, Request.Builder builder)
            throws NetException {
        return headSync(builder.url(url).head().build());
    }

    public Response headSync(Request request) throws NetException {
        try {
            Response response = getClient().newCall(request).execute();
            Map<String, List<String>> stringListMap = response.headers().toMultimap();
            for (Map.Entry<String, List<String>> entry : stringListMap.entrySet()) {
                L.Log.d(TAG, "header > key:%s   value:%s ", entry.getKey(), entry.getValue());
            }
            return response;
        } catch (IOException e) {
            throw new NetLocalException(NetException.NET_ERROR, e);
        }
    }

    public Subscription getAsyncToJsonString(String url,
                                             Callback<String> listener) {
        return Utils.addSubscribe(getAsyncToJsonString(url), listener);
    }

    public Subscription getAsyncToJsonString(String url, Request.Builder builder,
                                             Callback<String> listener) {
        return Utils.addSubscribe(getAsyncToJsonString(url, builder), listener);
    }

    public Observable<String> getAsyncToJsonString(String url) {
        return getAsyncToJsonString(url, newRequestBuilder());
    }

    public Observable<String> getAsyncToJsonString(String url, Request.Builder builder) {
        return getAsyncToJsonString(builder, Observable.just(url));
    }

    public Observable<String> getAsyncToJsonString(
                                                   final Request.Builder builder,
                                                   Observable<String> observable) {
        return observable.observeOn(Schedulers.io()).map(new Func1<String, String>() {
            @Override public String call(String s) {
                try {
                    return getSync(s, builder);
                } catch (SystemException e) {
                    throw new SystemRuntimeException(e);
                }
            }
        });
    }

    public Subscription postAsyncToJsonString(String url, String json,
                                              final Callback<String> listener) {
        return postAsyncToJsonString(url, Observable.just(json), listener);
    }

    public Subscription postAsyncToJsonString(final String url, Observable<String> observable,
                                              final Callback<String> listener) {
        return Utils.addSubscribe(postAsyncToJsonString(url, observable), listener);
    }

    public Observable<String> postAsyncToJsonString(String url, String json) {
        return postAsyncToJsonString(url, Observable.just(json));
    }

    public Observable<String> postAsyncToJsonString(final String url,
                                                    Observable<String> observable) {
        return observable.observeOn(Schedulers.io()).map(new Func1<String, String>() {
            @Override public String call(String jsonBody) {
                try {
                    return postSync(url, jsonBody);
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
            @Override public Observable<FileDownload> call(Observable<String> stringObservable) {
                return stringObservable.observeOn(Schedulers.io())
                        .flatMap(new Func1<String, Observable<FileDownload>>() {
                            @Override public Observable<FileDownload> call(String url) {
                                try {
                                    String extension = FilenameUtils.getExtension(url);
                                    if (!TextUtils.isEmpty(extension)) {
                                        return Observable
                                                .just(new FileDownload(FilenameUtils.getName(url),
                                                        url));
                                }
                                    Response response = OkHttp3Utils.get().headSync(url);
                                    String header = response.header("Content-Disposition");
                                    String fileName = Utils.getContentDispositionFileName(header,
                                            UUID.randomUUID().toString() + ".jpg");
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
            @Override public Observable<FileDownload> call(
                    Observable<FileDownload> downloadObservable) {
                return downloadObservable
                        .flatMap(new Func1<FileDownload, Observable<FileDownload>>() {
                            @Override public Observable<FileDownload> call(
                                    FileDownload fileDownload) {
                                try {
                                    OkHttpClient.Builder builder = OkHttp3Utils.get().cloneClient();
                                    builder.readTimeout(2, TimeUnit.MINUTES);
                                    Request request = new Request.Builder()
                                            .url(fileDownload.getUrl()).build();
                                    Response response = builder.build().newCall(request).execute();
                                    return Observable
                                            .just(new FileDownload(fileDownload.getFileName(),
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
            @Override public Observable<FileDownload> call(Observable<String> downloadObservable) {
                return downloadObservable.compose(applyGetFileName()).compose(applyDownloadFile())
                        .flatMap(new Func1<FileDownload, Observable<FileDownload>>() {
                            @Override public Observable<FileDownload> call(
                                    FileDownload fileDownload) {
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
        return Utils.addSubscribe(compose, callback);
    }

    public Subscription downloadImageToFile(final String url, final Context context, final File dir,
                                            Callback<FileDownload> callback) {
        Observable<FileDownload> observable = Observable.just(url).compose(applyDownloadImage())
                .map(new Func1<FileDownload, FileDownload>() {
                    @Override public FileDownload call(FileDownload fileName) {
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
        return Utils.addSubscribe(observable, callback);
    }

    public static JSONObject getResponseBodyString(ResponseBody body) {
        try {
            String json = body.string();
            return new JSONObject(json);
        } catch (IOException | JSONException ignored) {
        }
        return new JSONObject();
    }

}
