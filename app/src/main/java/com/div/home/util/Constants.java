package com.div.home.util;

import com.div.home.BuildConfig;

/**
 * Created by Nirav Mandani on 16-12-2019.
 * Followal Solutions
 */
public class Constants {

    public static final String LIVE_URL = "https://api.followal.com/"; // Live New Server Url
    public static final String LOCAL_URL = "http://192.168.0.161:3005/"; // Live New Server Url
    public static final int GPS_REQUEST = 999;


    public static final String[] customWeekDays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};


    public static String DEBUG_URL = BuildConfig.DEBUG ? LOCAL_URL : LIVE_URL;//Testing Local server
    public static String VERSION = "api/v1/"; // Live New Server Url
    public static String BASE_URL = String.format("%s%s", DEBUG_URL, VERSION);

    public static final class Pref {
        public static final String PREF_TOKEN = "pref_token";
        public static final String PREF_FCM_TOKEN = "pref_fcm_token";

        public static final String PREF_USER_FIRST_NAME = "pref_user_first_name";
        public static final String PREF_USER_LAST_NAME = "pref_user_last_name";
        public static final String PREF_USER_PHONE_NUMBER = "pref_user_phone_number";
        public static final String PREF_USER_ADDRESS = "pref_user_address";
        public static final String PREF_USER_CITY = "pref_user_city";
        public static final String PREF_USER_STATE = "pref_user_state";

    }
}
