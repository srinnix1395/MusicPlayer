package com.example.sev_user.musicplayer.custom;

import android.support.v7.util.DiffUtil;

import com.example.sev_user.musicplayer.model.Song;

import java.util.ArrayList;

/**
 * Created by DELL on 11/17/2016.
 */

public class SongDiffCallback extends DiffUtil.Callback {
    private ArrayList<Object> newList;
    private ArrayList<Object> oldList;

    public SongDiffCallback(ArrayList<Object> newList, ArrayList<Object> oldList) {
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
        Object oldObj = oldList.get(oldItemPosition);
        Object newObj = newList.get(newItemPosition);

        if (oldObj.getClass().equals(newObj.getClass())) {
            if (oldObj instanceof Song) {
                return (((Song) oldObj).getId() == ((Song) newObj).getId());
            }

            return oldObj.equals(newObj);
        } else {
            return false;
        }
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Object oldObj = oldList.get(oldItemPosition);
        Object newObj = newList.get(newItemPosition);

        if (oldObj.getClass().equals(newObj.getClass())) {
            if (oldObj instanceof Song) {
                Song newSong = (Song) newObj;
                Song oldSong = (Song) oldObj;

                return newSong.getName().equals(oldSong.getName()) && newSong.getAlbum().equals(oldSong.getAlbum())
                        && newSong.getData().equals(oldSong.getData()) && newSong.getArtistId() == oldSong.getArtistId();
            }

            return oldObj.equals(newObj);
        } else {
            return false;
        }
    }
}
