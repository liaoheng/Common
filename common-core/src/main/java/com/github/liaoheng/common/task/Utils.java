package com.github.liaoheng.common.task;

import com.github.liaoheng.common.util.MD5Utils;

import java.util.Arrays;

/**
 * @author liaoheng
 * @version 2020-07-03 15:00
 */
class Utils {
    public static int getKey(String key) {
        return Arrays.hashCode(MD5Utils.md5(key));
        //return Arrays.hashCode(key.getBytes());
    }
}
