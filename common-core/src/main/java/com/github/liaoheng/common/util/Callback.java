package com.github.liaoheng.common.util;

import android.support.annotation.NonNull;

/**
 * 通用回调
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
     * @param t
     */
    void onSuccess(T t);

    /**
     * 失败
     * @param e
     */
    void onError(SystemException e);

    /**
     * 完成
     */
    void onFinish();

    /**
     * 无操作通用回调
     * @param <T>
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
        public void onError(SystemException e) {

        }

        @Override
        public void onFinish() {

        }
    }

    /**
     * 对于onSuccess与onError打印日志
     * @author liaoheng
     * @version 2015年9月16日
     */
    class LogEmptyCallback<T> implements Callback<T> {
        private String TAG = LogEmptyCallback.class.getSimpleName();

        /**
         * 对于成功与失败打印日志
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
            L.Log.d(TAG, " onSuccess :" + t);
        }

        @Override
        public void onError(@NonNull SystemException e) {
            L.Log.w(TAG, e.getMessage(), e);
        }

        @Override
        public void onFinish() {
        }
    }
}
