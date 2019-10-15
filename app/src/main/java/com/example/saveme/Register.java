package com.example.saveme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.*;
import android.util.Log;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button submit1=findViewById(R.id.submit1)    ;


        submit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText edt = (EditText)findViewById(R.id.editumber1);
                final String number1 = edt.getText().toString();
                Toast.makeText(Register.this, "Added Number: "+number1, Toast.LENGTH_LONG).show();
                saveFile("number1.txt",number1);

            }
        });




    }
    public void saveFile(String file, String text)
    {
        try{
            FileOutputStream fos=openFileOutput(file,Context.MODE_PRIVATE);
            fos.write(text.getBytes());
            fos.close();
            Toast.makeText(this,"Saved!",Toast.LENGTH_SHORT).show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Toast.makeText(this,"Error saving file",Toast.LENGTH_SHORT).show();
        }
    }


}
