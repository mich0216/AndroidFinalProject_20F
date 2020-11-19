package com.example.androidfinalproject_20f;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * @author Chrishanthi Michael
 * CST 2335-20
 * The class CovidDataOpener extened from SQLiteOpenHelper
 * */
public class CovidDataOpener extends SQLiteOpenHelper {

    protected final static String DATABASE_NAME = "CovidDB";
    protected final static int VERSION_NUM = 1;
    public final static String TABLE_NAME = "COVIDDATA";
    public final static String COL_COUNTRY = "COUNTRY";
    public final static String COL_PROVINCE = "PROVINCE";
    public final static String COL_CASES = "CASES" ;
    public final static String COL_DATE = "DATE" ;
    public final static String COL_ID = "_id";

    public CovidDataOpener(Context ctx)
    {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }


    //This function gets called if no database file exists.
    //Look on your device in the /data/data/package-name/database directory.
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_COUNTRY + " text,"
                + COL_PROVINCE + " text,"
                + COL_CASES + " text,"
                + COL_DATE + " text);");  // add or remove columns
    }


    //this function gets called if the database version on your device is lower than VERSION_NUM
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {   //Drop the old table:
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }

    //this function gets called if the database version on your device is higher than VERSION_NUM
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {   //Drop the old table:
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }
}
