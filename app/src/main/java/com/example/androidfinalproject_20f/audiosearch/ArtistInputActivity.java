package com.example.androidfinalproject_20f.audiosearch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.androidfinalproject_20f.R;
import com.google.android.material.navigation.NavigationView;

/**
 * @author Vettivale Ponnampalam
 * CST 2335 -020
 * The main class for the ArtistInputActivity extended from AppCompatActivity
 */
public class ArtistInputActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * the albumNameEditText object
     */
    private EditText albumNameEditText;

    /**
     * the searchButton object
     */
    private Button searchButton;

    private Button showDatabaseAlbums;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_input);

        //This gets the toolbar from the layout:
        Toolbar tBar = (Toolbar) findViewById(R.id.toolbar);

        //This loads the toolbar, which calls onCreateOptionsMenu below:
        setSupportActionBar(tBar);

        albumNameEditText = findViewById(R.id.albumNameEditText);
        searchButton = findViewById(R.id.searchButton);
        showDatabaseAlbums = findViewById(R.id.showDatabaseAlbums);

        sp = getSharedPreferences("AudioSearchSharedPreference", Context.MODE_PRIVATE);

        albumNameEditText.setText(sp.getString("ARTIST_NAME", ""));

        searchButton.setOnClickListener(v -> {
            String artistName = albumNameEditText.getText().toString();

            if (artistName.isEmpty()) {

                Toast.makeText(this, getString(R.string.as_please_enter_album_name), Toast.LENGTH_SHORT).show();

            } else {

                SharedPreferences.Editor e = sp.edit();
                e.putString("ARTIST_NAME", artistName);
                e.commit();

                Intent i = new Intent(ArtistInputActivity.this, AlbumListActivity.class);
                i.putExtra("ARTIST_NAME", artistName);
                startActivity(i);

            }
        });

        showDatabaseAlbums.setOnClickListener(view -> {
            Intent i = new Intent(ArtistInputActivity.this, SavedAlbumListActivity.class);
            startActivity(i);
        });

        //For NavigationDrawer:
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar, R.string.as_open, R.string.as_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ArtistInputActivity.this);
                alertDialogBuilder.setTitle(R.string.as_instructions)
                        .setMessage(R.string.as_instruction_description)
                        .setPositiveButton(R.string.as_ok, null)
                        .create().show();

                break;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //what to do when the menu item is selected:
            case R.id.instructionsMenuItem:

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ArtistInputActivity.this);
                alertDialogBuilder.setTitle(R.string.as_instructions)
                        .setMessage(R.string.as_instruction_description)
                        .setPositiveButton(R.string.as_ok, null)
                        .create().show();

                break;
        }
        return true;
    }
}