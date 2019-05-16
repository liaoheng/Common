package com.github.liaoheng.common.network;

import android.content.Context;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.github.liaoheng.common.util.Callback;
import com.github.liaoheng.common.util.*;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.*;
import okhttp3.internal.Util;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * OKHttp3 工具类
 *
 * @author liaoheng
 * @version 2016-06-24 13:03:45
 */
@SuppressWarnings("WeakerAccess")
public class OkHttp3Utils {
    private final String TAG = OkHttp3Utils.class.getSimpleName();
    public final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final OkHttpClient mClient;
    private Map<String, String> mHeaders;
    private List<HeaderPlusListener> mHeaderPlus;

    public interface HeaderPlusListener {
        /**
         * http请求拦截
         *
         * @param originalRequest 原始Request
         * @param builder         前面处理后的builder
         * @param parameters      前面向后传递的参数
         * @return {@link Request.Builder}
         */
        Request.Builder headerInterceptor(Request originalRequest, Request.Builder builder,
                Map<String, Object> parameters);
    }

    /**
     * 会封装在{@link DefaultErrorInterceptor}中
     */
    public interface ErrorHandleListener {
        /**
         * @param response {@link Response}
         * @return Response
         * @throws NetException 最后会以{@link SystemRuntimeException}抛出
         */
        Response checkError(Response response) throws NetException;
    }

    private OkHttp3Utils(OkHttpClient.Builder builder, Interceptor errorInterceptor,
            List<HeaderPlusListener> headerPluses, Map<String, String> headers) {
        builder.addInterceptor(new HeaderPlusInterceptor());
        if (errorInterceptor != null) {
            builder.addInterceptor(errorInterceptor);
        }
        builder.addInterceptor(new LogInterceptor(TAG));

        this.mClient = builder.build();
        this.mHeaderPlus = headerPluses;
        this.mHeaders = headers;
    }

    /**
     * clone为true时，共用{@link OkHttp3Utils#INSTANCE}中的参数，否则不处理
     */
    private OkHttp3Utils(OkHttpClient.Builder builder, boolean clone) {
        this.mClient = builder.build();
        if (clone) {
            try {
                this.mHeaderPlus = OkHttp3Utils.get().getHeaderPlus();
                this.mHeaders = OkHttp3Utils.get().getHeaders();
            } catch (IllegalStateException ignored) {
            }
        }
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
        private OkHttpClient.Builder builder;
        private ErrorHandleListener errorHandleListener;
        private Map<String, String> headers;
        private List<HeaderPlusListener> headerPlus;
        private Cache cache;

        public Init setDefaultCache(Context context) {
            return setDefaultCache(context, DEFAULT_HTTP_DISK_CACHE_SIZE);
        }

        public Init setDefaultCache(Context context, long httpDiskCacheSize) {
            try {
                cache = getDefaultCache(context, httpDiskCacheSize);
            } catch (SystemException ignored) {
            }
            return this;
        }

        private Cache getDefaultCache(Context context, long httpDiskCacheSize) throws SystemException {
            File cacheFile = FileUtils.getProjectSpaceCacheDirectory(context, CommonNet.HTTP_CACHE_DIR);
            if (httpDiskCacheSize == 0) {
                httpDiskCacheSize = DEFAULT_HTTP_DISK_CACHE_SIZE;
            }
            return new Cache(cacheFile, httpDiskCacheSize);
        }

        public Init setDefaultErrorHandleListener() {
            errorHandleListener = new ErrorHandleListener() {
                @Override
                public Response checkError(Response response) throws NetServerException {
                    if (!response.isSuccessful()) {
                        String string = "code : " + response.code();
                        ResponseBody body = response.body();
                        if (body != null && body.contentLength() > 0) {
                            try {
                                string = body.string();
                            } catch (IOException ignored) {
                            }
                        }
                        throw new NetServerException(response.message(), string);
                    }
                    return response;
                }
            };
            return this;
        }

        public void initialization() {
            OkHttp3Utils.setInit(build());
        }

        /**
         * 默认不处理http错误请求，需要时使用{@link ErrorHandleListener}。
         */
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
            DefaultErrorInterceptor errorInterceptor = null;
            if (errorHandleListener != null) {
                errorInterceptor = new DefaultErrorInterceptor(errorHandleListener);
            }
            return new OkHttp3Utils(builder, errorInterceptor, headerPlus, headers);
        }

        /**
         * 生成OkHttp3Utils，从OkHttp3Utils中clone并需要使用之前的interceptors方法时。<br/>
         * Example:
         * <pre>
         *     OkHttpClient.Builder builder = OkHttp3Utils.getSingleOkHttpClientBuilder();
         *     OkHttp3Utils httpUtils = OkHttp3Utils.init().buildClone(builder);
         * </pre>
         *
         * @param cloneBuilder clone {@link OkHttp3Utils#INSTANCE} OkHttpClient.Builder
         */
        public OkHttp3Utils buildClone(OkHttpClient.Builder cloneBuilder) {
            return new OkHttp3Utils(cloneBuilder, true);
        }

        /**
         * 生成OkHttp3Utils，从OkHttp3Utils中clone并不使用之前的interceptors方法或一个全新的OkHttpClient。<br/>
         * Example:
         * <pre>
         *     OkHttpClient.Builder builder = new OkHttpClient.Builder();
         *     OkHttp3Utils httpUtils = OkHttp3Utils.init().build(builder);
         * </pre>
         *
         * @param builder OkHttpClient.Builder
         */
        public OkHttp3Utils build(OkHttpClient.Builder builder) {
            return new OkHttp3Utils(builder, false);
        }

        public Init setBuilder(OkHttpClient.Builder builder) {
            this.builder = builder;
            return this;
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

        public Init setHeaderPlus(List<HeaderPlusListener> headerPlus) {
            this.headerPlus = new LinkedList<>(headerPlus);
            return this;
        }

        public Init addHeaderPlus(HeaderPlusListener... headerPlus) {
            this.headerPlus = new LinkedList<>(Arrays.asList(headerPlus));
            return this;
        }

        public Init setCache(Cache cache) {
            this.cache = cache;
            return this;
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
            mHeaders = new HashMap<>();
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
        addHeader(key, value);
    }

    public void addHeader(String key, String value) {
        getHeaders().put(key, value);
    }

    public List<HeaderPlusListener> getHeaderPlus() {
        if (mHeaderPlus == null) {
            mHeaderPlus = new LinkedList<>();
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

    @SuppressWarnings("SuspiciousMethodCalls")
    public void updateHeaderPlus(Objects location,
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

        @NonNull
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
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
                    L.alog().d(TAG, "add header: %s : %s", entry.getKey(), entry.getValue());
                }
            }
            return chain.proceed(builder.build());
        }
    }

    public static class DefaultErrorInterceptor implements Interceptor {
        private ErrorHandleListener mErrorHandleListener;

        public DefaultErrorInterceptor(ErrorHandleListener errorHandleListener) {
            mErrorHandleListener = errorHandleListener;
        }

        @NonNull
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
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
     *
     * @see <a href="https://github.com/square/okhttp/blob/master/okhttp-logging-interceptor/src/main/java/okhttp3/logging/HttpLoggingInterceptor.java">HttpLoggingInterceptor</a>
     */
    public static class LogInterceptor implements Interceptor {

        private String tag;

        public LogInterceptor() {
            this(LogInterceptor.class.getSimpleName());
        }

        public LogInterceptor(String tag) {
            this.tag = tag;
        }

        private static final Charset UTF8 = Charset.forName("UTF-8");

        /**
         * Returns true if the body in question probably contains human readable text. Uses a small sample
         * of code points to detect unicode control characters commonly used in binary file signatures.
         */
        private boolean isPlaintext(Buffer buffer) {
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

        private static boolean bodyHasUnknownEncoding(Headers headers) {
            String contentEncoding = headers.get("Content-Encoding");
            return contentEncoding != null
                    && !contentEncoding.equalsIgnoreCase("identity")
                    && !contentEncoding.equalsIgnoreCase("gzip");
        }

        @NonNull
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request request = chain.request();

            if (L.isPrint()) {
                return chain.proceed(request);
            }

            RequestBody requestBody = request.body();
            boolean hasRequestBody = requestBody != null;

            Connection connection = chain.connection();
            String requestStartMessage = "--> "
                    + request.method()
                    + ' ' + request.url()
                    + (connection != null ? " " + connection.protocol() : "");
            L.alog().d(tag, requestStartMessage);

            if (hasRequestBody) {
                // Request body headers are only present when installed as a network interceptor. Force
                // them to be included (when available) so there values are known.
                if (requestBody.contentType() != null) {
                    L.alog().d(tag, "Content-Type: " + requestBody.contentType());
                }
                if (requestBody.contentLength() != -1) {
                    L.alog().d(tag, "Content-Length: " + requestBody.contentLength());
                }
            }

            Headers rheaders = request.headers();
            for (int i = 0, count = rheaders.size(); i < count; i++) {
                String name = rheaders.name(i);
                // Skip headers from the request body as they are explicitly logged above.
                if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                    L.alog().d(tag, rheaders.name(i) + ": " + rheaders.value(i));
                }
            }

            if (!hasRequestBody) {
                L.alog().d(tag, "--> END " + request.method());
            } else if (bodyHasUnknownEncoding(request.headers())) {
                L.alog().d(tag, "--> END " + request.method() + " (encoded body omitted)");
            } else if (requestBody.isDuplex()) {
                L.alog().d(tag, "--> END " + request.method() + " (duplex request body omitted)");
            }
            else {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);

                Charset charset = UTF8;
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }

                L.alog().d(tag, "");
                if (isPlaintext(buffer)) {
                    L.alog().d(tag, buffer.readString(charset));
                    L.alog().d(tag, "--> END " + request.method()
                            + " (" + requestBody.contentLength() + "-byte body)");
                } else {
                    L.alog().d(tag, "--> END " + request.method() + " (binary "
                            + requestBody.contentLength() + "-byte body omitted)");
                }
            }

            long startNs = System.nanoTime();
            Response response;
            try {
                response = chain.proceed(request);
            } catch (Exception e) {
                L.alog().d(tag, "<-- HTTP FAILED: " + e);
                throw e;
            }
            long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                L.alog().d(tag, "responseBody is null");
                return response;
            }
            long contentLength = responseBody.contentLength();
            String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
            L.alog().d(tag, "<-- "
                    + response.code()
                    + (response.message().isEmpty() ? "" : ' ' + response.message())
                    + ' ' + response.request().url()
                    + " (" + tookMs + "ms" + ", " + bodySize + " body" + ')');

            Headers sheaders = response.headers();
            for (int i = 0, count = sheaders.size(); i < count; i++) {
                L.alog().d(tag, sheaders.name(i) + ": " + sheaders.value(i));
            }

            if (!HttpHeaders.hasBody(response)) {
                L.alog().d(tag, "<-- END HTTP");
            } else if (bodyHasUnknownEncoding(response.headers())) {
                L.alog().d(tag, "<-- END HTTP (encoded body omitted)");
            } else {
                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE); // Buffer the entire body.
                Buffer buffer = source.getBuffer();

                Long gzippedLength = null;
                if ("gzip".equalsIgnoreCase(sheaders.get("Content-Encoding"))) {
                    gzippedLength = buffer.size();
                    try (GzipSource gzippedResponseBody = new GzipSource(buffer.clone())) {
                        buffer = new Buffer();
                        buffer.writeAll(gzippedResponseBody);
                    }
                }

                Charset charset = UTF8;
                MediaType contentType = responseBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }

                if (!isPlaintext(buffer)) {
                    L.alog().d(tag, "");
                    L.alog().d(tag, "<-- END HTTP (binary " + buffer.size() + "-byte body omitted)");
                    return response;
                }

                if (contentLength != 0) {
                    L.alog().d(tag, "");
                    L.alog().d(tag, buffer.clone().readString(charset));
                }

                if (gzippedLength != null) {
                    L.alog().d(tag, "<-- END HTTP (" + buffer.size() + "-byte, "
                            + gzippedLength + "-gzipped-byte body)");
                } else {
                    L.alog().d(tag, "<-- END HTTP (" + buffer.size() + "-byte body)");
                }
            }
            return response;
        }
    }

    private String getResponseBody(Response response) {
        try {
            return response.body() == null ? "" : response.body().string();
        } catch (IOException e) {
            return "";
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
            return getResponseBody(response);
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
            return getResponseBody(response);
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
                L.alog().d(TAG, "header > key:%s   value:%s ", entry.getKey(), entry.getValue());
            }
            return response;
        } catch (IOException e) {
            throw new NetLocalException(NetException.NET_ERROR, e);
        }
    }

    public Disposable getAsyncToJsonString(String url,
            Callback<String> listener) {
        return Utils.addSubscribe(getAsyncToJsonString(url), listener);
    }

    public Disposable getAsyncToJsonString(String url, Request.Builder builder,
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
        return observable.subscribeOn(Schedulers.io())
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String s) {
                        try {
                            return Observable.just(getSync(s, builder));
                        } catch (SystemException e) {
                            return Observable.error(e);
                        }
                    }
                });
    }

    public Disposable postAsyncToJsonString(String url, String json, Callback<String> listener) {
        return postAsyncToJsonString(url, Observable.just(json), listener);
    }

    public Disposable postAsyncToJsonString(String url, Observable<String> observable,
            final Callback<String> listener) {
        return Utils.addSubscribe(postAsyncToJsonString(url, observable), listener);
    }

    public Observable<String> postAsyncToJsonString(String url, String json) {
        return postAsyncToJsonString(url, Observable.just(json));
    }

    public Observable<String> postAsyncToJsonString(final String url,
            Observable<String> observable) {
        return observable.subscribeOn(Schedulers.io()).flatMap(new Function<String, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(String jsonBody) {
                try {
                    return Observable.just(postSync(url, jsonBody));
                } catch (SystemException e) {
                    return Observable.error(e);
                }
            }
        });
    }

    public class FileDownload {
        private String fileName;
        private String url;
        private InputStream inputStream;
        private File file;

        public FileDownload() {
        }

        public FileDownload(File file) {
            this.file = file;
        }

        public FileDownload(String fileName, String url) {
            this.fileName = fileName;
            this.url = url;
        }

        public FileDownload(String fileName, InputStream inputStream) {
            this.fileName = fileName;
            this.inputStream = inputStream;
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

        public InputStream getInputStream() {
            return inputStream;
        }

        public void setInputStream(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        public File getFile() {
            return file;
        }

        public void setFile(File file) {
            this.file = file;
        }
    }

    public ObservableTransformer<String, FileDownload> applyGetFileName() {
        return new ObservableTransformer<String, FileDownload>() {
            @Override
            public ObservableSource<FileDownload> apply(Observable<String> upstream) {
                return upstream
                        .flatMap(new Function<String, Observable<FileDownload>>() {
                            @Override
                            public Observable<FileDownload> apply(String url) {
                                try {
                                    String extension = FileUtils.getExtension(url);
                                    if (!TextUtils.isEmpty(extension)) {
                                        return Observable
                                                .just(new FileDownload(FileUtils.getName(url),
                                                        url));
                                    }
                                    Response response = OkHttp3Utils.get().headSync(url);
                                    String header = response.header("Content-Disposition");
                                    String fileName = Utils.getContentDispositionFileName(header,
                                            UUID.randomUUID().toString() + ".jpg");
                                    return Observable.just(new FileDownload(fileName, url));
                                } catch (NetException e) {
                                    return Observable.error(e);
                                }
                            }
                        });
            }
        };
    }

    public ObservableTransformer<FileDownload, FileDownload> applyDownloadFile() {
        return new ObservableTransformer<FileDownload, FileDownload>() {
            @Override
            public ObservableSource<FileDownload> apply(Observable<FileDownload> upstream) {
                return upstream
                        .flatMap(new Function<FileDownload, ObservableSource<FileDownload>>() {
                            @Override
                            public ObservableSource<FileDownload> apply(FileDownload fileDownload) {
                                try {
                                    OkHttpClient.Builder builder = OkHttp3Utils.get().cloneClient();
                                    builder.readTimeout(2, TimeUnit.MINUTES);
                                    Request request = new Request.Builder()
                                            .url(fileDownload.getUrl()).build();
                                    Response response = builder.build().newCall(request).execute();
                                    if (!response.isSuccessful()) {
                                        return Observable.error(new IOException("Response error"));
                                    }
                                    ResponseBody body = response.body();
                                    if (body == null) {
                                        return Observable.error(new IOException("Response body is null"));
                                    }
                                    return Observable
                                            .just(new FileDownload(fileDownload.getFileName(),
                                                    body.byteStream()));
                                } catch (IOException e) {
                                    return Observable.error(e);
                                }
                            }
                        });
            }
        };
    }

    public Disposable downloadFile(String url, final File dir,
            Callback<FileDownload> callback) {
        Observable<FileDownload> observable = Observable.just(url).subscribeOn(Schedulers.io())
                .compose(applyGetFileName())
                .compose(applyDownloadFile()).flatMap(new Function<FileDownload, ObservableSource<FileDownload>>() {
                    @Override
                    public ObservableSource<FileDownload> apply(FileDownload fileName) {
                        try {
                            File file = FileUtils.createFile(dir, fileName.getFileName());
                            FileUtils.copyInputStreamToFile(fileName.getInputStream(), file);
                            return Observable.just(new FileDownload(file));
                        } catch (IOException e) {
                            return Observable.error(e);
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
