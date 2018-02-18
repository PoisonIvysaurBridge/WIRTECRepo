package com.example.android.wir_tecrepo.activity_restaurant;

/**
 * {@link Restaurant} represents a restaurant that the user wants to add into the restaurant list.
 * It contains a restaurant name, a restaurant description, and importance weight.
*/

public class Restaurant {
    /** name for the restaurant */
    private String mRestaurantName;

    /** description for the restaurant */
    private String mRestaurantDesc;

    /** weight for the restaurant */
    private double mRestaurantWeight = 0;

    /**
     * Create a new Restaurant object.
     *
     * @param restaurantName is the name of the restaurant
     * @param restaurantDesc is the description of the restaurant
     * @param restaurantWeight is the weight that the user wishes to put
     *                          (e.g. 10 for favorite restaurants, 5 for default weight,
     *                          1 for less liked restaurants, etc.)
     */
    public Restaurant(String restaurantName, String restaurantDesc, double restaurantWeight) {
        mRestaurantName = restaurantName;
        mRestaurantDesc = restaurantDesc;
        mRestaurantWeight = restaurantWeight;
    }

    /**
     * Get the name of the restaurant.
     */
    public String getmRestaurantName() {
        return mRestaurantName;
    }

    /**
     * Set the name of the restaurant.
     */
    public void setmRestaurantName(String mRestaurantName) {
        this.mRestaurantName = mRestaurantName;
    }

    /**
     * Get the description of the restaurant.
     */
    public String getmRestaurantDesc() {
        return mRestaurantDesc;
    }

    /**
     * Set the description of the restaurant.
     */
    public void setmRestaurantDesc(String mRestaurantDesc) {
        this.mRestaurantDesc = mRestaurantDesc;
    }

    /**
     * Get the weight for the restaurant.
     */
    public double getmRestaurantWeight() {
        return mRestaurantWeight;
    }

    /**
     * Set the weight for the restaurant.
     */
    public void setmRestaurantWeight(double mRestaurantWeight) {
        this.mRestaurantWeight = mRestaurantWeight;
    }
}
