package com.leng.common.plus.view;

import java.util.Map;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.Gravity;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.leng.common.plus.core.ProgressHelper;
import com.leng.common.util.L;
import com.leng.common.util.UIUtils;
import com.leng.common.util.Utils;

/**
 * WebView
 * @author liaoheng
 * @version 2015年9月15日
 */
public class WebViewLayout extends FrameLayout {

    private final String       TAG                      = WebViewLayout.class.getSimpleName();
    public static final int    FILE_CHOOSER_RESULT_CODE = 9801;
    private ProgressHelper     mProgressUtils;
    private WebView            mWebView;
    private CPDownloadListener mDownloadListener;
    private CPWebChromeClient  mWebChromeClient;

    public WebViewLayout(Context context) {
        this(context, null);
    }

    public WebViewLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WebViewLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WebViewLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public ProgressHelper getProgressUtils() {
        return mProgressUtils;
    }

    public void loadUrl(String url) {
        L.Log.d(TAG, "load url : " + url);
        mWebView.loadUrl(url);
    }

    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        L.Log.d(TAG, "load url : %s ,headers : %s", url, additionalHttpHeaders);
        mWebView.loadUrl(url, additionalHttpHeaders);
    }

    public void pause() {
        mWebView.onPause();
    }

    public void resume() {
        mWebView.onResume();
    }

    public void destroy() {
        mFilePathCallback = null;
        mWebView.destroy();
    }

    public WebView getWebView() {
        return mWebView;
    }

    public boolean backPressed() {
        if (getWebView().canGoBack()) {
            getWebView().goBack();
            return false;
        }
        return true;
    }

    @SuppressLint({ "SetJavaScriptEnabled" })
    private void init() {
        mWebView = new WebView(getContext());
        FrameLayout.LayoutParams wParams = new FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        addView(mWebView, wParams);

        mProgressUtils =new ProgressHelper(new ProgressBar(getContext()));
        FrameLayout.LayoutParams bParams = new FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        bParams.gravity = Gravity.CENTER;
        addView(mProgressUtils.getProgressBar(), bParams);

        if (isInEditMode()) {//编辑器查看时不初始化数据
            return;
        }

        WebSettings settings = mWebView.getSettings();
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setAllowFileAccess(true);
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        mWebChromeClient = new CPWebChromeClient(getContext());
        mWebView.setWebChromeClient(mWebChromeClient);

        mWebView.setWebViewClient(new CPWebViewClient(getProgressUtils()));

        mDownloadListener = new CPDownloadListener();
        mWebView.setDownloadListener(mDownloadListener);
    }

    public void setUserAgent(String userAgent) {
        WebSettings settings = mWebView.getSettings();
        String agent = settings.getUserAgentString();
        settings.setUserAgentString(agent + " " + userAgent);
    }

    public void setWebViewClient(WebViewClient webViewClient) {
        mWebView.setWebViewClient(webViewClient);
    }

    public CPWebChromeClient getWebChromeClient() {
        return mWebChromeClient;
    }

    public void setWebChromeClient(CPWebChromeClient webChromeClient) {
        this.mWebChromeClient = webChromeClient;
        mWebView.setWebChromeClient(webChromeClient);
    }

    public CPDownloadListener getDownloadListener() {
        return mDownloadListener;
    }

    public void setDownloadListener(CPDownloadListener downloadListener) {
        this.mDownloadListener = downloadListener;
        mWebView.setDownloadListener(downloadListener);
    }

    private class CPDownloadListener implements DownloadListener {
        private String downloadFilePath;

        public CPDownloadListener() {
            downloadFilePath = Environment.DIRECTORY_DOWNLOADS;
        }

        public CPDownloadListener(String downloadFilePath) {
            this.downloadFilePath = downloadFilePath;
        }

        public String getDownloadFilePath() {
            return downloadFilePath;
        }

        public void setDownloadFilePath(String downloadFilePath) {
            this.downloadFilePath = downloadFilePath;
        }

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition,
                                    String mimetype, long contentLength) {
            String fileName = Utils.getContentDispositionFileName(contentDisposition, "noname");
            UIUtils.showToast(getContext(), "文件正在下载到SD卡" + downloadFilePath + "目录中");
            DownloadManager downloadManager = (DownloadManager) getContext()
                .getSystemService(Context.DOWNLOAD_SERVICE);

            Uri uri = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setDestinationInExternalPublicDir(downloadFilePath, fileName); //表示设置下载地址为sd卡的
            request.setNotificationVisibility(
                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            downloadManager.enqueue(request);
        }
    }

    private static ValueCallback mFilePathCallback;

    public static ValueCallback getFilePathCallback() {
        return mFilePathCallback;
    }

    public static void setFilePathCallback(ValueCallback filePathCallback) {
        mFilePathCallback = filePathCallback;
    }

    public static class CPWebChromeClient extends WebChromeClient {
        private Object context;

        public CPWebChromeClient(Object context) {
            validate(context);
            this.context = context;
        }

        private void validate(Object context) {
            if (!(context instanceof Activity) && !(context instanceof Fragment)
                && !(context instanceof android.app.Fragment)) {
                throw new IllegalArgumentException("Object is not activity or fragment");
            }
        }

        public void setContext(Object context) {
            validate(context);
            this.context = context;
        }

        private Context getContext() {
            if (context instanceof Activity) {
                return (Activity) context;
            } else if (context instanceof Fragment) {
                return ((Fragment) context).getActivity();
            } else if (context instanceof android.app.Fragment) {
                return ((android.app.Fragment) context).getActivity();
            } else {
                return null;
            }
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            new AlertDialog.Builder(getContext()).setMessage(message)
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        result.cancel();
                    }
                }).create().show();
            return true;
        }

        //For Android 5.0+
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
                                         FileChooserParams fileChooserParams) {
            setFilePathCallback(filePathCallback);
            open();
            return true;
        }

        private void open() {
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");

            if (context instanceof Activity) {
                ((Activity) context).startActivityForResult(Intent.createChooser(i, "Select file"),
                    WebViewLayout.FILE_CHOOSER_RESULT_CODE);
            } else if (context instanceof Fragment) {
                ((Fragment) context).startActivityForResult(Intent.createChooser(i, "Select file"),
                    WebViewLayout.FILE_CHOOSER_RESULT_CODE);
            } else if (context instanceof android.app.Fragment) {
                ((android.app.Fragment) context).startActivityForResult(
                    Intent.createChooser(i, "Select file"), WebViewLayout.FILE_CHOOSER_RESULT_CODE);
            }
        }

        // For Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType) {
            setFilePathCallback(uploadFile);
            open();
        }

        // For Android < 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            openFileChooser(uploadMsg, "");
        }

        // For Android > 4.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType,
                                    String capture) {
            openFileChooser(uploadMsg, "");
        }

    }

    @SuppressWarnings("unchecked")
    public static void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == WebViewLayout.FILE_CHOOSER_RESULT_CODE) {
            Uri result = resultCode == Activity.RESULT_OK ? intent.getData() : null;
            ValueCallback filePathCallback = WebViewLayout.getFilePathCallback();
            if (result == null) {
                filePathCallback.onReceiveValue(null);
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Uri[] x = { result };
                filePathCallback.onReceiveValue(x);
            } else {
                filePathCallback.onReceiveValue(result);
            }
        }
    }

    public static class CPWebViewClient extends WebViewClient {
        private ProgressHelper progressHelper;

        public CPWebViewClient(ProgressHelper progressHelper) {
            this.progressHelper = progressHelper;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (progressHelper != null)
                progressHelper.visible();
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (progressHelper != null)
                progressHelper.gone();
            super.onPageFinished(view, url);
        }
    }

}
