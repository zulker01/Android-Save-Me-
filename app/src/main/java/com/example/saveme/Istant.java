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

import static com.example.saveme.MainActivity.latitude;
import static com.example.saveme.MainActivity.longitude;

public class Istant extends AppCompatActivity {

    // calling
    private static final int REQUEST_CALL = 1;

    //messaging

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
        sendMessage();
        call();
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

    }


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

    public void sendMessage() {
        String phoneNumber = "05373724";
        String smsMessage = "I am in danger, HELP ! \n\n I am at " +
                "\n Latitude : " + latitude + "\nLongitude :  " + longitude + " \nLink : www.google.com/maps/place/" + latitude + "," + longitude + "\n" ;

        if (checkMessagePermission(Manifest.permission.SEND_SMS)) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, smsMessage, null, null);
            Toast.makeText(Istant.this, "Message Sent!", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(Istant.this,
                    new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
            Toast.makeText(Istant.this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }
    public void call() {
        //DatabaseHelper databaseHelper = new DatabaseHelper(null);
        //String mara = DatabaseHelper.getInstance().getNumber("1");
        //System.out.println(mara);
        String number="01521255917";
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+number));

        if( ActivityCompat.checkSelfPermission(Istant.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
        {
            Toast menuToast = Toast.makeText(Istant.this,R.string.phonePermission, Toast.LENGTH_LONG);
            menuToast.show();
            ActivityCompat.requestPermissions(Istant.this,
                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            // return ;
        }
        else {
            startActivity(callIntent);
        }
    }

    public boolean checkMessagePermission(String permission){
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




    }
