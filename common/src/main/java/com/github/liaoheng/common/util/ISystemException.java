package com.github.liaoheng.common.util;

/**
 * @author liaoheng
 * @version 2016-07-25 15:08
 */
public interface ISystemException {
    String DEFALUT               = "内部错误！";
    String DATA_ERROR            = "参数错误！";
    String PICTURE_ACQUIRE_ERROR = "无法获取图片！";

    Throwable getCause();
}
