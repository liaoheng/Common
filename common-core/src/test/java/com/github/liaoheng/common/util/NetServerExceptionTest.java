package com.github.liaoheng.common.util;

import com.github.liaoheng.common.BaseTest;
import com.github.liaoheng.common.TestApplication;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author liaoheng
 * @version 2016-07-25 15:30
 */
@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class)
public class NetServerExceptionTest extends BaseTest {

    @Test
    public void NetServerExceptionTest1() {
        try {
            throw new NetServerException("404", "404");
        } catch (NetServerException e) {
            SystemException exception = new SystemException(e);
            Throwable cause = exception.getCause();
            assertNotNull("is null", cause);
            assertTrue("is not NetServerException", cause instanceof NetServerException);
            assertEquals("msg incorrect", exception.getMessage(), "404");
        }
    }

    @Test
    public void NetServerExceptionTest2() throws JSONException {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", 11);
            jsonObject.put("msg", "error");
            throw new SystemRuntimeException(new NetServerException(jsonObject));
        } catch (SystemRuntimeException e) {
            Throwable cause = e.getCause();
            assertNotNull("is null", cause);
            assertTrue("is not NetServerException", cause instanceof NetServerException);

            NetServerException ne = (NetServerException) cause;
            JSONObject jsonObject = ne.getErrorBody();
            assertEquals("ErrorBody msg incorrect", jsonObject.getString("msg"), "error");

        }

    }

}