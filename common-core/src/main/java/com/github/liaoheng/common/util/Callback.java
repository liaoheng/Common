package com.github.liaoheng.common.util;

import androidx.annotation.Nullable;

/**
 * 通用回调
 *
 * @author liaoheng
 * @version 2015-07-21 19:55
 */
public interface Callback<T> {
    /**
     * 开始
     */
    void onPreExecute();

    /**
     * 结束
     */
    void onPostExecute();

    /**
     * 成功
     */
    void onSuccess(T t);

    /**
     * 失败
     */
    void onError(@Nullable Throwable e);

    /**
     * 完成
     */
    void onFinish();

    /**
     * 无操作通用回调
     */
    class EmptyCallback<T> implements Callback<T> {
        @Override
        public void onPreExecute() {

        }

        @Override
        public void onPostExecute() {

        }

        @Override
        public void onSuccess(T t) {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onFinish() {

        }
    }

    /**
     * 对于onSuccess与onError打印日志
     *
     * @author liaoheng
     * @version 2015年9月16日
     */
    class LogEmptyCallback<T> implements Callback<T> {
        private String TAG = LogEmptyCallback.class.getSimpleName();

        /**
         * 对于成功与失败打印日志
         *
         * @param TAG 日志TAG
         */
        public LogEmptyCallback(String TAG) {
            this.TAG = TAG;
        }

        public LogEmptyCallback() {
        }

        @Override
        public void onPreExecute() {
        }

        @Override
        public void onPostExecute() {
        }

        @Override
        public void onSuccess(T t) {
            L.alog().d(TAG, " onSuccess : " + t);
        }

        @Override
        public void onError(Throwable e) {
            L.alog().w(TAG, e, e.getMessage());
        }

        @Override
        public void onFinish() {
        }
    }
}
