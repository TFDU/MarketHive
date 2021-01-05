package com.flagcamp.secondhands.model;

import java.util.Objects;

public class OrderRequest {
    public int requestId;
    public User user;
    public Product product;
    public String status;
    public String timestamp;
    public String timezoneId;

    public OrderRequest(){
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderRequest orderRequest = (OrderRequest) o;
        return Objects.equals(requestId, orderRequest.requestId) &&
                Objects.equals(user, orderRequest.user) &&
                Objects.equals(product, orderRequest.product) &&
                Objects.equals(status, orderRequest.status) &&
                Objects.equals(timestamp, orderRequest.timestamp) &&
                Objects.equals(timezoneId, orderRequest.timezoneId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId, user, product, status, timestamp, timezoneId);
    }

    @Override
    public String toString() {
        return "Request{" +
                "requestId='" + requestId + '\'' +
                ", user=" + user +
                ", product=" + product +
                ", status='" + status + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", timezoneId='" + timezoneId + '\'' +
                '}';
    }
}
