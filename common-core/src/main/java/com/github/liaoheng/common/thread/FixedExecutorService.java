package com.github.liaoheng.common.thread;

import com.github.liaoheng.common.util.Utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author liaoheng
 * @version 2018-07-26 09:46
 */
public class FixedExecutorService {
    private ExecutorService mFixedExecutorService;

    public ExecutorService getExecutorService() {
        if (Utils.checkExecutorService(mFixedExecutorService)) {
            mFixedExecutorService = Executors.newFixedThreadPool(
                    Runtime.getRuntime().availableProcessors());
        }
        return mFixedExecutorService;
    }

    public Future<?> submit(Runnable task) {
        return getExecutorService().submit(task);
    }

    public void shutdown() {
        getExecutorService().shutdown();
    }

}
