package com.example.sev_user.musicplayer.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.sev_user.musicplayer.constant.Constant;

/**
 * Created by sev_user on 7/21/2016.
 */
public class SharedPreUtil {
	private SharedPreferences sharedPreferences;
	private static SharedPreUtil instance;
	
	public static SharedPreUtil getInstance(Context context) {
		if (instance == null) {
			instance = new SharedPreUtil(context);
		}
		return instance;
	}
	
	private SharedPreUtil(Context context) {
		sharedPreferences = context.getSharedPreferences(Constant.MUSIC_PLAYER_APP, Context.MODE_APPEND);
	}
	
	public void putBoolean(String key, boolean value) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.apply();
	}
	
	public boolean getBoolean(String key, boolean defValue) {
		return sharedPreferences.getBoolean(key, defValue);
	}
}
