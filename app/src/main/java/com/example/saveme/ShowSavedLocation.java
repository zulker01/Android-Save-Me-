package com.example.saveme;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

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
    FirebaseUser appUser;
    // define firebase object to save information
    private  DatabaseReference databaseReferenceofContacts;
    private  DatabaseReference databaseReferenceLocations;
    private  DatabaseReference databaseReferenceofContactsShow;
    private ArrayList<Pair<String,String>> contacts = new ArrayList <Pair <String,String> > ();
    private ArrayList<Pair<String,String>> currentContacts = new ArrayList <Pair <String,String> > ();
    private ArrayList<Pair<String,Pair<String,String> > > location = new ArrayList<Pair<String,Pair<String,String> > >();
    private StringBuffer buffer;
    private User currentUser;
    private  Integer retrieveDone = 0;

    String userName ;
    String userEmail ;
    String userPhone ;

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
        appUser = firebaseAuth.getCurrentUser();
        databaseReferenceLocations = FirebaseDatabase.getInstance().getReference("Users/"+appUser.getUid());

        buffer = new StringBuffer();
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(retrieveDone==1) {
                    saveLocations();
                }
                else
                {
                    Toast.makeText( ShowSavedLocation.this,"Data Retrieving , Please wait ", Toast.LENGTH_LONG).show();
                }

                // DatabaseHelper databaseHelper = new DatabaseHelper(null);
                //String mara = DatabaseHelper.getInstance().getNumber("1");
                //System.out.println(mara);

            }



        });
        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(retrieveDone==1) {
                    showMessage("Locations",buffer.toString());
                }
                else
                {
                    Toast.makeText( ShowSavedLocation.this,"Data Retrieving , Please wait ", Toast.LENGTH_LONG).show();
                }


                // DatabaseHelper databaseHelper = new DatabaseHelper(null);
                //String mara = DatabaseHelper.getInstance().getNumber("1");
                //System.out.println(mara);

            }



        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReferenceLocations.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // currentUser = dataSnapshot.getValue(User.class);
                userName = dataSnapshot.child("name").getValue().toString();
                userEmail = dataSnapshot.child("email").getValue().toString();
                userPhone = dataSnapshot.child("phone").getValue().toString();

                /*
                GenericTypeIndicator<ArrayList<Item>> t = new GenericTypeIndicator<ArrayList<Item>>() {};
                ArrayList<Item> yourStringArray = snapshot.getValue(t);
                Toast.makeText(getContext(),yourStringArray.get(0).getName(),Toast.LENGTH_LONG).show();

                 */
                GenericTypeIndicator<ArrayList<Pair<String,String>>> t = new GenericTypeIndicator<ArrayList<Pair<String,String>>>() {};
                //currentContacts = dataSnapshot.child("contacts").getValue(t);
/*
                String userName = currentUser.name;
                String userEmail = currentUser.email;
                String userPhone = currentUser.phone;
  */
                //Retrieve contacts
                long d = dataSnapshot.child("contacts").getChildrenCount();
                String contactName="";
                String contactPhone="";
                currentContacts.clear();


                for(Integer i=0;i<d;i++)
                {
                    contactName = dataSnapshot.child("contacts").child(i.toString()).child("first").getValue(String.class);
                    contactPhone = dataSnapshot.child("contacts").child(i.toString()).child("second").getValue(String.class);
                    currentContacts.add(new Pair <String,String> (contactName, contactPhone));

                }
                Toast.makeText( ShowSavedLocation.this,"Contact "+d+" "+contactName+" "+contactPhone+" "+userEmail+" "+userName+" "+userPhone, Toast.LENGTH_LONG).show();
                // Toast.makeText( ShowContactsActivity.this,"Contact name "+currentContacts.get(0).toString(), Toast.LENGTH_LONG).show();

                // retrieve locations
                long locationCount = dataSnapshot.child("location").getChildrenCount();
                String locationName="";
                String latitude="";
                String longitude="";

                location.clear();
                if(buffer.length()>0) {
                    buffer.delete(0, buffer.length() - 1);
                }
                for(Integer i=0;i<locationCount;i++)
                {
                    locationName = dataSnapshot.child("location").child(i.toString()).child("first").getValue(String.class);
                    latitude = dataSnapshot.child("location").child(i.toString()).child("second").child("first").getValue(String.class);
                    longitude = dataSnapshot.child("location").child(i.toString()).child("second").child("second").getValue(String.class);
                    location.add(new Pair<String, Pair<String, String>>(locationName,(new Pair<String, String>(latitude,longitude))));
                    buffer.append("NAME : " + locationName + "\n");
                    buffer.append("Latitude : " + latitude + "\n");
                    buffer.append("Longitude : " + longitude + "\n\n");
                }
                retrieveDone =1;
                Toast.makeText( ShowSavedLocation.this,"Contact "+locationCount+" "+locationName+" "+latitude+" "+userEmail+" "+userName+" "+userPhone, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void saveLocations(){
        String name = edit_Name.getText().toString();
        String latitude = edit_Lattitude.getText().toString();
        String longitude = edit_Longitude.getText().toString();
        edit_Lattitude.setText("");
        edit_Longitude.setText("");
        edit_Name.setText("");
        location.add(new Pair<String, Pair<String, String>>(name,(new Pair<String, String>(latitude,longitude))));
        User updateUser = new User(userName,userEmail,userPhone);
        updateUser.setContacts(currentContacts);
        updateUser.setLocation(location);
        FirebaseDatabase.getInstance().getReference("Users")
                .child(appUser.getUid())
                .setValue(updateUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Toast.makeText(ShowSavedLocation.this, "Location Added ", Toast.LENGTH_LONG).show();
                } else {
                    //display a failure message
                    Toast.makeText( ShowSavedLocation.this,"Contact Add Error", Toast.LENGTH_LONG).show();
                }
            }
        });
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
*/
    public void showMessage(String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }


}
