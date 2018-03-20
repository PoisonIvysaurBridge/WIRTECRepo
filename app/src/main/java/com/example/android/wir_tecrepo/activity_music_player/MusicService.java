package com.example.android.wir_tecrepo.activity_music_player;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;

import java.io.IOException;
import java.util.ArrayList;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    private IPlaySongListener songListener;
    private MediaPlayer mediaPlayer;
    private MusicBinder musicBinder;
    private ArrayList<Song> playlist;

    private int currentSongIndex = 0;

    public void setPlaylist(ArrayList<Song> playlist, IPlaySongListener songListener) {
        this.playlist = playlist;
        this.songListener = songListener;
    }

    public void playSong() {
        this.mediaPlayer.reset();
        Song song = this.playlist.get(this.currentSongIndex);
        long songID = song.getSongID();

        //set uri
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                songID);

        try {
            this.mediaPlayer.setDataSource(this.getApplicationContext(), trackUri);
            this.mediaPlayer.prepareAsync();
            this.songListener.onSongUpdated(this.currentSongIndex);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void playNext() {
        if(this.currentSongIndex < this.playlist.size() - 1) {
            this.currentSongIndex++;
        }
        else {
            this.currentSongIndex = 0;
        }

        this.playSong();
    }

    public void playPrevious() {
        if(this.currentSongIndex > 0) {
            this.currentSongIndex--;
        }
        else {
            this.currentSongIndex = this.playlist.size() - 1;
        }

        this.playSong();
    }

    public void setSong(int index) {
        this.currentSongIndex = index;
    }

    public boolean isPlaying() {
        if(this.mediaPlayer != null) {
            return this.mediaPlayer.isPlaying();
        }
        else {
            return false;
        }
    }

    public int getCurrentSongDuration() {
        if(this.mediaPlayer != null) {
            return this.mediaPlayer.getDuration();
        }
        else {
            return 0;
        }
    }

    public int getCurrentSongPosition() {
        if(this.mediaPlayer != null) {
            return this.mediaPlayer.getCurrentPosition();
        }
        else {
            return 0;
        }

    }

    public void resume() {
        if(this.mediaPlayer != null) {
            this.mediaPlayer.start();
        }
    }

    public void pause() {
        if(this.mediaPlayer != null) {
            this.mediaPlayer.pause();
        }
    }

    public void seek(int newSec) {
        if(this.mediaPlayer != null) {
            this.mediaPlayer.seekTo(newSec);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.currentSongIndex = 0;
        this.mediaPlayer = new MediaPlayer();

        this.mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        this.mediaPlayer.setOnPreparedListener(this);
        this.mediaPlayer.setOnCompletionListener(this);
        this.mediaPlayer.setOnErrorListener(this);

        this.musicBinder = new MusicBinder(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return this.musicBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        this.mediaPlayer.stop();
        this.mediaPlayer.release();
        this.mediaPlayer = null;
        return super.onUnbind(intent);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        this.playNext();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }


}
