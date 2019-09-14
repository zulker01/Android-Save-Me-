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

        Button backButton = (Button) findViewById(R.id.menuBack);



        backButton.setOnClickListener(new View.OnClickListener()
                                      {
                                          @Override
                                          public void onClick(View view)
                                          {
                                              Toast menuToast = Toast.makeText(menuPage.this,"pak pak menupage", Toast.LENGTH_LONG);
                                              menuToast.show();

                                              Intent intent = new Intent(menuPage.this, MainActivity.class);
                                              //intent.putExtra("pak",100);

                                              startActivity(intent);
                                          }
                                      }
        );
    }
}