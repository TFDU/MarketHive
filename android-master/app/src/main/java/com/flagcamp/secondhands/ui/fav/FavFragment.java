package com.flagcamp.secondhands.ui.fav;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.flagcamp.secondhands.model.Product;
import com.flagcamp.secondhands.repository.ProductRepository;
import com.flagcamp.secondhands.repository.ProductViewModelFactory;


import com.flagcamp.secondhands.databinding.FragmentFavBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;

import java.sql.Timestamp;


public class FavFragment extends Fragment {

    private FragmentFavBinding binding;
    private FavViewModel favViewModel;
    private final String tag = FavFragment.this.getClass().getSimpleName();

    public FavFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final FavProductAdapter favProductAdapter = new FavProductAdapter();
        favProductAdapter.setItemCallBack(new FavProductAdapter.ItemCallBack() {
            @Override
            public void onOpenDetail(Product product) {
                Log.d("onOpenProduct", product.toString());
                FavFragmentDirections.ActionNavigationFavToNavigationDetails direction = FavFragmentDirections.actionNavigationFavToNavigationDetails(product);
                NavHostFragment.findNavController(FavFragment.this).navigate(direction);
            }

            @Override
            public void onRemoveFav(Product product) {
                Log.d("onDeleteFav", product.toString());
                favViewModel.setProductId(product.productId);
                favViewModel.deleteFavProduct().observe(
                        getViewLifecycleOwner(),
                        deleteFavResponse -> {
                            if ("OK".equals(deleteFavResponse.status)) {
                                Toast.makeText(getContext(), "Delete product successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Something went wrong, please try later", Toast.LENGTH_SHORT).show();
                            }
                            //re-render fav list
                            getProductList(favProductAdapter);
                        }

                );

            }
        });

        getProductList(favProductAdapter);
    }

    private void getProductList(FavProductAdapter favProductAdapter){
        binding.favListsRecyclerView.setAdapter(favProductAdapter);
        binding.favListsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        ProductRepository repository = new ProductRepository(requireContext());
        favViewModel = new ViewModelProvider(this, new ProductViewModelFactory(repository)).get(FavViewModel.class);
        favViewModel.setTimestampInput(new Timestamp(System.currentTimeMillis()).toString());
        //get idToken
        FirebaseAuth.getInstance().getCurrentUser().getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    @Override
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if(task.isSuccessful()){
                            favViewModel.setIdToken(task.getResult().getToken());
                            favViewModel
                                    .getFavProductList()  //return variable is favProducts
                                    .observe(
                                            getViewLifecycleOwner(),
                                            favProducts -> {
                                                if (favProducts != null) {
                                                    Log.d(tag, favProducts.toString());
                                                    favProductAdapter.updateFavList(favProducts.user_favorites);
                                                } else {
                                                    Toast.makeText(getContext(), "Please favorite your products", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                        }else{
                            Toast.makeText(
                                    getContext(),
                                    "Please login your account",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }


}
