package com.div.home.util;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Nirav Mandani on 16-12-2019.
 * Followal Solutions
 */
public class Utils {

    public static boolean isNetworkConnected(Context con) {
        ConnectivityManager cm = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


}
