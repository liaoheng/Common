package com.github.liaoheng.common.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.github.liaoheng.common.adapter.base.BaseFragmentStatePagerAdapter;
import com.github.liaoheng.common.ui.core.TabPagerHelper;
import com.github.liaoheng.common.ui.model.PagerTab;
import com.github.liaoheng.common.util.L;

import java.util.List;

/**
 * @author liaoheng
 * @version 2015-12-08 11:13
 */
public class TabPagerAdapter extends BaseFragmentStatePagerAdapter<PagerTab> {
    private static final String TAG = TabPagerAdapter.class.getSimpleName();
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
    public Fragment getItemFragment(PagerTab item, int position) {
        if (mTabPagerOperation != null) {
            return mTabPagerOperation.getItem(item, position);
        } else {
            Object object = item.getObject();
            if (object instanceof Fragment){
                return (Fragment) object;
            }else{
                L.Log.w(TAG, "TabPagerOperation is null");
                return new Fragment();
            }
        }
    }
}
