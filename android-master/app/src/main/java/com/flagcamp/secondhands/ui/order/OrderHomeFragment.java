package com.flagcamp.secondhands.ui.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.flagcamp.secondhands.R;
import com.flagcamp.secondhands.databinding.FragmentOrderHomeBinding;
import com.flagcamp.secondhands.repository.ProductRepository;


public class OrderHomeFragment extends Fragment {
    private FragmentOrderHomeBinding binding;

    public OrderHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOrderHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ProductRepository repository = new ProductRepository(getContext());
        binding.OrderHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller= Navigation.findNavController(v);
                controller.navigate(R.id.action_order_home_to_order_history);
            }
        });

        binding.OrderRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller= Navigation.findNavController(v);
                controller.navigate(R.id.action_navigation_order_home_to_navigation_order_request);
            }
        });

        binding.MyRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller= Navigation.findNavController(v);
                controller.navigate(R.id.action_navigation_order_home_to_navigation_my_request);
            }
        });

        binding.MyPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller= Navigation.findNavController(v);
                controller.navigate(R.id.action_navigation_order_home_to_navigation_order_post);
            }
        });

        binding.SaleHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller= Navigation.findNavController(v);
                controller.navigate(R.id.action_navigation_order_home_to_navigation_sale_history);
            }
        });
        //NavHostFragment.findNavController(ProfileFragment.this).navigate(ProfileFragmentDirections.actionNavigationProfileToOrderHistory());
    }
}