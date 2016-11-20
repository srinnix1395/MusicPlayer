package com.example.sev_user.musicplayer.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sev_user.musicplayer.R;
import com.example.sev_user.musicplayer.activity.MainActivity;
import com.example.sev_user.musicplayer.adapter.SongAlbumAdapter;
import com.example.sev_user.musicplayer.constant.Constant;
import com.example.sev_user.musicplayer.model.Artist;
import com.example.sev_user.musicplayer.model.SongPlus;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sev_user on 7/22/2016.
 */
public class DetailArtistFragment extends Fragment {

    private static final String TAG = "DetailArtistFragment";

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.tvCover)
    TextView tvCover;

    @Bind(R.id.recyclerViewSong)
    RecyclerView recyclerViewSong;

    private ArrayList<SongPlus> arraySong;
    private SongAlbumAdapter adapter;
    private Artist artist;

    public static DetailArtistFragment newInstance(Bundle bundle) {
        DetailArtistFragment fragment = new DetailArtistFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_artist, container, false);
        ButterKnife.bind(this, view);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        initData(bundle);
        initView();
    }

    private void initView() {
        toolbar.setTitle(artist.getName());
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).backToMainFragment(DetailArtistFragment.this);
            }
        });

        tvCover.setText(artist.getName().substring(0, 2));
        tvCover.setBackgroundResource(artist.getColorPlaceHolder());

        recyclerViewSong.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewSong.setAdapter(adapter);
    }

    private void initData(Bundle bundle) {
        artist = bundle.getParcelable(Constant.ARTIST);
        arraySong = bundle.getParcelableArrayList(Constant.ARRAY_SONG_PLUS);
        adapter = new SongAlbumAdapter(arraySong, ((MainActivity) getActivity()));
    }
}
