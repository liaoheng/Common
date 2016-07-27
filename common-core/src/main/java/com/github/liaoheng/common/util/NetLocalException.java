package com.github.liaoheng.common.util;

/**
 * 网络本地方异常
 * @author liaoheng
 * @version 2015-07-01 15:35
 */
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
}
