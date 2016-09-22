package com.github.liaoheng.common.ui.core;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import com.github.liaoheng.common.ui.R;
import com.github.liaoheng.common.ui.adapter.TabPagerAdapter;
import com.github.liaoheng.common.ui.model.PagerTab;
import com.github.liaoheng.common.util.UIUtils;
import com.github.liaoheng.common.util.ValidateUtils;
import com.h6ah4i.android.tablayouthelper.TabLayoutHelper;
import java.util.List;

/**
 *may use {@link R.layout#lcu_layout_tab_pager}
 * @author liaoheng
 * @version 2015年10月16日
 */
public class TabPagerHelper {

    private ViewPager       mViewPager;
    private TabLayout       mTabLayout;
    private TabLayoutHelper mTabLayoutHelper;
    private TabPagerAdapter mTabPagerAdapter;

    public interface TabPagerOperation {
        Fragment getItem(PagerTab tab, int position);
    }

    public static TabPagerHelper with(@NonNull Activity activity) {
        return with(activity.getWindow().getDecorView());
    }

    public static TabPagerHelper with(@NonNull View view) {
        ViewPager viewPager = UIUtils.findViewById(view, R.id.lcp_tab_pager_view_pager);
        TabLayout tabLayout = UIUtils.findViewById(view, R.id.lcp_tab_pager_view_tab);
        return new TabPagerHelper(viewPager, tabLayout);
    }

    public TabPagerHelper(ViewPager viewPager, TabLayout tabLayout) {
        ValidateUtils.checkArgument(viewPager == null, "ViewPager is null");
        ValidateUtils.checkArgument(tabLayout == null, "TabLayout is null");
        this.mViewPager = viewPager;
        this.mTabLayout = tabLayout;
    }

    public void setViewPagerAdapter(PagerAdapter adapter) {
        mViewPager.setAdapter(adapter);
    }

    public void setAdapter(PagerAdapter adapter) {
        mViewPager.setAdapter(adapter);
        if (adapter.getCount() <= 1) {
            UIUtils.viewGone(mTabLayout);
            return;
        } else {
            UIUtils.viewVisible(mTabLayout);
        }
        mTabLayoutHelper = new TabLayoutHelper(mTabLayout, mViewPager);
        mTabLayoutHelper.setAutoAdjustTabModeEnabled(true);
    }

    public void setAdapter(FragmentManager fm, Context context, List<PagerTab> list,
                           TabPagerOperation operation) {
        mTabPagerAdapter = new TabPagerAdapter(fm, context, list, operation);
        setAdapter(mTabPagerAdapter);
    }

    public void setViewPagerAdapter(FragmentManager fm, Context context, List<PagerTab> list,
                                    TabPagerOperation operation) {
        mTabPagerAdapter = new TabPagerAdapter(fm, context, list, operation);
        setViewPagerAdapter(mTabPagerAdapter);
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

    public TabLayout getTabLayout() {
        return mTabLayout;
    }

    public TabLayoutHelper getTabLayoutHelper() {
        return mTabLayoutHelper;
    }

    public TabPagerAdapter getTabPagerAdapter() {
        return mTabPagerAdapter;
    }
}
