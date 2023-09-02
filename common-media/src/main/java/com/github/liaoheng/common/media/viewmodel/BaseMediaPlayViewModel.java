package com.github.liaoheng.common.media.viewmodel;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.github.liaoheng.common.media.util.Utils;
import com.github.liaoheng.common.util.DelayHandler;
import com.github.liaoheng.common.util.L;

public abstract class BaseMediaPlayViewModel extends BaseMediaViewModel {
    private final MutableLiveData<MediaMetadataCompat> mMetadataLiveData = new MutableLiveData<>();
    private final MutableLiveData<PlaybackStateCompat> mPlaybackStateLiveData = new MutableLiveData<>();
    private final MutableLiveData<Bitmap> mAlbumCoverLiveData = new MutableLiveData<>();
    private final MutableLiveData<Long> mPlayProgressLiveData = new MutableLiveData<>();
    private final MutableLiveData<Long> mPlayDurationLiveData = new MutableLiveData<>();

    public BaseMediaPlayViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<MediaMetadataCompat> monitorMetadata() {
        return mMetadataLiveData;
    }

    public MutableLiveData<PlaybackStateCompat> monitorPlaybackState() {
        return mPlaybackStateLiveData;
    }

    public MutableLiveData<Bitmap> monitorAlbumCover() {
        return mAlbumCoverLiveData;
    }

    public MutableLiveData<Long> monitorPlayProgress() {
        return mPlayProgressLiveData;
    }

    public MutableLiveData<Long> monitorPlayDuration() {
        return mPlayDurationLiveData;
    }

    private HandlerThread mHandlerThread;
    private DelayHandler mThreadHandler;

    protected static final int MSG_UPDATE_ALBUM_COVER = 1;
    protected static final int MSG_CLEAR_METADATA = 2;
    protected static final int MSG_UPDATE_METADATA = 3;
    protected static final int MSG_UPDATE_PLAYBACK_STATE = 4;

    public DelayHandler getThreadHandler() {
        return mThreadHandler;
    }

    @Override
    public void init(Context context) {
        super.init(context);
        mHandlerThread = new HandlerThread(TAG);
        mHandlerThread.start();
        mThreadHandler = new DelayHandler(mHandlerThread.getLooper(), mThreadHandlerCallback);
    }

    private final Handler.Callback mThreadHandlerCallback = message -> {
        if (message.what == MSG_UPDATE_ALBUM_COVER) {
            monitorAlbumCover().postValue(getAlbumCover((MediaMetadataCompat) message.obj));
        } else if (message.what == MSG_CLEAR_METADATA) {
            L.alog().i(TAG, "updateMetaData: null");
            clearMetaData();
            monitorMetadata().postValue(null);
            monitorAlbumCover().postValue(null);
        } else if (message.what == MSG_UPDATE_METADATA) {
            updateMetaData((MediaMetadataCompat) message.obj);
        } else if (message.what == MSG_UPDATE_PLAYBACK_STATE) {
            PlaybackStateCompat state = (PlaybackStateCompat) message.obj;
            L.alog().i(TAG, "updatePlaybackState: " + state);
            if (state != null && state.getState() == PlaybackStateCompat.STATE_PLAYING) {
                setUsePaused(false);
            }
            updatePlaybackState(state);
        } else {
            onThreadHandlerCallback(message);
        }
        return false;
    };

    protected void onThreadHandlerCallback(Message message) {

    }

    public void clearMetaData() {
    }

    protected boolean checkMetadata(MediaMetadataCompat metadata) {
        return false;
    }

    protected void updateMetaData(MediaMetadataCompat metadata) {
        if (checkMetadata(metadata)) {
            L.i(TAG, "updateMetaData#repeat: " + Utils.toMediaMetadata(metadata));
            return;
        }
        L.i(TAG, "updateMetaData: " + Utils.toMediaMetadata(metadata));
        monitorMetadata().postValue(metadata);
        updateAlbumCover(metadata);
        updatePlayDuration(metadata);
    }

    protected void updatePlaybackState(PlaybackStateCompat state) {
        monitorPlaybackState().postValue(state);
    }

    protected void updateAlbumCover(MediaMetadataCompat metadata) {
        mThreadHandler.sendDelayed(MSG_UPDATE_ALBUM_COVER, metadata, 500);
    }

    public Bitmap getAlbumCover(MediaMetadataCompat metadata) {
        return null;
    }

    protected void updatePlayDuration(MediaMetadataCompat metadata) {
        if (metadata == null) {
            return;
        }
        long duration = metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION);
        Long du = monitorPlayDuration().getValue();
        if (du != null && duration == du) {
            return;
        }
        L.i(TAG, "updatePlayDuration:" + duration);
        monitorPlayDuration().postValue(duration);
    }

    public boolean loadInfo() {
        if (isDisconnect()) {
            return false;
        }
        metadataChanged(getMetadata(), 200);
        playbackStateChanged(getPlaybackState(), 200);
        return true;
    }

    protected void metadataChanged(MediaMetadataCompat metadata, long delayMillis) {
        mThreadHandler.removeMessages(MSG_CLEAR_METADATA);
        if (metadata == null || metadata.getLong("is_null") == 1) {
            mThreadHandler.sendDelayed(MSG_CLEAR_METADATA, delayMillis);
            return;
        }
        mThreadHandler.sendDelayed(MSG_UPDATE_METADATA, metadata, delayMillis);
    }

    protected void playbackStateChanged(PlaybackStateCompat state, long delayMillis) {
        mThreadHandler.sendDelayed(MSG_UPDATE_PLAYBACK_STATE, state, delayMillis);
    }

    @Override
    public void release() {
        mThreadHandler.removeMessages(MSG_UPDATE_ALBUM_COVER);
        mThreadHandler.removeMessages(MSG_CLEAR_METADATA);
        mThreadHandler.removeMessages(MSG_UPDATE_METADATA);
        mThreadHandler.removeMessages(MSG_UPDATE_PLAYBACK_STATE);
        mHandlerThread.quit();
        super.release();
    }

    public void stopPlayProgress(){}

    public MediaMetadataCompat getMetadata() {
        if (isDisconnect()) {
            return null;
        }
        return internalGetMetadata();
    }

    protected MediaMetadataCompat internalGetMetadata() {return null;}

    public PlaybackStateCompat getPlaybackState() {
        if (isDisconnect()) {
            return null;
        }
        return internalGetPlaybackState();
    }

    public PlaybackStateCompat internalGetPlaybackState() {return null;}

    public boolean isUsePaused() {return false;}

    public void setUsePaused(boolean usePaused) {}

    public void play() {
        if (isDisconnect()) {
            return;
        }
        internalPlay();
    }

    public void internalPlay() {}

    public void pause() {
        if (isDisconnect()) {
            return;
        }
        internalPause();
    }

    public void internalPause() {}

    public void prepare() {
        if (isDisconnect()) {
            return;
        }
        internalPrepare();
    }

    public void internalPrepare() {}

    public boolean isPlaying() {
        if (isDisconnect()) {
            return false;
        }
        return internalIsPlaying();
    }

    public boolean internalIsPlaying() {return false;}

    public void skipToNext() {
        if (isDisconnect()) {
            return;
        }
        internalSkipToNext();
    }

    public void internalSkipToNext() {}

    public void skipToPrevious() {
        if (isDisconnect()) {
            return;
        }
        internalSkipToPrevious();
    }

    public void internalSkipToPrevious() {}

    public void seekTo(int progress) {
        if (isDisconnect()) {
            return;
        }
        internalSeekTo(progress);
    }

    public void internalSeekTo(int progress) {}
}
