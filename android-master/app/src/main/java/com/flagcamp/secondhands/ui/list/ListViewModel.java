package com.flagcamp.secondhands.ui.list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.flagcamp.secondhands.CurrentUserSingleton;
import com.flagcamp.secondhands.model.UpdateFavResponse;
import com.flagcamp.secondhands.model.SearchResponse;
import com.flagcamp.secondhands.repository.ProductRepository;
import com.flagcamp.secondhands.ui.search.SearchViewModel;

public class ListViewModel extends ViewModel{
    private final ProductRepository repository;
    private final MutableLiveData<String> searchInput = new MutableLiveData<>();
    private final MutableLiveData<String> categoryInput = new MutableLiveData<>("");
    private final MutableLiveData<String> locationInput = new MutableLiveData<>("");
    private String idToken;
    private int page;
    private int pageSize;
    private int productId;

    public ListViewModel(ProductRepository repository) {
        this.repository = repository;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public void setSearchInput(String query) {
        searchInput.setValue(query);
    }

    public void setCategoryInput(String query) {
        query = query.equals("All Categories") ? "N/A" : query;
        categoryInput.setValue(query);
    }

    public void setLocationInput(String query) {
        query = query.equals("All States") ? "N/A" : query;
        locationInput.setValue(query);
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public LiveData<SearchResponse> searchProducts() {
        CurrentUserSingleton currentUser = CurrentUserSingleton.getInstance();
        ListViewModel.CustomLiveData input = new ListViewModel.CustomLiveData(idToken, searchInput, categoryInput, locationInput, page, pageSize); // TODO: For real user
//        CustomLiveData input = new CustomLiveData("user2", searchInput, categoryInput, locationInput, page, pageSize); // For test
        return Transformations.switchMap(input, repository::searchProducts);
    }

    public LiveData<UpdateFavResponse> deleteFavProduct() {
        return repository.deleteFavProduct(idToken, productId);
    }

    public LiveData<UpdateFavResponse> addFavProduct() {
        return repository.addFavProduct(idToken, productId);
    }

    private static class CustomLiveData extends MediatorLiveData<SearchViewModel.Cell> {
        public CustomLiveData(String userToken,
                              MutableLiveData<String> searchInput,
                              MutableLiveData<String> categoryInput,
                              MutableLiveData<String> locationInput,
                              int page, int pageSize) {
            addSource(searchInput, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    setValue(new SearchViewModel.Cell(userToken, s, categoryInput.getValue(), locationInput.getValue(), page, pageSize));
                }
            });
        }
    }

    public static class Cell {
        public String userToken;
        public String searchInput;
        public String categoryInput;
        public String locationInput;
        public int page;
        public int pageSize;

        public Cell(String userToken, String searchInput, String categoryInput, String locationInput, int page, int pageSize) {
            this.userToken = userToken;
            this.searchInput = searchInput;
            this.categoryInput = categoryInput;
            this.locationInput = locationInput;
            this.page = page;
            this.pageSize = pageSize;
        }
    }
}
