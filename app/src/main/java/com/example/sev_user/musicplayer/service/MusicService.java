package com.example.sev_user.musicplayer.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.sev_user.musicplayer.R;
import com.example.sev_user.musicplayer.activity.MainActivity;
import com.example.sev_user.musicplayer.constant.Constant;
import com.example.sev_user.musicplayer.manager.MediaManager;
import com.example.sev_user.musicplayer.model.BaseModel;
import com.example.sev_user.musicplayer.model.Song;
import com.example.sev_user.musicplayer.model.SongPlus;
import com.example.sev_user.musicplayer.provider.MusicContentProvider;
import com.example.sev_user.musicplayer.utils.SharedPreUtil;

import java.util.ArrayList;

/**
 * Created by sev_user on 7/18/2016.
 */
public class MusicService extends Service {
	private MediaManager mediaManager;
	private BinderMusic binder = new BinderMusic();
	private Notification notification;
	private Handler handler;
	private NotificationCompat.Builder builder;
	
	private boolean isRegistered = false;
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			switch (intent.getAction()) {
				case Constant.ACTION_NEXT_MUSIC: {
					if (mediaManager.isShuffle()) {
						randomSong();
					} else {
						changeSong(1);
					}
					updateUI(Constant.WHAT_UPDATE_UI);
					break;
				}
				case Constant.ACTION_PLAY_MUSIC: {
					if (mediaManager.isPlaying()) {
						mediaManager.pause();
					} else {
						mediaManager.start();
					}
					updateUI(Constant.WHAT_UPDATE_UI);
					break;
				}
				case Constant.ACTION_PREV_MUSIC: {
					if (isShuffle()) {
						randomSong();
					} else {
						changeSong(-1);
					}
					updateUI(Constant.WHAT_UPDATE_UI);
					break;
				}
				case Constant.ACTION_CLOSE: {
					mediaManager.release();
					if (handler != null) {
						Message message = new Message();
						message.what = Constant.WHAT_CLOSE_SERVICE;
						handler.sendMessage(message);
					}
					SharedPreUtil.getInstance(MusicService.this).putBoolean(Constant.IS_PLAYING_SERVICE, false);
					break;
				}
				case Constant.ACTION_NEW_TASK: {
					if (handler != null) {
						Message message = new Message();
						message.what = Constant.WHAT_NEW_TASK;
						handler.sendMessage(message);
					}
					Intent intentNewTask = new Intent(context, MainActivity.class);
					intentNewTask.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intentNewTask.putExtra(Constant.ACTION_NEW_TASK, true);
					context.startActivity(intentNewTask);
					break;
				}
			}
		}
	};
	
	private void updateUI(int what) {
		updateNotification();
		if (handler != null) {
			Message message = new Message();
			message.what = what;
			handler.sendMessage(message);
		}
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		MusicContentProvider contentProvider = new MusicContentProvider(this);
		mediaManager = new MediaManager(this, contentProvider.getArrSong(contentProvider.getSongHasImage()));
		registerReceiver();
		SharedPreUtil.getInstance(this).putBoolean(Constant.IS_PLAYING_SERVICE, true);
	}
	
	private void registerReceiver() {
		if (!isRegistered) {
			IntentFilter filter = new IntentFilter();
			filter.addAction(Constant.ACTION_PLAY_MUSIC);
			filter.addAction(Constant.ACTION_NEXT_MUSIC);
			filter.addAction(Constant.ACTION_PREV_MUSIC);
			filter.addAction(Constant.ACTION_CLOSE);
			filter.addAction(Constant.ACTION_NEW_TASK);
			registerReceiver(receiver, filter);
			isRegistered = true;
		}
	}
	
	@Override
	public void onDestroy() {
		if (isRegistered) {
			unregisterReceiver(receiver);
		}
		SharedPreUtil.getInstance(this).putBoolean(Constant.IS_PLAYING_SERVICE, false);
		super.onDestroy();
	}
	
	private void runForeground() {
		startOrUpdateNotification();
		startForeground(Constant._ID_FOREGROUND_SERVICE, notification);
	}
	
	public void updateNotification() {
		startOrUpdateNotification();
		
		NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		manager.notify(Constant._ID_FOREGROUND_SERVICE, notification);
	}
	
	private void startOrUpdateNotification() {
		RemoteViews bigViews = new RemoteViews(getPackageName(), R.layout.notification_big_view_music);
		bigViews.setTextViewText(R.id.tvName, mediaManager.getNameSong());
		bigViews.setTextViewText(R.id.tvArtist, mediaManager.getArtist());
		
		Intent intentClose = new Intent(Constant.ACTION_CLOSE);
		PendingIntent piClose = PendingIntent.getBroadcast(this, 0, intentClose, PendingIntent.FLAG_UPDATE_CURRENT);
		bigViews.setOnClickPendingIntent(R.id.imvClose, piClose);
		
		Intent intentPlay = new Intent(Constant.ACTION_PLAY_MUSIC);
		PendingIntent piPlay = PendingIntent.getBroadcast(this, 0, intentPlay, PendingIntent.FLAG_UPDATE_CURRENT);
		bigViews.setOnClickPendingIntent(R.id.imvPlay, piPlay);
		
		Intent intentPrev = new Intent(Constant.ACTION_PREV_MUSIC);
		PendingIntent piPrev = PendingIntent.getBroadcast(this, 0, intentPrev, PendingIntent.FLAG_UPDATE_CURRENT);
		bigViews.setOnClickPendingIntent(R.id.imvPre, piPrev);
		
		Intent intentNext = new Intent(Constant.ACTION_NEXT_MUSIC);
		PendingIntent piNext = PendingIntent.getBroadcast(this, 0, intentNext, PendingIntent.FLAG_UPDATE_CURRENT);
		bigViews.setOnClickPendingIntent(R.id.imvNext, piNext);
		
		if (mediaManager.getAlbumCover() == null) {
			bigViews.setImageViewResource(R.id.imvCover, mediaManager.getCurrentPlaceholder());
		} else {
			bigViews.setImageViewUri(R.id.imvCover, Uri.parse(mediaManager.getCurrentImageUri()));
		}
		
		RemoteViews smallRemoteView = new RemoteViews(getPackageName(), R.layout.notification_view_music);
		smallRemoteView.setTextViewText(R.id.tvName, mediaManager.getNameSong());
		smallRemoteView.setTextViewText(R.id.tvArtist, mediaManager.getArtist());
		smallRemoteView.setOnClickPendingIntent(R.id.imvPlay, piPlay);
		smallRemoteView.setOnClickPendingIntent(R.id.imvPre, piPrev);
		smallRemoteView.setOnClickPendingIntent(R.id.imvNext, piNext);
		smallRemoteView.setOnClickPendingIntent(R.id.imvClose, piClose);
		
		if (mediaManager.getAlbumCover() == null) {
			smallRemoteView.setImageViewResource(R.id.imvCover, mediaManager.getCurrentPlaceholder());
//			setImageNotification(smallRemoteView, bigViews, R.drawable.ic_play_48_gray, R.drawable.ic_pause_48_gray,
//					R.drawable.ic_prev_gray, R.drawable.ic_next_gray, R.drawable.ic_close_gray, Color.parseColor("#8a000000")
//					, Color.WHITE, R.drawable.background_view_line_gray);
		} else {
			smallRemoteView.setImageViewUri(R.id.imvCover, Uri.parse(mediaManager.getCurrentImageUri()));
//			Bitmap bitmap = BitmapFactory.decodeFile(mediaManager.getCurrentImageUri());
//			if (bitmap != null) {
//				Palette p = Palette.from(bitmap).generate();
//				bitmap.recycle();
//				Palette.Swatch swatch = p.getDarkVibrantSwatch();
//				if (swatch == null) {
//					swatch = p.getDarkMutedSwatch();
//				}
//				if (swatch == null) {
//					swatch = p.getDominantSwatch();
//				}
//
//				if (swatch != null) {
//					setImageNotification(smallRemoteView, bigViews, R.drawable.ic_play_48_white, R.drawable.ic_pause_48_white,
//							R.drawable.ic_prev_white, R.drawable.ic_next_white, R.drawable.ic_close_white, Color.WHITE
//							, swatch.getRgb(), R.drawable.background_view_line_white);
//				} else {
//					setImageNotification(smallRemoteView, bigViews, R.drawable.ic_play_48_gray, R.drawable.ic_pause_48_gray,
//							R.drawable.ic_prev_gray, R.drawable.ic_next_gray, R.drawable.ic_close_gray
//							, Color.parseColor("#8a000000"), swatch.getRgb(), R.drawable.background_view_line_gray);
//				}
//			} else {
//				setImageNotification(smallRemoteView, bigViews, R.drawable.ic_play_48_gray, R.drawable.ic_pause_48_gray,
//						R.drawable.ic_prev_gray, R.drawable.ic_next_gray, R.drawable.ic_close_gray, Color.parseColor("#8a000000")
//						, Color.WHITE, R.drawable.background_view_line_gray);
//			}
		}
		setImageNotification(smallRemoteView, bigViews, R.drawable.ic_play_48_white, R.drawable.ic_pause_48_white,
				R.drawable.ic_prev_white, R.drawable.ic_next_white, R.drawable.ic_close_white, Color.WHITE
				, Color.parseColor("#424242"), R.drawable.background_view_line_white);
		
		Intent intentNewTask = new Intent(Constant.ACTION_NEW_TASK);
		
		builder = new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.ic_note)
				.setContent(smallRemoteView)
				.setCustomBigContentView(bigViews)
				.setContentIntent(PendingIntent.getBroadcast(this, 0, intentNewTask, 0));
		
		notification = builder.build();
	}
	
	private void setImageNotification(RemoteViews smallRemoteView, RemoteViews bigRemoteView, int icPlay
			, int icPause, int icPrev, int icNext, int icClose, int textColor, int backgroundColor, int viewLine) {
		smallRemoteView.setInt(R.id.layout, "setBackgroundColor", backgroundColor);
		bigRemoteView.setInt(R.id.layout, "setBackgroundColor", backgroundColor);
		
		smallRemoteView.setImageViewResource(R.id.imvPlay, mediaManager.isPlaying() ? icPause : icPlay);
		smallRemoteView.setImageViewResource(R.id.imvPre, icPrev);
		smallRemoteView.setImageViewResource(R.id.imvNext, icNext);
		smallRemoteView.setImageViewResource(R.id.imvClose, icClose);
		smallRemoteView.setTextColor(R.id.tvName, textColor);
		smallRemoteView.setTextColor(R.id.tvArtist, textColor);
		
		bigRemoteView.setImageViewResource(R.id.imvPlay, mediaManager.isPlaying() ? icPause : icPlay);
		bigRemoteView.setImageViewResource(R.id.imvPre, icPrev);
		bigRemoteView.setImageViewResource(R.id.imvNext, icNext);
		bigRemoteView.setImageViewResource(R.id.imvClose, icClose);
		bigRemoteView.setTextColor(R.id.tvName, textColor);
		bigRemoteView.setTextColor(R.id.tvArtist, textColor);
		bigRemoteView.setImageViewResource(R.id.viewLine, viewLine);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
    Log.e("abc", "onStartCommand: " + startId );
    runForeground();
		return START_NOT_STICKY;
	}
	
	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}
	
	public void setHandler(Handler mHandlerOnCompletion) {
		this.handler = mHandlerOnCompletion;
		mediaManager.setHandler(mHandlerOnCompletion);
	}
	
	public void reUpdateDataFromClient(SongPlus songPlus, boolean imvRandomActivated, boolean imvRepeatActivated) {
		if (songPlus != null && songPlus.getSong() != null) {
			mediaManager.create(songPlus.getPosition());
			mediaManager.seekTo(songPlus.getPositionSeekBar());
		}
		mediaManager.setShuffle(imvRandomActivated);
		mediaManager.setLevelRepeat(imvRepeatActivated ? Constant.LEVEL_LOOPING_ALL : Constant.LEVEL_LOOPING_NONE);
	}
	
	public void create(int position) {
		mediaManager.create(position);
		updateNotification();
	}
	
	public void changeSong(int i) {
		mediaManager.changeSong(i);
	}
	
	public void start() {
		mediaManager.start();
	}
	
	public void pause() {
		mediaManager.pause();
	}
	
	public int getCurrentPlaceholder() {
		return mediaManager.getCurrentPlaceholder();
	}
	
	public String getSongName() {
		return mediaManager.getNameSong();
	}
	
	public String getArtistName() {
		return mediaManager.getArtist();
	}
	
	public boolean isShuffle() {
		return mediaManager.isShuffle();
	}
	
	public void setShuffle(boolean shuffle) {
		mediaManager.setShuffle(shuffle);
	}
	
	public boolean getShuffle() {
		return mediaManager.getShuffle();
	}
	
	public int getLevelRepeat() {
		return mediaManager.getLevelRepeat();
	}
	
	public void setLevelRepeat(int looping) {
		mediaManager.setLevelRepeat(looping);
	}
	
	public boolean isPlaying() {
		return mediaManager.isPlaying();
	}
	
	public boolean isCreated() {
		return mediaManager.isCreated();
	}
	
	public void randomSong() {
		mediaManager.randomSong();
	}
	
	public int getCurrentSeekbar() {
		return mediaManager.getCurrentSeekBar();
	}
	
	public int getCurrentPosition() {
		return mediaManager.getCurrentPosition();
	}
	
	public int getDuration() {
		return mediaManager.getDuration();
	}
	
	public void seekTo(int newMsec) {
		mediaManager.seekTo(newMsec);
	}
	
	public String getAlbumCover() {
		return mediaManager.getAlbumCover();
	}
	
	public Song getCurrentSong() {
		return mediaManager.getCurrentSong();
	}
	
	public void setListSong(ArrayList<BaseModel> arrAudio) {
		mediaManager.setArrAudio(arrAudio);
	}
	
	public void setCurrentPosition(int position) {
		mediaManager.setCurrentPosition(position);
	}
	
	public class BinderMusic extends Binder {
		public MusicService getService() {
			return MusicService.this;
		}
	}
}
