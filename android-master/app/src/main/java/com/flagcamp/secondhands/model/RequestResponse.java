package com.flagcamp.secondhands.model;

import java.util.List;
import java.util.Objects;

public class RequestResponse {
    public String status;
    public String summary;
    public List<OrderRequest> requests;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestResponse that = (RequestResponse) o;
        return Objects.equals(status, that.status) &&
                Objects.equals(summary, that.summary) &&
                Objects.equals(requests, that.requests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, summary, requests);
    }

    @Override
    public String toString() {
        return "RequestResponse{" +
                "status='" + status + '\'' +
                ", summary='" + summary + '\'' +
                ", Request=" + requests +
                '}';
    }
}
