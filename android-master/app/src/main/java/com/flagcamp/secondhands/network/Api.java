package com.flagcamp.secondhands.network;

import com.flagcamp.secondhands.model.AcceptRequestResponse;
import com.flagcamp.secondhands.model.CreateRequestResponse;
import com.flagcamp.secondhands.model.DeleteRequestResponse;
import com.flagcamp.secondhands.model.MyPostResponse;
import com.flagcamp.secondhands.model.OrderResponse;
import com.flagcamp.secondhands.model.ProductResponse;
import com.flagcamp.secondhands.model.RatingResponse;
import com.flagcamp.secondhands.model.RequestResponse;
import com.flagcamp.secondhands.model.UpdateFavResponse;
import com.flagcamp.secondhands.model.FavResponse;
import com.flagcamp.secondhands.model.MapSearchResponse;
import com.flagcamp.secondhands.model.NewUser;
import com.flagcamp.secondhands.model.PostProductResponse;
import com.flagcamp.secondhands.model.Product;
import com.flagcamp.secondhands.model.SearchResponse;
import com.flagcamp.secondhands.model.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {
    @GET("products/search")
    Call<SearchResponse> search(@Query("id_token") String id_token, @Query("keywords") String query, @Query("category") String category, @Query("state") String state, @Query("page") int page, @Query("page_size") int page_size);

    @POST("users")
    Call<UserResponse> postUser(@Query("id_token") String id_token, @Body NewUser body);

    @POST("products")
    Call<PostProductResponse> postProduct(@Query("id_token") String id_token, @Body Product body);

    @GET("products/nearby")
    Call<MapSearchResponse> searchNearby(@Query("id_token") String id_token, @Query("lat") double lat, @Query("lon") double lon);

    @GET("users/user/favorites")
    Call<FavResponse> getFav(@Query("id_token") String id_token);

    @POST("users/user/favorites")
    Call<UpdateFavResponse> addFav(@Query("id_token") String id_token, @Query("productId") int productId);

    @DELETE("users/user/favorites")
    Call<UpdateFavResponse> deleteFav(@Query("id_token") String id_token, @Query("productId") int productId);

    @GET("index")
    Call<ProductResponse> getProduct();

    @POST("/users/customer/requests")
    Call<CreateRequestResponse> createRequest(@Query("id_token") String id_token, @Query("product_id") int productId);

    /*order    order    order    order    order    order    order    */
    @GET("orders/seller")
    Call<OrderResponse> getSellerOrder(@Query("id_token") String userToken);

    @GET("orders/customer")
    Call<OrderResponse> getBuyerOrder(@Query("id_token") String userToken);

    @PUT("orders/customer/order/{order_id}")
    Call<RatingResponse> rateOrder(@Path("order_id") Integer order_id, @Query("id_token") String userToken, @Query("rating") Double rating);

    // get all requests from buyer, seller
    @GET("users/customer/requests")
    Call<RequestResponse> getBuyerRequest(@Query("id_token") String userToken);

    @GET("users/seller/requests")
    Call<RequestResponse> getSellerRequest(@Query("id_token") String userToken);

    // cancel request
    @DELETE("users/customer/requests/request/{requestId}")
    Call<DeleteRequestResponse> deleteRequest(@Path("requestId") Integer requestId, @Query("id_token") String userToken);

    // decline request
    @DELETE("users/seller/requests/request/{requestId}")
    Call<DeleteRequestResponse> declineRequest(@Path("requestId") Integer requestId, @Query("id_token") String userToken);

    // accept request to order
    @PUT("users/seller/requests/request/{requestId}")
    Call<AcceptRequestResponse> acceptRequest(@Path("requestId") Integer requestId, @Query("id_token") String userToken);

    @GET("users/user/products")
    Call<MyPostResponse> getPostRequest(@Query("id_token") String userToken);

    @DELETE("products/product/{productId}")
    Call<DeleteRequestResponse> deletePostRequest(@Path("productId") Integer productId, @Query("id_token") String userToken);
}
