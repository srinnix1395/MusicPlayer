package com.example.sev_user.musicplayer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sev_user.musicplayer.R;
import com.example.sev_user.musicplayer.model.Artist;
import com.example.sev_user.musicplayer.viewholder.ArtistViewHolder;
import com.example.sev_user.musicplayer.viewholder.HeaderViewHolder;

import java.util.ArrayList;

/**
 * Created by sev_user on 7/14/2016.
 */
public class ArtistAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_ARTIST = 0;
    private static final int VIEW_STRING = 1;

    private ArrayList<Object> arrayList;

    public ArtistAdapter(ArrayList<Object> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder;
        View view;
        switch (viewType) {
            case VIEW_ARTIST: {
                view = inflater.inflate(R.layout.view_holder_artist, parent, false);
                viewHolder = new ArtistViewHolder(view);
                break;
            }
            case VIEW_STRING: {
                view = inflater.inflate(R.layout.view_holder_header, parent, false);
                viewHolder = new HeaderViewHolder(view);
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
        if (arrayList.get(position) instanceof Artist) {
            ((ArtistViewHolder) holder).setupViewHolder((Artist) arrayList.get(position));
        } else if (arrayList.get(position) instanceof String) {
            ((HeaderViewHolder) holder).setupViewHolder((String) arrayList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (arrayList.get(position) instanceof Artist) {
            return VIEW_ARTIST;
        }
        if (arrayList.get(position) instanceof String) {
            return VIEW_STRING;
        }
        return -1;
    }

    class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

}
