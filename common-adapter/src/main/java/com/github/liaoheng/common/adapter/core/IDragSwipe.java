package com.github.liaoheng.common.adapter.core;

/**
 * @author liaoheng
 * @date 2022-08-15 17:01
 */
public interface IDragSwipe {
    /**
     * 两个Item交换位置
     * @param fromPosition 第一个Item的位置
     * @param toPosition 第二个Item的位置
     */
    void onItemSwapped(int fromPosition, int toPosition);
}
