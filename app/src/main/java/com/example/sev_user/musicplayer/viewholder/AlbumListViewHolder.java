package com.example.sev_user.musicplayer.viewholder;

import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.bumptech.glide.Glide;
import com.example.sev_user.musicplayer.R;
import com.example.sev_user.musicplayer.callback.OnClickViewHolderCallback;
import com.example.sev_user.musicplayer.model.Album;

/**
 * Created by DELL on 11/19/2016.
 */

public class AlbumListViewHolder extends SongViewHolder {

    private Album album;

    public AlbumListViewHolder(View itemView, OnClickViewHolderCallback callback) {
        super(itemView, callback);
    }

    public void setupViewHolder(Album album, int position) {
        this.album = album;
        this.position = position;
		
        Glide.with(itemView.getContext())
                .load(album.getImage())
                .thumbnail(0.1f)
                .placeholder(album.getPlaceHolder())
                .error(album.getPlaceHolder())
                .into(imvCover);

        tvName.setText(album.getAlbumName());
        tvInfo.setText(album.getArtistName());
        if (album.isHasLine()) {
            vline.setVisibility(View.VISIBLE);
        } else {
            vline.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        callback.onClickAlbum(album, position);
    }

    @Override
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
