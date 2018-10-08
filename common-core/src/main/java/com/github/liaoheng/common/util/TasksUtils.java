package com.github.liaoheng.common.util;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.liaoheng.common.Common;

import org.joda.time.DateTime;

/**
 * 任务工具
 *
 * @author liaoheng
 * @version 2018-01-24 13:57
 */
public class TasksUtils {
    private final static String TASK_FILE_NAME = "com.github.liaoheng.common_tasks";
    private final static String TASK_ONE = "task_one";
    private PreferencesUtils mTaskPreferencesUtils;
    private static TasksUtils mTasksUtils;

    public TasksUtils(PreferencesUtils taskPreferencesUtils) {
        mTaskPreferencesUtils = taskPreferencesUtils;
    }

    public static TasksUtils get(Context context) {
        if (mTasksUtils == null) {
            mTasksUtils = new TasksUtils(PreferencesUtils.from(context, TASK_FILE_NAME));
        }
        return mTasksUtils;
    }

    public static TasksUtils get() {
        if (mTasksUtils == null) {
            mTasksUtils = new TasksUtils(PreferencesUtils.from(TASK_FILE_NAME));
        }
        return mTasksUtils;
    }

    private PreferencesUtils getTaskPreferencesUtils() {
        if (mTaskPreferencesUtils == null) {
            throw new IllegalArgumentException("PreferencesUtils is null");
        }
        return mTaskPreferencesUtils;
    }

    /**
     * 首次
     */
    public boolean isOne() {
        return getTaskPreferencesUtils().getBoolean(TASK_ONE, true);
    }

    /**
     * 首次完成
     */
    public void markOne() {
        getTaskPreferencesUtils().putBoolean(TASK_ONE, false).apply();
    }

    /**
     * 记次任务，从0开始计数
     *
     * @param count 需要执行多少次
     * @param tag 任务标记
     * @return 剩余多少次，为0时任务完成
     */
    public int taskCount(int count, String tag) {
        if (count < 1) {
            throw new IllegalArgumentException("count < 1 ");
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
        return count;
    }

    /**
     * 任务 完成，删除
     */
    public void markTaskDone(String tag) {
        getTaskPreferencesUtils().remove(tag);
    }

    /**
     * 任务 完成，时间戳
     */
    public void markDone(String tag) {
        getTaskPreferencesUtils().putLong(tag, DateTime.now().getMillis()).apply();
    }

    /**
     * 上一次任务操作与当前任务操作的距离，单位天
     *
     * @param day 距离，天
     * @param tag 任务标记
     * @return true，超过或等于@param day
     */
    public boolean isToDaysDo(int day, String tag) {
        long date = getTaskPreferencesUtils().getLong(tag, -1);
        return DateTimeUtils.isToDaysDo(date, day);
    }

    //*************************Provider*******************************//

    public static boolean isToDaysDoProvider(Context context, int day, String tag) {
        Cursor cursor = context.getContentResolver()
                .query(TasksContract.TaskEntry.CONTENT_URI, null, TasksContract.TaskEntry.COLUMN_TAG + "=?",
                        new String[] { tag }, null);
        long date = -1;
        if (cursor != null && cursor.moveToNext()) {
            date = cursor.getLong(2);
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(TasksContract.TaskEntry.COLUMN_TAG, tag);
            contentValues.put(TasksContract.TaskEntry.COLUMN_DATE, date);
            context.getContentResolver().insert(TasksContract.TaskEntry.CONTENT_URI, contentValues);
        }
        if (cursor != null) {
            cursor.close();
        }
        return DateTimeUtils.isToDaysDo(date, day);
    }

    public static final String DB_CREATE = "create table " + TasksContract.TaskEntry.TABLE_NAME +
            " (" + TasksContract.TaskEntry._ID + " integer primary key autoincrement, " +
            TasksContract.TaskEntry.COLUMN_TAG + " text not null, " +
            TasksContract.TaskEntry.COLUMN_DATE + " integer not null);";

    public static class TasksContract {
        public static String CONTENT_AUTHORITY = Common.getPackageName();
        private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
        public static final String PATH = "tasks";

        public static final class TaskEntry implements BaseColumns {

            public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH).build();

            public static Uri buildUri(long id) {
                return ContentUris.withAppendedId(CONTENT_URI, id);
            }

            public static final String TABLE_NAME = "tasks";

            public static final String COLUMN_TAG = "tag";
            public static final String COLUMN_DATE = "date";
        }
    }

    public static class TasksProvider {
        private SQLiteOpenHelper mDbHelper;
        private UriMatcher mUriMatcher;

        public boolean onCreate(SQLiteOpenHelper helper) {
            mDbHelper = helper;
            mUriMatcher = buildUriMatcher();
            return true;
        }

        private final int OK = 111;

        UriMatcher buildUriMatcher() {
            final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
            final String authority = TasksContract.CONTENT_AUTHORITY;

            matcher.addURI(authority, TasksContract.PATH, OK);

            return matcher;
        }

        @Nullable
        public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                @Nullable String[] selectionArgs, @Nullable String sortOrder) {
            if (mUriMatcher.match(uri) != OK) {
                throw new IllegalArgumentException("Error Uri: " + uri);
            }
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            return db.query(TasksContract.TaskEntry.TABLE_NAME, projection, selection, selectionArgs, sortOrder, null,
                    null);
        }

        @Nullable
        public String getType(@NonNull Uri uri) {
            return null;
        }

        @Nullable
        public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
            if (mUriMatcher.match(uri) != OK) {
                throw new IllegalArgumentException("Error Uri: " + uri);
            }
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            long _id;
            _id = db.insert(TasksContract.TaskEntry.TABLE_NAME, null, values);
            Uri returnUri;
            if (_id > 0) {
                returnUri = TasksContract.TaskEntry.buildUri(_id);
            } else {
                throw new SQLException("Failed to insert row into " + uri);
            }
            return returnUri;
        }

        public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
            if (mUriMatcher.match(uri) != OK) {
                throw new IllegalArgumentException("Error Uri: " + uri);
            }
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            return db.delete(TasksContract.TaskEntry.TABLE_NAME, selection, selectionArgs);
        }

        public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                @Nullable String[] selectionArgs) {
            if (mUriMatcher.match(uri) != OK) {
                throw new IllegalArgumentException("Error Uri: " + uri);
            }
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            return db.update(TasksContract.TaskEntry.TABLE_NAME, values, selection, selectionArgs);
        }
    }

}
