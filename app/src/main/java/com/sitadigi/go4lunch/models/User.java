package com.sitadigi.go4lunch.models;

import org.jetbrains.annotations.Nullable;

public class User {

    private String uid;
    private String username;
    private String email;
    @Nullable
    private String urlPicture;
    private String userRestoId;
    private String userRestoName;
    private  String userRestoType;

   // public User() {
    //}

    public User(String uid, String username, String email,
                @Nullable String urlPicture) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.urlPicture = urlPicture;
        this.userRestoId = "NoRestoChoice";
    }

    public User(String uid, String username, String email, @Nullable String urlPicture,
                String userRestoId, String userRestoName, String userRestoType) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.urlPicture = urlPicture;
        this.userRestoId = userRestoId;
        this.userRestoName = userRestoName;
        this.userRestoType = userRestoType;
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

    public String getUserRestoId() {
        return userRestoId;
    }

    public String getUserRestoName() {
        return userRestoName;
    }

    public String getUserRestoType() {
        return userRestoType;
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

    public void setUserRestoId(String userRestoId) {
        this.userRestoId = userRestoId;
    }

    public void setUserRestoName(String userRestoName) {
        this.userRestoName = userRestoName;
    }

    public void setUserRestoType(String userRestoType) {
        this.userRestoType = userRestoType;
    }
}
