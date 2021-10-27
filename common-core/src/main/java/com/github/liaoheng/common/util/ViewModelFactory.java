package com.github.liaoheng.common.util;

import androidx.lifecycle.ViewModelProvider;

/**
 * ViewModel工厂代理
 *
 * @author liaoheng
 * @date 2021-09-24 11:29
 */
public class ViewModelFactory {

    private static ViewModelProvider.Factory mProxy = null;

    public static void initProxy(ViewModelProvider.Factory proxy) {
        mProxy = proxy;
    }

    public static ViewModelProvider.Factory get() {
        if (mProxy == null) {
            throw new IllegalStateException("ViewModelFactory not init");
        }
        return mProxy;
    }

}
