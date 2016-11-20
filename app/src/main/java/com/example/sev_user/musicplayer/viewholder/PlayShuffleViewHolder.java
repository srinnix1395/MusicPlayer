package com.example.sev_user.musicplayer.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.sev_user.musicplayer.callback.OnClickShuffleCallback;

/**
 * Created by sev_user on 7/20/2016.
 */
public class PlayShuffleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private OnClickShuffleCallback callback;

    public PlayShuffleViewHolder(View itemView, OnClickShuffleCallback callback) {
        super(itemView);
        itemView.setOnClickListener(this);
        this.callback = callback;
    }

    @Override
    public void onClick(View v) {
        callback.onClickPlayShuffle();
    }
}
