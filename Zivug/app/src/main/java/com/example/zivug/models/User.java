package com.example.zivug.models;

public class User
{
    private String userName;
    private String uId;
    private String urlPicture;
    private String status;
    private String ageUser;
    private Location Location;
    private String levelOfReligion;
    private String gender;

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

    public User(String userName, String uId, String urlPicture, String lastSeen, Location location)
    {
        this.userName = userName;
        this.uId = uId;
        this.urlPicture = urlPicture;
        this.Location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getAgeUser() {
        return ageUser;
    }

    public void setAgeUser(String age) {
        this.ageUser = age;
    }

    public Location getLocation() {
        return Location;
    }

    public void setLocation(Location location) {
        this.Location = location;
    }

    public String getLevelOfReligion() {
        return levelOfReligion;
    }

    public void setLevelOfReligion(String levelOfReligion) {
        this.levelOfReligion = levelOfReligion;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
