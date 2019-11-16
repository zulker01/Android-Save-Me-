package com.example.saveme;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class ShowSavedLocation extends AppCompatActivity {
    //SavedLocation myDb;

    //MainActivity mainActivity;
    EditText edit_Name, edit_Longitude,edit_Lattitude;
    Button addData;
    Button viewAll;
    Button deleteData;


    Button updateData;

    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;

    // define firebase object to save information
    private DatabaseReference datbaseReferenceofLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_savedlocation);
        //myDb = new SavedLocation(this);
       // mainActivity = new MainActivity();
        edit_Name =(EditText) findViewById(R.id.address);
        edit_Lattitude = (EditText) findViewById(R.id.lattitude);
        edit_Longitude = (EditText) findViewById(R.id.longitude);
        addData = (Button) findViewById(R.id.AddData);
        viewAll = (Button) findViewById(R.id.ViewData);
        updateData = (Button) findViewById(R.id.updateData);
        deleteData = (Button) findViewById(R.id.deleteData);
        edit_Longitude.setText(this.getIntent().getExtras().getString("key2"));
        edit_Lattitude.setText(this.getIntent().getExtras().getString("key1"));
        /*AddData();
        viewAll();
        UpdateData();
        DeleteData();
       // mainActivity.getLocation();
        //double lat = (double) mainActivity.lattitude;
        //edit_Address.setText("");
        */

        // initialize firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();

        /*
        if the user is not logged in , prompt to log in
        */
        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(ShowSavedLocation.this,user_login.class));

        }
        FirebaseUser appUser = firebaseAuth.getCurrentUser();
        datbaseReferenceofLocations = FirebaseDatabase.getInstance().getReference("Users/"+appUser.getUid()+"/Locations");

        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveLocations();
                // DatabaseHelper databaseHelper = new DatabaseHelper(null);
                //String mara = DatabaseHelper.getInstance().getNumber("1");
                //System.out.println(mara);

            }



        });

    }


    private void saveLocations(){
        String name = edit_Name.getText().toString();
        String latitude = edit_Lattitude.getText().toString();
        String longitude = edit_Longitude.getText().toString();

        String id = datbaseReferenceofLocations.push().getKey();
        Locations locations = new Locations(name,latitude,longitude);
        FirebaseUser appUser = firebaseAuth.getCurrentUser();
        datbaseReferenceofLocations.child(id).setValue(locations);

        /*
        FirebaseDatabase.getInstance().getReference("Users/"+user.getUid())
                .child("contacts")
                .setValue(contact).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Toast.makeText(ShowContactsActivity.this, "Contact Added ", Toast.LENGTH_LONG).show();
                } else {
                    //display a failure message
                    Toast.makeText( ShowContactsActivity.this,"Contact Add Error", Toast.LENGTH_LONG).show();
                }
            }
        });
*/
        Toast.makeText(ShowSavedLocation.this," Location saved ",Toast.LENGTH_LONG).show();
    }

/*
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

 */
}
