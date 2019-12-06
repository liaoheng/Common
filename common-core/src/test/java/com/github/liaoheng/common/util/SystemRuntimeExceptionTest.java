package com.github.liaoheng.common.util;

import com.github.liaoheng.common.BaseTest;
import com.github.liaoheng.common.TestApplication;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author liaoheng
 * @version 2016-10-25 10:09
 */
@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class)
public class SystemRuntimeExceptionTest extends BaseTest {

    @Test
    public void SystemRuntimeException1Test() {
        try {
            throw new SystemRuntimeException(new SystemException("timeout"));
        } catch (SystemRuntimeException e) {
            Throwable cause = e.getCause();
            assertNull("cause not null", cause);
            assertEquals("msg incorrect", e.getMessage(), "timeout");
        }
    }

    @Test
    public void SystemRuntimeException2Test() {
        try {
            throw new SystemRuntimeException(new SystemException("timeout", new IOException("io")));
        } catch (SystemRuntimeException e) {
            Throwable cause = e.getCause();
            assertNotNull("cause is null", cause);
            assertTrue("is not IOException", cause instanceof IOException);
            assertEquals("msg incorrect", e.getMessage(), "timeout");
        }
    }
}
