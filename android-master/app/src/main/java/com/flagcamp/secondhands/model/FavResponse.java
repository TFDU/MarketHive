package com.flagcamp.secondhands.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class FavResponse {

    public int num_of_favorites;

    public List<FavProduct> user_favorites;

    @Override
    public String toString() {
        return "FavResponse{" +
                "num_of_favorites='" + num_of_favorites + '\'' +
                ", user_favorites=" + user_favorites +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FavResponse that = (FavResponse) o;
        return Objects.equals(num_of_favorites, that.num_of_favorites) &&
                Objects.equals(user_favorites, that.user_favorites);
    }

    @Override
    public int hashCode() {
        return Objects.hash(num_of_favorites, user_favorites);
    }
}
