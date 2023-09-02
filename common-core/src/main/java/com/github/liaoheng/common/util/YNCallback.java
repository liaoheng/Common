package com.github.liaoheng.common.util;

/**
 * @author liaoheng
 * @version 2019-12-06 11:35
 */
public interface YNCallback {
    void onAllow();

    void onDeny();

    class EmptyCallback implements YNCallback {
        @Override
        public void onAllow() {

        }

        @Override
        public void onDeny() {

        }
    }
}
