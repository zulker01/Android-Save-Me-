
        package com.example.saveme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
                                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                        callIntent.setData(Uri.parse("tel:"+number));
                                        startActivity(callIntent);

                                    }
                                }
        );


       // String pak =  getLocationPage.cityName;
        message.setOnClickListener(new View.OnClickListener()
                                   {
                                       @Override
                                       public void onClick(View view)
                                       {
                                           String number="01785373724";
                                           String pak =  getLocationPage.cityName;
                                           Intent smsIntent = new Intent(Intent.ACTION_SENDTO,
                                                   Uri.parse("sms:"+number));
                                           smsIntent.putExtra("sms_body", "Help Me, I am in Danger"+pak);
                                           startActivity(smsIntent);

                                       }
                                   }
        );
/*


*/
    }
}