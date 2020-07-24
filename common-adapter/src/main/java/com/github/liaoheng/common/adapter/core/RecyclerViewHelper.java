package com.github.liaoheng.common.adapter.core;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.liaoheng.common.adapter.R;
import com.github.liaoheng.common.adapter.base.IBaseRecyclerAdapter;
import com.github.liaoheng.common.adapter.internal.HeaderViewRecyclerAdapter;
import com.github.liaoheng.mugen.Mugen;
import com.github.liaoheng.mugen.MugenCallbacks;

/**
 * 使用layout: {@link R.layout#lca_view_list} 或使用相同id控件,基本实现：
 * <pre>
 *  <lo>基于<a href="https://github.com/vinaysshenoy/mugen">Mugen</a>实现上拉加载</lo>
 *  <lo>基于<a href="https://developer.android.com/training/swipe/add-swipe-interface.html">SwipeRefreshLayout</a>实现下拉刷新</lo>
 *  <lo>基于{@link HeaderViewRecyclerAdapter}实现RecyclerView中添加在头view与尾view</lo>
 * </pre>
 *
 * @author liaoheng
 * @version 2016-12-8 16:10
 */
public class RecyclerViewHelper {

    /**
     * 当前数据是否在加载中
     */
    private boolean mLoadMoreLoading;
    /**
     * 全部数据加载是否以加载完成  ，true 完成 ，false 相反
     */
    private boolean mHasLoadedAllItems;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private HeaderViewRecyclerAdapter mHeaderViewRecyclerAdapter;
    /**
     * 数据全部加载完成显示view
     */
    private View mNoneView;
    /**
     * 当前数据加载中显示view
     */
    private View mLoadingView;
    private LoadMoreListener mLoadMoreListener;
    private RefreshListener mRefreshListener;

    /**
     * 在GridLayoutManager布局下，对头或尾的view设置为占用一行
     */
    public static class MergedIntoLineSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

        private GridLayoutManager mLayoutManager;
        private HeaderViewRecyclerAdapter mViewRecyclerAdapter;

        public MergedIntoLineSpanSizeLookup(GridLayoutManager layoutManager,
                HeaderViewRecyclerAdapter viewRecyclerAdapter) {
            mLayoutManager = layoutManager;
            mViewRecyclerAdapter = viewRecyclerAdapter;
        }

        @Override
        public int getSpanSize(int position) {
            if (mViewRecyclerAdapter != null) {
                if ((mViewRecyclerAdapter.hasHeader() && mViewRecyclerAdapter.isHeader(position))
                        || mViewRecyclerAdapter.hasFooter() && mViewRecyclerAdapter
                        .isFooter(position)) {
                    return mLayoutManager.getSpanCount();//当前item为头或者尾view时，当前view占用一行
                }
            }
            return 1;
        }
    }

    /**
     * see {@link Builder#Builder(Activity)}
     */
    public static Builder newBuilder(@NonNull Activity activity) {
        return new Builder(activity);
    }

    /**
     * see {@link Builder#Builder(Context, View)}
     */
    public static Builder newBuilder(@NonNull Context context, @NonNull View contentView) {
        return new Builder(context, contentView);
    }

    public static class Builder {
        private Context context;
        private RecyclerView recyclerView;
        private SwipeRefreshLayout swipeRefreshLayout;
        private HeaderViewRecyclerAdapter headerViewRecyclerAdapter;
        private View none, load;
        private LoadMoreListener loadMoreListener;
        private RefreshListener refreshListener;
        private IBaseRecyclerAdapter.OnItemClickListener<?> onItemClickListener;
        private IBaseRecyclerAdapter.OnItemLongClickListener<?> onItemLongClickListener;
        private RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter;

        /**
         * 不初始化布局Layout ，需要在之后第一顺序调用{@link Builder#initView}或自己传入相应控件
         *
         * @param context {@link Context}
         */
        public Builder(@NonNull Context context) {
            this.context = context;
        }

        /**
         * 初始化布局Layout
         *
         * @param context     {@link Context}
         * @param contentView 使用layout: {@link R.layout#lca_view_list} 或使用相同id控件
         */
        public Builder(@NonNull Context context, @NonNull View contentView) {
            this(context, contentView, null);
        }

        /**
         * 初始化布局Layout
         *
         * @param context       {@link Context}
         * @param contentView   使用layout: {@link R.layout#lca_view_list} 或使用相同id控件
         * @param layoutManager {@link RecyclerView.LayoutManager} 为空时使用LinearLayoutManager
         */
        public Builder(@NonNull Context context, @NonNull View contentView,
                RecyclerView.LayoutManager layoutManager) {
            this.context = context;
            initView(contentView, layoutManager);
        }

        /**
         * 初始化布局Layout
         *
         * @param activity {@link Activity}
         */
        public Builder(@NonNull Activity activity) {
            this(activity, activity.getWindow().getDecorView());
        }

        /**
         * 初始化布局Layout
         *
         * @param activity      {@link Activity}
         * @param layoutManager {@link RecyclerView.LayoutManager} 为空时使用LinearLayoutManager
         */
        public Builder(@NonNull Activity activity,
                @NonNull RecyclerView.LayoutManager layoutManager) {
            this(activity, activity.getWindow().getDecorView(), layoutManager);
        }

        /**
         * 初始化布局Layout
         *
         * @param contentView   使用layout: {@link R.layout#lca_view_list} 或使用相同id控件
         * @param layoutManager {@link RecyclerView.LayoutManager} 为空时使用LinearLayoutManager
         */
        public void initView(@NonNull View contentView, RecyclerView.LayoutManager layoutManager) {
            recyclerView = (RecyclerView) contentView.findViewById(R.id.lca_list_recycler_view);
            getRecyclerView();
            if (swipeRefreshLayout == null) {
                swipeRefreshLayout = (SwipeRefreshLayout) contentView
                        .findViewById(R.id.lca_list_swipe_container);
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setEnabled(false);
                }
            }
            setLayoutManager(layoutManager);
        }

        /**
         * 初始化布局Layout
         *
         * @param activity      {@link Activity}
         * @param layoutManager {@link RecyclerView.LayoutManager}
         */
        public void initView(@NonNull Activity activity, RecyclerView.LayoutManager layoutManager) {
            initView(activity.getWindow().getDecorView(), layoutManager);
        }

        public Builder setSwipeRefreshLayout(@NonNull SwipeRefreshLayout swipeRefreshLayout) {
            this.swipeRefreshLayout = swipeRefreshLayout;
            return this;
        }

        public Builder setRecyclerView(@NonNull RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
            return this;
        }

        /**
         * @see RecyclerView#setLayoutManager(RecyclerView.LayoutManager)
         */
        public Builder setLayoutManager(RecyclerView.LayoutManager layoutManager) {
            if (layoutManager == null) {
                layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,
                        false);
            }
            getRecyclerView().setLayoutManager(layoutManager);
            return this;
        }

        /**
         * 开启水平下滑线
         */
        public Builder enableHorizontalDividerLineResId(@ColorRes int color) {
            return enableHorizontalDividerLine(ContextCompat.getColor(context, color));
        }

        /**
         * 开启垂直下滑线
         */
        public Builder enableVerticalDividerLineResId(@ColorRes int color) {
            return enableVerticalDividerLine(ContextCompat.getColor(context, color));
        }

        /**
         * 开启水平下滑线
         */
        public Builder enableHorizontalDividerLine(@ColorInt int color) {
            return addItemDecoration(
                    new HorizontalDividerItemDecoration.Builder(context).color(color).build());
        }

        /**
         * 开启垂直下滑线
         */
        public Builder enableVerticalDividerLine(@ColorInt int color) {
            return addItemDecoration(
                    new VerticalDividerItemDecoration.Builder(context).color(color).build());
        }

        /**
         * @see RecyclerView#addItemDecoration(RecyclerView.ItemDecoration)
         */
        public Builder addItemDecoration(RecyclerView.ItemDecoration decor) {
            getRecyclerView().addItemDecoration(decor);
            return this;
        }

        /**
         * 在计算每个item宽或者高时是否为固定值
         *
         * @see RecyclerView#setHasFixedSize(boolean)
         */
        public Builder setHasFixedSize(boolean hasFixedSize) {
            getRecyclerView().setHasFixedSize(hasFixedSize);
            return this;
        }

        /**
         * @see RecyclerView#setItemAnimator(RecyclerView.ItemAnimator)
         */
        public Builder setItemAnimator(RecyclerView.ItemAnimator itemAnimator) {
            getRecyclerView().setItemAnimator(itemAnimator);
            return this;
        }

        /**
         * 在GridLayoutManager布局下，对使用了头或尾的view情况下，设置view占用一行
         */
        public Builder setMergedIntoLineSpanSizeLookup() {
            if (headerViewRecyclerAdapter != null && getRecyclerView().getLayoutManager() != null
                    && getRecyclerView().getLayoutManager() instanceof GridLayoutManager) {
                GridLayoutManager gridLayoutManager = (GridLayoutManager) getRecyclerView()
                        .getLayoutManager();
                setSpanSizeLookup(new MergedIntoLineSpanSizeLookup(gridLayoutManager,
                        headerViewRecyclerAdapter));
            }
            return this;
        }

        /**
         * @see GridLayoutManager#setSpanSizeLookup(GridLayoutManager.SpanSizeLookup)
         */
        public Builder setSpanSizeLookup(GridLayoutManager.SpanSizeLookup spanSizeLookup) {
            if (getRecyclerView().getLayoutManager() != null && getRecyclerView()
                    .getLayoutManager() instanceof GridLayoutManager) {
                GridLayoutManager gridLayoutManager = (GridLayoutManager) getRecyclerView()
                        .getLayoutManager();
                gridLayoutManager.setSpanSizeLookup(spanSizeLookup);
            }
            return this;
        }

        /**
         * 添加底部上拉加载过程view ,默认使用{@link R.layout#lca_view_list_footer}
         */
        public Builder addLoadMoreFooterView() {
            addLoadMoreFooterView(LayoutInflater.from(context)
                    .inflate(R.layout.lca_view_list_footer, getRecyclerView(), false));
            return this;
        }

        /**
         * 添加底部上拉加载过程view
         *
         * @param footer 使用与{@link R.layout#lca_view_list_footer}中对应控件ID
         */
        public Builder addLoadMoreFooterView(@NonNull View footer) {
            none = footer.findViewById(R.id.lca_list_footer_none_btn);
            load = footer.findViewById(R.id.lca_list_footer_load);
            addFooterView(footer);
            return this;
        }

        /**
         * 添加底部上拉加载过程view
         *
         * @param footerRes 使用与{@link R.layout#lca_view_list_footer}中对应控件ID
         */
        public Builder addLoadMoreFooterView(@LayoutRes int footerRes,
                @NonNull HandleView handleView) {
            View footer = LayoutInflater.from(context).inflate(footerRes, getRecyclerView(), false);
            addLoadMoreFooterView(footer, handleView);
            return this;
        }

        /**
         * 添加底部上拉加载过程view
         *
         * @param handleView {@link HandleView#layout(Context, ViewGroup)} 返回{@link R.layout#lca_view_list_footer}中对应控件ID
         */
        public Builder addLoadMoreFooterView(@NonNull HandleView handleView) {
            return addLoadMoreFooterView(handleView.layout(context, getRecyclerView()), handleView);
        }

        /**
         * 添加底部上拉加载过程view
         *
         * @param footer 使用与{@link R.layout#lca_view_list_footer}中对应控件ID
         */
        public Builder addLoadMoreFooterView(@NonNull View footer, @NonNull HandleView handleView) {
            handleView.handle(footer);
            addLoadMoreFooterView(footer);
            return this;
        }

        /**
         * 在RecyclerView中添加尾view
         *
         * @param headerRes 使用与{@link R.layout#lca_view_list_footer}中对应控件ID
         */
        public Builder addFooterView(@LayoutRes int headerRes, @NonNull HandleView handleView) {
            View footer = LayoutInflater.from(context).inflate(headerRes, getRecyclerView(), false);
            return addFooterView(footer, handleView);
        }

        /**
         * 在RecyclerView中添加尾view
         */
        public Builder addFooterView(@NonNull HandleView handleView) {
            return addFooterView(handleView.layout(context, getRecyclerView()), handleView);
        }

        /**
         * 在RecyclerView中添加尾view
         *
         * @param footer 使用与{@link R.layout#lca_view_list_footer}中对应控件ID
         */
        public Builder addFooterView(View footer, @NonNull HandleView handleView) {
            handleView.handle(footer);
            addFooterView(footer);
            return this;
        }

        /**
         * 在RecyclerView中添加尾view
         */
        public Builder addFooterView(View view) {
            if (view == null) {
                return this;
            }
            if (headerViewRecyclerAdapter == null) {
                headerViewRecyclerAdapter = new HeaderViewRecyclerAdapter();
            }
            headerViewRecyclerAdapter.addFooterView(view);
            return this;
        }

        /**
         * 在RecyclerView中添加头view
         */
        public Builder addHeaderView(@LayoutRes int headerRes, @NonNull HandleView handleView) {
            View header = LayoutInflater.from(context).inflate(headerRes, getRecyclerView(), false);
            return addHeaderView(header, handleView);
        }

        /**
         * 在RecyclerView中添加头view
         */
        public Builder addHeaderView(@NonNull HandleView handleView) {
            return addHeaderView(handleView.layout(context, getRecyclerView()), handleView);
        }

        /**
         * 在RecyclerView中添加头view
         */
        public Builder addHeaderView(View header, @NonNull HandleView handleView) {
            handleView.handle(header);
            addHeaderView(header);
            return this;
        }

        /**
         * 在RecyclerView中添加头view
         */
        public Builder addHeaderView(View view) {
            if (view == null) {
                return this;
            }
            if (headerViewRecyclerAdapter == null) {
                headerViewRecyclerAdapter = new HeaderViewRecyclerAdapter();
            }
            headerViewRecyclerAdapter.addHeaderView(view);
            return this;
        }

        /**
         * 设置回调并打开 <a href="https://developer.android.com/training/swipe/add-swipe-interface.html">下拉刷新</a> 和<a href="https://github.com/vinaysshenoy/mugen">上拉加载</a>
         */
        public Builder setLoadMoreAndRefreshListener(RefreshListener refreshListener,
                LoadMoreListener loadMoreListener) {
            setRefreshListener(refreshListener);
            setLoadMoreListener(loadMoreListener);
            return this;
        }

        /**
         * 设置回调并打开 <a href="https://developer.android.com/training/swipe/add-swipe-interface.html">下拉刷新</a>
         */
        public Builder setRefreshListener(RefreshListener refreshListener) {
            this.refreshListener = refreshListener;
            return this;
        }

        /**
         * 设置回调并打开 <a href="https://github.com/vinaysshenoy/mugen">上拉加载</a>
         */
        public Builder setLoadMoreListener(LoadMoreListener loadMoreListener) {
            this.loadMoreListener = loadMoreListener;
            return this;
        }

        /**
         * 当上拉加载数据全部完成时，在显示的view上添加点击事件回调
         */
        public Builder setFooterLoadMoreListener(final LoadMoreListener loadMoreListener) {
            if (none != null) {
                none.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (loadMoreListener != null) {
                            loadMoreListener.onLoadMore();
                        }
                    }
                });
            }
            return this;
        }

        /**
         * 当上拉加载数据全部完成时，在显示的view上添加点击事件使用与下拉加载回调一致
         */
        public Builder setFooterLoadMoreListener() {
            if (none != null) {
                none.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (loadMoreListener != null) {
                            loadMoreListener.onLoadMore();
                        }
                    }
                });
            }
            return this;
        }

        public Builder setOnItemClickListener(
                @NonNull IBaseRecyclerAdapter.OnItemClickListener<?> listener) {
            this.onItemClickListener = listener;
            return this;
        }

        public Builder setOnItemLongClickListener(
                @NonNull IBaseRecyclerAdapter.OnItemLongClickListener<?> listener) {
            this.onItemLongClickListener = listener;
            return this;
        }

        public Builder setAdapter(
                @NonNull RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter) {
            this.adapter = adapter;
            return this;
        }

        public Builder addRecyclerViewOnScrollListener(RecyclerView.OnScrollListener listener) {
            getRecyclerView().addOnScrollListener(listener);
            return this;
        }

        private RecyclerView getRecyclerView() {
            if (recyclerView == null) {
                throw new IllegalArgumentException("RecyclerView is null");
            }
            return recyclerView;
        }

        @SuppressWarnings("unchecked")
        public RecyclerViewHelper build() {
            if (adapter != null) {
                if (adapter instanceof IBaseRecyclerAdapter) {
                    IBaseRecyclerAdapter recyclerAdapter = ((IBaseRecyclerAdapter) adapter);
                    if (onItemClickListener != null) {
                        recyclerAdapter.setOnItemClickListener(onItemClickListener);
                    }
                    if (onItemLongClickListener != null) {
                        recyclerAdapter.setOnItemLongClickListener(onItemLongClickListener);
                    }
                }
                if (headerViewRecyclerAdapter != null) {
                    headerViewRecyclerAdapter.setAdapter(adapter);
                    recyclerView.setAdapter(headerViewRecyclerAdapter);
                } else {
                    recyclerView.setAdapter(adapter);
                }
            }
            return new RecyclerViewHelper(recyclerView, swipeRefreshLayout,
                    headerViewRecyclerAdapter, none, load, loadMoreListener, refreshListener);
        }
    }

    private RecyclerViewHelper(RecyclerView mRecyclerView, SwipeRefreshLayout mSwipeRefreshLayout,
            HeaderViewRecyclerAdapter mHeaderViewRecyclerAdapter, View noneView,
            View loadingView, LoadMoreListener mLoadMoreListener,
            RefreshListener mRefreshListener) {
        this.mRecyclerView = mRecyclerView;
        this.mSwipeRefreshLayout = mSwipeRefreshLayout;
        this.mHeaderViewRecyclerAdapter = mHeaderViewRecyclerAdapter;
        this.mNoneView = noneView;
        this.mLoadingView = loadingView;
        this.mLoadMoreListener = mLoadMoreListener;
        this.mRefreshListener = mRefreshListener;
        attachLoadMoreListener();
        attachRefreshListener();
    }

    /**
     * etc:
     * <pre>
     *     refresh action callback{
     *          loading{
     *              setSwipeRefreshing(true)
     *          }
     *          done{
     *              setSwipeRefreshing(false)
     *          }
     *      }
     *  </pre>
     * 更新SwipeRefreshLayout下拉更新进度条状态
     *
     * @param refresh true 进行中 false 完成
     */
    public void setSwipeRefreshing(final boolean refresh) {
        if (mSwipeRefreshLayout == null) {
            return;
        }
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(refresh);
            }
        });
    }

    /**
     * etc:
     * <pre>
     *      loadMore action callback{
     *          loading{
     *              setLoadMoreLoading(true)
     *          }
     *          done{
     *              setLoadMoreLoading(false)
     *          }
     *      }
     *  </pre>
     * 判断当前是否下拉加载数据中
     *
     * @param loading true 进行中，false 空闲
     */
    public void setLoadMoreLoading(boolean loading) {
        mLoadMoreLoading = loading;
    }

    /**
     * etc:
     * <pre>
     *      callback{
     *         done{
     *              setLoadMoreHasLoadedAllItems(curPage >= pageTotal)
     *         }
     *      }
     *  </pre>
     * 判断当前下拉加载全部数据是否加载完成
     *
     * @param hasLoaded true 加载完成，false 相反
     */
    public void setLoadMoreHasLoadedAllItems(boolean hasLoaded) {
        mHasLoadedAllItems = hasLoaded;
        setNoneViewDisplayState();
        setLoadingViewDisplayState();
    }

    /**
     * 得到当前上拉加载全部数据是否完成
     *
     * @return true 加载完成，false 相反
     */
    public boolean getLoadMoreHasLoadedAllItems() {
        return mHasLoadedAllItems;
    }

    /**
     * 改变为上拉加载数据全部完成状态
     */
    public void changeToLoadMoreComplete() {
        setLoadMoreHasLoadedAllItems(true);
    }

    /**
     * 改变为上拉加载数据进行中状态
     */
    public void changeToLoadMoreLoading() {
        setLoadMoreHasLoadedAllItems(false);
        setNoneViewDisplayState();
        setLoadingViewDisplayState();
    }

    /**
     * 当前上拉加载全部数据完成，显示NoneView
     */
    private void setNoneViewDisplayState() {
        if (mNoneView != null) {
            if (getLoadMoreHasLoadedAllItems()) {
                mNoneView.setVisibility(View.VISIBLE);
            } else {
                mNoneView.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 当前上拉加载全部数据未完成，显示LoadingView
     */
    private void setLoadingViewDisplayState() {
        if (mLoadingView != null) {
            if (getLoadMoreHasLoadedAllItems()) {
                mLoadingView.setVisibility(View.GONE);
            } else {
                mLoadingView.setVisibility(View.VISIBLE);
            }
        }
    }

    public void attachLoadMoreListener() {
        if (mLoadMoreListener == null) {
            return;
        }
        Mugen.with(mRecyclerView, new MugenCallbacks() {

            @Override
            public void onLoadMore() {
                mLoadMoreListener.onLoadMore();
            }

            @Override
            public boolean isLoading() {
                return mLoadMoreLoading;
            }

            @Override
            public boolean hasLoadedAllItems() {
                return getLoadMoreHasLoadedAllItems();
            }
        }).start();
    }

    public void attachRefreshListener() {
        if (mRefreshListener == null) {
            return;
        }
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setEnabled(true);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

                @Override
                public void onRefresh() {
                    mRefreshListener.onRefresh();
                }
            });
        }
    }

    /**
     * 下拉加载监听
     */
    public interface LoadMoreListener {
        void onLoadMore();
    }

    /**
     * 上拉刷新监听
     */
    public interface RefreshListener {
        void onRefresh();
    }

    public RecyclerViewHelper setAdapter(
            @NonNull RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter) {
        if (mHeaderViewRecyclerAdapter == null) {
            mRecyclerView.setAdapter(adapter);
        } else {
            //之前添加过刷新
            if (mHeaderViewRecyclerAdapter.getWrappedAdapter() == adapter) {
                mHeaderViewRecyclerAdapter.notifyDataSetChanged();
                return this;
            }
            mHeaderViewRecyclerAdapter.setAdapter(adapter);
            mRecyclerView.setAdapter(mHeaderViewRecyclerAdapter);
        }
        return this;
    }

    @Deprecated
    public RecyclerViewHelper setOnItemClickListener(
            IBaseRecyclerAdapter.OnItemClickListener listener) {
        RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
        if (adapter == null) {
            return this;
        }
        if (adapter instanceof HeaderViewRecyclerAdapter) {
            adapter = ((HeaderViewRecyclerAdapter) adapter).getWrappedAdapter();
        }
        if (adapter instanceof IBaseRecyclerAdapter) {
            ((IBaseRecyclerAdapter) adapter).setOnItemClickListener(listener);
        }
        return this;
    }

    @Deprecated
    public RecyclerViewHelper setOnItemLongClickListener(
            IBaseRecyclerAdapter.OnItemLongClickListener listener) {
        RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
        if (adapter == null) {
            return this;
        }
        if (adapter instanceof HeaderViewRecyclerAdapter) {
            adapter = ((HeaderViewRecyclerAdapter) adapter).getWrappedAdapter();
        }
        if (adapter instanceof IBaseRecyclerAdapter) {
            ((IBaseRecyclerAdapter) adapter).setOnItemLongClickListener(listener);
        }
        return this;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }
}
