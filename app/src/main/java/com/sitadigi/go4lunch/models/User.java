package com.sitadigi.go4lunch.models;

import org.jetbrains.annotations.Nullable;

public class User {

    private String uid;
    private String username;
    private String email;
    @Nullable
    private String urlPicture;
    private String userRestaurantId;
    private String userRestaurantName;
    private  String userRestaurantType;



    public User(String uid, String username, String email,
                @Nullable String urlPicture) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.urlPicture = urlPicture;
        this.userRestaurantId = "NoRestoChoice";
    }

    public User(String uid, String username, String email, @Nullable String urlPicture,
                String userRestaurantId, String userRestaurantName, String userRestaurantType) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.urlPicture = urlPicture;
        this.userRestaurantId = userRestaurantId;
        this.userRestaurantName = userRestaurantName;
        this.userRestaurantType = userRestaurantType;
    }
//-------------------GETTER----------------------------------------

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    @Nullable
    public String getUrlPicture() {
        return urlPicture;
    }

    public String getUserRestaurantId() {
        return userRestaurantId;
    }

    public String getUserRestaurantName() {
        return userRestaurantName;
    }

    public String getUserRestaurantType() {
        return userRestaurantType;
    }
    //------------------SETTER----------------------------------------

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUrlPicture(@Nullable String urlPicture) {
        this.urlPicture = urlPicture;
    }

    public void setUserRestaurantId(String userRestaurantId) {
        this.userRestaurantId = userRestaurantId;
    }

    public void setUserRestaurantName(String userRestaurantName) {
        this.userRestaurantName = userRestaurantName;
    }

    public void setUserRestaurantType(String userRestaurantType) {
        this.userRestaurantType = userRestaurantType;
    }
}
