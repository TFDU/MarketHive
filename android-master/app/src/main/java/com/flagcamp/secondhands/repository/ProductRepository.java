/*
This class is in charge of providing data.
METHOD getProducts return a livedata of ProductResponse, which containing a list of products.
In the future we need to call Apis implemented by our backend team to generate data.
For now I build a method called generateDummyProductDataForHomePage to generate data for testing home page and product detail page,
this method returns a ProductResponse and is called by getProducts.
This ProductResponse has 8 fields:
    List<Product> products : all 70 product objects
    List<Product> productsCat0 :　10 product objects of category0
    ...
    List<Product> productsCat6 :　10 product objects of category6

if we decide to use same repository like this, I believe you can either build your own dataApi or call mine, FOR NOW.
 */
package com.flagcamp.secondhands.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.flagcamp.secondhands.model.AcceptRequestResponse;
import com.flagcamp.secondhands.model.CreateRequestResponse;
import com.flagcamp.secondhands.model.DeleteRequestResponse;
import com.flagcamp.secondhands.model.MyPostResponse;
import com.flagcamp.secondhands.model.OrderResponse;
import com.flagcamp.secondhands.model.RatingResponse;
import com.flagcamp.secondhands.model.RequestResponse;
import com.flagcamp.secondhands.model.UpdateFavResponse;
import com.flagcamp.secondhands.model.FavResponse;
import com.flagcamp.secondhands.model.MapSearchResponse;
import com.flagcamp.secondhands.model.MyRequest;
import com.flagcamp.secondhands.model.Order;
import com.flagcamp.secondhands.model.OrderPost;
import com.flagcamp.secondhands.model.OrderRequest;
import com.flagcamp.secondhands.model.PostProductResponse;
import com.flagcamp.secondhands.model.Product;
import com.flagcamp.secondhands.model.ProductResponse;
import com.flagcamp.secondhands.model.SearchResponse;
import com.flagcamp.secondhands.network.Api;
import com.flagcamp.secondhands.network.RetrofitClient;
import com.flagcamp.secondhands.ui.fav.FavViewModel;
import com.flagcamp.secondhands.ui.map.MapViewModel;
import com.flagcamp.secondhands.ui.search.SearchViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepository {
    private final Api api;
    // order dummy
    private List<Order> result;
    private List<OrderRequest> requestList;
    private List<MyRequest> myRequestList;
    private List<OrderPost> orderPostList;


    public ProductRepository(Context context){
        api = RetrofitClient.newInstance(context).create(Api.class);
    }

    public LiveData<ProductResponse> getProducts(String query){
        MutableLiveData<ProductResponse> everyThingLiveData = new MutableLiveData<>();
        api.getProduct()
                .enqueue(new Callback<ProductResponse>() {
                    @Override
                    public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                        if (response.isSuccessful()) {
                            everyThingLiveData.setValue(response.body());
                        } else {
                            everyThingLiveData.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductResponse> call, Throwable t) {
                        everyThingLiveData.setValue(null);
                    }
                });
        return everyThingLiveData;
    }

    public LiveData<SearchResponse> searchProducts(SearchViewModel.Cell cell){
        MutableLiveData<SearchResponse> everyThingLiveData = new MutableLiveData<>();
        api.search(cell.userToken, cell.searchInput, cell.categoryInput, cell.locationInput, cell.page, cell.pageSize)
                .enqueue(new Callback<SearchResponse>() {
                    @Override
                    public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                        if (response.isSuccessful()) {
                            everyThingLiveData.setValue(response.body());
                        } else {
                            everyThingLiveData.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchResponse> call, Throwable t) {
                        everyThingLiveData.setValue(null);
                    }
                });
        return everyThingLiveData;
    }

    public LiveData<PostProductResponse> postProduct(String idToken, Product product) {
        MutableLiveData<PostProductResponse> postProductLiveData = new MutableLiveData<>();
        api.postProduct(idToken, product) // TODO:
                .enqueue(new Callback<PostProductResponse>() {
                    @Override
                    public void onResponse(Call<PostProductResponse> call, Response<PostProductResponse> response) {
                        if (response.isSuccessful()) {
                            postProductLiveData.setValue(response.body());
                        } else {
                            postProductLiveData.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<PostProductResponse> call, Throwable t) {
                        postProductLiveData.setValue(null);
                    }
                });
        return postProductLiveData;
    }

    public LiveData<MapSearchResponse> getAllProductInfo(MapViewModel.Cell cell){
        MutableLiveData<MapSearchResponse> markers = new MutableLiveData<>();
        api.searchNearby(cell.id_token,cell.lat, cell.lon).enqueue(new Callback<MapSearchResponse>() {
            @Override
            public void onResponse(Call<MapSearchResponse> call, Response<MapSearchResponse> response) {
                if(response.isSuccessful()){
                    markers.setValue(response.body());
                }else{
                    markers.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<MapSearchResponse> call, Throwable t) {
                markers.setValue(null);
            }
        });
        return markers;
    }

    public LiveData<CreateRequestResponse> createRequest(String idToken, int productId){
        MutableLiveData<CreateRequestResponse> createRequestResponseMutableLiveData = new MutableLiveData<>();
        api.createRequest(idToken, productId).enqueue(new Callback<CreateRequestResponse>() { // TODO: For real
//        api.createRequest("user2", productId).enqueue(new Callback<CreateRequestResponse>(){
            @Override
            public void onResponse(Call<CreateRequestResponse> call, Response<CreateRequestResponse> response) {
                if (response.isSuccessful()) {
                    createRequestResponseMutableLiveData.setValue(response.body());
                } else {
                    createRequestResponseMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<CreateRequestResponse> call, Throwable t) {
                createRequestResponseMutableLiveData.setValue(null);
            }

        });
        return createRequestResponseMutableLiveData;
    }

    public LiveData<FavResponse> getFavProductList(FavViewModel.GetCell getCell){
        MutableLiveData<FavResponse> favProductList = new MutableLiveData<>();
        api.getFav(getCell.id_token).enqueue(new Callback<FavResponse>() {
            @Override
            public void onResponse(Call<FavResponse> call, Response<FavResponse> response) {
                if(response.isSuccessful()){
                    favProductList.setValue(response.body());
                }else{
                    favProductList.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<FavResponse> call, Throwable t) {
                favProductList.setValue(null);
            }
        });
        return favProductList;
    }

    public LiveData<UpdateFavResponse> addFavProduct(String idToken, int productId) {
        MutableLiveData<UpdateFavResponse> deleteFavResponseMutableLiveData = new MutableLiveData<>();
        api.addFav(idToken, productId).enqueue(new Callback<UpdateFavResponse>() { // TODO: For real
//        api.addFav("user2", productId).enqueue(new Callback<UpdateFavResponse>() { // For test
            @Override
            public void onResponse(Call<UpdateFavResponse> call, Response<UpdateFavResponse> response) {
                if (response.isSuccessful()) {
                    deleteFavResponseMutableLiveData.setValue(response.body());
                } else {
                    deleteFavResponseMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<UpdateFavResponse> call, Throwable t) {
                deleteFavResponseMutableLiveData.setValue(null);
            }
        });
        return deleteFavResponseMutableLiveData;
    }

    public LiveData<UpdateFavResponse>deleteFavProduct(String idToken, int productId){
        MutableLiveData<UpdateFavResponse> deleteFavResponseMutableLiveData = new MutableLiveData<>();
        api.deleteFav(idToken, productId).enqueue(new Callback<UpdateFavResponse>() { // TODO: For real
//        api.deleteFav("user2", productId).enqueue(new Callback<UpdateFavResponse>() { // For test
            @Override
            public void onResponse(Call<UpdateFavResponse> call, Response<UpdateFavResponse> response) {
                if (response.isSuccessful()) {
                    deleteFavResponseMutableLiveData.setValue(response.body());
                } else {
                    deleteFavResponseMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<UpdateFavResponse> call, Throwable t) {
                deleteFavResponseMutableLiveData.setValue(null);
            }
        });
        return deleteFavResponseMutableLiveData;
    }

    /*Order                    order                  order*/

    public LiveData<OrderResponse> getSellerOrder(String idToken){
        MutableLiveData<OrderResponse> everyThingLiveData = new MutableLiveData<>();
        api.getSellerOrder(idToken)
                .enqueue(new Callback<OrderResponse>() {
                    @Override
                    public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                        if (response.isSuccessful()) {
                            everyThingLiveData.setValue(response.body());
                        } else {
                            everyThingLiveData.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<OrderResponse> call, Throwable t) {
                        everyThingLiveData.setValue(null);
                    }
                });
        return everyThingLiveData;
    }

    public LiveData<OrderResponse> getBuyerOrder(String idToken){
        MutableLiveData<OrderResponse> everyThingLiveData = new MutableLiveData<>();
        api.getBuyerOrder(idToken)
                .enqueue(new Callback<OrderResponse>() {
                    @Override
                    public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                        if (response.isSuccessful()) {
                            everyThingLiveData.setValue(response.body());
                        } else {
                            everyThingLiveData.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<OrderResponse> call, Throwable t) {
                        everyThingLiveData.setValue(null);
                    }
                });
        return everyThingLiveData;
    }

    public LiveData<RatingResponse> rateOrder(int order_id, String idToken, double rating){
        MutableLiveData<RatingResponse> everyThingLiveData = new MutableLiveData<>();
        api.rateOrder(order_id, idToken, rating).enqueue(new Callback<RatingResponse>() {
            @Override
            public void onResponse(Call<RatingResponse> call, Response<RatingResponse> response) {
                if (response.isSuccessful()) {
                    everyThingLiveData.setValue(response.body());
                } else {
                    everyThingLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<RatingResponse> call, Throwable t) {
                everyThingLiveData.setValue(null);
            }
        });
        return everyThingLiveData;
    }



    public LiveData<RequestResponse> getBuyerRequest(String idToken){
        MutableLiveData<RequestResponse> everyThingLiveData = new MutableLiveData<>();
        api.getBuyerRequest(idToken).enqueue(new Callback<RequestResponse>() {
            @Override
            public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                if (response.isSuccessful()) {
                    everyThingLiveData.setValue(response.body());
                } else {
                    everyThingLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<RequestResponse> call, Throwable t) {
                everyThingLiveData.setValue(null);
            }
        });
        return everyThingLiveData;
    }

    public LiveData<RequestResponse> getSellerRequest(String idToken){
        MutableLiveData<RequestResponse> everyThingLiveData = new MutableLiveData<>();
        api.getSellerRequest(idToken).enqueue(new Callback<RequestResponse>() {
            @Override
            public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                if (response.isSuccessful()) {
                    everyThingLiveData.setValue(response.body());
                } else {
                    everyThingLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<RequestResponse> call, Throwable t) {
                everyThingLiveData.setValue(null);
            }
        });
        return everyThingLiveData;
    }


    public LiveData<DeleteRequestResponse> deleteRequest(int requestId, String idToken){
        MutableLiveData<DeleteRequestResponse> everyThingLiveData = new MutableLiveData<>();
        api.deleteRequest(requestId, idToken).enqueue(new Callback<DeleteRequestResponse>() {
            @Override
            public void onResponse(Call<DeleteRequestResponse> call, Response<DeleteRequestResponse> response) {
                if (response.isSuccessful()) {
                    everyThingLiveData.setValue(response.body());
                } else {
                    everyThingLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<DeleteRequestResponse> call, Throwable t) {
                everyThingLiveData.setValue(null);
            }
        });
        return everyThingLiveData;
    }

    public LiveData<DeleteRequestResponse> declineRequest(int requestId, String idToken){
        MutableLiveData<DeleteRequestResponse> everyThingLiveData = new MutableLiveData<>();
        api.declineRequest(requestId, idToken).enqueue(new Callback<DeleteRequestResponse>() {
            @Override
            public void onResponse(Call<DeleteRequestResponse> call, Response<DeleteRequestResponse> response) {
                if (response.isSuccessful()) {
                    everyThingLiveData.setValue(response.body());
                } else {
                    everyThingLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<DeleteRequestResponse> call, Throwable t) {
                everyThingLiveData.setValue(null);
            }
        });
        return everyThingLiveData;
    }

    public LiveData<AcceptRequestResponse> acceptRequest(int requestId, String idToken){
        MutableLiveData<AcceptRequestResponse> everyThingLiveData = new MutableLiveData<>();
        api.acceptRequest(requestId, idToken).enqueue(new Callback<AcceptRequestResponse>() {
            @Override
            public void onResponse(Call<AcceptRequestResponse> call, Response<AcceptRequestResponse> response) {
                if (response.isSuccessful()) {
                    everyThingLiveData.setValue(response.body());
                } else {
                    everyThingLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<AcceptRequestResponse> call, Throwable t) {
                everyThingLiveData.setValue(null);
            }
        });
        return everyThingLiveData;
    }

    public LiveData<MyPostResponse> getPostRequest(String idToken){
        Log.d("post", "1231321654654");
        MutableLiveData<MyPostResponse> everyThingLiveData = new MutableLiveData<>();
        api.getPostRequest(idToken).enqueue(new Callback<MyPostResponse>() {
            @Override
            public void onResponse(Call<MyPostResponse> call, Response<MyPostResponse> response) {
                if (response.isSuccessful()) {
                    everyThingLiveData.setValue(response.body());
                } else {
                    everyThingLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<MyPostResponse> call, Throwable t) {
                everyThingLiveData.setValue(null);
            }
        });
        return everyThingLiveData;
    }

    public LiveData<DeleteRequestResponse> deletePostRequest(int productId, String idToken){
        MutableLiveData<DeleteRequestResponse> everyThingLiveData = new MutableLiveData<>();
        api.deletePostRequest(productId, idToken).enqueue(new Callback<DeleteRequestResponse>() {
            @Override
            public void onResponse(Call<DeleteRequestResponse> call, Response<DeleteRequestResponse> response) {
                if (response.isSuccessful()) {
                    everyThingLiveData.setValue(response.body());
                } else {
                    everyThingLiveData.setValue(null);
                }
            }
            @Override
            public void onFailure(Call<DeleteRequestResponse> call, Throwable t) {
                everyThingLiveData.setValue(null);
            }
        });
        return everyThingLiveData;
    }
}
