package com.example.sev_user.musicplayer.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.sev_user.musicplayer.R;
import com.example.sev_user.musicplayer.activity.MainActivity;
import com.example.sev_user.musicplayer.adapter.SongAlbumAdapter;
import com.example.sev_user.musicplayer.constant.Constant;
import com.example.sev_user.musicplayer.model.Song;
import com.example.sev_user.musicplayer.model.SongPlus;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sev_user on 7/21/2016.
 */
public class DetailAlbumFragment extends Fragment {
    private static final String TAG = "DetailAlbumFragment";
    private View view;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.imvCover)
    ImageView imvCover;

    @Bind(R.id.tvName)
    TextView tvName;

    @Bind(R.id.tvInfo)
    TextView tvInfo;

    @Bind(R.id.tvArtist)
    TextView tvArtist;

    private ArrayList<SongPlus> arrayList;
    private SongAlbumAdapter albumAdapter;
    private String albumName;
    private String albumCover;
    private String info;
    private String artist;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail_album, container, false);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        this.albumName = bundle.getString(Constant.ALBUM_NAME);
        this.albumCover = bundle.getString(Constant.ALBUM_COVER);
        this.info = bundle.getString(Constant.ALBUM_INFO);
        this.artist = bundle.getString(Constant.ALBUM_ARTIST);
        arrayList = getArrayListSong(bundle.getString(Constant.ARRAY_SONG_PLUS));
        initData();
        initView(view);
        return view;
    }

    private ArrayList<SongPlus> getArrayListSong(String string) {
        ArrayList<SongPlus> array = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(string);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                JSONObject song = object.getJSONObject("song");
                int id = song.getInt("id");
                String name = song.getString("name");
                String image = null;
                try {
                    image = song.getString("image");
                } catch (Exception e) {
                    Log.e(TAG, "getArrayListSong: Khong co hinh");
                }
                String data = song.getString("data");
                String artist = song.getString("artist");
                int artistId = song.getInt("artistId");
                String album = song.getString("album");
                int idAlbum = song.getInt("idAlbum");
                SongPlus songPlus = new SongPlus(new Song(id, image, name, data, artist, artistId, album, idAlbum, false, 0), 0, object.getInt("position"));
                array.add(songPlus);
            }
        } catch (Exception ex) {
            ex.printStackTrace();

        }
        return array;
    }

    private void initData() {
        albumAdapter = new SongAlbumAdapter(arrayList);
    }

    private void initView(View view) {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(albumName.toUpperCase());
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).backToMainFragment(DetailAlbumFragment.this);
            }
        });

        Glide.with(view.getContext())
                .load(albumCover)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.song_cover_green)
                .into(imvCover);
        tvName.setText(albumName);
        tvInfo.setText(info);
        tvArtist.setText(artist);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(albumAdapter);
    }
}
