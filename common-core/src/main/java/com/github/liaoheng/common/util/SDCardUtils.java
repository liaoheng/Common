package com.github.liaoheng.common.util;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * 外部储存相关的辅助类
 * <br/> Dependency : android.permission.READ_EXTERNAL_STORAGE
 * <br/> Dependency : android.permission.WRITE_EXTERNAL_STORAGE
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2014-11-03
 * @author <a href="http://www.cnblogs.com/smiler/p/3854761.html" target="_blank">幕三少</a>
 */
public class SDCardUtils {

    /**
     * 判断外部储存是否可用
     */
    public static boolean isExternalStorageEnable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 获取外部储存根目录，/storage/emulated/0 or /sdcard
     */
    public static File getExternalStorageDirectory(Context context) {
        return context.getExternalFilesDir(null);
    }

    /**
     * 获取外部储存系统音乐目录，/storage/emulated/0/Music
     */
    public static File getExternalStorageMusicPublicDirectory(Context context) {
        return context.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
    }

    /**
     * 获取外部储存系统相册目录，/storage/emulated/0/Pictures
     */
    public static File getExternalStoragePicturesPublicDirectory(Context context) {
        return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    }

    /**
     * 获取外部储存系统视频目录，/storage/emulated/0/Movies
     */
    public static File getExternalStorageMoviesPublicDirectory(Context context) {
        return context.getExternalFilesDir(Environment.DIRECTORY_MOVIES);
    }

    /**
     * 获取外部储存系统下载目录，/storage/emulated/0/Download
     */
    public static File getExternalStorageDownloadPublicDirectory(Context context) {
        return context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
    }

    /**
     * 获取外部储存系统相机目录，/storage/emulated/0/DCIM
     */
    public static File getExternalStorageDCIMPublicDirectory(Context context) {
        return context.getExternalFilesDir(Environment.DIRECTORY_DCIM);
    }

    /**
     * 获取外部储存系统文档目录，/storage/emulated/0/Documents
     */
    public static File getExternalStorageDocumentsPublicDirectory(Context context) {
        return context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
    }

    /**
     * 获得path剩余容量
     */
    public static long getPathAvailableSize(File path) {
        StatFs statFs = new StatFs(path.getAbsolutePath());
        long blockSize;
        long availableBlocks;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = statFs.getBlockSizeLong();
            availableBlocks = statFs.getAvailableBlocksLong();
        } else {
            blockSize = statFs.getBlockSize();
            availableBlocks = statFs.getAvailableBlocks();
        }
        return blockSize * availableBlocks;
    }

    /**
     * 获得机身可用内存
     *
     * @return byte
     */
    public static long getROMAvailableSize() {
        File path = Environment.getDataDirectory();
        StatFs statFs = new StatFs(path.getPath());
        long blockSize;
        long availableBlocks;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = statFs.getBlockSizeLong();
            availableBlocks = statFs.getAvailableBlocksLong();
        } else {
            blockSize = statFs.getBlockSize();
            availableBlocks = statFs.getAvailableBlocks();
        }
        return blockSize * availableBlocks;
    }

}
