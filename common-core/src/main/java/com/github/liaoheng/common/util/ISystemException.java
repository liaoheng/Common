package com.github.liaoheng.common.util;

/**
 * @author liaoheng
 * @version 2016-07-25 15:08
 */
public interface ISystemException {
    Throwable getCause();
    Throwable getOriginalCause();
    String getOriginalMessage();
}
