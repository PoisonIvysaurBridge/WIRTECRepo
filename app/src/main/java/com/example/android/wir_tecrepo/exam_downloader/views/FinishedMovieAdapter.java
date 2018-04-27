package com.example.android.wir_tecrepo.exam_downloader.views;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.wir_tecrepo.R;
import com.example.android.wir_tecrepo.exam_downloader.models.MovieModel;

public class FinishedMovieAdapter extends RecyclerView.Adapter<FinishedMovieViewHolder>{
    private final static String TAG = "FinishedMovieAdapter";

    private MovieModel[] finishedList;

    public FinishedMovieAdapter(MovieModel[] finishedList) {
        this.finishedList = finishedList;
    }

    @Override
    public FinishedMovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewInstance = LayoutInflater.from(parent.getContext()).inflate(R.layout.finished_download_item, parent, false);
        return new FinishedMovieViewHolder(viewInstance);
    }

    @Override
    public void onBindViewHolder(FinishedMovieViewHolder holder, int position) {
        MovieModel movieModel = this.finishedList[position];
        holder.getMovieTitleTxt().setText(movieModel.getName());
        holder.getDescText().setText(movieModel.getDescription());
    }

    @Override
    public int getItemCount() {
        return this.finishedList.length;
    }
}
