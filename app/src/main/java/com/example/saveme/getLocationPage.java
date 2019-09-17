package com.example.saveme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static java.lang.System.exit;

public class getLocationPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_location);

        Button back  =  (Button) findViewById(R.id.getCurrentLocationBack);

        back.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View view)
                                    {


                                        Intent intent = new Intent(getLocationPage.this, menuPage.class);
                                        //intent.putExtra("pak",100);

                                        startActivity(intent);
                                        finish();
                                    }
                                }
        );
    }
}
