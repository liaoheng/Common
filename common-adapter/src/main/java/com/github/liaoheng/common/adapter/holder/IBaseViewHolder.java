package com.github.liaoheng.common.adapter.holder;

import android.content.Context;
import android.view.View;

import androidx.annotation.IdRes;

/**
 * @author liaoheng
 * @version 2016-06-13 18:31
 */
public interface IBaseViewHolder<K> {
    void onHandle(K item, int position);

    void onHandle(K item, int position,Object args);

    <T extends View> T findViewById(@IdRes int id);

    Context getContext();
}
