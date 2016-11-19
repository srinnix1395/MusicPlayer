package com.example.sev_user.musicplayer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sev_user.musicplayer.R;
import com.example.sev_user.musicplayer.model.BaseModel;
import com.example.sev_user.musicplayer.model.Header;
import com.example.sev_user.musicplayer.viewholder.EmptyViewHolder;
import com.example.sev_user.musicplayer.viewholder.HeaderViewHolder;
import com.example.sev_user.musicplayer.viewholder.PlayShuffleViewHolder;
import com.example.sev_user.musicplayer.viewholder.SongViewHolder;

import java.util.ArrayList;

/**
 * Created by DELL on 11/19/2016.
 */

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_VIEW_TYPE_ITEM = 0;
    private static final int ITEM_VIEW_TYPE_SHOW_ALL = 1;
    private static final int ITEM_VIEW_TYPE_HEADER = 2;

    private ArrayList<BaseModel> arrayList;

    public SearchAdapter(ArrayList<BaseModel> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder;
        View view;
        switch (viewType) {
            case ITEM_VIEW_TYPE_ITEM: {
                view = inflater.inflate(R.layout.view_holder_song, parent, false);
                viewHolder = new SongViewHolder(view);
                break;
            }
            case ITEM_VIEW_TYPE_HEADER: {
                view = inflater.inflate(R.layout.view_holder_header, parent, false);
                viewHolder = new HeaderViewHolder(view);
                break;
            }
            case ITEM_VIEW_TYPE_SHOW_ALL: {
                view = inflater.inflate(R.layout.view_holder_show_all, parent, false);
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

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        switch (arrayList.get(position).getTypeModel()) {
            case BaseModel.TYPE_SONG:
            case BaseModel.TYPE_ALBUM:
            case BaseModel.TYPE_ARTIST: {
                return ITEM_VIEW_TYPE_ITEM;
            }
            case BaseModel.TYPE_HEADER: {
                if (((Header) arrayList.get(position)).getHeader().contains("show all")) {
                    return ITEM_VIEW_TYPE_SHOW_ALL;
                }
                return ITEM_VIEW_TYPE_HEADER;
            }
            default:
                return 1;
        }
    }
}
