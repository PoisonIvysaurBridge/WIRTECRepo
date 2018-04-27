package com.example.android.wir_tecrepo.exam_downloader.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.wir_tecrepo.R;


public class OngoingMovieViewHolder extends RecyclerView.ViewHolder {
    private final static String TAG = "OngoingMovieViewHolder";

    private TextView movieTitleTxt;
    private TextView descText;
    private ProgressBar progressBar;

    public OngoingMovieViewHolder(View view) {
        super(view);

        this.movieTitleTxt = view.findViewById(R.id.movie_title_txt);
        this.descText = view.findViewById(R.id.desc_txt);
        this.progressBar = view.findViewById(R.id.download_progress_bar);
    }


    public TextView getMovieTitleTxt() {
        return movieTitleTxt;
    }

    public TextView getDescText() {
        return descText;
    }

    public void initializeBar(int currentValue, int maxValue) {
        this.progressBar.setMax(maxValue);
        this.progressBar.setProgress(currentValue);
    }

    public void updateProgress(int value) {
        this.progressBar.setProgress(value);
    }
}
