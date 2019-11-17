package com.example.saveme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import java.util.ArrayList;

public class ShowContactsActivity extends AppCompatActivity {
    //DatabaseHelper myDb;
    EditText edit_Name, edit_getId;
    EditText edit_Number;
    // EditText getEdit_Number
    Button addData;
    Button viewAll;
    Button deleteData;
    Button updateData;

    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;
    FirebaseUser appUser;
    // define firebase object to save information
    private  DatabaseReference databaseReferenceofContacts;
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
        setContentView(R.layout.activity_show_contacts);
        //myDb = new DatabaseHelper(this);

        edit_Name =(EditText) findViewById(R.id.editText2);
        edit_Number = (EditText) findViewById(R.id.editText3);
        edit_getId = (EditText) findViewById(R.id.editText);
        addData = (Button) findViewById(R.id.btnAddData);
        viewAll = (Button) findViewById(R.id.btnViewData);

       // addData();
       // viewAll();
        // initialize firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();

        /*
        if the user is not logged in , prompt to log in
        */
        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(ShowContactsActivity.this,user_login.class));

        }
        appUser = firebaseAuth.getCurrentUser();
        databaseReferenceofContacts = FirebaseDatabase.getInstance().getReference("Users").child(appUser.getUid());
        databaseReferenceofContactsShow = FirebaseDatabase.getInstance().getReference("Users").child(appUser.getUid());

        buffer = new StringBuffer();
        updateData = (Button) findViewById(R.id.btnupdateData);
        deleteData = (Button) findViewById(R.id.btndeleteData);
       /* AddData();
        viewAll();
        UpdateData();
        DeleteData();


        */

       addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(retrieveDone==1) {
                    saveContacts();
                }
                else
                {
                    Toast.makeText( ShowContactsActivity.this,"Data Retrieving , Please wait ", Toast.LENGTH_LONG).show();
                }
            }



        });
       viewAll.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               if(retrieveDone==1) {
                   showMessage("Contacts",buffer.toString());
               }
               else
               {
                   Toast.makeText( ShowContactsActivity.this,"Data Retrieving , Please wait ", Toast.LENGTH_LONG).show();
               }


           }



       });
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReferenceofContactsShow.addValueEventListener(new ValueEventListener() {
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
                if(buffer.length()>0) {
                    buffer.delete(0, buffer.length() - 1);
                }
                for(Integer i=0;i<d;i++)
                {
                   contactName = dataSnapshot.child("contacts").child(i.toString()).child("first").getValue(String.class);
                    contactPhone = dataSnapshot.child("contacts").child(i.toString()).child("second").getValue(String.class);
                    currentContacts.add(new Pair <String,String> (contactName, contactPhone));
                    buffer.append("NAME : " + contactName + "\n");
                    buffer.append("NUMBER : " + contactPhone + "\n\n");
                }
                Toast.makeText( ShowContactsActivity.this,"Contact "+d+" "+contactName+" "+contactPhone+" "+userEmail+" "+userName+" "+userPhone, Toast.LENGTH_LONG).show();
               // Toast.makeText( ShowContactsActivity.this,"Contact name "+currentContacts.get(0).toString(), Toast.LENGTH_LONG).show();

                // retrieve locations
                long locationCount = dataSnapshot.child("location").getChildrenCount();
                String locationName="";
                String latitude="";
                String longitude="";

                location.clear();

                for(Integer i=0;i<locationCount;i++)
                {
                    locationName = dataSnapshot.child("location").child(i.toString()).child("first").getValue(String.class);
                    latitude = dataSnapshot.child("location").child(i.toString()).child("second").child("first").getValue(String.class);
                    longitude = dataSnapshot.child("location").child(i.toString()).child("second").child("second").getValue(String.class);
                    location.add(new Pair<String, Pair<String, String>>(locationName,(new Pair<String, String>(latitude,longitude))));

                }
                retrieveDone =1;
                Toast.makeText( ShowContactsActivity.this,"Contact "+locationCount+" "+locationName+" "+latitude+" "+userEmail+" "+userName+" "+userPhone, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void saveContacts(){
        String name = edit_Name.getText().toString();
        String phone = edit_Number.getText().toString();

        currentContacts.add(new Pair <String,String> (name, phone));
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
                    Toast.makeText(ShowContactsActivity.this, "Contact Added ", Toast.LENGTH_LONG).show();
                } else {
                    //display a failure message
                    Toast.makeText( ShowContactsActivity.this,"Contact Add Error", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Toast.makeText(ShowContactsActivity.this," ",Toast.LENGTH_LONG).show();
    }



    public void showMessage(String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }


}
