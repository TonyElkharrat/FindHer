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

public class LocationRequest
{
    Activity activity;

    public LocationRequest(Activity activity)
    {
        this.activity = activity;
    }



}

