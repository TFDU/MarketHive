package com.flagcamp.secondhands.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class MapSearchResponse {
    public String coordinates;
    public List<Product> available_products_nearby;

    @Override
    public String toString() {
        return "MapSearchResponse{" +
                "coordinates='" + coordinates + '\'' +
                ", available_products_nearby=" + available_products_nearby +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapSearchResponse that = (MapSearchResponse) o;
        return Objects.equals(coordinates, that.coordinates) &&
                Objects.equals(available_products_nearby, that.available_products_nearby);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinates, available_products_nearby);
    }
}
