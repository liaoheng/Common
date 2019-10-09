package com.github.liaoheng.common.util;

import com.github.liaoheng.common.BaseTest;
import com.github.liaoheng.common.TestApplication;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

/**
 * @author liaoheng
 * @version 2019-10-09 13:58
 */
@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class)
public class FileUtilsTest extends BaseTest {

    @Test
    public void getExtension() {
        String fullName = "/data/data/com.github.liaoheng.common.sample/shared_prefs/WebViewChromiumPrefs.xml";
        String fileExtension = FileUtils.getExtension(fullName);
        //log(fileExtension);
        assertEquals("no xml", fileExtension, "xml");
    }

    @Test
    public void getName() {
        String fullName = "/data/data/com.github.liaoheng.common.sample/shared_prefs/WebViewChromiumPrefs.xml";

        String name = FileUtils.getName(fullName);
        //log(name);
        assertEquals("name error", name, "WebViewChromiumPrefs");
    }
}
