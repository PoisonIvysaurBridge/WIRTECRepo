package com.example.android.wir_tecrepo;

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.wir_tecrepo.activity_music_player.IPlaySongListener;
import com.example.android.wir_tecrepo.activity_music_player.MusicController;
import com.example.android.wir_tecrepo.activity_music_player.MusicPlayerControl;
import com.example.android.wir_tecrepo.activity_music_player.Song;
import com.example.android.wir_tecrepo.activity_music_player.SongAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MusicPlayer2Activity extends AppCompatActivity implements IPlaySongListener {
    private final static String TAG = "MusicPlayerActivity";
    private final static int PERMISSION_READ_EXTERNAL_STORAGE = 1;

    private ArrayList<Song> songList;
    private RecyclerView songView;
    private SongAdapter songAdapter;

    private MusicPlayerService musicSrv;
    private Intent playIntent;
    private boolean musicBound = false;
    private ServiceConnection musicConnection;

    private MusicPlayer2Control musicPlayerControl;
    private MusicPlayer2Controller controller;

    private TextView titleView;
    private TextView artistView;

    public boolean paused = false, playbackPaused = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player_2);
        songView = (RecyclerView) findViewById(R.id.play_list);
        songList = new ArrayList<Song>();

        // request permissions
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READ_EXTERNAL_STORAGE);

        setupMusicService();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(playIntent == null){
            playIntent = new Intent(this, MusicPlayerService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        paused = true;
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(paused){
            setController();
            paused = false;
        }
    }

    @Override
    protected void onStop() {
        controller.hide();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        stopService(playIntent);
        musicSrv = null;
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.music2_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_shuffle:
                Collections.shuffle(songList);
                if(this.musicSrv != null) {
                    this.musicSrv.setSong(0);
                    this.musicSrv.playSong();
                    setController();

                    //this.startForegroundNotif(0);
                } else {Log.e(TAG, "Music service is not properly setup!");}
                break;
            case R.id.action_end:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Log.d(TAG, "Permission granted!");
                // permission was granted, yay!
                this.getSongList();
                this.setupUI();
            } else {
                // permission denied, boo!
                this.finish();
            }
        }
    }

    public void getSongList() {
        //retrieve song info
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                songList.add(new Song(thisId, thisTitle, thisArtist));
            }
            while (musicCursor.moveToNext());
        }

        // sort songs alphabetically
        Collections.sort(songList, new Comparator<Song>(){
            public int compare(Song a, Song b){
                return a.getSongName().compareTo(b.getSongName());
            }
        });
    }

    private void setupUI() {
        this.titleView = this.findViewById(R.id.song_title_txt);
        this.artistView = this.findViewById(R.id.artist_txt);

        this.titleView.setText("Select a song");
        this.artistView.setText("");

        this.songView = this.findViewById(R.id.play_list);
        this.songAdapter = new SongAdapter(this.songList, this);
        RecyclerView.LayoutManager recylerLayoutManager = new LinearLayoutManager(this);
        this.songView.setLayoutManager(recylerLayoutManager);
        this.songView.setItemAnimator(new DefaultItemAnimator());
        this.songView.setAdapter(this.songAdapter);

    }

    @Override
    public void onPlayRequested(int songIndex) {
        if(musicSrv != null) {
            musicSrv.setSong(songIndex);
            musicSrv.playSong();
            if(playbackPaused){
                setController();
                playbackPaused = false;
            }
            setController();

            //this.startForegroundNotif(songIndex);
        }
        else {
            Log.e(TAG, "Music service is not properly setup!");
        }
    }

    @Override
    public void onSongUpdated(int songIndex) {

    }

    private void setupMusicService() {
        //connect to the service
        musicConnection = new ServiceConnection(){

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MusicPlayerService.MusicBinder binder = (MusicPlayerService.MusicBinder)service;
                //get service
                musicSrv = binder.getService();
                //pass list
                musicSrv.setList(songList, MusicPlayer2Activity.this);
                musicBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                musicBound = false;
            }
        };
    }

    private void setController(){
        musicPlayerControl = new MusicPlayer2Control(this, musicSrv);
        //set the controller up
        controller = new MusicPlayer2Controller(this);
        controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        });

        controller.setMediaPlayer(musicPlayerControl);
        controller.setAnchorView(findViewById(R.id.media_control_layout));
        controller.setEnabled(true);
        controller.show(0); //0 means screen will be persistent
    }

    public boolean isMusicBound() {
        return this.musicBound;
    }

    private void playNext(){
        musicSrv.playNext();
        if(playbackPaused){
            setController();
            playbackPaused = false;
        }
        controller.show(0);
    }

    //play previous
    private void playPrev(){
        musicSrv.playPrev();
        if(playbackPaused){
            setController();
            playbackPaused = false;
        }
        controller.show(0);
    }
}
