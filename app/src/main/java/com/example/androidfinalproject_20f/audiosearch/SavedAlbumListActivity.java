package com.example.androidfinalproject_20f.audiosearch;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.androidfinalproject_20f.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

/**
 * @author Vettivale Ponnampalam
 * CST 2335 -020
 * The main class for the AlbumListActivity
 */
public class SavedAlbumListActivity extends AppCompatActivity {

    /**
     * the albumListView
     */
    private ListView albumListView;

    /**
     * list of Album
     */
    private ArrayList<Album> list = new ArrayList<>();

    /**
     * the albumAdapter object
     */
    private AlbumAdaptor myAdapter;
    /**
     * the progressbar object
     */
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_list);

        //This gets the toolbar from the layout:
        Toolbar tBar = (Toolbar) findViewById(R.id.toolbar);

        //This loads the toolbar, which calls onCreateOptionsMenu below:
        setSupportActionBar(tBar);

        albumListView = findViewById(R.id.albumListView);
        progress = findViewById(R.id.progress);
        progress.setVisibility(View.INVISIBLE);

        myAdapter = new AlbumAdaptor();
        albumListView.setAdapter(myAdapter);

        albumListView.setOnItemLongClickListener((parent, view, position, id) -> {

            Album alb = list.get(position);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SavedAlbumListActivity.this);
            alertDialogBuilder.setTitle(R.string.as_album_details)

                    .setMessage(
                            getString(R.string.as_id) + alb.getId() +
                                    "\n" + +position +
                                    "\n" + getString(R.string.as_title) + alb.getTitle() +
                                    "\n" + getString(R.string.as_year) + alb.getYear() +
                                    "\n" + getString(R.string.as_description) + alb.getDescription() +
                                    "\n" + getString(R.string.as_genre) + alb.getGenre() +
                                    "\n" + getString(R.string.as_sale) + alb.getSale()
                    )

                    .setPositiveButton("Ok", null)

                    .create().show();

            return true;

        });

        // load albums from database


        boolean isTablet = findViewById(R.id.fragmentLocation) != null;

        albumListView.setOnItemClickListener((parent, view, position, id) -> {

            Album alb = list.get(position);

            //Create a bundle to pass data to the new fragment
            Bundle dataToPass = new Bundle();
            dataToPass.putLong("ID", alb.getId());
            dataToPass.putLong("ALBUMIDFROMINTERNET", alb.getAlbumIDFromInternet());
            dataToPass.putString("TITLE", alb.getTitle());
            dataToPass.putString("YEAR", alb.getYear());
            dataToPass.putString("DESCRIPTION", alb.getDescription());
            dataToPass.putString("GENRE", alb.getGenre());
            dataToPass.putString("SALE", alb.getSale());


            if (isTablet) {
                DetailsFragment dFragment = new DetailsFragment(); //add a DetailFragment
                dFragment.setArguments(dataToPass);//pass it a bundle for information
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
                        .commit(); //actually load the fragment. Calls onCreate() in DetailFragment
            } else {
                Intent nextActivity = new Intent(SavedAlbumListActivity.this, EmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivity(nextActivity); //make the transition
            }

        });

    }

    private void loadDataFromDatabase() {
        //get a database connection:
        MyOpener dbOpener = new MyOpener(this);
        SQLiteDatabase db = dbOpener.getWritableDatabase(); //This calls onCreate() if you've never built the table before, or onUpgrade if the version here is newer


        // We want to get all of the columns. Look at MyOpener.java for the definitions:
        String[] columns = {MyOpener.COL_ID, MyOpener.COL_TITLE, MyOpener.COL_ALBUMIDFROMINTERNET, MyOpener.COL_YEAR, MyOpener.COL_DESCRIPTION, MyOpener.COL_GENRE, MyOpener.COL_SALE};
        //query all the results from the database:
        Cursor results = db.query(false, MyOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        //Now the results object has rows of results that match the query.
        //find the column indices:

        int idColIndex = results.getColumnIndex(MyOpener.COL_ID);
        int albumIDFromInternetIndex = results.getColumnIndex(MyOpener.COL_ALBUMIDFROMINTERNET);
        int titleIndex = results.getColumnIndex(MyOpener.COL_TITLE);
        int yearIndex = results.getColumnIndex(MyOpener.COL_YEAR);
        int descriptionIndex = results.getColumnIndex(MyOpener.COL_DESCRIPTION);
        int genreIndex = results.getColumnIndex(MyOpener.COL_GENRE);
        int saleIndex = results.getColumnIndex(MyOpener.COL_SALE);
        //iterate over the results, return true if there is a next item:
        list.clear();
        while (results.moveToNext()) {
            long id = results.getLong(idColIndex);
            long albumIDFromInternet = results.getLong(albumIDFromInternetIndex);
            String title = results.getString(titleIndex);
            String year = results.getString(yearIndex);
            String description = results.getString(descriptionIndex);
            String genre = results.getString(genreIndex);
            String sale = results.getString(saleIndex);

            Album alb = new Album(id, albumIDFromInternet, title, year, description, genre, sale);

            list.add(alb);
        }

        if (list.isEmpty()) {
            Snackbar.make(albumListView, getString(R.string.no_albums_in_db), Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDataFromDatabase();
        myAdapter.notifyDataSetChanged();
    }

    /**
     * AlbumAdaptor extended from BaseAdaper
     */
    private class AlbumAdaptor extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Album getItem(int position) {
            return list.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View newView = convertView;

            newView = inflater.inflate(R.layout.album_layout, parent, false);
            TextView tv = newView.findViewById(R.id.albumTitleTextView);
            tv.setText(getItem(position).getTitle());

            return newView;
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getId();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //what to do when the menu item is selected:
            case R.id.instructionsMenuItem:

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SavedAlbumListActivity.this);
                alertDialogBuilder.setTitle(R.string.instructions)
                        .setMessage(R.string.instruction_description)
                        .setPositiveButton(R.string.ok, null)
                        .create().show();

                break;
        }
        return true;
    }
}