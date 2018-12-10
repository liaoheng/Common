package com.github.liaoheng.common.adapter.core;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

/**
 * RecyclerView滑动时停止加载图片
 * @author liaoheng
 * @version 2015-04-28 10:41
 */
public class RVPauseOnScrollListener extends RecyclerView.OnScrollListener {

    private       ScrollEventListener mListener;
    private final boolean             pauseOnScroll;
    private final boolean             pauseOnSettling;

    public interface ScrollEventListener {
        void onPause(Context context);

        void onResume(Context context);
    }

    /**
     *RecyclerView滑动时停止加载图片
     * @param listener {@link ScrollEventListener}
     * @param pauseOnScroll 拖曳中是否停止
     * @param pauseOnSettling 无动作滑行移动中是否停止
     */
    public RVPauseOnScrollListener(ScrollEventListener listener, boolean pauseOnScroll,
                                   boolean pauseOnSettling) {
        this.mListener = listener;
        this.pauseOnScroll = pauseOnScroll;
        this.pauseOnSettling = pauseOnSettling;
    }

    /**
     *RecyclerView滑动时停止加载图片
     * @param listener {@link ScrollEventListener}
     */
    public RVPauseOnScrollListener(ScrollEventListener listener) {
        this(listener, true, true);
    }

    @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        Context context = recyclerView.getContext();
        switch (newState) {
            case RecyclerView.SCROLL_STATE_IDLE://空闲
                mListener.onResume(context);
                break;
            case RecyclerView.SCROLL_STATE_DRAGGING://拖曳中
                if (pauseOnScroll) {
                    mListener.onPause(context);
                }
                break;
            case RecyclerView.SCROLL_STATE_SETTLING://无动作滑行移动中
                if (pauseOnSettling) {
                    mListener.onPause(context);
                }
                break;
        }
    }

    @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
    }
}
