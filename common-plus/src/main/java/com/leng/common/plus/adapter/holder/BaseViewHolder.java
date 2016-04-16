package com.leng.common.plus.adapter.holder;

import android.content.Context;
import android.support.annotation.IdRes;
import android.view.View;

import com.leng.common.util.UIUtils;

/**
 * @author liaoheng
 * @version 2015年10月21日
 */
public class BaseViewHolder {
    private View itemView;

    public BaseViewHolder(View itemView) {
        if (itemView == null) {
            throw new IllegalArgumentException("itemView is null");
        }
        this.itemView = itemView;
    }

    public <T extends View> T findViewById(@IdRes int id) {
        if (itemView != null)
            return UIUtils.findViewById(itemView, id);
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T extends BaseViewHolder> T setTag() {
        if (itemView != null)
            itemView.setTag(this);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getTag(View itemView) {
        if (itemView != null)
            return (T) itemView.getTag();
        return null;
    }

    public Context getContext() {
        if (itemView == null)
            return null;
        return itemView.getContext();
    }

    public View getItemView() {
        return itemView;
    }
}
