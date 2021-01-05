package com.flagcamp.secondhands.ui.order;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.flagcamp.secondhands.CurrentUserSingleton;
import com.flagcamp.secondhands.model.DeleteRequestResponse;
import com.flagcamp.secondhands.model.MyPostResponse;
import com.flagcamp.secondhands.model.MyRequest;
import com.flagcamp.secondhands.model.Order;
import com.flagcamp.secondhands.model.OrderPost;
import com.flagcamp.secondhands.model.OrderRequest;
import com.flagcamp.secondhands.model.OrderResponse;
import com.flagcamp.secondhands.model.Product;
import com.flagcamp.secondhands.model.RequestResponse;
import com.flagcamp.secondhands.repository.ProductRepository;

import java.util.List;

public class OrderViewModel extends ViewModel {
    private final ProductRepository repository;
    //private String idToken;
    private final MutableLiveData<String> idToken = new MutableLiveData<>();

    public void setIdToken() {
        this.idToken.setValue(CurrentUserSingleton.getInstance().getIdToken());
    }

    public OrderViewModel(ProductRepository repository) {
        this.repository = repository;
    }


    public LiveData<OrderResponse> getAllSaleHistoryLive() {
        //Log.d("Check Null", Transformations.switchMap(idToken, repository::getSellerOrder).toString());
        return Transformations.switchMap(idToken, repository::getSellerOrder);
    }

    public LiveData<OrderResponse> getAllOrderHistoryLive() {
        Log.d("Check Null", Transformations.switchMap(idToken, repository::getBuyerOrder).toString());
        return Transformations.switchMap(idToken, repository::getBuyerOrder);
    }

    public void rateOrder(Order order, float rating){
        repository.rateOrder(order.productOrderId, CurrentUserSingleton.getInstance().getIdToken(), (double)rating);
    }

    public LiveData<RequestResponse> getBuyerRequestLive() {
        Log.d("Check Null", Transformations.switchMap(idToken, repository::getBuyerRequest).toString());
        return Transformations.switchMap(idToken, repository::getBuyerRequest);
    }

    public LiveData<RequestResponse> getSellerRequestLive() {
        Log.d("Check Null", Transformations.switchMap(idToken, repository::getSellerRequest).toString());
        return Transformations.switchMap(idToken, repository::getSellerRequest);
    }

    public void declineRequestLive(OrderRequest request) {
        repository.declineRequest(request.requestId, CurrentUserSingleton.getInstance().getIdToken());
    }

    public void deleteRequestLive(OrderRequest request) {
        repository.deleteRequest(request.requestId, CurrentUserSingleton.getInstance().getIdToken());
    }

    public void acceptRequestLive(OrderRequest request) {
        repository.acceptRequest(request.requestId, CurrentUserSingleton.getInstance().getIdToken());
    }

    public LiveData<MyPostResponse> getPostRequest() {
        Log.d("Check Null", Transformations.switchMap(idToken, repository::getPostRequest).toString());
        return Transformations.switchMap(idToken, repository::getPostRequest);
    }

    public void deletePostRequest(Product product) {
        repository.deletePostRequest(product.productId, CurrentUserSingleton.getInstance().getIdToken());
    }
}
