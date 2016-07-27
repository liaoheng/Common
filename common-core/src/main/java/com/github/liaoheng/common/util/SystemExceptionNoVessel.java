package com.github.liaoheng.common.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author liaoheng
 * @version 2016-07-25 16:28
 */
@Target(ElementType.TYPE )
@Retention(RetentionPolicy.RUNTIME)
public @interface SystemExceptionNoVessel {
}
