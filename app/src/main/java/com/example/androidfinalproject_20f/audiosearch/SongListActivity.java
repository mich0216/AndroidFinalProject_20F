package com.example.androidfinalproject_20f.audiosearch;

import android.content.Intent;
import android.net.Uri;
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
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * @author Vettivale Ponnampalam
 * CST 2335 -020
 * The main class for the AlbumListActivity
 */
public class SongListActivity extends AppCompatActivity {

    /**
     * the albumListView
     */
    private ListView albumListView;

    /**
     * list of Album
     */
    private ArrayList<Song> list = new ArrayList<>();

    /**
     * the albumAdapter object
     */
    private SongAdaptor myAdapter;
    /**
     * the progressbar object
     */
    private ProgressBar progress;


    private long albumIDFromInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        //This gets the toolbar from the layout:
        Toolbar tBar = (Toolbar) findViewById(R.id.toolbar);

        //This loads the toolbar, which calls onCreateOptionsMenu below:
        setSupportActionBar(tBar);

        albumIDFromInternet = getIntent().getLongExtra("ALBUMIDFROMINTERNET", 0);

        albumListView = findViewById(R.id.albumListView);
        progress = findViewById(R.id.progress);

        myAdapter = new SongAdaptor();
        albumListView.setAdapter(myAdapter);

        AlbumSongsQuery request = new AlbumSongsQuery(); //creates a background thread

        request.execute("https://theaudiodb.com/api/v1/json/1/track.php?m=" + albumIDFromInternet);


        boolean isTablet = findViewById(R.id.fragmentLocation) != null;

        albumListView.setOnItemClickListener((parent, view, position, id) -> {

            // https://stackoverflow.com/a/3004542
            Song s = list.get(position);
            String url = "https://www.google.com/search?q=" + s.getArtistName() + "+" + s.getSongName();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);

        });

    }

    private class AlbumSongsQuery extends AsyncTask<String, Integer, String> {

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

                JSONArray albumJsonArray = jsonObject.getJSONArray("track");

                for (int i = 0; i < albumJsonArray.length(); i++) {

                    JSONObject albumJsonObject = albumJsonArray.getJSONObject(i);

                    Song a = new Song(
                            albumJsonObject.has("strTrack") ? albumJsonObject.getString("strTrack") : "",
                            albumJsonObject.has("strArtist") ? albumJsonObject.getString("strArtist") : ""
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

            Snackbar.make(albumListView, getString(R.string.as_showing_songs_for_selected), Snackbar.LENGTH_LONG).show();


        }
    }

    /**
     * AlbumAdaptor extended from BaseAdaper
     */
    private class SongAdaptor extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Song getItem(int position) {
            return list.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View newView = convertView;

            newView = inflater.inflate(R.layout.album_layout, parent, false);
            TextView tv = newView.findViewById(R.id.albumTitleTextView);
            tv.setText(getItem(position).getSongName());

            return newView;
        }

        @Override
        public long getItemId(int position) {
            return (long) position;
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

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SongListActivity.this);
                alertDialogBuilder.setTitle(R.string.as_instructions)
                        .setMessage(R.string.as_instruction_description)
                        .setPositiveButton(R.string.as_ok, null)
                        .create().show();

                break;
        }
        return true;
    }
}