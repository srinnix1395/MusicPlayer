package com.example.sev_user.musicplayer.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.example.sev_user.musicplayer.R;
import com.example.sev_user.musicplayer.activity.SplashActivity;
import com.example.sev_user.musicplayer.constant.Constant;
import com.example.sev_user.musicplayer.manager.MediaManager;
import com.example.sev_user.musicplayer.model.Song;
import com.example.sev_user.musicplayer.model.SongPlus;
import com.example.sev_user.musicplayer.provider.MusicContentProvider;
import com.example.sev_user.musicplayer.utils.SharedPreUtil;

/**
 * Created by sev_user on 7/18/2016.
 */
public class MusicService extends Service {
    private static final String TAG = "MusicService";
    private MediaManager mediaManager;
    private BinderMusic binder = new BinderMusic();
    private boolean isPlayingService = true;
    private Notification notification;
    private Handler handler;

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
                    isPlayingService = false;
                    SharedPreUtil.getInstance(MusicService.this).putBoolean(Constant.IS_PLAYING_SERVICE, false);
                    break;
                }
                case Constant.ACTION_NEW_TASK:{
                    if (handler != null) {
                        Message message = new Message();
                        message.what = Constant.WHAT_NEW_TASK;
                        handler.sendMessage(message);
                    }
                    Intent intent1 = new Intent(context, SplashActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent1);
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
        mediaManager = new MediaManager(this, contentProvider.getArrSong());
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
        SharedPreUtil.getInstance(this).putBoolean(Constant.IS_PLAYING_SERVICE, true);
    }

    @Override
    public void onDestroy() {
        if (isRegistered) {
            unregisterReceiver(receiver);
            isRegistered = false;
        }
        SharedPreUtil.getInstance(this).putBoolean(Constant.IS_PLAYING_SERVICE, false);
        super.onDestroy();
    }

    private void runForeground() {
        startNotification();
        startForeground(Constant._ID_FOREGROUND_SERVICE, notification);
    }

    public void updateNotification() {
        startNotification();

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(Constant._ID_FOREGROUND_SERVICE, notification);
    }

    private void startNotification() {
        notification = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_note).build();

        RemoteViews bigViews = new RemoteViews(getPackageName(), R.layout.notification_big_view_music);
        bigViews.setTextViewText(R.id.tvName, mediaManager.getNameSong());
        bigViews.setTextViewText(R.id.tvArtist, mediaManager.getArtist());

        Intent intentClose = new Intent(Constant.ACTION_CLOSE);
        PendingIntent piClose = PendingIntent.getBroadcast(this, 0, intentClose, PendingIntent.FLAG_UPDATE_CURRENT);
        bigViews.setOnClickPendingIntent(R.id.imvClose, piClose);


        bigViews.setImageViewResource(R.id.imvPlay, mediaManager.isPlaying() ? R.drawable.ic_pause_48_gray : R.drawable.ic_play_gray_48);
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

        notification.bigContentView = bigViews;

        RemoteViews views = new RemoteViews(getPackageName(), R.layout.notification_view_music);
        views.setTextViewText(R.id.tvName, mediaManager.getNameSong());
        views.setTextViewText(R.id.tvArtist, mediaManager.getArtist());
        views.setOnClickPendingIntent(R.id.imvPlay, piPlay);
        views.setOnClickPendingIntent(R.id.imvPre, piPrev);
        views.setOnClickPendingIntent(R.id.imvNext, piNext);
        views.setImageViewResource(R.id.imvPlay, mediaManager.isPlaying() ? R.drawable.ic_pause_48_gray : R.drawable.ic_play_gray_48);
        views.setOnClickPendingIntent(R.id.imvClose, piClose);

        if (mediaManager.getAlbumCover() == null) {
            views.setImageViewResource(R.id.imvCover, mediaManager.getCurrentPlaceholder());
        } else {
            views.setImageViewUri(R.id.imvCover, Uri.parse(mediaManager.getCurrentImageUri()));
        }

        notification.contentView = views;

//        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, SplashActivity.class), 0);
        Intent intentNewTask = new Intent(Constant.ACTION_NEW_TASK);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, intentNewTask, 0);
        notification.contentIntent = pi;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        runForeground();
        if (!isPlayingService) {
            return START_NOT_STICKY;
        }
        return START_STICKY;
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
        if (songPlus.getSong() != null) {
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
        return mediaManager.getCurrentSeekbar();
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

    public class BinderMusic extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }
}
