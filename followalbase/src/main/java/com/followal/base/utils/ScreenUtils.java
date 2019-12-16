package com.followal.base.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

import androidx.annotation.NonNull;


public class ScreenUtils {

    public static int getScreenHeight(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }

    public static final class Companion {

        private Companion() {
        }

        public static final int getScreenWidth(@NonNull Context context) {
            Object systemService = context.getSystemService(Context.WINDOW_SERVICE);
            if (systemService == null) {
                throw new RuntimeException("null cannot be cast to non-null type android.view.WindowManager");
            } else {
                WindowManager windowManager = (WindowManager) systemService;
                DisplayMetrics dm = new DisplayMetrics();
                windowManager.getDefaultDisplay().getMetrics(dm);
                return dm.widthPixels;
            }
        }

        public static final int dpToPx(@NonNull Context context, int value) {
            return (int) TypedValue.applyDimension(1, (float) value, context.getResources().getDisplayMetrics());
        }

    }
}
