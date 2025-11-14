package com.github.liaoheng.ui.base;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;

import com.trello.lifecycle4.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle4.LifecycleProvider;
import com.trello.rxlifecycle4.LifecycleTransformer;

import io.reactivex.rxjava3.core.Observable;

/**
 * RxLifecycle3 Base Activity
 *
 * @author liaoheng
 * @see <a href="https://github.com/trello/RxLifecycle/blob/master/rxlifecycle-components/src/main/java/com/trello/rxlifecycle3/components/support/RxAppCompatActivity.java">RxAppCompatActivity</a>
 */
public abstract class CURxBaseActivity extends CUBaseActivity implements LifecycleProvider<Lifecycle.Event> {

    private final LifecycleProvider<Lifecycle.Event> mLifecycleProvider = AndroidLifecycle.createLifecycleProvider(
            this);

    public CURxBaseActivity getActivity() {
        return this;
    }

    @Override
    @NonNull
    @CheckResult
    public final Observable<Lifecycle.Event> lifecycle() {
        return mLifecycleProvider.lifecycle();
    }

    @Override
    @NonNull
    @CheckResult
    public <T> LifecycleTransformer<T> bindUntilEvent(@NonNull Lifecycle.Event event) {
        return mLifecycleProvider.bindUntilEvent(event);
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return mLifecycleProvider.bindToLifecycle();
    }
}
