package com.github.liaoheng.common.util;

import com.github.liaoheng.common.BaseTest;
import com.github.liaoheng.common.BuildConfig;
import java.io.FileNotFoundException;
import java.util.concurrent.TimeoutException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author liaoheng
 * @version 2016-07-25 11:35
 */
@RunWith(RobolectricGradleTestRunner.class) @Config(constants = BuildConfig.class, sdk = 21) public class SystemExceptionTest extends BaseTest {

    @Test public void addExceptionTest() {
        String[] s = new String[] { "123", "456" };
        try {
            String s1 = s[3];
        } catch (ArrayIndexOutOfBoundsException e) {
            SystemException exception = new SystemException(e);
            Throwable cause = exception.getCause();
            assertNotNull("is null", cause);
            assertTrue("is not ArrayIndexOutOfBoundsException",
                    cause instanceof ArrayIndexOutOfBoundsException);
            assertEquals("msg is error", exception.getMessage(), "3");
        }
    }

    @Test public void addSystemException1Test() {
        try {
            throw new SystemException("one error", new SystemException("two error"));
        } catch (SystemException e) {
            Throwable cause = e.getCause();
            assertNotNull("is null", cause);
            assertEquals("msg is error", e.getMessage(), "one error");
        }
    }

    @Test public void addSystemException2Test() {
        try {
            throw new SystemException("one error");
        } catch (SystemException e) {
            Throwable cause = e.getCause();
            assertNotNull("is null", cause);
            assertEquals("msg is error", e.getMessage(), "one error");
        }
    }

    @Test public void addSystemException3Test() {
        try {
            throw new SystemException(new SystemException("two error"));
        } catch (SystemException e) {
            Throwable cause = e.getCause();
            assertNotNull("is null", cause);
            assertEquals("msg is error", e.getMessage(), "two error");
        }
    }

    @Test public void addSystemException4Test() {
        String[] s = new String[] { "123", "456" };
        try {
            try {
                String s1 = s[3];
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new SystemException(SystemException.DATA_ERROR, e);
            }
        } catch (SystemException e) {
            Throwable cause = e.getCause();
            assertNotNull("is null", cause);
            assertTrue("is not ArrayIndexOutOfBoundsException",
                    cause instanceof ArrayIndexOutOfBoundsException);
            assertEquals("msg is error", e.getMessage(), SystemException.DATA_ERROR);
        }
    }

    @Test public void addSystemException5Test() {
        try {
            throw new SystemRuntimeException(new SystemException(new TimeoutException("timeout")));
        } catch (SystemRuntimeException ex) {
            SystemException e = new SystemException(ex);
            Throwable cause = e.getCause();
            assertNotNull("is null", cause);
            ShadowLog.d(TAG, "", cause);
            assertTrue("is not TimeoutException", cause instanceof TimeoutException);
            assertEquals("msg is error", e.getMessage(), "timeout");
        }
    }

    @Test public void addSystemException6Test() {
        try {
            throw new IllegalArgumentException(new SystemException("arg is null"));
        } catch (IllegalArgumentException ex) {
            SystemException e = new SystemException(ex);
            Throwable cause = e.getCause();
            assertNotNull("is null", cause);
            ShadowLog.d(TAG, "", cause);
            assertTrue("is not SystemException", cause instanceof IllegalArgumentException);
            assertEquals("msg is error", e.getMessage(),
                    "com.github.liaoheng.common.util.SystemException: arg is null");
        }
    }

    @Test public void addSystemException7Test() {
        try {
            throw new IllegalArgumentException(
                    new SystemException(new FileNotFoundException("not can file found")));
        } catch (IllegalArgumentException ex) {
            SystemException e = new SystemException(ex);
            Throwable cause = e.getCause();
            assertNotNull("is null", cause);
            ShadowLog.d(TAG, "", cause);
            assertTrue("is not SystemException", cause instanceof IllegalArgumentException);
            assertEquals("msg is error", e.getMessage(),
                    "com.github.liaoheng.common.util.SystemException: not can file found");
        }
    }

    @Test public void addSystemException8Test() {
        try {
            throw new IllegalArgumentException(
                    new SystemException(new FileNotFoundException("not can file found")));
        } catch (IllegalArgumentException ex) {
            SystemException e = new SystemException(ex);
            Throwable cause = e.getCause();
            assertNotNull("is null", cause);
            ShadowLog.d(TAG, "", cause);
            assertTrue("is not SystemException", cause instanceof IllegalArgumentException);
            assertEquals("msg is error", e.getMessage(),
                    "com.github.liaoheng.common.util.SystemException: not can file found");
        }
    }
}