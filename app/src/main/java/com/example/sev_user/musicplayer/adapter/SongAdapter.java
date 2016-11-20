package com.example.sev_user.musicplayer.adapter;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sev_user.musicplayer.R;
import com.example.sev_user.musicplayer.callback.OnClickShuffleCallback;
import com.example.sev_user.musicplayer.callback.OnClickViewHolderCallback;
import com.example.sev_user.musicplayer.constant.Constant;
import com.example.sev_user.musicplayer.custom.SongDiffCallback;
import com.example.sev_user.musicplayer.model.BaseModel;
import com.example.sev_user.musicplayer.model.Header;
import com.example.sev_user.musicplayer.model.Song;
import com.example.sev_user.musicplayer.viewholder.EmptyViewHolder;
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

    private ArrayList<BaseModel> arrayList;
    private OnClickViewHolderCallback onClickViewHolderCallback;
    private OnClickShuffleCallback onClickShuffleCallback;

    public SongAdapter(ArrayList<BaseModel> arrayList, OnClickViewHolderCallback onClickViewHolderCallback
            , OnClickShuffleCallback onClickShuffleCallback) {
        this.arrayList = arrayList;
        this.onClickViewHolderCallback = onClickViewHolderCallback;
        this.onClickShuffleCallback = onClickShuffleCallback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder;
        View view;
        switch (viewType) {
            case VIEW_SONG: {
                view = inflater.inflate(R.layout.view_holder_song, parent, false);
                viewHolder = new SongViewHolder(view, onClickViewHolderCallback);
                break;
            }
            case VIEW_STRING: {
                view = inflater.inflate(R.layout.view_holder_header, parent, false);
                viewHolder = new HeaderViewHolder(view);
                break;
            }
            case VIEW_PLAY_SHUFFLE: {
                view = inflater.inflate(R.layout.view_holder_play_shuffle, parent, false);
                viewHolder = new PlayShuffleViewHolder(view, onClickShuffleCallback);
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
        switch (arrayList.get(position).getTypeModel()) {
            case BaseModel.TYPE_SONG: {
                ((SongViewHolder) holder).setupViewHolder((Song) arrayList.get(position), position);
                break;
            }
            case BaseModel.TYPE_HEADER: {
                if (!((Header) arrayList.get(position)).getHeader().equals(Constant.PLAY_SHUFFLE_ALL)) {
                    ((HeaderViewHolder) holder).setupViewHolder((Header) arrayList.get(position));
                }
                break;
            }
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (arrayList.get(position).getTypeModel() == BaseModel.TYPE_SONG) {
            return VIEW_SONG;
        }
        if (arrayList.get(position).getTypeModel() == BaseModel.TYPE_HEADER
                && ((Header) arrayList.get(position)).getHeader().equals(Constant.PLAY_SHUFFLE_ALL)) {
            return VIEW_PLAY_SHUFFLE;
        }
        return VIEW_STRING;
    }

    public void swapItems(ArrayList<BaseModel> newList) {
        final SongDiffCallback diffCallback = new SongDiffCallback(newList, arrayList);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        arrayList.clear();
        arrayList.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }
}
