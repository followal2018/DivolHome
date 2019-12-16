package com.div.home.util;

import android.annotation.SuppressLint;
import android.content.Context;


public final class AppContext {
    @SuppressLint("StaticFieldLeak")
    private static AppContext sInstance;
    private Context context;
    private Preferences pref;

    private AppContext() {

    }

    public static AppContext getInstance() {
        if (AppContext.sInstance == null) {
            AppContext.sInstance = new AppContext();
        }

        return AppContext.sInstance;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
        this.pref = new Preferences(context);
    }

    public Preferences getPreference() {
        return pref;
    }

    public String getAccessToken() {
        return pref.getString(Constants.Pref.PREF_TOKEN);
    }

    public void setAccessToken(String accessToken) {
        pref.putString(Constants.Pref.PREF_TOKEN, accessToken);
    }

}