package com.example.saveme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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


        menuButton.setOnClickListener(new View.OnClickListener()
                                      {
                                          @Override
                                          public void onClick(View view)
                                          {
                                              Toast menuToast = Toast.makeText(MainActivity.this,"pak pak", Toast.LENGTH_LONG);
                                              menuToast.show();

                                              Intent intent = new Intent(MainActivity.this, menuPage.class);
                                              //intent.putExtra("pak",100);

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
                                              Toast quitToast = Toast.makeText(MainActivity.this,"mara kha", Toast.LENGTH_LONG);
                                              quitToast.show();
                                              exit(0);
                                          }
                                      }
        );
    }
}
