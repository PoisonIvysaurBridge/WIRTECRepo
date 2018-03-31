package com.example.android.wir_tecrepo.activity_music_player;

import android.widget.MediaController;


public class MusicPlayerControl implements MediaController.MediaPlayerControl {

    private MusicPlayerActivity  musicPlayerActivity;
    private MusicService musicService;

    private boolean paused = false, playbackPaused = false;

    public MusicPlayerControl(MusicPlayerActivity musicPlayerActivity, MusicService musicService) {
        this.musicPlayerActivity = musicPlayerActivity;
        this.musicService = musicService;
    }
    @Override
    public void start() {
        if(this.musicService != null) {
            this.musicService.resume();
        }
    }

    @Override
    public void pause() {
        if(this.musicService != null) {
            playbackPaused = true;
            musicPlayerActivity.setPlaybackPaused(playbackPaused);
            this.musicService.pause();
        }
    }

    @Override
    public int getDuration() {
        if(this.musicService != null && this.musicPlayerActivity.isMusicBound() && this.musicService.isPlaying()) {
            return this.musicService.getCurrentSongDuration();
        }
        else {
            return 0;
        }
    }

    @Override
    public int getCurrentPosition() {
        if(this.musicService != null && this.musicPlayerActivity.isMusicBound() && this.musicService.isPlaying()) {
            return this.musicService.getCurrentSongPosition();
        }
        else {
            return 0;
        }
    }

    @Override
    public void seekTo(int pos) {
        if(this.musicService != null) {
            this.musicService.seek(pos);
        }
    }

    @Override
    public boolean isPlaying() {
        if(this.musicService != null) {
            return this.musicService.isPlaying();
        }
        else {
            return false;
        }
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean isPlaybackPaused() {
        return playbackPaused;
    }

    public void setPlaybackPaused(boolean playbackPaused) {
        this.playbackPaused = playbackPaused;
    }
}
