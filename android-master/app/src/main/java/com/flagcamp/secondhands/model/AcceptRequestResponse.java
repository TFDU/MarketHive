package com.flagcamp.secondhands.model;

import java.util.Objects;

public class AcceptRequestResponse {
    public String status;
    public String message;
    public Order created_order;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AcceptRequestResponse that = (AcceptRequestResponse) o;
        return Objects.equals(status, that.status) &&
                Objects.equals(message, that.message) &&
                Objects.equals(created_order, that.created_order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, message, created_order);
    }

    @Override
    public String toString() {
        return "AcceptRequestResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", created_order=" + created_order +
                '}';
    }
}
