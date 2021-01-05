package com.flagcamp.secondhands.ui.order;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.flagcamp.secondhands.databinding.FragmentOrderPostBinding;
import com.flagcamp.secondhands.model.Product;
import com.flagcamp.secondhands.repository.ProductRepository;
import com.flagcamp.secondhands.repository.ProductViewModelFactory;

import java.util.List;


public class OrderPostFragment extends Fragment {
    private FragmentOrderPostBinding binding;
    private OrderViewModel viewModel;
    private List<Product> posts;


    public OrderPostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOrderPostBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        OrderPostAdapter orderPostAdapter = new OrderPostAdapter();
        binding.orderPostRecyclerView.setAdapter(orderPostAdapter);
        binding.orderPostRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));


        ProductRepository repository = new ProductRepository(getContext());
        viewModel = new ViewModelProvider(this, new ProductViewModelFactory(repository))
                .get(OrderViewModel.class);

        //orderPostAdapter.setPosts(viewModel.getPostRequest());
        viewModel.setIdToken();
        viewModel
                .getPostRequest()
                .observe(
                        getViewLifecycleOwner(),
                        postResponse -> {
                            if (postResponse != null) {
                                posts = postResponse.products_of_user;
                                orderPostAdapter.setPosts(posts);
                                Log.d("OrderFragment", postResponse.toString());
                            }
                        });

        orderPostAdapter.setItemCallback(new OrderPostAdapter.ItemCallback() {
            @Override
            public void onOpenDetails(Product product) {
                Log.d("onOpenDetails", "Open detail of " + product.productName);
                OrderPostFragmentDirections.ActionNavigationOrderPostToNavigationDetail direction = OrderPostFragmentDirections.actionNavigationOrderPostToNavigationDetail(product);
                NavHostFragment.findNavController(OrderPostFragment.this).navigate(direction);
            }

            @Override
            public void onDelete(Product product, int position) {
                Log.d("onDelete", "Delete " + product.productName);
                viewModel.deletePostRequest(product);
                orderPostAdapter.removeItem(position);
            }

        });

    }


}