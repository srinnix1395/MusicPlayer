package com.example.sev_user.musicplayer.fragment;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.sev_user.musicplayer.R;
import com.example.sev_user.musicplayer.adapter.AlbumAdapter;
import com.example.sev_user.musicplayer.model.Album;
import com.example.sev_user.musicplayer.provider.MusicContentProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Callable;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sev_user on 7/14/2016.
 */
public class AlbumFragment extends Fragment {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    private ArrayList<Album> albumArrayList;
    private AlbumAdapter adapter;
    private int sortType = AlbumAdapter.DEFAULT_SORT;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FF4081"), PorterDuff.Mode.SRC_ATOP);

        albumArrayList = new ArrayList<>();
        adapter = new AlbumAdapter(getContext(), albumArrayList);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(adapter);
    }

    private void initData() {
        Single.fromCallable(new Callable<ArrayList<Album>>() {
            @Override
            public ArrayList<Album> call() throws Exception {
                MusicContentProvider contentProvider = new MusicContentProvider(getContext());
                return contentProvider.getAlbums();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ArrayList<Album>>() {
                    @Override
                    public void onSuccess(ArrayList<Album> value) {
                        albumArrayList.addAll(value);
                        adapter.notifyDataSetChanged();

                        progressBar.setVisibility(View.INVISIBLE);
                        progressBar.setEnabled(false);
                    }

                    @Override
                    public void onError(Throwable error) {
                        error.printStackTrace();
                    }
                });


    }

    public void sort(int type) {
        ArrayList<Album> newList = new ArrayList<>();
        newList.addAll(albumArrayList);

        if (type != sortType) {
            Collections.reverse(newList);
            adapter.swapItems(newList);
            sortType = type;
        }
    }

    public ArrayList<Album> getAlbumArrayList() {
        return albumArrayList;
    }
}
