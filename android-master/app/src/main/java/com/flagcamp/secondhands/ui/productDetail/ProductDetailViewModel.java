package com.flagcamp.secondhands.ui.productDetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.flagcamp.secondhands.model.CreateRequestResponse;
import com.flagcamp.secondhands.model.ProductResponse;
import com.flagcamp.secondhands.model.UpdateFavResponse;
import com.flagcamp.secondhands.repository.ProductRepository;

public class ProductDetailViewModel extends ViewModel {
    private int productId;
    private String idToken;
    private final ProductRepository repository;
    private final MutableLiveData<String> searchInput = new MutableLiveData<>();

    public ProductDetailViewModel(ProductRepository productRepository){
        this.repository = productRepository;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setSearchInput(String query){
        searchInput.setValue(query);
    }

    public LiveData<ProductResponse> getProducts(){
        return Transformations.switchMap(searchInput, repository::getProducts);
    }
    public LiveData<UpdateFavResponse> addFavProduct() {
        return repository.addFavProduct(idToken, productId);
    }
    public LiveData<CreateRequestResponse> createRequest(){
        return repository.createRequest(idToken, productId);
    }
    public LiveData<UpdateFavResponse> deleteFavProduct() {
//        DeleteCustomLiveData input = new DeleteCustomLiveData(idToken, productIdInput);
//        return Transformations.switchMap(input, repository::deleteFavProduct);
        return repository.deleteFavProduct(idToken, productId);
    }
}
