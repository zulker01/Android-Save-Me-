package com.example.saveme;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Locale;


public class voiceCommand extends AppCompatActivity {
    public  double latitude = 0;
    public   double longitude = 0;
    private TextView txvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voice_command_page);
        txvResult = findViewById(R.id.txvResult);
    }

    public void getSpeechInput(View view) {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txvResult.setText(result.get(0));

                    if(result.get(0).equals("save me"))
                    {
                        callNow();
                        sendLoction();

                    }

                }
                break;
        }
    }

    public void callNow()
    {
        String number="01785373724";
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+number));

        if( ActivityCompat.checkSelfPermission(voiceCommand.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
        {
            Toast menuToast = Toast.makeText(voiceCommand.this, R.string.phonePermission, Toast.LENGTH_LONG);
            menuToast.show();
            return ;
        }
        startActivity(callIntent);
    }

    public void sendLoction()
    {
        String phoneNumber="785373724";
        String smsMessage = "I am in danger, HELP ! \n\n I am at " +"\n Latitude : "+latitude+"\nLongitude :  "+longitude+" ";

        if(checkPermission(Manifest.permission.SEND_SMS)){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, smsMessage, null, null);
            Toast.makeText(voiceCommand.this, "Message Sent!", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(voiceCommand.this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkPermission(String permission){
        int check = ContextCompat.checkSelfPermission(voiceCommand.this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }
}
