package com.github.liaoheng.common.adapter.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.function.Consumer;

/**
 * 分页加载{@link RecyclerView}
 *
 * @author liaoheng
 * @date 2021-09-27 10:27
 * @see <a href="https://blog.csdn.net/qq_33337504/article/details/103807033">csdn</a>
 */
public class LoadMoreRecyclerView extends RecyclerView {
    public LoadMoreRecyclerView(@NonNull Context context) {
        super(context);
        init();
    }

    public LoadMoreRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadMoreRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private int lastVisibleItemPosition;
    /**
     * 数据是否加载中
     */
    private boolean mIsLoading;
    /**
     * 是否还有数据
     */
    private boolean mHasLoadingMore;
    private Consumer<Object> mCallback;

    public void setLoading(boolean loading) {
        mIsLoading = loading;
    }

    public void setHasLoadingMore(boolean hasLoadingMore) {
        mHasLoadingMore = hasLoadingMore;
    }

    public void setLoadMoreCallback(Consumer<Object> mCallback) {
        this.mCallback = mCallback;
    }

    private void init() {
        setItemAnimator(null);//需要去掉动画
        addOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (getLinearLayoutManager() == null) {
                        return;
                    }
                    int totalItemCount = getLinearLayoutManager().getItemCount();
                    //有可加载数据，不在加载中，最后一排时加载数据
                    if (mHasLoadingMore && !mIsLoading && lastVisibleItemPosition >= totalItemCount - 1) {
                        mCallback.accept(null);
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (getLinearLayoutManager() == null) {
                    return;
                }
                lastVisibleItemPosition = getLinearLayoutManager().findLastVisibleItemPosition();
            }
        });
    }

    private LinearLayoutManager getLinearLayoutManager() {
        if (getLayoutManager() instanceof LinearLayoutManager) {
            return (LinearLayoutManager) getLayoutManager();
        }
        return null;
    }

}
