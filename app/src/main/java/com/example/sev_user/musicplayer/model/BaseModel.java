package com.example.sev_user.musicplayer.model;

import android.os.Parcelable;

/**
 * Created by DELL on 11/19/2016.
 */

public abstract class BaseModel implements Parcelable {
    public static final int TYPE_ALBUM = 0;
    public static final int TYPE_ARTIST = 1;
    public static final int TYPE_SONG = 2;
    public static final int TYPE_SONG_PLUS = 3;
    public static final int TYPE_HEADER = 4;

    protected boolean hasLine;

    public boolean isHasLine() {
        return hasLine;
    }

    public void setHasLine(boolean hasLine) {
        this.hasLine = hasLine;
    }

    public abstract int getTypeModel();
}
