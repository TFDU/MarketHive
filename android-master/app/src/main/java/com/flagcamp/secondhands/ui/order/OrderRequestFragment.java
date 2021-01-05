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

import com.flagcamp.secondhands.databinding.FragmentOrderRequestBinding;
import com.flagcamp.secondhands.model.OrderRequest;
import com.flagcamp.secondhands.repository.ProductRepository;
import com.flagcamp.secondhands.repository.ProductViewModelFactory;

import java.util.List;


public class OrderRequestFragment extends Fragment {

    private FragmentOrderRequestBinding binding;
    private OrderViewModel viewModel;
    private List<OrderRequest> requests;

    public OrderRequestFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = com.flagcamp.secondhands.databinding.FragmentOrderRequestBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        OrderRequestAdapter orderRequestAdapter = new OrderRequestAdapter();
        binding.orderRequestRecyclerView.setAdapter(orderRequestAdapter);
        binding.orderRequestRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));


        ProductRepository repository = new ProductRepository(getContext());
        viewModel = new ViewModelProvider(this, new ProductViewModelFactory(repository))
                .get(OrderViewModel.class);

        viewModel.setIdToken();
        viewModel
                .getSellerRequestLive()
                .observe(
                        getViewLifecycleOwner(),
                        requestResponse -> {
                            if (requestResponse != null) {
                                requests = requestResponse.requests;
                                orderRequestAdapter.setRequests(requests);
                                Log.d("OrderFragment", requestResponse.toString());
                            }
                        });

        orderRequestAdapter.setItemCallback(new OrderRequestAdapter.ItemCallback() {
            @Override
            public void onOpenDetails(OrderRequest orderRequest) {
                Log.d("onOpenDetails", "Open detail of " + orderRequest.product.productName);
                OrderRequestFragmentDirections.ActionNavigationOrderRequestToNavigationDetail direction = OrderRequestFragmentDirections.actionNavigationOrderRequestToNavigationDetail(orderRequest.product);
                NavHostFragment.findNavController(OrderRequestFragment.this).navigate(direction);
            }

            @Override
            public void onAccept(OrderRequest orderRequest, int position) {
                Log.d("onAccept", "Accept " + orderRequest.product.productName);
                viewModel.acceptRequestLive(orderRequest);
                orderRequestAdapter.removeItem(position);
            }

            @Override
            public void onDecline(OrderRequest orderRequest, int position) {
                Log.d("onOpenDetails", "Decline " + orderRequest.product.productName);
                viewModel.declineRequestLive(orderRequest);
                orderRequestAdapter.removeItem(position);
            }

            @Override
            public void onOpenProfile(OrderRequest orderRequest) {
                Log.d("onOpenDetails", "Buyer is " + orderRequest.user.name);
                OrderRequestFragmentDirections.ActionNavigationOrderRequestToNavigationVisitorViewProfile direction = OrderRequestFragmentDirections.actionNavigationOrderRequestToNavigationVisitorViewProfile(orderRequest.user);
                NavHostFragment.findNavController(OrderRequestFragment.this).navigate(direction);
            }

        });

    }
}