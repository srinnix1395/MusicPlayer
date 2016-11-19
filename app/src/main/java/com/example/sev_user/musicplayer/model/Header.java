package com.example.sev_user.musicplayer.model;

import android.os.Parcel;

/**
 * Created by DELL on 11/19/2016.
 */

public class Header extends BaseModel {
    private String header;

    public Header(String header) {
        this.header = header;
    }

    protected Header(Parcel in) {
        header = in.readString();
    }

    public static final Creator<Header> CREATOR = new Creator<Header>() {
        @Override
        public Header createFromParcel(Parcel in) {
            return new Header(in);
        }

        @Override
        public Header[] newArray(int size) {
            return new Header[size];
        }
    };

    public String getHeader() {
        return header;
    }

    @Override
    public int getTypeModel() {
        return BaseModel.TYPE_HEADER;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(header);
    }
}
