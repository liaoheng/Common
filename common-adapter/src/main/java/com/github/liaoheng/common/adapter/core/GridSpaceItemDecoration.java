package com.github.liaoheng.common.adapter.core;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.liaoheng.common.adapter.internal.HeaderViewRecyclerAdapter;

/**
 * RecyclerView子项间距离
 *
 * @author liaoheng
 * @date 2021-09-16 09:53
 */
public class GridSpaceItemDecoration extends RecyclerView.ItemDecoration {
    private final int mSpace;
    private final int mItemWidth;

    /**
     * 设置子项间距离
     *
     * @param space     间距离
     * @param itemWidth item大小
     */
    public GridSpaceItemDecoration(int space, int itemWidth) {
        mSpace = space;
        mItemWidth = itemWidth;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull
            RecyclerView.State state) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            int headerCount = 0;
            if (parent.getAdapter() instanceof HeaderViewRecyclerAdapter) {
                headerCount = ((HeaderViewRecyclerAdapter) parent.getAdapter()).getHeaderCount();
            }
            GridLayoutManager manager = (GridLayoutManager) layoutManager;
            int spanCount = manager.getSpanCount();
            int position = parent.getChildAdapterPosition(view);
            if (position < headerCount) {
                return;
            }
            position = position - headerCount;
            if (position >= spanCount) {
                outRect.top = mSpace;
            }
            if (mItemWidth <= 0) {
                if ((position + 1) % spanCount != 0) {
                    outRect.right = mSpace;
                }
                return;
            }

            //固定itemWidth时，通过此方法平均分配
            //https://blog.csdn.net/qq_41872247/article/details/106764746
            double w = ((double) parent.getWidth() - mItemWidth * spanCount) / (spanCount * (spanCount - 1));
            int p = position % spanCount;
            outRect.left = (int) (w * p);
        }
    }
}
