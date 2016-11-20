package com.example.sev_user.musicplayer.callback;

import com.example.sev_user.musicplayer.model.Album;
import com.example.sev_user.musicplayer.model.Artist;
import com.example.sev_user.musicplayer.model.Song;

/**
 * Created by sev_user on 7/18/2016.
 */
public interface OnClickViewHolderCallback {
    void onClickSong(Song song, int position);

    void onClickAlbum(Album album, int position);

    void onClickArtist(Artist artist);
}
