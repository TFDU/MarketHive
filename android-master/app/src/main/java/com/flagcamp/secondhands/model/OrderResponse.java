package com.flagcamp.secondhands.model;

import java.util.List;
import java.util.Objects;

public class OrderResponse {
    public String status;
    public String summary;
    public List<Order> orders;


    @Override
    public String toString() {
        return "NewsResponse{" +
                "summary=" + summary +
                ", orders=" + orders +
                ", status='" + status + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderResponse that = (OrderResponse) o;
        return Objects.equals(summary, that.summary) &&
                Objects.equals(orders, that.orders) &&
                Objects.equals(status, that.status);
    }


    @Override
    public int hashCode() {
        return Objects.hash(summary, orders, status);
    }

}