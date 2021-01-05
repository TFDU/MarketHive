package com.flagcamp.secondhands.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.flagcamp.secondhands.ui.fav.FavViewModel;
import com.flagcamp.secondhands.ui.home.HomeViewModel;
import com.flagcamp.secondhands.ui.list.ListViewModel;
import com.flagcamp.secondhands.ui.map.MapViewModel;
import com.flagcamp.secondhands.ui.order.OrderViewModel;
import com.flagcamp.secondhands.ui.post.PostViewModel;
import com.flagcamp.secondhands.ui.productDetail.ProductDetailViewModel;
import com.flagcamp.secondhands.ui.search.SearchViewModel;

public class ProductViewModelFactory implements ViewModelProvider.Factory{
    private final ProductRepository repository;

    public ProductViewModelFactory(ProductRepository repository){
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            return (T) new HomeViewModel(repository);
        } else if(modelClass.isAssignableFrom(ProductDetailViewModel.class)){
            return (T) new ProductDetailViewModel(repository);
        } else if(modelClass.isAssignableFrom((FavViewModel.class))){
            return (T) new FavViewModel(repository);
        } else if (modelClass.isAssignableFrom(SearchViewModel.class)) {
            return (T) new SearchViewModel(repository);
        } else if (modelClass.isAssignableFrom(PostViewModel.class)) {
            return (T) new PostViewModel(repository);
        } else if (modelClass.isAssignableFrom((MapViewModel.class))) {
            return (T) new MapViewModel(repository);
        } else if (modelClass.isAssignableFrom((OrderViewModel.class))) {
            return (T) new OrderViewModel(repository);
        } else if (modelClass.isAssignableFrom((ListViewModel.class))) {
            return (T) new ListViewModel(repository);
        }else{
            throw new IllegalStateException("Unknown ViewModel");
        }
    }
}
