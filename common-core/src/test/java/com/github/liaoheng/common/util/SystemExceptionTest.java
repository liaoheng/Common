package com.github.liaoheng.common.util;

import com.github.liaoheng.common.BaseTest;
import com.github.liaoheng.common.TestApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.FileNotFoundException;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;

/**
 * @author liaoheng
 * @version 2016-07-25 11:35
 */
@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class)
public class SystemExceptionTest extends BaseTest {

    @Test
    public void addExceptionTest() {
        String[] s = new String[] { "123", "456" };
        try {
            String s1 = s[3];
        } catch (ArrayIndexOutOfBoundsException e) {
            SystemException exception = new SystemException(e);
            Throwable cause = exception.getCause();
            assertNotNull("is null", cause);
            //ShadowLog.d(TAG, "", cause);
            assertTrue("is not ArrayIndexOutOfBoundsException",
                    cause instanceof ArrayIndexOutOfBoundsException);
            assertEquals("msg is error", exception.getMessage(), "3");
        }
    }

    @Test
    public void addSystemException1Test() {
        try {
            throw new SystemException("one error", new SystemDataException("two error"));
        } catch (SystemException e) {
            Throwable cause = e.getCause();
            assertNotNull("is null", cause);
            //ShadowLog.d(TAG, "", cause);
            assertEquals("title is error", e.getMessage(), "one error");
            assertEquals("cause msg is error", cause.getMessage(), "two error");
        }
    }

    @Test
    public void addSystemException2Test() {
        try {
            throw new SystemException("one error");
        } catch (SystemException e) {
            assertNotNull("is null", e);
            //loge(e);
            assertEquals("msg is error", e.getMessage(), "one error");
        }
    }

    @Test
    public void addSystemException3Test() {
        try {
            throw new SystemException(new SystemDataException("two error"));
        } catch (SystemException e) {
            loge(e);
            Throwable cause = e.getCause();
            assertNotNull("is null", cause);
            assertEquals("msg is error", e.getMessage(), "two error");
        }
    }

    @Test
    public void addSystemException4Test() {
        String[] s = new String[] { "123", "456" };
        try {
            try {
                String s1 = s[3];
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new SystemException("error", e);
            }
        } catch (SystemException e) {
            Throwable cause = e.getCause();
            assertNotNull("is null", cause);
            //ShadowLog.d(TAG, "", cause);
            assertTrue("is not ArrayIndexOutOfBoundsException",
                    cause instanceof ArrayIndexOutOfBoundsException);
            assertEquals("msg is error", e.getMessage(), "error");
        }
    }

    @Test
    public void addSystemException5Test() {
        try {
            throw new SystemRuntimeException(new SystemException(new TimeoutException("timeout")));
        } catch (SystemRuntimeException ex) {
            SystemException e = new SystemException(ex);
            Throwable cause = e.getCause();
            assertNotNull("is null", cause);
            //ShadowLog.d(TAG, "", cause);
            assertTrue("is not TimeoutException", cause instanceof TimeoutException);
            assertEquals("msg is error", e.getMessage(), "timeout");
        }
    }

    @Test
    public void addSystemException6Test() {
        try {
            throw new IllegalArgumentException(new SystemException("arg is null"));
        } catch (IllegalArgumentException ex) {
            SystemException e = new SystemException(ex);
            Throwable cause = e.getCause();
            assertNotNull("is null", cause);
            //ShadowLog.d(TAG, "", cause);
            assertTrue("is not IllegalArgumentException", cause instanceof IllegalArgumentException);
        }
    }

    @Test
    public void addSystemException7Test() {
        try {
            throw new IllegalArgumentException(
                    new SystemException(new FileNotFoundException("not can file found")));
        } catch (IllegalArgumentException ex) {
            SystemException e = new SystemException(ex);
            //loge(e);
            Throwable cause = e.getCause();
            assertNotNull("is null", cause);
            //loge(cause);
            assertTrue("is not IllegalArgumentException", cause instanceof IllegalArgumentException);
            assertEquals("msg is error", e.getMessage(),
                    "java.io.FileNotFoundException : not can file found");
        }
    }

    @Test
    public void addSystemException8Test() {
        String[] s = new String[] { "123", "456" };
        try {
            try {
                String s1 = s[3];
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new SystemException(SystemException.INTERNAL_ERROR, SystemException.UNKNOWN_ERROR_STRING, e);
            }
        } catch (SystemException e) {
            Throwable cause = e.getCause();
            assertNotNull("is null", cause);
            //ShadowLog.d(TAG, "", cause);
            assertTrue("is not ArrayIndexOutOfBoundsException",
                    cause instanceof ArrayIndexOutOfBoundsException);
            assertEquals("msg is error", e.getMessage(), SystemException.UNKNOWN_ERROR_STRING);
            assertEquals("type is error", e.getType(), SystemException.INTERNAL_ERROR);
            assertEquals("type msg is error", e.getTypeMessage(), SystemException.INTERNAL_ERROR_STRING);
        }
    }

    @Test
    public void addSystemException9Test() {
        try {
            try {
                throw new FileNotFoundException("not can file found");
            } catch (FileNotFoundException e) {
                throw new SystemException("error", e);
            }
        } catch (SystemException e) {
            Throwable cause = e.getCause();
            assertNotNull("is null", cause);
            assertTrue("is not FileNotFoundException",
                    cause instanceof FileNotFoundException);
            assertEquals("type is error", e.getType(), 0);
            assertEquals("type msg is error", e.getTypeMessage(), "");
        }
    }

    @Test
    public void addSystemException10Test() {
        try {
            throw new SystemException("error", "data error");
        } catch (SystemException e) {
            Throwable cause = e.getCause();
            assertNotNull("is null", cause);
            assertTrue("is not SystemDataException",
                    cause instanceof SystemDataException);
            assertEquals("msg is error", e.getMessage(), "error");
            assertEquals("cause msg is error", cause.getMessage(), "data error");
            assertEquals("type is error", e.getType(), 0);
            assertEquals("type msg is error", e.getTypeMessage(), "");
        }
    }

    @Test
    public void addSystemException11Test() {
        try {
            throw new SystemException(SystemException.INTERNAL_ERROR, "my error");
        } catch (SystemException e) {
            Throwable cause = e.getCause();
            assertNull("is null", cause);
            assertEquals("msg is error", e.getMessage(), "my error");
            assertEquals("type is error", e.getType(), SystemException.INTERNAL_ERROR);
            assertEquals("type msg is error", e.getTypeMessage(), SystemException.INTERNAL_ERROR_STRING);
        }
    }
}