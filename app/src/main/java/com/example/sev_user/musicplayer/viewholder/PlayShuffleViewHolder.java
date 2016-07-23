package com.example.sev_user.musicplayer.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.sev_user.musicplayer.callback.OnClickViewHolder;

/**
 * Created by sev_user on 7/20/2016.
 */
public class PlayShuffleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private OnClickViewHolder callback;

    public PlayShuffleViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        callback = (OnClickViewHolder) itemView.getContext();
    }

    @Override
    public void onClick(View v) {
        callback.onClickPlayShuffle();
    }
}
