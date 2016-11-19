package com.example.sev_user.musicplayer.model;

/**
 * Created by sev_user on 7/15/2016.
 */
public class Album extends BaseModel{
    private int id;
    private String image;
    private String albumName;
    private String artistName;
    private int numberOfSong;
    private int firstYear;

    public Album(int id, String image, String albumName, String artistName, int numberOfSong, int firstYear) {
        this.id = id;
        this.image = image;
        this.albumName = albumName;
        this.artistName = artistName;
        this.numberOfSong = numberOfSong;
        this.firstYear = firstYear;
    }

    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getArtistName() {
        return artistName;
    }

    public int getNumberOfSong() {
        return numberOfSong;
    }

    public int getFirstYear() {
        return firstYear;
    }

    @Override
    public int getTypeModel() {
        return BaseModel.TYPE_ALBUM;
    }
}
