package com.example.zivug.models;

public class User
{
    private String userName;
    private String uId;
    private String urlPicture;
    private String status;


    public User()
    {

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getUrlPicture() {
        return urlPicture;
    }

    public void setUrlPicture(String urlPicture) {
        this.urlPicture = urlPicture;
    }

    public User(String userName, String uId, String urlPicture, String lastSeen)
    {
        this.userName = userName;
        this.uId = uId;
        this.urlPicture = urlPicture;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
