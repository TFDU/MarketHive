package com.flagcamp.secondhands.model;

import java.util.List;
import java.util.Objects;

public class SearchResponse {
    public String summary;
    public List<Product> products;
    public String page;
    public String page_size;
    public String status;

    @Override
    public String toString() {
        return "NewsResponse{" +
                "summary=" + summary +
                ", products=" + products +
                ", page='" + page + '\'' +
                ", page_size='" + page_size + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchResponse that = (SearchResponse) o;
        return Objects.equals(summary, that.summary) &&
                Objects.equals(products, that.products) &&
                Objects.equals(page, that.page) &&
                Objects.equals(page_size, that.page_size) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(summary, products, page, page_size, status);
    }
}
