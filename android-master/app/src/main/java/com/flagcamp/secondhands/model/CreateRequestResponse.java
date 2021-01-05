package com.flagcamp.secondhands.model;

import java.util.Objects;

public class CreateRequestResponse {
    public String status;

    @Override
    public String toString() {
        return "CreateRequestResponse{" +
                "status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateRequestResponse that = (CreateRequestResponse) o;
        return Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status);
    }
}
