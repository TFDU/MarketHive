package com.flagcamp.secondhands.model;

import java.io.Serializable;

public class Category implements Serializable {
    public String category;

    public Category(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
