package com.example.zivug.models;

public class Favorites
{
    private String ReciverUserID;
    private String SenderUserID;
    private String request_type;


    public Favorites()
    {

    }

    public String getReciverUserID() {
        return ReciverUserID;
    }

    public void setReciverUserID(String ReciverUserID) {
        this.ReciverUserID = ReciverUserID;
    }

    public String getSenderUserID() {
        return SenderUserID;
    }

    public void setSenderUserID(String SenderUserID) {
        this.SenderUserID = SenderUserID;
    }


    public String getRequest_type() {
        return request_type;
    }

    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }

    public Favorites(String ReciverUserID , String SenderUserID, String request_type )
    {
        this.ReciverUserID = ReciverUserID;
        this.SenderUserID = SenderUserID;
        this.request_type = request_type;
    }
}
