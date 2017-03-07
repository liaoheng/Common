package com.github.liaoheng.common.adapter.holder;

import android.content.Context;
import android.support.annotation.IdRes;
import android.view.View;

/**
 * Base ListViewHolder for ListView
 *
 * @author liaoheng
 * @version 2015年10月21日
 */
public abstract class BaseListViewHolder<K> implements IBaseViewHolder<K> {
    private final View itemView;

    public BaseListViewHolder(View itemView) {
        if (itemView == null) {
            throw new IllegalArgumentException("itemView is null");
        }
        this.itemView = itemView;
    }

    /**
     * {@inheritDoc}
     */
    @Override public void onHandle(K item, int position) {
    }
    /**
     * {@inheritDoc}
     */
    @Override public void onHandle(K item, int position, Object args) {
    }

    @SuppressWarnings("unchecked") @Override public <T extends View> T findViewById(@IdRes int id) {
        return (T) itemView.findViewById(id);
    }

    @SuppressWarnings("unchecked") public <T extends BaseListViewHolder> T setTag() {
        itemView.setTag(this);
        return (T) this;
    }

    @SuppressWarnings("unchecked") public static <T> T getTag(View itemView) {
        return (T) itemView.getTag();
    }

    public Context getContext() {
        return itemView.getContext();
    }

    public View getItemView() {
        return itemView;
    }
}
