package com.ankita.dell.quiz_app.BroadcastReceiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.ankita.dell.quiz_app.MainActivity;
import com.ankita.dell.quiz_app.R;

/**
 * Created by Dell on 20-Jan-19.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        long when= System.currentTimeMillis();
      //  int NOTIFICATION_ID=234; //New1
        NotificationManager notificationManager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

       /* if (android.os.Build.VERSION.SDK_INT >=android.os.Build.VERSION_CODES.0)
        {
            String CHANNEL_ID="my_channel_01";
            CharSequence name="my_channel";
            String Description="This is my channel";
            int importance=NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mchannel=new NotificationChannel(CHANNEL_ID,name,importance);
            mchannel.setDescription(Description);
            mchannel.enableLights(true);
            mchannel.setLightColor(Color.RED);
            mchannel.enableVibration(true);
            mchannel.setVibrationPattern(new long[]{100,200,300,400,500,400,300,200,400});
            mchannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mchannel);
    }*/
        Intent notificationIntent=new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        Uri alarmSound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder=new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Online Quiz App")
                .setContentText("Hey ! Try to solve question today")
                .setSound(alarmSound)
                .setAutoCancel(true)
                .setWhen(when)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{1000,1000,1000,1000,1000});

        notificationManager.notify(0,builder.build());

    }
}
