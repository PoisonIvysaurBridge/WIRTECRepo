package com.example.android.wir_tecrepo;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ChopinTime extends AppCompatActivity{
    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chopin_time);

        mediaPlayer = MediaPlayer.create(this, R.raw.chopin_winter_wind);

        Button playButton = (Button) findViewById(R.id.play_button);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this, "Play", Toast.LENGTH_SHORT).show();
                mediaPlayer.start();
            }
        });

        Button pauseButton = (Button) findViewById(R.id.pause_button);

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this, "Pause", Toast.LENGTH_SHORT).show();
                mediaPlayer.pause();
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Toast.makeText(ChopinTime.this, "finish playing", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
