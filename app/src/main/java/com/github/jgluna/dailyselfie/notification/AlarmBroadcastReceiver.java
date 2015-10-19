package com.github.jgluna.dailyselfie.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.github.jgluna.dailyselfie.MainActivity;

public class AlarmBroadcastReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("RECEIVER", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        Intent activityIntent = new Intent(context, MainActivity.class);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(activityIntent);
    }
}
