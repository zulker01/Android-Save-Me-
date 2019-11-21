package com.example.saveme;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Calendar;


import java.util.List;
import java.util.Locale;


import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


import static com.google.android.gms.common.api.GoogleApiClient.*;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.Date;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ConnectionCallbacks,
        OnConnectionFailedListener, LocationListener {
    //UI navigation items
    DrawerLayout drawer;
    CardView callButton;
    CardView messageButton;
    CardView voiceButton;

    //firebase
    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;
    FirebaseUser appUser;
    // define firebase object to save information
    private DatabaseReference databaseReferenceLocations;
    private ArrayList<Pair<String, String>> currentContacts = new ArrayList<Pair<String, String>>();
    private ArrayList<Pair<String, Pair<String, String>>> location = new ArrayList<Pair<String, Pair<String, String>>>();
    private StringBuffer buffer;
    String userName;
    String userEmail;
    String userPhone;

    private Integer retrieveDone = 0;
    public long initialTimetoCheckDataFound = 1;
    public long delayTimetoCheckDataFound = 3;
    public  String emergencyNum = "";
    public static Boolean allowCheckSwitch = false;

    //firebase

    // calling
    private static final int REQUEST_CALL = 1;

    //messaging

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    // danger notification
    public static int dangerCheckCounter = 0;

    public static void makezero() {
        dangerCheckCounter = 0;
    }


    // getting date time
    private static final DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public static double latitude = 23.7287984;
    public static double longitude = 90.3990832;

    // scheduling notification


    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    //scheduling notification

    public long initialTimetoCheckLocation = 2;
    public long delayTimetoCheckLocation = 10;
    // location

    final String TAG = "GPS";
    private final static int ALL_PERMISSIONS_RESULT = 101;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;


    LocationManager locationManager;
    Location loc;
    ArrayList<String> permissions = new ArrayList<>();
    ArrayList<String> permissionsToRequest;
    ArrayList<String> permissionsRejected = new ArrayList<>();
    boolean isGPS = false;
    boolean isNetwork = false;
    boolean canGetLocation = true;
    //location

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        //firebase
        // initialize firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();

        /*
        if the user is not logged in , prompt to log in
        */
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(MainActivity.this, user_login.class));
            Toast.makeText(MainActivity.this, "Please login first", Toast.LENGTH_SHORT).show();
        }

        // current app user
        appUser = firebaseAuth.getCurrentUser();
        databaseReferenceLocations = FirebaseDatabase.getInstance().getReference("Users/" + appUser.getUid());

        buffer = new StringBuffer();


        ScheduledExecutorService schedulerToCheckDataFound = Executors.newSingleThreadScheduledExecutor();
        //firebase

        if(allowCheckSwitch) {
            // this will send notification  if stayed in unknown place
            scheduler.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {

                    //this will check for locations in every 30 minutes
                    // if user is in unknown location for 2 hours
                    // a notification will be sent

                    if (TestSafe()) {
                        dangerCheckCounter = 0;
                    } else {
                        dangerCheckCounter++;
                    }
                    if (dangerCheckCounter % 6 == 0) {
                        sendNotificationForLocation();
                    }
                    //if user stays in unknown location and notification not answered for several hours
                    // then auto call and message will be sent
                    if (dangerCheckCounter > 12) {
                        call(emergencyNum);
                        sendMessage(emergencyNum);
                    }
                }
            }, initialTimetoCheckLocation, delayTimetoCheckLocation, SECONDS);
        }
        int b = 0;
        if (b == 0) {
            sendNotification();
        }


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

        callButton = findViewById(R.id.callButton);
        voiceButton = findViewById(R.id.voiceButton);
        messageButton = findViewById(R.id.messageButton);


        //location

        locationManager = (LocationManager) getSystemService(Service.LOCATION_SERVICE);
        isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);

        if (!isGPS && !isNetwork) {
            Log.d(TAG, "Connection off");
            showSettingsAlert();
            getLastLocation();
        } else {
            Log.d(TAG, "Connection on");
            // check permissions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (permissionsToRequest.size() > 0) {
                    requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]),
                            ALL_PERMISSIONS_RESULT);
                    Log.d(TAG, "Permission requests");
                    canGetLocation = false;
                }
            }

            // get location
            getLocation();
        }
        //location

        // works if call button is tapped
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emergencyNum.equals("")) {
                    Toast.makeText(MainActivity.this, "Can't call now, Make sure you have saved a valid number. Or try again later", Toast.LENGTH_SHORT).show();
                } else
                    call(emergencyNum);
            }
        });
        // works if message button is tapped
        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (emergencyNum.equals("")) {
                    Toast.makeText(MainActivity.this, "Can't message now, Make sure you have saved a valid number. Or try again later", Toast.LENGTH_SHORT).show();
                } else
                    sendMessage(emergencyNum);
            }


        });

        voiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, voiceCommand.class);
                intent.putExtra("pak", 100);

                Toast.makeText(MainActivity.this, "location  Sent! " + latitude + " " + longitude, Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        // if app started with voice command ( google assistant ) or tap
        Set<String> categories = getIntent().getCategories();
        if (categories != null && categories.contains(Intent.CATEGORY_LAUNCHER)) {
            //Toast.makeText(MainActivity.this, "started via voice ", Toast.LENGTH_SHORT).show();
            //Log.i(LOGTAG, "app started via voice");
        } else {
            // Log.i(LOGTAG, "app started with user tap");
            //Toast.makeText(MainActivity.this, "tap start ", Toast.LENGTH_SHORT).show();
        }


    }


    public boolean TestSafe() {

        String latti = String.valueOf(latitude);
        String longi = String.valueOf(longitude);
        for (Integer i = 0; i < location.size(); i++) {

            if (latti.equals(location.get(i).second.first) && longi.equals(location.get(i).second.second)) {
                return true;
            }

        }

        return false;
    }

    // method to send notification
    public void sendNotification() {
        Intent intent1 = new Intent(getApplicationContext(), NotificationReceiver.class);
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


        NotificationManager notificationManager = (NotificationManager)
                getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, b.build());
    }

    public void sendNotificationForLocation() {
        Intent intent2 = new Intent(getApplicationContext(), NotificationReceiverForLocation.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent2,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder b = new NotificationCompat.Builder(getApplicationContext());

        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_notification)
                .setTicker("Hearty365")
                .setContentTitle("you are in unknown place for many hours!!")
                .setContentText("If you are safe tap here")
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent)
                .setContentInfo("Info");


        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(2, b.build());
    }


    //method to send message
    public void sendMessage(String phoneNumber) {

        String smsMessage = "I am in danger, HELP ! \n\n I am at " +
                "\n Latitude : " + latitude + "\nLongitude :  " + longitude + " \nLink : www.google.com/maps/place/" + latitude + "," + longitude + "\n" + getCompleteAddressString(latitude, longitude);

        if (checkMessagePermission(Manifest.permission.SEND_SMS)) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, smsMessage, null, null);
            Toast.makeText(MainActivity.this, "Message Sent!", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
            Toast.makeText(MainActivity.this, "Permission needed to send message", Toast.LENGTH_SHORT).show();
        }


    }


    public boolean checkMessagePermission(String permission) {
        int check = ContextCompat.checkSelfPermission(MainActivity.this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }


    // works when back button pressed
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // menu bar options
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

                Intent intent = new Intent(MainActivity.this, ShowContactsActivity.class);
                intent.putExtra("pak", 100);
                startActivity(intent);
                break;
            }
            case R.id.savedLocation: {
                getLocation();
                Intent intent = new Intent(MainActivity.this, ShowSavedLocation.class);
                intent.putExtra("key1", getLattitude());
                intent.putExtra("key2", getLongitude());
                startActivity(intent);
                break;
            }

            case R.id.nav_settings: {

                Intent intent = new Intent(MainActivity.this, settings.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_login: {

                Intent intent = new Intent(MainActivity.this, user_login.class);
                intent.putExtra("pak", 100);
                startActivity(intent);
                //finish();
                break;
            }


        }
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    // method to call
    public void call(String number) {
        //DatabaseHelper databaseHelper = new DatabaseHelper(null);
        //String mara = DatabaseHelper.getInstance().getNumber("1");
        //System.out.println(mara);

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Toast menuToast = Toast.makeText(MainActivity.this, R.string.phonePermission, Toast.LENGTH_LONG);
            menuToast.show();
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            // return ;
        } else {
            startActivity(callIntent);
        }
    }

    //location

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged");

        /*
            here we will add the notification functions.
            whenever location is changed , the timer will wait for 15 minutes.
            then it will check if the location is saved previously.
            if not  , a notification will be sent
         */
        //getLocation();
        updateUI(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
        getLocation();
    }

    @Override
    public void onProviderDisabled(String s) {
        if (locationManager != null) {
            locationManager.removeUpdates(MainActivity.this);
        }
    }


    public void getLocation() {
        try {
            if (canGetLocation) {
                Log.d(TAG, "Can get location");
                if (isGPS) {
                    // from GPS
                    Log.d(TAG, "GPS on");
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (loc != null)
                            updateUI(loc);
                    }
                } else if (isNetwork) {
                    // from Network Provider
                    Log.d(TAG, "NETWORK_PROVIDER on");
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (loc != null)
                            updateUI(loc);
                    }
                } else {
                    loc.setLatitude(0);
                    loc.setLongitude(0);
                    updateUI(loc);
                }
            } else {
                Log.d(TAG, "Can't get location");
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public String getLattitude() {
        String s = Double.toString(latitude);
        //Text lat = (Text) latitude;
        return s;
    }

    public String getLongitude() {
        String s = Double.toString(latitude);
        //Text lat = (Text) latitude;
        return s;
    }

    private void getLastLocation() {
        try {
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, false);
            Location location = locationManager.getLastKnownLocation(provider);
            Log.d(TAG, provider);
            Log.d(TAG, location == null ? "NO LastLocation" : location.toString());
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private ArrayList findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList result = new ArrayList();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canAskPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canAskPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {


            case ALL_PERMISSIONS_RESULT:
                Log.d(TAG, "onRequestPermissionsResult");
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(
                                                        new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }
                } else {
                    Log.d(TAG, "No rejected permissions.");
                    canGetLocation = true;
                    getLocation();
                }
                break;
        }
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("GPS is not Enabled!");
        alertDialog.setMessage("Do you want to turn on GPS?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void updateUI(Location loc) {
        Log.d(TAG, "updateUI");
        latitude = loc.getLatitude();
        longitude = loc.getLongitude();
        Log.d(TAG, latitude + " " + longitude);
        //tvTime.setText(DateFormat.getTimeInstance().format(loc.getTime()));
        Toast.makeText(MainActivity.this, "location  " + latitude + " " + longitude, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(MainActivity.this);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
    //location


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


                GenericTypeIndicator<ArrayList<Pair<String, String>>> t = new GenericTypeIndicator<ArrayList<Pair<String, String>>>() {
                };
                //currentContacts = dataSnapshot.child("contacts").getValue(t);

                //Retrieve contacts
                long d = dataSnapshot.child("contacts").getChildrenCount();
                String contactName = "";
                String contactPhone = "";
                currentContacts.clear();


                for (Integer i = 0; i < d; i++) {
                    contactName = dataSnapshot.child("contacts").child(i.toString()).child("first").getValue(String.class);
                    contactPhone = dataSnapshot.child("contacts").child(i.toString()).child("second").getValue(String.class);
                    currentContacts.add(new Pair<String, String>(contactName, contactPhone));
                    break;


                }
                emergencyNum = contactPhone;

                //Toast.makeText( MainActivity.this,"Contact emer"+emergencyNum+" "+d+" "+contactName+" "+contactPhone+" "+userEmail+" "+userName+" "+userPhone, Toast.LENGTH_SHORT).show();
                // Toast.makeText( ShowContactsActivity.this,"Contact name "+currentContacts.get(0).toString(), Toast.LENGTH_LONG).show();

                // retrieve locations
                long locationCount = dataSnapshot.child("location").getChildrenCount();
                String locationName = "";
                String latitude = "";
                String longitude = "";

                location.clear();
                if (buffer.length() > 0) {
                    buffer.delete(0, buffer.length() - 1);
                }
                for (Integer i = 0; i < locationCount; i++) {
                    locationName = dataSnapshot.child("location").child(i.toString()).child("first").getValue(String.class);
                    latitude = dataSnapshot.child("location").child(i.toString()).child("second").child("first").getValue(String.class);
                    longitude = dataSnapshot.child("location").child(i.toString()).child("second").child("second").getValue(String.class);
                    location.add(new Pair<String, Pair<String, String>>(locationName, (new Pair<String, String>(latitude, longitude))));
                    buffer.append("NAME : " + locationName + "\n");
                    buffer.append("Latitude : " + latitude + "\n");
                    buffer.append("Longitude : " + longitude + "\n\n");
                }


                retrieveDone = 1;
                //  Toast.makeText( Istant.this,"Contact "+locationCount+" "+locationName+" "+latitude+" "+userEmail+" "+userName+" "+userPhone, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //firebase
}




