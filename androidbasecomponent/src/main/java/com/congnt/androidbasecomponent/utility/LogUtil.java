package com.congnt.androidbasecomponent.utility;

import android.util.Log;

import java.util.Arrays;

public class LogUtil {
    private static boolean isDebug = true;
    private static String tag = "NO TAG";

    /**
     * Setup Log Util
     *
     * @param isDebug If TRUE all log will print, else FALSE the log will be off
     * @param tag     Tag text using for Log TAG
     */
    public static void init(boolean isDebug, String tag) {
        LogUtil.isDebug = isDebug;
        LogUtil.tag = tag;
    }

    /**
     * Show log with info type
     *
     * @param object
     */
    public static void i(Object object) {
        if (isDebug) {
            Log.i(tag, String.valueOf(object));
        }
    }

    /**
     * Show log with debug type
     *
     * @param object
     */
    public static void d(Object object) {
        if (isDebug) {
            Log.d(tag, String.valueOf(object));
        }
    }

    /**
     * Show log with error type
     *
     * @param object
     */
    public static void e(Object... object) {
        if (isDebug) {
            Log.e(tag, Arrays.toString(object));
        }
    }

    /**
     * Show log with warning type
     *
     * @param object
     */
    public static void w(Object object) {
        if (isDebug) {
            Log.w(tag, String.valueOf(object));
        }
    }
}