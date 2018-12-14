package com.github.liaoheng.common.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.liaoheng.common.util.UIUtils;

import java.lang.reflect.Field;

/**
 * @author https://github.com/LuckyJayce/ViewPagerIndicator
 */
@SuppressWarnings("TryWithIdenticalCatches")
public class CUBaseFragment extends Fragment {
    protected final String TAG = getFragment().getClass().getSimpleName();
    protected LayoutInflater inflater;
    private View contentView;
    private ViewGroup container;

    public static Fragment setBundle(Fragment fragment, Bundle args) {
        if (args != null) {
            fragment.setArguments(args);
        }
        return fragment;
    }

    public Fragment getFragment() {
        return this;
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
    public final View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        this.inflater = inflater;
        this.container = container;
        onCreateView(savedInstanceState);
        if (contentView == null) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
        return contentView;
    }

    protected void onCreateView(Bundle savedInstanceState) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        contentView = null;
        container = null;
        inflater = null;
    }

    public void setContentView(int layoutResID) {
        setContentView(inflater.inflate(layoutResID, container, false));
    }

    public void setContentView(View view) {
        contentView = view;
    }

    public View getContentView() {
        return contentView;
    }

    public <T extends View> T findViewById(int id) {
        if (contentView == null) {
            return null;
        }
        return UIUtils.findViewById(contentView, id);
    }

    // http://stackoverflow.com/questions/15207305/getting-the-error-java-lang-illegalstateexception-activity-has-been-destroyed
    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
