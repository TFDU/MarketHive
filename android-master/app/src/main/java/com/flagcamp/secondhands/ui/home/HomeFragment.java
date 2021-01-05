/*
I have problem with encapsulate the rendering code.
So if you want to use my rendering code to save time, the easiest way for now is to copy some codes,
OR YOU CAN TRY TO TEACH ME HOW TO ENCAPSULATE THEM.

Since I have so many views on homepage, I use [] to store adapters and managers.
I use StaggeredGridLayout to make a recyclerView HORIZONTALLY scrollable, change it to your own layout. eg LinearLayout
 */
package com.flagcamp.secondhands.ui.home;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.flagcamp.secondhands.CurrentUserSingleton;
import com.flagcamp.secondhands.R;
import com.flagcamp.secondhands.databinding.FragmentHomeBinding;
import com.flagcamp.secondhands.model.Category;
import com.flagcamp.secondhands.model.Product;
import com.flagcamp.secondhands.repository.ProductRepository;
import com.flagcamp.secondhands.repository.ProductViewModelFactory;
import com.flagcamp.secondhands.ui.search.SearchAdapter;

public class HomeFragment extends Fragment {
    private static int CATEGORIES = 7;

    private HomeViewModel viewModel;
    private HomeProductAdapter[] productAdapters = new HomeProductAdapter[CATEGORIES];
    private StaggeredGridLayoutManager[] layoutManagers = new StaggeredGridLayoutManager[CATEGORIES];
    private Parcelable[] listState = new Parcelable[CATEGORIES];
    private RecyclerView[] recyclerViews = new RecyclerView[CATEGORIES];
    private FragmentHomeBinding binding;


    public HomeFragment(){
        // required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        // Save list state
        for(int i = 0; i < CATEGORIES; i++){
            listState[i] = layoutManagers[i].onSaveInstanceState(); // save state to re-render after back action
        }

    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        for(int i = 0; i < CATEGORIES; i++){
            if (listState[i] == null) {
                productAdapters[i] = new HomeProductAdapter();

            }
        }
        recyclerViews[0] = binding.productsResultsRecyclerView0;
        recyclerViews[1] = binding.productsResultsRecyclerView1;
        recyclerViews[2] = binding.productsResultsRecyclerView2;
        recyclerViews[3] = binding.productsResultsRecyclerView3;
        recyclerViews[4] = binding.productsResultsRecyclerView4;
        recyclerViews[5] = binding.productsResultsRecyclerView5;
        recyclerViews[6] = binding.productsResultsRecyclerView6;

//        binding.mapButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_home_to_navigation_map, null));
        binding.homeMapButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_navigation_map);
            }
        });
        binding.homeBabyandkidButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                HomeFragmentDirections.ActionNavigationHomeToNavigationList direction = HomeFragmentDirections.actionNavigationHomeToNavigationList(new Category("Baby & Kids"));
                NavHostFragment.findNavController(HomeFragment.this).navigate(direction);
            }
        });
        binding.homeCarButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                HomeFragmentDirections.ActionNavigationHomeToNavigationList direction = HomeFragmentDirections.actionNavigationHomeToNavigationList(new Category("Cars"));
                NavHostFragment.findNavController(HomeFragment.this).navigate(direction);
            }
        });
        binding.homeClothingButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                HomeFragmentDirections.ActionNavigationHomeToNavigationList direction = HomeFragmentDirections.actionNavigationHomeToNavigationList(new Category("Clothing & Shoes"));
                NavHostFragment.findNavController(HomeFragment.this).navigate(direction);
            }
        });
        binding.homeHealthButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                HomeFragmentDirections.ActionNavigationHomeToNavigationList direction = HomeFragmentDirections.actionNavigationHomeToNavigationList(new Category("Beauty & Health"));
                NavHostFragment.findNavController(HomeFragment.this).navigate(direction);
            }
        });
        binding.homeHouseholdButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                HomeFragmentDirections.ActionNavigationHomeToNavigationList direction = HomeFragmentDirections.actionNavigationHomeToNavigationList(new Category("Household"));
                NavHostFragment.findNavController(HomeFragment.this).navigate(direction);
            }
        });
        binding.homeElectronicButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                HomeFragmentDirections.ActionNavigationHomeToNavigationList direction = HomeFragmentDirections.actionNavigationHomeToNavigationList(new Category("Electronics"));
                NavHostFragment.findNavController(HomeFragment.this).navigate(direction);
            }
        });
        binding.homeFurnitureButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                HomeFragmentDirections.ActionNavigationHomeToNavigationList direction = HomeFragmentDirections.actionNavigationHomeToNavigationList(new Category("Furniture"));
                NavHostFragment.findNavController(HomeFragment.this).navigate(direction);
            }
        });
        for(int i = 0; i < CATEGORIES; i++){
            layoutManagers[i] = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
            recyclerViews[i].setAdapter(productAdapters[i]);
            recyclerViews[i].setLayoutManager(layoutManagers[i]);
        }
        for(int i = 0; i < CATEGORIES; i++){
            productAdapters[i].setItemCallback(new HomeProductAdapter.ItemCallback() {
                @Override
                public void onOpenDetails(Product product) {
                    // TODO
                    /*
                    HomeFragmentDirections.ActionNavigationHomeToNavigationDetail direction = HomeFragmentDirections.actionNavigationHomeToNavigationDetail(product);
                    NavHostFragment.findNavController(HomeFragment.this).navigate(direction);

                    */
                    HomeFragmentDirections.ActionNavigationHomeToNavigationDetail direction = HomeFragmentDirections.actionNavigationHomeToNavigationDetail(product);
                    NavHostFragment.findNavController(HomeFragment.this).navigate(direction);

                }
                @Override
                public void onRemoveFav(Product product) {
                    Log.d("onDeleteFav", product.toString());
                    viewModel.setIdToken(CurrentUserSingleton.getInstance().getIdToken());
                    viewModel.setProductId(product.productId);
                    viewModel.deleteFavProduct().observe(
                            getViewLifecycleOwner(),
                            deleteFavResponse -> {
                                if ("OK".equals(deleteFavResponse.status)) {
                                    Toast.makeText(getContext(), "No longer like", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Something went wrong, please try later", Toast.LENGTH_SHORT).show();
                                }
                                //re-render fav list
//                                getProducts();
                            }

                    );


                }
                public void onAddFav(Product product) {
                    viewModel.setIdToken(CurrentUserSingleton.getInstance().getIdToken());
                    viewModel.setProductId(product.productId);
                    viewModel.addFavProduct()
                            .observe(
                                    getViewLifecycleOwner(),
                                    updateFavResponse -> {
                                        if (updateFavResponse != null && updateFavResponse.status.equals("OK")) {
                                            Toast.makeText(getContext(), "Like", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(getContext(), "Something Wrong, Please Try Again Later", Toast.LENGTH_LONG).show();
                                        }
                                    }
                            );

                }


            });
            if(listState[i] != null) {
                layoutManagers[i].onRestoreInstanceState(listState[i]);
                listState[i] = null;
            }
        }



        ProductRepository repository = new ProductRepository(requireContext());
        viewModel = new ViewModelProvider(this, new ProductViewModelFactory(repository)).get(HomeViewModel.class);
        viewModel.setSearchInput("q");

        viewModel.getProducts().observe(getViewLifecycleOwner(),productResponse -> {
            if(productResponse != null){
                for(int i = 0; i < CATEGORIES; i++){
                    productAdapters[i].setProducts(productResponse.getCat(i));
                }
            }
        });



    }

}
