package com.example.sev_user.musicplayer.model;

/**
 * Created by sev_user on 7/15/2016.
 */
public class Song extends BaseModel{
    private int id;
    private String image;
    private String name;
    private String data;
    private String artist;
    private int artistId;
    private String album;
    private int idAlbum;
    private boolean hasLine;
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

    public boolean isHasLine() {
        return hasLine;
    }

    public void setHasLine(boolean hasLine) {
        this.hasLine = hasLine;
    }

    public int getIdAlbum() {
        return idAlbum;
    }

    public void setIdAlbum(int idAlbum) {
        this.idAlbum = idAlbum;
    }

    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    public int getPlaceHolder() {
        return placeHolder;
    }

    @Override
    public int getTypeModel() {
        return BaseModel.TYPE_SONG;
    }
}
