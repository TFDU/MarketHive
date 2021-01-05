package com.flagcamp.secondhands.ui.chat;

import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flagcamp.secondhands.R;
import com.flagcamp.secondhands.model.Message;

public class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {

    TextView text;
    TextView username;
    TextView time;
    ImageView imgProfile;

    public ReceivedMessageViewHolder(@NonNull View itemView) {
        super(itemView);

        text = itemView.findViewById(R.id.message_body_text);
        username = itemView.findViewById(R.id.message_name_text);
        time = itemView.findViewById(R.id.message_time_text);
        imgProfile = itemView.findViewById(R.id.message_profile_image);
    }

}
