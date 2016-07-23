package com.example.sev_user.musicplayer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sev_user.musicplayer.R;
import com.example.sev_user.musicplayer.constant.Constant;
import com.example.sev_user.musicplayer.model.Song;
import com.example.sev_user.musicplayer.viewholder.HeaderViewHolder;
import com.example.sev_user.musicplayer.viewholder.PlayShuffleViewHolder;
import com.example.sev_user.musicplayer.viewholder.SongViewHolder;

import java.util.ArrayList;

/**
 * Created by sev_user on 7/15/2016.
 */

public class SongAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_SONG = 0;
    private static final int VIEW_STRING = 1;
    private static final int VIEW_PLAY_SHUFFLE = 2;

    private ArrayList<Object> arrayList;

    public SongAdapter(ArrayList<Object> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder;
        View view;
        switch (viewType) {
            case VIEW_SONG: {
                view = inflater.inflate(R.layout.view_holder_song, parent, false);
                viewHolder = new SongViewHolder(view);
                break;
            }
            case VIEW_STRING: {
                view = inflater.inflate(R.layout.view_holder_header, parent, false);
                viewHolder = new HeaderViewHolder(view);
                break;
            }
            case VIEW_PLAY_SHUFFLE: {
                view = inflater.inflate(R.layout.view_holder_play_shuffle, parent, false);
                viewHolder = new PlayShuffleViewHolder(view);
                break;
            }
            default: {
                view = inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
                viewHolder = new EmptyViewHolder(view);
                break;
            }
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (arrayList.get(position) instanceof Song) {
            ((SongViewHolder) holder).setupViewHolder((Song) arrayList.get(position), position);
        } else if (!(arrayList.get(position)).equals(Constant.PLAY_SHUFFLE_ALL)) {
            ((HeaderViewHolder) holder).setupViewHolder((String) arrayList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (arrayList.get(position) instanceof Song) {
            return VIEW_SONG;
        }
        if (arrayList.get(position) instanceof String) {
            if (arrayList.get(position).equals(Constant.PLAY_SHUFFLE_ALL)) {
                return VIEW_PLAY_SHUFFLE;
            }
            return VIEW_STRING;
        }
        return -1;
    }

    class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View view) {
            super(view);
        }
    }
}
