package com.example.androidfinalproject_20f.audiosearch;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.androidfinalproject_20f.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
/**
 * @author Vettivale Ponnampalam
 * CST 2335 -020
 * The main class for the AlbumListActivity
 * */
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_list);

        String artistName = getIntent().getStringExtra("ARTIST_NAME");

        albumListView = findViewById(R.id.albumListView);
        progress = findViewById(R.id.progress);

        myAdapter = new AlbumAdaptor();
        albumListView.setAdapter(myAdapter);

        albumListView.setOnItemClickListener((parent, view, position, id) -> {

            Album alb = list.get(position);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AlbumListActivity.this);
            alertDialogBuilder.setTitle("Album Details")

                    .setMessage("ID: " + alb.getId() + "\n" + "Album Title: " + alb.getTitle() + "\n" + "Position: " + position)

                    .setPositiveButton("Ok", null)

                    .create().show();

        });

        // Show loading for 2 second
        new Handler().postDelayed(() -> {

            list.add(new Album(1, "Lunatique"));
            list.add(new Album(2, "Le Passage"));
            list.add(new Album(3, "Paradis secret"));
            list.add(new Album(4, "Nouvelle page"));
            myAdapter.notifyDataSetChanged();
            progress.setVisibility(View.INVISIBLE);

            Snackbar.make(albumListView, "Showing the albums for artist: " + artistName, Snackbar.LENGTH_LONG).show();

        }, 2000);

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