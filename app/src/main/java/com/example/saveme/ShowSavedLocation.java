package com.example.saveme;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class ShowSavedLocation extends AppCompatActivity {
    SavedLocation myDb;

    //MainActivity mainActivity;
    EditText edit_Address, edit_Longitude,edit_Lattitude;
    Button addData;
    Button viewAll;
    Button deleteData;


    Button updateData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_savedlocation);
        myDb = new SavedLocation(this);
       // mainActivity = new MainActivity();
        edit_Address =(EditText) findViewById(R.id.address);
        edit_Lattitude = (EditText) findViewById(R.id.lattitude);
        edit_Longitude = (EditText) findViewById(R.id.longitude);
        addData = (Button) findViewById(R.id.AddData);
        viewAll = (Button) findViewById(R.id.ViewData);
        updateData = (Button) findViewById(R.id.updateData);
        deleteData = (Button) findViewById(R.id.deleteData);
        edit_Longitude.setText(this.getIntent().getExtras().getString("key2"));
        edit_Lattitude.setText(this.getIntent().getExtras().getString("key1"));
        AddData();
        viewAll();
        UpdateData();
        DeleteData();
       // mainActivity.getLocation();
        //double lat = (double) mainActivity.lattitude;
        //edit_Address.setText("");

    }

    public void DeleteData(){
        deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer deleteRows = myDb.deleteData(edit_Address.getText().toString());
                if(deleteRows >0)
                    Toast.makeText(ShowSavedLocation.this, " Data deleted",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(ShowSavedLocation.this, " Data is not deleted",Toast.LENGTH_SHORT).show();

                edit_Address.setText("");
                edit_Longitude.setText("");
                edit_Lattitude.setText("");

            }
        });
    }

    public void UpdateData(){
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("fuck");



                boolean isUpdate = myDb.updateData(edit_Address.getText().toString(), edit_Lattitude.getText().toString(), edit_Longitude.getText().toString());

                if(isUpdate == true){
                    Toast.makeText(ShowSavedLocation.this, " Data Updated",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(ShowSavedLocation.this, " Data is not Updated",Toast.LENGTH_SHORT).show();
                }
                //MainActivity mainActivity = new MainActivity();

               // EditText editText = (EditText) mainActivity.longitude;

            }
        });
    }

    public void AddData(){
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInserted = myDb.insertData((edit_Address.getText().toString()), edit_Lattitude.getText().toString(),edit_Longitude.getText().toString());
                if(isInserted == true)
                    Toast.makeText(ShowSavedLocation.this, " Data Inserted",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(ShowSavedLocation.this, " Data is not Inserted",Toast.LENGTH_SHORT).show();


            }
        });
    }

    public void viewAll(){
        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = myDb.getAllData();
                if(res.getCount() == 0) {
                    showMessage("Error","Nothing found");
                    return;
                }

                StringBuffer buffer = new StringBuffer();
                while(res.moveToNext()){
                    buffer.append("ADDRESS : " + res.getString(0)+"\n");
                    buffer.append("LATTITUDE : " + res.getString(1)+"\n");
                    buffer.append("LONGITUDE : " + res.getString(2)+"\n\n");

                }showMessage("Data",buffer.toString());
            }
        });
    }

    public void showMessage(String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }
}
