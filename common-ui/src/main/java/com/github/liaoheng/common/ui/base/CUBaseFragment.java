package com.github.liaoheng.common.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.github.liaoheng.common.util.UIUtils;

/**
 * 基础通用Fragment
 *
 * @author LuckyJayce
 * @author liaoheng
 * @see <a href="https://github.com/LuckyJayce/ViewPagerIndicator">LuckyJayce</a>
 */
@SuppressWarnings("TryWithIdenticalCatches")
public abstract class CUBaseFragment extends Fragment {
    protected final String TAG = getFragment().getClass().getSimpleName();
    private LayoutInflater mInflater;
    private View mContentView;
    private ViewGroup mContainer;

    public static Fragment setBundle(Fragment fragment, Bundle args) {
        if (args != null) {
            fragment.setArguments(args);
        }
        return fragment;
    }

    public Fragment getFragment() {
        return this;
    }

    public LayoutInflater getInflater() {
        return mInflater;
    }

    public Context getApplicationContext() {
        return getActivity().getApplicationContext();
    }

    public void setActivityTitle(String title) {
        FragmentActivity activity = getActivity();
        if (activity instanceof CUBaseActivity) {
            ((CUBaseActivity) activity).setMTitle(title);
        }
    }

    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mInflater = inflater;
        mContainer = container;
        onCreateView(savedInstanceState);
        if (mContentView == null) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
        return mContentView;
    }

    protected abstract void onCreateView(Bundle savedInstanceState);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mContentView = null;
        mContainer = null;
        mInflater = null;
    }

    public void setContentView(int layoutResID) {
        setContentView(mInflater.inflate(layoutResID, mContainer, false));
    }

    public void setContentView(View view) {
        mContentView = view;
    }

    public View getContentView() {
        return mContentView;
    }

    public <T extends View> T findViewById(int id) {
        if (mContentView == null) {
            return null;
        }
        return UIUtils.findViewById(mContentView, id);
    }
}
