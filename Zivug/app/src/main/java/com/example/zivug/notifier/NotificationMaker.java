package com.example.zivug.notifier;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.zivug.Activities.ZivugActivity;
import com.example.zivug.R;
import com.example.zivug.models.Message;

public class NotificationMaker
{
   static AlarmManager alarmManager;
   static PendingIntent alarmPendingIntent;
    public  void programNotification(int time,Context context , final String message, String title, int icon)
    {
        alarmManager =  (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmPendingIntent = createPendingIntent(context);
        alarmManager.setRepeating(AlarmManager.RTC,System.currentTimeMillis(),time,alarmPendingIntent);
    }

    public void removeProgram()
    {
        alarmManager.cancel(alarmPendingIntent);
    }

    private PendingIntent createPendingIntent(Context context)
    {
        Intent notificationIntent = new Intent(context, ZivugActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    public  void makeNotification(Context context , final String message, String title, int icon)
    {

            NotificationManager mNotificationManager =      (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder =  new NotificationCompat.Builder(context, "notify_001");
            PendingIntent pendingIntent = createPendingIntent(context);

            NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
            bigText.bigText(message);
            bigText.setBigContentTitle(title);
            bigText.setSummaryText("New notification from Zivug");
            mBuilder.setContentIntent(pendingIntent);
            mBuilder.setSmallIcon(icon);
            mBuilder.setContentTitle("Your Title");
            mBuilder.setContentText("");
            mBuilder.setPriority(Notification.PRIORITY_MAX);
            mBuilder.setStyle(bigText);
            mBuilder.setAutoCancel(true);


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

    }
}
