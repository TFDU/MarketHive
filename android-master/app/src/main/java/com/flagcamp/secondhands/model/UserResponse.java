package com.flagcamp.secondhands.model;

import com.google.gson.annotations.SerializedName;

public class UserResponse {
    public String status;
    @SerializedName("updated_user")
    public User user;
}