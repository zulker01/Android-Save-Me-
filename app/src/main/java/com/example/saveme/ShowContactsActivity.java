package com.example.saveme;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ShowContactsActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    EditText edit_Name, edit_getId;
    EditText edit_Number;
    // EditText getEdit_Number
    Button addData;
    Button viewAll;
    Button deleteData;

    Button updateData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contacts);
        myDb = new DatabaseHelper(this);

        edit_Name =(EditText) findViewById(R.id.editText2);
        edit_Number = (EditText) findViewById(R.id.editText3);
        edit_getId = (EditText) findViewById(R.id.editText);
        addData = (Button) findViewById(R.id.btnAddData);
        viewAll = (Button) findViewById(R.id.btnViewData);

       // addData();
       // viewAll();

        updateData = (Button) findViewById(R.id.btnupdateData);
        deleteData = (Button) findViewById(R.id.btndeleteData);
        AddData();
        viewAll();
        UpdateData();
        DeleteData();

    }


    public void DeleteData(){
        deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer deleteRows = myDb.deleteData(edit_Number.getText().toString());
                if(deleteRows >0)
                    Toast.makeText(ShowContactsActivity.this, " Data deleted",Toast.LENGTH_SHORT).show();
                else

                    Toast.makeText(ShowContactsActivity.this, " Data is not deleted",Toast.LENGTH_SHORT).show();

                edit_getId.setText("");
                edit_Name.setText("");
                edit_Number.setText("");

            }
        });
    }

    public void UpdateData(){
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("fuck");

                boolean isUpdate = myDb.updateData(edit_getId.getText().toString(), edit_Name.getText().toString(), edit_Number.getText().toString());

                if(isUpdate == true){
                    Toast.makeText(ShowContactsActivity.this, " Data Updated",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(ShowContactsActivity.this, " Data is not Updated",Toast.LENGTH_SHORT).show();

                }
                edit_getId.setText("");
                edit_Name.setText("");
                edit_Number.setText("");
            }
        });
    }

    public void AddData(){
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInserted = myDb.insertData(edit_Name.getText().toString(), edit_Number.getText().toString());
                if(isInserted == true)
                    Toast.makeText(ShowContactsActivity.this, " Data Inserted",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(ShowContactsActivity.this, " Data is not Inserted",Toast.LENGTH_SHORT).show();

                edit_getId.setText("");
                edit_Name.setText("");
                edit_Number.setText("");

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
                    buffer.append("ID : " + res.getString(0)+"\n");
                    buffer.append("NAME : " + res.getString(1)+"\n");
                    buffer.append("NUMBER : " + res.getString(2)+"\n\n");

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
