package com.example.findher.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Message {

    private String message;
    private Long dateCreated;
    private String userSender;
    private String userReceiver;

    public Message() { }

    public Message(String message, Long dateCreated, String userSender, String userReceiver) {
        this.message = message;
        this.dateCreated = dateCreated;
        this.userSender = userSender;
        this.userReceiver = userReceiver;
    }

    public String getMessage()
    {
        return message;
    }

    @ServerTimestamp
    public Long getDateCreated()
    {
        return dateCreated;
    }

    public String getUserSender()
    {
        return userSender;
    }


    public void setMessage(String message)
    {
        this.message = message;
    }

    public void setDateCreated(Long dateCreated)
    {
        this.dateCreated = dateCreated;
    }

    public void setUserSender(String userSender)
    {
        this.userSender = userSender;
    }


    public String getUserReceiver() {
        return userReceiver;
    }

    public void setUserReceiver(String userReceiver) {
        this.userReceiver = userReceiver;
    }
}