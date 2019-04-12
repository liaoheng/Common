package com.github.liaoheng.common.thread;

import java.util.UUID;

/**
 * @author liaoheng
 * @version 2018-08-16 14:44
 */
public abstract class JobThread extends Thread {

    public JobThread(String name) {
        super(name + " : " + UUID.randomUUID());
        setDaemon(true);
    }

    protected volatile boolean isRunning;

    public synchronized boolean isRunning() {
        return isRunning;
    }

    public synchronized void start() {
        if (isRunning()) {
            return;
        }
        super.start();
        isRunning = true;
    }

    public synchronized void shutdown() {
        isRunning = false;
        interrupt();
    }

    @Override
    public void run() {
        try {
            process();
        } catch (InterruptedException ignored) {
        }
    }

    protected abstract void process() throws InterruptedException;
}
