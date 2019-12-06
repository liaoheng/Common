package com.github.liaoheng.common.util;

/**
 * 网络本地异常
 *
 * @author liaoheng
 * @version 2015-07-01 15:35
 */
@SystemExceptionNoVessel
public class NetLocalException extends NetException {

    public NetLocalException(String errorMessage, Throwable e) {
        super(errorMessage, e);
    }

    public NetLocalException(String errorMessage) {
        super(errorMessage);
    }

    public NetLocalException(Throwable e) {
        super(e);
    }

    public NetLocalException(Throwable e,Object code) {
        super(e);
        mErrorBody = code;
    }

    public NetLocalException(String errorMessage, Object code) {
        super(errorMessage);
        mErrorBody = code;
    }
}
