package com.example.sev_user.musicplayer.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.sev_user.musicplayer.R;
import com.example.sev_user.musicplayer.callback.OnClickViewHolder;
import com.example.sev_user.musicplayer.model.SongPlus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sev_user on 7/21/2016.
 */
public class SongAlbumViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final View view;
    @Bind(R.id.imvMore)
    ImageView imvMore;

    @Bind(R.id.tvName)
    TextView tvName;

    @Bind(R.id.tvInfo)
    TextView tvInfo;

    private SongPlus song;
    private OnClickViewHolder callback;

    public SongAlbumViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        ButterKnife.bind(this, view);
        callback = (OnClickViewHolder) view.getContext();
        view.setOnClickListener(this);
    }

    public void setupViewHolder(SongPlus song) {
        this.song = song;
        tvName.setText(song.getSong().getName());
        tvInfo.setText(song.getSong().getArtist() + " | " + song.getSong().getAlbum());
    }

    @Override
    public void onClick(View v) {
        callback.onClickSongAlbum(song);
    }

    @OnClick(R.id.imvMore)
    void onClickMore() {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), imvMore);
        popupMenu.getMenuInflater().inflate(R.menu.menu_popup_song, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.miPlay: {
                        callback.onClickSong(song.getSong(), song.getPosition());
                        break;
                    }
                }
                return true;
            }
        });
        popupMenu.show();
    }
}
