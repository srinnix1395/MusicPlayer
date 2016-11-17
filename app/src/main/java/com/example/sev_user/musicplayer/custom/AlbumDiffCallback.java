package com.example.sev_user.musicplayer.custom;

import android.support.v7.util.DiffUtil;

import com.example.sev_user.musicplayer.model.Album;

import java.util.ArrayList;

/**
 * Created by DELL on 11/17/2016.
 */

public class AlbumDiffCallback extends DiffUtil.Callback {
    private ArrayList<Album> newList;
    private ArrayList<Album> oldList;

    public AlbumDiffCallback(ArrayList<Album> newList, ArrayList<Album> oldList) {
        this.newList = newList;
        this.oldList = oldList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Album newAlbum = newList.get(newItemPosition);
        Album oldAlbum = oldList.get(oldItemPosition);

        return newAlbum.getAlbumName().equals(oldAlbum.getAlbumName()) && newAlbum.getArtistName().equals(oldAlbum.getArtistName());
    }
}
