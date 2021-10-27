package com.github.liaoheng.common.util;

import java.util.List;

import androidx.lifecycle.MutableLiveData;

/**
 * @author liaoheng
 * @date 2021-09-10 13:55
 */
public class UpdateListLiveData<T> extends MutableLiveData<List<T>> {
    final Object mDataLock = new Object();

    public UpdateListLiveData() {
        super();
    }

    public UpdateListLiveData(List<T> value) {
        setValue(value);
    }

    public void add(T t) {
        if (getValue() == null) {
            return;
        }
        synchronized (mDataLock) {
            getValue().add(t);
            setValue(getValue());
        }
    }

    public void remove(T t) {
        if (getValue() == null) {
            return;
        }
        synchronized (mDataLock) {
            getValue().remove(t);
            setValue(getValue());
        }
    }

}
