package com.github.liaoheng.common.util;

import com.github.liaoheng.common.BaseTest;
import com.github.liaoheng.common.TestApplication;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.FileNotFoundException;
import java.io.IOException;
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
        SystemException exception = new SystemException(new ArrayIndexOutOfBoundsException("3"));
        Throwable cause = exception.getCause();
        assertNotNull("is null", cause);
        assertTrue("is not ArrayIndexOutOfBoundsException",
                cause instanceof ArrayIndexOutOfBoundsException);
        assertEquals("msg incorrect", exception.getMessage(), "3");
    }

    @Test
    public void addSystemException1Test() {
        try {
            throw new SystemException("one error", new SystemDataException("two error"));
        } catch (SystemException e) {
            Throwable cause = e.getCause();
            assertNotNull("is null", cause);
            assertEquals("msg incorrect", e.getMessage(), "one error");
            assertEquals("cause msg incorrect", cause.getMessage(), "two error");
        }
    }

    @Test
    public void addSystemException2Test() {
        try {
            throw new SystemException("one error");
        } catch (SystemException e) {
            assertNotNull("is null", e);
            assertEquals("msg incorrect", e.getMessage(), "one error");
        }
    }

    @Test
    public void addSystemException3Test() {
        try {
            throw new SystemException(new SystemDataException("two error"));
        } catch (SystemException e) {
            Throwable cause = e.getCause();
            assertNotNull("is null", cause);
            assertEquals("msg incorrect", e.getMessage(), "two error");
        }
    }

    @Test
    public void addSystemException4Test() {
        try {
            try{
                try {
                    throw new ArrayIndexOutOfBoundsException("3");
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new IllegalStateException("get index error", e);
                }
            }catch (IllegalStateException e){
                throw new SystemException(e);
            }
        } catch (SystemException e) {
            Throwable cause = e.getCause();
            assertNotNull("is null", cause);
            assertTrue("is not IllegalStateException",
                    cause instanceof IllegalStateException);
            assertEquals("msg incorrect", e.getMessage(), "get index error");
        }
    }

    @Test
    public void addSystemException5Test() {
        try {
            try{
                try {
                    throw new ArrayIndexOutOfBoundsException("3");
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new IllegalStateException("get index error", e);
                }
            }catch (IllegalStateException e){
                throw new SystemException("error",e);
            }
        } catch (SystemException e) {
            Throwable cause = e.getCause();
            assertNotNull("is null", cause);
            assertTrue("is not IllegalStateException",
                    cause instanceof IllegalStateException);
            assertEquals("msg incorrect", e.getMessage(), "error");
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
            Throwable cause = e.getCause();
            assertNotNull("is null", cause);
            assertTrue("is not IllegalArgumentException", cause instanceof IllegalArgumentException);
            assertEquals("msg incorrect", e.getMessage(),
                    "java.io.FileNotFoundException : not can file found");
        }
    }

    @Test
    public void addSystemException8Test() {
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
        }
    }
}