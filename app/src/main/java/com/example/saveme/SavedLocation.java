package com.example.saveme;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SavedLocation extends SQLiteOpenHelper {
    private static final int version=1;
    private static final String Database_Name="savedlocation.db";
    private static final String Table_Name="location";
    private static final String col_1="ADDRESS";
    private static final String col_2="LATTITUDE";
    private static final String col_3="LONGITUDE";

    public SavedLocation(@Nullable Context context) {
        super(context, Database_Name, null, version);
        SQLiteDatabase db = this.getWritableDatabase();
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + Table_Name +"(ADDRESS TEXT PRIMARY KEY, LATTITUDE TEXT, LONGITUDE TEXT )");
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Table_Name);
        onCreate(db);
    }

    public boolean insertData (String address, String lattitude, String longitude){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_1, address);
        contentValues.put(col_2, lattitude);
        contentValues.put(col_3, longitude);
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

    public boolean updateData(String address, String lattitude, String longitude){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        System.out.println("fuck");
        contentValues.put(col_1, address);
        contentValues.put(col_2, lattitude);
        contentValues.put(col_3, longitude);
        db.update(Table_Name,contentValues,"ADDRESS =?",new String[]{address});
        return true;
    }

    public Integer deleteData(String address){
        SQLiteDatabase db = this.getWritableDatabase() ;
        return db.delete(Table_Name,"ADDRESS = ?",new String[]{address});
    }


    public String findaddress(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res  = db.rawQuery("select ADDRESS from " + Table_Name,null);
        String add = res.getString(0);
        return add;
    }
    public String testLattitude(String address) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("select DISTINCT LATTITUDE from" + Table_Name + "WHERE group by ADDRESS = ?", new String[]{address});
        String mara;

        String textView = res.getString(1);
        return textView;
    }
    public String testLongitude(String address) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select DISTINCT LONGITUDE from" + Table_Name + "WHERE GROUP BY ADDRESS = ?", new String[]{address});
        String mara;

        String textView = res.getString(2);
        return textView;
    }

}
