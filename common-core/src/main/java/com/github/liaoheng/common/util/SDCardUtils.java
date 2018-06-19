package com.github.liaoheng.common.util;

import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * 外部储存相关的辅助类
 * <br/> Dependency : android.permission.READ_EXTERNAL_STORAGE
 * <br/> Dependency : android.permission.WRITE_EXTERNAL_STORAGE
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2014-11-03
 * @author <a href="http://www.cnblogs.com/smiler/p/3854761.html" target="_blank">幕三少</a>
 */
public class SDCardUtils {
    private static final String TAG = SDCardUtils.class.getSimpleName();

    @Deprecated
    public static boolean isSDCardEnable() {
        return isExternalStorageEnable();
    }

    @Deprecated
    public static File getSDCardPath() {
        return getExternalStorageDirectory();
    }

    /**
     * 判断外部储存是否可用
     */
    public static boolean isExternalStorageEnable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 获取外部储存根目录，/storage/emulated/0 or /sdcard
     */
    public static File getExternalStorageDirectory() {
        return Environment.getExternalStorageDirectory();
    }

    /**
     * 获取外部储存系统音乐目录，/storage/emulated/0/Music
     */
    public static File getExternalStorageMusicPublicDirectory() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
    }

    /**
     * 获取外部储存系统相册目录，/storage/emulated/0/Pictures
     */
    public static File getExternalStoragePicturesPublicDirectory() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    }

    /**
     * 获取外部储存系统视频目录，/storage/emulated/0/Movies
     */
    public static File getExternalStorageMoviesPublicDirectory() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
    }

    /**
     * 获取外部储存系统下载目录，/storage/emulated/0/Download
     */
    public static File getExternalStorageDownloadPublicDirectory() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    }

    /**
     * 获取外部储存系统相机目录，/storage/emulated/0/DCIM
     */
    public static File getExternalStorageDCIMPublicDirectory() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
    }

    /**
     * 获取外部储存系统文档目录，/storage/emulated/0/Documents
     */
    public static File getExternalStorageDocumentsPublicDirectory() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
    }

    /**
     * Get SD card path by CMD.
     */
    public static String getSDCardPathCMD() {
        String cmd = "cat /proc/mounts";
        String sdcard = null;
        Runtime run = Runtime.getRuntime();// 返回与当前 Java 应用程序相关的运行时对象
        BufferedReader bufferedReader = null;
        try {
            Process p = run.exec(cmd);// 启动另一个进程来执行命令
            bufferedReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(
                    p.getInputStream())));
            String lineStr;
            while ((lineStr = bufferedReader.readLine()) != null) {
                L.Log.d(TAG, "proc/mounts:   " + lineStr);
                if (lineStr.contains("sdcard") && lineStr.contains(".android_secure")) {
                    String[] strArray = lineStr.split(" ");
                    if (strArray.length >= 5) {
                        sdcard = strArray[1].replace("/.android_secure", "");
                        L.Log.d(TAG, "find sd card path:   " + sdcard);
                        return sdcard;
                    }
                }
                if (p.waitFor() != 0 && p.exitValue() == 1) {
                    // p.exitValue()==0表示正常结束，1：非正常结束
                    L.Log.e(TAG, cmd + " 命令执行失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(bufferedReader);
        }
        sdcard = Environment.getExternalStorageDirectory().getPath();
        L.Log.d(TAG, "not find sd card path return default:   " + sdcard);
        return sdcard;
    }

    /**
     * Get SD card path list.
     */
    public static ArrayList<String> getSDCardPathEx() {
        ArrayList<String> list = new ArrayList<String>();
        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            String line;
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                L.Log.d(TAG, "mount:  " + line);
                if (line.contains("secure")) {
                    continue;
                }
                if (line.contains("asec")) {
                    continue;
                }

                if (line.contains("fat")) {
                    String columns[] = line.split(" ");
                    if (columns.length > 1) {
                        list.add("*" + columns[1]);
                    }
                } else if (line.contains("fuse")) {
                    String columns[] = line.split(" ");
                    if (columns.length > 1) {
                        list.add(columns[1]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获得sd卡剩余容量，即可以大小
     *
     * @return byte
     */
    public static long getSDAvailableSize() {
        StatFs statFs = new StatFs(getSDCardPath().getAbsolutePath());
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
