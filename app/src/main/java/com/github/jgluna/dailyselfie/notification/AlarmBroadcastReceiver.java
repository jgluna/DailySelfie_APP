package com.github.jgluna.dailyselfie.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.github.jgluna.dailyselfie.NotificationActivity;
import com.github.jgluna.dailyselfie.R;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent activityIntent = new Intent(context, NotificationActivity.class);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, activityIntent, 0);
        Notification notification = new Notification.Builder(context)
                .setContentTitle("Daily Selfie")
                .setContentText("Time to take your selfie!!")
                .setSmallIcon(R.mipmap.ic_launcher).setContentIntent(pendingIntent)
                .build();
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        manager.notify(0, notification);
    }

}
