package tr.com.utkansargin.cachemodule;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import java.util.Map;

import tr.com.utkansargin.cachemodule.cache.MemoryCache;
import tr.com.utkansargin.cachemodule.cache.SharedPreferencesCache;
import tr.com.utkansargin.cachemodule.model.Person;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MemoryCache memCache = MemoryCache.getInstance();
        SharedPreferencesCache spCache = SharedPreferencesCache.getInstance();
        memCache.setNextCacher(spCache);

        memCache.putCache("test", 333);

        Log.i("CACHE", String.valueOf(memCache.getCache("test")));

        int i = memCache.getCache("test");

        Log.i("CACHE", "i: " + i);


        spCache.putCache("tString", "utkan");
        spCache.putCache("tInt", 3);
        spCache.putCache("tFloat", 3.3f);

        String tsVar = spCache.getCache("tString");
        int tiVar = spCache.getCache("tInt");
        float tfVar = spCache.getCache("tFloat");

        Log.i("CACHE", String.valueOf(tsVar));
        Log.i("CACHE", String.valueOf(tiVar));
        Log.i("CACHE", String.valueOf(tfVar));

        Log.i("CACHE", String.valueOf(spCache.getCache("test")));

        Person p = new Person();
        p.setName("utkan");
        p.setSurname("sargin");

        spCache.putCache("person", p);

        Person pc = spCache.getCache("person", Person.class);

        Log.i("CACHE", "person: " + pc.getName() + " " + pc.getSurname());

        //develer kovalasin

        //1
        //2
        //hebele
    }


}
