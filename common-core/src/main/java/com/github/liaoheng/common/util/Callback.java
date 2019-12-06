package com.github.liaoheng.common.util;

/**
 * @author liaoheng
 * @version 2015-07-21 19:55
 */
public interface Callback<T> {
    /**
     * start
     */
    void onPreExecute();

    /**
     * stop
     */
    void onPostExecute();

    void onSuccess(T t);

    void onError(Throwable e);

    void onFinish();

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
     * @author liaoheng
     * @version 2015年9月16日
     */
    class LogEmptyCallback<T> implements Callback<T> {
        private String TAG = LogEmptyCallback.class.getSimpleName();

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
