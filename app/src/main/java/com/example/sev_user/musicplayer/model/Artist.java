package com.example.sev_user.musicplayer.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sev_user on 7/14/2016.
 */
public class Artist extends BaseModel implements Parcelable{
    private int id;
    private String name;
    private String info;
    private int colorPlaceHolder;

    public Artist(int id, String name, String info, int colorPlaceHolder, boolean hasLine) {
        this.id = id;
        this.name = name;
        this.info = info;
        this.colorPlaceHolder = colorPlaceHolder;
        this.hasLine = hasLine;
    }

    protected Artist(Parcel in) {
        id = in.readInt();
        name = in.readString();
        info = in.readString();
        colorPlaceHolder = in.readInt();
        hasLine = in.readByte() != 0;
    }

    public static final Creator<Artist> CREATOR = new Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel in) {
            return new Artist(in);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public int getColorPlaceHolder() {
        return colorPlaceHolder;
    }

    @Override
    public int getTypeModel() {
        return BaseModel.TYPE_ARTIST;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(info);
        parcel.writeInt(colorPlaceHolder);
        parcel.writeByte((byte) (hasLine ? 1 : 0));
    }
}
