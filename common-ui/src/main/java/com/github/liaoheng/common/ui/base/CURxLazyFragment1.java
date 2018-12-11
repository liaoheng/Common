package com.github.liaoheng.common.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * RxLifecycle1 Base LazyFragment
 *
 * @author liaoheng
 * @version 2016-7-29 14:19
 * @see <a href="https://github.com/trello/RxLifecycle/blob/master/rxlifecycle-components/src/main/java/com/trello/rxlifecycle3/components/support/RxFragment.java">RxFragment</a>
 */
public abstract class CURxLazyFragment1 extends CULazyFragment implements LifecycleProvider<FragmentEvent> {
    private final BehaviorSubject<FragmentEvent> lifecycleSubject = BehaviorSubject.create();

    public CURxLazyFragment1 getFragment() {
        return this;
    }

    @Override
    @NonNull
    @CheckResult
    public final Observable<FragmentEvent> lifecycle() {
        return lifecycleSubject.asObservable();
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull FragmentEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindFragment(lifecycleSubject);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        lifecycleSubject.onNext(FragmentEvent.ATTACH);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleSubject.onNext(FragmentEvent.CREATE);
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        lifecycleSubject.onNext(FragmentEvent.CREATE_VIEW);
    }

    @Override
    protected void onStartLazy() {
        super.onStartLazy();
        lifecycleSubject.onNext(FragmentEvent.START);
    }

    @Override
    protected void onResumeLazy() {
        super.onResumeLazy();
        lifecycleSubject.onNext(FragmentEvent.RESUME);
    }

    @Override
    protected void onPauseLazy() {
        lifecycleSubject.onNext(FragmentEvent.PAUSE);
        super.onPauseLazy();
    }

    @Override
    protected void onStopLazy() {
        lifecycleSubject.onNext(FragmentEvent.STOP);
        super.onStopLazy();
    }

    @Override
    protected void onDestroyViewLazy() {
        lifecycleSubject.onNext(FragmentEvent.DESTROY_VIEW);
        super.onDestroyViewLazy();
    }

    @Override
    public void onDestroy() {
        lifecycleSubject.onNext(FragmentEvent.DESTROY);
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        lifecycleSubject.onNext(FragmentEvent.DETACH);
        super.onDetach();
    }
}
