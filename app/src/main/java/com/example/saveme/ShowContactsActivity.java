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
    private StringBuffer buffer;
    private User currentUser;

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

                saveContacts();
                // DatabaseHelper databaseHelper = new DatabaseHelper(null);
                //String mara = DatabaseHelper.getInstance().getNumber("1");
                //System.out.println(mara);

            }



        });
       viewAll.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               showMessage("Data",buffer.toString());
               // DatabaseHelper databaseHelper = new DatabaseHelper(null);
               //String mara = DatabaseHelper.getInstance().getNumber("1");
               //System.out.println(mara);

           }



       });
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReferenceofContactsShow.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //iterating through all the nodes
               /* for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //Contacts contactsAccess = postSnapshot.getValue(Contacts.class);
                    String name = contactsAccess.getName();
                    //String name = postSnapshot.child("Users/"+appUser.getUid()+"/Contacts/name").getValue(String.class);
                    //String phone = postSnapshot.child("Users/"+appUser.getUid()+"/Contacts/phone").getValue(String.class);



                    buffer.append("NAME : " +name+"\n");
                    buffer.append("NUMBER : " + contactsAccess.phone+"\n\n");



                       // buffer.append("NAME : " +name+"\n");
                        //buffer.append("NUMBER : " + phone+"\n\n");

                   // Log.d("contacts ", "NAME : " +contacts.name+"\n"+"NUMBER : " + contacts.phone+"\n\n");


                }*/
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
                Toast.makeText( ShowContactsActivity.this,"Contact "+userEmail+" "+userName+" "+userPhone, Toast.LENGTH_LONG).show();
               // Toast.makeText( ShowContactsActivity.this,"Contact name "+currentContacts.get(0).toString(), Toast.LENGTH_LONG).show();
                for(int i =0;i<contacts.size();i++) {
                    buffer.append("NAME : " + contacts.get(i).first + "\n");
                    buffer.append("NUMBER : " + contacts.get(i).second + "\n\n");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void saveContacts(){
        String name = edit_Name.getText().toString();
        String phone = edit_Number.getText().toString();
        /*
        String userName = appUser.getUid();
        String id = databaseReferenceofContacts.push().getKey();
        Contacts  contact = new Contacts(id,name,phone);
        //FirebaseUser appUser = firebaseAuth.getCurrentUser();
        //databaseReferenceofContacts.setValue(contact);
        */
        /*
        String userName = currentUser.name;
        String userEmail = currentUser.email;
        String userPhone = currentUser.phone;
*/
        contacts.add(new Pair <String,String> (name, phone));
        User updateUser = new User(userName,userEmail,userPhone);
        updateUser.setContacts(contacts);
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

/*
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
    */

    public void showMessage(String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }


}
