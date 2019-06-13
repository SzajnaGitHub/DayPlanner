package com.inc.dayplanner;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.inc.dayplanner.Activities.MainActivity;
/**
 * @author Marcin Szajna, Kacper Seweryn
 *
 * Klasa pozwalająca na stworzenie i zarejestrowanie powiadomienia w systemie android.
 *
 *
 */

public class NotificationHelper extends ContextWrapper {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";

    private NotificationManager mManager;

    /**
     * konstruktor który tworzy kanał powiadomień podczas tworzenia samego obiektu
     *
     * @param base
     *
     */
    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    /**
     * funkcja prywatna odpowiedzialna za utworzenie kanału do zarejestrowania powiadomienia w systemie
     */
    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);

        getManager().createNotificationChannel(channel);
    }
    /**
     * funkcja publiczna odpowiedzialna za przypisanie do zmiennej mManager aktualnego serwisu powiadomień
     */
    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return mManager;
    }

    /**
     * funkcja publiczna odpowiedzialna za ustawienie i zarejestrowanie powiadomienia w systemie
     */
    public NotificationCompat.Builder getChannelNotification() {
        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle(MainActivity.contentActivityReminder)
                .setContentText("Reminder: "+MainActivity.timeEarlierReminder)
                .setSmallIcon(R.drawable.ic_notification_icon2);
    }
}
