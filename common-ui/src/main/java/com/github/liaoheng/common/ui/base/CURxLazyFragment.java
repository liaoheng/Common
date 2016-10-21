package com.github.liaoheng.common.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.trello.rxlifecycle.FragmentEvent;
import com.trello.rxlifecycle.FragmentLifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * @author liaoheng
 * @version 2016-7-29 14:19
 */
public class CURxLazyFragment extends CULazyFragment implements FragmentLifecycleProvider {
    private final BehaviorSubject<FragmentEvent> lifecycleSubject = BehaviorSubject.create();

    @Override @NonNull @CheckResult public final Observable<FragmentEvent> lifecycle() {
        return lifecycleSubject.asObservable();
    }

    @Override @NonNull @CheckResult public final <T> LifecycleTransformer<T> bindUntilEvent(
            @NonNull FragmentEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override @NonNull @CheckResult public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycle.bindFragment(lifecycleSubject);
    }

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        lifecycleSubject.onNext(FragmentEvent.ATTACH);
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleSubject.onNext(FragmentEvent.CREATE);
    }

    @Override protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        lifecycleSubject.onNext(FragmentEvent.CREATE_VIEW);
    }

    @Override protected void onFragmentStartLazy() {
        super.onFragmentStartLazy();
        lifecycleSubject.onNext(FragmentEvent.START);
    }

    @Override protected void onResumeLazy() {
        super.onResumeLazy();
        lifecycleSubject.onNext(FragmentEvent.RESUME);
    }

    @Override protected void onPauseLazy() {
        lifecycleSubject.onNext(FragmentEvent.PAUSE);
        super.onPauseLazy();
    }

    @Override protected void onFragmentStopLazy() {
        lifecycleSubject.onNext(FragmentEvent.STOP);
        super.onFragmentStopLazy();
    }

    @Override protected void onDestroyViewLazy() {
        lifecycleSubject.onNext(FragmentEvent.DESTROY_VIEW);
        super.onDestroyViewLazy();
    }

    @Override public void onDestroy() {
        lifecycleSubject.onNext(FragmentEvent.DESTROY);
        super.onDestroy();
    }

    @Override public void onDetach() {
        lifecycleSubject.onNext(FragmentEvent.DETACH);
        super.onDetach();
    }
}
