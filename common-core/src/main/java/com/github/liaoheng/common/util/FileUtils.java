package com.github.liaoheng.common.util;

import android.content.Context;
import android.text.TextUtils;

import com.github.liaoheng.common.Common;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * 文件工具
 * <br/> Dependency : android.permission.READ_EXTERNAL_STORAGE
 * <br/> Dependency : android.permission.WRITE_EXTERNAL_STORAGE
 *
 * @author liaoheng
 */
public class FileUtils {
    private static final String TAG = FileUtils.class.getSimpleName();
    public static final String ERROR_SDCARD_NOT_AVAILABLE = "error_sdcard_not_available";
    public static final String ERROR_SDCARD_SPACE_INSUFFICIENT = "error_sdcard__space_insufficient";

    /**
     * 得到SD卡的路径
     */
    public static File getExternalStoragePath() throws IOException {
        isExternalStorageEnable();
        return SDCardUtils.getExternalStorageDirectory();// 获取根目录
    }

    public static void isExternalStorageEnable() throws IOException {
        if (!SDCardUtils.isExternalStorageEnable()) {
            throw new IOException(ERROR_SDCARD_NOT_AVAILABLE);
        }
    }

    public static void isExternalStorageLessMB(long mb) throws IOException {
        if (SDCardUtils.getSDAvailableSize() < mb * 1024) {
            throw new IOException(ERROR_SDCARD_SPACE_INSUFFICIENT);
        }
    }

    /**
     * 在父目录下创建文件
     *
     * @param parent   父目录，不存在会自动创建
     * @param fileName 需要创建文件名
     */
    public static File createFile(File parent, String fileName) {
        if (!parent.exists()) {
            createPath(parent);
        }
        File newFile = new File(parent, fileName);
        if (!newFile.exists()) {
            try {
                if (newFile.createNewFile()) {
                    L.alog().d(TAG, "create file :" + newFile.getAbsolutePath());
                }
            } catch (IOException ignored) {
            }
        }
        return newFile;
    }

    /**
     * 在父目录下创建临时文件，使用{@link UUID#randomUUID()}生成文件名
     *
     * @param parent 父目录，不存在会自动创建
     * @param nameEx 需要创建文件格式
     */
    public static File createTempFile(File parent, String nameEx) {
        String fileName = UUID.randomUUID().toString() + "." + nameEx;
        return createFile(parent, fileName);
    }

    /**
     * 在父目录下创建临时文件，使用{@link UUID#randomUUID()}生成文件名
     *
     * @param parent 父目录，不存在会自动创建
     */
    public static File createTempFile(File parent) {
        return createTempFile(parent, "tmp");
    }

    /**
     * 创建路径
     *
     * @param rootPath 父路径
     * @param path     建立的路径
     */
    public static File createPath(String rootPath, String path) {
        return createPath(new File(rootPath, path));
    }

    /**
     * 创建路径
     *
     * @param rootPath 父路径
     * @param path     建立的路径
     */
    public static File createPath(File rootPath, String path) {
        return createPath(new File(rootPath, path));
    }

    /**
     * 创建路径
     *
     * @param path 路径
     */
    public static File createPath(String path) {
        return createPath(new File(path));
    }

    /**
     * 创建路径
     *
     * @param path 路径
     */
    public static File createPath(File path) {
        if (!path.exists()) {
            if (path.mkdirs()) {
                L.alog().d(TAG, "create path:" + path.getAbsolutePath());
            }
        }
        return path;
    }

    /**
     * 建立隐藏所有媒体文件的文件夹
     *
     * @param path 缓存目录
     * @return {@link File}
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static File createHideMediaDirectory(File path) {
        createPath(path);
        File noMedia = new File(path, ".nomedia");
        if (!noMedia.exists()) {
            try {
                noMedia.createNewFile();
            } catch (IOException ignored) {
            }
        }
        return path;
    }

    //-----------------------------------------外部储存空间----------------------------------------------------

    /**
     * 获取外部储存项目空间目录的路径，sd/Android/data/{package}/
     */
    @SuppressWarnings("ConstantConditions")
    public static File getProjectSpacePath(Context context) throws IOException {
        File externalCacheDir = context.getExternalCacheDir();
        if (externalCacheDir == null) {
            throw new IOException(ERROR_SDCARD_NOT_AVAILABLE);
        }
        return externalCacheDir.getParentFile();
    }

    /**
     * 在外部储存项目空间中创建目录，sd/Android/data/{package}/{dir}/
     *
     * @param dir 目录名
     */
    public static File createProjectSpaceDir(Context context, String dir) throws IOException {
        return createPath(getProjectSpacePath(context), dir);
    }

    /**
     * 得到外部储存项目空间的临时目录，sd/Android/data/{package}/temp/
     */
    public static File getProjectSpaceTempDirectory(Context context) throws IOException {
        return createProjectSpaceDir(context, "temp");
    }

    /**
     * 得到外部储存项目空间的图片目录，sd/Android/data/{package}/pictures/
     */
    public static File getProjectSpacePicturesDirectory(Context context) throws IOException {
        return createProjectSpaceDir(context, "pictures");
    }

    /**
     * 得到内部储存项目空间的缓存目录，sd/Android/data/{package}/{cacheDir}/
     *
     * @param cacheDir 缓存目录名
     */
    public static File getProjectSpaceCacheDirectory(Context context, String cacheDir) throws IOException {
        isExternalStorageEnable();
        isExternalStorageLessMB(100);
        return createHideMediaDirectory(createProjectSpaceDir(context, cacheDir));
    }

    //-----------------------------------------外部储存----------------------------------------------------

    /**
     * 获取外部储存项目目录的路径，sd/{project_name}/
     */
    public static File getProjectPath(String projectName) throws IOException {
        return new File(getExternalStoragePath(), projectName);
    }

    /**
     * 获取外部储存项目目录的路径，sd/{project_name}/
     */
    public static File getProjectPath() throws IOException {
        return getProjectPath(Common.getProjectName());
    }

    /**
     * 在外部储存项目中创建目录，sd/{project_name}/{dir}/
     *
     * @param dir 目录名
     */
    public static File createProjectDir(String dir) throws IOException {
        return createPath(getProjectPath(), dir);
    }

    /**
     * 得到外部储存项目的临时目录，sd/{project_name}/temp/
     */
    public static File getProjectTempDirectory() throws IOException {
        return createProjectDir("temp");
    }

    /**
     * 得到外部储存项目的图片目录，sd/{project_name}/pictures/
     */
    public static File getProjectPicturesDirectory() throws IOException {
        return createProjectDir("pictures");
    }

    /**
     * 得到外部储存项目的缓存目录，sd/{project_name}/{cacheDir}/
     *
     * @param cacheDir 缓存目录名
     */
    public static File getProjectCacheDirectory(String cacheDir) throws IOException {
        isExternalStorageEnable();
        isExternalStorageLessMB(100);
        return createHideMediaDirectory(createProjectDir(cacheDir));
    }

    //-----------------------------------------内部储存----------------------------------------------------

    /**
     * 获取内部储存项目空间的路径，/data/data/{package}/
     */
    public static File getDataProjectPath(Context context) {
        return context.getCacheDir().getParentFile();
    }

    /**
     * 在内部储存项目空间中创建目录，/data/data/{package}/{dir}/
     *
     * @param dir 目录名
     */
    public static File createDataProjectDir(Context context, String dir) {
        return context.getDir(dir, Context.MODE_PRIVATE);
    }

    //-----------------------------------------完----------------------------------------------------

    /**
     * 判断文件是否存在
     */
    public static void exists(File file, String errorMessage) throws IOException {
        if (TextUtils.isEmpty(errorMessage)) {
            errorMessage = "File does not exist.";
        }
        if (file == null) {
            throw new IOException("File is null.");
        }
        if (!file.exists()) {
            throw new IOException(errorMessage);
        }
    }

    /**
     * 判断文件是否存在
     */
    public static void exists(String file, String errorMessage) throws IOException {
        exists(new File(file), errorMessage);
    }

    /**
     * 判断文件是否存在
     */
    public static void exists(String filePath) throws IOException {
        exists(new File(filePath));
    }

    /**
     * 判断文件是否存在
     */
    public static void exists(File filePath) throws IOException {
        exists(filePath, "");
    }

    /**
     * 判断文件是否存在
     */
    public static boolean existsBoolean(File filePath) {
        try {
            exists(filePath, "");
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 判断文件是否存在
     */
    public static boolean existsBoolean(String filePath) {
        try {
            exists(filePath, "");
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 删除文件或目录
     */
    public static void delete(File file) {
        if (file == null) {
            return;
        }
        try {
            org.apache.commons.io.FileUtils.forceDelete(file);
            L.alog().d(TAG, "delete file ：" + file.getAbsolutePath());
        } catch (IOException ignored) {
        }
    }

    /**
     * 清空路径
     */
    public static void cleanPath(File path) {
        if (path == null) {
            return;
        }
        try {
            org.apache.commons.io.FileUtils.cleanDirectory(path);
            L.alog().d(TAG, "clean path ：" + path.getAbsolutePath());
        } catch (IOException ignored) {
        }
    }

    /**
     * 获取文件夹大小
     * <p>
     * <pre>
     *            Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
     *      Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
     *      </pre>
     * </p>
     */
    public static long getFolderSize(File file) {
        long size = 0;
        if (!file.isDirectory()) {
            return file.length();
        }
        File[] fileList = file.listFiles();
        for (int i = 0; i < fileList.length; i++) {
            // 如果下面还有文件
            if (fileList[i].isDirectory()) {
                size = size + getFolderSize(fileList[i]);
            } else {
                size = size + fileList[i].length();
            }
        }
        return size;
    }

    /**
     * 格式化文件大小单位
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }

    /**
     * @see FilenameUtils#getExtension(String)
     */
    public static String getExtension(String filename) {
        return FilenameUtils.getExtension(filename);
    }

    /**
     * @see FilenameUtils#getName(String)
     */
    public static String getName(String filename) {
        return FilenameUtils.getName(filename);
    }

    /**
     * @see org.apache.commons.io.FileUtils#copyInputStreamToFile(InputStream, File)
     */
    public static void copyInputStreamToFile(InputStream source, final File destination) throws IOException {
        org.apache.commons.io.FileUtils.copyInputStreamToFile(source, destination);
    }
}
