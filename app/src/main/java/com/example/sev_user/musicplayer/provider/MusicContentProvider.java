package com.example.sev_user.musicplayer.provider;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.sev_user.musicplayer.constant.Constant;
import com.example.sev_user.musicplayer.model.Album;
import com.example.sev_user.musicplayer.model.Artist;
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

    private ArrayList<Artist> getArtist() {
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

    public ArrayList<Object> getArrSong() {
        ArrayList<Song> arrSong = getSongHasImage();
        ArrayList<Object> arrObjects = new ArrayList<>();

        if (arrSong.size() > 0) {
            arrObjects.add(Constant.PLAY_SHUFFLE_ALL);
        }

        Comparator<Song> comparator = new Comparator<Song>() {
            @Override
            public int compare(Song song, Song t1) {
                return song.getName().compareToIgnoreCase(t1.getName());
            }
        };
        Collections.sort(arrSong, comparator);

        if (!Character.isLetter(arrSong.get(0).getName().charAt(0))) {
            arrObjects.add("#");
        }

        int positionLetter = 0;

        for (int i = 0, size = arrSong.size(); i < size; i++) {
            if (!Character.isLetter(arrSong.get(i).getName().charAt(0))) {
                arrObjects.add(arrSong.get(i));
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
        String lastLetter;
        for (int i = positionLetter, size = arrSong.size(); i < size; i++) {
            currentLetter = String.valueOf(arrSong.get(i).getName().charAt(0));
            lastLetter = String.valueOf(arrSong.get(i - 1).getName().charAt(0));
            if (!currentLetter.equalsIgnoreCase(lastLetter)) {
                ((Song) arrObjects.get(arrObjects.size() - 1)).setHasLine(false);
                arrObjects.add(currentLetter);
            }
            arrObjects.add(arrSong.get(i));
        }

        return arrObjects;
    }

    public ArrayList<Object> getArrArtist() {
        ArrayList<Artist> arrArtist = getArtist();
        ArrayList<Object> arrObjects = new ArrayList<>();

        Comparator<Artist> comparator = new Comparator<Artist>() {
            @Override
            public int compare(Artist artist, Artist t1) {
                return artist.getName().compareToIgnoreCase(t1.getName());
            }
        };
        Collections.sort(arrArtist, comparator);

        if (!Character.isLetter(arrArtist.get(0).getName().charAt(0))) {
            arrObjects.add("#");
        }

        int positionLetter = 0;

        for (int i = 0, size = arrArtist.size(); i < size; i++) {
            if (!Character.isLetter(arrArtist.get(i).getName().charAt(0))) {
                arrObjects.add(arrArtist.get(i));
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
        for (int i = positionLetter, size = arrArtist.size(); i < size; i++) {
            currentLetter = String.valueOf(arrArtist.get(i).getName().charAt(0));
            lastLetter = String.valueOf(arrArtist.get(i - 1).getName().charAt(0));
            if (!currentLetter.equalsIgnoreCase(lastLetter)) {
                ((Artist) arrObjects.get(arrObjects.size() - 1)).setHasLine(false);
                arrObjects.add(currentLetter);
            }
            arrObjects.add(arrArtist.get(i));
        }
        return arrObjects;
    }

    private ArrayList<Song> getSongHasImage() {
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

