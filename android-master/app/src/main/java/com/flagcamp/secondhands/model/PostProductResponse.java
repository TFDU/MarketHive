package com.flagcamp.secondhands.model;

import java.util.Objects;

public class PostProductResponse {
    public String status;
    public Product product;

    @Override
    public String toString() {
        return "PostProductResponse{" +
                "status='" + status + '\'' +
                ", product=" + product +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostProductResponse that = (PostProductResponse) o;
        return Objects.equals(status, that.status) &&
                Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, product);
    }
}