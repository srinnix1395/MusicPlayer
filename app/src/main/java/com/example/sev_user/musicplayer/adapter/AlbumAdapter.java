package com.example.sev_user.musicplayer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sev_user.musicplayer.R;
import com.example.sev_user.musicplayer.model.Album;
import com.example.sev_user.musicplayer.viewholder.AlbumViewHolder;

import java.util.ArrayList;

/**
 * Created by sev_user on 7/15/2016.
 */
public class AlbumAdapter extends RecyclerView.Adapter<AlbumViewHolder> {
    private ArrayList<Album> arrayList;

    public AlbumAdapter(ArrayList<Album> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_holder_album, parent, false);
        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlbumViewHolder holder, int position) {
        holder.setupViewHolder(arrayList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
