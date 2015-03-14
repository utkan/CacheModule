package tr.com.utkansargin.cachemodule;

import android.app.Application;

public class ApplicationController extends Application {

    private static ApplicationController instance = null;

    public static ApplicationController getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
    }
}
