package com.congnt.androidbasecomponent.utility;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by congnt24 on 24/09/2016.
 */

public class SoundUtil {
    public static SoundUtil instance;
    private MediaPlayer mediaPlayer;

    public SoundUtil() {
    }

    public static SoundUtil getInstance() {
        if (instance == null) {
            instance = new SoundUtil();
        }
        return instance;
    }

    public void playSound(Context context, int idRaw) {
        mediaPlayer = MediaPlayer.create(context, idRaw);
        mediaPlayer.start();
    }

    public void playSoundRepeat(Context context, int idRaw) {
        mediaPlayer = MediaPlayer.create(context, idRaw);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.setLooping(false);
            mediaPlayer.reset();
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
