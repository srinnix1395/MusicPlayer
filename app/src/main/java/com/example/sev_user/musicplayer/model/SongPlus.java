package com.example.sev_user.musicplayer.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sev_user on 7/21/2016.
 */
public class SongPlus extends BaseModel implements Parcelable {
    private Song song;
    private int positionSeekBar;
    private int position;

    public SongPlus(Song song, int positionSeekBar, int position) {
        this.song = song;
        this.positionSeekBar = positionSeekBar;
        this.position = position;
    }

    protected SongPlus(Parcel in) {
        positionSeekBar = in.readInt();
        position = in.readInt();
    }

    public static final Creator<SongPlus> CREATOR = new Creator<SongPlus>() {
        @Override
        public SongPlus createFromParcel(Parcel in) {
            return new SongPlus(in);
        }

        @Override
        public SongPlus[] newArray(int size) {
            return new SongPlus[size];
        }
    };

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

    @Override
    public int getTypeModel() {
        return BaseModel.TYPE_SONG_PLUS;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(positionSeekBar);
        parcel.writeInt(position);
    }
}
