package com.example.sev_user.musicplayer.viewholder;

import android.support.v4.content.ContextCompat;
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
import com.example.sev_user.musicplayer.model.Album;
import com.example.sev_user.musicplayer.utils.ImageUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sev_user on 7/15/2016.
 */
public class AlbumViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private View view;

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
        view = itemView;
        ButterKnife.bind(this, view);
        callback = (OnClickViewHolder) view.getContext();
    }

    public void setupViewHolder(Album album, int position) {
        this.album = album;
        this.position = position;
        if (album.getImage() == null) {
            imvAlbumArt.setImageResource(R.drawable.ic_note);
            imvAlbumArt.setScaleType(ImageView.ScaleType.FIT_CENTER);
            int dp = (int) ImageUtils.convertDpToPixel(25f, view.getContext());
            imvAlbumArt.setPadding(dp, dp, dp, dp);
            imvAlbumArt.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.colorAccent));
        } else {
            imvAlbumArt.setScaleType(ImageView.ScaleType.FIT_XY);
            imvAlbumArt.setPadding(0, 0, 0, 0);
            imvAlbumArt.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.colorWhiteFade));
            Glide.with(view.getContext())
                    .load(album.getImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .crossFade()
                    .into(imvAlbumArt);
        }
        tvAlbumName.setText(album.getAlbumName());
        tvArtistName.setText(album.getArtistName());
        view.setOnClickListener(this);
    }

    @OnClick(R.id.imvMore)
    void onClickImvMore() {

    }

    @Override
    public void onClick(View v) {
        callback.onClickAlbum(album, position);
    }

    @OnClick(R.id.imvMore)
    void onClickMore() {
        final PopupMenu popupMenu = new PopupMenu(view.getContext(), imvMore);
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
