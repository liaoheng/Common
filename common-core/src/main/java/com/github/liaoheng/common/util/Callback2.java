package com.github.liaoheng.common.util;

/**
 * @author liaoheng
 * @date 2022-03-21 18:19:57
 */
public interface Callback2<T, R> {
    R apply(T t);
}