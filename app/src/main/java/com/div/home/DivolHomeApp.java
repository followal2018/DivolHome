package com.div.home;

import android.app.Application;

import com.div.home.util.AppContext;

/**
 * Created by Nirav Mandani on 16-12-2019.
 * Followal Solutions
 */
public class DivolHomeApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppContext.getInstance().setContext(this);


    }
}
