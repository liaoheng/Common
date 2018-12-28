package com.github.liaoheng.common.util;

import com.github.liaoheng.common.BaseTest;
import com.github.liaoheng.common.TestApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertTrue;

/**
 * @author liaoheng
 * @version 2017-06-28 18:30
 */
@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class)
public class StringTest extends BaseTest {

    @Test
    public void StringChineseTest() {
        boolean z = StringUtils.isChinese("中文");
        assertTrue("is not chinese", z);
    }

}
