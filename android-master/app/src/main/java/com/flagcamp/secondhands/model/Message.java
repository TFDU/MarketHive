package com.flagcamp.secondhands.model;

import java.util.Date;

public class Message {

    private String messageUsername;
    private String messageText;
    private String messageUserId;
    private long messageTime;
    private String messagePhotoUrl;

    public Message(String messageUsername, String messageText, String messageUserId, String messagePhotoUrl, long messageTime) {
        this.messageUsername = messageUsername;
        this.messageText = messageText;
        this.messageUserId = messageUserId;
        this.messageTime = messageTime;
//        this.messageTime = new Date().getTime();
        this.messagePhotoUrl = messagePhotoUrl;
    }

    public String getMessageUsername() {
        return messageUsername;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageUserId() {
        return messageUserId;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public String getMessagePhotoUrl() {
        return messagePhotoUrl;
    }
}
