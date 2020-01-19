package com.github.liaoheng.common.thread;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.github.liaoheng.common.util.Logcat;

import java.util.UUID;

/**
 * @author liaoheng
 * @version 2018-10-25 15:22
 */
public interface IWorkProcessThread {
    void start();

    void stop();

    boolean isRunning();

    @Nullable
    Handler getRunnable();

    interface Handler {
        String name();

        void onStart(String name);

        void onHandler() throws InterruptedException;

        void onStop(String name);
    }

    abstract class BaseHandler implements Handler {
        private String mThreadName;
        private Logcat.ILogcat mLogcat;
        private String mTag;

        public BaseHandler(Logcat.ILogcat logcat, String tag) {
            mLogcat = logcat;
            mTag = tag;
        }

        public BaseHandler(Logcat.ILogcat logcat, String tag, String name) {
            this(logcat, tag);
            mThreadName = name;
        }

        @Override
        public String name() {
            return TextUtils.isEmpty(mThreadName) ? UUID.randomUUID().toString() : mThreadName;
        }

        @Override
        public void onStart(String name) {
            mLogcat.d(mTag, "start > %s", name);
        }

        @Override
        public void onStop(String name) {
            mLogcat.d(mTag, "stop > %s", name);
        }
    }
}
