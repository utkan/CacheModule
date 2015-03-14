package tr.com.utkansargin.cachemodule.cache;

import android.util.Log;

import com.google.gson.Gson;

public abstract class AbstractCacher {

    //next element in chain or responsibility
    protected AbstractCacher nextCacher;

    public void setNextCacher(AbstractCacher nextCacher) {
        this.nextCacher = nextCacher;
    }

    public synchronized void clearCache() {

        clear();

        if (nextCacher != null) {
            nextCacher.clearCache();
        }
    }

    public synchronized void removeCache(String key) {

        remove(key);

        if (nextCacher != null) {
            nextCacher.removeCache(key);
        }
    }

    public synchronized <T> void putCache(String key, T value) {

        Log.i(this.getClass().getSimpleName(), "putCache:" + key);

        if (value.getClass() == Integer.class ||
                value.getClass() == Boolean.class ||
                value.getClass() == Float.class ||
                value.getClass() == Long.class ||
                value.getClass() == String.class
                ) {
            put(key, value);
        } else {
            put(key, new Gson().toJson(value));
        }

        if (nextCacher != null) {
            nextCacher.putCache(key, value);
        }
    }

    public synchronized <T> T getCache(String key) {

        T value = get(key);

        Log.i(this.getClass().getSimpleName(), "getCache:" + key);

        if (value == null && nextCacher != null) {
            value = nextCacher.getCache(key);

            put(key, value);
        }

        return value;
    }

    public synchronized <T> T getCache(String key, Class<?> clzz) {

        T value = (T) new Gson().fromJson((String) get(key), clzz);

        Log.i(this.getClass().getSimpleName(), "getCache:" + key);

        if (value == null && nextCacher != null) {
            value = nextCacher.getCache(key, clzz);

            put(key, new Gson().toJson(value));
        }

        return value;
    }


    abstract protected void clear();

    abstract protected void remove(String key);

    abstract protected <T> void put(String key, T value);

    abstract protected <T> T get(String key);
}
