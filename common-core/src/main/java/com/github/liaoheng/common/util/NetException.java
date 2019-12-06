package com.github.liaoheng.common.util;

/**
 * 自定网络服务异常
 *
 * @author liaoheng
 * @version 2015-07-01 15:35
 */
public class NetException extends SystemException {
    public NetException(String errorMessage, Throwable e) {
        super(errorMessage, e);
    }

    public NetException(String errorMessage) {
        super(errorMessage);
    }

    public NetException(Throwable e) {
        super(e);
    }

    public NetException(String errorMessage, Object error) {
        super(errorMessage);
        setErrorBody(error);
    }

    @Override
    public String toString() {
        return mErrorBody == null ? "" : "{" + mErrorBody + "}" + super.toString();
    }
}
