package com.example.sev_user.musicplayer.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sev_user.musicplayer.R;
import com.example.sev_user.musicplayer.adapter.AlbumAdapter;
import com.example.sev_user.musicplayer.adapter.PagerAdapter;
import com.example.sev_user.musicplayer.callback.OnClickShuffleCallback;
import com.example.sev_user.musicplayer.callback.OnClickSongAlbumCallback;
import com.example.sev_user.musicplayer.callback.OnClickViewHolderCallback;
import com.example.sev_user.musicplayer.callback.ResultFragmentCallback;
import com.example.sev_user.musicplayer.callback.ShowAllResultCallback;
import com.example.sev_user.musicplayer.constant.Constant;
import com.example.sev_user.musicplayer.fragment.AlbumFragment;
import com.example.sev_user.musicplayer.fragment.ArtistFragment;
import com.example.sev_user.musicplayer.fragment.DetailAlbumFragment;
import com.example.sev_user.musicplayer.fragment.DetailArtistFragment;
import com.example.sev_user.musicplayer.fragment.ResultFragment;
import com.example.sev_user.musicplayer.fragment.SearchFragment;
import com.example.sev_user.musicplayer.fragment.SongFragment;
import com.example.sev_user.musicplayer.model.Album;
import com.example.sev_user.musicplayer.model.Artist;
import com.example.sev_user.musicplayer.model.BaseModel;
import com.example.sev_user.musicplayer.model.Song;
import com.example.sev_user.musicplayer.model.SongPlus;
import com.example.sev_user.musicplayer.service.MusicService;
import com.example.sev_user.musicplayer.utils.ImageUtils;
import com.example.sev_user.musicplayer.utils.SharedPreUtil;
import com.squareup.seismic.ShakeDetector;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements OnClickViewHolderCallback, ShakeDetector.Listener
		, OnClickShuffleCallback, OnClickSongAlbumCallback, ShowAllResultCallback, ResultFragmentCallback {
	
	private static final String TAG_FRAGMENT_RESULT = "TAG_FRAGMENT_RESULT";
	private static final String TAG_FRAGMENT_DETAIL_ALBUM = "TAG_FRAGMENT_DETAIL_ALBUM";
	private static final String TAG_FRAGMENT_DETAIL_ARTIST = "TAG_FRAGMENT_DETAIL_ARTIST";
	private static final String TAG_FRAGMENT_SEARCH = "TAG_FRAGMENT_SEARCH";
	
	@Bind(R.id.bottom_sheet)
	LinearLayout layoutPlay;
	
	@Bind(R.id.imvPlay)
	ImageView imvPlay;
	
	@Bind(R.id.imvPlayToolbar)
	ImageView imvPlayToolbar;
	
	@Bind(R.id.tvCurrentPosition)
	TextView tvCurrentPosition;
	
	@Bind(R.id.tvDuration)
	TextView tvDuration;
	
	@Bind(R.id.seekBar)
	SeekBar seekBar;
	
	@Bind(R.id.imvRandom)
	ImageView imvShuffle;
	
	@Bind(R.id.imvRepeat)
	ImageView imvRepeat;
	
	@Bind(R.id.imvCover)
	ImageView imvCover;
	
	@Bind(R.id.imvSong)
	ImageView imvSongThumbnail;
	
	@Bind(R.id.tvSongName)
	TextView tvSongName;
	
	@Bind(R.id.tvArtistName)
	TextView tvArtistName;
	
	@Bind(R.id.viewList)
	RelativeLayout layoutList;
	
	@Bind(R.id.toolbar)
	Toolbar toolbar;
	
	@Bind(R.id.tabLayout)
	TabLayout tabLayout;
	
	@Bind(R.id.viewPager)
	ViewPager viewPager;
	
	private SongFragment songFragment;
	private ArtistFragment artistFragment;
	private AlbumFragment albumFragment;
	
	private PagerAdapter adapter;
	private BottomSheetBehavior behavior;
	
	private MusicService musicService;
	private SongPlus currentSong;
	private boolean isRunning = true;
	private String currentAction = "";
	private boolean isBind;
	private boolean isPaused = false;
	
	private Handler mHandlerOnCompletion = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case Constant.WHAT_UPDATE_UI: {
					if (mHandlerOnCompletion != null) {
						updateDataSong();
						currentSong = new SongPlus(musicService.getCurrentSong(), 0, musicService.getCurrentPosition());
					}
					break;
				}
				case Constant.WHAT_CLOSE_SERVICE: {
					isRunning = false;
					musicService.stopForeground(true);
					MainActivity.this.stopService(new Intent(MainActivity.this, MusicService.class));
					if (isBind) {
						unbindService(connection);
						isBind = false;
					}
					imvPlayToolbar.setActivated(false);
					imvPlay.setActivated(false);
					musicService = null;
					break;
				}
				case Constant.WHAT_NEW_TASK: {
					isRunning = false;
					if (isBind) {
						unbindService(connection);
						isBind = false;
					}
					break;
				}
			}
		}
	};
	
	private ServiceConnection connection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			isBind = true;
			musicService = ((MusicService.BinderMusic) service).getService();
			musicService.setHandler(mHandlerOnCompletion);
			switch (currentAction) {
				case Constant.ACTION_PLAY_MUSIC: {
					if (currentSong == null) {
						currentSong = new SongPlus(null, 0, 0);
					}
					currentSong.setPositionSeekBar(seekBar.getProgress());
					musicService.reUpdateDataFromClient(currentSong
							, imvShuffle.isActivated(), imvRepeat.isActivated());
					musicService.start();
					break;
				}
				case Constant.ACTION_CLICK_SONG: {
					musicService.reUpdateDataFromClient(currentSong, imvShuffle.isActivated(), imvRepeat.isActivated());
					musicService.create(currentSong.getPosition());
					musicService.start();
					break;
				}
				case Constant.ACTION_NEXT_MUSIC: {
					musicService.reUpdateDataFromClient(currentSong, imvShuffle.isActivated(), imvRepeat.isActivated());
					if (musicService.getShuffle()) {
						musicService.randomSong();
					} else {
						musicService.changeSong(1);
					}
					break;
				}
				case Constant.ACTION_PREV_MUSIC: {
					musicService.reUpdateDataFromClient(currentSong, imvShuffle.isActivated(), imvRepeat.isActivated());
					if (musicService.getShuffle()) {
						musicService.randomSong();
					} else {
						musicService.changeSong(-1);
					}
					break;
				}
				case Constant.ACTION_PLAY_SHUFFLE: {
					musicService.reUpdateDataFromClient(currentSong, imvShuffle.isActivated(), imvRepeat.isActivated());
					musicService.randomSong();
					break;
				}
				
			}
			
			updateDataSong();
			musicService.updateNotification();
			currentSong = new SongPlus(musicService.getCurrentSong(), 0, musicService.getCurrentPosition());
			isRunning = true;
			new UpdateUIAsyncTask().execute();
			if (currentAction.isEmpty()) {
				initData();
			}
		}
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
		}
	};
	private Animation animTranslateInImage;
	private Animation animTranslateInButton;
	private Animation animTranslateOutButton;
	private Animation animTranslateOutImage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
		initViews();
		initAnimation();
		if (SharedPreUtil.getInstance(this).getBoolean(Constant.IS_PLAYING_SERVICE, false)) {
			initService();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_toolbar_main, menu);
		boolean settingShake = SharedPreUtil.getInstance(this).getBoolean(Constant.SETTING_SHAKE_TO_SHUFFLE, true);
		if (settingShake) {
			SharedPreUtil.getInstance(this).putBoolean(Constant.SETTING_SHAKE_TO_SHUFFLE, true);
			menu.findItem(R.id.miShakeToShuffle).setChecked(true);
		}
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.miSearch: {
				SearchFragment searchFragment = SearchFragment.newInstance(songFragment.getArrayListAll(),
						artistFragment.getArtists(), albumFragment.getAlbumArrayList());
				
				FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
				transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
				transaction.add(R.id.viewList, searchFragment, TAG_FRAGMENT_SEARCH);
				transaction.show(searchFragment);
				transaction.commit();
				
				Fragment detailAlbumFragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_DETAIL_ALBUM);
				if (detailAlbumFragment != null) {
					backToMainFragment(detailAlbumFragment);
				}
				Fragment detailArtistFragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_DETAIL_ARTIST);
				if (detailArtistFragment != null) {
					backToMainFragment(detailArtistFragment);
				}
				break;
			}
			case R.id.miPlayShuffle: {
				playShuffleAll();
				break;
			}
			case R.id.miShakeToShuffle: {
				item.setChecked(!item.isChecked());
				SharedPreUtil.getInstance(this).putBoolean(Constant.SETTING_SHAKE_TO_SHUFFLE, item.isChecked());
				break;
			}
			case R.id.miSortAZ: {
				if (getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_DETAIL_ALBUM) == null
						|| getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_DETAIL_ARTIST) == null) {
					sortAZ();
				}
				break;
			}
			case R.id.miSortZA: {
				if (getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_DETAIL_ALBUM) == null
						|| getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_DETAIL_ARTIST) == null) {
					sortZA();
				}
				break;
			}
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void sortZA() {
		switch (viewPager.getCurrentItem()) {
			case 0: {
				artistFragment.sort(AlbumAdapter.DESCENDING);
				break;
			}
			case 1: {
				albumFragment.sort(AlbumAdapter.DESCENDING);
				break;
			}
			case 2: {
				songFragment.sort(musicService != null ? musicService.getCurrentSong() : null,
						AlbumAdapter.DESCENDING);
				break;
			}
		}
	}
	
	private void sortAZ() {
		switch (viewPager.getCurrentItem()) {
			case 0: {
				artistFragment.sort(AlbumAdapter.ASCENDING);
				break;
			}
			case 1: {
				albumFragment.sort(AlbumAdapter.ASCENDING);
				break;
			}
			case 2: {
				songFragment.sort(musicService != null ? musicService.getCurrentSong() : null
						, AlbumAdapter.ASCENDING);
				break;
			}
		}
	}
	
	public void resetArrayAudio(ArrayList<BaseModel> newList, int currentPosition) {
		if (musicService != null && musicService.isCreated()) {
			musicService.setListSong(newList);
			musicService.setCurrentPosition(currentPosition);
		}
	}
	
	@Override
	public void onBackPressed() {
		if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
			behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
			return;
		}
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		
		if (fragmentManager.findFragmentByTag(TAG_FRAGMENT_DETAIL_ALBUM) != null) {
			backToMainFragment(fragmentManager.findFragmentByTag(TAG_FRAGMENT_DETAIL_ALBUM));
			return;
		}
		
		if (fragmentManager.findFragmentByTag(TAG_FRAGMENT_DETAIL_ARTIST) != null) {
			backToMainFragment(fragmentManager.findFragmentByTag(TAG_FRAGMENT_DETAIL_ARTIST));
			return;
		}
		
		if (fragmentManager.findFragmentByTag(TAG_FRAGMENT_RESULT) != null) {
			backToMainFragment(getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_RESULT));
			return;
		}
		
		if (fragmentManager.findFragmentByTag(TAG_FRAGMENT_SEARCH) != null) {
			backToMainFragment(fragmentManager.findFragmentByTag(TAG_FRAGMENT_SEARCH));
			return;
		}
		
		finish();
	}
	
	@Override
	protected void onDestroy() {
		isRunning = false;
		if (isBind) {
			unbindService(connection);
			isBind = false;
		}
		mHandlerOnCompletion = null;
		super.onDestroy();
	}
	
	//process service-------------------------
	
	private void initData() {
		if (musicService != null && musicService.isCreated()) {
			tvSongName.setText(musicService.getSongName());
			tvArtistName.setText(musicService.getArtistName());
			imvPlay.setActivated(musicService.isPlaying());
			imvPlayToolbar.setActivated(musicService.isPlaying());
			setTimeDuration();
			
			if (musicService.getAlbumCover() == null) {
				imvSongThumbnail.setScaleType(ImageView.ScaleType.FIT_XY);
				imvSongThumbnail.setImageResource(musicService.getCurrentPlaceholder());
			} else {
				imvSongThumbnail.setScaleType(ImageView.ScaleType.FIT_CENTER);
				Glide.with(this)
						.load(musicService.getAlbumCover())
						.thumbnail(0.1f)
						.into(imvSongThumbnail);
			}
			
			Glide.with(this)
					.load(musicService.getAlbumCover())
					.error(musicService.getCurrentPlaceholder())
					.into(imvCover);
		} else {
			Song song = songFragment.getFirstSong();
			if (song == null) {
				return;
			}
			
			tvSongName.setText(song.getName());
			tvArtistName.setText(song.getArtist());
			imvPlay.setActivated(false);
			imvPlayToolbar.setActivated(false);
			setTimeDuration();
			
			if (song.getImage() == null) {
				imvSongThumbnail.setScaleType(ImageView.ScaleType.FIT_XY);
				imvSongThumbnail.setImageResource(song.getPlaceHolder());
			} else {
				imvSongThumbnail.setScaleType(ImageView.ScaleType.FIT_CENTER);
				Glide.with(this)
						.load(song.getImage())
						.thumbnail(0.1f)
						.into(imvSongThumbnail);
			}
			
			Glide.with(this)
					.load(song.getImage())
					.error(song.getPlaceHolder())
					.into(imvCover);
		}
	}
	
	private void initService() {
		Intent intent = new Intent(this, MusicService.class);
		startService(intent);
		bindService(intent, connection, BIND_AUTO_CREATE);
	}
	
	//process service-------------------------
	
	private void initViews() {
		SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		ShakeDetector sd = new ShakeDetector(this);
		sd.start(sensorManager);
		
		setSupportActionBar(toolbar);
		if (getSupportActionBar() != null) {
			getSupportActionBar().setTitle(R.string.music);
		}
		toolbar.setTitleTextColor(Color.WHITE);
		
		songFragment = new SongFragment();
		albumFragment = new AlbumFragment();
		artistFragment = new ArtistFragment();
		ArrayList<Fragment> arrayList = new ArrayList<>();
		arrayList.add(artistFragment);
		arrayList.add(albumFragment);
		arrayList.add(songFragment);
		adapter = new PagerAdapter(getSupportFragmentManager(), arrayList);
		
		initViewPager();
		initBottomSheet();
		
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				
			}
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				if (musicService != null && musicService.isCreated()) {
					musicService.seekTo(seekBar.getProgress());
				}
			}
		});
		
		imvShuffle.setActivated(SharedPreUtil.getInstance(this).getBoolean(Constant.SHUFFLE, false));
	}
	
	private void initAnimation() {
		animTranslateInImage = AnimationUtils.loadAnimation(MainActivity.this, R.anim.transition_in_left);
		animTranslateInImage.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				imvSongThumbnail.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
		});
		
		animTranslateInButton = AnimationUtils.loadAnimation(MainActivity.this, R.anim.transition_in_right);
		animTranslateInButton.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				imvPlayToolbar.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
		});
		
		animTranslateOutImage = AnimationUtils.loadAnimation(MainActivity.this, R.anim.transition_out_left);
		animTranslateOutImage.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				imvSongThumbnail.setVisibility(View.INVISIBLE);
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
		});
		
		animTranslateOutButton = AnimationUtils.loadAnimation(MainActivity.this, R.anim.transition_out_right);
		animTranslateOutButton.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				imvPlayToolbar.setVisibility(View.INVISIBLE);
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
		});
	}
	
	private void initBottomSheet() {
		behavior = BottomSheetBehavior.from(layoutPlay);
		behavior.setPeekHeight((int) ImageUtils.convertDpToPixel(56f, this));
		
		Intent intent = getIntent();
		if (intent != null && intent.hasExtra(Constant.ACTION_NEW_TASK)) {
			behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
		}
		
		behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
			@Override
			public void onStateChanged(@NonNull View bottomSheet, int newState) {
				switch (newState) {
					case BottomSheetBehavior.STATE_COLLAPSED: {
						if (imvPlayToolbar.getVisibility() != View.VISIBLE) {
							imvSongThumbnail.startAnimation(animTranslateInImage);
							imvPlayToolbar.startAnimation(animTranslateInButton);
						}
						break;
					}
					case BottomSheetBehavior.STATE_EXPANDED: {
						if (imvSongThumbnail.getVisibility() == View.VISIBLE) {
							imvSongThumbnail.startAnimation(animTranslateOutImage);
							imvPlayToolbar.startAnimation(animTranslateOutButton);
						}
						break;
					}
				}
			}
			
			@Override
			public void onSlide(@NonNull View bottomSheet, float slideOffset) {
				
			}
		});
		
		imvCover.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				//to can't click the behind view
				return true;
			}
		});
	}
	
	private void initViewPager() {
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(2, true);
		viewPager.setOffscreenPageLimit(3);
		tabLayout.setupWithViewPager(viewPager);
		tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
	
	@Override
	protected void onResume() {
		super.onResume();
		isPaused = false;
	}
	
	@Override
	protected void onPause() {
		isPaused = true;
		super.onPause();
	}
	
	//override OnClickViewHolderCallback interface-------------
	@Override
	public void onClickSong(Song song, int position) {
		currentSong = new SongPlus(song, 0, position);
		if (musicService == null) {
			initService();
			currentAction = Constant.ACTION_CLICK_SONG;
			return;
		}
		musicService.create(position);
		musicService.start();
		updateDataSong();
	}
	
	@Override
	public void onClickAlbum(Album album, int position) {
		ArrayList<SongPlus> arrSongPlus = getSongOfAlbum(songFragment.getArrayListAll(), album);
		
		Bundle bundle = new Bundle();
		bundle.putParcelableArrayList(Constant.ARRAY_SONG_PLUS, arrSongPlus);
		bundle.putParcelable(Constant.ALBUM, album);
		DetailAlbumFragment detailAlbumFragment = DetailAlbumFragment.newInstance(bundle);
		
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
		transaction.add(R.id.viewList, detailAlbumFragment, TAG_FRAGMENT_DETAIL_ALBUM);
		transaction.show(detailAlbumFragment);
		transaction.commit();
	}
	
	@Override
	public void onClickArtist(Artist artist) {
		ArrayList<SongPlus> arrayList = getSongOfArtist(songFragment.getArrayListAll(), artist);
		 
		Bundle bundle = new Bundle();
		bundle.putParcelableArrayList(Constant.ARRAY_SONG_PLUS, arrayList);
		bundle.putParcelable(Constant.ARTIST, artist);
		DetailArtistFragment detailArtistFragment = DetailArtistFragment.newInstance(bundle);
		
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
		transaction.add(R.id.viewList, detailArtistFragment, TAG_FRAGMENT_DETAIL_ARTIST);
		transaction.show(detailArtistFragment);
		transaction.commit();
	}
	
	private ArrayList<SongPlus> getSongOfArtist(ArrayList<BaseModel> arrayListSong, Artist artist) {
		ArrayList<SongPlus> arrayList = new ArrayList<>();
		for (BaseModel obj : arrayListSong) {
			if (obj.getTypeModel() == BaseModel.TYPE_SONG && ((Song) obj).getArtistId() == artist.getId()) {
				arrayList.add(new SongPlus(((Song) obj), 0, arrayListSong.indexOf(obj)));
			}
		}
		return arrayList;
	}
	
	@Override
	public void onClickSongAlbum(SongPlus songPlus) {
		this.currentSong = songPlus;
		if (musicService == null) {
			initService();
			currentAction = Constant.ACTION_CLICK_SONG;
			return;
		}
		musicService.create(songPlus.getPosition());
		musicService.start();
		updateDataSong();
	}
	
	private ArrayList<SongPlus> getSongOfAlbum(ArrayList<BaseModel> arrayList, Album album) {
		ArrayList<SongPlus> arrSong = new ArrayList<>();
		for (Object obj : arrayList) {
			if (obj instanceof Song && ((Song) obj).getIdAlbum() == album.getId()) {
				arrSong.add(new SongPlus(((Song) obj), 0, arrayList.indexOf(obj)));
			}
		}
		return arrSong;
	}
	
	//override OnClickViewHolderCallback interface-------------
	@OnClick({R.id.imvPlay, R.id.imvPlayToolbar, R.id.imvNext, R.id.imvPre, R.id.imvRandom, R.id.imvRepeat, R.id.linearLayoutPlay})
	void onClick(View view) {
		switch (view.getId()) {
			case R.id.imvPlayToolbar:
			case R.id.imvPlay: {
				onClickPlay();
				break;
			}
			case R.id.imvNext: {
				onClickNext();
				break;
			}
			case R.id.imvPre: {
				onClickPrev();
				break;
			}
			case R.id.imvRandom: {
				onClickRandom();
				break;
			}
			case R.id.imvRepeat: {
				onClickRepeat();
				break;
			}
			case R.id.linearLayoutPlay: {
				if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
					behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
				}
				break;
			}
		}
	}
	
	//START - function play, pause...---------------------------
	private void onClickPlay() {
		if (musicService == null) {
			initService();
			currentAction = Constant.ACTION_PLAY_MUSIC;
			return;
		}
		
		if (!musicService.isCreated()) {
			musicService.create(2);
			musicService.start();
			updateDataSong();
		} else {
			if (musicService.isPlaying()) {
				musicService.pause();
				imvPlay.setActivated(false);
				imvPlayToolbar.setActivated(false);
			} else {
				musicService.start();
				imvPlay.setActivated(true);
				imvPlayToolbar.setActivated(true);
			}
			musicService.updateNotification();
		}
		currentSong = new SongPlus(musicService.getCurrentSong(), 0, musicService.getCurrentPosition());
	}
	
	private void onClickPrev() {
		if (musicService == null) {
			initService();
			currentAction = Constant.ACTION_PREV_MUSIC;
			return;
		}
		if (musicService.getShuffle()) {
			musicService.randomSong();
		} else {
			musicService.changeSong(-1);
		}
		updateDataSong();
		currentSong = new SongPlus(musicService.getCurrentSong(), 0, musicService.getCurrentPosition());
	}
	
	private void onClickRepeat() {
		if (musicService == null) {
			imvRepeat.setActivated(!imvRepeat.isActivated());
			return;
		}
		if (musicService.isCreated()) {
			switch (musicService.getLevelRepeat()) {
				case Constant.LEVEL_LOOPING_ALL: {
					imvRepeat.setActivated(true);
					musicService.setLevelRepeat(Constant.LEVEL_LOOPING_NONE);
					break;
				}
				case Constant.LEVEL_LOOPING_NONE: {
					imvRepeat.setActivated(false);
					musicService.setLevelRepeat(Constant.LEVEL_LOOPING_ALL);
					break;
				}
			}
		}
	}
	
	private void onClickRandom() {
		if (musicService == null) {
			imvShuffle.setActivated(!imvShuffle.isActivated());
			return;
		}
		musicService.setShuffle(!musicService.getShuffle());
		imvShuffle.setActivated(musicService.getShuffle());
	}
	
	private void onClickNext() {
		if (musicService == null) {
			initService();
			currentAction = Constant.ACTION_NEXT_MUSIC;
			return;
		}
		
		if (musicService.getShuffle()) {
			musicService.randomSong();
		} else {
			musicService.changeSong(1);
		}
		updateDataSong();
		currentSong = new SongPlus(musicService.getCurrentSong(), 0, musicService.getCurrentPosition());
	}
	
	private void playShuffleAll() {
		if (musicService == null) {
			initService();
			currentAction = Constant.ACTION_PLAY_SHUFFLE;
			return;
		}
		musicService.randomSong();
		updateDataSong();
		musicService.updateNotification();
		currentSong = new SongPlus(musicService.getCurrentSong(), 0, musicService.getCurrentPosition());
	}
	
	@Override
	public void onClickPlayShuffle() {
		playShuffleAll();
	}
	//END - function play, pause...---------------------------
	
	
	//hear shake to chang song
	@Override
	public void hearShake() {
		if (SharedPreUtil.getInstance(this).getBoolean(Constant.SETTING_SHAKE_TO_SHUFFLE, false)) {
			playShuffleAll();
		}
	}
	
	public void backToMainFragment(Fragment fragment) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		
		if (fragment instanceof DetailAlbumFragment || fragment instanceof DetailArtistFragment) {
			transaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
		}
		
		
		if (fragment instanceof ResultFragment || fragment instanceof SearchFragment) {
			transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
		}
		
		transaction.remove(fragment);
		fragment.onDestroyView();
		transaction.commit();
	}
	
	@Override
	public void showAllResult(Bundle bundle) {
		ResultFragment resultFragment = new ResultFragment();
		resultFragment.setArguments(bundle);
		
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
		transaction.add(R.id.viewList, resultFragment, TAG_FRAGMENT_RESULT);
		transaction.commit();
	}
	
	@Override
	public SparseIntArray getMapResult() {
		return ((SearchFragment) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_SEARCH)).getMapResult();
	}
	
	//update UI--------------------------------------------------
	private class UpdateUIAsyncTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			while (isRunning) {
				if (!isPaused) {
					publishProgress();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
			if (musicService.isCreated() && musicService.isPlaying()) {
				setTimeCurrentPosition();
			}
		}
	}
	
	private void setTimeCurrentPosition() {
		int currentPosition = musicService.getCurrentSeekbar();
		seekBar.setProgress(currentPosition);
		
		int totalSecond = currentPosition / 1000;
		String minute = String.valueOf(totalSecond / 60);
		if (minute.length() == 1) {
			minute = "0" + minute;
		}
		
		String seconds = String.valueOf(totalSecond % 60);
		if (seconds.length() == 1) {
			seconds = "0" + seconds;
		}
		tvCurrentPosition.setText(minute + ":" + seconds);
	}
	
	private void setTimeDuration() {
		int duration = musicService.getDuration();
		seekBar.setMax(duration);
		
		int totalSecond = duration / 1000;
		String minute = String.valueOf(totalSecond / 60);
		if (minute.length() == 1) {
			minute = "0" + minute;
		}
		
		String seconds = String.valueOf(totalSecond % 60);
		if (seconds.length() == 1) {
			seconds = "0" + seconds;
		}
		tvDuration.setText(minute + ":" + seconds);
	}
	
	private void updateDataSong() {
		imvPlay.setActivated(musicService.isPlaying());
		imvPlayToolbar.setActivated(musicService.isPlaying());
		
		setTimeDuration();
		seekBar.setMax(musicService.getDuration());
		Glide.with(this)
				.load(musicService.getAlbumCover())
				.error(musicService.getCurrentPlaceholder())
				.into(imvCover);
		
		Glide.with(this)
				.load(musicService.getAlbumCover())
				.thumbnail(0.1f)
				.placeholder(musicService.getCurrentPlaceholder())
				.error(musicService.getCurrentPlaceholder())
				.into(imvSongThumbnail);
		
		tvSongName.setText(musicService.getSongName());
		tvArtistName.setText(musicService.getArtistName());
		musicService.updateNotification();
	}
}
