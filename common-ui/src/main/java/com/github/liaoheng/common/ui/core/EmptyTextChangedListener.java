package com.github.liaoheng.common.ui.core;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

/**
 * 检查是否为输入字符是否为空
 *
 * @author liaoheng
 * @version 2016-10-25 19:10
 */
public class EmptyTextChangedListener implements TextWatcher {

    private boolean empty = true;
    private final Consumer<CharSequence> mCallback;

    /**
     * 检查是否为输入字符是否为空
     *
     * @param callback yes 非空  no 空
     */
    public EmptyTextChangedListener(@NonNull Consumer<CharSequence> callback) {
        mCallback = callback;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        empty = TextUtils.isEmpty(s);
    }

    @Override
    public void afterTextChanged(Editable s) {
        mCallback.accept(empty ? null : s);
    }
}
