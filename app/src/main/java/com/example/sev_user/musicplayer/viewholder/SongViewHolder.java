package com.example.sev_user.musicplayer.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.sev_user.musicplayer.R;
import com.example.sev_user.musicplayer.callback.OnClickViewHolder;
import com.example.sev_user.musicplayer.model.Song;
import com.example.sev_user.musicplayer.utils.ImageUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sev_user on 7/15/2016.
 */
public class SongViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @Bind(R.id.imvCover)
    ImageView imvCover;

    @Bind(R.id.tvName)
    TextView tvName;

    @Bind(R.id.tvInfo)
    TextView tvInfo;

    @Bind(R.id.vline)
    View vline;

    @Bind(R.id.imvMore)
    ImageView imvMore;

    private int position;
    private OnClickViewHolder callback;
    private Song song;

    public SongViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
        callback = (OnClickViewHolder) itemView.getContext();
    }

    public void setupViewHolder(Song song, int position) {
        this.song = song;
        this.position = position;

        Glide.with(itemView.getContext())
                .load(song.getImage())
                .thumbnail(0.1f)
                .placeholder(ImageUtils.randomImage())
                .error(ImageUtils.randomImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imvCover);

        tvName.setText(song.getName());
        tvInfo.setText(song.getArtist() + " | " + song.getAlbum());
        if (song.isHasLine()) {
            vline.setVisibility(View.VISIBLE);
        } else {
            vline.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        callback.onClickSong(song, position);
    }

    @OnClick(R.id.imvMore)
    void onClickMore() {
        PopupMenu popupMenu = new PopupMenu(itemView.getContext(), imvMore);
        popupMenu.getMenuInflater().inflate(R.menu.menu_popup_song, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.miPlay: {
                        callback.onClickSong(song, position);
                        break;
                    }
                }
                return true;
            }
        });
        popupMenu.show();
    }
}
