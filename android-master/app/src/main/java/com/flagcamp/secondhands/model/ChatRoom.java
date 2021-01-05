package com.flagcamp.secondhands.model;

public class ChatRoom {
    private String senderId; // sender
    private String senderName; // sender

    public ChatRoom(String senderId, String senderName) {
        this.senderId = senderId;
        this.senderName = senderName;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getSenderName() {
        return senderName;
    }
}
