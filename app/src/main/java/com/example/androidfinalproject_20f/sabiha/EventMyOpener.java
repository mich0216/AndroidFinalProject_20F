package com.example.androidfinalproject_20f.sabiha;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EventMyOpener extends SQLiteOpenHelper {

    public final static String TABLE_NAME = "EVENTS";
    public final static String COL_NAME = "NAME";
    public final static String COL_STARTDATE = "STARTDATE";
    public final static String COL_MINPRICE = "MINPRICE";
    public final static String COL_MAXPRICE = "MAXPRICE";
    public final static String COL_TICKETMASTERURL = "TICKETMASTERURL";
    public final static String COL_IMAGEURL = "IMAGEURL";
    public final static String COL_ID = "_id";
    protected final static String DATABASE_NAME = "EventsDB";
    protected final static int VERSION_NUM = 1;

    public EventMyOpener(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }


    //This function gets called if no database file exists.
    //Look on your device in the /data/data/package-name/database directory.
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NAME + " text,"
                + COL_STARTDATE + " text,"
                + COL_MINPRICE + " REAL,"
                + COL_MAXPRICE + " REAL,"
                + COL_TICKETMASTERURL + " text,"
                + COL_IMAGEURL + " text);");  // add or remove columns
    }


    //this function gets called if the database version on your device is lower than VERSION_NUM
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {   //Drop the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }

    //this function gets called if the database version on your device is higher than VERSION_NUM
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {   //Drop the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }
}
