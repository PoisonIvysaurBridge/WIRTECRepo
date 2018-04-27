package com.example.android.wir_tecrepo.exam_downloader.listeners;


import com.example.android.wir_tecrepo.exam_downloader.models.MovieModel;
import com.example.android.wir_tecrepo.exam_downloader.services.FakeDownloadService;

/**
 * Holds list of listeners to be used for triggered events.
 */

public class MovieDownloadPackage {
    public interface IDownloadListener {
        void onDownloadInitiated(int index);
    }

    public interface IFinishedListener {
        void onDownloadFinished(MovieModel movieModel, FakeDownloadService service);
    }
}
