package com.leng.common.plus.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.leng.common.plus.model.PagerTab;
import com.leng.common.plus.core.TabPagerHelper;
import com.leng.common.util.L;

/**
 * @author liaoheng
 * @version 2015-12-08 11:13
 */
public class TabPagerAdapter extends BaseFragmentStatePagerAdapter<PagerTab> {
    private static final String              TAG = TabPagerAdapter.class.getSimpleName();
    private TabPagerHelper.TabPagerOperation mTabPagerOperation;

    public TabPagerAdapter(FragmentManager fm, Context context, List<PagerTab> list,
                           TabPagerHelper.TabPagerOperation mTabPagerOperation) {
        super(fm, context, list);
        this.mTabPagerOperation = mTabPagerOperation;
    }

    private int mChildCount;

    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        if (mChildCount > 0) {
            mChildCount--;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return getList().get(position).getName();
    }

    @Override
    public Fragment getItem(int position) {
        if (mTabPagerOperation != null) {
            return mTabPagerOperation.getItem(getList().get(position), position);
        } else {
            L.Log.w(TAG, "TabPagerOperation is null");
            return new Fragment();
        }
    }
}
