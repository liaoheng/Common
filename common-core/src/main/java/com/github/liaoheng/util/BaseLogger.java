package com.github.liaoheng.util;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * @author liaoheng
 * @date 2025-11-11 15:26
 */
public class BaseLogger implements ILogger {
    private boolean isPrint = true;
    private final String prefix;

    public BaseLogger(String prefix) {
        this.prefix = prefix;
    }

    public void setPrint(boolean print) {
        isPrint = print;
    }

    @Override
    public boolean isNotPrint() {
        return !isPrint;
    }

    @Override
    public String getTag(String tag) {
        return createTag(tag);
    }

    protected String createTag(String tag) {
        if (TextUtils.isEmpty(prefix)) {
            return tag;
        }
        if (TextUtils.isEmpty(tag)) {
            return prefix;
        }
        return prefix + "-" + tag;
    }

    protected String createMessage(String message, @Nullable Object... args) {
        if (null == message) {
            return "";
        }
        if (args == null || args.length == 0) {
            return message;
        }
        try {
            return String.format(message, args);
        } catch (Exception e) {
            return message;
        }
    }

    @Override
    public void json(String message) {
        json("", message);
    }

    @Override
    public void json(@NonNull String TAG, String message) {
        if (isNotPrint()) {
            return;
        }
        Log.d(createTag(TAG), formatJson(message));
    }

    private String formatJson(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            return jsonObject.toString(4);
        } catch (JSONException e) {
            return json;
        }
    }

    @Override
    public void v(String message, @Nullable Object... parameter) {
        v("", message, parameter);
    }

    @Override
    public void v(@NonNull String TAG, String message, @Nullable Object... parameter) {
        if (isNotPrint()) {
            return;
        }
        Log.v(createTag(TAG), createMessage(message, parameter));
    }

    @Override
    public void d(String message, @Nullable Object... parameter) {
        d("", message, parameter);
    }

    @Override
    public void d(@NonNull String TAG, String message, @Nullable Object... parameter) {
        if (isNotPrint()) {
            return;
        }
        Log.d(createTag(TAG), createMessage(message, parameter));
    }

    @Override
    public void i(String message, @Nullable Object... parameter) {
        i("", message, parameter);
    }

    @Override
    public void i(@NonNull String TAG, String message, @Nullable Object... parameter) {
        if (isNotPrint()) {
            return;
        }
        Log.i(createTag(TAG), createMessage(message, parameter));
    }

    @Override
    public void w(String message, @Nullable Object... parameter) {
        w("", message, parameter);
    }

    @Override
    public void w(@NonNull String TAG, String message, @Nullable Object... parameter) {
        if (isNotPrint()) {
            return;
        }
        Log.w(createTag(TAG), createMessage(message, parameter));
    }

    @Override
    public void w(@Nullable Throwable e, String message, @Nullable Object... parameter) {
        w("", e, message, parameter);
    }

    @Override
    public void w(String TAG, @Nullable Throwable e, String message, @Nullable Object... parameter) {
        if (isNotPrint()) {
            return;
        }
        Log.w(createTag(TAG), createMessage(message, parameter), e);
    }

    @Override
    public void e(String message, @Nullable Object... parameter) {
        e("", message, parameter);
    }

    @Override
    public void e(@NonNull String TAG, String message, @Nullable Object... parameter) {
        if (isNotPrint()) {
            return;
        }
        Log.e(createTag(TAG), createMessage(message, parameter));
    }

    @Override
    public void e(@NonNull Throwable e, String message, @Nullable Object... parameter) {
        e("", e, message, parameter);
    }

    @Override
    public void e(@NonNull String TAG, @NonNull Throwable e, String message, @Nullable Object... parameter) {
        if (isNotPrint()) {
            return;
        }
        Log.e(createTag(TAG), createMessage(message, parameter), e);
    }

    public String toLogString(Object object) {
        if (object == null) {
            return "null";
        }
        if (!object.getClass().isArray()) {
            return object.toString();
        }
        if (object instanceof boolean[]) {
            return Arrays.toString((boolean[]) object);
        }
        if (object instanceof byte[]) {
            return Arrays.toString((byte[]) object);
        }
        if (object instanceof char[]) {
            return Arrays.toString((char[]) object);
        }
        if (object instanceof short[]) {
            return Arrays.toString((short[]) object);
        }
        if (object instanceof int[]) {
            return Arrays.toString((int[]) object);
        }
        if (object instanceof long[]) {
            return Arrays.toString((long[]) object);
        }
        if (object instanceof float[]) {
            return Arrays.toString((float[]) object);
        }
        if (object instanceof double[]) {
            return Arrays.toString((double[]) object);
        }
        if (object instanceof Object[]) {
            return Arrays.deepToString((Object[]) object);
        }
        return "Couldn't find a correct type for the object";
    }

}
