package com.example.android.wir_tecrepo.activity_restaurant;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.wir_tecrepo.R;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.MyViewHolder> {

    private List<Restaurant> restaurantList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, desc, weight;
        //public final ImageButton moreButt;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.resto_name);
            desc = (TextView) view.findViewById(R.id.resto_desc);
            weight = (TextView) view.findViewById(R.id.resto_weight);
            //moreButt = (ImageButton) view.findViewById(R.id.moreButton);

            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
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
        // to perform recyc
        // r view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Restaurant item, int position) {
        restaurantList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }
}




//
//import android.content.Context;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * {@link RestaurantAdapter} is an {@link ArrayAdapter} that can provide the layout for each list item
// * based on a data source, which is a list of {@link Restaurant} objects.
// */
//
//public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {
//
//    private ArrayList<Restaurant> mRestaurants;
//
//    // Provide a reference to the views for each data item
//    // Complex data items may need more than one view per item, and
//    // you provide access to all the views for a data item in a view holder
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        // each data item is just a string in this case
//        public View mView;
//        public TextView mName;
//        public TextView mDesc;
//        public TextView mWeight;
//        public ViewHolder(View v, TextView name, TextView desc, TextView weight) {
//            super(v);
//            mName = name;
//            mDesc = desc;
//            mWeight = weight;
//        }
//    }
//    /**
//     * Create a new {@link Restaurant} object.
//     *
//     * @param restaurants is the list of {@link Restaurant}s to be displayed.
//     */
//    public RestaurantAdapter(ArrayList<Restaurant> restaurants) {
//        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
//        // the second argument is used when the ArrayAdapter is populating a single TextView.
//        // Because this is a custom adapter for three TextViews, the adapter is not
//        // going to use this second argument, so it can be any value. Here, we used 0.
//        // create a new view
//        mRestaurants = restaurants;
//    }
//
//    // Create new views (invoked by the layout manager)
//    @Override
//    public RestaurantAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
//                                                   int viewType) {
//        // create a new view
//        View listItemView = LayoutInflater.from(parent.getContext()).inflate(
//                R.layout.restaurant_list_item, parent, false);
//
//        TextView name = (TextView) listItemView.findViewById(R.id.resto_name);
//
//        TextView desc = (TextView) listItemView.findViewById(R.id.resto_desc);
//
//        TextView weight = (TextView) listItemView.findViewById(R.id.resto_desc);
//
//        // set the view's size, margins, paddings and layout parameters
//        ViewHolder vh = new ViewHolder(listItemView, name, desc, weight);
//        return vh;
//
//
//    }
//
//    // Replace the contents of a view (invoked by the layout manager)
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        // - get element from your dataset at this position
//        // - replace the contents of the view with that element
//
//        holder.mName.setText(mRestaurants.get(position).getmRestaurantName());
//
//        holder.mDesc.setText(mRestaurants.get(position).getmRestaurantDesc());
//
//        holder.mWeight.setText("" +mRestaurants.get(position).getmRestaurantWeight());
//
//    }
//
//    // Return the size of your dataset (invoked by the layout manager)
//    @Override
//    public int getItemCount() {
//        return mRestaurants.size();
//    }
//
//}