package com.flagcamp.secondhands.model;

import java.util.Objects;

public class Order {
    public int productOrderId;
    public Product product;
    public User customer;
    public double rating;
    public boolean confirmed;
    public String timestamp;
    public String timezoneId;

    public Order(){
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return productOrderId == order.productOrderId &&
                Double.compare(order.rating, rating) == 0 &&
                confirmed == order.confirmed &&
                product.equals(order.product) &&
                customer.equals(order.customer) &&
                Objects.equals(timestamp, order.timestamp) &&
                Objects.equals(timezoneId, order.timezoneId);
    }

    @Override
    public String toString() {
        return "Order{" +
                "productOrderId=" + productOrderId +
                ", product=" + product +
                ", customer=" + customer +
                ", rating=" + rating +
                ", confirmed=" + confirmed +
                ", timestamp='" + timestamp + '\'' +
                ", timezoneId='" + timezoneId + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(productOrderId, product, customer, rating, confirmed, timestamp, timezoneId);
    }



}
