package com.example.android.wir_tecrepo.exam_downloader.services;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.android.wir_tecrepo.exam_downloader.listeners.MovieDownloadPackage;
import com.example.android.wir_tecrepo.exam_downloader.models.MovieModel;
import com.example.android.wir_tecrepo.exam_downloader.views.OngoingMovieViewHolder;


public class FakeDownloadService extends Service {
    private final static String TAG = "FakeDownloadService";

    private FakeDownloadBinder binder;

    public FakeDownloadService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return this.binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        this.binder = new FakeDownloadBinder(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        this.binder = null;
        return super.onUnbind(intent);
    }

    public void setMovieToDownload(final MovieModel movieModel, final OngoingMovieViewHolder movieViewHolder, final MovieDownloadPackage.IFinishedListener finishedListener, final Activity activity) {
        ThreadAction threadAction = new ThreadAction() {
            @Override
            public void onProgress(final int currentProgress) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        movieViewHolder.updateProgress(currentProgress);
                        if(currentProgress >= 100 && finishedListener != null) {
                            finishedListener.onDownloadFinished(movieModel, FakeDownloadService.this);
                        }
                    }
                });
            }
        };

        movieViewHolder.initializeBar(0, 100);
        BackgroundThread backgroundThread = new BackgroundThread(threadAction);
        backgroundThread.start();
    }

    private interface ThreadAction {
        void onProgress(int currentProgress);
    }

    /*
     * Background thread that is used for simulating a fake download through thread.sleep()
     */
    private class BackgroundThread extends Thread {
        private ThreadAction threadAction;

        public BackgroundThread(ThreadAction threadAction) {
            this.threadAction = threadAction;
        }

        @Override
        public void run() {
            try {
                int currentProgress = 0;
                for(currentProgress = 0; currentProgress <= 100; currentProgress+=5) {
                    Thread.sleep(250);
                    this.threadAction.onProgress(currentProgress);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
