package com.github.liaoheng.common.util;

import com.github.liaoheng.common.core.SystemExceptionHelper;

/**
 * 自定异常
 *
 * @author liaoheng
 * @version 2015-07-21 20:06:58
 */
public class SystemException extends Exception implements ISystemException {

    private SystemExceptionHelper mExceptionHelper;

    public SystemException(String errorMessage, Throwable e) {
        super(errorMessage);
        mExceptionHelper = SystemExceptionHelper.with(e);
    }

    public SystemException(String errorMessage) {
        this(errorMessage, errorMessage);
    }

    public SystemException(String errorMessage,String myErrorMessage) {
        this(errorMessage, new SystemDataException(myErrorMessage));
    }

    public SystemException(Throwable e) {
        this(e.getMessage(), e);
    }


    @Override public Throwable getCause() {
        if (mExceptionHelper==null){
            return super.getCause();
        }
        return mExceptionHelper.getCause();
    }

    /**
     * {@link Throwable#getCause()}
     * @return
     */
    public Throwable getOriginalCause() {
        return super.getCause();
    }

    @Override public String toString() {
        if (mExceptionHelper == null || mExceptionHelper.getThrowable() == null) {
            return super.toString();
        }
        return mExceptionHelper.throwableToString(this);
    }

    public Throwable getThrowable() {
        return mExceptionHelper.getThrowable();
    }

    public void setThrowable(Throwable throwable) {
        mExceptionHelper.setThrowable(throwable);
    }
}
