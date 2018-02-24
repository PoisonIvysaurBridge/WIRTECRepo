package com.example.android.wir_tecrepo.activity_lock;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.android.wir_tecrepo.R;

import java.util.ArrayList;
import java.util.List;

public class LockerAdapter extends RecyclerView.Adapter<LockerAdapter.LockerViewHolder> {
    private final static String TAG = "S16_RestaurantAdapter";

    private List<LockerDataModel> modelList;

    public LockerAdapter(ArrayList<LockerDataModel> modelList) {
        this.modelList = modelList;
    }

    @Override
    public LockerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //TODO: modify this
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.locker_item, parent, false);

        return new LockerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LockerViewHolder holder, int position) {
        //TODO: modify this
        LockerDataModel lock = modelList.get(position);
        holder.itemView.setText(lock.getNumberString());
    }

    @Override
    public int getItemCount() {
        return this.modelList.size();
    }



    public class LockerViewHolder extends RecyclerView.ViewHolder {

        private TextView itemView;

        public LockerViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView.findViewById(R.id.locker_key_view);
        }

        public TextView getLockerKeyView() {
            return this.itemView;
        }
    }
}
