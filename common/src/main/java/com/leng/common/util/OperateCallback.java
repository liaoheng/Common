package com.leng.common.util;

/**
 * 动作通用回调
 * @author liaoheng
 * @version 2015-07-21 19:55
 */
public interface OperateCallback<T> {
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
     * @param t
     */
    void onError(T t);

    /**
     * 完成
     * @param t
     */
    void onFinish(T t);

    /**
     * 无操作通用回调
     * @param <T>
     */
    class EmptyOperateCallback<T> implements OperateCallback<T> {
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
        public void onError(T t) {

        }

        @Override
        public void onFinish(T t) {

        }
    }
}
