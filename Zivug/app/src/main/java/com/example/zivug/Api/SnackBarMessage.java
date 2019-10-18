package com.example.zivug.Api;

import android.app.Activity;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class SnackBarMessage
{
    public static void showSnackBar(Activity activity , String message)
    {
        Snackbar snackBar = Snackbar.make(activity.getWindow().getDecorView().getRootView(),message, Snackbar.LENGTH_LONG);
        snackBar.show();
    }
}
