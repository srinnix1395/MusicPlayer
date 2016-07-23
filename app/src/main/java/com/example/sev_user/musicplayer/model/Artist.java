package com.example.sev_user.musicplayer.model;

/**
 * Created by sev_user on 7/14/2016.
 */
public class Artist {
    private int id;
    private String name;
    private String info;
    private int colorPlaceHolder;
    private boolean hasLine;

    public Artist(int id, String name, String info, int colorPlaceHolder, boolean hasLine) {
        this.id = id;
        this.name = name;
        this.info = info;
        this.colorPlaceHolder = colorPlaceHolder;
        this.hasLine = hasLine;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public boolean isHasLine() {
        return hasLine;
    }

    public void setHasLine(boolean hasLine) {
        this.hasLine = hasLine;
    }

    public int getColorPlaceHolder() {
        return colorPlaceHolder;
    }
}
