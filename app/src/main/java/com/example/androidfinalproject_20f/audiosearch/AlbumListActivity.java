package com.example.androidfinalproject_20f.audiosearch;

import android.content.Intent;
import android.os.AsyncTask;
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
import com.example.androidfinalproject_20f.chrish.MainActivity;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * @author Vettivale Ponnampalam
 * CST 2335 -020
 * The main class for the AlbumListActivity
 */
public class AlbumListActivity extends AppCompatActivity {

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
    private String artistName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_list);

        //This gets the toolbar from the layout:
        Toolbar tBar = (Toolbar) findViewById(R.id.toolbar);

        //This loads the toolbar, which calls onCreateOptionsMenu below:
        setSupportActionBar(tBar);

        artistName = getIntent().getStringExtra("ARTIST_NAME");

        albumListView = findViewById(R.id.albumListView);
        progress = findViewById(R.id.progress);

        myAdapter = new AlbumAdaptor();
        albumListView.setAdapter(myAdapter);

        albumListView.setOnItemLongClickListener((parent, view, position, id) -> {

            Album alb = list.get(position);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AlbumListActivity.this);
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

        ArtistAlbumQuery request = new ArtistAlbumQuery(); //creates a background thread

        try {
            request.execute("https://www.theaudiodb.com/api/v1/json/1/searchalbum.php?s=" + URLEncoder.encode(artistName, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

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
                Intent nextActivity = new Intent(AlbumListActivity.this, EmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivity(nextActivity); //make the transition
            }

        });

    }

    private class ArtistAlbumQuery extends AsyncTask<String, Integer, String> {

        protected String doInBackground(String... args) {
            try {

                // process JSON for UV code
                URL UVURL = new URL(args[0]);
                //open the connection
                HttpURLConnection uvUrlConnection = (HttpURLConnection) UVURL.openConnection();
                //wait for data:
                InputStream uvResponse = uvUrlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(uvResponse, "UTF-8"), 8);

                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString();

                JSONObject jsonObject = new JSONObject(result);

                JSONArray albumJsonArray = jsonObject.getJSONArray("album");

                for (int i = 0; i < albumJsonArray.length(); i++) {

                    JSONObject albumJsonObject = albumJsonArray.getJSONObject(i);

                    Album a = new Album(
                            albumJsonObject.has("idAlbum") ? albumJsonObject.getLong("idAlbum") : 0,
                            albumJsonObject.has("strAlbum") ? albumJsonObject.getString("strAlbum") : "",
                            albumJsonObject.has("intYearReleased") ? albumJsonObject.getString("intYearReleased") : "",
                            albumJsonObject.has("strDescriptionEN") ? albumJsonObject.getString("strDescriptionEN") : "",
                            albumJsonObject.has("strGenre") ? albumJsonObject.getString("strGenre") : "",
                            albumJsonObject.has("intSales") ? albumJsonObject.getString("intSales") : ""
                    );
                    list.add(a);

                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return "Done";
        }

        public void onProgressUpdate(Integer... args) {
            progress.setProgress(args[0]);
        }

        public void onPostExecute(String fromDoInBackground) {
            myAdapter.notifyDataSetChanged();
            progress.setVisibility(View.INVISIBLE);

            Snackbar.make(albumListView, getString(R.string.as_snackbar_message) + artistName, Snackbar.LENGTH_LONG).show();


        }
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

            case R.id.audiohome:
                Intent mainPage = new Intent(this, MainActivity.class);
                startActivity(mainPage);
                break;
                //what to do when the menu item is selected:
            case R.id.instructionsMenuItem:

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AlbumListActivity.this);
                alertDialogBuilder.setTitle(R.string.as_instructions)
                        .setMessage(R.string.as_instruction_description)
                        .setPositiveButton(R.string.as_ok, null)
                        .create().show();

                break;
        }
        return true;
    }
}