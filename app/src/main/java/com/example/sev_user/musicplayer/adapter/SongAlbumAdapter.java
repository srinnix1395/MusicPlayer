package com.example.sev_user.musicplayer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sev_user.musicplayer.R;
import com.example.sev_user.musicplayer.callback.OnClickViewHolderCallback;
import com.example.sev_user.musicplayer.model.SongPlus;
import com.example.sev_user.musicplayer.viewholder.SongAlbumViewHolder;

import java.util.ArrayList;

/**
 * Created by sev_user on 7/21/2016.
 */
public class SongAlbumAdapter extends RecyclerView.Adapter<SongAlbumViewHolder> {
    private ArrayList<SongPlus> arrayList;
    private OnClickViewHolderCallback callback;

    public SongAlbumAdapter(ArrayList<SongPlus> arrayList, OnClickViewHolderCallback callback) {
        this.arrayList = arrayList;
        this.callback = callback;
    }

    @Override
    public SongAlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_song_album, parent, false);
        return new SongAlbumViewHolder(view, callback);
    }

    @Override
    public void onBindViewHolder(SongAlbumViewHolder holder, int position) {
        holder.setupViewHolder(arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
