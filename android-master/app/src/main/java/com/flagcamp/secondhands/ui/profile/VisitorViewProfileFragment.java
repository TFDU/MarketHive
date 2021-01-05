package com.flagcamp.secondhands.ui.profile;

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
import com.flagcamp.secondhands.R;
import com.flagcamp.secondhands.databinding.FragmentVisitorViewProfileBinding;
import com.flagcamp.secondhands.model.ChatRoom;
import com.flagcamp.secondhands.model.User;
import com.flagcamp.secondhands.ui.chat.ChatFragmentAdapter;
import com.flagcamp.secondhands.ui.chat.ChatRoomFragment;
import com.flagcamp.secondhands.ui.chat.ChatRoomFragmentDirections;
import com.flagcamp.secondhands.ui.chat.MessageListFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;


public class VisitorViewProfileFragment extends Fragment implements ChatFragmentAdapter.ChatFragmentInterface{

    private FragmentVisitorViewProfileBinding binding;
    private FirebaseFirestore database;

    public VisitorViewProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentVisitorViewProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        User user = VisitorViewProfileFragmentArgs.fromBundle(getArguments()).getUser();
        binding.visitorViewProfileNameTextView.setText(user.name);
        Picasso.get().load(user.photoUrl).into(binding.visitorViewProfilePhotoImageView);
        binding.visitorViewProfileEmailTextView.setText(user.email);
        binding.visitorViewProfileRatingScoreTextView.setText(user.rating);
        binding.visitorViewProfileChatButton.setOnClickListener(v -> {
            openMessageWindow(user);
            addFriendship(user);
        });
    }

    @Override
    public void openMessageWindow(User user) {
        VisitorViewProfileFragmentDirections.ActionNavigationVisitorViewProfileToNavigationMessage direction = VisitorViewProfileFragmentDirections.actionNavigationVisitorViewProfileToNavigationMessage(user);
        NavHostFragment.findNavController(VisitorViewProfileFragment.this).navigate(direction);
    }

    private void addFriendship(User user) {
        database = FirebaseFirestore.getInstance();
        CurrentUserSingleton currentUser = CurrentUserSingleton.getInstance();
        DocumentReference friendshipRef = database.collection("chatRooms").document("friendship");
        ChatRoom chatRoom1 = new ChatRoom(user.userUID, user.name);
        ChatRoom chatRoom2 = new ChatRoom(currentUser.getUserId(), currentUser.getUserName());
        friendshipRef.collection(currentUser.getUserId()).document(user.userUID).set(chatRoom1);
        friendshipRef.collection(user.userUID).document(currentUser.getUserId()).set(chatRoom2);
    }

}