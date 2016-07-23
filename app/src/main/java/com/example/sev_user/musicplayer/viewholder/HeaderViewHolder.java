package com.example.sev_user.musicplayer.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.sev_user.musicplayer.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sev_user on 7/14/2016.
 */
public class HeaderViewHolder extends RecyclerView.ViewHolder {
    private View view;

    @Bind(R.id.tvHeader)
    TextView tvHeader;

    public HeaderViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        ButterKnife.bind(this, view);
    }

    public void setupViewHolder(String s) {
        tvHeader.setText(s);
    }
}
