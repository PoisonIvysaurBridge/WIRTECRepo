package com.example.android.wir_tecrepo.activity_music_player;

public interface IPlaySongListener {
    void onPlayRequested(int songIndex);
    void onSongUpdated(int songIndex);
}