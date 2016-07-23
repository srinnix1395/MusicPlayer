package com.example.sev_user.musicplayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.sev_user.musicplayer.R;
import com.example.sev_user.musicplayer.activity.MainActivity;
import com.example.sev_user.musicplayer.adapter.SongAlbumAdapter;
import com.example.sev_user.musicplayer.model.SongPlus;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sev_user on 7/23/2016.
 */
public class SearchFragment extends Fragment {

    private View view;

//    @Bind(R.id.etSearch)
//    EditText etSearch;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private SongAlbumAdapter adapter;
    private ArrayList<SongPlus> arrayList;
    private SearchView searchView;
    private MenuItem item;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initData();
        initView();
    }

    private void initView() {
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).backToMainFragment(SearchFragment.this);
            }
        });
        toolbar.inflateMenu(R.menu.menu_toolbar_has_search);
        item = toolbar.getMenu().findItem(R.id.miSearch);
        searchView = (SearchView) item.getActionView();
        int searchImgId = getResources().getIdentifier("android:id/search_button", null, null);
        ImageView v = (ImageView) searchView.findViewById(searchImgId);
        v.setImageResource(R.drawable.ic_search);
        item.expandActionView();

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

    }

    private void initData() {

    }
}
