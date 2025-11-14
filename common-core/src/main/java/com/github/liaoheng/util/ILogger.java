package com.github.liaoheng.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author liaoheng
 * @date 2025-11-11 15:12
 */
public interface ILogger {
    boolean isNotPrint();

    void setPrint(boolean print);

    String getTag(String tag);

    void json(String message);

    void json(@NonNull String TAG, String message);

    void v(String message, @Nullable Object... parameter);

    void v(@NonNull String TAG, String message, @Nullable Object... parameter);

    void d(String message, @Nullable Object... parameter);

    void d(@NonNull String TAG, String message, @Nullable Object... parameter);

    void i(String message, @Nullable Object... parameter);

    void i(@NonNull String TAG, String message, @Nullable Object... parameter);

    void w(String message, @Nullable Object... parameter);

    void w(@NonNull String TAG, String message, @Nullable Object... parameter);

    void w(@Nullable Throwable e, String message, @Nullable Object... parameter);

    void w(String TAG, @Nullable Throwable e, String message, @Nullable Object... parameter);

    void e(String message, @Nullable Object... parameter);

    void e(@NonNull String TAG, String message, @Nullable Object... parameter);

    void e(@NonNull Throwable e, String message, @Nullable Object... parameter);

    void e(@NonNull String TAG, @NonNull Throwable e, String message, @Nullable Object... parameter);

}
