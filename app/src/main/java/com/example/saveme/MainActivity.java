package com.example.saveme;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    CardView callButton;
    CardView messageButton;
    CardView voiceButton;

    public static double latitude = 0;
    public static  double longitude = 0;
    ShowContactsActivity showContactsActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //navigation things
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        callButton=findViewById(R.id.callButton);
        voiceButton=findViewById(R.id.voiceButton);
        messageButton=findViewById(R.id.messageButton);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 call();
            }
        });



        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    String phoneNumber="785373724";
                    String smsMessage = "I am in danger, HELP ! \n\n I am at " +getLocationPage.cityName+"\n Latitude : "+latitude+"\nLongitude :  "+longitude+" ";

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

        });

        voiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, voiceCommand.class);
                intent.putExtra("pak",100);
                startActivity(intent);
            }
        });



    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.emergencyContacts: {
                Toast.makeText(this, "hoise", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, ShowContactsActivity.class);
                intent.putExtra("pak",100);
                startActivity(intent);
                break;
            }


        }
            drawer.closeDrawer(GravityCompat.START);
            return true;

    }

    private void call() {
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

    private void sendMessage(){
        String number="01785373724";
        String pak =  getLocationPage.cityName;
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO,
                Uri.parse("sms:"+number));
        smsIntent.putExtra("sms_body", "Help Me, I am in Danger"+pak);
        startActivity(smsIntent);
    }

    private void sendVoice(){

    }
}




