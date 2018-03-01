package com.example.android.wir_tecrepo.activity_music_player;

public class Song {
    private long songID;
    private String songName;
    private String artist;

    public Song(long songID, String songName, String artist) {
        this.songID = songID;
        this.songName = songName;
        this.artist = artist;
    }


    public long getSongID() {
        return songID;
    }

    public String getSongName() {
        return songName;
    }

    public String getArtist() {
        return artist;
    }
}
