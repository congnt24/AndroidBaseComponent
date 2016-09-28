package com.congnt.androidbasecomponent.utility;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Build;
import android.os.Looper;
import android.os.PowerManager;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by congnt24 on 27/09/2016.
 */

public class AndroidUtil {

    //The Wake lock.
    private static PowerManager.WakeLock wakeLock;

    //THREAD
    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    //SERVICE

    /**
     * Check if the service is running in the device.
     *
     * @param context      the context
     * @param serviceClass the service class
     * @return boolean boolean
     */
    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        try {
            ActivityManager manager =
                    (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(
                    Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //KEYBOARD

    /**
     * Hide soft keyboard when release from EditText
     *
     * @param view
     */
    public static void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Show soft keyboard when focus at EditText
     *
     * @param view
     */
    public static void showKeyboard(View view) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1 && view.hasFocus()) {
            view.clearFocus();
        }
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);
    }

    //VIEW

    /**
     * Get the screen dimensions
     *
     * @param activity the activity
     * @return the int [ ]
     */
    public static int[] getScreenSize(Activity activity) {
        Point size = new Point();
        WindowManager w = activity.getWindowManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            w.getDefaultDisplay().getSize(size);
            return new int[]{size.x, size.y};
        } else {
            Display d = w.getDefaultDisplay();
            return new int[]{d.getWidth(), d.getHeight()};
        }
    }

    /**
     * Set orientation change lock
     *
     * @param activity the activity
     * @param status   the status
     */
    public static void setLockOrientation(Activity activity, boolean status) {
        if (status) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
            }
        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
        }
    }

    //WAKE LOCK

    /**
     * Hold wake lock.
     *
     * @param context the context
     */
    public static void holdWakeLock(Context context) {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");
        wakeLock.acquire();
    }

    /**
     * Release wake lock.
     */
    public static void releaseWakeLock() {
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
        }
    }
}
