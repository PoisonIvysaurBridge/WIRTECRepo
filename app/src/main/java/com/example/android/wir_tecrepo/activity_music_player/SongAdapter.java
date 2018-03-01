package com.example.android.wir_tecrepo.activity_music_player;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.wir_tecrepo.R;

import java.util.ArrayList;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.MyViewHolder>{

    private List<Song> playList;
    private IPlaySongListener songListener;

    public SongAdapter(ArrayList<Song> modelList, IPlaySongListener songListener) {
        this.playList = modelList;
        this.songListener = songListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_item_layout, parent, false);

        return new MyViewHolder(itemView, this.songListener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Song model = this.playList.get(position);
        holder.songTxtView.setText(model.getSongName());
        holder.artistTxtView.setText(model.getArtist());
    }

    @Override
    public int getItemCount() {
        return playList.size();
    }





    // View Holder helper class

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView songTxtView;
        public TextView artistTxtView;

        private IPlaySongListener songListener;

        public MyViewHolder(View itemView, final IPlaySongListener songListener) {
            super(itemView);

            this.songTxtView = itemView.findViewById(R.id.song_title_txt);
            this.artistTxtView = itemView.findViewById(R.id.artist_name_txt);
            this.songListener = songListener;

            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    songListener.onPlayRequested(MyViewHolder.this.getAdapterPosition());
                }
            });
        }

            /*
            editResto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Activity activity = (Activity) itemView.getContext();
                    Intent intent = new Intent(activity, AddRestaurant.class);

                    // Make a bundle containing the current restaurant details
                    Bundle bundle = new Bundle();
                    bundle.putInt(AddRestaurant.EDIT_MODEL_INDEX_KEY, modelIndex);
                    bundle.putString(AddRestaurant.RESTAURANT_NAME_KEY, name.getText().toString());
                    bundle.putString(AddRestaurant.RESTAURANT_DESC_KEY, desc.getText().toString());
                    bundle.putDouble(AddRestaurant.WEIGHT_KEY, Double.parseDouble(weight.getText().toString()));
                    // Edit the restaurant item
                    intent.putExtras(bundle);
                    activity.startActivityForResult(intent, RestaurantActivity.EDIT_RESTO_REQUEST);
                }
            });
            */
    }
}

