package com.github.liaoheng.common.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.github.liaoheng.common.Common;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件工具
 *
 * @author liaoheng
 */
public class FileUtils extends org.apache.commons.io.FileUtils {
    private static final String TAG = FileUtils.class.getSimpleName();

    /**
     * 创建临时文件
     *
     * @param path
     * @param fileName 文件名
     * @return 
     * @throws SystemException
     */
    public static File createFile(File path, String fileName) throws SystemException {
        try {
            if (!path.exists()) {
                createPath(path);
            }
            File tempPath = new File(path, fileName);
            if (!tempPath.exists()) {
                if (!tempPath.createNewFile()) {
                    throw new IOException(tempPath.getAbsolutePath());
                } else {
                    L.Log.d(TAG, "create file :" + tempPath.getAbsolutePath());
                }
            }
            return tempPath;
        } catch (IOException e) {
            throw new SystemException("文件创建失败！", e);
        }
    }

    /**
     * 创建临时文件
     *
     * @param path
     * @param nameEx 文件后缀
     * @return {@link UUID#randomUUID()}
     * @throws SystemException
     */
    public static File createTempFile(File path, String nameEx) throws SystemException {
        try {
            if (!path.exists()) {
                createPath(path);
            }
            File tempPath = new File(path, UUID.randomUUID().toString() + nameEx);
            if (!tempPath.exists()) {
                if (!tempPath.createNewFile()) {
                    throw new IOException(tempPath.getAbsolutePath());
                } else {
                    L.Log.d(TAG, "create file :" + tempPath.getAbsolutePath());
                }
            }
            return tempPath;
        } catch (IOException e) {
            throw new SystemException("临时文件创建失败！", e);
        }
    }

    /**
     * 创建临时文件
     *
     * @param path
     * @param nameEx 文件后缀
     * @return
     * @throws SystemException
     */
    public static File createTempFile(String path, String nameEx) throws SystemException {
        return createTempFile(new File(path), nameEx);
    }

    /**
     * 得到临时目录
     *
     * @return
     * @throws SystemException
     */
    public static File getProjectTempDirectory() throws SystemException {
        return createProjectSDExternalPath("temp");
    }

    /**
     * 得到保存图片目录
     *
     * @return
     * @throws SystemException
     */
    public static File getProjectImageDirectory() throws SystemException {
        return createProjectSDPath("images");
    }

    /**
     * 在SD卡项目名下创建目录
     * @param ptah 路径
     * @return {@link File}
     * @throws SystemException
     */
    public static File createProjectSDPath(String ptah) throws SystemException {
        return createPath(getProjectSDPath(ptah));
    }

    /**
     * 在SD  {@link Context#getExternalCacheDir()} 项目名下创建目录
     * @param ptah 路径
     * @return {@link File}
     * @throws SystemException
     */
    public static File createProjectSDExternalPath(String ptah) throws SystemException {
        File sdAndroidPath = Common.getSDExternalPath();
        return createPath(sdAndroidPath.getAbsolutePath(), ptah);
    }

    public static File getSDExternalPath(@NonNull Context context) {
        File externalCacheDir = context.getExternalCacheDir();
        if (externalCacheDir == null) {
            return context.getCacheDir();
        }
        return externalCacheDir.getParentFile();
    }

    /**
     * 得到项目名下路径
     * @param ptah 路径
     * @return {@link File}
     * @throws SystemException
     */
    public static File getProjectSDPath(String ptah) throws SystemException {
        File root = new File(getSDPath(), Common.getProjectName());
        return new File(root, ptah);
    }

    /**
     * 在SD卡上创建路径
     * @param rootPath 父路径
     * @param path 建立的路径
     * @return  {@link File}
     * @throws SystemException
     */
    public static File createSDPath(String rootPath, String path) throws SystemException {
        String temp = getSDPath().getAbsolutePath() + File.separator + rootPath;
        return createSDPath(new File(temp, path).getAbsolutePath());
    }

    /**
     * 在SD卡上创建路径
     * @param path 路径
     * @return  {@link File}
     * @throws SystemException
     */
    public static File createSDPath(String path) throws SystemException {
        return createPath(getSDPath().getAbsolutePath(), path);
    }

    /**
     * 创建路径
     * @param rootPath 父路径
     * @param path  建立的路径
     * @return  {@link File}
     * @throws SystemException
     */
    public static File createPath(String rootPath, String path) throws SystemException {
        return createPath(new File(rootPath, path));
    }

    /**
     * 创建路径
     * @param path 路径
     * @return  {@link File}
     * @throws SystemException
     */
    public static File createPath(File path) throws SystemException {
        if (!path.exists()) {
            if (!path.mkdirs()) {
                throw new SystemException("创建路径失败:" + path.getAbsolutePath());
            } else {
                L.Log.d(TAG, "create path:" + path.getAbsolutePath());
            }
        }
        return path;
    }

    /**
     * 创建路径
     * @param path 路径
     * @return  {@link File}
     * @throws SystemException
     */
    public static File createPath(String path) throws SystemException {
        return createPath(new File(path));
    }

    /**
     * SD卡建立缓存文件夹
     * @param cacheDir 缓存目录名
     * @return {@link File} 以建立的缓存路径
     */
    public static File createCacheSDDirectory(String cacheDir) throws SystemException {
        isSDCardEnable();
        isSDLessMB(100);
        return createHideMediaDirectory(createProjectSDPath(cacheDir));
    }

    /**
     * SD {@link Context#getExternalCacheDir()} 建立缓存文件夹
     * @param cacheDir 缓存目录名
     * @return {@link File} 以建立的缓存路径
     */
    public static File createCacheSDAndroidDirectory(String cacheDir) throws SystemException {
        isSDCardEnable();
        isSDLessMB(100);
        File parentFile = Common.getSDExternalPath();
        return createHideMediaDirectory(createPath(parentFile.getAbsolutePath(), cacheDir));
    }

    /**
     * 建立隐藏所有媒体文件的文件夹
     * @param path 缓存目录
     * @return {@link File}
     */
    public static File createHideMediaDirectory(File path) throws SystemException {
        try {
            if (!path.exists()) {
                if (path.mkdirs()) {
                    throw new IOException("path create failure");
                }
            }
            File noMedia = new File(path, ".nomedia");
            if (!noMedia.exists()) {
                if (!noMedia.createNewFile()) {
                    throw new IOException(" \".nomedia\" create failure");
                }
            }
        } catch (IOException e) {
            throw new SystemException(e);
        }
        return path;
    }

    /**
     * 得到SD卡的路径
     *
     * @return
     * @throws SystemException
     */
    public static File getSDPath() throws SystemException {
        isSDCardEnable();
        return SDCardUtils.getSDCardPath();// 获取根目录
    }

    public static void isSDCardEnable() throws SystemException {
        if (!SDCardUtils.isSDCardEnable()) {
            throw new SystemException("SD卡不存在或不可用！");
        }
    }

    public static void isSDLessMB(long mb) throws SystemException {
        if (SDCardUtils.getSDAvailableSize() < mb * 1024) {
            throw new SystemException("SD卡空间不足" + mb + "MB！");
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param file
     * @param errorMessage
     * @throws SystemException
     */
    public static void exists(File file, String errorMessage) throws SystemException {
        if (TextUtils.isEmpty(errorMessage)) {
            errorMessage = "文件不存在！";
        }
        if (file == null) {
            throw new SystemException("文件为null");
        }
        if (!file.exists()) {
            throw new SystemException(errorMessage);
        }
        if (!file.isFile()) {
            throw new SystemException("不是文件",
                new SystemException(file.getAbsolutePath() + "不是文件!"));
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param file
     * @param errorMessage
     * @throws SystemException
     */
    public static void exists(String file, String errorMessage) throws SystemException {
        exists(new File(file), errorMessage);
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath
     * @throws SystemException
     */
    public static void exists(String filePath) throws SystemException {
        exists(new File(filePath));
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath
     * @throws SystemException
     */
    public static void exists(File filePath) throws SystemException {
        exists(filePath, "");
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath
     */
    public static boolean existsBoolean(File filePath) {
        try {
            exists(filePath, "");
            return true;
        } catch (SystemException e) {
            return false;
        }
    }

    /**
     * 删除文件或目录
     *
     * @param file
     */
    public static void delete(File file) {
        try {
            forceDelete(file);
        } catch (IOException e) {
            L.Log.w(TAG, "删除文件失败：" + file.getAbsolutePath(), e);
            return;
        }
        L.Log.d(TAG, "删除文件成功：" + file.getAbsolutePath());
    }

    /**
     * 清空路径
     */
    public static void cleanPath(File path) {
        if (path == null) {
            return;
        }
        try {
            cleanDirectory(path);
            L.Log.d(TAG, "清空:" + path);
        } catch (IOException e) {
            L.Log.w(TAG, "", e);
        }
    }
}
