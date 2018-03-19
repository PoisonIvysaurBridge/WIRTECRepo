package com.example.android.wir_tecrepo;

import android.widget.MediaController;

public class MusicPlayer2Control implements MediaController.MediaPlayerControl{
    private MusicPlayer2Activity musicPlayerActivity;
    private MusicPlayerService musicService;

    public MusicPlayer2Control(MusicPlayer2Activity musicPlayer2Activity, MusicPlayerService musicService) {
        this.musicPlayerActivity = musicPlayer2Activity;
        this.musicService = musicService;
    }

    @Override
    public void start() {
        if(this.musicService != null) {
            this.musicService.go();
        }
    }

    @Override
    public void pause() {
        if(this.musicService != null) {
            musicPlayerActivity.paused = true;
            this.musicService.pausePlayer();
        }
    }

    @Override
    public int getDuration() {
        if(musicService != null && musicPlayerActivity.isMusicBound() && musicService.isPng()) {
            return musicService.getPosn();
        }
        else {
            return 0;
        }
    }

    @Override
    public int getCurrentPosition() {
        if(this.musicService != null && musicPlayerActivity.isMusicBound() && musicService.isPng()) {
            return musicService.getPosn();
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
        if(musicService != null) {
            return musicService.isPng();
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
}
