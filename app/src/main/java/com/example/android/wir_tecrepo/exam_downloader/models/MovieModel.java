package com.example.android.wir_tecrepo.exam_downloader.models;

/**
 * Represents the movie model
 */

public class MovieModel {
    private final static String TAG = "MovieModel";

    private String name, description;
    private int viewPosition = 0;

    public MovieModel(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void setViewPosition(int index) {
        this.viewPosition = index;
    }

    public int getViewPosition() {
        return this.viewPosition;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }
}
