package com.example.sev_user.musicplayer.model;

/**
 * Created by DELL on 11/19/2016.
 */

public abstract class BaseModel {
    public static final int TYPE_ALBUM = 0;
    public static final int TYPE_ARTIST = 1;
    public static final int TYPE_SONG = 2;
    public static final int TYPE_SONG_PLUS = 3;
    public static final int TYPE_HEADER = 4;


    public abstract int getTypeModel();
}
