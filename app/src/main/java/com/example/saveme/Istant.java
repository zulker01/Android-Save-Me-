package com.example.saveme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.SmsManager;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.example.saveme.MainActivity.dangerCheckCounter;

import static com.example.saveme.MainActivity.latitude;
import static com.example.saveme.MainActivity.longitude;
import static java.util.concurrent.TimeUnit.SECONDS;

public class Istant extends AppCompatActivity {

    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;
    FirebaseUser appUser;
    // define firebase object to save information

    private DatabaseReference databaseReferenceLocations;
    // contacts array
    private ArrayList<Pair<String, String>> currentContacts = new ArrayList<Pair<String, String>>();
    private StringBuffer buffer;
    // user profile of current user
    String userName;
    String userEmail;
    String userPhone;
    String emergencyNum = "";

    // calling
    private static final int REQUEST_CALL = 1;

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    /*Button record=findViewById(R.id.record);
    Button stop=findViewById(R.id.stop);
    Button play=findViewById(R.id.play);
    Button playstop=findViewById(R.id.playstop);
    String pathsave="";
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    final int REQUEST_PERMISSION_CODE=1000;
    */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_istant);
        /*
        if(checkPermissionFromDevice())
        {
            record.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pathsave= Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+UUID.randomUUID().toString()
                            +"_save_me.3gp";
                    setupMediaRecorder();
                    try{
                        mediaRecorder.prepare();
                        mediaRecorder.start();

                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }
                    play.setEnabled(false);
                    playstop.setEnabled(false);

                    Toast.makeText(Istant.this,"Recoeding...",Toast.LENGTH_SHORT).show();

                }
            });

            stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mediaRecorder.stop();
                    stop.setEnabled(false);
                    play.setEnabled(true);
                    record.setEnabled(true);
                    playstop.setEnabled(false);
                }
            });

            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stop.setEnabled(false);
                    play.setEnabled(false);
                    record.setEnabled(false);
                    playstop.setEnabled(true);
                    mediaPlayer=new MediaPlayer();
                    try{
                        mediaPlayer.setDataSource(pathsave);
                        mediaPlayer.prepare();

                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }


                }
            });
            playstop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    stop.setEnabled(false);
                    play.setEnabled(true);
                    record.setEnabled(true);
                    playstop.setEnabled(false);
                    if(mediaPlayer!=null)
                    {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        setupMediaRecorder();

                    }
                }

            });

        }
        else
        {
            requestPermissions();
        }


    }

    private void setupMediaRecorder() {
        mediaRecorder= new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(pathsave);
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        },REQUEST_PERMISSION_CODE);

         */




    /*
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode)
        {
            case REQUEST_PERMISSION_CODE:
            {
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this,"Pemission Granted",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this,"Pemission Denied",Toast.LENGTH_SHORT).show();

            }
        }
    }

     */

        // initialize firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();

        /*
        if the user is not logged in , prompt to log in
        */
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(Istant.this, user_login.class));
            Toast.makeText(Istant.this, "Please login first", Toast.LENGTH_SHORT).show();
        }

        // getting current user
        appUser = firebaseAuth.getCurrentUser();
        databaseReferenceLocations = FirebaseDatabase.getInstance().getReference("Users/" + appUser.getUid());

        buffer = new StringBuffer();

    }


    public void sendMessage(String phoneNumber) {


        String smsMessage = "I am in danger, HELP ! \n\n I am at " +
                "\n Latitude : " + latitude + "\nLongitude :  " + longitude + " \nLink : www.google.com/maps/place/" + latitude + "," + longitude + "\n";

        if (checkMessagePermission(Manifest.permission.SEND_SMS)) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, smsMessage, null, null);
            Toast.makeText(Istant.this, "Message Sent!", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(Istant.this,
                    new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
            Toast.makeText(Istant.this, "Permission Needed to send message", Toast.LENGTH_SHORT).show();
        }
    }

    public void call(String number) {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));

        if (ActivityCompat.checkSelfPermission(Istant.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Toast menuToast = Toast.makeText(Istant.this, R.string.phonePermission, Toast.LENGTH_LONG);
            menuToast.show();
            ActivityCompat.requestPermissions(Istant.this,
                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);

        } else {
            startActivity(callIntent);
        }
    }

    public boolean checkMessagePermission(String permission) {
        int check = ContextCompat.checkSelfPermission(Istant.this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }
    /*


    private boolean checkPermissionFromDevice()
    {
        int write_external_storage_result=ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result=ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO);
        return write_external_storage_result==PackageManager.PERMISSION_GRANTED &&
                record_audio_result==PackageManager.PERMISSION_GRANTED;
    }

     */


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

                //Retrieve contacts
                long d = dataSnapshot.child("contacts").getChildrenCount();
                String contactName = "";
                String contactPhone = "";
                currentContacts.clear();

                // getting contacts
                for (Integer i = 0; i < d; i++) {
                    contactName = dataSnapshot.child("contacts").child(i.toString()).child("first").getValue(String.class);
                    contactPhone = dataSnapshot.child("contacts").child(i.toString()).child("second").getValue(String.class);
                    currentContacts.add(new Pair<String, String>(contactName, contactPhone));
                    break;

                }
                // calling and messaging to emergency contact
                emergencyNum = contactPhone;
                call(emergencyNum);
                sendMessage(emergencyNum);
                Toast.makeText(Istant.this, "Contact " + d + " " + contactName + " " + contactPhone + " " + userEmail + " " + userName + " " + userPhone, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
