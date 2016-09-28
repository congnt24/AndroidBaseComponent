package com.congnt.androidbasecomponent.utility;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioAttributes;
import android.os.Vibrator;

public class VibratorUtil {

    @TargetApi(11)
    public static boolean hasVibrator(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        return vibrator.hasVibrator();
    }

    public static void vibrate(Context context) {
        vibrate(context, 200);
    }

    public static void vibrate(Context context, long milliseconds) {
        vibrate(context, new long[]{milliseconds});
    }

    public static void vibrate(Context context, long[] pattern) {
        vibrate(context, pattern, -1);
    }

    public static void vibrate(Context context, long[] pattern, int repeat) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(pattern, repeat);
    }

    @TargetApi(21)
    public static void vibrate(Context context, long milliseconds, AudioAttributes attributes) {
        vibrate(context, new long[]{milliseconds}, -1, attributes);
    }

    @TargetApi(21)
    public static void vibrate(Context context, long[] pattern, int repeat, AudioAttributes attributes) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(pattern, repeat, attributes);
    }

    public static void cancel(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.cancel();
    }
}