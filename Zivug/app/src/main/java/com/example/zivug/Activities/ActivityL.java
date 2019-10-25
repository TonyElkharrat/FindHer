package com.example.zivug.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.zivug.Api.JsonParser;
import com.example.zivug.Api.LocationHelper;
import com.example.zivug.notifier.cityListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ActivityL extends Activity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        LocationHelper.getLocationUser(ActivityL.this);
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid()).child("Location");
        JsonParser.SetListener(new cityListener() {
            @Override
            public void getLocation(String city,String latitude,String longitude)
            {
                HashMap locationUser = new HashMap();
                locationUser.put("cityUser",city);
                locationUser.put("latitude",latitude);
                locationUser.put("longitude",longitude);
                reference.updateChildren(locationUser);
                Intent intent = new Intent(ActivityL.this,ZivugActivity.class);
                startActivity(intent);
            }
        });
    }

}
