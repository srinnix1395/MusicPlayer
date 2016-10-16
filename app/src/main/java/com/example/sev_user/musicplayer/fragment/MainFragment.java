package com.example.sev_user.musicplayer.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sev_user.musicplayer.R;
import com.example.sev_user.musicplayer.adapter.PagerAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sev_user on 7/22/2016.
 */
public class MainFragment extends Fragment {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.tabLayout)
    TabLayout tabLayout;

    @Bind(R.id.viewPager)
    ViewPager viewPager;

    private SongFragment songFragment;
    private AlbumFragment albumFragment;
    private ArtistFragment artistFragment;
    private PagerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initViews();
    }

    private void initViews() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("NHẠC");
        toolbar.setTitleTextColor(Color.WHITE);

        songFragment = new SongFragment();
        albumFragment = new AlbumFragment();
        artistFragment = new ArtistFragment();
        ArrayList<Fragment> arrayList = new ArrayList<>();
        arrayList.add(artistFragment);
        arrayList.add(albumFragment);
        arrayList.add(songFragment);
        adapter = new PagerAdapter((getActivity()).getSupportFragmentManager(), arrayList);

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(2, true);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(), true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(), true);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(), true);
            }
        });
    }

    public SongFragment getSongFragment() {
        return songFragment;
    }
}
