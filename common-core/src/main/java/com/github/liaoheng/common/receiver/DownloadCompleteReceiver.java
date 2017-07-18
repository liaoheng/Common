package com.github.liaoheng.common.receiver;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.text.TextUtils;

import com.github.liaoheng.common.R;
import com.github.liaoheng.common.util.L;
import com.github.liaoheng.common.util.UIUtils;

/**
 * System Download Management
 *
 * @author liaoheng
 * @version 2016-06-26 14:08
 */
public class DownloadCompleteReceiver extends BroadcastReceiver {
    private final String TAG = DownloadCompleteReceiver.class.getSimpleName();
    private long mDownloadedFileID;

    public DownloadCompleteReceiver(long downloadedFileID) {
        this.mDownloadedFileID = downloadedFileID;
    }

    private String mimeType;

    private boolean open = true;

    private boolean register;

    public boolean isRegister() {
        return register;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public void registerReceiver(Context context) {
        register = true;
        context.registerReceiver(this, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public void unregisterReceiver(Context context) {
        register = false;
        context.unregisterReceiver(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            if (mDownloadedFileID == -1) {
                return;
            }
            if (!isOpen()) {
                mDownloadedFileID = -1;
                L.Log.d(TAG, "Download Complete");
                UIUtils.showToast(context, context.getString(R.string.lcm_download_complete));
                return;
            }
            DownloadManager downloadManager = (DownloadManager) context
                    .getSystemService(Context.DOWNLOAD_SERVICE);
            // Grabs the Uri for the file that was downloaded.
            Uri mostRecentDownload = downloadManager.getUriForDownloadedFile(mDownloadedFileID);
            if (TextUtils.isEmpty(getMimeType())) {
                // DownloadManager stores the Mime Type. Makes it really easy for us.
                mimeType = downloadManager.getMimeTypeForDownloadedFile(mDownloadedFileID);
            }
            downloadManager.remove(mDownloadedFileID);

            Intent fileIntent = new Intent(Intent.ACTION_VIEW);
            fileIntent.setDataAndType(mostRecentDownload, mimeType);
            fileIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                context.startActivity(fileIntent);
            } catch (ActivityNotFoundException e) {
                L.Log.w(TAG, e.getMessage());
                UIUtils.showToast(context, "Not Open File");
            }

            mDownloadedFileID = -1;
        }
    }

}
