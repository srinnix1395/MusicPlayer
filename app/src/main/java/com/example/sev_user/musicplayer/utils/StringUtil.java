package com.example.sev_user.musicplayer.utils;

import java.util.ArrayList;

/**
 * Created by sev_user on 7/19/2016.
 */
public class StringUtil {
    public static boolean hasNotLetter(ArrayList<String> arrayList) {
        for (String s : arrayList) {
            if (!Character.isLetter(s.charAt(0))) {
                return true;
            }
        }
        return false;
    }
}
