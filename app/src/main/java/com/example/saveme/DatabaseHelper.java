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
    private static final String Database_Name="ContactsDatabase.db";
    private static final String Table_Name="Contacts";
    private static final String col_1="ID";
    private static final String col_2="NAME";
    private static final String col_3="NUMBER";

    public DatabaseHelper(@Nullable Context context) {
        super(context, Database_Name, null, version);
        SQLiteDatabase db = this.getWritableDatabase();
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + Table_Name +"(Contact_ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, NUMBER TEXT )");
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
     db.execSQL("DROP TABLE IF EXISTS " + Table_Name);
     onCreate(db);
    }

    public boolean insertData (String name, String number){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_2, name);
        contentValues.put(col_3, number);
        long result = db.insert(Table_Name, null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res  = db.rawQuery("select * from " + Table_Name,null);
        return res;
    }

}
