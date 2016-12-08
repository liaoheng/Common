package com.github.liaoheng.common.adapter.base;

/**
 * Base Recycler Adapter Interface
 * @author liaoheng
 * @version 2016-12-08 15:52
 */
public interface IBaseRecyclerAdapter<K> extends IBaseAdapter<K> {
    void setOnItemClickListener(OnItemClickListener<K> onItemClickListener);

    void setOnItemLongClickListener(OnItemLongClickListener<K> onItemLongClickListener);
}
