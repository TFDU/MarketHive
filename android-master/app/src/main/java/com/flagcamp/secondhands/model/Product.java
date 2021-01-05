/*
This class is the product model including all the fields we need.
basic methods including constructer(), toString(), equals(), and hashCode() are already generated.
TODO(NOT NECESSARY): 1. change location field and category field to enum.
                    2. replace constructer with a builder
 */

package com.flagcamp.secondhands.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Product implements Serializable {
    public int productId;
    public User user;
    public String productName;
    public String category;
    public String price;
    public boolean availability;
    public String description;
    public String imageUrls;
    public String timestamp;
    public String timezoneId;
    public double lat;
    public double lon;
    public String state;
    public String city;
    public boolean gpsenabled;
    public String location;
    public boolean favorite;

    public Product(){
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return productId == product.productId &&
                availability == product.availability &&
                Double.compare(product.lat, lat) == 0 &&
                Double.compare(product.lon, lon) == 0 &&
                gpsenabled == product.gpsenabled &&
                favorite == product.favorite &&
                Objects.equals(user, product.user) &&
                Objects.equals(productName, product.productName) &&
                Objects.equals(category, product.category) &&
                Objects.equals(price, product.price) &&
                Objects.equals(description, product.description) &&
                Objects.equals(imageUrls, product.imageUrls) &&
                Objects.equals(timestamp, product.timestamp) &&
                Objects.equals(timezoneId, product.timezoneId) &&
                Objects.equals(state, product.state) &&
                Objects.equals(city, product.city) &&
                Objects.equals(location, product.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, user, productName, category, price, availability, description, imageUrls, timestamp, timezoneId, lat, lon, state, city, gpsenabled, location, favorite);
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", seller=" + user +
                ", productName='" + productName + '\'' +
                ", category='" + category + '\'' +
                ", price='" + price + '\'' +
                ", availability=" + availability +
                ", description='" + description + '\'' +
                ", imageUrls=" + imageUrls +
                ", timestamp='" + timestamp + '\'' +
                ", timezoneId='" + timezoneId + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", gpsenabled=" + gpsenabled +
                ", location='" + location + '\'' +
                ", favorite=" + favorite +
                '}';
    }
}

