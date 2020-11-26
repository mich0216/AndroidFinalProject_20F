package com.example.androidfinalproject_20f.audiosearch;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

        artistName = getIntent().getStringExtra("ARTIST_NAME");

        albumListView = findViewById(R.id.albumListView);
        progress = findViewById(R.id.progress);

        myAdapter = new AlbumAdaptor();
        albumListView.setAdapter(myAdapter);

        albumListView.setOnItemClickListener((parent, view, position, id) -> {

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

        });

        ArtistAlbumQuery request = new ArtistAlbumQuery(); //creates a background thread
        request.execute("https://www.theaudiodb.com/api/v1/json/1/searchalbum.php?s=" + artistName);

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
}