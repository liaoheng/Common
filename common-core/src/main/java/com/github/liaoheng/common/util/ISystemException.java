package com.github.liaoheng.common.util;

/**
 * @author liaoheng
 * @version 2016-07-25 15:08
 */
public interface ISystemException {
    int UNKNOWN_ERROR = 1;
    int INTERNAL_ERROR = 2;

    String INTERNAL_ERROR_STRING = "Internal Error";
    String UNKNOWN_ERROR_STRING = "Unknown Error";

    Throwable getCause();
    Throwable getOriginalCause();
    String getOriginalMessage();
}
