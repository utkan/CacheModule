package tr.com.utkansargin.cachemodule.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import java.io.InvalidObjectException;
import java.util.Map;

import tr.com.utkansargin.cachemodule.ApplicationController;

public class SharedPreferencesCache extends AbstractCacher {

    SharedPreferences sharedPreferences;

    private static SharedPreferencesCache instance = null;
    SharedPreferences.Editor editor;
    private static String prefix;

    protected SharedPreferencesCache() {

        prefix = this.getClass().getSimpleName();

        sharedPreferences = ApplicationController.getInstance().getApplicationContext()
                .getSharedPreferences(
                        this.getClass().getSimpleName(), Context.MODE_PRIVATE);
    }

    public static SharedPreferencesCache getInstance() {
        if (instance == null) {
            instance = new SharedPreferencesCache();
        }
        return instance;
    }

    public SharedPreferences.Editor getEditor() {
        return sharedPreferences.edit();
    }

    @Override
    protected void clear() {

        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith(prefix)) {
                getEditor().remove(key);
            }
        }
        getEditor().commit();
    }

    @Override
    protected void remove(String key) {
        getEditor().remove(prefix + key).commit();
    }

    @Override
    protected <T> void put(String key, T value) {

        if (TextUtils.isEmpty(key)) {
            return;
        }

        if (value.getClass() == Integer.class) {
            getEditor().putInt(prefix + key, (Integer) value).commit();
        } else if (value.getClass() == Boolean.class) {
            getEditor().putBoolean(prefix + key, (Boolean) value).commit();
        } else if (value.getClass() == Float.class) {
            getEditor().putFloat(prefix + key, (Float) value).commit();
        } else if (value.getClass() == Long.class) {
            getEditor().putLong(prefix + key, (Long) value).commit();
        } else if (value.getClass() == String.class) {
            getEditor().putString(prefix + key, (String) value).commit();
        } else {
            Log.e(this.getClass().getSimpleName(), "value has not a handled class");
        }

        if (nextCacher != null) {
            nextCacher.putCache(key, value);
        }
    }

    @Override
    protected <T> T get(String key) {

        if (TextUtils.isEmpty(key) || !sharedPreferences.contains(prefix + key)) {
            return null;
        }

        T value = null;

        Map<String, ?> allEntries = sharedPreferences.getAll();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {

            if (entry.getKey().equals(prefix + key)) {

                value = (T) entry.getValue();

                break;
            }
        }

        if (value == null) {

            if (nextCacher != null) {
                value = nextCacher.getCache(key);
            }

            if (value != null) {
                put(key, value);
            }
        }

        return value;
    }
}
