package com.sitadigi.go4lunch.models;

import org.jetbrains.annotations.Nullable;

public class User {

    private String uid;
    private String username;
    private String email;
    @Nullable
    private String urlPicture;

    public User() {
    }

    public User(String uid, String username, String email,
                @Nullable String urlPicture) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.urlPicture = urlPicture;
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
}
