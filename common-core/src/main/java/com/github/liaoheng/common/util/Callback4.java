package com.github.liaoheng.common.util;

/**
 * 通用回调
 * @author liaoheng
 * @version 2015-07-21 19:55
 */
public interface Callback4<T> {
    /**
     * 开始
     */
    void onPreExecute();

    /**
     * 结束
     */
    void onPostExecute();

    /**
     * yes
     * @param t
     */
    void onYes(T t);

    /**
     * no
     * @param t
     */
    void onNo(T t);

    /**
     * 完成
     * @param t
     */
    void onFinish(T t);

    /**
     * 无操作通用回调
     * @param <T>
     */
    class EmptyCallback<T> implements Callback4<T> {
        @Override public void onPreExecute() {

        }

        @Override public void onPostExecute() {

        }

        @Override public void onYes(T t) {

        }

        @Override public void onNo(T t) {

        }

        @Override public void onFinish(T t) {

        }

    }
}
