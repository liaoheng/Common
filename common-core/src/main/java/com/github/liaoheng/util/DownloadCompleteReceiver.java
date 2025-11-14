package com.github.liaoheng.util;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.text.TextUtils;

import androidx.core.content.ContextCompat;

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

    private String mDownloadCompleteHit;

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

    public void setDownloadCompleteHit(String completeHit) {
        mDownloadCompleteHit = completeHit;
    }

    public void registerReceiver(Context context) {
        register = true;
        ContextCompat.registerReceiver(context, this, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
                ContextCompat.RECEIVER_NOT_EXPORTED);
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
                L.d(TAG, "Download Complete");
                UIUtils.showToast(context, mDownloadCompleteHit);
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
                L.w(TAG, e.getMessage());
                UIUtils.showToast(context, "Not Open File");
            }

            mDownloadedFileID = -1;
        }
    }

}
