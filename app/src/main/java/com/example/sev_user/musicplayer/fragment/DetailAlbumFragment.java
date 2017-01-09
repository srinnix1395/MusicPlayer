package com.example.sev_user.musicplayer.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.example.sev_user.musicplayer.model.Album;
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
	private Album album;
	
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
		if (bundle != null) {
			album = bundle.getParcelable(Constant.ALBUM);
			arrayList = bundle.getParcelableArrayList(Constant.ARRAY_SONG_PLUS);
		}
	}
	
	private void initData() {
		if (arrayList == null) {
			arrayList = new ArrayList<>();
		}
		albumAdapter = new SongAlbumAdapter(arrayList, (MainActivity) getActivity());
	}
	
	private void initView() {
		toolbar.setTitleTextColor(Color.WHITE);
		toolbar.setNavigationIcon(R.drawable.ic_back);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((MainActivity) getActivity()).backToMainFragment(DetailAlbumFragment.this);
			}
		});
		
		if (album != null) {
			toolbar.setTitle(album.getAlbumName());
			Glide.with(this)
					.load(album.getImage())
					.placeholder(album.getPlaceHolder())
					.error(album.getPlaceHolder())
					.into(imvCover);
			
			tvName.setText(album.getAlbumName());
			tvInfo.setText(album.getNumberOfSong() + " bài hát | " + album.getFirstYear());
			tvArtist.setText(album.getArtistName());
		}
		
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		recyclerView.setAdapter(albumAdapter);
	}
}
