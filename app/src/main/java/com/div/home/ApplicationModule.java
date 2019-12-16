package com.div.home;

import android.content.Context;

import com.div.home.util.AuthenticationRequestInterceptor;
import com.div.home.util.Constants;
import com.div.home.util.Preferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Nirav Mandani on 13-02-2019.
 * Followal Solutions
 */
@Module
public class ApplicationModule {

    private final Context appContext;

    public ApplicationModule(Context appContext) {
        this.appContext = appContext;
    }

    @Provides
    @Singleton
    Context provideAppContext() {
        return appContext;
    }

    @Provides
    @Singleton
    Preferences providePreferences() {
        return new Preferences(appContext);
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder().serializeSpecialFloatingPointValues();
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient okHttpClient, Gson gson) {
        return new Retrofit.Builder().baseUrl(Constants.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkClient(AuthenticationRequestInterceptor authenticationRequestInterceptor) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().writeTimeout(5, TimeUnit.MINUTES).readTimeout(5, TimeUnit.MINUTES).addInterceptor(authenticationRequestInterceptor).addInterceptor(logging).build();
        return okHttpClient;
    }

}
