package com.example.sev_user.musicplayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sev_user.musicplayer.R;
import com.example.sev_user.musicplayer.adapter.AlbumAdapter;
import com.example.sev_user.musicplayer.model.Album;
import com.example.sev_user.musicplayer.provider.MusicContentProvider;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sev_user on 7/14/2016.
 */
public class AlbumFragment extends Fragment {
    private View view;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private ArrayList<Album> arrAlbum;
    private AlbumAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_album, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView();
    }

    private void initView() {
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        recyclerView.setAdapter(adapter);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setChangeDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);
    }

    private void initData() {
        MusicContentProvider contentProvider = new MusicContentProvider(view.getContext());
        arrAlbum = contentProvider.getAlbums();
        adapter = new AlbumAdapter(arrAlbum);
    }


    public ArrayList<Album> getArrayListAlbum() {
        return arrAlbum;
    }
}
