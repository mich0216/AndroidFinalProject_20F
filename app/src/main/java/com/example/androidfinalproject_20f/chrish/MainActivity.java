package com.example.androidfinalproject_20f.chrish;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;


import com.example.androidfinalproject_20f.R;
import com.example.androidfinalproject_20f.ahmed.Recipe;
import com.example.androidfinalproject_20f.audiosearch.Album;
import com.example.androidfinalproject_20f.sabiha.Event;
import com.example.androidfinalproject_20f.sabiha.EventSearchActivity;

import com.example.androidfinalproject_20f.audiosearch.ArtistInputActivity;

import com.example.androidfinalproject_20f.ahmed.RecipeSearchActivity;
import com.google.android.material.navigation.NavigationView;

/**
 * @author Ahmed Elakad, Chrishanthi Michael, Sabiha Rahman, Vettival Ponnampalam
 * CST 2335-020
 * MainActivty is the starting class
 */



public class MainActivity extends AppCompatActivity {


    @Override
    //program starts here
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView loads objects onto the screen.
        setContentView(R.layout.activity_main);

        // this gets the toolbar from the layout
        Toolbar tBar = (Toolbar) findViewById(R.id.mainToolbar);
        //This loads the toolbar, which calls onCreateOptionMev
        setSupportActionBar(tBar);

        // for Navigation Drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar, R.string.covidOpen, R.string.covidClose);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener (item -> {

            switch (item.getItemId()) {
                //what to do when the menu item is selected:
                case R.id.eventSearch:
                    // when the menu item is selected go to event search page
                    Intent event = new Intent(this, EventSearchActivity.class);
                    startActivity(event);
                    break;
                // when the menu item is selected go to recipe search page
                case R.id.receipeSearch:
                    Intent recipe = new Intent(this, RecipeSearchActivity.class);
                    startActivity(recipe);
                    break;
                // when the menu item is selected go to covid case search page
                case R.id.covidSearch:
                    Intent covidSearch = new Intent(this, WelcomePageCovid.class);
                    startActivity(covidSearch);
                    break;
                // when the menu item is selected go to album search page
                case R.id.alblumSearch:
                    Intent album = new Intent(this, ArtistInputActivity.class);
                    startActivity(album);
                    break;

            }

            DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        Button button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, EventSearchActivity.class);
            startActivity(i);
        });



        Button audioSearchButton = findViewById(R.id.button4);
        audioSearchButton.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, ArtistInputActivity.class);
            startActivity(i);
        });
        // button to go to covid data page
        Button cButton = findViewById(R.id.covidButton);

        // creating a transition to load WelcomePageCovid from main activity page
        Intent goToWelcomePage = new Intent(this, WelcomePageCovid.class);

        //when you click the button, it will start the next activity
        cButton.setOnClickListener(click ->
        {
            startActivity(goToWelcomePage);
        });

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, RecipeSearchActivity.class);

            startActivity(i);
        });

    }
    // Inflate the menu items for use in the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.maintoolandnag, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Look at your menu XML file. Put a case for every id in that file:
        switch (item.getItemId()) {
            // when the menu item is selected go to event search page
            case R.id.eventSearch:
                Intent event = new Intent(this, EventSearchActivity.class);
                startActivity(event);
                break;
            // when the menu item is selected go to recipe search page
            case R.id.receipeSearch:
                Intent recipe = new Intent(this, RecipeSearchActivity.class);
                startActivity(recipe);
                break;
            // when the menu item is selected go to covid case search page
            case R.id.covidSearch:
                Intent covidSearch = new Intent(this, WelcomePageCovid.class);
                startActivity(covidSearch);
                break;
            // when the menu item is selected go to album search page
            case R.id.alblumSearch:
                Intent album = new Intent(this, ArtistInputActivity.class);
                startActivity(album);
                break;

        }
        return true;
    }



}