package com.github.liaoheng.common.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.github.liaoheng.common.ui.R;
import com.github.liaoheng.common.util.DisplayUtils;

import java.util.concurrent.TimeUnit;

/**
 * 基于{@link ViewPager2}的换灯片，支持水平或者上下滑动
 * <pre>
 * &lt;com.bw.iqiyi.view.SlideViewPager
 *     android:orientation=&quot;vertical&quot;
 *     android:layout_width=&quot;match_parent&quot;
 *     android:layout_height=&quot;match_parent&quot;&gt;
 * &lt;/com.bw.iqiyi.view.SlideViewPager&gt;
 * </pre>
 *
 * @author liaoheng
 * @date 2021-09-22 15:04
 */
public class SlideViewPager extends FrameLayout {
    public ViewPager2 mViewPager;
    /**
     * 幻灯片指示器
     */
    public LinearLayout mSlidesDots;

    public SlideViewPager(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public SlideViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public void init(@NonNull Context context, @Nullable AttributeSet attrs) {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mViewPager = new ViewPager2(context, attrs);
        mViewPager.registerOnPageChangeCallback(mOnPageChangeCallback);
        setOrientation(context, attrs);
        addView(mViewPager, layoutParams);
    }

    private void setOrientation(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SlideViewPager);
        if (Build.VERSION.SDK_INT >= 29) {
            saveAttributeDataForStyleable(context, R.styleable.SlideViewPager, attrs, a, 0, 0);
        }
        try {
            mViewPager.setOrientation(
                    a.getInt(R.styleable.SlideViewPager_android_orientation, ViewPager2.ORIENTATION_HORIZONTAL));
        } finally {
            a.recycle();
        }
    }

    public void setAdapter(@Nullable @SuppressWarnings("rawtypes") RecyclerView.Adapter adapter) {
        mViewPager.setAdapter(adapter);
    }

    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                if (mViewPager == null || mViewPager.getAdapter() == null) {
                    return;
                }
                int currentItem = mViewPager.getCurrentItem() + 1;
                if (currentItem >= mViewPager.getAdapter().getItemCount()) {
                    currentItem = 0;
                }
                mViewPager.setCurrentItem(currentItem);
                turnPage();
            }
        }
    };

    /**
     * 延时翻页
     */
    private void turnPage() {
        Message message = mHandler.obtainMessage();
        message.what = 1;
        mHandler.sendMessageDelayed(message, TimeUnit.MINUTES.toMillis(1));
    }

    /**
     * 开启幻灯片播放
     */
    public void enableSlide() {
        turnPage();
        initDots(0);
    }

    /**
     * 停止幻灯片播放
     */
    public void disableSlide() {
        if (mViewPager != null) {
            mViewPager.registerOnPageChangeCallback(mOnPageChangeCallback);
        }
        mHandler.removeMessages(1);
    }

    public void setDotView(LinearLayout view) {
        mSlidesDots = view;
    }

    /**
     * 初始化指示器
     */
    public void initDots(int select) {
        if (mViewPager == null || mViewPager.getAdapter() == null) {
            return;
        }
        if (mSlidesDots == null) {
            return;
        }
        mSlidesDots.removeAllViews();
        int margin = DisplayUtils.dp2px(getContext(), 4);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(margin, margin, margin, margin);
        for (int i = 0; i < mViewPager.getAdapter().getItemCount(); i++) {
            ImageView dot = new ImageView(getContext());
            mSlidesDots.addView(dot, params);
        }
        updateDots(select);
    }

    /**
     * 更新指示器
     */
    public void updateDots(int select) {
        if (mSlidesDots == null) {
            return;
        }
        for (int i = 0; i < mSlidesDots.getChildCount(); i++) {
            if (i == select) {
                ((ImageView) mSlidesDots.getChildAt(i)).setImageResource(R.drawable.lcu_ic_slides_selected_dot);
            } else {
                ((ImageView) mSlidesDots.getChildAt(i)).setImageResource(R.drawable.lcu_ic_slides_unselected_dot);
            }
        }
    }

    private final ViewPager2.OnPageChangeCallback mOnPageChangeCallback = new ViewPager2.OnPageChangeCallback() {

        @Override
        public void onPageSelected(int position) {
            updateDots(position);
        }
    };
}
