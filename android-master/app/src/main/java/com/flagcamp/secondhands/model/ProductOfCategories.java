package com.flagcamp.secondhands.model;

import java.util.List;
import java.util.Objects;

public class ProductOfCategories {
    public List<Product> clothing_and_shoes;
    public List<Product> beauty_and_health;
    public List<Product> electronics;
    public List<Product> household;
    public List<Product> baby_and_kids;
    public List<Product> car;
    public List<Product> furniture;

    public ProductOfCategories(List<Product> clothing_and_shoes, List<Product> beauty_and_health, List<Product> electronics, List<Product> household, List<Product> baby_and_kids, List<Product> car, List<Product> furniture) {
        this.clothing_and_shoes = clothing_and_shoes;
        this.beauty_and_health = beauty_and_health;
        this.electronics = electronics;
        this.household = household;
        this.baby_and_kids = baby_and_kids;
        this.car = car;
        this.furniture = furniture;
    }

    @Override
    public String toString() {
        return "ProductOfCatagories{" +
                "clothing_and_shoes=" + clothing_and_shoes +
                ", beauty_and_health=" + beauty_and_health +
                ", electronics=" + electronics +
                ", household=" + household +
                ", baby_and_kids=" + baby_and_kids +
                ", car=" + car +
                ", furniture=" + furniture +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductOfCategories that = (ProductOfCategories) o;
        return Objects.equals(clothing_and_shoes, that.clothing_and_shoes) &&
                Objects.equals(beauty_and_health, that.beauty_and_health) &&
                Objects.equals(electronics, that.electronics) &&
                Objects.equals(household, that.household) &&
                Objects.equals(baby_and_kids, that.baby_and_kids) &&
                Objects.equals(car, that.car) &&
                Objects.equals(furniture, that.furniture);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clothing_and_shoes, beauty_and_health, electronics, household, baby_and_kids, car, furniture);
    }
}

