package com.flagcamp.secondhands.model;

import java.util.List;
import java.util.Objects;

public class MyPostResponse {
    public String status;
    public String summary;
    public List<Product> products_of_user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyPostResponse that = (MyPostResponse) o;
        return Objects.equals(status, that.status) &&
                Objects.equals(summary, that.summary) &&
                Objects.equals(products_of_user, that.products_of_user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, summary, products_of_user);
    }

    @Override
    public String toString() {
        return "MyPostResponse{" +
                "status='" + status + '\'' +
                ", summary='" + summary + '\'' +
                ", products_of_user=" + products_of_user +
                '}';
    }
}
