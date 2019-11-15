package com.example.saveme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;

import static com.example.saveme.MainActivity.latitude;
import static com.example.saveme.MainActivity.longitude;

public class Istant extends AppCompatActivity {

    // calling
    private static final int REQUEST_CALL = 1;

    //messaging

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_istant);
        sendMessage();
        call();
    }

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




    }
