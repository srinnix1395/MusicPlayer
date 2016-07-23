package com.example.sev_user.musicplayer.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.example.sev_user.musicplayer.R;

import java.util.Random;

/**
 * Created by sev_user on 7/15/2016.
 */
public class ImageUtils {
    public static int COLORS[] = {
            R.color.colorAccent,
            R.color.colorLightPurple,
            R.color.colorGreen,
            R.color.colorOrange,
            R.color.colorAccent,
            R.color.colorBlue
    };

    public static int PLACE_HOLDER[] = {
            R.drawable.song_cover_accent,
            R.drawable.song_cover_blue,
            R.drawable.song_cover_dark_purple,
            R.drawable.song_cover_green,
            R.drawable.song_cover_light_purple,
            R.drawable.song_cover_orange
    };

    public static int randomImage() {
        Random random = new Random();
        int number = random.nextInt(PLACE_HOLDER.length);
        return PLACE_HOLDER[number];
    }

    public static int randomColor() {
        Random random = new Random();
        int number = random.nextInt(COLORS.length);
        return COLORS[number];
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

}
