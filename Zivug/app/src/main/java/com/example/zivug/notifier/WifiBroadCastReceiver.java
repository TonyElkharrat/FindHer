package com.example.zivug.notifier;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.wifi.WifiManager;

import com.example.zivug.Api.UploadManager;
import com.example.zivug.fragments.AccountProfileFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.content.Context.MODE_PRIVATE;


public class WifiBroadCastReceiver extends BroadcastReceiver
{
   private  static onUploadPhotoNotifier listener;

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (action.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
            if (intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false))
            {
                SharedPreferences pref = context.getSharedPreferences("photoToSend", MODE_PRIVATE);
                String uri = pref.getString("photoSaved",null);
                if(uri!=null)
                {

                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(FirebaseAuth.getInstance().getUid());
                    StorageReference filePath = storageReference.child(AccountProfileFragment.m_numberOfPhotos + ".jpg");
                    Uri pathUri = Uri.parse(uri);
                    UploadManager.updateNumberOfPhoto(AccountProfileFragment.m_numberOfPhotos);
                    UploadManager.uploadPhoto((Activity) context,pathUri,filePath);
                    listener.onUploadPhoto();
                }
            } else {
                // wifi connection was lost
            }
        }
    }

    public static void setListener(onUploadPhotoNotifier i_listener)
    {
       listener = i_listener;
    }
}
