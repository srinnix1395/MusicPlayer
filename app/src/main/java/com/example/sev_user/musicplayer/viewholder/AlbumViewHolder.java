package com.example.sev_user.musicplayer.viewholder;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sev_user.musicplayer.R;
import com.example.sev_user.musicplayer.callback.OnClickViewHolder;
import com.example.sev_user.musicplayer.model.Album;
import com.example.sev_user.musicplayer.utils.ImageUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sev_user on 7/15/2016.
 */
public class AlbumViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @Bind(R.id.imvAlbumArt)
    ImageView imvAlbumArt;

    @Bind(R.id.tvAlbumName)
    TextView tvAlbumName;

    @Bind(R.id.tvArtistName)
    TextView tvArtistName;

    @Bind(R.id.imvMore)
    ImageView imvMore;

    private OnClickViewHolder callback;
    private Album album;
    private int position;

    public AlbumViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        callback = (OnClickViewHolder) itemView.getContext();
    }

    public void setupViewHolder(Album album, int position, int margin, int noMargin) {
        this.album = album;
        this.position = position;

        GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) itemView.getLayoutParams();

        if (position % 2 == 0) {
            layoutParams.leftMargin = margin;
            layoutParams.rightMargin = noMargin;
        } else {
            layoutParams.leftMargin = noMargin;
            layoutParams.rightMargin = margin;
        }
        itemView.requestLayout();

        Glide.with(itemView.getContext())
                .load(album.getImage())
                .crossFade()
                .placeholder(ImageUtils.randomImage())
                .error(ImageUtils.randomImage())
                .into(imvAlbumArt);

        tvAlbumName.setText(album.getAlbumName());
        tvArtistName.setText(album.getArtistName());
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        callback.onClickAlbum(album, position);
    }

    @OnClick(R.id.imvMore)
    void onClickMore() {
        final PopupMenu popupMenu = new PopupMenu(itemView.getContext(), imvMore);
        popupMenu.getMenuInflater().inflate(R.menu.menu_popup_album_artist, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.miDetail: {
                        callback.onClickAlbum(album, position);
                        break;
                    }
                }
                return true;
            }
        });
        popupMenu.show();
    }
}
