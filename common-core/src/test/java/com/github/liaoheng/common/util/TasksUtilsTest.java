package com.github.liaoheng.common.util;

import com.github.liaoheng.common.BaseTest;
import com.github.liaoheng.common.BuildConfig;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertTrue;

/**
 * @author liaoheng
 * @version 2018-01-21 16:58
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class TasksUtilsTest extends BaseTest {
    @Test
    public void isToDaysDoTest() {
        DateTime now = DateTime.now();
        //log("now : %s", now.toString());
        DateTime next = DateTime.now().minusHours(now.getHourOfDay() + 1);
        //log("next : %s", next.toString());
        boolean toDaysDo = TasksUtils.isToDaysDo(next, now, 1);
        //log("toDaysDo : %s", toDaysDo);
        assertTrue(toDaysDo);
    }
}
