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

import com.flagcamp.secondhands.databinding.FragmentSaleHistoryBinding;
import com.flagcamp.secondhands.model.Order;
import com.flagcamp.secondhands.repository.ProductRepository;
import com.flagcamp.secondhands.repository.ProductViewModelFactory;

import java.util.List;


public class SaleHistoryFragment extends Fragment {

    private FragmentSaleHistoryBinding binding;
    private OrderViewModel viewModel;
    private List<Order> orders;

    public SaleHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSaleHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SaleHistoryAdapter saleHistoryAdapter = new SaleHistoryAdapter();
        binding.saleHistoryRecyclerView.setAdapter(saleHistoryAdapter);
        binding.saleHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));


        ProductRepository repository = new ProductRepository(getContext());
        viewModel = new ViewModelProvider(this, new ProductViewModelFactory(repository))
                .get(OrderViewModel.class);


        //saleHistoryAdapter.setOrders(viewModel.getAllSaleHistory());

        viewModel.setIdToken();
        viewModel
                .getAllSaleHistoryLive()
                .observe(
                        getViewLifecycleOwner(),
                        orderSellerResponse -> {
                            Log.d("sdsd", "1231321231");
                            if (orderSellerResponse != null) {
                                Log.d("SellerOrder", orderSellerResponse.toString());
                                orders = orderSellerResponse.orders;
                                saleHistoryAdapter.setOrders(orders);
                            } else {
                                Log.d("SellerOrder: ", "no product returned");
                            }
                        });
       // hardcodeDataOrder = viewModel.getAllSaleHistory();

        saleHistoryAdapter.setItemCallback(new SaleHistoryAdapter.ItemCallback() {
            @Override
            public void onOpenDetails(Order order) {
                Log.d("onOpenDetails", order.product.productName);
                SaleHistoryFragmentDirections.ActionNavigationSaleHistoryToNavigationDetail direction = SaleHistoryFragmentDirections.actionNavigationSaleHistoryToNavigationDetail(order.product);
                NavHostFragment.findNavController(SaleHistoryFragment.this).navigate(direction);
            }
        });

    }

}