package com.example.sev_user.musicplayer.model;

/**
 * Created by DELL on 11/19/2016.
 */

public class Header extends BaseModel {
    private String header;

    public Header(String header) {
        this.header = header;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    @Override
    public int getTypeModel() {
        return BaseModel.TYPE_HEADER;
    }
}
