package com.example.sev_user.musicplayer.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.example.sev_user.musicplayer.R;

/**
 * Created by sev_user on 7/15/2016.
 */
public class ImageUtils {
	private static final int COLORS[] = {
			R.color.colorPink,
			R.color.colorDarkPurple,
			R.color.colorGreen,
			R.color.colorOrange,
			R.color.colorBlue,
			R.color.colorLightPurple,
			R.color.colorLightGreen
	};
	private static int[] PLACE_HOLDER = {
			R.drawable.background_no_image_pink,
			R.drawable.background_no_image_light_green,
			R.drawable.background_no_image_light_purple,
			R.drawable.background_no_image_orange,
			R.drawable.background_no_image_blue,
			R.drawable.background_no_image_green,
			R.drawable.background_no_image_dark_purple
	};
	
	
	public static int randomImage(int i) {
		return PLACE_HOLDER[i % COLORS.length];
	}
	
	public static int randomColor(int i) {
		return COLORS[i % COLORS.length];
	}
	
	public static float convertDpToPixel(float dp, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
	}
	
}
