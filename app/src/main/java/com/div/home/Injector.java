package com.div.home;

/**
 * Created by Nirav Mandani on 13-02-2019.
 * Followal Solutions
 */
public enum Injector {
    INSTANCE;

    private static final String TAG = "Injector";

    private ApplicationComponent appComponent;
//    private UserComponent userComponent;

    private boolean isInitialized;

    /*public void init(Application application) {
        if (isInitialized) {
            Log.e(TAG, "Injector init called twice");
            return;
        }
        appComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(application)).build();
        isInitialized = true;
//        resetInjector();
    }*/

   /* private void resetInjector() {
        appComponent = null;
        isInitialized = false;
    }*/

    public ApplicationComponent appComponent() {
        checkInitialization();
        return appComponent;
    }


   /* public UserComponent userComponent() {
        checkInitialization();
        return userComponent;
    }*/

    private void checkInitialization() {
        if (!isInitialized) throw new IllegalStateException();
    }

    /*public void setUserComponent(Preferences preferences) {
        checkInitialization();
        if (preferences.getString(PREF_TOKEN) == null || preferences.getString(PREF_TOKEN).isEmpty()) {
            userComponent = null;
        } else {
            userComponent = DaggerUserComponent.builder()
                    .applicationComponent(appComponent).userModule(new UserModule(preferences))
                    .build();

        }
    }*/
}
