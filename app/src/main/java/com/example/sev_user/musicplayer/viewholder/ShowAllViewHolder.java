package com.example.sev_user.musicplayer.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.sev_user.musicplayer.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by DELL on 11/19/2016.
 */

public class ShowAllViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.tvShowAll)
    TextView tvShowAll;

    public ShowAllViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setupData(String type) {
        tvShowAll.setText(String.format("show all %s", type));
    }
}
