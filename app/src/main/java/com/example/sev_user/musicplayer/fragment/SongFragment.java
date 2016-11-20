package com.example.sev_user.musicplayer.fragment;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.example.sev_user.musicplayer.adapter.SongAdapter;
import com.example.sev_user.musicplayer.model.BaseModel;
import com.example.sev_user.musicplayer.model.Song;
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
public class SongFragment extends Fragment {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    private ArrayList<BaseModel> songArrayList;

    private SongAdapter adapter;
    private int sortType = AlbumAdapter.ASCENDING;

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
                MusicContentProvider provider = new MusicContentProvider(getContext());
                return provider.getArrSong(provider.getSongHasImage());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ArrayList<BaseModel>>() {
                    @Override
                    public void onSuccess(ArrayList<BaseModel> value) {
                        songArrayList.addAll(value);
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

        songArrayList = new ArrayList<>();
        adapter = new SongAdapter(songArrayList, ((MainActivity) getActivity()), ((MainActivity) getActivity()));

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setChangeDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);
    }

    public void sort(Song currentSong, int type) {
        if (type != sortType) {
            ArrayList<BaseModel> newList = reverseList(currentSong);
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

    private ArrayList<BaseModel> reverseList(Song currentSong) {
        int newCurrentPosition = 2;
        ArrayList<BaseModel> newList = new ArrayList<>();
        newList.add(songArrayList.get(0));

        for (int i = songArrayList.size() - 1, pivot = 1, j = 1; i > 0; i--) {
            BaseModel object = songArrayList.get(i);

            newList.add(pivot, object);
            j++;
            if (object.getTypeModel() == BaseModel.TYPE_HEADER) {
                pivot = j;
            } else if (currentSong != null && ((Song) object).getId() == currentSong.getId()) {
                newCurrentPosition = i;
            }
        }

        if (currentSong == null) {
            ((MainActivity) getActivity()).resetArrayAudio(newList, 2);
        } else {
            ((MainActivity) getActivity()).resetArrayAudio(newList, newCurrentPosition);
        }

        return newList;
    }

    public ArrayList<BaseModel> getArrayListAll() {
        return songArrayList;
    }

    public Song getFirstSong() {
        if (songArrayList.size() > 0) {
            return (Song) songArrayList.get(2);
        }
        return null;
    }
}
