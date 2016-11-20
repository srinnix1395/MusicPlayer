package com.example.sev_user.musicplayer.fragment;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.sev_user.musicplayer.R;
import com.example.sev_user.musicplayer.activity.MainActivity;
import com.example.sev_user.musicplayer.adapter.AlbumAdapter;
import com.example.sev_user.musicplayer.adapter.ArtistAdapter;
import com.example.sev_user.musicplayer.model.Artist;
import com.example.sev_user.musicplayer.model.BaseModel;
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

    private ArrayList<BaseModel> artistArrayList;
    private ArrayList<Artist> artists;

    private ArtistAdapter adapter;
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

    private void initData() {
        Single.fromCallable(new Callable<ArrayList<BaseModel>>() {
            @Override
            public ArrayList<BaseModel> call() throws Exception {
                MusicContentProvider contentProvider = new MusicContentProvider(getContext());
                artists = contentProvider.getArtist();
                return contentProvider.getArrArtist(artists);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ArrayList<BaseModel>>() {
                    @Override
                    public void onSuccess(ArrayList<BaseModel> value) {
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
        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FF4081"), PorterDuff.Mode.SRC_ATOP);

        artistArrayList = new ArrayList<>();
        adapter = new ArtistAdapter(artistArrayList, ((MainActivity) getActivity()));

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setChangeDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);
    }

    public void sort(int type) {
        if (type != sortType) {
            ArrayList<BaseModel> newList = reverseList();
            adapter.swapItems(newList);
            sortType = type;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            }, 500);
        }
    }

    private ArrayList<BaseModel> reverseList() {
        ArrayList<BaseModel> newList = new ArrayList<>();

        for (int i = artistArrayList.size() - 1, pivot = 0, j = 0; i >= 0; i--) {
            newList.add(pivot, artistArrayList.get(i));
            j++;
            if (artistArrayList.get(i).getTypeModel() == BaseModel.TYPE_HEADER) {
                pivot = j;
            }
        }
        return newList;
    }

    public ArrayList<Artist> getArtists() {
        return artists;
    }
}
