package com.example.smartmediaschedular;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class Remainder extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String title=intent.getStringExtra(Intent.EXTRA_TITLE);
        String[] split=title.split("-");
        NotificationHelper notificationHelper = new NotificationHelper(context,title);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
        notificationHelper.getManager().notify(Integer.parseInt(split[3]), nb.build());


    }
}
