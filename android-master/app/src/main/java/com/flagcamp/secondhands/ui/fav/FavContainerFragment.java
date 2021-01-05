package com.flagcamp.secondhands.ui.fav;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flagcamp.secondhands.R;
import com.flagcamp.secondhands.databinding.FragmentFavContainerBinding;
import com.flagcamp.secondhands.databinding.FragmentPostContainerBinding;


public class FavContainerFragment extends Fragment {

    private FragmentFavContainerBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("FavContainerFragment", "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavContainerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}