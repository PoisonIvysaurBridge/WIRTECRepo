package com.example.android.wir_tecrepo.exam_downloader.views;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.wir_tecrepo.R;
import com.example.android.wir_tecrepo.exam_downloader.models.MovieModel;


public class DownloadingMovieAdapter extends RecyclerView.Adapter<OngoingMovieViewHolder> {
    private final static String TAG = "MovieAdapter";

    private MovieModel[] downloadingList;

    public DownloadingMovieAdapter(MovieModel[] downloadingList) {
        this.downloadingList = downloadingList;
    }

    @Override
    public OngoingMovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewInstance = LayoutInflater.from(parent.getContext()).inflate(R.layout.ongoing_download_item, parent, false);
        return new OngoingMovieViewHolder(viewInstance);
    }

    @Override
    public void onBindViewHolder(OngoingMovieViewHolder holder, int position) {
        MovieModel movieModel = this.downloadingList[position];
        movieModel.setViewPosition(position);
        holder.getMovieTitleTxt().setText(movieModel.getName());
        holder.getDescText().setText(movieModel.getDescription());
    }

    @Override
    public int getItemCount() {
        return this.downloadingList.length;
    }
}
