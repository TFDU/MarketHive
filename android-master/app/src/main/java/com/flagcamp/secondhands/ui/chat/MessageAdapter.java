package com.flagcamp.secondhands.ui.chat;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flagcamp.secondhands.CurrentUserSingleton;
import com.flagcamp.secondhands.R;
import com.flagcamp.secondhands.model.Message;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int MY_TEXT_MESSAGE = 0, THEIR_TEXT_MESSAGE = 1;

    private List<Message> messageList;
    CurrentUserSingleton currentUser = CurrentUserSingleton.getInstance();

    public MessageAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);
        if (message.getMessageUserId().equals(currentUser.getUserId())) {
            return MY_TEXT_MESSAGE;
        }
        return THEIR_TEXT_MESSAGE;
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        Log.d("bind:", ((Integer) position).toString());

        switch (viewHolder.getItemViewType()) {
            case MY_TEXT_MESSAGE:
                SentMessageViewHolder holder1 = (SentMessageViewHolder) viewHolder;
                configureMyTextMessageViewHolder(holder1, position);
                break;
            case THEIR_TEXT_MESSAGE:
                ReceivedMessageViewHolder holder2 = (ReceivedMessageViewHolder) viewHolder;
                configureTheirTextMessageViewHolder(holder2, position);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == MY_TEXT_MESSAGE) {
            View view = inflater.inflate(R.layout.message_sent_item, parent, false);
            viewHolder = new SentMessageViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.message_received_item, parent, false);
            viewHolder = new ReceivedMessageViewHolder(view);
        }
        return viewHolder;
    }

    private void configureMyTextMessageViewHolder(SentMessageViewHolder viewHolder, int position) {
        Message message = messageList.get(position);
        Picasso.get().load(message.getMessagePhotoUrl()).into(viewHolder.imgProfile);
        viewHolder.text.setText(message.getMessageText());
        viewHolder.time.setText(DateFormat.format("dd MMM h:mm a", message.getMessageTime()));
    }

    private void configureTheirTextMessageViewHolder(ReceivedMessageViewHolder viewHolder, int position) {
        Message message = messageList.get(position);
        viewHolder.username.setText(message.getMessageUsername());
        Picasso.get().load(message.getMessagePhotoUrl()).into(viewHolder.imgProfile);
        viewHolder.text.setText(message.getMessageText());
        viewHolder.time.setText(DateFormat.format("dd MMM h:mm a", message.getMessageTime()));
    }

}