package com.example.sev_user.musicplayer.fragment;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.sev_user.musicplayer.R;
import com.example.sev_user.musicplayer.adapter.ArtistAdapter;
import com.example.sev_user.musicplayer.provider.MusicContentProvider;

import java.util.ArrayList;
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
public class ArtistFragment extends android.support.v4.app.Fragment {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    private ArrayList<Object> artistArrayList;
    private ArtistAdapter adapter;

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

    private void initData() {
        Single.fromCallable(new Callable<ArrayList<Object>>() {
            @Override
            public ArrayList<Object> call() throws Exception {
                MusicContentProvider contentProvider = new MusicContentProvider(getContext());
                return contentProvider.getArrArtist();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ArrayList<Object>>() {
                    @Override
                    public void onSuccess(ArrayList<Object> value) {
                        artistArrayList.addAll(value);
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

    private void initView() {
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#333333"), PorterDuff.Mode.SRC_ATOP);

        artistArrayList = new ArrayList<>();
        adapter = new ArtistAdapter(artistArrayList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setChangeDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);
    }
}
