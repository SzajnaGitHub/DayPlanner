package com.inc.dayplanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {
    @Override

    /**
     * funkcja wywolywana w celu utworzenia obiektu klasy NotificationHelper, pobiera context oraz intent i na jego podstawie tworzy obiekt
     * @param context
     * @param intent
     */
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
        notificationHelper.getManager().notify(1,nb.build());
    }
}
