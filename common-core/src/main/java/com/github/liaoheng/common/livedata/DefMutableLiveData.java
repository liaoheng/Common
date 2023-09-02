package com.github.liaoheng.common.livedata;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.lang.reflect.Field;

/**
 * 有默认值的MutableLiveData
 *
 * @author liaoheng
 * @date 2021-09-28 18:15
 */
public class DefMutableLiveData<T> extends MutableLiveData<T> {
    public DefMutableLiveData() {
        super();
    }

    public DefMutableLiveData(T t) {
        super();
        try {
            setData(this, t);
            setVersion(this, 0);
        } catch (Throwable e) {
            setValue(t);
        }
    }

    public static <T> void setData(Object object, T t) throws NoSuchFieldException, IllegalAccessException {
        if (!(object instanceof LiveData)) {
            return;
        }
        Class<?> superClazz = object.getClass();
        while (true) {
            superClazz = superClazz.getSuperclass();
            if (superClazz == null) {
                break;
            }
            if (superClazz == LiveData.class) {
                Field mData = superClazz.getDeclaredField("mData");
                mData.setAccessible(true);
                mData.set(object, t);
            }
        }
    }

    public static void setVersion(Object object, int v) throws NoSuchFieldException, IllegalAccessException {
        if (!(object instanceof LiveData)) {
            return;
        }
        Class<?> superClazz = object.getClass();
        while (true) {
            superClazz = superClazz.getSuperclass();
            if (superClazz == null) {
                break;
            }
            if (superClazz == LiveData.class) {
                Field mVersion = superClazz.getDeclaredField("mVersion");
                mVersion.setAccessible(true);
                mVersion.set(object, v);
            }
        }
    }
}
