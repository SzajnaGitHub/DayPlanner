package com.inc.dayplanner;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.inc.dayplanner.Activities.MainActivity;


public class NotificationHelper extends ContextWrapper {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";

    public static final String channe2ID = "channe2ID";
    public static final String channe2Name = "Channel Cancelled Activity";

    private NotificationManager mManager;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
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
        if(MainActivity.notificationReminder==true){
            return new NotificationCompat.Builder(getApplicationContext(), channelID)
                    .setContentTitle(MainActivity.contentActivityReminder)
                    .setContentText("Reminder: "+MainActivity.timeEarlierReminder)
                    .setSmallIcon(R.drawable.ic_notification_icon2);
        }else {
            return new NotificationCompat.Builder(getApplicationContext(), channelID)
                    .setContentTitle("Cancelled Activity")
                    .setContentText("Activity " + CheckCancelledActivities.nameActivityCancelled + " has been canceled")
                    .setSmallIcon(R.drawable.ic_notification_icon2);
        }
    }
}
