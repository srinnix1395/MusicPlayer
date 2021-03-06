package com.example.sev_user.musicplayer.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.sev_user.musicplayer.R;
import com.example.sev_user.musicplayer.model.Header;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sev_user on 7/14/2016.
 */
public class HeaderViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.tvHeader)
    TextView tvHeader;

    public HeaderViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setupViewHolder(Header header) {
        tvHeader.setText(header.getHeader());
    }
}
