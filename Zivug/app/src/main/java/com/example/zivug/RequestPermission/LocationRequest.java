package com.example.zivug.RequestPermission;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.example.zivug.Activities.MainActivity;

public class LocationRequest implements ActivityCompat.OnRequestPermissionsResultCallback
{
    Activity activity;
    private static final int REQUEST_LOCATION_CODE = 1;

    public LocationRequest(Activity activity)
    {
        this.activity = activity;
    }

    public  void makeLocationRequest()
    {
        if (Build.VERSION.SDK_INT >= 23)
        {
            int haspermission = activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);

            if (haspermission != PackageManager.PERMISSION_GRANTED)
            {
                activity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            }

            else
            {

            }
    }

}
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        activity.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1&& grantResults[0]!= PackageManager.PERMISSION_GRANTED)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Attention").setMessage("We need LocationHelper access to inform you about weather" +
                    "around your location").setPositiveButton("I understand", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:"+activity.getPackageName()));
                    activity.startActivity(intent);
                }
            }).setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    activity.finish();
                }
            }).setCancelable(false).show();
        }

        else
            {

        }

    }

}

