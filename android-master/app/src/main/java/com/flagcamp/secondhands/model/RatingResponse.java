package com.flagcamp.secondhands.model;

import java.util.Objects;

public class RatingResponse {
    public String status;
    public Order order;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RatingResponse that = (RatingResponse) o;
        return status.equals(that.status) &&
                Objects.equals(order, that.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, order);
    }

    @Override
    public String toString() {
        return "RatingResponse{" +
                "status='" + status + '\'' +
                ", order=" + order +
                '}';
    }
}
