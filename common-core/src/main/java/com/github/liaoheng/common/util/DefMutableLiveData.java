package com.github.liaoheng.common.util;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

/**
 * 有默认值的MutableLiveData
 *
 * @author liaoheng
 * @date 2021-09-28 18:15
 */
public class DefMutableLiveData<T> extends MutableLiveData<T> {

    public DefMutableLiveData(T t) {
        setValue(t);
    }

    @NonNull
    @Override
    public T getValue() {
        return super.getValue();
    }
}
