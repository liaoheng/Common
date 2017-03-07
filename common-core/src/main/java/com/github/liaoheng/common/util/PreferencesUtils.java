package com.github.liaoheng.common.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.SharedPreferencesCompat;
import java.util.Map;

/**
 * SharedPreferences util
 * @see <a href="https://github.com/tushar-acharya/PrefCompat">Modified from</a>
 * @see <a href="http://stackoverflow.com/questions/16319237/cant-put-double-sharedpreferences">SharedPreferences double</a>
 */
public class PreferencesUtils {
    private static Context                  mContext;
    private        SharedPreferences        mSharedPreferences;
    private        SharedPreferences.Editor mSharedPreferencesEditor;

    public static void init(Context context) {
        mContext = context.getApplicationContext();
    }

    public static PreferencesUtils with() {
        if (mContext == null) {
            throw new IllegalStateException("not init");
        }
        return new PreferencesUtils(PreferenceManager.getDefaultSharedPreferences(mContext));
    }

    public static PreferencesUtils from(String name) {
        return from(name, Context.MODE_PRIVATE);
    }

    public static PreferencesUtils from(String name, int mode) {
        if (mContext == null) {
            throw new IllegalStateException("not init");
        }
        return new PreferencesUtils(mContext.getSharedPreferences(name, mode));
    }

    public PreferencesUtils(SharedPreferences sharedPreferences) {
        this.mSharedPreferences = sharedPreferences;
    }

    @SuppressLint("CommitPrefEdits") private SharedPreferences.Editor getSharedPreferencesEditor() {
        if (mSharedPreferencesEditor == null) {
            mSharedPreferencesEditor = mSharedPreferences.edit();
        }
        return mSharedPreferencesEditor;
    }

    public PreferencesUtils putString(String key, String value) {
        getSharedPreferencesEditor().putString(key, value);
        return this;
    }

    public PreferencesUtils putInt(String key, int value) {
        getSharedPreferencesEditor().putInt(key, value);
        return this;
    }

    public PreferencesUtils putLong(String key, long value) {
        getSharedPreferencesEditor().putLong(key, value);
        return this;
    }

    public PreferencesUtils putFloat(String key, float value) {
        getSharedPreferencesEditor().putFloat(key, value);
        return this;
    }

    public PreferencesUtils putDouble(String key, double value) {
        getSharedPreferencesEditor().putLong(key, Double.doubleToRawLongBits(value));
        return this;
    }

    public PreferencesUtils putBoolean(String key, boolean value) {
        getSharedPreferencesEditor().putBoolean(key, value);
        return this;
    }

    public String getString(String key) {
        return getString(key, "");
    }

    public String getString(String key, String def) {
        return mSharedPreferences.getString(key, def);
    }

    public int getInt(String key) {
        return getInt(key, -1);
    }

    public int getInt(String key, int def) {
        return mSharedPreferences.getInt(key, def);
    }

    public long getLong(String key) {
        return getLong(key, -1);
    }

    public long getLong(String key, long def) {
        return mSharedPreferences.getLong(key, def);
    }

    public float getFloat(String key) {
        return getFloat(key, -1);
    }

    public float getFloat(String key, float def) {
        return mSharedPreferences.getFloat(key, def);
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean def) {
        return mSharedPreferences.getBoolean(key, def);
    }

    public double getDouble(String key) {
        return getDouble(key, -1);
    }

    public double getDouble(String key, double def) {
        return Double
                .longBitsToDouble(mSharedPreferences.getLong(key, Double.doubleToLongBits(def)));
    }

    public void remove(String key) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove(key);
        getSharedPreferencesCompat().apply(editor);
    }

    /**
     * 清除所有数据
     */
    public void clear() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        getSharedPreferencesCompat().apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param key
     * @return
     */
    public boolean contains(String key) {
        return mSharedPreferences.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @return
     */
    public Map<String, ?> getAll() {
        return mSharedPreferences.getAll();
    }

    public SharedPreferencesCompat.EditorCompat getSharedPreferencesCompat() {
        return SharedPreferencesCompat.EditorCompat.getInstance();
    }

    public void apply() {
        if (mSharedPreferencesEditor == null) {
            return;
        }
        getSharedPreferencesCompat().apply(mSharedPreferencesEditor);
        mSharedPreferencesEditor = null;
    }
}
