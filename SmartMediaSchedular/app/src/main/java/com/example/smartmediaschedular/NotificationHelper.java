package com.example.smartmediaschedular;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Vibrator;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;

public class NotificationHelper extends ContextWrapper {

    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";

    private NotificationManager mManager;

    String sender,receiver,msg,title,string;

    public NotificationHelper(Context base,String title) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();

            String[] split=title.split("-");

            sender=split[0];

            receiver=split[1];
            msg=split[2];


        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification() {
        Vibrator v=(Vibrator) this.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(1000);
        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.logo))
                .setContentTitle(sender+" : "+receiver)
                .setContentText(msg)
                .setSmallIcon(R.drawable.logo)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setStyle(new NotificationCompat.BigTextStyle().bigText("\n"+sender+" : "+receiver+"\n\n"+msg)
                        .setSummaryText("Remainder of Scheduling !"));

    }

}

//.setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.logo))
//.setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle("Receiver : "+receiver)
//                        .bigText(msg)