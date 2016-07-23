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

            arrayList.add(new Artist(id, name, numberOfAlbum + " album | " + numberOfTrack + " bài hát",color, true));
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
        ArrayList<Object> arrLetter = new ArrayList<>();
        ArrayList<Object> arrNumber = new ArrayList<>();
        ArrayList<String> arrHeader = new ArrayList<>();

        arrNumber.add(Constant.PLAY_SHUFFLE_ALL);
        arrNumber.add("#");
        String firstLetter = "";

        for (int i = 0; i < arrSong.size(); i++) {
            firstLetter = arrSong.get(i).getName().substring(0, 1);
            if (Character.isLetter(firstLetter.charAt(0))) {
                arrLetter.add(arrSong.get(i));
            } else {
                arrNumber.add(arrSong.get(i));
            }
        }

        Comparator<Object> comparator = new Comparator<Object>() {
            @Override
            public int compare(Object lhs, Object rhs) {
                if (((Song) lhs).getName().compareTo(((Song) rhs).getName()) > 0) {
                    return 1;
                }
                if (((Song) lhs).getName().compareTo(((Song) rhs).getName()) < 0) {
                    return -1;
                }
                return 0;
            }
        };
        Collections.sort(arrLetter, comparator);

        for (Song temp : arrSong) {
            firstLetter = temp.getName().substring(0, 1);
            if (!arrHeader.contains(firstLetter) && Character.isLetter(firstLetter.charAt(0))) {
                arrHeader.add(firstLetter);
            }
        }

        Comparator<String> comparatorHeader = new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                if (lhs.compareTo(rhs) > 0) {
                    return 1;
                }
                if (lhs.compareTo(rhs) < 0) {
                    return -1;
                }
                return 0;
            }
        };
        Collections.sort(arrHeader, comparatorHeader);

        for (String s : arrHeader) {
            arrObjects.add(s);
            for (Song temp : arrSong) {
                if (temp.getName().substring(0, 1).equals(s)) {
                    arrObjects.add(temp);
                }
            }
            ((Song) arrObjects.get(arrObjects.size() - 1)).setHasLine(false);
        }

        if (arrNumber.size() > 2) {
            ((Song) arrNumber.get(arrNumber.size() - 1)).setHasLine(false);
            arrNumber.addAll(arrObjects);
            return arrNumber;
        }

        arrObjects.add(Constant.PLAY_SHUFFLE_ALL);
        return arrObjects;
    }

    public ArrayList<Object> getArrArtist() {
        ArrayList<Artist> arrArtist = getArtist();
        ArrayList<Object> arrObjects = new ArrayList<>();
        ArrayList<Object> arrLetter = new ArrayList<>();
        ArrayList<Object> arrNumber = new ArrayList<>();
        ArrayList<String> arrHeader = new ArrayList<>();

        arrNumber.add("#");
        String firstLetter = "";

        for (int i = 0; i < arrArtist.size(); i++) {
            firstLetter = arrArtist.get(i).getName().substring(0, 1);
            if (Character.isLetter(firstLetter.charAt(0))) {

                arrLetter.add(arrArtist.get(i));
            } else if (!firstLetter.substring(0, 1).equals("<")) {
                arrNumber.add(arrArtist.get(i));
            }
        }

        Comparator<Object> comparatorObject = new Comparator<Object>() {
            @Override
            public int compare(Object lhs, Object rhs) {
                if (((Artist) lhs).getName().compareTo(((Artist) rhs).getName()) > 0) {
                    return 1;
                }
                if (((Artist) lhs).getName().compareTo(((Artist) rhs).getName()) < 0) {
                    return -1;
                }
                return 0;
            }
        };
        Collections.sort(arrLetter, comparatorObject);

        for (Artist temp : arrArtist) {
            firstLetter = temp.getName().substring(0, 1);
            if (!arrHeader.contains(firstLetter) && Character.isLetter(firstLetter.charAt(0))) {
                arrHeader.add(firstLetter);
            }
        }

        Comparator<String> comparatorHeader = new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                if (lhs.compareTo(rhs) > 0) {
                    return 1;
                }
                if (lhs.compareTo(rhs) < 0) {
                    return -1;
                }
                return 0;
            }
        };
        Collections.sort(arrHeader, comparatorHeader);

        for (String s : arrHeader) {
            arrObjects.add(s);
            for (Artist temp : arrArtist) {
                if (temp.getName().substring(0, 1).equals(s)) {
                    arrObjects.add(temp);
                }
            }
            ((Artist) arrObjects.get(arrObjects.size() - 1)).setHasLine(false);
        }

        if (arrNumber.size() > 1) {
            ((Artist) arrNumber.get(arrNumber.size() - 1)).setHasLine(false);
            arrNumber.addAll(arrObjects);
            return arrNumber;
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

