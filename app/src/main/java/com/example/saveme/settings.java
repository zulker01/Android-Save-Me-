package com.example.saveme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class settings extends AppCompatActivity {

    public  Boolean switchState;
    Button submit           ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        submit = submit = (Button) findViewById(R.id.submitButton);

        /// initiate a Switch
        final Switch simpleSwitch = (Switch) findViewById(R.id.simpleSwitch);

        // check current state of a Switch (true or false).
        switchState = simpleSwitch.isChecked();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchState = simpleSwitch.isChecked();
                if(switchState)
                {
                    MainActivity.allowCheckSwitch = true;
                    simpleSwitch.setChecked(true);
                }
                else
                {
                    MainActivity.allowCheckSwitch = false;
                    simpleSwitch.setChecked(false);
                }

            }
        });
    }



}
