package com.flagcamp.secondhands.ui.chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.flagcamp.secondhands.CurrentUserSingleton;
import com.flagcamp.secondhands.databinding.FragmentChatRoomBinding;
import com.flagcamp.secondhands.model.ChatRoom;
import com.flagcamp.secondhands.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class ChatRoomFragment extends Fragment implements ChatFragmentAdapter.ChatFragmentInterface {

    List<ChatRoom> chatRoomList = new ArrayList<>();
    private ChatFragmentAdapter chatFragmentAdapter;
    private FirebaseFirestore database;
    private FragmentChatRoomBinding binding;


    public ChatRoomFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatRoomBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        database = FirebaseFirestore.getInstance();
        CurrentUserSingleton currentUser = CurrentUserSingleton.getInstance();

        CollectionReference friendshipRef = database.collection("chatRooms").document("friendship").collection(currentUser.getUserId());
        friendshipRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot query = task.getResult();
                    if (!query.isEmpty()) {
                        chatRoomList.clear();
                        for (DocumentSnapshot doc: query.getDocuments()) {
                            Log.d("Document", doc.getData().toString());

                            ChatRoom chatRoom = new ChatRoom(doc.get("senderId").toString(), doc.get("senderName").toString());
                            chatRoomList.add(chatRoom);
                        }
                        chatFragmentAdapter.notifyDataSetChanged();
                    } else {
                        Log.d("Test", "The read failed");
                    }
                }
            }
        });

        chatFragmentAdapter = new ChatFragmentAdapter(chatRoomList, ChatRoomFragment.this);
        binding.fragmentChatRoomRecyclerView.setAdapter(chatFragmentAdapter);

        binding.fragmentChatRoomRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void openMessageWindow(User user) {
        ChatRoomFragmentDirections.ActionNavigationChatRoomToNavigationMessage direction = ChatRoomFragmentDirections.actionNavigationChatRoomToNavigationMessage(user);
        NavHostFragment.findNavController(ChatRoomFragment.this).navigate(direction);
    }
}