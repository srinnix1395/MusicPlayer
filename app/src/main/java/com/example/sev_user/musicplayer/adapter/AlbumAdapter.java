package com.example.sev_user.musicplayer.adapter;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sev_user.musicplayer.R;
import com.example.sev_user.musicplayer.custom.AlbumDiffCallback;
import com.example.sev_user.musicplayer.model.Album;
import com.example.sev_user.musicplayer.utils.ImageUtils;
import com.example.sev_user.musicplayer.viewholder.AlbumViewHolder;

import java.util.ArrayList;

/**
 * Created by sev_user on 7/15/2016.
 */
public class AlbumAdapter extends RecyclerView.Adapter<AlbumViewHolder> {
    public static final int ASCENDING = 0;
    public static final int DESCENDING = 1;
    public static final int DEFAULT_SORT = ASCENDING;

    private ArrayList<Album> arrayList;
    private int margin;
    private int noMargin;

    public AlbumAdapter(Context context, ArrayList<Album> arrayList) {
        this.arrayList = arrayList;
        margin = (int) ImageUtils.convertDpToPixel(10, context);
        noMargin = (int) ImageUtils.convertDpToPixel(1, context);
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_holder_album, parent, false);
        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlbumViewHolder holder, int position) {
        holder.setupViewHolder(arrayList.get(position), position, margin, noMargin);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void swapItems(ArrayList<Album> newList) {
        final AlbumDiffCallback diffCallback = new AlbumDiffCallback(newList, arrayList);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        arrayList.clear();
        arrayList.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }
}
