package com.flagcamp.secondhands.ui.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flagcamp.secondhands.CurrentUserSingleton;
import com.flagcamp.secondhands.LoginActivity;
import com.flagcamp.secondhands.databinding.FragmentProfileBinding;
import com.flagcamp.secondhands.model.User;
import com.flagcamp.secondhands.ui.chat.ChatRoomFragment;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CurrentUserSingleton currentUser = CurrentUserSingleton.getInstance();

        binding.profileNameTextView.setText(currentUser.getUserName());
        Picasso.get().load(currentUser.getPhotoUrl()).into(binding.profilePhotoImageView);
        binding.profileEmailTextView.setText(currentUser.getEmail());
        binding.profileRatingScoreTextView.setText(currentUser.getRating());

        User user = new User(currentUser.getUserId(), currentUser.getUserName());
        binding.profileChatRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // link to Message List Page
                ProfileFragmentDirections.ActionNavigationProfileToNavigationChatRoom direction = ProfileFragmentDirections.actionNavigationProfileToNavigationChatRoom(user);
                NavHostFragment.findNavController(ProfileFragment.this).navigate(direction);
            }
        });

        binding.profileOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // link to Order Management page
                ProfileFragmentDirections.ActionNavigationProfileToNavigationOrderHome direction = ProfileFragmentDirections.actionNavigationProfileToNavigationOrderHome(user);
                NavHostFragment.findNavController(ProfileFragment.this).navigate(direction);
            }
        });

        binding.profileLogoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });

    }

}