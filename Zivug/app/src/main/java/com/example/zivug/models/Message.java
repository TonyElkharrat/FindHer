package com.example.zivug.models;

public class Message {

    private String message;
    private String dateCreated;
    private String userSender;
    private String userReceiver;
    private boolean isRead;
    private  String type;


    public Message()
    {

    }

    public Message(String message, String dateCreated, String userSender, String userReceiver, Boolean isRead, String type) {
        this.message = message;
        this.dateCreated = dateCreated;
        this.userSender = userSender;
        this.userReceiver = userReceiver;
        this.isRead = isRead;

        this.type = type;
    }

    public String getMessage()
    {
        return message;
    }

    public String getDateCreated()
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

    public void setDateCreated(String dateCreated)
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

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean read) {
        isRead = read;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}