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
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private final int mSpace;
    private final int mOrientation;

    /**
     * 设置子项间距离
     *
     * @param space       间距离
     * @param orientation 间距离方向, {@link RecyclerView#HORIZONTAL} 或 {@link RecyclerView#VERTICAL} 或任意数前面两者都设置
     */
    public SpaceItemDecoration(int space, int orientation) {
        mSpace = space;
        mOrientation = orientation;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull
            RecyclerView.State state) {
        if (mOrientation == RecyclerView.VERTICAL) {
            outRect.bottom = mSpace;
        } else if (mOrientation == RecyclerView.HORIZONTAL) {
            outRect.right = mSpace;
        } else {
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                int headerCount = 0;
                if (parent.getAdapter() instanceof HeaderViewRecyclerAdapter) {
                    headerCount = ((HeaderViewRecyclerAdapter) parent.getAdapter()).getHeaderCount();
                }
                GridLayoutManager manager = (GridLayoutManager) layoutManager;
                int spanCount = manager.getSpanCount();
                int position = parent.getChildAdapterPosition(view) - headerCount;
                if (position >= spanCount) {
                    outRect.top = mSpace;
                }
                if ((position + 1) % spanCount != 0) {
                    outRect.right = mSpace;
                }
                return;
            }
            outRect.bottom = mSpace;
            outRect.right = mSpace;
        }
    }
}
