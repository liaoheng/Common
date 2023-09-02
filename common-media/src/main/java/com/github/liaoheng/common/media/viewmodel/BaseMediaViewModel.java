package com.github.liaoheng.common.media.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public abstract class BaseMediaViewModel extends AndroidViewModel {
    protected String TAG = this.getClass().getSimpleName();
    private final MutableLiveData<Boolean> mConnectStatusLiveData = new MutableLiveData<>();

    public BaseMediaViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Boolean> monitorConnectStatus() {
        return mConnectStatusLiveData;
    }

    public void init(Context context) {
        TAG = this.getClass().getSimpleName() + "<" + context.getClass().getSimpleName() + ">";

    }

    public void connect() {

    }

    protected void onServiceConnect(boolean connect) {}

    public void release() {

    }

    @Override
    protected void onCleared() {
        release();
        super.onCleared();
    }

    public boolean isDisconnect() {
        return false;
    }
}
