package com.example.sev_user.musicplayer.manager;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import com.example.sev_user.musicplayer.constant.Constant;
import com.example.sev_user.musicplayer.model.BaseModel;
import com.example.sev_user.musicplayer.model.Song;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by sev_user on 7/18/2016.
 */
public class MediaManager implements MediaPlayer.OnCompletionListener {
    private Context context;
    private ArrayList<BaseModel> arrAudio;
    private boolean shuffle = false;
    private int currentPosition;
    private MediaPlayer media;
    private int levelRepeat = Constant.LEVEL_LOOPING_NONE;
    private Handler handler;

    public MediaManager(Context context, ArrayList<BaseModel> arrAudio) {
        this.context = context;
        this.arrAudio = arrAudio;
        currentPosition = arrAudio.size() > 2 ? 2 : 0;
        create(currentPosition);
    }

    public void create(int position) {
        if (arrAudio.get(currentPosition).getTypeModel() == BaseModel.TYPE_SONG) {
            currentPosition = position;
            if (media != null) {
                media.reset();
                media.release();
            }
            media = MediaPlayer.create(context, Uri.parse(((Song) arrAudio.get(currentPosition)).getData()));
            if (media != null) {
                media.setOnCompletionListener(this);
            } else {
                changeSong(1);
            }
        }
    }

    public void changeSong(int i) {
        currentPosition += i;
        if (currentPosition >= arrAudio.size()) {
            currentPosition = 2;
        } else if (currentPosition < 2) {
            currentPosition = arrAudio.size() - 1;
        }
        if (arrAudio.get(currentPosition).getTypeModel() == BaseModel.TYPE_HEADER) {
            currentPosition += i;
        }
        create(currentPosition);
        start();
    }

    public void start() {
        if (media != null) {
            media.start();
        }
    }

    public void pause() {
        if (media != null) {
            media.pause();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (levelRepeat == Constant.LEVEL_LOOPING_ONE) {
            create(currentPosition);
            start();
            return;
        }
        if (shuffle) {
            randomSong();
        } else {
            changeSong(1);
        }
        if (handler != null) {
            Message message = new Message();
            message.what = Constant.WHAT_UPDATE_UI;
            handler.sendMessage(message);
        }
    }

    public void randomSong() {
        Random random = new Random();
        int randomNumber = random.nextInt(arrAudio.size());
        if (arrAudio.get(randomNumber).getTypeModel() == BaseModel.TYPE_HEADER) {
            randomNumber++;
        }
        if (randomNumber == 1) {
            randomNumber++;
        }
        create(randomNumber);
        start();
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public int getDuration() {
        return media.getDuration();
    }

    public boolean isShuffle() {
        return shuffle;
    }

    public void setShuffle(boolean shuffle) {
        this.shuffle = shuffle;
    }

    public boolean isPlaying() {
        return media.isPlaying();
    }

    public boolean isCreated() {
        return media != null;
    }

    public void seekTo(int newMsec) {
        media.seekTo(newMsec);
    }

    public String getNameSong() {
        return ((Song) arrAudio.get(currentPosition)).getName();
    }

    public String getArtist() {
        return ((Song) arrAudio.get(currentPosition)).getArtist();
    }

    public String getAlbumCover() {
        return ((Song) arrAudio.get(currentPosition)).getImage();
    }

    public boolean getShuffle() {
        return shuffle;
    }

    public int getLevelRepeat() {
        return levelRepeat;
    }

    public void setLevelRepeat(int levelRepeat) {
        this.levelRepeat = levelRepeat;
    }

    public int getCurrentPlaceholder() {
        return ((Song) arrAudio.get(currentPosition)).getPlaceHolder();
    }

    public void setHandler(Handler mHandlerOnCompletion) {
        this.handler = mHandlerOnCompletion;
    }

    public String getCurrentImageUri() {
        return ((Song) arrAudio.get(currentPosition)).getImage();
    }

    public void reset() {
        media.reset();
    }

    public void release() {
        media.release();
    }

    public Song getCurrentSong() {
        return arrAudio.get(currentPosition) instanceof Song ? (Song) arrAudio.get(currentPosition) : null;
    }

    public int getCurrentSeekBar() {
        return media.getCurrentPosition();
    }

    public void setArrAudio(ArrayList<BaseModel> arrAudio) {
        this.arrAudio = arrAudio;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }
}
