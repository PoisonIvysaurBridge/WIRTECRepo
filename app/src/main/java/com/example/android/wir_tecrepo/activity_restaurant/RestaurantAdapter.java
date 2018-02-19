package com.example.android.wir_tecrepo.activity_restaurant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.wir_tecrepo.R;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.MyViewHolder> {

    private List<Restaurant> restaurantList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, desc, weight;
        public Button editResto;
        //public final ImageButton moreButt;
        private int modelIndex = -1;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.resto_name);
            desc = (TextView) view.findViewById(R.id.resto_desc);
            weight = (TextView) view.findViewById(R.id.resto_weight);
            editResto = (Button) view.findViewById(R.id.edit_resto);
            //moreButt = (ImageButton) view.findViewById(R.id.moreButton);

            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);

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
        }
    }


    public RestaurantAdapter(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.test, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Restaurant restaurant = restaurantList.get(position);
        holder.name.setText(restaurant.getmRestaurantName());
        holder.desc.setText(restaurant.getmRestaurantDesc());
        holder.weight.setText("" + restaurant.getmRestaurantWeight());
        holder.modelIndex = position;

        /*
        holder.moreButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //creating a popup menu
                PopupMenu popup = new PopupMenu(this,holder.moreButt);
                //inflating menu from xml resource
                popup.inflate(R.menu.options_menu);
                //adding click listener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu1:
                            //handle menu1 click
                                break;
                            case R.id.menu2:
                                //handle menu2 click
                                break;
                            case R.id.menu3:
                                //handle menu3 click
                                break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();

            }
        });*/
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    public void removeItem(int position) {
        restaurantList.remove(position);
        // notify the item removed by position
        // to perform recycling
        // for view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Restaurant item, int position) {
        restaurantList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }
}