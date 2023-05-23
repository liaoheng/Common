package com.github.liaoheng.common.ui.widget;

import android.database.DataSetObserver;
import android.os.Build;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.RequiresApi;

import com.github.liaoheng.common.ui.R;
import com.github.liaoheng.common.ui.adapter.PagerAdapter;
import com.github.liaoheng.common.ui.model.PagerTab;
import com.github.liaoheng.common.util.L;

import java.util.List;
import java.util.function.Consumer;

/**
 * 频道页面切换帮助类
 *
 * @author liaoheng
 * @date 2021-10-19 10:33
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class TabsHelper {
    private final String TAG = TabsHelper.class.getSimpleName();

    /**
     * 频道页面切换
     *
     * @param fragmentCallback 页面切换回调
     */
    public static TabsHelper with(TabLayout tabLayout, @LayoutRes int layout, FragmentCallback fragmentCallback) {
        return new TabsHelper(tabLayout, layout, fragmentCallback);
    }

    private final PagerAdapter mPagerAdapter;
    private final TabLayout mTabLayout;
    private final FragmentCallback mSelectFragmentCallback;
    @LayoutRes
    private final int itemLayout;
    private int tabId = -1;

    public TabLayout getTabLayout() {
        return mTabLayout;
    }

    /**
     * @param layout           导航条项自定义view
     * @param fragmentCallback 生成页面回调
     */
    private TabsHelper(TabLayout tabLayout, @LayoutRes int layout, FragmentCallback fragmentCallback) {
        mTabLayout = tabLayout;
        itemLayout = layout;
        mSelectFragmentCallback = fragmentCallback;
        mPagerAdapter = new PagerAdapter();
        mPagerAdapter.registerAdapterDataObserver(mTabPagerObserver);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getCustomView() != null) {
                    tab.getCustomView().findViewById(R.id.lcu_tabs_item_title).setSelected(true);
                }
                PagerTab nav = (PagerTab) tab.getTag();
                if (nav == null) {
                    return;
                }
                tabId = nav.getId();
                mPagerAdapter.notifyChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getCustomView() != null) {
                    tab.getCustomView().findViewById(R.id.lcu_tabs_item_title).setSelected(false);
                }
                PagerTab nav = (PagerTab) tab.getTag();
                if (nav == null) {
                    return;
                }
                if (nav.isTemp()) {
                    mTabLayout.removeTab(tab);
                    mPagerAdapter.remove(nav);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private final DataSetObserver mTabPagerObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            TabLayout.Tab tab = findTab(tabId);
            if (tab == null) {
                return;
            }
            createFragment(tab.getPosition());
        }
    };

    /**
     * 生成位置下的页面
     */
    private void createFragment(int position) {
        if (mPagerAdapter.getCount() < 1) {
            return;
        }
        PagerTab channel = mPagerAdapter.getItem(position);
        mSelectFragmentCallback.selectFragment(position, channel);
    }

    /**
     * 生成导航条
     *
     * @param channels 导航条
     */
    public void initTabs(List<PagerTab> channels) {
        L.alog().d(TAG,"initTabs: "+channels.size());
        int old = tabId;
        mTabLayout.removeAllTabs();
        mPagerAdapter.clear();
        if (channels.isEmpty()) {
            return;
        }
        channels.forEach(this::addTab);
        tabId = old;
        getCurTabIndex(TabLayout.Tab::select);
    }

    public TabLayout.Tab addTempTab(PagerTab channel) {
        channel.setTemp(true);
        if (mPagerAdapter.getCount() > 0) {
            TabLayout.Tab ftab = findTab(channel.getId());
            if (ftab != null) {
                return ftab;
            }
            int index = Math.max(mPagerAdapter.getCount() - 1, 0);
            PagerTab item = mPagerAdapter.getItem(index);
            if (item.isTemp()) {
                mPagerAdapter.remove(item);
                mPagerAdapter.add(channel);
                TabLayout.Tab tab = mTabLayout.getTabAt(index);
                if (itemLayout != 0) {
                    TextView tv = tab.getCustomView().findViewById(R.id.lcu_tabs_item_title);
                    tv.setText(channel.getName());
                } else {
                    tab.setText(channel.getName());
                    tab.view.setLongClickable(false);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        tab.view.setTooltipText("");
                    }
                }
                tab.setTag(channel);
                return tab;
            }
        }
        return addTab(channel);
    }

    public TabLayout.Tab addTab(PagerTab channel) {
        TabLayout.Tab tab = mTabLayout.newTab();
        if (itemLayout != 0) {
            tab.setCustomView(itemLayout);
            TextView tv = tab.getCustomView().findViewById(R.id.lcu_tabs_item_title);
            tv.setText(channel.getName());
        } else {
            tab.setText(channel.getName());
            tab.view.setLongClickable(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                tab.view.setTooltipText("");
            }
        }
        tab.setTag(channel);
        mPagerAdapter.add(channel);
        mTabLayout.addTab(tab);
        return tab;
    }

    public void select(TabLayout.Tab tab) {
        PagerTab nav = (PagerTab) tab.getTag();
        if (nav != null) {
            tabId = nav.getId();
        }
        getCurTabIndex(TabLayout.Tab::select);
    }

    public TabLayout.Tab findTab(int id) {
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            PagerTab nav = (PagerTab) tab.getTag();
            if (nav == null) {
                continue;
            }
            if (nav.getId() == id) {
                return tab;
            }
        }
        return null;
    }

    /**
     * 获取当前位置
     *
     * @param consumer 当前位置回回调
     */
    private void getCurTabIndex(Consumer<TabLayout.Tab> consumer) {
        mTabLayout.postDelayed(() -> {
            consumer.accept(getCurTab());
            mPagerAdapter.notifyChanged();
        }, 100);
    }

    private TabLayout.Tab getCurTab() {
        TabLayout.Tab tab = findTab(tabId);
        return tab == null ? mTabLayout.getTabAt(0) : tab;
    }

    public void destroy() {
        if (mPagerAdapter != null) {
            try {
                mPagerAdapter.unregisterAdapterDataObserver(mTabPagerObserver);
            } catch (Throwable ignored) {
            }
        }
    }

    public interface FragmentCallback {
        /**
         * 选中页面
         *
         * @param position 页面中的位置
         */
        void selectFragment(int position, PagerTab tab);
    }
}
