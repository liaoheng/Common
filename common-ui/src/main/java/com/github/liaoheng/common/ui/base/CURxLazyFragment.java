package com.github.liaoheng.common.ui.base;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;

import com.trello.lifecycle4.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle4.LifecycleProvider;
import com.trello.rxlifecycle4.LifecycleTransformer;

import io.reactivex.rxjava3.core.Observable;

/**
 * RxLifecycle Base LazyFragment
 *
 * @author liaoheng
 * @version 2016-7-29 14:19
 */
public abstract class CURxLazyFragment extends CULazyFragment implements LifecycleProvider<Lifecycle.Event> {
    private final LifecycleProvider<Lifecycle.Event> mLifecycleProvider = AndroidLifecycle.createLifecycleProvider(
            this);

    @Override
    @NonNull
    @CheckResult
    public final Observable<Lifecycle.Event> lifecycle() {
        return mLifecycleProvider.lifecycle();
    }

    @Override
    public <T> LifecycleTransformer<T> bindUntilEvent(Lifecycle.Event event) {
        return mLifecycleProvider.bindUntilEvent(event);
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return mLifecycleProvider.bindToLifecycle();
    }
}
