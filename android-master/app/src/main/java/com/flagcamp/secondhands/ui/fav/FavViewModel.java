package com.flagcamp.secondhands.ui.fav;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.flagcamp.secondhands.model.UpdateFavResponse;
import com.flagcamp.secondhands.model.FavResponse;
import com.flagcamp.secondhands.repository.ProductRepository;

public class FavViewModel extends ViewModel {
    private final ProductRepository repository;
    private String idToken;
    private MutableLiveData<String> timestampInput = new MutableLiveData<>();
    private int productId;


    public FavViewModel(ProductRepository repository) {
        this.repository = repository;
    }

    public void setTimestampInput(String timestamp) {
        timestampInput.setValue(timestamp);
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public void setProductId(int productId) {
      this.productId = productId;
    }

    public LiveData<FavResponse> getFavProductList() {
        GetCustomLiveData input = new GetCustomLiveData(idToken, timestampInput);
        return Transformations.switchMap(input, repository::getFavProductList);
    }

    private static class GetCustomLiveData extends MediatorLiveData<GetCell> {
        public GetCustomLiveData(
                String id_token,
                MutableLiveData<String> timestampInput) {
            addSource(timestampInput, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    setValue(new GetCell(id_token, timestampInput.getValue()));
                }
            });
        }
    }

    public LiveData<UpdateFavResponse> deleteFavProduct() {
//        DeleteCustomLiveData input = new DeleteCustomLiveData(idToken, productIdInput);
//        return Transformations.switchMap(input, repository::deleteFavProduct);
        return repository.deleteFavProduct(idToken, productId);
    }

//    private static class DeleteCustomLiveData extends MediatorLiveData<DeleteCell> {
//        public DeleteCustomLiveData(
//                String id_token,
//                MutableLiveData<Integer> productIdInput ) {
//            addSource(productIdInput, new Observer<Integer>() {
//                @Override
//                public void onChanged(Integer in) {
//                    setValue(new DeleteCell("user2", productIdInput.getValue()));
//                }
//            });
//        }
//    }

    public static class GetCell {
        public String id_token;
        private String timeStamp;

        public GetCell(String id_token, String timeStamp) {
            this.id_token = id_token;
            this.timeStamp = timeStamp;
        }
    }

//    public static class DeleteCell {
//        public String id_token;
//        public int productId;
//
//        public DeleteCell(String id_token, int productId) {
//            this.id_token = id_token;
//            this.productId = productId;
//        }
//    }


}
