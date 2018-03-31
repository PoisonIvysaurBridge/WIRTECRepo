package com.example.android.wir_tecrepo.activity_music_player;

import android.Manifest;
import android.app.PendingIntent;
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
import android.widget.Toast;

import com.example.android.wir_tecrepo.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MusicPlayerActivity extends AppCompatActivity implements IPlaySongListener{
    private final static String TAG = "MusicPlayerActivity";
    private final static int PERMISSION_READ_EXTERNAL_STORAGE = 1;
    private final static int MUSIC_PLAY_NOTIFY_ID = 1;


    private ArrayList<Song> songList = new ArrayList<>();
    private RecyclerView songView;
    private SongAdapter songAdapter;

    private MusicService musicService;
    private Intent playIntent;
    private boolean musicBound=false;
    private ServiceConnection musicConnection;

    private MusicController musicController;
    private MusicPlayerControl musicPlayerControl;

    private TextView titleView;
    private TextView artistView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        // requesting of permissions can be done in the onCreate as well
        this.requestPermissions();
        // but for this app, the requesting of permissions is in the Main activity when
        // the user clicks on the music player menu option in the navigation menu

        this.setupMusicService();
        this.startMusicService();
    }

    @Override
    protected void onDestroy() {
        this.stopService(this.playIntent);
        this.unbindService(this.musicConnection); //IMPORTANT! DO NOT FORGET

        //IMPORTANT FOR RESETTING THE MUSIC PLAYER CONTROL
        if(musicController!= null){
            this.musicController.markForCleaning(true);
            this.musicController.hide();
            this.musicService.stopForeground(true);
            this.musicService = null;
            this.musicController = null;
            this.musicPlayerControl = null;
            Log.d(TAG, "Music service successfully stopped!");

        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.music_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_end) {
            this.finish();
        }
        else if(item.getItemId() == R.id.action_shuffle){
            //Collections.shuffle(songList);
            //songAdapter.notifyDataSetChanged();
            if(this.musicService != null) {
                this.musicService.setSong(new Random().nextInt(songList.size()));
                this.musicService.playSong();
                this.setupMusicController();

                this.startForegroundNotif(0);
            }
            else {
                Log.e(TAG, "Music service is not properly setup!");
            }
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
                this.loadSongsFromStorage();
                this.setupUI();


            } else {
                // permission denied, boo!
                this.finish();
            }
        }
    }

    @Override
    public void onPlayRequested(int songIndex) {
        if(this.musicService != null) {
            this.musicService.setSong(songIndex);
            this.musicService.playSong();
            this.setupMusicController();

            //this.startForegroundNotif(songIndex);
        }
        else {
            Log.e(TAG, "Music service is not properly setup!");
        }
    }

    @Override
    public void onSongUpdated(int songIndex) {
        Song song = this.songList.get(songIndex);
        this.titleView.setText(song.getSongName());
        this.artistView.setText(song.getArtist());
        this.startForegroundNotif(songIndex);
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                PERMISSION_READ_EXTERNAL_STORAGE);
    }

    private void loadSongsFromStorage() {
        ContentResolver musicResolver = this.getContentResolver();
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
    }

    private void startForegroundNotif(int songIndex) {
        Song song = this.songList.get(songIndex);
        String songName = song.getSongName();
        String songArtist = song.getArtist();
        // TODO notification channel
        /*
        Intent notIntent = new Intent(this, MusicPlayerActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                notIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            */
        MusicPlayNotification playNotification = new MusicPlayNotification();

        ///* code of Ms.
        Intent mainIntent = new Intent(this.musicService, MusicService.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendInt = PendingIntent.getActivity(this.musicService, 0,
                mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        this.musicService.startForeground(MUSIC_PLAY_NOTIFY_ID, playNotification.buildNotification(
                this, songName, songArtist, pendInt));
    }

    private void setupMusicService() {
        this.musicConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MusicService.MusicBinder binder = (MusicService.MusicBinder) service;

                MusicPlayerActivity.this.musicService = binder.getService();
                MusicPlayerActivity.this.musicService.setPlaylist(MusicPlayerActivity.this.songList, MusicPlayerActivity.this);
                MusicPlayerActivity.this.musicBound = true;
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {
                MusicPlayerActivity.this.musicBound = false;
            }
        };
    }

    private void startMusicService() {
        if(this.playIntent==null){
            this.playIntent = new Intent(this, MusicService.class);
            this.bindService(this.playIntent, this.musicConnection, Context.BIND_AUTO_CREATE);
            //this.startService(this.playIntent);

            Log.d(TAG, "Successfully setup service!");
        }
    }

    public boolean isMusicBound() {
        return this.musicBound;
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

    @Override   // NOT WORKING
    public void onBackPressed() {
        Toast.makeText(this, "BACK PRESSED!",Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }

    private void setupMusicController() {
        //setup the music controller

        if(this.musicPlayerControl != null) {
            this.musicController.markForCleaning(true);
            this.musicController.hide();
            this.musicController = null;
            this.musicPlayerControl = null;
        }

        this.musicPlayerControl = new MusicPlayerControl(this, this.musicService);
        this.musicController = new MusicController(this);
        this.musicController.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicService.playNext();
                musicController.show(0);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicService.playPrevious();
                musicController.show(0);
            }
        });

        this.musicController.setMediaPlayer(this.musicPlayerControl);
        this.musicController.setAnchorView(this.findViewById(R.id.media_control_layout));
        this.musicController.setEnabled(true);
        this.musicController.show(0); //0 means screen will be persistent
    }
}
