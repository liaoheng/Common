package com.github.liaoheng.common.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 图像相关工具类
 *
 * @author liaoheng
 */
public class BitmapUtils {
    private static final String TAG = BitmapUtils.class.getSimpleName();

    public final static MimeTypeMap.MimeType IMG_JPG = MimeTypeMap.MimeType.JPG;
    public final static MimeTypeMap.MimeType IMG_JPEG = MimeTypeMap.MimeType.JPEG;
    public final static MimeTypeMap.MimeType IMG_PNG = MimeTypeMap.MimeType.PNG;
    public final static MimeTypeMap.MimeType IMG_WEBP = MimeTypeMap.MimeType.WEBP;

    /**
     * convert byte array to Bitmap
     */
    public static Bitmap byteToBitmap(byte[] b) {
        return (b == null || b.length == 0) ? null : BitmapFactory.decodeByteArray(b, 0, b.length);
    }

    /**
     * convert Bitmap to byte array
     */
    public static byte[] bitmapToByte(Bitmap b) {
        return bitmapToByte(b, Bitmap.CompressFormat.PNG);
    }

    /**
     * convert Bitmap to byte array
     */
    public static byte[] bitmapToByte(Bitmap b, Bitmap.CompressFormat compressFormat) {
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        b.compress(compressFormat, 100, o);
        return o.toByteArray();
    }

    /**
     * convert Bitmap to InputStream
     */
    public static InputStream bitmapToStream(Bitmap bitmap, Bitmap.CompressFormat compressFormat) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(compressFormat, 100, bos);
        return new ByteArrayInputStream(bos.toByteArray());
    }

    /**
     * convert Drawable to Bitmap
     */
    public static Bitmap drawableToBitmap(@NonNull Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    /**
     * convert Bitmap to Drawable
     */
    public static Drawable bitmapToDrawable(Bitmap bitmap) {
        return bitmap == null ? null : new BitmapDrawable(bitmap);
    }

    /**
     * scale video
     */
    public static Bitmap createVideoThumbnail(String filePath) {
        return ThumbnailUtils.createVideoThumbnail(filePath,
                MediaStore.Images.Thumbnails.MINI_KIND);
    }

    /**
     * scale image
     */
    public static Bitmap createBitmapThumbnail(Bitmap org, int newWidth, int newHeight) {
        return createBitmapThumbnail(org, (float) newWidth / org.getWidth(),
                (float) newHeight / org.getHeight());
    }

    /**
     * scale image
     */
    public static Bitmap createBitmapThumbnail(Bitmap org, float scaleWidth, float scaleHeight) {
        if (org == null) {
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(org, 0, 0, org.getWidth(), org.getHeight(), matrix, true);
    }

    public static Bitmap toRoundCorner(Bitmap bitmap) {
        int height = bitmap.getHeight();
        int width = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, height);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(0xff424242);
        //paint.setColor(Color.TRANSPARENT);
        canvas.drawCircle(width / 2, height / 2, width / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static Bitmap.CompressFormat getImageExtension(File file) {
        return getImageExtension(file.getAbsolutePath());
    }

    /**
     * 获取图片文件格式对应Bitmap.CompressFormat
     *
     * @see Bitmap.CompressFormat
     */
    public static Bitmap.CompressFormat getImageExtension(String file) {
        String extension = FileUtils.getExtension(file);
        Bitmap.CompressFormat format = Bitmap.CompressFormat.PNG;
        MimeTypeMap.MimeType mimeType = MimeTypeMap.MimeType.findFromExtension(extension);
        if (IMG_JPG.equalsMimeType(mimeType)) {
            format = Bitmap.CompressFormat.JPEG;
        } else if (IMG_WEBP.equalsMimeType(mimeType)) {
            format = Bitmap.CompressFormat.WEBP;
        }
        return format;
    }

    public static Bitmap.CompressFormat getImageExtension(Bitmap bitmap) {
        if (bitmap.hasAlpha()) {
            return Bitmap.CompressFormat.PNG;
        } else {
            return Bitmap.CompressFormat.JPEG;
        }
    }

    /**
     * 通过文件路径获取bitmap
     *
     * @param filePath 图片路径 等比例800
     */
    public static Bitmap getBitmapByUri(String filePath) {
        return getBitmapByUri(filePath, 800, 800);
    }

    /**
     * 通过文件路径获取bitmap
     *
     * @param filePath 图片路径
     * @param width    等比例压缩图片 宽
     * @param height   等比例压缩图片 长
     */
    @Nullable
    public static Bitmap getBitmapByUri(String filePath, int width,
            int height) {
        if (FileUtils.existsBoolean(filePath)) {
            return null;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        if (width < options.outWidth || height < options.outHeight) {
            options.inPreferredConfig = Bitmap.Config.RGB_565;
        } else {
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        }

        options.inInputShareable = true;
        options.inPurgeable = true;
        options.inSampleSize = calculateInSampleSize(options, width, height);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError e) {
            L.Log.w(TAG, e);
        }
        if (null == bitmap) {
            return null;
        }

        int angle = getExifOrientation(filePath);
        if (angle != 0) {//角度调整为正
            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            Bitmap result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                    matrix, true);
            bitmap.recycle();
            return result;
        } else {
            return bitmap;
        }
    }

    public static String getPathByUri(Context context, Uri uri) {
        String fileName = "";
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                fileName = cursor.getString(index);
            }
            cursor.close();
        }
        return fileName;
    }

    /**
     * 获取照片角度
     */
    private static int getExifOrientation(String path) {
        int degree = 0;
        try {
            ExifInterface exif = new ExifInterface(path);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
            }
        } catch (IOException ignored) {
        }
        return degree;
    }

    /**
     * 按比例压缩
     */
    public static Bitmap compressBitmapByScale(Bitmap bitmap, Bitmap.CompressFormat format,
            int reqWidth, int reqHeight) {
        return compressBitmapByScale(bitmap, format, reqWidth, reqHeight, false);
    }

    /**
     * 按比例压缩
     */
    public static Bitmap compressBitmapByScale(Bitmap bitmap, Bitmap.CompressFormat format,
            int reqWidth, int reqHeight, boolean recycle) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(format, 100, stream);

        ByteArrayInputStream isBm = new ByteArrayInputStream(stream.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(isBm, null, newOpts);

        newOpts.inSampleSize = calculateInSampleSize(newOpts, reqWidth, reqHeight);
        newOpts.inJustDecodeBounds = false;

        ByteArrayInputStream inputStream = new ByteArrayInputStream(stream.toByteArray());
        if (recycle) {
            recycle(bitmap);
        }
        return BitmapFactory.decodeStream(inputStream, null, newOpts);
    }

    /**
     * 按质量压缩,只对JPG作用
     *
     * @param scale KB
     */
    public static Bitmap compressBitmapByQuality(Bitmap bitmap, int scale) {
        return compressBitmapByQuality(bitmap, scale, false);
    }

    /**
     * 按质量压缩,只对JPG作用
     *
     * @param scale KB
     */
    public static Bitmap compressBitmapByQuality(Bitmap bitmap, int scale, boolean recycle) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        int options = 90;
        while (baos.toByteArray().length / 1024 > scale) {
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
            options -= 10;
            if (options == 0) {
                break;
            }
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        Bitmap out = BitmapFactory.decodeStream(isBm, null, null);
        if (recycle) {
            recycle(bitmap);
        }
        return out;
    }

    /**
     * 按质量压缩和比例压缩
     *
     * @param bitmap  源bitmap
     * @param scale   KB
     * @param w       weight
     * @param h       height
     * @param recycle 是否释放源bitmap
     */
    public static Bitmap compressBitmapByQualityAndScale(Bitmap bitmap, int scale, int w, int h, boolean recycle) {
        Bitmap.CompressFormat imageExtension = getImageExtension(bitmap);
        if (imageExtension.equals(Bitmap.CompressFormat.JPEG) && scale > 0) {
            bitmap = compressBitmapByQuality(bitmap, scale, recycle);
        }
        if (w == 0 || h == 0) {
            return bitmap;
        }
        return compressBitmapByScale(bitmap, imageExtension, w, h, recycle);
    }

    public static Bitmap compressBitmapByQualityAndScale(Bitmap bitmap, int scale, int w, int h) {
        return compressBitmapByQualityAndScale(bitmap, scale, w, h, true);
    }

    public static void recycle(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    public static int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { //API 19
            return bitmap.getAllocationByteCount();
        }
        return bitmap.getRowBytes() * bitmap.getHeight(); //earlier version
    }

    /**
     * 生成缩略图的比
     *
     * @param reqWidth  缩略图宽
     * @param reqHeight 缩略图长
     * @see <a href='https://developer.android.com/topic/performance/graphics/load-bitmap.html'>load-bitmap</a>
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,
            int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = Math.min(heightRatio, widthRatio);
        }
        return inSampleSize;
    }

    /**
     * 将给定图片维持宽高比缩放后，截取正中间的正方形部分。
     *
     * @param bitmap     原图
     * @param edgeLength 希望得到的正方形部分的边长
     * @return 缩放截取正中部分后的位图。
     */
    public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength) {
        if (null == bitmap || edgeLength <= 0) {
            return null;
        }

        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();

        if (widthOrg > edgeLength && heightOrg > edgeLength) {
            //压缩到一个最小长度是edgeLength的bitmap
            int longerEdge = (int) (edgeLength * Math.max(widthOrg, heightOrg)
                    / Math.min(widthOrg, heightOrg));
            int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
            int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
            Bitmap scaledBitmap;

            try {
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
            } catch (Exception e) {
                return null;
            }

            //从图中截取正中间的正方形部分。
            int xTopLeft = (scaledWidth - edgeLength) / 2;
            int yTopLeft = (scaledHeight - edgeLength) / 2;

            try {
                result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength,
                        edgeLength);
                scaledBitmap.recycle();
            } catch (Exception e) {
                return null;
            }
        }

        return result;
    }

}
