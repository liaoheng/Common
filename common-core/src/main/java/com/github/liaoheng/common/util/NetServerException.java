package com.github.liaoheng.common.util;

import androidx.annotation.NonNull;

/**
 * 网络服务器异常
 *
 * @author liaoheng
 * @version 2015-07-01 15:35
 */
@SystemExceptionNoVessel
public class NetServerException extends NetException {
    private Object mErrorBody;

    public NetServerException(Throwable e) {
        super(e);
    }

    public NetServerException(String errorMessage, Object errorBody) {
        super(errorMessage);
        mErrorBody = errorBody;
    }

    public NetServerException(Object errorBody) {
        super("ServerError", null);
        mErrorBody = errorBody;
    }

    @NonNull
    @Override
    public String toString() {
        return "msg: " + getMessage() + " { " + mErrorBody + " } ";
    }
}
