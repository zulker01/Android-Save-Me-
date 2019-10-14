
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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.util.*;

import org.w3c.dom.Text;

import static java.lang.System.exit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button menuButton = (Button) findViewById(R.id.homeMenuButton);
        Button quitButton = (Button) findViewById(R.id.homeQuitButton);
        Button call=(Button) findViewById(R.id.homeCall);
        Button message=(Button) findViewById(R.id.homeSendLocation);


        menuButton.setOnClickListener(new View.OnClickListener()
                                      {
                                          @Override
                                          public void onClick(View view)
                                          {


                                              Intent intent = new Intent(MainActivity.this, menuPage.class);

                                              startActivity(intent);
                                              finish();
                                          }
                                      }
        );
        quitButton.setOnClickListener(new View.OnClickListener()
                                      {
                                          @Override
                                          public void onClick(View view)
                                          {

                                              exit(0);
                                          }
                                      }
        );

        call.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View view)
                                    {
                                        String number="01785373724";
                                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                                        callIntent.setData(Uri.parse("tel:"+number));

                                        if( ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
                                        {
                                            Toast menuToast = Toast.makeText(MainActivity.this,R.string.phonePermission, Toast.LENGTH_LONG);
                                            menuToast.show();
                                            return ;
                                        }
                                        startActivity(callIntent);

                                    }
                                }
        );



        message.setOnClickListener(new View.OnClickListener()
                                   {
                                       @Override
                                       public void onClick(View view)
                                       {
                                           String phoneNumber="0178537372";
                                           String smsMessage = "I am in danger, HELP ! \n\n I am at " +getLocationPage.cityName+"\n Latitude : "+getLocationPage.latitude+"\nLongitude :  "+getLocationPage.longitude+" ";

                                           if(checkPermission(Manifest.permission.SEND_SMS)){
                                               SmsManager smsManager = SmsManager.getDefault();
                                               smsManager.sendTextMessage(phoneNumber, null, smsMessage, null, null);
                                               Toast.makeText(MainActivity.this, "Message Sent!", Toast.LENGTH_SHORT).show();
                                           }else {
                                               Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                                           }

                                           /* done by mahodi :
                                            Intent smsIntent = new Intent(Intent.ACTION_SENDTO,
                                                   Uri.parse("sms:"+phoneNumber));
                                           smsIntent.putExtra("sms_body", smsMessage);
                                           startActivity(smsIntent);

                                           */


                                       }
                                       public boolean checkPermission(String permission){
                                           int check = ContextCompat.checkSelfPermission(MainActivity.this, permission);
                                           return (check == PackageManager.PERMISSION_GRANTED);
                                       }
                                   }
        );



    }
}


