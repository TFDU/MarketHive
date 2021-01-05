package com.flagcamp.secondhands.model;

import java.io.Serializable;

public class User implements Serializable {
    public String userUID;
    public String name;
    public String photoUrl;
    public String email;
    public String rating;

    public User() {

    }

    public User(String userId, String name) {
        this.userUID = userId;
        this.name = name;
    }

    public User(String userId, String name, String photoUrl, String email, String rating) {
        this.userUID = userId;
        this.name = name;
        this.photoUrl = photoUrl;
        this.email = email;
        this.rating = rating;
    }
}
