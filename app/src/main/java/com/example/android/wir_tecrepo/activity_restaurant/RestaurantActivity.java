package com.example.android.wir_tecrepo.activity_restaurant;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.widget.TextView;

import com.example.android.wir_tecrepo.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RestaurantActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    /** The request code for the add new restaurant intent */   static final int ADD_RESTO_REQUEST = 0;
    /** The request code for the editing restaurant intent */   static final int EDIT_RESTO_REQUEST = 1;

    /** The list containing restaurant objects */               private List<Restaurant> restaurants = new ArrayList<>();
    /** The recycler view containing the restaurant items */    private RecyclerView mRecyclerView;
    /** The adapter used for the recycler view */               private RestaurantAdapter mAdapter;
    /** The layout manager for the recycler view */             private RecyclerView.LayoutManager mLayoutManager;
    /** The layout for the snackbar with undo delete */         private ConstraintLayout constraintLayout;
    /** TextView that is displayed when the list is empty */    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        // use a constraint layout for the delete snackbar with UNDO
        constraintLayout = findViewById(R.id.constraint_layout);

        // set visibility of the empty view to be GONE initially
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        mEmptyStateTextView.setVisibility(View.GONE);

        // setup the recycler view adapter, layout, etc.
        prepareRecyclerView();

        // add restaurant items into the restaurants list
        prepareRestaurants();

        // prepare the buttons in the UI
        prepareButtons();
    }

    /**
     * This method setups the recycler view
     * - setting the adapter
     * - setting the layout of the recycler view
     * - adding an item touch listener, etc.
     */
    public void prepareRecyclerView(){
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager for the recycler view
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // This draws a line separator for each row, but card views are used so no need for this
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

        /* Uncomment this if you want to make the entire list row to be clickable instead of an EDIT button
        // add a click listener to go to the restaurant details for editing an existing item
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
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
                //Toast.makeText(getApplicationContext(), "You Long pressed me!", Toast.LENGTH_SHORT).show();
            }
        }));
        */
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
     * This method setups the buttons to be displayed in the restaurant activity UI
     */
    public void prepareButtons(){

        // ADD Button to go to add a new restaurant activity
        Button add = (Button) findViewById(R.id.add_resto);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RestaurantActivity.this, AddRestaurant.class);
                startActivityForResult(intent, ADD_RESTO_REQUEST);
            }
        });

        // SURPRISE button to pick a random restaurant
        Button surprise = (Button) findViewById(R.id.surprise_resto);
        surprise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // EQUAL RANDOMNESS
                Collections.shuffle(restaurants);
                //mAdapter.notifyDataSetChanged(); //enable this to view the shuffling animation

                // WEIGHTED RANDOMNESS
                Restaurant chosenOne = doWeightedRandomness();
                if(chosenOne == null){
                    Snackbar.make(view, "You have no restaurants :(", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else{
                    Snackbar snackbar = Snackbar.make(view, "LET'S EAT AT... " + chosenOne.getmRestaurantName() + "!!!", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Action", null).show();

                    TextView snackbarActionTextView =  snackbar.getView().findViewById( android.support.design.R.id.snackbar_text );
                    snackbarActionTextView.setTextSize( 30 );
                    snackbarActionTextView.setTypeface(snackbarActionTextView.getTypeface(), Typeface.BOLD);
                }
            }
        });

        // CLEAR Button to go to add a new restaurant activity
        Button clear = (Button) findViewById(R.id.clear_resto);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restaurants.clear();
                //mAdapter.notifyDataSetChanged();
                mRecyclerView.setVisibility(View.GONE);
                mEmptyStateTextView.setText("No Restaurants. :(");
                mEmptyStateTextView.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * This method generates a weighted list of restaurants, based on the given weight of a restaurant.
     * In the weighted list, there will repeated restaurant objects to give the weighted probability
     * that a higher weighted restaurant will have a higher probability of being picked.
     *
     * @return Restaurant object
     */
    public Restaurant doWeightedRandomness(){
        // this is the weighted list
        List<Restaurant> weightedRestos = new ArrayList<>();

        // loop around all the restaurants in the restaurants list
        for (int i = 0; i < restaurants.size(); i++) {
            // get the weight of each restaurant
            double weight = restaurants.get(i).getmRestaurantWeight();

            // keep on adding that restaurant according to its weight to the weighted list
            for(int j = 0; j < weight; j++) {
                weightedRestos.add(restaurants.get(i));
            }
        }
        if(restaurants.size() > 0){
            // the weighted list will have the equal probability of getting a restaurant
            // based on how many times it occured in the weighted list
            int randomNum = new Random().nextInt(weightedRestos.size());
            return weightedRestos.get(randomNum);
        }
        return null;
    }
    /**
     * This method gets the sub activity results
     * @param requestCode is the request code sent to the new intent (add or edit restaurant codes)
     * @param resultCode is the code that indicates if the result was successful or not
     * @param data is the intent data that was passed back from the sub activity
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_RESTO_REQUEST) { // if the user presses the ADD button
            if (resultCode == RESULT_OK) {  // if the user presses submit instead of the cancel button
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    String restoName = bundle.getString(AddRestaurant.RESTAURANT_NAME_KEY);
                    String restoDesc = bundle.getString(AddRestaurant.RESTAURANT_DESC_KEY);
                    Double restoWeight = bundle.getDouble(AddRestaurant.WEIGHT_KEY, 0.0);
                    restaurants.add(0, new Restaurant(restoName, restoDesc, restoWeight));
                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mEmptyStateTextView.setVisibility(View.GONE);
                }
            }
        } else if (requestCode == EDIT_RESTO_REQUEST) { // if the user clicks on a restaurant in the list
            if (resultCode == RESULT_OK) { // if the user presses submit instead of the cancel button
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    Integer position = bundle.getInt(AddRestaurant.EDIT_MODEL_INDEX_KEY, -1);
                    String restoName = bundle.getString(AddRestaurant.RESTAURANT_NAME_KEY);
                    String restoDesc = bundle.getString(AddRestaurant.RESTAURANT_DESC_KEY);
                    Double restoWeight = bundle.getDouble(AddRestaurant.WEIGHT_KEY, 0.0);

                    if(position >= 0) {
                        restaurants.get(position).setmRestaurantName(restoName);
                        restaurants.get(position).setmRestaurantDesc(restoDesc);
                        restaurants.get(position).setmRestaurantWeight(restoWeight);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
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
            if(restaurants.size() == 0 ){
                mRecyclerView.setVisibility(View.GONE);
                mEmptyStateTextView.setText("No Restaurants. :(");
                mEmptyStateTextView.setVisibility(View.VISIBLE);
            }

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(constraintLayout, name + " removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    mAdapter.restoreItem(deletedItem, deletedIndex);
                    mEmptyStateTextView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);

            snackbar.show();
        }
    }
}
