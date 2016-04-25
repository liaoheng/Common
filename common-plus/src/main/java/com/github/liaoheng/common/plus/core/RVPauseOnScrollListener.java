package com.github.liaoheng.common.plus.core;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.squareup.picasso.Picasso;

/**
 * RecyclerView滑动时停止加载图片
 * @author liaoheng
 * @version 2015-04-28 10:41
 */
public class RVPauseOnScrollListener extends RecyclerView.OnScrollListener {

    private Context       mContext;
    private Picasso       picasso;
    private final boolean pauseOnScroll;
    private final boolean pauseOnSettling;

    /**
     *RecyclerView滑动时停止加载图片
     * @param context {@link Context}
     * @param picasso {@link Picasso}
     * @param pauseOnScroll 拖曳中是否停止
     * @param pauseOnSettling 无动作滑行移动中是否停止
     */
    public RVPauseOnScrollListener(Context context, Picasso picasso, boolean pauseOnScroll,
                                   boolean pauseOnSettling) {
        this.picasso = picasso;
        this.mContext = context;
        this.pauseOnScroll = pauseOnScroll;
        this.pauseOnSettling = pauseOnSettling;
    }

    /**
     *RecyclerView滑动时停止加载图片
     * @param context {@link Context}
     * @param picasso {@link Picasso}
     */
    public RVPauseOnScrollListener(Context context, Picasso picasso) {
        this(context,picasso,true,true);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        switch (newState) {
            case RecyclerView.SCROLL_STATE_IDLE://空闲
                picasso.resumeTag(mContext);
                break;
            case RecyclerView.SCROLL_STATE_DRAGGING://拖曳中
                if (pauseOnScroll) {
                    picasso.pauseTag(mContext);
                }
                break;
            case RecyclerView.SCROLL_STATE_SETTLING://无动作滑行移动中
                if (pauseOnSettling) {
                    picasso.pauseTag(mContext);
                }
                break;
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
    }
}
