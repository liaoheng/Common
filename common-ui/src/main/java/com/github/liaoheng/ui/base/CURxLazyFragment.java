package com.github.liaoheng.ui.base;

import android.os.Bundle;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;

import com.trello.lifecycle4.android.lifecycle.RxLifecycleAndroidLifecycle;
import com.trello.rxlifecycle4.LifecycleProvider;
import com.trello.rxlifecycle4.LifecycleTransformer;
import com.trello.rxlifecycle4.RxLifecycle;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

/**
 * RxLifecycle3 Base LazyFragment
 *
 * @author liaoheng
 * @version 2016-7-29 14:19
 * @see <a href="https://github.com/trello/RxLifecycle/blob/master/rxlifecycle-components/src/main/java/com/trello/rxlifecycle3/components/support/RxFragment.java">RxFragment</a>
 */
public abstract class CURxLazyFragment extends CULazyFragment implements LifecycleProvider<Lifecycle.Event> {
    private final BehaviorSubject<Lifecycle.Event> lifecycleSubject = BehaviorSubject.create();

    public CURxLazyFragment getFragment() {
        return this;
    }

    @NonNull
    @Override
    @CheckResult
    public Observable<Lifecycle.Event> lifecycle() {
        return lifecycleSubject.hide();
    }

    @NonNull
    @Override
    @CheckResult
    public <T> LifecycleTransformer<T> bindUntilEvent(@NonNull Lifecycle.Event event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @NonNull
    @Override
    @CheckResult
    public <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroidLifecycle.bindLifecycle(lifecycleSubject);
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        lifecycleSubject.onNext(Lifecycle.Event.ON_CREATE);
    }

    @Override
    protected void onStartLazy() {
        super.onStartLazy();
        lifecycleSubject.onNext(Lifecycle.Event.ON_START);
    }

    @Override
    protected void onResumeLazy() {
        super.onResumeLazy();
        lifecycleSubject.onNext(Lifecycle.Event.ON_RESUME);
    }

    @Override
    protected void onPauseLazy() {
        lifecycleSubject.onNext(Lifecycle.Event.ON_PAUSE);
        super.onPauseLazy();
    }

    @Override
    protected void onStopLazy() {
        lifecycleSubject.onNext(Lifecycle.Event.ON_STOP);
        super.onStopLazy();
    }

    @Override
    protected void onDestroyViewLazy() {
        lifecycleSubject.onNext(Lifecycle.Event.ON_DESTROY);
        super.onDestroyViewLazy();
    }
}
