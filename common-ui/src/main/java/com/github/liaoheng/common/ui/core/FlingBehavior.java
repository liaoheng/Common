package com.github.liaoheng.common.ui.core;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.google.android.material.appbar.AppBarLayout;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

/**
 * <a href="http://www.jianshu.com/p/4535442d568f">解决RecyclerView折叠动画不平滑问题</a>
 * <p>
 * <pre>
 *             CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
 *                 ViewGroup.LayoutParams.WRAP_CONTENT);
 *               params.setBehavior(new FlingBehavior());
 *                 mAppBarLayout.setLayoutParams(params);
 *  </pre>
 * </p>
 *
 * @author liaoheng
 * @version 2017-06-26 17:15
 */
public final class FlingBehavior extends AppBarLayout.Behavior {
    private static final int TOP_CHILD_FLING_THRESHOLD = 3;
    private boolean isPositive;

    public FlingBehavior() {
    }

    public FlingBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float velocityX,
            float velocityY, boolean consumed) {
        if (velocityY > 0 && !isPositive || velocityY < 0 && isPositive) {
            velocityY = velocityY * -1;
        }
        if (target instanceof RecyclerView && velocityY < 0) {
            final RecyclerView recyclerView = (RecyclerView) target;
            final View firstChild = recyclerView.getChildAt(0);
            final int childAdapterPosition = recyclerView.getChildAdapterPosition(firstChild);
            consumed = childAdapterPosition > TOP_CHILD_FLING_THRESHOLD;
        }
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy,
            int[] consumed) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        isPositive = dy > 0;
    }
}
