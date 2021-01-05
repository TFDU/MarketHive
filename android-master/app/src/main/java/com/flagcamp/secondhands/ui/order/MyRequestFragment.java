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

import com.flagcamp.secondhands.model.MyRequest;
import com.flagcamp.secondhands.model.OrderRequest;
import com.flagcamp.secondhands.repository.ProductRepository;
import com.flagcamp.secondhands.repository.ProductViewModelFactory;

import java.util.List;


public class MyRequestFragment extends Fragment {

    private com.flagcamp.secondhands.databinding.FragmentMyRequestBinding binding;
    private OrderViewModel viewModel;
    private List<OrderRequest> orderRequests;

    public MyRequestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = com.flagcamp.secondhands.databinding.FragmentMyRequestBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MyRequestAdapter myRequestAdapter = new MyRequestAdapter();
        binding.myRequestRecyclerView.setAdapter(myRequestAdapter);
        binding.myRequestRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));


        ProductRepository repository = new ProductRepository(getContext());
        viewModel = new ViewModelProvider(this, new ProductViewModelFactory(repository))
                .get(OrderViewModel.class);

        //myRequestAdapter.setOrderRequests(viewModel.);
        viewModel.setIdToken();
        viewModel
                .getBuyerRequestLive()
                .observe(
                        getViewLifecycleOwner(),
                        requestResponse -> {
                            if (requestResponse != null) {
                                orderRequests = requestResponse.requests;
                                myRequestAdapter.setOrderRequests(orderRequests);
                                Log.d("MyRequestFragment", requestResponse.toString());
                            }
                        });

        myRequestAdapter.setItemCallback(new MyRequestAdapter.ItemCallback() {
            @Override
            public void onOpenDetails(OrderRequest orderRequest) {
                Log.d("onOpenDetails", "Open detail of " + orderRequest.product.productName);
                MyRequestFragmentDirections.ActionNavigationMyRequestToNavigationDetail direction = MyRequestFragmentDirections.actionNavigationMyRequestToNavigationDetail(orderRequest.product);
                NavHostFragment.findNavController(MyRequestFragment.this).navigate(direction);
            }

            @Override
            public void onCancel(OrderRequest orderRequest, int position) {
                Log.d("onCancel", "Cancel " + orderRequest.user.name);
                viewModel.deleteRequestLive(orderRequest);
                myRequestAdapter.removeItem(position);
            }
        });

    }
}