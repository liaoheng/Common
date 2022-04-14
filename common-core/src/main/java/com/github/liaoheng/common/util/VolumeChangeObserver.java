package com.github.liaoheng.common.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;

import java.lang.ref.WeakReference;

/**
 * 监控系统音量
 *
 * @author liaoheng
 * @date 2021-11-17 16:59
 */
public class VolumeChangeObserver {
    private static final String VOLUME_CHANGED_ACTION = "android.media.VOLUME_CHANGED_ACTION";
    private static final String MASTER_MUTE_CHANGED_ACTION = "android.media.MASTER_MUTE_CHANGED_ACTION";
    private static final String EXTRA_VOLUME_STREAM_TYPE = "android.media.EXTRA_VOLUME_STREAM_TYPE";
    private static final String EXTRA_MASTER_VOLUME_MUTED = "android.media.EXTRA_MASTER_VOLUME_MUTED";

    public interface VolumeChangeListener {
        /**
         * 系统媒体音量变化
         *
         * @param volume
         */
        void onVolumeChanged(int volume);
    }

    private VolumeChangeListener mVolumeChangeListener;
    private VolumeBroadcastReceiver mVolumeBroadcastReceiver;
    private final AudioManager mAudioManager;
    private boolean mRegistered = false;

    public VolumeChangeObserver(Context context) {
        mAudioManager = (AudioManager) context.getApplicationContext()
                .getSystemService(Context.AUDIO_SERVICE);
    }

    /**
     * 获取当前媒体音量
     *
     * @return
     */
    public int getCurrentMusicVolume() {
        return mAudioManager != null ? mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) : -1;
    }

    /**
     * 获取系统最大媒体音量
     *
     * @return
     */
    public int getMaxMusicVolume() {
        return mAudioManager != null ? mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) : 15;
    }

    public VolumeChangeListener getVolumeChangeListener() {
        return mVolumeChangeListener;
    }

    public void setVolumeChangeListener(VolumeChangeListener volumeChangeListener) {
        this.mVolumeChangeListener = volumeChangeListener;
    }

    /**
     * 注册音量广播接收器
     *
     * @return
     */
    public void registerReceiver(Context context) {
        mVolumeBroadcastReceiver = new VolumeBroadcastReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(VOLUME_CHANGED_ACTION);
        filter.addAction(MASTER_MUTE_CHANGED_ACTION);
        context.registerReceiver(mVolumeBroadcastReceiver, filter);
        if (mVolumeChangeListener != null) {
            mVolumeChangeListener.onVolumeChanged(getCurrentMusicVolume());
        }
        mRegistered = true;
    }

    /**
     * 解注册音量广播监听器，需要与 registerReceiver 成对使用
     */
    public void unregisterReceiver(Context context) {
        if (mRegistered) {
            try {
                context.unregisterReceiver(mVolumeBroadcastReceiver);
                mVolumeChangeListener = null;
                mRegistered = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static class VolumeBroadcastReceiver extends BroadcastReceiver {
        private WeakReference<VolumeChangeObserver> mObserverWeakReference;

        public VolumeBroadcastReceiver(VolumeChangeObserver volumeChangeObserver) {
            mObserverWeakReference = new WeakReference<>(volumeChangeObserver);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            //媒体音量改变才通知
            if (VOLUME_CHANGED_ACTION.equals(intent.getAction())
                    && (intent.getIntExtra(EXTRA_VOLUME_STREAM_TYPE, -1) == AudioManager.STREAM_MUSIC)) {
                VolumeChangeObserver observer = mObserverWeakReference.get();
                if (observer != null) {
                    VolumeChangeListener listener = observer.getVolumeChangeListener();
                    if (listener != null) {
                        int volume = observer.getCurrentMusicVolume();
                        L.alog().d("VolumeBroadcastReceiver", "onReceive#VOLUME_CHANGED_ACTION: " + volume);
                        listener.onVolumeChanged(volume);
                    }
                }
            } else if (MASTER_MUTE_CHANGED_ACTION.equals(intent.getAction())) {
                VolumeChangeObserver observer = mObserverWeakReference.get();
                if (observer != null) {
                    VolumeChangeListener listener = observer.getVolumeChangeListener();
                    if (listener != null) {
                        boolean mute = intent.getBooleanExtra(EXTRA_MASTER_VOLUME_MUTED, false);
                        L.alog().d("VolumeBroadcastReceiver", "onReceive#MASTER_MUTE_CHANGED_ACTION#mute=" + mute);
                        listener.onVolumeChanged(mute ? 0 : 999);
                    }
                }
            }
        }
    }
}
