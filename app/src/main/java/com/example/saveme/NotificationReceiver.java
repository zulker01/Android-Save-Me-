package com.example.saveme;

import android.Manifest;
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
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.example.saveme.MainActivity.latitude;
import static com.example.saveme.MainActivity.longitude;


public class NotificationReceiver extends AppCompatActivity {

    private Button mulai;

    // calling
    private static final int REQUEST_CALL = 1;

    //messaging

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        callNow();
        sendLoction();
        MainActivity.makezero();
        sendNotification();

      /* try {
           start();
        } catch (IOException e) {
            //e.printStackTrace();
        }


        Button stop = (Button) findViewById(R.id.btnstop);

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                }
            }
        });

       */

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_receiver);


        Button menu = (Button) findViewById(R.id.btnmenu);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationReceiver.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }


    public void callNow() {
        String number = "01785373724";
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));

        if (ActivityCompat.checkSelfPermission(NotificationReceiver.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Toast menuToast = Toast.makeText(NotificationReceiver.this, R.string.phonePermission, Toast.LENGTH_LONG);
            menuToast.show();

            ActivityCompat.requestPermissions(NotificationReceiver.this,
                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);

        }
        else {
            startActivity(callIntent);
        }

        }

    public void sendLoction() {
        String phoneNumber = "785373724";
        String smsMessage = "I am in danger, HELP ! \n\n I am at " + "\n Latitude : " + latitude + "\nLongitude :  " + longitude + getCompleteAddressString(latitude,longitude);

        if (checkPermission(Manifest.permission.SEND_SMS)) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, smsMessage, null, null);
            Toast.makeText(NotificationReceiver.this, "Message Sent!", Toast.LENGTH_SHORT).show();

        } else {

        }else {
            ActivityCompat.requestPermissions(NotificationReceiver.this,
                    new String[]{Manifest.permission.SEND_SMS},MY_PERMISSIONS_REQUEST_SEND_SMS);


            Toast.makeText(NotificationReceiver.this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkPermission(String permission) {
        int check = ContextCompat.checkSelfPermission(NotificationReceiver.this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    public void sendNotification()
    {
        Intent intent1 = new Intent(getApplicationContext(),NotificationReceiver.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder b = new NotificationCompat.Builder(getApplicationContext());

        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_notification)
                .setTicker("Hearty365")
                .setContentTitle("Are you safe?")
                .setContentText("If not Tap Here")
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent)
                .setContentInfo("Info");


        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, b.build());
    }


    public String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                //Log.w("My Current loction address", strReturnedAddress.toString());
            } else {
                //Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }
}


    /*final static MediaRecorder recorder = new MediaRecorder();
    public static  String path;

    public NotificationReceiver(String path) {
        this.path = sanitizePath(path);
    }

    public static String sanitizePath(String path) {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        if (!path.contains(".")) {
            path += ".3gp";
        }
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + path;
    }

    public static void start() throws IOException {
        String state = android.os.Environment.getExternalStorageState();
        if (!state.equals(android.os.Environment.MEDIA_MOUNTED)) {
            throw new IOException("SD Card is not mounted.  It is " + state
                    + ".");
        }

        // make sure the directory we plan to store the recording in exists
        File directory = new File(path).getParentFile();
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("Path to file could not be created.");
        }

        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(path);
        recorder.prepare();
        recorder.start();
    }

    public static void stop() throws IOException {
        recorder.stop();
        recorder.release();
    }

    public static void playarcoding(String path) throws IOException {
        MediaPlayer mp = new MediaPlayer();
        mp.setDataSource(path);
        mp.prepare();
        mp.start();
        mp.setVolume(10, 10);
    }
}

     */



//public class NotificationReceiver extends BroadcastReceiver {
    /*
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

     */

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
