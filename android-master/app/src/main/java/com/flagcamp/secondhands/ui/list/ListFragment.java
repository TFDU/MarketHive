package com.flagcamp.secondhands.ui.list;

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
import com.flagcamp.secondhands.databinding.FragmentListBinding;
import com.flagcamp.secondhands.databinding.FragmentSearchBinding;
import com.flagcamp.secondhands.databinding.SearchProductItemBinding;
import com.flagcamp.secondhands.model.Product;
import com.flagcamp.secondhands.repository.ProductRepository;
import com.flagcamp.secondhands.repository.ProductViewModelFactory;
import com.flagcamp.secondhands.ui.productDetail.ProductDetailFragmentArgs;
import com.flagcamp.secondhands.ui.search.SearchAdapter;
import com.flagcamp.secondhands.ui.search.SearchFragment;
import com.flagcamp.secondhands.ui.search.SearchFragmentDirections;
import com.flagcamp.secondhands.ui.search.SearchViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;

public class ListFragment extends Fragment {
    private FragmentListBinding binding;
    private ListViewModel viewModel;
    private ListAdapter listAdapter;
    private LinearLayoutManager layoutManager;
    private int page = 1;
    private int pageSize = 10;
    private Parcelable listState;
    private String cate;


    private boolean isLoading = false;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (listState == null) {
            listAdapter = new ListAdapter();
        }
        binding.resultsRecyclerView.setAdapter(listAdapter);

        layoutManager = new LinearLayoutManager(requireContext());
        binding.resultsRecyclerView.setLayoutManager(layoutManager);

        binding.searchProgressBar.setVisibility(View.GONE);

        ProductRepository repository = new ProductRepository(requireContext());
        viewModel = new ViewModelProvider(this, new ProductViewModelFactory(repository)).get(ListViewModel.class);

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
                                                                listAdapter.setProducts(productResponse.products, page++, pageSize);
                                                            } else {
                                                                Log.d("search: ", "no product returned");
                                                            }
                                                        }
                                                );
                                        listAdapter.notifyDataSetChanged();
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
        viewModel.setLocationInput("All States");

        listAdapter.setItemCallback(new ListAdapter.ItemCallback() {
            @Override
            public void onOpenDetails(Product product) { // switch fragment
                ListFragmentDirections.ActionNavigationListToNavigationDetails direction = ListFragmentDirections.actionNavigationListToNavigationDetails(product);
                NavHostFragment.findNavController(ListFragment.this).navigate(direction);
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

        viewModel.setSearchInput("");



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
                                                        listAdapter.setProducts(productResponse.products, page++, pageSize);
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
        cate = ListFragmentArgs.fromBundle(getArguments()).getCategory().category; //TODO NAVIGATION
        String category = cate;
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

}
