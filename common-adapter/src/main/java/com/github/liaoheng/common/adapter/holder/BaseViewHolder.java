package com.github.liaoheng.common.adapter.holder;

import android.content.Context;
import android.support.annotation.IdRes;
import android.view.View;

/**
 * Base ViewHolder for ListView
 *
 * @author liaoheng
 * @version 2015年10月21日
 */
public abstract class BaseViewHolder<K> implements IBaseViewHolder<K> {
    private final View itemView;

    public BaseViewHolder(View itemView) {
        if (itemView == null) {
            throw new IllegalArgumentException("itemView is null");
        }
        this.itemView = itemView;
    }

    @Override public void onHandle(K item, int position) {
    }

    @Override public <T extends View> T findViewById(@IdRes int id) {
        return (T) itemView.findViewById(id);
    }

    @SuppressWarnings("unchecked") public <T extends BaseViewHolder> T setTag() {
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
