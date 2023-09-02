package com.github.liaoheng.common.media.util;

import android.support.v4.media.MediaMetadataCompat;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.widget.TextView;

/**
 * @author liaoheng
 * @date 2023-09-02 16:29
 */
public class Utils {

    /**
     * 获取音乐标题
     */
    public static CharSequence getMusicSubTitle(CharSequence title) {
        if (TextUtils.isEmpty(title) || "<unknown>".contentEquals(title)) {
            return "";
        }
        return title;
    }

    /**
     * 获取蓝牙音乐标题
     */
    public static CharSequence getBtMusicTitle(CharSequence title) {
        if (TextUtils.isEmpty(title) || "unknow".contentEquals(title)) {
            return "";
        }
        return title;
    }

    public static CharSequence getPosition(long progress) {
        return DateUtils.formatElapsedTime(progress / 1000);
    }

    public static CharSequence getDuration(long duration) {
        return DateUtils.formatElapsedTime(duration / 1000);
    }

    public static String toMediaMetadata(MediaMetadataCompat metadata) {
        return (metadata == null ? "null"
                : metadata.getDescription() + " > " + "id: " + metadata.getDescription().getMediaId());
    }

    public static void setText(TextView view, CharSequence content, int def) {
        if (view == null) {
            return;
        }
        if (TextUtils.isEmpty(content)) {
            view.setText(def);
        } else {
            view.setText(content);
        }
    }

}
