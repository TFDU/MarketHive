package com.flagcamp.secondhands.ui.order;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.flagcamp.secondhands.databinding.FragmentOrderHistoryBinding;
import com.flagcamp.secondhands.model.Order;
import com.flagcamp.secondhands.repository.ProductRepository;
import com.flagcamp.secondhands.repository.ProductViewModelFactory;

import java.util.List;


public class OrderHistoryFragment extends Fragment {

    private FragmentOrderHistoryBinding binding;
    private OrderViewModel viewModel;
    private List<Order> orders;

    public OrderHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOrderHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        OrderHistoryAdapter orderHistoryAdapter = new OrderHistoryAdapter();
        binding.orderHistoryRecyclerView.setAdapter(orderHistoryAdapter);
        binding.orderHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));


        ProductRepository repository = new ProductRepository(getContext());
        viewModel = new ViewModelProvider(this, new ProductViewModelFactory(repository))
                .get(OrderViewModel.class);


        //orderHistoryAdapter.setOrders(viewModel.getAllOrderHistory());
        Log.d("Break point", "11111111");

        viewModel.setIdToken();
        viewModel
                .getAllOrderHistoryLive()
                .observe(
                        getViewLifecycleOwner(),
                        orderResponse -> {
                            if (orderResponse != null) {
                                Log.d("OrderFragment", orderResponse.toString());
                                orders = orderResponse.orders;
                                orderHistoryAdapter.setOrders(orders);
                            }
                        });

        orderHistoryAdapter.setItemCallback(new OrderHistoryAdapter.ItemCallback() {
            @Override
            public void onOpenDetails(Order order) {
                Log.d("onOpenDetails", order.product.productName);
                OrderHistoryFragmentDirections.ActionNavigationOrderHistoryToNavigationDetail direction = OrderHistoryFragmentDirections.actionNavigationOrderHistoryToNavigationDetail(order.product);
                NavHostFragment.findNavController(OrderHistoryFragment.this).navigate(direction);
            }

            @Override
            public void onRating(RatingBar ratingBar, Order order) {
                Log.d("onOpenDetails", String.valueOf(ratingBar.getRating()));
                viewModel.rateOrder(order, ratingBar.getRating());
                ratingBar.setIsIndicator(true);
            }
        });
    }
}