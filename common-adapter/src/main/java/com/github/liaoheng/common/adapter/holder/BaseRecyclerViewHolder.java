package com.github.liaoheng.common.adapter.holder;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Base ViewHolder for RecyclerView
 *
 * @author liaoheng
 * @version 2015-10-27 17:20
 */
public abstract class BaseRecyclerViewHolder<K> extends RecyclerView.ViewHolder
        implements IBaseViewHolder<K> {

    public BaseRecyclerViewHolder(View itemView) {
        super(itemView);
    }

    @SuppressWarnings("unchecked") @Override public <T extends View> T findViewById(@IdRes int id) {
        return (T) itemView.findViewById(id);
    }

    @Override public Context getContext() {
        return itemView.getContext();
    }

    /**
     * {@inheritDoc}
     */
    @Override public void onHandle(K item, int position) {}
    /**
     * {@inheritDoc}
     */
    @Override public void onHandle(K item, int position, Object args) {}
}
