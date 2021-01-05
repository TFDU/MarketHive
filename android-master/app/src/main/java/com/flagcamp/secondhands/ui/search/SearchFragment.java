package com.flagcamp.secondhands.ui.search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.flagcamp.secondhands.R;
import com.flagcamp.secondhands.databinding.FragmentSearchBinding;
import com.flagcamp.secondhands.databinding.SearchProductItemBinding;
import com.flagcamp.secondhands.model.Product;
import com.flagcamp.secondhands.repository.ProductRepository;
import com.flagcamp.secondhands.repository.ProductViewModelFactory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;

import retrofit2.http.Headers;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private SearchViewModel viewModel;
    private SearchAdapter searchAdapter;
    private LinearLayoutManager layoutManager;
    private int page = 1;
    private int pageSize = 10;
    private Parcelable listState;


    private boolean isLoading = false;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (listState == null) {
            searchAdapter = new SearchAdapter();
        }
        binding.resultsRecyclerView.setAdapter(searchAdapter);

        layoutManager = new LinearLayoutManager(requireContext());
        binding.resultsRecyclerView.setLayoutManager(layoutManager);

        binding.searchProgressBar.setVisibility(View.GONE);

        ProductRepository repository = new ProductRepository(requireContext());
        viewModel = new ViewModelProvider(this, new ProductViewModelFactory(repository)).get(SearchViewModel.class);

        // Scroll to bottom
        binding.searchScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {

                    binding.searchProgressBar.setVisibility(View.VISIBLE);

                    FirebaseAuth.getInstance().getCurrentUser().getIdToken(true)
                            .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                public void onComplete(@NonNull Task<GetTokenResult> task) {
                                    if (task.isSuccessful()) {
                                        viewModel.setIdToken(task.getResult().getToken()); // String idToken;
                                        viewModel.setPage(page);
                                        viewModel.searchProducts()
                                                .observe(
                                                        getViewLifecycleOwner(),
                                                        productResponse -> {
                                                            if (productResponse != null) {
                                                                binding.searchProgressBar.setVisibility(View.GONE);
                                                                searchAdapter.setProducts(productResponse.products, page++, pageSize);
                                                            } else {
                                                                Log.d("search: ", "no product returned");
                                                            }
                                                        }
                                                );
                                        searchAdapter.notifyDataSetChanged();
                                    } else {
                                        // Handle error -> task.getException();
                                    }
                                }
                            });
                }
            }

        });

        viewModel.setPage(page);
        viewModel.setPageSize(pageSize);
        setUpSearch();
        setUpCategory();
        setUpLocation();

        searchAdapter.setItemCallback(new SearchAdapter.ItemCallback() {
            @Override
            public void onOpenDetails(Product product) { // switch fragment
                SearchFragmentDirections.ActionNavigationSearchToNavigationDetail direction = SearchFragmentDirections.actionNavigationSearchToNavigationDetail(product);
                NavHostFragment.findNavController(SearchFragment.this).navigate(direction);
            }

            @Override
            public void onRemoveFav(Product product) {
                viewModel.setProductId(product.productId);
                viewModel.deleteFavProduct()
                        .observe(
                                getViewLifecycleOwner(),
                                updateFavResponse -> {
                                    if (updateFavResponse != null && updateFavResponse.status.equals("OK")) {
                                        Toast.makeText(getContext(), "No Longer Like", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getContext(), "Something Wrong, Please Try Again Later", Toast.LENGTH_LONG).show();

                                    }
                                }
                        );
            }

            @Override
            public void onAddFav(Product product) {
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

        if (listState != null) {
            layoutManager.onRestoreInstanceState(listState);
            listState = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Save list state
        listState = layoutManager.onSaveInstanceState(); // save state to re-render after back action
    }

    private void setUpSearch() {
        binding.searchTextView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty()) {
                    viewModel.setSearchInput(query);
                }
                binding.searchTextView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        if (listState == null) { // first render
            FirebaseAuth.getInstance().getCurrentUser().getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                viewModel.setIdToken(task.getResult().getToken()); // String idToken;
                                viewModel.searchProducts()
                                        .observe(
                                                getViewLifecycleOwner(),
                                                productResponse -> {
                                                    if (productResponse != null) {
                                                        binding.searchProgressBar.setVisibility(View.GONE);
                                                        searchAdapter.setProducts(productResponse.products, page++, pageSize);
                                                    } else {
                                                        Log.d("search: ", "no product returned");
                                                    }
                                                }
                                        );
                            } else {
                                // Handle error -> task.getException();
                            }
                        }
                    });
        }


    }

    private void setUpCategory() {
        ArrayAdapter<CharSequence> categorySpinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.category, android.R.layout.simple_spinner_item);
        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.searchCategorySpinner.setAdapter(categorySpinnerAdapter);
        binding.searchCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String category = parent.getItemAtPosition(position).toString();
                String[] splitCat = category.split(" ");
                if (splitCat.length > 2) {
                    StringBuilder sbCat = new StringBuilder();
                    sbCat.append(splitCat[0]);
                    sbCat.append("%20%26%20");
                    sbCat.append(splitCat[2]);
                    viewModel.setCategoryInput(sbCat.toString());
                } else {
                    viewModel.setCategoryInput(category);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpLocation() {
        ArrayAdapter<CharSequence> locationSpinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.location, android.R.layout.simple_spinner_item);
        locationSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.searchLocationSpinner.setAdapter(locationSpinnerAdapter);
        binding.searchLocationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModel.setLocationInput(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


}