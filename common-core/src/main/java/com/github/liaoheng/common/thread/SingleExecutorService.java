package com.github.liaoheng.common.thread;

import com.github.liaoheng.common.util.Utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author liaoheng
 * @version 2018-07-26 09:46
 */
public class SingleExecutorService {
    private ExecutorService mSingleExecutorService;

    public ExecutorService getExecutorService() {
        if (Utils.checkExecutorService(mSingleExecutorService)) {
            mSingleExecutorService = Executors.newSingleThreadExecutor();
        }
        return mSingleExecutorService;
    }

    public Future<?> submit(Runnable task) {
        return getExecutorService().submit(task);
    }

    public void shutdown() {
        getExecutorService().shutdown();
    }

}
