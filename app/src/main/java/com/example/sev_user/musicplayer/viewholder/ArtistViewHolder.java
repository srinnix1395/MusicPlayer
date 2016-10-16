package com.example.sev_user.musicplayer.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.sev_user.musicplayer.R;
import com.example.sev_user.musicplayer.callback.OnClickViewHolder;
import com.example.sev_user.musicplayer.model.Artist;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sev_user on 7/14/2016.
 */
public class ArtistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @Bind(R.id.tvCover)
    TextView tvCover;

    @Bind(R.id.tvName)
    TextView tvName;

    @Bind(R.id.tvInfo)
    TextView tvInfo;

    @Bind(R.id.vline)
    View viewLine;

    @Bind(R.id.imvMore)
    ImageView imvMore;

    private OnClickViewHolder callback;
    private Artist artist;

    public ArtistViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
        callback = (OnClickViewHolder) itemView.getContext();
    }

    public void setupViewHolder(Artist artist) {
        this.artist = artist;
        tvCover.setBackgroundColor(artist.getColorPlaceHolder());
        tvCover.setText(artist.getName().substring(0, 2));
        tvName.setText(artist.getName());
        tvInfo.setText(artist.getInfo());
        viewLine.setVisibility(artist.isHasLine() ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        callback.onClickArtist(artist);
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
                        callback.onClickArtist(artist);
                        break;
                    }
                }
                return true;
            }
        });
        popupMenu.show();
    }
}
