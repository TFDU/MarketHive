package com.flagcamp.secondhands.ui.chat;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.flagcamp.secondhands.CurrentUserSingleton;
import com.flagcamp.secondhands.databinding.FragmentMessageListBinding;
import com.flagcamp.secondhands.model.Message;
import com.flagcamp.secondhands.model.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MessageListFragment extends Fragment{

    private FragmentMessageListBinding binding;
    private List<Message> messageList = new ArrayList<>();
    private MessageAdapter messageAdapter;
    private FirebaseFirestore database;
    private CollectionReference chatRoomRef;
    private CurrentUserSingleton currentUser;

    public MessageListFragment() {
        // Required empty public constructor
        currentUser = CurrentUserSingleton.getInstance();
        database = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMessageListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        chatRoomRef = database.collection("chatRooms/chatRoom/" + getChatRoomName());

        updateMessageList();

        messageAdapter = new MessageAdapter(messageList);
        binding.messageListRecyclerview.setAdapter(messageAdapter);
        binding.messageListRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.chatboxSendButton.setOnClickListener(v -> {
            if (!binding.chatboxEdittext.getText().toString().isEmpty()) {
                Message message = new Message(currentUser.getUserName(),
                        binding.chatboxEdittext.getText().toString(),
                        currentUser.getUserId(),
                        currentUser.getPhotoUrl(),
                        new Date().getTime());
                chatRoomRef.add(message);

                binding.chatboxEdittext.setText("");
                hideKeyboard(getContext(), view);
            }
        });
    }

    private void updateMessageList() {
        chatRoomRef.orderBy("messageTime", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && !value.isEmpty()) {
                    for (DocumentChange change : value.getDocumentChanges()) {
                        DocumentSnapshot doc = change.getDocument();
                        Log.d("doc:", doc.getData().toString());
                        Message message = new Message(doc.get("messageUsername").toString(),
                                doc.get("messageText").toString(),
                                doc.get("messageUserId").toString(),
                                doc.get("messagePhotoUrl").toString(),
                                (long) doc.get("messageTime"));
                        messageList.add(message);
                    }

                    messageAdapter.notifyDataSetChanged();
                    binding.messageListRecyclerview.smoothScrollToPosition(messageList.size() - 1);
                } else {
                    if (error == null) {
                        Log.d("Message Render", "Empty message list");
                    } else {
                        Log.d("Message Render:", error.getMessage());
                    }
                }
            }
        });
    }

    private String getChatRoomName() {
        User sender = MessageListFragmentArgs.fromBundle(getArguments()).getUser();
        String chatRoomName = currentUser.getUserId() + ',' + sender.userUID;
        if (currentUser.getUserId().compareTo(sender.userUID) < 0) {
            chatRoomName = sender.userUID + ',' + currentUser.getUserId();
        }
        return chatRoomName;
    }

    public void hideKeyboard(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}