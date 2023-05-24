package com.github.liaoheng.common.util;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * base {@link ViewModel}
 *
 * @author liaoheng
 * @date 2021-09-08 09:41
 */
public abstract class BaseViewModel extends ViewModel {
    protected final String TAG = this.getClass().getSimpleName();
    /**
     * load error
     */
    private final MutableLiveData<Throwable> mLoadErrorLiveData = new MutableLiveData<>();
    /**
     * load status
     */
    private final MutableLiveData<Boolean> mLoadStatusLiveData = new MutableLiveData<>();
    /**
     * load {@link Disposable}
     */
    private final CompositeDisposable mDisposables = new CompositeDisposable();

    public MutableLiveData<Throwable> monitorLoadError() {
        return mLoadErrorLiveData;
    }

    public MutableLiveData<Boolean> monitorLoadStatus() {
        return mLoadStatusLiveData;
    }

    public abstract class LoadStatusCallback<T> extends Callback.EmptyCallback<T> {

        @Override
        public abstract void onSuccess(T t);

        @Override
        public void onPreExecute() {
            mLoadStatusLiveData.postValue(true);
        }

        @Override
        public void onPostExecute() {
            mLoadStatusLiveData.postValue(false);
        }

        @Override
        public void onError(Throwable e) {
            mLoadErrorLiveData.postValue(e);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Utils.dispose(mDisposables);
    }

    public <T> Disposable addSubscribe(Observable<T> observable,
            final Callback<T> listener) {
        return Utils.addSubscribe(observable, listener);
    }

    public void addDisposables(Disposable disposable) {
        mDisposables.add(disposable);
    }

    public <T> void addDisposables(Observable<T> observable,
            final Callback<T> listener) {
        addDisposables(addSubscribe(observable, listener));
    }
}
