package com.example.android.wir_tecrepo;

public interface IPlaySongListener {
    void onPlayRequested(int songIndex);
    void onSongUpdated(int songIndex);
}