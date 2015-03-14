package tr.com.utkansargin.cachemodule.cache;

import android.text.TextUtils;
import android.util.LruCache;

public class MemoryCache extends AbstractCacher {

    static int cacheSize = 4 * 1024 * 1024; // 4MiB
    static LruCache lruCache = new LruCache(cacheSize);

    private static MemoryCache instance = null;

    protected MemoryCache() {
        // Exists only to defeat instantiation.
    }

    public static MemoryCache getInstance() {
        if(instance == null) {
            instance = new MemoryCache();
        }
        return instance;
    }

    @Override
    protected void clear() {
        lruCache.evictAll();
    }

    @Override
    protected void remove(String key) {
        lruCache.remove(key);
    }

    @Override
    protected <T> void put(String key, T value) {

        if (TextUtils.isEmpty(key)) {
            return ;
        }

        lruCache.put(key, value);

        if (nextCacher != null) {
            nextCacher.putCache(key, value);
        }
    }

    @Override
    protected <T> T get(String key) {

        if (TextUtils.isEmpty(key)) {
            return null;
        }

        T value = null;

        if (lruCache.get(key) != null) {

            return (T) lruCache.get(key);

        } else {

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
