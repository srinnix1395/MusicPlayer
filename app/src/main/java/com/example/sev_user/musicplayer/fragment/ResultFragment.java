package com.example.sev_user.musicplayer.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sev_user.musicplayer.R;
import com.example.sev_user.musicplayer.activity.MainActivity;
import com.example.sev_user.musicplayer.adapter.SearchAdapter;
import com.example.sev_user.musicplayer.constant.Constant;
import com.example.sev_user.musicplayer.model.BaseModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by DELL on 11/19/2016.
 */

public class ResultFragment extends Fragment {
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private String query;
    private String type;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resut, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initData();
        initViews();
    }

    private void initViews() {
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).backToMainFragment(ResultFragment.this);
            }
        });
        toolbar.setTitle("Toàn bộ " + type + " có \"" + query + "\"");

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initData() {
        Bundle bundle = getArguments();
        ArrayList<BaseModel> arrayList = bundle.getParcelableArrayList(Constant.ARRAY);
        query = bundle.getString(Constant.QUERY);
        type = bundle.getString(Constant.TYPE);

        for (BaseModel model : arrayList) {
            model.setHasLine(true);
        }

        SparseIntArray mapSong = ((MainActivity) getActivity()).getMapResult();
        SearchAdapter adapter = new SearchAdapter(getContext(), arrayList, ((MainActivity) getActivity()));
        adapter.setMapSong(mapSong);
        recyclerView.setAdapter(adapter);
    }
}
