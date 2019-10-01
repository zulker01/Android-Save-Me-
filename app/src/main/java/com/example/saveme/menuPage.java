package com.example.saveme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static java.lang.System.exit;

public class menuPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_page);

        Button addnum = (Button) findViewById(R.id.addNumber);
        Button back = (Button) findViewById(R.id.menuBack);
        Button menuGetLocation = (Button) findViewById(R.id.menuGetLocation);
        Button menuVoiceCommand = (Button) findViewById(R.id.menuVoiceCommand);

        addnum.setOnClickListener(new View.OnClickListener()
                                      {
                                          @Override
                                          public void onClick(View view)
                                          {

                                              Intent intent = new Intent(menuPage.this, AddNumber.class);
                                              //intent.putExtra("pak",100);

                                              startActivity(intent);
                                              finish();
                                          }
                                      }
        );
        back.setOnClickListener(new View.OnClickListener()
                                      {
                                          @Override
                                          public void onClick(View view)
                                          {


                                              Intent intent = new Intent(menuPage.this, MainActivity.class);
                                              //intent.putExtra("pak",100);

                                              startActivity(intent);
                                              finish();
                                          }
                                      }
        );
        menuGetLocation.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View view)
                                    {

                                        Toast menuToast = Toast.makeText(menuPage.this,"pak pak", Toast.LENGTH_LONG);
                                        menuToast.show();

                                        Intent intent = new Intent(menuPage.this, getLocationPage.class);
                                        //intent.putExtra("pak",100);

                                        startActivity(intent);
                                        finish();
                                    }
                                }
        );
        menuVoiceCommand.setOnClickListener(new View.OnClickListener()
                                           {
                                               @Override
                                               public void onClick(View view)
                                               {

                                                   Toast menuToast = Toast.makeText(menuPage.this,"pak pak", Toast.LENGTH_LONG);
                                                   menuToast.show();

                                                   Intent intent = new Intent(menuPage.this, voiceCommand.class);
                                                   //intent.putExtra("pak",100);

                                                   startActivity(intent);
                                                   finish();
                                               }
                                           }
        );
    }
}