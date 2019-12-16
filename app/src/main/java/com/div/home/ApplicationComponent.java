package com.div.home;

import android.content.Context;

import com.div.home.util.Preferences;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * Created by Nirav Mandani on 13-02-2019.
 * Followal Solutions
 */
@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    Context context();

    Preferences sharedPreferences();

    Retrofit retrofit();

    void inject(DivolHomeApp divolHomeApp);

}
