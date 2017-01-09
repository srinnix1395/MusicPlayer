package com.example.sev_user.musicplayer.adapter;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sev_user.musicplayer.R;
import com.example.sev_user.musicplayer.callback.OnClickViewHolderCallback;
import com.example.sev_user.musicplayer.custom.ArtistDiffCallback;
import com.example.sev_user.musicplayer.model.Artist;
import com.example.sev_user.musicplayer.model.BaseModel;
import com.example.sev_user.musicplayer.model.Header;
import com.example.sev_user.musicplayer.viewholder.ArtistViewHolder;
import com.example.sev_user.musicplayer.viewholder.HeaderViewHolder;

import java.util.ArrayList;

/**
 * Created by sev_user on 7/14/2016.
 */
public class ArtistAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_ARTIST = 0;
    private static final int VIEW_STRING = 1;

    private ArrayList<BaseModel> arrayList;
    private OnClickViewHolderCallback onClickViewHolderCallback;

    public ArtistAdapter(ArrayList<BaseModel> arrayList, OnClickViewHolderCallback onClickViewHolderCallback) {
        this.arrayList = arrayList;
        this.onClickViewHolderCallback = onClickViewHolderCallback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder;
        View view;
        switch (viewType) {
            case VIEW_ARTIST: {
                view = inflater.inflate(R.layout.item_artist, parent, false);
                viewHolder = new ArtistViewHolder(view, onClickViewHolderCallback);
                break;
            }
            case VIEW_STRING: {
                view = inflater.inflate(R.layout.item_header, parent, false);
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
        switch (arrayList.get(position).getTypeModel()) {
            case BaseModel.TYPE_ARTIST: {
                ((ArtistViewHolder) holder).setupViewHolder((Artist) arrayList.get(position));
                break;
            }
            case BaseModel.TYPE_HEADER: {
                ((HeaderViewHolder) holder).setupViewHolder((Header) arrayList.get(position));
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
        if (arrayList.get(position).getTypeModel() == BaseModel.TYPE_ARTIST) {
            return VIEW_ARTIST;
        }
        return VIEW_STRING;
    }

    public void swapItems(ArrayList<BaseModel> newList) {
        final ArtistDiffCallback diffCallback = new ArtistDiffCallback(newList, arrayList);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        arrayList.clear();
        arrayList.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }

    class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

}
