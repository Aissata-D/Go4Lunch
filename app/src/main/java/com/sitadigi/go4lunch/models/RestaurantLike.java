package com.sitadigi.go4lunch.models;

public class RestaurantLike {
    String restaurantName;

    public RestaurantLike(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }
}
