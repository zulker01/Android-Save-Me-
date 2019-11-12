package com.example.saveme;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;



public class NotificationReceiver extends BroadcastReceiver {
    public NotificationReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
// fire notification

        // Gets an instance of the NotificationManager service
        final NotificationManager mgr = (NotificationManager)     context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("")
                        .setContentText("You have new horoscope");
        // This pending intent will open after notification click
        Intent resultIntent = new Intent(context, MainActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(context, 0, resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        //mBuilder.setContentIntent(resultPendingIntent);
        //Toast.makeText(this,"tapped on notification ",Toast.LENGTH_LONG).show();
       // Toast.makeText(MainActivity.class,"tapped noti",Toast.LENGTH_LONG).show();
        mBuilder.setAutoCancel(true);
        // Sets an ID for the notification
        int mNotificationId = 001;
        // Builds the notification and issues it.
        mgr.notify(mNotificationId, mBuilder.build());
    }
}

/*
public class NotificationReceiver extends IntentService {
    private static final int NOTIF_ID = 1;

    public AlarmReceiver() {
        super("AlarmReceiver");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Intent intnt = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder b = new NotificationCompat.Builder(getApplicationContext());

        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_notifications)
                .setTicker("Hearty365")
                .setContentTitle("Are you safe?")
                .setContentText("If not Tap Here")
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent)
                .setContentInfo("Info");


        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, b.build());
    }

}


 */



/*public class NotificationReceiver extends Service {

    private final static String TAG = "ShowNotification";

    @Override
    public void onCreate() {
        super.onCreate();

        Intent mainIntent = new Intent(this, MainActivity.class);

        NotificationManager notificationManager
                = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification noti = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(this, 0, mainIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT))
                .setContentTitle("HELLO " + System.currentTimeMillis())
                .setContentText("PLEASE CHECK WE HAVE UPDATED NEWS")
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_notifications)
                .setTicker("ticker message")
                .setWhen(System.currentTimeMillis())
                .build();

        notificationManager.notify(0, noti);

        Log.i(TAG, "Notification created");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }


}

 */
