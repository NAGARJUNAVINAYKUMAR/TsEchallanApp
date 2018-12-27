package com.tspolice.echallan;

import android.app.Application;

/**
 * Created by Srinivas on 9/17/2018.
 */

public class App extends Application {
    private static App instance;

    public static App getInstance() {

        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initApplication();
    }

    private void initApplication()
    {
        instance = this;
    }
}
