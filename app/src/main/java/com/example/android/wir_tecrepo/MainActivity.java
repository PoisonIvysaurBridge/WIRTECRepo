package com.example.android.wir_tecrepo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.wir_tecrepo.activity_lock.LockerActivity;
import com.example.android.wir_tecrepo.activity_music_player.MusicPlayerActivity;
import com.example.android.wir_tecrepo.activity_restaurant.RestaurantActivity;
import com.example.android.wir_tecrepo.miscellaneous.ChopinTime;
import com.example.android.wir_tecrepo.miscellaneous.CookieActivity;
import com.example.android.wir_tecrepo.miscellaneous.CourtCounter;
import com.example.android.wir_tecrepo.miscellaneous.HappyBirthdayCard;
import com.example.android.wir_tecrepo.miscellaneous.JustJava;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private final static int PERMISSION_READ_EXTERNAL_STORAGE = 0;
    SharedPreferences prefs;
    private static final String MUSIC_FIRST_TIME_KEY = "MUSIC_FIRST_TIME_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefs = this.getSharedPreferences("com.example.android",MODE_PRIVATE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "This app was made by Ivana Koon Yee U. Lim", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                TextView textView = (TextView) findViewById(R.id.name);
                textView.setText("Ivy Lim");
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.first_activity) {
            Intent intent = new Intent(MainActivity.this, FirstActivity.class);
            startActivity(intent);
            /*
            MenuItem firstAct = (MenuItem) findViewById(R.id.first_activity);
            firstAct.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    return false;

                }
            });
            */

        } else if (id == R.id.lifecycle_activity) {
            Intent intent = new Intent(MainActivity.this, LifecycleActivity.class);
            startActivity(intent);

        } else if (id == R.id.restaurant_activity) {
            Intent intent = new Intent(MainActivity.this, RestaurantActivity.class);
            startActivity(intent);

        } else if (id == R.id.music_player_activity) {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[PERMISSION_READ_EXTERNAL_STORAGE])){
                    Toast.makeText(this, "Permission is required to access media stored on your device", Toast.LENGTH_SHORT).show();
                    this.requestPermissions(permissions, PERMISSION_READ_EXTERNAL_STORAGE);
                }else {
                    if(prefs.getBoolean(MUSIC_FIRST_TIME_KEY, true)){
                        prefs.edit().putBoolean(MUSIC_FIRST_TIME_KEY, false).apply();
                        Toast.makeText(this, "Music Player launched for the first time!", Toast.LENGTH_SHORT).show();
                        this.requestPermissions(permissions, PERMISSION_READ_EXTERNAL_STORAGE);
                    }
                    else {
                        Toast.makeText(this, "Permission is disabled for this application!", Toast.LENGTH_SHORT).show();
                    }
                }
            }else {
                Toast.makeText(this, "Permission is already granted.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, MusicPlayerActivity.class);
                startActivity(intent);
            }

        } else if (id == R.id.lock_activity) {
            Intent intent = new Intent(MainActivity.this, LockerActivity.class);
            startActivity(intent);

        } else if (id == R.id.birthday_card) {
            Intent intent = new Intent(MainActivity.this, HappyBirthdayCard.class);
            startActivity(intent);

        } else if (id == R.id.eat_cookie) {
            Intent intent = new Intent(MainActivity.this, CookieActivity.class);
            startActivity(intent);

        } else if (id == R.id.court_counter) {
            Intent intent = new Intent(MainActivity.this, CourtCounter.class);
            startActivity(intent);

        } else if (id == R.id.just_java) {
            Intent intent = new Intent(MainActivity.this, JustJava.class);
            startActivity(intent);

        } else if (id == R.id.chopin_time) {
            Intent intent = new Intent(MainActivity.this, ChopinTime.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
