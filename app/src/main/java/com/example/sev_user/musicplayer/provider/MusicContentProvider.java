package com.example.sev_user.musicplayer.provider;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.sev_user.musicplayer.constant.Constant;
import com.example.sev_user.musicplayer.model.Album;
import com.example.sev_user.musicplayer.model.Artist;
import com.example.sev_user.musicplayer.model.BaseModel;
import com.example.sev_user.musicplayer.model.Header;
import com.example.sev_user.musicplayer.model.Song;
import com.example.sev_user.musicplayer.utils.ImageUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by sev_user on 7/14/2016.
 */
public class MusicContentProvider {
    private ContentResolver contentResolver;

    public MusicContentProvider(Context context) {
        contentResolver = context.getContentResolver();
    }

    public ArrayList<Artist> getArtist() {
        Cursor cursor = contentResolver.query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI, Constant.COLUMN_ARTIST, null, null, null);
        ArrayList<Artist> arrayList = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            int numberOfTrack = cursor.getInt(2);
            int numberOfAlbum = cursor.getInt(3);
            int color = ImageUtils.randomColor();

            arrayList.add(new Artist(id, name, numberOfAlbum + " album | " + numberOfTrack + " bài hát", color, true));
        }
        cursor.close();
        return arrayList;
    }

    public ArrayList<Album> getAlbums() {
        ArrayList<Album> albums = new ArrayList<>();
        Cursor cursor = contentResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, Constant.COLUMN_ALBUM, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String image = cursor.getString(1);
                String albumName = cursor.getString(2);
                String artistName = cursor.getString(3);
                int numberOfSong = cursor.getInt(4);
                int firstYear = cursor.getInt(5);

                albums.add(new Album(id, image, albumName, artistName, numberOfSong, firstYear));
            }
            cursor.close();
        }

        Comparator<Album> comparator = new Comparator<Album>() {
            @Override
            public int compare(Album album, Album t1) {
                return album.getAlbumName().compareToIgnoreCase(t1.getAlbumName());
            }
        };
        Collections.sort(albums, comparator);
        return albums;
    }


    private ArrayList<Song> getSongs() {
        ArrayList<Song> arrSong = new ArrayList<>();
        Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, Constant.COLUMN_SONG, MediaStore.Audio.Media.IS_MUSIC + "= 1", null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(1);
            String name = cursor.getString(2);
            String data = cursor.getString(3);
            int artistId = cursor.getInt(4);
            String artist = cursor.getString(5);
            int albumId = cursor.getInt(6);
            String album = cursor.getString(7);

            arrSong.add(new Song(id, "", name, data, artist, artistId, album, albumId, true, ImageUtils.randomImage()));
        }
        cursor.close();
        return arrSong;
    }

    public ArrayList<BaseModel> getArrSong(ArrayList<Song> songs) {
        ArrayList<BaseModel> arrObjects = new ArrayList<>();

        if (songs.size() > 0) {
            arrObjects.add(new Header(Constant.PLAY_SHUFFLE_ALL));
        }

        Comparator<Song> comparator = new Comparator<Song>() {
            @Override
            public int compare(Song song, Song t1) {
                return song.getName().compareToIgnoreCase(t1.getName());
            }
        };
        Collections.sort(songs, comparator);

        if (!Character.isLetter(songs.get(0).getName().charAt(0))) {
            arrObjects.add(new Header("#"));
        }

        int positionLetter = 0;

        for (int i = 0, size = songs.size(); i < size; i++) {
            if (!Character.isLetter(songs.get(i).getName().charAt(0))) {
                arrObjects.add(songs.get(i));
                positionLetter = i;
            } else {
                ((Song) arrObjects.get(arrObjects.size() - 1)).setHasLine(false);
                if (positionLetter != 0) {
                    positionLetter++;
                }
                break;
            }
        }

        String currentLetter;
        String prevLetter;
        for (int i = positionLetter, size = songs.size(); i < size; i++) {
            currentLetter = String.valueOf(songs.get(i).getName().charAt(0));
            prevLetter = String.valueOf(songs.get(i - 1).getName().charAt(0));
            if (!currentLetter.equalsIgnoreCase(prevLetter)) {
                ((Song) arrObjects.get(arrObjects.size() - 1)).setHasLine(false);
                arrObjects.add(new Header(currentLetter));
            }
            arrObjects.add(songs.get(i));
        }

        if (arrObjects.size() > 0) {
            ((Song) arrObjects.get(arrObjects.size() - 1)).setHasLine(false);
        }

        return arrObjects;
    }

    public ArrayList<BaseModel> getArrArtist(ArrayList<Artist> artists) {
        ArrayList<BaseModel> arrObjects = new ArrayList<>();

        Comparator<Artist> comparator = new Comparator<Artist>() {
            @Override
            public int compare(Artist artist, Artist t1) {
                return artist.getName().compareToIgnoreCase(t1.getName());
            }
        };
        Collections.sort(artists, comparator);

        if (!Character.isLetter(artists.get(0).getName().charAt(0))) {
            arrObjects.add(new Header("#"));
        }

        int positionLetter = 0;

        for (int i = 0, size = artists.size(); i < size; i++) {
            if (!Character.isLetter(artists.get(i).getName().charAt(0))) {
                arrObjects.add(artists.get(i));
                positionLetter = i;
            } else {
                ((Artist) arrObjects.get(arrObjects.size() - 1)).setHasLine(false);
                if (positionLetter != 0) {
                    positionLetter++;
                }
                break;
            }
        }

        String currentLetter;
        String lastLetter;
        for (int i = positionLetter, size = artists.size(); i < size; i++) {
            currentLetter = String.valueOf(artists.get(i).getName().charAt(0));
            lastLetter = String.valueOf(artists.get(i - 1).getName().charAt(0));
            if (!currentLetter.equalsIgnoreCase(lastLetter)) {
                ((Artist) arrObjects.get(arrObjects.size() - 1)).setHasLine(false);
                arrObjects.add(new Header(currentLetter));
            }
            arrObjects.add(artists.get(i));
        }

        if (arrObjects.size() > 0) {
            ((Artist) arrObjects.get(arrObjects.size() - 1)).setHasLine(false);
        }
        return arrObjects;
    }

    public ArrayList<Song> getSongHasImage() {
        ArrayList<Song> arrSong = getSongs();
        String[] columns = {
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM_ART
        };
        Cursor cursor = contentResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, columns, null, null, null);
        while (cursor.moveToNext()) {
            for (Song song : arrSong) {
                if (song.getIdAlbum() == cursor.getInt(0)) {
                    song.setImage(cursor.getString(1));
                }
            }
        }
        cursor.close();
        return arrSong;
    }
}

