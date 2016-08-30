package com.github.liaoheng.common.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.SharedPreferencesCompat;

import java.util.List;
import java.util.Map;

/**
 * SharedPreferences封装类
 * <a href="https://github.com/tushar-acharya/PrefCompat">部分代码</a>
 */
public class PreferencesUtils {
    private static Context mContext;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mSharedPreferencesEditor;

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

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key
     * @param object
     */
    @SuppressLint("CommitPrefEdits")
    public PreferencesUtils put(String key, Object object) {
        if (mSharedPreferencesEditor == null)
            mSharedPreferencesEditor = mSharedPreferences.edit();
        put(mSharedPreferencesEditor, key, object);
        return this;
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key
     * @param object
     */
    public void putApply(String key, Object object) {
        put(key,object);
        apply();
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key
     * @param object
     */
    private void put(SharedPreferences.Editor editor, String key, Object object) {
        if (object == null) return;
        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }
    }

    public void putListApply(List<NameValue<String, Object>> list) {
        putList(list);
        apply();
    }

    @SuppressLint("CommitPrefEdits")
    public PreferencesUtils putList(List<NameValue<String, Object>> list) {
        if (mSharedPreferencesEditor == null)
            mSharedPreferencesEditor = mSharedPreferences.edit();
        for (NameValue<String, Object> arg : list) {
            put(mSharedPreferencesEditor, arg.getName(), arg.getValue());
        }
        return this;
    }

    public void apply() {
        if (mSharedPreferencesEditor == null) {
            return;
        }
        getSharedPreferencesCompat().apply(mSharedPreferencesEditor);
        mSharedPreferencesEditor = null;
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param key
     * @param defaultObject
     * @return
     */
    public Object get(String key, Object defaultObject) {
        return get(mSharedPreferences, key, defaultObject);
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param sp
     * @param key
     * @param defaultObject
     * @return
     */
    private Object get(SharedPreferences sp, String key, Object defaultObject) {
        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }
        return null;
    }

    /**
     * 得到保存String数据
     *
     * @param key
     * @return
     */
    public String getString(String key) {
        return getString(key, "");
    }

    /**
     * 得到保存String数据
     *
     * @param key
     * @param def
     * @return
     */
    public String getString(String key, String def) {
        return (String) get(mSharedPreferences, key, def);
    }

    /**
     * 得到保存int数据
     *
     * @param key
     * @return
     */
    public int getInt(String key) {
        return getInt(key, -1);
    }

    /**
     * 得到保存int数据
     *
     * @param key
     * @param def
     * @return
     */
    public int getInt(String key, int def) {
        return (int) get(mSharedPreferences, key, def);
    }

    /**
     * 得到保存long数据
     *
     * @param key
     * @return
     */
    public long getLong(String key) {
        return getLong(key, -1);
    }

    /**
     * 得到保存long数据
     *
     * @param key
     * @param def
     * @return
     */
    public long getLong(String key, long def) {
        return (long) get(mSharedPreferences, key, def);
    }

    /**
     * 得到保存float数据
     *
     * @param key
     * @return
     */
    public float getFloat(String key) {
        return getFloat(key, -1);
    }

    /**
     * 得到保存float数据
     *
     * @param key
     * @param def
     * @return
     */
    public float getFloat(String key, float def) {
        return (float) get(mSharedPreferences, key, def);
    }

    /**
     * 得到保存boolean数据
     *
     * @param key
     * @return
     */
    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    /**
     * 得到保存boolean数据
     *
     * @param key
     * @param def
     * @return
     */
    public boolean getBoolean(String key, boolean def) {
        return (boolean) get(mSharedPreferences, key, def);
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param key
     */
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
}
