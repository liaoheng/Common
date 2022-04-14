package com.github.liaoheng.common.util;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * 异步执行
 *
 * @author MaTianyu
 * @author liaoheng
 */
public class AsyncExecutor {
    private static ExecutorService threadPool;

    private AsyncExecutor() {
    }

    private static AsyncExecutor mAsyncExecutor;

    public static AsyncExecutor get() {
        if (mAsyncExecutor == null) {
            mAsyncExecutor = new AsyncExecutor();
        }
        return mAsyncExecutor;
    }

    public void init(ExecutorService threadPool) {
        if (AsyncExecutor.threadPool != null) {
            shutdownNow();
        }
        if (threadPool == null) {
            AsyncExecutor.threadPool = Executors.newCachedThreadPool();
        } else {
            AsyncExecutor.threadPool = threadPool;
        }
    }

    public synchronized void shutdownNow() {
        if (threadPool != null && !threadPool.isShutdown()) {
            threadPool.shutdownNow();
        }
        threadPool = null;
    }

    public <T> FutureTask<T> execute(Callable<T> call) {
        FutureTask<T> task = new FutureTask<T>(call);
        threadPool.execute(task);
        return task;
    }

    /**
     * 将任务投入线程池执行
     */
    public <T> FutureTask<T> execute(final Worker<T> worker) {
        Callable<T> call = new Callable<T>() {
            @Override
            public T call() throws Exception {
                return postResult(worker, worker.doInBackground());
            }
        };
        FutureTask<T> task = new FutureTask<T>(call) {
            @Override
            protected void done() {
                try {
                    get();
                } catch (InterruptedException ignored) {
                } catch (CancellationException e) {
                    postCancel(worker);
                } catch (ExecutionException e) {
                    throw new RuntimeException("An error occured while executing doInBackground()",
                            e.getCause());
                }
            }
        };
        threadPool.execute(task);
        return task;
    }

    /**
     * 将子线程结果传递到UI线程
     */
    private <T> T postResult(final Worker<T> worker, final T result) {
        HandlerUtils.post(new Runnable() {
            @Override
            public void run() {
                worker.onPostExecute(result);
            }
        });
        return result;
    }

    /**
     * 将子线程结果传递到UI线程
     */
    private <T> void postCancel(final Worker<T> worker) {
        HandlerUtils.post(new Runnable() {
            @Override
            public void run() {
                worker.onCanceled();
            }
        });
    }

    public static abstract class Worker<T> {
        protected abstract T doInBackground();

        protected void onPostExecute(T data) {
        }

        protected void onCanceled() {
        }
    }
}
