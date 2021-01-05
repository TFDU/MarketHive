package com.flagcamp.secondhands.ui.chat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flagcamp.secondhands.R;
import com.flagcamp.secondhands.databinding.ChatItemBinding;
import com.flagcamp.secondhands.model.ChatRoom;
import com.flagcamp.secondhands.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ChatFragmentAdapter extends RecyclerView.Adapter<ChatFragmentAdapter.ViewHolder> {

    List<ChatRoom> chatRoomList;
    ChatFragmentInterface chatFragmentInterface;

    public ChatFragmentAdapter(List<ChatRoom> chatRoomList, ChatFragmentInterface chatFragmentInterface) {
        this.chatRoomList = chatRoomList;
        this.chatFragmentInterface = chatFragmentInterface;
    }

    @NonNull
    @Override
    public ChatFragmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        ViewHolder viewHolder = new ChatFragmentAdapter.ViewHolder(view);

        Log.d("test", "create " + viewHolder.toString());

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatFragmentAdapter.ViewHolder holder, int position) {
        Log.d("test", "bind " + holder.toString());

        final ChatRoom chatRoom = chatRoomList.get(position);
        holder.senderName.setText(chatRoom.getSenderName());
        User user = new User(chatRoom.getSenderId(), chatRoom.getSenderName());
        holder.chatButton.setOnClickListener(v -> chatFragmentInterface.openMessageWindow(user));
    }

    @Override
    public int getItemCount() {
        return chatRoomList.size();
    }

    public interface ChatFragmentInterface {
        void openMessageWindow(User user);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView senderName;
        FloatingActionButton chatButton;

        ViewHolder(View itemView) {
            super(itemView);
            ChatItemBinding binding = ChatItemBinding.bind(itemView);
            senderName = binding.itemChatText;
            chatButton = binding.itemChatButton;
        }
    }
}
