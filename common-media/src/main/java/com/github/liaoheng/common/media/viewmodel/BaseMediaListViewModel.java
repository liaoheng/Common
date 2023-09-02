package com.github.liaoheng.common.media.viewmodel;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.media.MediaBrowserCompat;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.github.liaoheng.common.livedata.UpdateListLiveData;
import com.github.liaoheng.common.util.DelayHandler;
import com.github.liaoheng.common.util.L;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseMediaListViewModel extends BaseMediaViewModel {
    private final UpdateListLiveData<MediaBrowserCompat.MediaItem> mMediaItemsLiveData = new UpdateListLiveData<>(
            new ArrayList<>());

    private final MutableLiveData<String> mNotifyLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<MediaBrowserCompat.MediaItem>> mLoadedLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> mMediaScanStateLiveData = new MutableLiveData<>();

    public BaseMediaListViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<String> monitorNotify() {
        return mNotifyLiveData;
    }

    public MutableLiveData<List<MediaBrowserCompat.MediaItem>> monitorLoaded() {
        return mLoadedLiveData;
    }

    public UpdateListLiveData<MediaBrowserCompat.MediaItem> monitorMediaItems() {
        return mMediaItemsLiveData;
    }

    public MutableLiveData<Integer> monitorMediaScanState() {
        return mMediaScanStateLiveData;
    }

    private DelayHandler mHandler;
    private static final int MSG_NOTIFY_LIST_MODE = 10;
    private static final int MSG_UPDATE_LIST = 20;
    private static final int MSG_USB_HAVE_MEDIA = 30;
    private String lastBucketID;

    public void setLastBucketId(String bucketId) {
        lastBucketID = bucketId;
    }

    public String getLastBucketId() {
        return lastBucketID;
    }

    @Override
    public void init(Context context) {
        super.init(context);
        mHandler = new DelayHandler(Looper.getMainLooper(), mHandlerCallback);
    }

    public DelayHandler getHandler() {
        return mHandler;
    }

    protected boolean handleMessage(Message msg) {return false;}

    private final Handler.Callback mHandlerCallback = msg -> {
        if (msg.what == MSG_NOTIFY_LIST_MODE) {
            if (msg.obj == null) {
                return false;
            }
            L.alog().d(TAG, "updateListModeDelayed#mode: " + msg.obj + " #page: " + msg.arg1);

        } else if (msg.what == MSG_UPDATE_LIST) {
            List<MediaBrowserCompat.MediaItem> mediaItems = (List<MediaBrowserCompat.MediaItem>) msg.obj;
            if (mediaItems.isEmpty()) {
                monitorMediaItems().clear();
            } else {
                monitorMediaItems().clearNoNotify();
                monitorMediaItems().addAll(mediaItems);
            }
        } else if (msg.what == MSG_USB_HAVE_MEDIA) {
            Integer state = (Integer) msg.obj;
            L.d(TAG, "mediaScanState: " + state);
            monitorMediaScanState().postValue(state);
        } else {
            handleMessage(msg);
        }
        return false;
    };

    protected void updateList(List<MediaBrowserCompat.MediaItem> mediaItems, int delayMillis) {
        getHandler().sendDelayed(MSG_UPDATE_LIST, mediaItems, delayMillis);
    }

    protected void clearList() {
        getHandler().removeMessages(MSG_UPDATE_LIST);
    }

    public boolean isLoad() {
        return mHandler.hasMessages(MSG_NOTIFY_LIST_MODE);
    }

    public void release() {
        mHandler.removeMessages(MSG_NOTIFY_LIST_MODE);
        super.release();
    }
}
