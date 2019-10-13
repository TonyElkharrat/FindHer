package com.example.zivug;

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
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.example.zivug.Activities.FindHerActivity;
import com.example.zivug.Activities.MainActivity;
import com.example.zivug.fragments.ChatFragment;
import com.example.zivug.models.Message;
import com.example.zivug.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.ExecutionException;

public class ChatNotification extends Service
{

    Bitmap bitmap;
    String userSender;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        dataChangeListener();
        return super.onStartCommand(intent, flags, startId);

    }

    private void dataChangeListener()
    {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                Query lastQuery = databaseReference.child("Chats").orderByKey().limitToLast(1);
                lastQuery.addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        for (DataSnapshot snapshot: dataSnapshot.getChildren())
                        {
                            final Message message = snapshot.getValue(Message.class);
                            if(message.getUserReceiver().equals(FirebaseAuth.getInstance().getUid())&& message.getIsRead()==false)
                            {

                                databaseReference.child("Users").child(message.getUserSender()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                    @Override
                                                                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                                                            User user = dataSnapshot.getValue(User.class);
                                                                                                                            createNotification(message,user.getUserName() );

                                                                                                                    }

                                                                                                                    @Override
                                                                                                                    public void onCancelled(@NonNull DatabaseError databaseError)
                                                                                                                    {

                                                                                                                    }
                                                                                                                });



                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle possible errors.
                    }
                });
//                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
//                    {
//                    Message messageI = dataSnapshot1.getValue(Message.class);
//                    int a =0;
//                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });

    }

    private void createNotification(final Message message, String userSenderName)
    {

        NotificationManager    mNotificationManager =      (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder =  new NotificationCompat.Builder(getApplicationContext(), "notify_001");
        Intent notificationIntent = new Intent(getApplicationContext(), FindHerActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(message.getMessage());

        bigText.setBigContentTitle(userSenderName);
        bigText.setSummaryText("New message from Zivug");

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.drawable.ic_romantic_message);
        mBuilder.setContentTitle("Your Title");
        mBuilder.setContentText("");
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText);
        mBuilder.setAutoCancel(true);


// === Removed some obsoletes
        if (Build.VERSION.SDK_INT >=26)
        {
            String channelId = "messageId";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        mNotificationManager.notify(0, mBuilder.build());

        // }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }


}
