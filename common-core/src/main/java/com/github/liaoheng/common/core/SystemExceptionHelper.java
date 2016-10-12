package com.github.liaoheng.common.core;

import com.github.liaoheng.common.util.ISystemException;
import com.github.liaoheng.common.util.SystemExceptionNoVessel;

/**
 * @author liaoheng
 * @version 2016-07-26 15:00
 */
public class SystemExceptionHelper {

    private Throwable mThrowable;

    private SystemExceptionHelper(Throwable throwable) {
        mThrowable = throwable;
    }

    public Throwable getThrowable() {
        return mThrowable;
    }

    public void setThrowable(Throwable throwable) {
        this.mThrowable = throwable;
    }

    public static SystemExceptionHelper with(Throwable throwable) {
        return new SystemExceptionHelper(throwable);
    }

    public Throwable getCause() {
        if (mThrowable != null && mThrowable instanceof ISystemException) {
            SystemExceptionNoVessel annotation = mThrowable.getClass()
                    .getAnnotation(SystemExceptionNoVessel.class);
            if (annotation != null) {
                return mThrowable;
            }
            return mThrowable.getCause();
        }
        return mThrowable;
    }

    public Throwable getCause(Throwable me) {
        if (mThrowable != null && mThrowable instanceof ISystemException) {
            SystemExceptionNoVessel annotation = mThrowable.getClass()
                    .getAnnotation(SystemExceptionNoVessel.class);
            if (annotation != null) {
                return mThrowable;
            }
            Throwable cause1 = mThrowable.getCause();
            return cause1 == null ? me : cause1;
        }
        return mThrowable == null ? me : mThrowable;
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored") public String throwableToString(
            Throwable me) {
        Throwable cause = getCause();
        if (cause == null) {
            cause = me;
        }
        String msg = cause.toString();
        String name = cause.getClass().getName();
        if (msg == null) {
            return name;
        }
        return name + ": " + msg;
    }

}
