package com.github.liaoheng.common.adapter.core;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import com.github.liaoheng.common.adapter.internal.HeaderViewRecyclerAdapter;
import com.github.liaoheng.common.adapter.R;
import com.mugen.Mugen;
import com.mugen.MugenCallbacks;

/**
 * RecyclerView帮助{@link R.layout#lca_view_list}
 *
 * @author liaoheng
 * @version 2015年10月19日
 */
public class RecyclerViewHelper {

    boolean                   loading;
    boolean                   hasLoadedAllItems;
    RecyclerView              mRecyclerView;
    SwipeRefreshLayout        mSwipeRefreshLayout;
    HeaderViewRecyclerAdapter mHeaderViewRecyclerAdapter;
    View                      none, load;
    LoadMoreListener mLoadMoreListener;
    RefreshListener  mRefreshListener;

    public static class KSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

        GridLayoutManager         mLayoutManager;
        HeaderViewRecyclerAdapter mViewRecyclerAdapter;

        public KSpanSizeLookup(GridLayoutManager mLayoutManager,
                               HeaderViewRecyclerAdapter mViewRecyclerAdapter) {
            this.mLayoutManager = mLayoutManager;
            this.mViewRecyclerAdapter = mViewRecyclerAdapter;
        }

        @Override public int getSpanSize(int position) {
            if (mViewRecyclerAdapter != null) {
                if ((mViewRecyclerAdapter.hasHeader() && mViewRecyclerAdapter.isHeader(position))
                    || mViewRecyclerAdapter.hasFooter() && mViewRecyclerAdapter
                        .isFooter(position)) {
                    return mLayoutManager.getSpanCount();
                }
            }
            return 1;
        }
    }

    public static class Builder {
        Context                    context;
        RecyclerView               recyclerView;
        SwipeRefreshLayout         swipeRefreshLayout;
        RecyclerView.LayoutManager layoutManager;
        HeaderViewRecyclerAdapter  headerViewRecyclerAdapter;
        View                       none, load;
        LoadMoreListener loadMoreListener;
        RefreshListener  refreshListener;

        public Builder(@NonNull Context context) {
            this.context = context;
        }

        public Builder(@NonNull Context context, @NonNull View contentView) {
            this.context = context;
            initView(contentView);
        }

        public Builder(@NonNull Activity activity) {
            this.context = activity;
            initView(activity);
        }

        public Builder setSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
            this.swipeRefreshLayout = swipeRefreshLayout;
            return this;
        }

        public Builder setRecyclerView(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
            return this;
        }

        public Builder setLayoutManager() {
            return setLayoutManager(null);
        }

        public Builder setLayoutManager(RecyclerView.LayoutManager layoutManager) {
            this.layoutManager = layoutManager;
            if (layoutManager == null) {
                layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,
                        false);
                getRecyclerView().setLayoutManager(layoutManager);
                getRecyclerView().setItemAnimator(new DefaultItemAnimator());
            } else {
                getRecyclerView().setLayoutManager(layoutManager);
            }
            return this;
        }

        /**
         * 添加底部加载{@link R.layout#lca_view_list_footer}
         *
         * @return
         */
        public Builder addLoadMoreFooterView() {
            addLoadMoreFooterView(LayoutInflater.from(context)
                    .inflate(R.layout.lca_view_list_footer, getRecyclerView(), false));
            return this;
        }

        /**
         * 添加底部加载{@link R.layout#lca_view_list_footer}
         *
         * @return
         */
        public Builder addLoadMoreFooterView(@NonNull View footer) {
            none = footer.findViewById(R.id.lca_list_footer_none_btn);
            load = footer.findViewById(R.id.lca_list_footer_load);
            addFooterView(footer);
            return this;
        }

        /**
         * 添加底部加载{@link R.layout#lca_view_list_footer}
         *
         * @return
         */
        public Builder addLoadMoreFooterView(@LayoutRes int footerRes, HandleView handleView) {
            View footer = LayoutInflater.from(context).inflate(footerRes, getRecyclerView(), false);
            handleView.handle(footer);
            addLoadMoreFooterView(footer);
            return this;
        }

        public GridLayoutManager.SpanSizeLookup getLoadMoreSpanSizeLookup(
                GridLayoutManager gridLayoutManager,
                HeaderViewRecyclerAdapter headerViewRecyclerAdapter) {
            return new KSpanSizeLookup(gridLayoutManager, headerViewRecyclerAdapter);
        }

        public Builder setSpanSizeLookup(GridLayoutManager.SpanSizeLookup spanSizeLookup) {
            if (layoutManager != null && layoutManager instanceof GridLayoutManager) {
                GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                gridLayoutManager.setSpanSizeLookup(spanSizeLookup);
            }
            return this;
        }

        public Builder addFooterView(View view) {
            if (headerViewRecyclerAdapter == null) {
                headerViewRecyclerAdapter = new HeaderViewRecyclerAdapter();
            }
            headerViewRecyclerAdapter.addFooterView(view);
            return this;
        }

        public Builder addHeaderView(@LayoutRes int headerRes, HandleView handleView) {
            View header = LayoutInflater.from(context).inflate(headerRes, getRecyclerView(), false);
            handleView.handle(header);
            addHeaderView(header);
            return this;
        }

        public Builder addHeaderView(View view) {
            if (headerViewRecyclerAdapter == null) {
                headerViewRecyclerAdapter = new HeaderViewRecyclerAdapter();
            }
            headerViewRecyclerAdapter.addHeaderView(view);
            return this;
        }

        public Builder setLoadMoreAndRefreshListener(RefreshListener refreshListener,
                                                     LoadMoreListener loadMoreListener) {
            setRefreshListener(refreshListener);
            setLoadMoreListener(loadMoreListener);
            return this;
        }

        public Builder setLoadMoreListener(LoadMoreListener loadMoreListener) {
            this.loadMoreListener = loadMoreListener;
            return this;
        }

        public Builder setRefreshListener(RefreshListener refreshListener) {
            this.refreshListener = refreshListener;
            return this;
        }

        public Builder setFooterLoadMoreListener(final LoadMoreListener loadMoreListener) {
            if (none != null) {
                none.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        if (loadMoreListener != null) {
                            loadMoreListener.onLoadMore();
                        }
                    }
                });
            }
            return this;
        }

        public Builder setFooterLoadMoreListener() {
            if (none != null) {
                none.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        if (loadMoreListener != null) {
                            loadMoreListener.onLoadMore();
                        }
                    }
                });
            }
            return this;
        }

        public Builder addRecyclerViewOnScrollListener(RecyclerView.OnScrollListener listener) {
            getRecyclerView().addOnScrollListener(listener);
            return this;
        }

        public RecyclerView getRecyclerView() {
            if (recyclerView == null) {
                throw new IllegalArgumentException("RecyclerView is null");
            }
            return recyclerView;
        }

        private void initView(@NonNull View contentView) {
            if (recyclerView == null) {
                recyclerView = (RecyclerView) contentView.findViewById(R.id.lca_list_recycler_view);
            }
            if (swipeRefreshLayout == null) {
                swipeRefreshLayout = (SwipeRefreshLayout) contentView
                        .findViewById(R.id.lca_list_swipe_container);
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setEnabled(false);
                }
            }
        }

        private void initView(@NonNull Activity activity) {
            initView(activity.getWindow().getDecorView());
        }

        public RecyclerViewHelper build() {
            return new RecyclerViewHelper(recyclerView, swipeRefreshLayout,
                    headerViewRecyclerAdapter, none, load, loadMoreListener, refreshListener);
        }
    }

    public RecyclerViewHelper(RecyclerView mRecyclerView, SwipeRefreshLayout mSwipeRefreshLayout,
                              HeaderViewRecyclerAdapter mHeaderViewRecyclerAdapter, View none,
                              View load, LoadMoreListener mLoadMoreListener,
                              RefreshListener mRefreshListener) {
        this.mRecyclerView = mRecyclerView;
        this.mSwipeRefreshLayout = mSwipeRefreshLayout;
        this.mHeaderViewRecyclerAdapter = mHeaderViewRecyclerAdapter;
        this.none = none;
        this.load = load;
        this.mLoadMoreListener = mLoadMoreListener;
        this.mRefreshListener = mRefreshListener;
        attachLoadMoreListener();
        attachRefreshListener();
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public void setRefreshCallback(final boolean refresh) {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override public void run() {
                mSwipeRefreshLayout.setRefreshing(refresh);
            }
        });
    }

    public void setHasLoadedAllItems(boolean hasLoaded) {
        this.hasLoadedAllItems = hasLoaded;
        noneVisible();
        loadVisible();
    }

    public boolean getHasLoadedAllItems() {
        return hasLoadedAllItems;
    }

    public void loaded() {
        setHasLoadedAllItems(true);
    }

    public void loading() {
        setHasLoadedAllItems(false);
        noneVisible();
        loadVisible();
    }

    private void noneVisible() {
        if (none != null) {
            if (getHasLoadedAllItems()) {
                none.setVisibility(View.VISIBLE);
            } else {
                none.setVisibility(View.GONE);
            }
        }
    }

    private void loadVisible() {
        if (load != null) {
            if (getHasLoadedAllItems()) {
                load.setVisibility(View.GONE);
            } else {
                load.setVisibility(View.VISIBLE);
            }
        }
    }

    public void attachLoadMoreListener() {
        if (mLoadMoreListener == null) {
            return;
        }
        Mugen.with(mRecyclerView, new MugenCallbacks() {

            @Override public void onLoadMore() {
                mLoadMoreListener.onLoadMore();
            }

            @Override public boolean isLoading() {
                return loading;
            }

            @Override public boolean hasLoadedAllItems() {
                return getHasLoadedAllItems();
            }
        }).start();
    }

    public void attachRefreshListener() {
        if (mRefreshListener == null) {
            return;
        }
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setEnabled(true);
            mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

                @Override public void onRefresh() {
                    mRefreshListener.onRefresh();
                }
            });
        }
    }

    public interface LoadMoreListener {
        void onLoadMore();
    }

    public interface RefreshListener {
        void onRefresh();
    }

    public void setAdapter(RecyclerView.Adapter<? extends ViewHolder> adapter) {
        if (mHeaderViewRecyclerAdapter == null) {
            mRecyclerView.setAdapter(adapter);
        } else {
            mHeaderViewRecyclerAdapter.setAdapter(adapter);
            mRecyclerView.setAdapter(mHeaderViewRecyclerAdapter);
        }
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }

    public HeaderViewRecyclerAdapter getHeaderViewRecyclerAdapter() {
        return mHeaderViewRecyclerAdapter;
    }
}
