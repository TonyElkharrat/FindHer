package com.example.zivug.notifier;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.zivug.Activities.ZivugActivity;
import com.example.zivug.R;
import com.example.zivug.models.Message;
import com.example.zivug.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.jar.Attributes;

public class ChatNotification extends IntentService
{

    Bitmap bitmap;
    String userSender;

    public ChatNotification(String name) {
        super("My Intent ");
    }
    public ChatNotification() {
        super("My Intent ");
    }
    boolean isMessageRead;


    private void dataChangeListener()
    {

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final Message message = snapshot.getValue(Message.class);
                    if (message != null) {
                        if (message.getUserReceiver().equals(FirebaseAuth.getInstance().getUid()) && message.getIsRead() == false)
                        {
                            isMessageRead = message.getIsRead();
                            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
                            databaseReference.child(message.getUserSender()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if(isMessageRead==false)
                                    {
                                        User user = dataSnapshot.getValue(User.class);
                                        NotificationMaker notificationMaker = new NotificationMaker();

                                        notificationMaker.makeNotification(getApplicationContext(), "New notification from Zivug", user.getUserName(), R.drawable.ic_romantic_message);
                                        isMessageRead = true;
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {
            }
        });

    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent)
    {
        dataChangeListener();

    }


}
