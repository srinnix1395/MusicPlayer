package com.example.sev_user.musicplayer.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sev_user on 7/15/2016.
 */
public class Song extends BaseModel implements Parcelable {
    private int id;
    private String image;
    private String name;
    private String data;
    private String artist;
    private int artistId;
    private String album;
    private int idAlbum;
    private int placeHolder;

    public Song(int id, String image, String name, String data, String artist, int artistId, String album, int idAlbum, boolean hasLine, int placeHolder) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.data = data;
        this.artist = artist;
        this.artistId = artistId;
        this.album = album;
        this.idAlbum = idAlbum;
        this.hasLine = hasLine;
        this.placeHolder = placeHolder;
    }

    protected Song(Parcel in) {
        id = in.readInt();
        image = in.readString();
        name = in.readString();
        data = in.readString();
        artist = in.readString();
        artistId = in.readInt();
        album = in.readString();
        idAlbum = in.readInt();
        hasLine = in.readByte() != 0;
        placeHolder = in.readInt();
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getData() {
        return data;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public int getIdAlbum() {
        return idAlbum;
    }

    public int getArtistId() {
        return artistId;
    }

    public int getPlaceHolder() {
        return placeHolder;
    }

    @Override
    public int getTypeModel() {
        return BaseModel.TYPE_SONG;
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(image);
        parcel.writeString(name);
        parcel.writeString(data);
        parcel.writeString(artist);
        parcel.writeInt(artistId);
        parcel.writeString(album);
        parcel.writeInt(idAlbum);
        parcel.writeByte((byte) (hasLine ? 1 : 0));
        parcel.writeInt(placeHolder);
    }
}
