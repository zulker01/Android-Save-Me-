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
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.example.saveme.MainActivity.latitude;
import static com.example.saveme.MainActivity.longitude;


public class NotificationReceiver extends AppCompatActivity {

    private Button mulai;

    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;
    FirebaseUser appUser;
    // define firebase object to save information
    private DatabaseReference databaseReferenceofContacts;
    private  DatabaseReference databaseReferenceLocations;
    private  DatabaseReference databaseReferenceofContactsShow;
    // private ArrayList<Pair<String,String>> contacts = new ArrayList <Pair <String,String> > ();
    private ArrayList<Pair<String,String>> currentContacts = new ArrayList <Pair <String,String> > ();
    private ArrayList<Pair<String,Pair<String,String> > > location = new ArrayList<Pair<String,Pair<String,String> > >();
    private StringBuffer buffer;
    String userName ;
    String userEmail ;
    String userPhone ;
    String emergencyNum ="";
    private  Integer retrieveDone = 0;

    // calling
    private static final int REQUEST_CALL = 1;

    //messaging

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // initialize firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();

        /*
        if the user is not logged in , prompt to log in
        */
        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(NotificationReceiver.this,user_login.class));
            Toast.makeText(NotificationReceiver.this, "Please login first", Toast.LENGTH_SHORT).show();
        }
        appUser = firebaseAuth.getCurrentUser();
        databaseReferenceLocations = FirebaseDatabase.getInstance().getReference("Users/"+appUser.getUid());

        buffer = new StringBuffer();

        
        MainActivity.makezero();
        sendNotification();

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

    public void sendMessage(String phoneNumber) {

        String smsMessage = "I am in danger, HELP ! \n\n I am at " +
                "\n Latitude : " + latitude + "\nLongitude :  " + longitude + " \nLink : www.google.com/maps/place/" + latitude + "," + longitude + "\n" ;

        if (checkMessagePermission(Manifest.permission.SEND_SMS)) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, smsMessage, null, null);
            Toast.makeText(NotificationReceiver.this, "Message Sent!", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(NotificationReceiver.this,
                    new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
            Toast.makeText(NotificationReceiver.this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }
    public void call(String number) {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+number));

        if( ActivityCompat.checkSelfPermission(NotificationReceiver.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
        {
            Toast menuToast = Toast.makeText(NotificationReceiver.this,R.string.phonePermission, Toast.LENGTH_LONG);
            menuToast.show();
            ActivityCompat.requestPermissions(NotificationReceiver.this,
                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            // return ;
        }
        else {
            startActivity(callIntent);
        }
    }

    public boolean checkMessagePermission(String permission){
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
    
    //firebase
    @Override
    protected void onStart() {
        super.onStart();
        databaseReferenceLocations.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // currentUser = dataSnapshot.getValue(User.class);
                userName = dataSnapshot.child("name").getValue().toString();
                userEmail = dataSnapshot.child("email").getValue().toString();
                userPhone = dataSnapshot.child("phone").getValue().toString();


                GenericTypeIndicator<ArrayList<Pair<String,String>>> t = new GenericTypeIndicator<ArrayList<Pair<String,String>>>() {};
                //currentContacts = dataSnapshot.child("contacts").getValue(t);

                //Retrieve contacts
                long d = dataSnapshot.child("contacts").getChildrenCount();
                String contactName="";
                String contactPhone="";
                currentContacts.clear();


                for(Integer i=0;i<d;i++)
                {
                    contactName = dataSnapshot.child("contacts").child(i.toString()).child("first").getValue(String.class);
                    contactPhone = dataSnapshot.child("contacts").child(i.toString()).child("second").getValue(String.class);
                    currentContacts.add(new Pair <String,String> (contactName, contactPhone));
                    break;

                }
                emergencyNum = contactPhone;
                call(emergencyNum);
                sendMessage(emergencyNum);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

