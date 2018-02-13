package com.example.android.wir_tecrepo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RestaurantActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    /** The request code for the add new restaurant intent */   static final int ADD_RESTO_REQUEST = 0;
    /** The request code for the editing restaurant intent */   static final int EDIT_RESTO_REQUEST = 1;

    /** The list containing restaurant objects */               private List<Restaurant> restaurants = new ArrayList<>();
    /** The recycler view containing the restaurant items */    private RecyclerView mRecyclerView;
    /** The adapter used for the recycler view */               private RestaurantAdapter mAdapter;
    /** The layout manager for the recycler view */             private RecyclerView.LayoutManager mLayoutManager;
    /** The layout for the snackbar with undo delete */         private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        constraintLayout = findViewById(R.id.constraint_layout);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // This draws a line separator for each row
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // specify an adapter (see also next example)
        mAdapter = new RestaurantAdapter(restaurants);
        mRecyclerView.setAdapter(mAdapter);

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);

        // add restaurant items into the restaurants list
        prepareRestaurants();

        // updates the restaurant list UI
        mAdapter.notifyDataSetChanged();

        // Add Button to go to add a new restaurant activity
        Button add = (Button) findViewById(R.id.add_resto);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RestaurantActivity.this, AddRestaurant.class);
                startActivityForResult(intent, ADD_RESTO_REQUEST);
            }
        });

        // Shuffle button to pick a random restaurant
        Button surprise = (Button) findViewById(R.id.surprise_resto);
        surprise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.shuffle(restaurants);
                mAdapter.notifyDataSetChanged();
                Snackbar.make(view, "LET'S EAT AT... " + restaurants.get(0).getmRestaurantName() + "!!!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // add a click listener to go to the restaurant details for editing an existing item
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Restaurant restaurant = restaurants.get(position);
                // Toast.makeText(getApplicationContext(), restaurant.getmRestaurantName() + " is selected!", Toast.LENGTH_SHORT).show();

                // Make a bundle containing the current restaurant details
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                bundle.putString("restoName", restaurants.get(position).getmRestaurantName());
                bundle.putString("restoDesc", restaurants.get(position).getmRestaurantDesc());
                bundle.putDouble("restoWeight", restaurants.get(position).getmRestaurantWeight());
                // Edit the restaurant item
                Intent intent = new Intent(RestaurantActivity.this, AddRestaurant.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, EDIT_RESTO_REQUEST);
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(getApplicationContext(), "You Long pressed me!", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    /**
     * This method gets the sub activity results
     * @param requestCode is the request code sent to the new intent (add or edit restaurant codes)
     * @param resultCode is the code that indicates if the result was successful or not
     * @param data is the intent data that was passed back from the sub activity
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_RESTO_REQUEST) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    String restoName = bundle.getString("restoName");
                    String restoDesc = bundle.getString("restoDesc");
                    Double restoWeight = bundle.getDouble("restoWeight", 0.0);
                    restaurants.add(0, new Restaurant(restoName, restoDesc, restoWeight));
                    mAdapter.notifyDataSetChanged();
                }
            }
        } else if (requestCode == EDIT_RESTO_REQUEST) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    Integer position = bundle.getInt("position");
                    String restoName = bundle.getString("restoName");
                    String restoDesc = bundle.getString("restoDesc");
                    Double restoWeight = bundle.getDouble("restoWeight", 0.0);
                    restaurants.get(position).setmRestaurantName(restoName);
                    restaurants.get(position).setmRestaurantDesc(restoDesc);
                    restaurants.get(position).setmRestaurantWeight(restoWeight);
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    /**
     * This method adds the dummy restaurant data into the restaurants list.
     */
    private void prepareRestaurants() {
        restaurants.add(new Restaurant("Pericos", "Canteen @ LS building DLSU", 5));
        restaurants.add(new Restaurant("La Casita @ 6th Andrew", "Canteen @ Andrew building DLSU", 9));
        restaurants.add(new Restaurant("La Casita @ 2nd Razon", "Canteen @ Razon building DLSU", 3));

        restaurants.add(new Restaurant("first resto", "Canteen @ LS building DLSU", 5));
        restaurants.add(new Restaurant("second resto", "Canteen @ Andrew building DLSU", 9));
        restaurants.add(new Restaurant("third resto", "Canteen @ Razon building DLSU", 3));
        restaurants.add(new Restaurant("fourth resto", "Canteen @ LS building DLSU", 5));
        restaurants.add(new Restaurant("5th rest", "Canteen @ Andrew building DLSU", 9));
        restaurants.add(new Restaurant("6th resto", "Canteen @ Razon building DLSU", 3));
        restaurants.add(new Restaurant("seventh resto", "Canteen @ LS building DLSU", 5));
        restaurants.add(new Restaurant("eighth resto", "Canteen @ Andrew building DLSU", 9));
        restaurants.add(new Restaurant("9th", "Canteen @ Razon building DLSU", 3));

    }

    /**
     * callback when recycler view is swiped
     * item will be removed on swiped
     * undo option will be provided in snackbar to restore the item
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof RestaurantAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String name = restaurants.get(viewHolder.getAdapterPosition()).getmRestaurantName();

            // backup of removed item for undo purpose
            final Restaurant deletedItem = restaurants.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(constraintLayout, name + " removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    mAdapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}
