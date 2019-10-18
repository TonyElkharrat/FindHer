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
   static Location locationObject;

    public  static  boolean isCityInRadius(int radius, double otherCityLongitude,double otherCityLatitude)
    {
        float[] results = new float[1];
        Location.distanceBetween(32.011261, 34.774811, otherCityLatitude, otherCityLongitude, results);
        float distanceInMeters = results[0];
        boolean isWithinRadius = distanceInMeters < radius;
        return isWithinRadius;
    }

    public static void getCityUser(final Context context)
    {
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(context);
        LocationCallback callback = new LocationCallback()
        {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location s = locationResult.getLastLocation();
                locationObject = s;
                StringBuilder longitudeAndLatitude = new StringBuilder();
                longitudeAndLatitude.append(s.getLatitude()+"+"+s.getLongitude());
                JsonParser.ParseJsonFromLongitudeAndLatitudeToCity("https://api.opencagedata.com/geocode/v1/json?q=",context,longitudeAndLatitude.toString());

            }
        };

        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        client.requestLocationUpdates(request,callback,null);
//               request.setInterval(5000);
//               request.setFastestInterval(5000);

    }
}
