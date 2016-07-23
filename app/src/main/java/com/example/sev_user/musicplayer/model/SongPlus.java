package com.example.sev_user.musicplayer.model;

/**
 * Created by sev_user on 7/21/2016.
 */
public class SongPlus {
    private Song song;
    private int positionSeekBar;
    private int position;

    public SongPlus(Song song, int positionSeekBar, int position) {
        this.song = song;
        this.positionSeekBar = positionSeekBar;
        this.position = position;
    }

    public Song getSong() {
        return song;
    }

    public int getPositionSeekBar() {
        return positionSeekBar;
    }

    public int getPosition() {
        return position;
    }

    public void setPositionSeekBar(int positionSeekBar) {
        this.positionSeekBar = positionSeekBar;
    }
}
