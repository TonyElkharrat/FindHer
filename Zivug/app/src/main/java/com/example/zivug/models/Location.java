package com.example.zivug.models;

public class Location
{
    private  String latitude;
    private  String longitude;
    private  String cityUser;

    public Location()
    {

    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCityUser() {
        return cityUser;
    }

    public void setCityUser(String cityUser) {
        this.cityUser = cityUser;
    }
}

