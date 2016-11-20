package com.example.sev_user.musicplayer.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sev_user.musicplayer.R;
import com.example.sev_user.musicplayer.activity.MainActivity;
import com.example.sev_user.musicplayer.adapter.SongAlbumAdapter;
import com.example.sev_user.musicplayer.constant.Constant;
import com.example.sev_user.musicplayer.model.SongPlus;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sev_user on 7/21/2016.
 */
public class DetailAlbumFragment extends Fragment {

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

    public static DetailAlbumFragment newInstance(Bundle bundle) {
        DetailAlbumFragment fragment = new DetailAlbumFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_album, container, false);
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
        super.onViewCreated(view, savedInstanceState);
        getData();
        initData();
        initView();
    }

    private void getData() {
        Bundle bundle = getArguments();
        albumName = bundle.getString(Constant.ALBUM_NAME);
        albumCover = bundle.getString(Constant.ALBUM_COVER);
        info = bundle.getString(Constant.ALBUM_INFO);
        artist = bundle.getString(Constant.ALBUM_ARTIST);
        arrayList = bundle.getParcelableArrayList(Constant.ARRAY_SONG_PLUS);
    }

    private void initData() {
        albumAdapter = new SongAlbumAdapter(arrayList, (MainActivity) getActivity());
    }

    private void initView() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(albumName);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).backToMainFragment(DetailAlbumFragment.this);
            }
        });

        Glide.with(this)
                .load(albumCover)
                .error(R.drawable.background_no_image_green)
                .into(imvCover);

        tvName.setText(albumName);
        tvInfo.setText(info);
        tvArtist.setText(artist);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(albumAdapter);
    }
}
