package com.example.androidfinalproject_20f.audiosearch;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.androidfinalproject_20f.R;
import com.google.android.material.snackbar.Snackbar;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {

    private Bundle dataFromActivity;
    private long id, albumIDFromInternet;
    private String title, year, description, genre, sale;
    private AppCompatActivity parentActivity;
    private SQLiteDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();
        id = dataFromActivity.getLong("ID");
        albumIDFromInternet = dataFromActivity.getLong("ALBUMIDFROMINTERNET");
        title = dataFromActivity.getString("TITLE");
        year = dataFromActivity.getString("YEAR");
        description = dataFromActivity.getString("DESCRIPTION");
        genre = dataFromActivity.getString("GENRE");
        sale = dataFromActivity.getString("SALE");

        // Inflate the layout for this fragment

        View result = inflater.inflate(R.layout.activity_details_fragment, container, false);

        //show the message
        TextView textViewTitle = (TextView) result.findViewById(R.id.textViewTitle);
        TextView textViewYear = (TextView) result.findViewById(R.id.textViewYear);
        TextView textViewDescription = (TextView) result.findViewById(R.id.textViewDescription);
        TextView textViewGenre = (TextView) result.findViewById(R.id.textViewGenre);
        TextView textViewSale = (TextView) result.findViewById(R.id.textViewSale);

        textViewTitle.setText(title);
        textViewYear.setText(year);
        textViewDescription.setText(description);
        textViewGenre.setText(genre);
        textViewSale.setText(sale);

        // get the Hide button, and add a click listener:
        Button hideButton = (Button) result.findViewById(R.id.hideButton);
        hideButton.setOnClickListener(Clk -> {
            //Tell the parent activity to remove
            parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
        });

        Button buttonGetSongsName = (Button) result.findViewById(R.id.buttonGetSongsName);
        buttonGetSongsName.setOnClickListener(Clk -> {

            Intent i = new Intent(parentActivity, SongListActivity.class);
            i.putExtra("ALBUMIDFROMINTERNET", albumIDFromInternet);
            parentActivity.startActivity(i);

        });

        //get a database connection:
        MyOpener dbOpener = new MyOpener(parentActivity);
        db = dbOpener.getWritableDatabase();

        Button databaseButton = (Button) result.findViewById(R.id.databaseButton);
        databaseButton.setOnClickListener(Clk -> {

            if (id != 0) {
                // remove album from database
                db.delete(MyOpener.TABLE_NAME, MyOpener.COL_ID + "= ?", new String[]{Long.toString(id)});
                id = 0;
                Snackbar.make(databaseButton, getResources().getString(R.string.as_remove_msg), Snackbar.LENGTH_SHORT).show();
            } else {
                //store the album in the database
                ContentValues newRowValue = new ContentValues();

                //Now provide a value for every database column defined in MyOpener.java:
                //put string name in the NAME column:
                newRowValue.put(MyOpener.COL_ALBUMIDFROMINTERNET, albumIDFromInternet);
                newRowValue.put(MyOpener.COL_TITLE, title);
                newRowValue.put(MyOpener.COL_YEAR, year);
                newRowValue.put(MyOpener.COL_DESCRIPTION, description);
                newRowValue.put(MyOpener.COL_GENRE, genre);
                newRowValue.put(MyOpener.COL_SALE, sale);

                //Now insert in the database:
                long newId = db.insert(MyOpener.TABLE_NAME, null, newRowValue);
                id = newId;
                Snackbar.make(databaseButton, getResources().getString(R.string.as_save_to_db_msg), Snackbar.LENGTH_SHORT).show();
            }

        });

        return result;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //context will either be FragmentExample for a tablet, or EmptyActivity for phone
        parentActivity = (AppCompatActivity) context;
    }
}