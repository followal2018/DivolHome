package com.followal.base.widget;

/**
 * Created by Vishal on 22-03-2019.
 * Followal Solutions
 */
public class Clock {

    private Clock() {
        throw new AssertionError("No Instances.");
    }

    public static long millis() {
        return System.currentTimeMillis();
    }

    public static long seconds() {
        return millis() / 1000;
    }
}
