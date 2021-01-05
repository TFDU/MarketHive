package com.flagcamp.secondhands.ui.post;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.flagcamp.secondhands.model.PostProductResponse;
import com.flagcamp.secondhands.model.Product;
import com.flagcamp.secondhands.model.SearchResponse;
import com.flagcamp.secondhands.repository.ProductRepository;
import com.flagcamp.secondhands.ui.search.SearchViewModel;

public class PostViewModel extends ViewModel {
    private final ProductRepository repository;
    private String idToken;
    private Product product;

    public PostViewModel(ProductRepository repository) {
        this.repository = repository;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public LiveData<PostProductResponse> postProduct() {
        return repository.postProduct(idToken, product);
    }
}
