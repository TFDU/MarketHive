package com.flagcamp.secondhands;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;

public class CurrentUserSingleton {
    private static CurrentUserSingleton instance = new CurrentUserSingleton();
    private String idToken;
    private String userId;
    private String userName;
    private String photoUrl;
    private String email;
    private String rating;

    private CurrentUserSingleton() {}

    public static CurrentUserSingleton getInstance() {
        return instance;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getIdToken() {

        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}
