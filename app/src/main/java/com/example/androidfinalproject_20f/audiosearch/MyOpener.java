package com.example.androidfinalproject_20f.audiosearch;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpener extends SQLiteOpenHelper {

    protected final static String DATABASE_NAME = "AudioSearchDB";
    protected final static int VERSION_NUM = 1;
    public final static String TABLE_NAME = "Albums";
    public final static String COL_ID = "_id";
    public final static String COL_ALBUMIDFROMINTERNET = "ALBUMIDFROMINTERNET";
    public final static String COL_TITLE = "TITLE";
    public final static String COL_YEAR = "YEAR";
    public final static String COL_DESCRIPTION = "DESCRIPTION";
    public final static String COL_GENRE = "GENRE";
    public final static String COL_SALE = "SALE";

    public MyOpener(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }


    //This function gets called if no database file exists.
    //Look on your device in the /data/data/package-name/database directory.
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_ALBUMIDFROMINTERNET + " INTEGER,"
                + COL_TITLE + " text,"
                + COL_YEAR + " text,"
                + COL_DESCRIPTION + " text,"
                + COL_GENRE + " text,"
                + COL_SALE + " text);");  // add or remove columns
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
