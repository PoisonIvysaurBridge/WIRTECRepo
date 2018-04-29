package com.example.android.wir_tecrepo.exam_downloader;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.example.android.wir_tecrepo.R;
import com.example.android.wir_tecrepo.exam_downloader.listeners.MovieDownloadPackage;
import com.example.android.wir_tecrepo.exam_downloader.models.MovieModel;
import com.example.android.wir_tecrepo.exam_downloader.models.MovieRepository;
import com.example.android.wir_tecrepo.exam_downloader.services.DownloadFinishedNotification;
import com.example.android.wir_tecrepo.exam_downloader.services.FakeDownloadBinder;
import com.example.android.wir_tecrepo.exam_downloader.services.FakeDownloadService;
import com.example.android.wir_tecrepo.exam_downloader.views.DownloadingMovieAdapter;
import com.example.android.wir_tecrepo.exam_downloader.views.FinishedMovieAdapter;
import com.example.android.wir_tecrepo.exam_downloader.views.OngoingMovieViewHolder;

import java.util.ArrayList;

public class DownloaderActivity extends AppCompatActivity implements MovieDownloadPackage.IFinishedListener {
    private final static String TAG = "DownloaderActivity";

    private RecyclerView downloadingView;
    private DownloadingMovieAdapter downloadingMovieAdapter;

    private RecyclerView finishedView;
    private FinishedMovieAdapter finishedMovieAdapter;

    private MovieModel[] downloadingList;
    private MovieModel[] finishedList;

    private ArrayList<FakeDownloadService> downloadServices = new ArrayList<>();
    private ArrayList<ServiceConnection> downloadConnections = new ArrayList<>();
    private ArrayList<Intent> downloadIntents = new ArrayList<>();

    private int notifCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloader);

        this.setupDownloadingList();
        this.setupFinishedList();
        this.setupBtns();
    }

    @Override
    protected void onDestroy() {
        this.destroyService();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.refreshLists();
        this.handleDownloads();
    }

    private void setupBtns() {
        Button downloadListBtn = this.findViewById(R.id.download_btn);
        downloadListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DownloaderActivity.this, MovieListActivity.class);
                startActivity(i);
            }
        });
    }

    private void destroyService() {
        for(int i = 0; i < this.downloadIntents.size(); i++) {
            this.stopService(this.downloadIntents.get(i));
        }
        for(int i = 0; i < this.downloadConnections.size(); i++) {
            this.unbindService(this.downloadConnections.get(i)); //IMPORTANT! DO NOT FORGET
        }


        this.downloadConnections.clear();
        this.downloadIntents.clear();

        Log.d(TAG, "Services destroyed!");
    }

    private void setupDownloadingList() {
        this.downloadingList = MovieRepository.getInstance().getDownloadingMovies();
        this.downloadingView = this.findViewById(R.id.ongoing_view);
        this.downloadingMovieAdapter = new DownloadingMovieAdapter(this.downloadingList);
        RecyclerView.LayoutManager recylerLayoutManager = new LinearLayoutManager(this);
        this.downloadingView.setLayoutManager(recylerLayoutManager);
        this.downloadingView.setItemAnimator(new DefaultItemAnimator());
        this.downloadingView.setAdapter(this.downloadingMovieAdapter);
    }

    private void setupFinishedList() {
        this.finishedList = MovieRepository.getInstance().getFinishedMovies();
        this.finishedView = this.findViewById(R.id.finished_view);
        this.finishedMovieAdapter = new FinishedMovieAdapter(this.finishedList);
        RecyclerView.LayoutManager recylerLayoutManager = new LinearLayoutManager(this);
        this.finishedView.setLayoutManager(recylerLayoutManager);
        this.finishedView.setItemAnimator(new DefaultItemAnimator());
        this.finishedView.setAdapter(this.finishedMovieAdapter);
    }

    private void refreshLists() {
        this.downloadingMovieAdapter = null;
        this.finishedMovieAdapter = null;

        this.downloadingList = MovieRepository.getInstance().getDownloadingMovies();
        this.downloadingMovieAdapter = new DownloadingMovieAdapter(this.downloadingList);
        this.downloadingView.setAdapter(this.downloadingMovieAdapter);

        this.finishedList = MovieRepository.getInstance().getFinishedMovies();
        this.finishedMovieAdapter = new FinishedMovieAdapter(this.finishedList);
        this.finishedView.setAdapter(this.finishedMovieAdapter);
    }

    private void handleDownloads() {
        final MovieModel movieModel = MovieRepository.getInstance().getLatestDownloadableMovie();
        //Toast.makeText(this,"in handle downloads", Toast.LENGTH_SHORT).show();
        if(movieModel != null) {
            //start a new download service
            ServiceConnection downloadConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder iBinder) {
                    FakeDownloadBinder binder = (FakeDownloadBinder) iBinder;
                    FakeDownloadService downloadService = binder.getService();
                    OngoingMovieViewHolder viewHolder = (OngoingMovieViewHolder) downloadingView.findViewHolderForAdapterPosition(movieModel.getViewPosition());
                    downloadService.setMovieToDownload(movieModel, viewHolder, DownloaderActivity.this, DownloaderActivity.this);
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                }
            };

            this.downloadConnections.add(downloadConnection);

            Intent downloadIntent = new Intent(this, FakeDownloadService.class);
            this.downloadIntents.add(downloadIntent);
            this.bindService(downloadIntent, downloadConnection, Context.BIND_AUTO_CREATE);
            this.startService(downloadIntent);
        }
    }

    @Override
    public void onDownloadFinished(MovieModel movieModel, FakeDownloadService service) {
        MovieRepository.getInstance().markMovieFinished(movieModel.getViewPosition());
        this.refreshLists();

        Intent i = new Intent(this, MovieViewActivity.class);
        i.putExtra(MovieViewActivity.MOVIE_TITLE_KEY, movieModel.getName());
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                i, PendingIntent.FLAG_UPDATE_CURRENT);

        DownloadFinishedNotification.notify(this, movieModel.getName(), this.notifCounter, pendInt);
        this.notifCounter++;
    }
}
