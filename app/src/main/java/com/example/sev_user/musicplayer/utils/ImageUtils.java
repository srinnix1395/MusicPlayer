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
            R.color.colorBlue,
            R.color.colorLightGreen
    };
    private static int[] PLACE_HOLDER ={
            R.drawable.background_no_image_pink,
            R.drawable.background_no_image_light_purple,
            R.drawable.background_no_image_green,
            R.drawable.background_no_image_orange,
            R.drawable.background_no_image_blue,
            R.drawable.background_no_image_light_green
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
        return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

}
