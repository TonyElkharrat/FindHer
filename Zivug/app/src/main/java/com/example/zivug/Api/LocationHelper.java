package com.example.zivug.Api;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationHelper
{

    public  static  boolean isCityInRadius(int radius, double otherCityLongitude,double otherCityLatitude)
    {
        float[] results = new float[1];
        Location.distanceBetween(32.011261, 34.774811, otherCityLatitude, otherCityLongitude, results);
        float distanceInMeters = results[0];
        boolean isWithinRadius = distanceInMeters < radius;
        return isWithinRadius;
    }

    public static void getLocationUser(final Context context)
    {
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(context);

        LocationCallback callback = new LocationCallback()
        {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
                JsonParser.ParseJsonFromLongitudeAndLatitudeToCity("https://api.opencagedata.com/geocode/v1/json?q=",context,location.getLatitude()+"", location.getLongitude()+"");

            }
        };

        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        client.requestLocationUpdates(request,callback,null);

    }


}
