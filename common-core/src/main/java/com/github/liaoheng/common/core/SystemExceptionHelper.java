package com.github.liaoheng.common.core;

import com.github.liaoheng.common.util.ISystemException;
import com.github.liaoheng.common.util.SystemExceptionNoVessel;

import androidx.annotation.Nullable;

/**
 * 对自定义异常类处理，两种类型的异常：1.载体类型，处理时过滤这个异常；2.内容类型，不过滤。通过注解{@link SystemExceptionNoVessel}判断。
 * <br/> 本来由Throwable持有的cause现在由当前类持有。
 * <br/> 实现当多个自定义异常层层包裹时，最后通过getCause得到的是最底层的异常。 单元测试SystemExceptionTest#addSystemException5Test做类似测试。
 *
 * @author liaoheng
 * @version 2016-07-26 15:00
 */
public class SystemExceptionHelper {

    private Throwable mThrowable;//被持有的原始异常

    private SystemExceptionHelper(Throwable throwable) {
        mThrowable = throwable;
    }

    public Throwable getThrowable() {
        return mThrowable;
    }

    public void setThrowable(Throwable throwable) {
        this.mThrowable = throwable;
    }

    /**
     * @param throwable 原始异常
     */
    public static SystemExceptionHelper with(Throwable throwable) {
        return new SystemExceptionHelper(throwable);
    }

    /**
     * 过滤载体类型异常
     */
    @Nullable
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

    /**
     * 输出异常栈信息，过滤载体类型异常，当前cause没有时，使用当前异常本身。
     *
     * @param me 当前异常
     */
    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    public String throwableToString(
            Throwable me) {
        Throwable cause = getCause();
        if (cause == null) {
            cause = me;
        }
        String msg;
        if (cause instanceof ISystemException) {
            ISystemException exception = (ISystemException) cause;
            msg = exception.getOriginalMessage();
        } else {
            msg = cause.getMessage();
        }
        String name = cause.getClass().getName();
        if (msg == null) {
            return name;
        }
        return name + " : " + msg;
    }

}
