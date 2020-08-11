package com.github.liaoheng.common.util;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

import com.github.liaoheng.common.Common;
import com.google.common.base.Strings;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;

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
@SuppressWarnings("UnstableApiUsage")
public class FileUtils {
    private static final String TAG = FileUtils.class.getSimpleName();
    public static final String ERROR_SDCARD_NOT_AVAILABLE = "error_sdcard_not_available";
    public static final String ERROR_SDCARD_SPACE_INSUFFICIENT = "error_sdcard_space_insufficient";

    public static void isExternalStorageEnable() throws IOException {
        if (!SDCardUtils.isExternalStorageEnable()) {
            throw new IOException(ERROR_SDCARD_NOT_AVAILABLE);
        }
    }

    public static void isExternalStorageLessMB(File path, long mb) throws IOException {
        if (SDCardUtils.getPathAvailableSize(path) < mb * 1024) {
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
        isExternalStorageLessMB(getProjectSpacePath(context), 100);
        return createHideMediaDirectory(createProjectSpaceDir(context, cacheDir));
    }

    //-----------------------------------------外部储存----------------------------------------------------

    /**
     * 获取外部储存项目目录的路径，sd/{project_name}/
     */
    @Deprecated
    public static File getProjectPath(String projectName) {
        return new File(Environment.getExternalStorageDirectory(), projectName);
    }

    /**
     * 获取外部储存项目目录的路径，sd/{project_name}/
     */
    @Deprecated
    public static File getProjectPath() throws IOException {
        return getProjectPath(Common.getProjectName());
    }

    /**
     * 在外部储存项目中创建目录，sd/{project_name}/{dir}/
     *
     * @param dir 目录名
     */
    @Deprecated
    public static File createProjectDir(String dir) throws IOException {
        return createPath(getProjectPath(), dir);
    }

    /**
     * 得到外部储存项目的临时目录，sd/{project_name}/temp/
     */
    @Deprecated
    public static File getProjectTempDirectory() throws IOException {
        return createProjectDir("temp");
    }

    /**
     * 得到外部储存项目的图片目录，sd/{project_name}/pictures/
     */
    @Deprecated
    public static File getProjectPicturesDirectory() throws IOException {
        return createProjectDir("pictures");
    }

    /**
     * 得到外部储存项目的缓存目录，sd/{project_name}/{cacheDir}/
     *
     * @param cacheDir 缓存目录名
     */
    @Deprecated
    public static File getProjectCacheDirectory(String cacheDir) throws IOException {
        isExternalStorageEnable();
        isExternalStorageLessMB(getProjectPath(), 100);
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
        if (!file.exists()) {
            return;
        }
        if (file.delete()) {
            L.alog().d(TAG, "delete file ：" + file.getAbsolutePath());
        }
    }

    /**
     * 清空路径
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void cleanPath(File path) {
        if (path == null) {
            return;
        }
        if (!path.isDirectory()) {
            return;
        }
        File[] files = path.listFiles();
        if (files == null) {
            return;
        }
        for (final File file : files) {
            file.delete();
        }
        L.alog().d(TAG, "clean path ：" + path.getAbsolutePath());
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

    public static String getExtension(String fullName) {
        return Files.getFileExtension(fullName);
    }

    public static String getName(String fullName) {
        String extension = getExtension(fullName);
        return Files.getNameWithoutExtension(fullName) + (Strings.isNullOrEmpty(extension) ? "" : "." + extension);
    }

    public static void copyInputStreamToFile(InputStream source, final File destination) throws IOException {
        Files.asByteSink(destination, FileWriteMode.APPEND).writeFrom(source);
    }

    //https://juejin.im/post/5d0b1739e51d4510a73280cc
    public static Uri saveFileToPictureCompat(Context context, String name, File from) throws IOException {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return saveFileToPicture(context, name, from);
        } else {
            boolean isExternalStorageLegacy = true;
            try {
                isExternalStorageLegacy = Environment.isExternalStorageLegacy();
            } catch (NoSuchMethodError ignored) {
            }
            if (isExternalStorageLegacy) {
                return saveFileToPicture(context, name, from);
            }
            return saveFileToPictureQ(context, name, from);
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    public static Uri saveFileToPicture(Context context, String name, File from) throws IOException {
        File file = new File(SDCardUtils.getExternalStoragePicturesPublicDirectory(), Common.getProjectName());
        File outFile = FileUtils.createFile(file, name);
        Files.copy(from, outFile);
        Uri uri = Uri.fromFile(outFile);
        context.sendBroadcast(
                new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
        return uri;
    }

    //https://developer.android.com/training/data-storage/files/external-scoped
    @SuppressWarnings("UnstableApiUsage")
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static Uri saveFileToPictureQ(Context context, String name, File from) throws IOException {
        Uri uri;
        ContentValues contentValues = null;
        try (Cursor query = context.getContentResolver().query(MediaStore.Images.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL_PRIMARY), null,
                MediaStore.Images.Media.DISPLAY_NAME + "='" + name + "' AND "
                        + MediaStore.Images.Media.OWNER_PACKAGE_NAME + "='"
                        + context.getPackageName() + "'",
                null, null)) {
            if (query != null && query.moveToFirst()) {
                long id = query.getLong(query.getColumnIndex(MediaStore.Images.Media._ID));
                uri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY), id);
            } else {
                contentValues = new ContentValues();
                contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, name);
                contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                contentValues.put(MediaStore.Images.Media.IS_PENDING, 1);
                contentValues.put(MediaStore.Images.Media.RELATIVE_PATH,
                        Environment.DIRECTORY_PICTURES + File.separator + Common.getProjectName());
                uri = context.getContentResolver().insert(MediaStore.Images.Media.getContentUri(
                        MediaStore.VOLUME_EXTERNAL_PRIMARY), contentValues);
            }
        }

        if (uri == null) {
            throw new IOException("getContentResolver uri is null");
        }
        ParcelFileDescriptor fd = context.getContentResolver().openFileDescriptor(uri, "w");
        if (fd == null) {
            throw new IOException("openFileDescriptor is null");
        }
        Files.copy(from, new FileOutputStream(fd.getFileDescriptor()));
        if (contentValues != null) {
            contentValues.clear();
            contentValues.put(MediaStore.Images.Media.IS_PENDING, 0);
            context.getContentResolver().update(uri, contentValues, null, null);
        }
        return uri;
    }
}
