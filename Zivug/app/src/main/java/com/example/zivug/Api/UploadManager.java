package com.example.zivug.Api;

import android.app.Activity;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.zivug.Animations.AnimationMaker;
import com.example.zivug.models.NumberOfPhotos;
import com.example.zivug.notifier.onUploadPhotoNotifier;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import javax.net.ssl.SNIHostName;

public class UploadManager
{
       static onUploadPhotoNotifier m_lisetener;

    public static void setListener(onUploadPhotoNotifier i_listener)
    {
       m_lisetener = i_listener;
    }
    public static void uploadPhoto(final Activity activity , final Uri uri, final StorageReference filePath)
    {

        filePath.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
            {
                AnimationMaker.updateUI(activity,task);
                m_lisetener.onUploadPhoto();
            }
        });
    }


    public static void updateNumberOfPhoto(int numberOfPhotos)
    {
        HashMap hashMap = new HashMap();
        hashMap.put("numberOfPhotos",numberOfPhotos+"");

        final DatabaseReference database = FirebaseDatabase.getInstance().getReference()
                .child("numberOfPhotoUser")
                .child(FirebaseAuth.getInstance().getUid());
               database.setValue(hashMap);

    }
}

