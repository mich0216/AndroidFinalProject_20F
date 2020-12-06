package com.example.androidfinalproject_20f.ahmed;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class RecipeDataOpener extends SQLiteOpenHelper {

    public final static String DATABASE_NAME = "RecipeDB";
    public final static int VERSION_NUM = 1;
    public final static String TABLE_NAME = "recipeData";
    public final static String COL_ID = "_id";
    public final static String COL_TITLE = "title";
    public final static String COL_RECIPE_URL = "recipeUrl" ;
    public final static String COL_INGREDIENTS = "ingredients" ;
    public final static String COL_IMAGE_URL = "iamgeurl" ;


    public RecipeDataOpener(Context ctx) {
        super(ctx,DATABASE_NAME,null,VERSION_NUM);
    }


    //This function gets called if no database file exists.
    //Look on device in the /data/data/package-name/database directory.
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME +
                " ("+COL_ID+ " Integer PRIMARY KEY AUTOINCREMENT, "
                + COL_TITLE + " VARCHAR(255),"
                + COL_RECIPE_URL + " VARCHAR(255),"
                + COL_IMAGE_URL + " VARCHAR(255),"
                + COL_INGREDIENTS + " VARCHAR(255));");  // add or remove columns
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Drop the old table:
        sqLiteDatabase.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(sqLiteDatabase);
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
