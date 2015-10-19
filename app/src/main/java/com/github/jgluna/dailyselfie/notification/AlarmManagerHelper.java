package com.github.jgluna.dailyselfie.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class AlarmManagerHelper {

    private final Context context;

    public AlarmManagerHelper(Context context) {
        this.context = context;
    }

    public boolean setAlarm(int hourOfDay) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        cal.add(Calendar.MINUTE, 2);
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        return true;
    }

    public boolean updateAlarm() {
        return false;
    }

    public boolean deleteAlarm() {
        return false;
    }

}
