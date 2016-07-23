package com.github.liaoheng.common.util;

/**
 * 通用回调
 * @author liaoheng
 * @version 2015-07-21 19:55
 */
public interface Callback2<T> {
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
    void onError(Throwable e);

    /**
     * 完成
     */
    void onFinish();

    /**
     * 无操作通用回调
     * @param <T>
     */
    class EmptyCallback2<T> implements Callback2<T> {
        @Override public void onPreExecute() {

        }

        @Override public void onPostExecute() {

        }

        @Override public void onSuccess(T o) {

        }

        @Override public void onError(Throwable e) {

        }

        @Override public void onFinish() {

        }
    }
}
