package com.example.sev_user.musicplayer.constant;

import android.provider.MediaStore;

/**
 * Created by sev_user on 7/18/2016.
 */
public class Constant {
    public static final String[] COLUMN_ARTIST = {
            MediaStore.Audio.Artists._ID,
            MediaStore.Audio.Artists.ARTIST,
            MediaStore.Audio.Artists.NUMBER_OF_TRACKS,
            MediaStore.Audio.Artists.NUMBER_OF_ALBUMS
    };

    public static final String[] COLUMN_ALBUM = {
            MediaStore.Audio.Albums._ID,
            MediaStore.Audio.Albums.ALBUM_ART,
            MediaStore.Audio.Albums.ALBUM,
            MediaStore.Audio.Albums.ARTIST,
            MediaStore.Audio.Albums.NUMBER_OF_SONGS,
            MediaStore.Audio.Albums.LAST_YEAR
    };

    public static final String[] COLUMN_SONG = {
            MediaStore.Audio.Media.IS_MUSIC,
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ARTIST_ID,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ALBUM,
    };

    public static final int _ID_FOREGROUND_SERVICE = 1012;
    public static final String ACTION_PLAY_MUSIC = "action_play";
    public static final String ACTION_NEXT_MUSIC = "action_next";
    public static final String ACTION_PREV_MUSIC = "action_prev";
    public static final String ACTION_CLOSE = "action_close";
    public static final String ACTION_NEW_TASK = "action_new_task";

    public static final String PLAY_SHUFFLE_ALL = "Phát ngẫu nhiên tất cả";

    public static final int LEVEL_LOOPING_NONE = 0;
    public static final int LEVEL_LOOPING_ONE = 1;
    public static final int LEVEL_LOOPING_ALL = 2;

    public static final int WHAT_UPDATE_UI = 3;
    public static final int WHAT_CLOSE_SERVICE = 4;
    public static final int WHAT_NEW_TASK = 5;
    public static final String MUSIC_PLAYER_APP = "Music_player_app";
    public static final String IS_PLAYING_SERVICE = "is_playing_service";
    public static final String ACTION_CLICK_SONG = "action_click_song";
    public static final String ACTION_PLAY_SHUFFLE = "action_play_shuffle";
    public static final java.lang.String ALBUM_ID = "album_id";
    public static final String ARRAY_SONG_PLUS = "array_song_plus";
    public static final String ALBUM_NAME = "album_name";
    public static final java.lang.String ALBUM_COVER = "album_cover";
    public static final String ALBUM_INFO = "album_info";
    public static final String ALBUM_ARTIST = "album_artist";
    public static final String ARTIST = "artist";
    public static final String SETTING_SHAKE_TO_SHUFFLE = "shake_shuffle";
    public static final String ARRAY_SONG = "array_song";
    public static final String ARRAY_ALBUM = "array_album";
    public static final String ARRAY_ARTIST = "array_artist";
    public static final String ARRAY = "array";
    public static final String QUERY = "query";
    public static final String TYPE = "type";
    public static final String MAP_RESULT = "map_result";
}
