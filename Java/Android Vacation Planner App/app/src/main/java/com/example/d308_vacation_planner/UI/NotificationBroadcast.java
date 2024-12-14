package com.example.d308_vacation_planner.UI;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.d308_vacation_planner.R;


public class NotificationBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent)
    {
        int id = intent.getIntExtra("notificationId", -1);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(new NotificationChannel(id+"", "Vacation Notifications", NotificationManager.IMPORTANCE_DEFAULT));

        Notification notification = new NotificationCompat.Builder(context, id+"").setContentText(intent.getStringExtra("notificationText")).setContentTitle(intent.getStringExtra("notificationTitle")).setSmallIcon(R.drawable.ic_launcher_background).build();
        manager.notify(id, notification);
    }

}
