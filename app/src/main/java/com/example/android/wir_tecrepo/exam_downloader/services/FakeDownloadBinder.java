package com.example.android.wir_tecrepo.exam_downloader.services;

import android.os.Binder;

public class FakeDownloadBinder extends Binder {
    private final static String TAG = "FakeDownloadBinder";

    private FakeDownloadService service;

    public FakeDownloadBinder(FakeDownloadService service) {
        this.service = service;
    }

    public FakeDownloadService getService() {
        return this.service;
    }
}
