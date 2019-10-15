package com.example.saveme;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int version=1;
    private static final String databaseName="ContactsDatabase.sqlite";
    public static String APP_DATA_PATH="";
    public static final String DB_SUB_PATH="/databases/" + databaseName;
    private SQLiteDatabase dataBase;
    private static final String tableName1="Contacts";
    private static final String contactID="Contact_ID";
    private static final String contactName="Contact_Name";
    private static final String contactNumber="Contact_Number";
    private static final String createContactsTable="create table "+tableName1+"("+contactID+" INTEGER primary key autoincrement, "+contactName+" varchar(100), "+contactNumber+" INTEGER);";
    private static final String dropTable1="drop table if exists "+tableName1;
    Context context;
    public DatabaseHelper(@Nullable Context context) {
        super(context, databaseName, null, version);
        APP_DATA_PATH=context.getApplicationInfo().dataDir;
        this.context=context;
    }
    public boolean openDataBase() throws SQLException {
        String mPath = APP_DATA_PATH + DB_SUB_PATH;
        //Note that this method assumes that the db file is already copied in place
        dataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.OPEN_READWRITE);
        return dataBase != null;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        try{

            db.execSQL(createContactsTable);
            Toast.makeText(context, "Tables are created", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public synchronized void close(){
        if(dataBase != null) {dataBase.close();}
        super.close();
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try{
            db.execSQL(dropTable1);
            Toast.makeText(context, "OnUpgrade method is called", Toast.LENGTH_SHORT).show();
            onCreate(db);
        } catch (Exception e) {
            Toast.makeText(context, "upFailed", Toast.LENGTH_SHORT).show();
        }
    }
    public Cursor displayAllContacts(){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor cursor= sqLiteDatabase.rawQuery("select * from "+tableName1 ,null);
        return cursor;
    }

    public void addContacts(String Name, int Number){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Name, Number);
        sqLiteDatabase.insert("Contacts", null, values);
    }
}
