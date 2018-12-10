package com.github.liaoheng.common.adapter.holder;

import android.content.Context;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Base ViewHolder for RecyclerView
 *
 * @author liaoheng
 * @version 2015-10-27 17:20
 */
public abstract class BaseRecyclerViewHolder<K> extends RecyclerView.ViewHolder
        implements IBaseViewHolder<K> {
    protected final String TAG = this.getClass().getSimpleName();

    public BaseRecyclerViewHolder(View itemView) {
        super(itemView);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends View> T findViewById(@IdRes int id) {
        return (T) itemView.findViewById(id);
    }

    @Override
    public Context getContext() {
        return itemView.getContext();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onHandle(@Nullable K item, int position) {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void onHandle(@Nullable K item, int position, @Nullable Object args) {}
}
