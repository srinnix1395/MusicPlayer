package com.example.sev_user.musicplayer.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.sev_user.musicplayer.R;
import com.example.sev_user.musicplayer.adapter.SearchAdapter;
import com.example.sev_user.musicplayer.callback.OnClickViewHolderCallback;
import com.example.sev_user.musicplayer.constant.Constant;
import com.example.sev_user.musicplayer.model.Album;
import com.example.sev_user.musicplayer.model.Artist;
import com.example.sev_user.musicplayer.model.BaseModel;
import com.example.sev_user.musicplayer.model.Song;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by DELL on 11/19/2016.
 */

public class ResultActivity extends AppCompatActivity implements OnClickViewHolderCallback {
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private String query;
    private String type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);
        initData();
        initViews();
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Toàn bộ " + type + " có \"" + query + "\"");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initData() {
        Intent intent = getIntent();
        ArrayList<BaseModel> arrayList = intent.getParcelableArrayListExtra(Constant.ARRAY);
        query = intent.getStringExtra(Constant.QUERY);
        type = intent.getStringExtra(Constant.TYPE);

        for (BaseModel model : arrayList) {
            model.setHasLine(true);
        }

        recyclerView.setAdapter(new SearchAdapter(this, arrayList, this));
    }

    @Override
    public void onClickSong(Song song, int position) {

    }

    @Override
    public void onClickAlbum(Album album, int position) {

    }

    @Override
    public void onClickArtist(Artist artist) {

    }
}
