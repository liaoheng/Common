package com.github.liaoheng.common.util;

import org.joda.time.DateTime;
import org.joda.time.Days;

/**
 * @author liaoheng
 * @version 2018-01-24 13:57
 */
public class TasksUtils {
    private static PreferencesUtils mTaskPreferencesUtils;

    protected static PreferencesUtils getTaskPreferencesUtils() {
        if (mTaskPreferencesUtils == null) {
            mTaskPreferencesUtils = PreferencesUtils.from(TASK_FILE_NAME);
        }
        return mTaskPreferencesUtils;
    }

    private final static String TASK_FILE_NAME = "com.github.liaoheng.common_tasks";
    private final static String TASK_ONE = "task_one";

    public static boolean isOne() {
        return getTaskPreferencesUtils().getBoolean(TASK_ONE, true);
    }

    public static void markOne() {
        getTaskPreferencesUtils().putBoolean(TASK_ONE, false).apply();
    }

    public static int taskCount(int count, String tag) {
        if (count < 1) {
            throw new SystemRuntimeException(new SystemDataException("count < 1 "));
        }
        int c = getTaskPreferencesUtils().getInt(tag, -1);
        if (c != -1) {
            if (c == 0) {
                markTaskDone(tag);
                return c;
            }
            c--;
            getTaskPreferencesUtils().putInt(tag, c).apply();
            return c;
        }
        getTaskPreferencesUtils().putInt(tag, count).apply();
        return -1;
    }

    public static void markTaskDone(String tag) {
        getTaskPreferencesUtils().remove(tag);
    }

    public static boolean isToDaysDo(int day, String tag) {
        long date = getTaskPreferencesUtils().getLong(tag, -1);
        return isToDaysDo(date, day);
    }

    public static boolean isToDaysDo(long date, int day) {
        if (date == -1) {
            return true;
        }

        DateTime next = new DateTime(date);
        DateTime now = DateTime.now();
        return isToDaysDo(next, now, day);
    }

    /**
     * 上一次操作与现在操作的距离，单位天
     *
     * @param next local date
     * @param now local date
     * @param day 距离，天
     * @return true，超过或等于@param day
     */
    public static boolean isToDaysDo(DateTime next, DateTime now, int day) {
        int days = Days.daysBetween(next.toLocalDate(), now.toLocalDate()).getDays();
        return days >= day;
    }

    public static void markDone(String tag) {
        getTaskPreferencesUtils().putLong(tag, DateTime.now().getMillis()).apply();
    }
}
