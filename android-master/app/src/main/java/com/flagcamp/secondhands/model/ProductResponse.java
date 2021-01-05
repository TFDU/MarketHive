/*
This is a class I use to encapsulate product objects.
Will be used in Class ProductRepository, whose method getProducts will be called in HomeViewModel and ProductDetailViewModel

Basically it has 8 fields, the first one is a list containing all the product objects,
the others are lists containing product objects of a certain categories.

I build it because my homepage implementation needs data of product objects of all categories seperated
 */
package com.flagcamp.secondhands.model;

import java.util.List;
import java.util.Objects;

public class ProductResponse {
    public String status;
    public String display_limit;
    public ProductOfCategories products_by_category;


    public ProductResponse(String status, String display_limit, ProductOfCategories products_by_category) {
        this.status = status;
        this.display_limit = display_limit;
        this.products_by_category = products_by_category;
    }

    public List<Product> getCat(int index){


        if(index == 0){
            return  products_by_category.clothing_and_shoes;
        }else if(index == 1){
            return products_by_category.beauty_and_health;
        }else if(index == 2){
            return products_by_category.electronics;
        }else if(index == 3){
            return products_by_category.household;
        }else if(index == 4){
            return products_by_category.baby_and_kids;
        }else if(index == 5){
            return products_by_category.car;
        }else{
            return products_by_category.furniture;
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductResponse that = (ProductResponse) o;
        return Objects.equals(status, that.status) &&
                Objects.equals(display_limit, that.display_limit) &&
                Objects.equals(products_by_category, that.products_by_category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, display_limit, products_by_category);
    }

    @Override
    public String toString() {
        return "ProductResponse{" +
                "status='" + status + '\'' +
                ", display_limit='" + display_limit + '\'' +
                ", productOfCatagories=" + products_by_category +
                '}';
    }
}

