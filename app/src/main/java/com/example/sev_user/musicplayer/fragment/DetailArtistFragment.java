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
import android.widget.TextView;

import com.example.sev_user.musicplayer.R;
import com.example.sev_user.musicplayer.activity.MainActivity;
import com.example.sev_user.musicplayer.adapter.SongAlbumAdapter;
import com.example.sev_user.musicplayer.constant.Constant;
import com.example.sev_user.musicplayer.model.Artist;
import com.example.sev_user.musicplayer.model.Song;
import com.example.sev_user.musicplayer.model.SongPlus;
import com.example.sev_user.musicplayer.utils.ImageUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sev_user on 7/22/2016.
 */
public class DetailArtistFragment extends Fragment {

    private static final String TAG = "DetailArtistFragment";
    private View view;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.tvCover)
    TextView tvCover;

    @Bind(R.id.recyclerViewSong)
    RecyclerView recyclerViewSong;

    private ArrayList<SongPlus> arraySong;
    private SongAlbumAdapter adapter;
    private Artist artist;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail_artist, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        initData(bundle);
        initView();
    }

    private void initView() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).setTitle(artist.getName());
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).backToMainFragment(DetailArtistFragment.this);
            }
        });

        tvCover.setText(artist.getName().substring(0, 2));
        tvCover.setBackgroundColor(ImageUtils.COLORS[3]);

        recyclerViewSong.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerViewSong.setAdapter(adapter);
    }

    private void initData(Bundle bundle) {
        artist = getArtist(bundle.getString(Constant.ARTIST));
        arraySong = getSong(bundle.getString(Constant.ARRAY_SONG_PLUS));
        adapter = new SongAlbumAdapter(arraySong);
    }

    private ArrayList<SongPlus> getSong(String string) {
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

    private Artist getArtist(String string) {
        Artist artist = null;
        try {
            JSONObject object = new JSONObject(string);
            int id = object.getInt("id");
            String name = object.getString("name");
            String info = object.getString("info");
            int colorPlaceHolder = object.getInt("colorPlaceHolder");
            artist = new Artist(id, name, info, colorPlaceHolder, false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return artist;
    }
}
