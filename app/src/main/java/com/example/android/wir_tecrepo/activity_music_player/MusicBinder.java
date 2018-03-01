package com.example.android.wir_tecrepo.activity_music_player;

import android.os.Binder;

public class MusicBinder extends Binder {

    private MusicService musicService;
    public MusicBinder(MusicService musicService) {
        this.musicService = musicService;
    }

    public MusicService getService() {
        return this.musicService;
    }
}
