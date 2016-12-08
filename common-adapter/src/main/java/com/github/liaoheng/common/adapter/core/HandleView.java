package com.github.liaoheng.common.adapter.core;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author liaoheng
 * @version 2016-11-30
 */
public interface HandleView {
    View layout(Context context, ViewGroup parent);
    void handle(View view);

    abstract class EmptyHandleView implements HandleView{
        @Override public View layout(Context context, ViewGroup parent) {
            return null;
        }
    }
}
